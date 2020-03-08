package ru.bartex.smetaelectro.ui.smetatabs;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ru.bartex.smetaelectro.ListOfSmetasStructured;
import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.SmetasMat;
import ru.bartex.smetaelectro.SmetasMatCost;
import ru.bartex.smetaelectro.SmetasWork;
import ru.bartex.smetaelectro.SmetasWorkCost;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.SmetaOpenHelper;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.files.FileWork;
import ru.bartex.smetaelectro.ui.main.MainActivity;
import ru.bartex.smetaelectro.ui.smetabefore.ListOfSmetasNames;

public class Smetas extends AppCompatActivity {

    public static final String TAG = "33333";
    long file_id;
    int pos;

    private SQLiteDatabase database;
    private ViewPager viewPager;
    private WorkMatPagerAdapter adapter;
    private Fragment workFrag, matFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smetas);

        initDB();
        initBottomNavigation();

        Intent intent = getIntent();
        file_id = intent.getExtras().getLong(P.ID_FILE);
        //Получаем имя файла с текущей  сметой
        String fileName = FileWork.getNameFromId(database, file_id);
        Log.d(TAG, "Smetas - onCreate  fileName = " + fileName);

        initToolbar();
        initFab();

        //создаём фрагменты
        workFrag = SmetasTabWork.newInstance(file_id, 0);
        matFrag = SmetasTabMat.newInstance(file_id, 1);

        //здесь используется вариант  добавления фрагментов из активити
        adapter = new WorkMatPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(workFrag, "Работа" );
        adapter.addFragment(matFrag, "Материалы" );

        viewPager = findViewById(R.id.container_smetas_work_mat);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setTabTextColors(Color.WHITE, Color.GREEN);
        tabs.setupWithViewPager(viewPager);
    }

    private void initDB() {
        //
        database = new SmetaOpenHelper(this).getWritableDatabase();
    }

    private void initBottomNavigation() {
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //показываем имя текущей сметы в заголовке экрана
        toolbar.setTitle(R.string.smetas_name_on_bar);
        toolbar.setTitleTextColor(Color.GREEN);
    }

    private void initFab() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentItem = viewPager.getCurrentItem();
                switch (currentItem){
                    case 0:
                        Intent intent = new Intent(Smetas.this, SmetasWork.class);
                        intent.putExtra(P.ID_FILE, file_id);
                        startActivity(intent);
                        break;

                    case 1:
                        Intent intent2 = new Intent(Smetas.this, SmetasMat.class);
                        intent2.putExtra(P.ID_FILE, file_id);
                        Log.d(TAG, "(((((Smetas - onCreate ))))))   file_id = " + file_id);
                        startActivity(intent2);
                        break;
                }

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "//  Smetas onSaveInstanceState // " );
        outState.putLong("file_id", file_id);
        outState.putInt("pos", pos);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        file_id = savedInstanceState.getLong("file_id");
        pos = savedInstanceState.getInt("pos");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_smetas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_struct) {
            int currentItem = viewPager.getCurrentItem();
            Log.d(TAG, "Smetas - onOptionsItemSelected    currentItem = " + currentItem);
            Intent intent = new Intent(Smetas.this, ListOfSmetasStructured.class);
            intent.putExtra(P.TAB_POSITION, currentItem);
            intent.putExtra(P.ID_FILE, file_id);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //создаём контекстное меню для списка
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu_smetas_of_work_mat, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        handleMenuItemClick(item);
        return super.onContextItemSelected(item);
    }

    //обработка для контекстного меню
        private void handleMenuItemClick(MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                case R.id.menu_delete_smetas_item: {
                    int currentTab = viewPager.getCurrentItem();
                    if (currentTab == 0){
                        ((SmetasTabWork) workFrag).getAdapter().removeElement();
                    }else if(currentTab == 1){
                        ((SmetasTabMat) matFrag).getAdapter().removeElement();
                    }
                    break;
                }
                case R.id.menu_cancel_smetas_item: {
                    break;
                }
            }
    }


//
//    private boolean handleMenuItemClick(MenuItem item) {
//        final AdapterView.AdapterContextMenuInfo acmi =
//                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//
//        //если удалить из контекстного меню
//        if (item.getItemId() == P.DELETE_ITEM_SMETA) {
//
//            Log.d(TAG, "Smetas P.DELETE_ITEM_SMETA");
//            AlertDialog.Builder builder = new AlertDialog.Builder(Smetas.this);
//            builder.setTitle(R.string.Delete_Item);
//            builder.setPositiveButton(R.string.DeleteNo, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                }
//            });
//            builder.setNegativeButton(R.string.DeleteYes, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Log.d(TAG, "Smetas P.DELETE_ITEM_SMETA acmi.position  =" +
//                            (acmi.position));
//
//                    TextView tv = acmi.targetView.findViewById(R.id.base_text);
//                    String name = tv.getText().toString();
//                    switch (viewPager.getCurrentItem()){
//                        //switch (positionItem){
//                        case 0:
//                            Log.d(TAG, "Smetas P.DELETE_ITEM_SMETA case 0");
//                            //находим id по имени работы
//                            long work_id = Work.getIdFromName(database, name);
//                            Log.d(TAG, "Smetas onContextItemSelected file_id = " +
//                                    file_id + " work_id =" + work_id+ " work_name =" + name);
//
//                            //удаляем пункт сметы из таблицы FW
//                            FW.deleteItemFrom_FW(database, file_id, work_id);
//                            //обновляем данные списка фрагмента активности
//                            updateAdapter(0);
//                            break;
//
//                        case 1:
//                            Log.d(TAG, "Smetas P.DELETE_ITEM_SMETA case 1");
//                            //находим id по имени работы
//                            long mat_id = Mat.getIdFromName(database, name);
//                            Log.d(TAG, "Smetas onContextItemSelected file_id = " +
//                                    file_id + " mat_id =" + mat_id + " mat_name =" + name);
//
//                            //mSmetaOpenHelper.displayFM();
//                            //удаляем пункт сметы из таблицы FM
//                            FM.deleteItemFrom_FM(database, file_id, mat_id);
//                            //mSmetaOpenHelper.displayFM();
//
//                            updateAdapter(1);
//                            break;
//                    }
//                }
//            });
//            builder.show();
//            return true;
//            //если изменить из контекстного меню
//        } else if (item.getItemId() == P.CANCEL) {
//            //getActivity().finish();
//            return true;
//        }
//        return false;
//    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // Для данного варианта в манифесте указан режим singlTask для активности MainActivity
                    Intent intentHome = new Intent(Smetas.this, MainActivity.class);
                    // установка флагов- создаём новую задачу и убиваем старую вместе
                    // со всеми предыдущими переходами - работает дёргано по сравнению с манифестом
                    //intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    //      Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentHome);
                    return true;
                case R.id.navigation_smetas:
                    // Для данного варианта в манифесте указан режим singlTask для активности ListOfSmetasNames
                    Intent intent_smetas = new Intent(Smetas.this, ListOfSmetasNames.class);
                    startActivity(intent_smetas);
                    return true;
                case R.id.navigation_costs:
                    int curItem = viewPager.getCurrentItem();
                    switch (curItem){
                        case 0:
                            Intent intent_costs_work = new Intent(
                                    Smetas.this, SmetasWorkCost.class);
                            intent_costs_work.putExtra(P.ID_FILE, file_id);
                            startActivity(intent_costs_work);
                            return true;

                        case 1:
                            Intent intent_costs_mat = new Intent(Smetas.this, SmetasMatCost.class);
                            intent_costs_mat.putExtra(P.ID_FILE, file_id);
                            startActivity(intent_costs_mat);
                            return true;
                    }
            }
            return false;
        }
    };

//    //можно так обновлять адаптер, если бы контекстное меню было сдесь
//    private void updateAdapter(int currentItem){
////        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
////        mViewPager.setAdapter(mSectionsPagerAdapter);
////        mViewPager.setCurrentItem(currentItem);
////        mSectionsPagerAdapter.notifyDataSetChanged();
//    }

}
