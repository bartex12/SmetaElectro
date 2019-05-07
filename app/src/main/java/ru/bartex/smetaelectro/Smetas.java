package ru.bartex.smetaelectro;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;

public class Smetas extends AppCompatActivity {

    public static final String TAG = "33333";

    long file_id;
    int pos;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    public ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smetas);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Intent intent = getIntent();
        file_id = intent.getExtras().getLong(P.ID_FILE);

        Log.d(TAG, "(((((Smetas - onCreate ))))))   file_id = " + file_id);

        //Получаем имя файла с текущей  сметой
        SmetaOpenHelper mDbHelper = new SmetaOpenHelper(this);
        String fileName = mDbHelper.getFileNameById(file_id);

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
            Intent intent = new Intent(Smetas.this, SmetaListStructured.class);
            intent.putExtra(P.TAB_POSITION, currentItem);
            intent.putExtra(P.ID_FILE, file_id);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
            switch (position){
                case 0:
                    pos = position;
                    SmetasTab1Rabota tab1Rabota = SmetasTab1Rabota.newInstance(file_id, pos);
                    return tab1Rabota;
                case 1:
                    pos = position;
                    SmetasTab2Materialy tab2Materialy = SmetasTab2Materialy.newInstance(file_id, pos);
                    return tab2Materialy;
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

                    // Для данного варианта в манифесте

            }
            return false;
        }
    };
}
