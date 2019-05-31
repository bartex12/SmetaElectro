package ru.bartex.smetaelectro;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.FileWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.Mat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.TableControllerSmeta;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.Work;

public class Smetas extends AppCompatActivity {

    public static final String TAG = "33333";

    long file_id;
    int pos;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    public ViewPager mViewPager;
    SmetaOpenHelper mSmetaOpenHelper;
    TableControllerSmeta tableControllerSmeta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smetas);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Intent intent = getIntent();
        file_id = intent.getExtras().getLong(P.ID_FILE);

        mSmetaOpenHelper = new SmetaOpenHelper(this);
        tableControllerSmeta  = new TableControllerSmeta(this);

        Log.d(TAG, "(((((Smetas - onCreate ))))))   file_id = " + file_id);

        //Получаем имя файла с текущей  сметой
        String fileName = tableControllerSmeta.getNameFromId(file_id, FileWork.TABLE_NAME);
        Log.d(TAG, "Smetas - onCreate  fileName = " + fileName);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //показываем имя текущей сметы в заголовке экрана
        toolbar.setTitle(R.string.smetas_name_on_bar);
        toolbar.setTitleTextColor(Color.GREEN);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsWork);
        tabLayout.setTabTextColors(Color.WHITE, Color.GREEN);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentItem = mViewPager.getCurrentItem();
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
            int currentItem = mViewPager.getCurrentItem();
            Log.d(TAG, "Smetas - onOptionsItemSelected    currentItem = " + currentItem);
            Intent intent = new Intent(Smetas.this, ListOfSmetasStructured.class);
            intent.putExtra(P.TAB_POSITION, currentItem);
            intent.putExtra(P.ID_FILE, file_id);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //создаём контекстное меню для списка  -  сделано в SmetasFrag
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, P.DELETE_ITEM_SMETA, 0, "Удалить пункт");
        menu.add(0, P.CANCEL, 0, "Отмена");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        final AdapterView.AdapterContextMenuInfo acmi =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        //если удалить из контекстного меню
        if (item.getItemId() == P.DELETE_ITEM_SMETA) {

            Log.d(TAG, "Smetas P.DELETE_ITEM_SMETA");
            AlertDialog.Builder builder = new AlertDialog.Builder(Smetas.this);
            builder.setTitle(R.string.Delete_Item);
            builder.setPositiveButton(R.string.DeleteNo, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setNegativeButton(R.string.DeleteYes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG, "Smetas P.DELETE_ITEM_SMETA acmi.position  =" +
                            (acmi.position));

                    TextView tv = acmi.targetView.findViewById(R.id.base_text);
                    String name = tv.getText().toString();
                    switch (mViewPager.getCurrentItem()){
                        //switch (positionItem){
                        case 0:
                            Log.d(TAG, "Smetas P.DELETE_ITEM_SMETA case 0");
                            //находим id по имени работы
                            long work_id = tableControllerSmeta.getIdFromName(name, Work.TABLE_NAME);
                            Log.d(TAG, "Smetas onContextItemSelected file_id = " +
                                    file_id + " work_id =" + work_id+ " work_name =" + name);

                            //удаляем пункт сметы из таблицы FW
                            mSmetaOpenHelper.deleteWorkItemFromFW(file_id, work_id);
                            //обновляем данные списка фрагмента активности
                            updateAdapter(0);
                            break;

                        case 1:
                            Log.d(TAG, "Smetas P.DELETE_ITEM_SMETA case 1");
                            //находим id по имени работы
                            long mat_id = tableControllerSmeta.getIdFromName(name, Mat.TABLE_NAME);
                            Log.d(TAG, "Smetas onContextItemSelected file_id = " +
                                    file_id + " mat_id =" + mat_id + " mat_name =" + name);

                            //mSmetaOpenHelper.displayFM();
                            //удаляем пункт сметы из таблицы FM
                            mSmetaOpenHelper.deleteMatItemFromFM(file_id, mat_id);
                            //mSmetaOpenHelper.displayFM();

                            updateAdapter(1);
                            break;
                    }
                }
            });
            builder.show();
            return true;
            //если изменить из контекстного меню
        } else if (item.getItemId() == P.CANCEL) {
            //getActivity().finish();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            pos = position;

            switch (position){
                case 0:
                    //использован шаблон проектирования Стратегия
                    //SmetasFrag_Tab1Rab extend SmetasFrag, а во SmetasFrag переменная
                    // интерфейса BehaviorWorkOrMat выполняет метод performUpdateAdapter()
                    //с методом интерфейса updateAdapter внутри)
                    // причём в SmetasFrag_Tab1Rab эта переменная указывает на класс, который будет выполнять
                    // метод updateAdapter - это класс BehaviorWorkOrMat_Work
                SmetasFrag smetasTab0 = SmetasFrag_Tab1Rab.newInstance(file_id, pos);
                return smetasTab0;
                case 1:
                    //использован шаблон проектирования Стратегия
                    //SmetasFrag_Tab2Mat extend SmetasFrag, а во SmetasFrag переменная
                    // интерфейса BehaviorWorkOrMat выполняет метод performUpdateAdapter()
                    //с методом интерфейса updateAdapter внутри
                    // причём в SmetasFrag_Tab2Mat эта переменная указывает на класс, который будет выполнять
                    // метод updateAdapter - это класс BehaviorWorkOrMat_Mat
                    SmetasFrag smetasTab1 = SmetasFrag_Tab2Mat.newInstance(file_id, pos);
                    return smetasTab1;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }

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
                    int curItem = mViewPager.getCurrentItem();
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

    //можно так обновлять адаптер, если бы контекстное меню было сдесь
    private void updateAdapter(int currentItem){
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());;
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(currentItem);
        mSectionsPagerAdapter.notifyDataSetChanged();
    }

}
