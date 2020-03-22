package ru.bartex.smetaelectro.ui.smetas2tabs;

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

import ru.bartex.smetaelectro.ListOfSmetasStructured;
import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetamat.SmetasMat;
import ru.bartex.smetaelectro.ui.smetas3tabs.costmat.SmetasMatCost;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetawork.SmetasWork;
import ru.bartex.smetaelectro.ui.smetas3tabs.costwork.SmetasWorkCost;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.SmetaOpenHelper;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.files.FileWork;
import ru.bartex.smetaelectro.ui.main.MainActivity;
import ru.bartex.smetaelectro.ui.smetabefore.ListOfSmetasNames;

public class SmetasTab extends AppCompatActivity {

    public static final String TAG = "33333";
    private long file_id;
    private int currentTabItem;

    private SQLiteDatabase database;
    public ViewPager viewPager;
    private SmetasTabPagerAdapter adapter;
    private Fragment workFrag, matFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smetas_tab);

        initDB();

        Intent intent = getIntent();
        file_id = intent.getExtras().getLong(P.ID_FILE);
        //Получаем имя файла с текущей  сметой
        String fileName = FileWork.getNameFromId(database, file_id);
        Log.d(TAG, "SmetasTab - onCreate  fileName = " + fileName);

        initBottomNavigation();
        initToolbar();
        initFab();
        // фрагменты инициализируются здесь, чтобы не менять их при возврате из смет
        createTabFrags();
        // адаптер, ViewPager инициализируются в onResume,
        // чтобы при возврате на SmetasTab происходило обновление пунктов списка
    }

    @Override
    protected void onResume() {
        super.onResume();


        initPageAdapter();
        initViewPager();
        Log.d(TAG, "// SmetasTab - onResume //currentTabItem = " + viewPager.getCurrentItem());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "// SmetasTab onStop //");
        //сохраняем currentTabItem - если не было дестрой, onRestoreInstanceState не вызывается
        currentTabItem = viewPager.getCurrentItem();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        database.close();
    }

    private void createTabFrags() {
        //создаём фрагменты
        workFrag = SmetasTabWork.newInstance(file_id, 0);
        matFrag = SmetasTabMat.newInstance(file_id, 1);
    }

    private void initPageAdapter() {
        //здесь используется вариант  добавления фрагментов из активити
        adapter = new SmetasTabPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(workFrag, "Работа" );
        adapter.addFragment(matFrag, "Материалы" );
    }

    private void initViewPager() {
        viewPager = findViewById(R.id.container_smetas_tab);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentTabItem);

        TabLayout tabs = findViewById(R.id.tabs_smetas_tab);
        tabs.setTabTextColors(Color.WHITE, Color.GREEN);
        tabs.setupWithViewPager(viewPager);
    }

    private void initDB() {
        //
        database = new SmetaOpenHelper(this).getWritableDatabase();
    }

    private void initBottomNavigation() {
        BottomNavigationView navigation = findViewById(R.id.navigation_smetas_tab);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_smetas_tab);
        setSupportActionBar(toolbar);
        //показываем имя текущей сметы в заголовке экрана
        toolbar.setTitle(R.string.smetas_name_on_bar);
        toolbar.setTitleTextColor(Color.GREEN);
    }

    private void initFab() {
        FloatingActionButton fab = findViewById(R.id.fab_smetas_tab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentItem = viewPager.getCurrentItem();
                switch (currentItem){
                    case 0:
                        Intent intent = new Intent(SmetasTab.this, SmetasWork.class);
                        intent.putExtra(P.ID_FILE, file_id);
                        startActivity(intent);
                        break;

                    case 1:
                        Intent intent2 = new Intent(SmetasTab.this, SmetasMat.class);
                        intent2.putExtra(P.ID_FILE, file_id);
                        Log.d(TAG, "(((((SmetasTab - onCreate ))))))   file_id = " + file_id);
                        startActivity(intent2);
                        break;
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("file_id", file_id);
        outState.putInt("pos", viewPager.getCurrentItem());
        Log.d(TAG, "//  SmetasTab onSaveInstanceState // pos =  " + viewPager.getCurrentItem());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        file_id = savedInstanceState.getLong("file_id");
        currentTabItem = savedInstanceState.getInt("pos");
        Log.d(TAG, "//  SmetasTab onRestoreInstanceState // pos =  " + currentTabItem);
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
            Log.d(TAG, "SmetasTab - onOptionsItemSelected    currentItem = " + currentItem);
            Intent intent = new Intent(SmetasTab.this, ListOfSmetasStructured.class);
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
        //return super.onContextItemSelected(item);
        return true;
    }

    //обработка для контекстного меню
    private void handleMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_delete_smetas_item: {
                switch (viewPager.getCurrentItem()){
                    case 0:
                        //передаём позицию вкладки
                        adapter.remove(0);
                        viewPager.setCurrentItem(0);
                        adapter.notifyDataSetChanged();
                        break;

                    case 1:
                        //передаём позицию вкладки
                        adapter.remove(1);
                        viewPager.setCurrentItem(1);
                        adapter.notifyDataSetChanged();
                        break;
                }
                Log.d(TAG, "ХХ SmetasTab handleMenuItemClick pos = " +
                        viewPager.getCurrentItem());
                break;
            }
            case R.id.menu_cancel_smetas_item: {
                break;
            }
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // Для данного варианта в манифесте указан режим singlTask для активности MainActivity
                    Intent intentHome = new Intent(SmetasTab.this, MainActivity.class);
                    // установка флагов- создаём новую задачу и убиваем старую вместе
                    // со всеми предыдущими переходами - работает дёргано по сравнению с манифестом
                    //intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    //      Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentHome);
                    return true;
                case R.id.navigation_smetas:
                    // Для данного варианта в манифесте указан режим singlTask для активности ListOfSmetasNames
                    Intent intent_smetas = new Intent(SmetasTab.this, ListOfSmetasNames.class);
                    startActivity(intent_smetas);
                    return true;
                case R.id.navigation_costs:
                    int curItem = viewPager.getCurrentItem();
                    switch (curItem){
                        case 0:
                            Intent intent_costs_work = new Intent(
                                    SmetasTab.this, SmetasWorkCost.class);
                            intent_costs_work.putExtra(P.ID_FILE, file_id);
                            startActivity(intent_costs_work);
                            return true;

                        case 1:
                            Intent intent_costs_mat = new Intent(SmetasTab.this, SmetasMatCost.class);
                            intent_costs_mat.putExtra(P.ID_FILE, file_id);
                            startActivity(intent_costs_mat);
                            return true;
                    }
            }
            return false;
        }
    };

    private void updatePageAdapter(int currentItem){
        //adapter =new  SmetasTabPagerAdapter(getSupportFragmentManager());
       // viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentItem);
        adapter.notifyDataSetChanged();
    }
}
