package ru.bartex.smetaelectro;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;

public class SmetasMatCost extends AppCompatActivity implements
        SmetasMatTab0Cat.OnClickCatCostMatListener, SmetasMatTab1Type.OnClickTypeMatListener,
        DialogSaveCost.OnCatTypeMatCostNameListener{

    public static final String TAG = "33333";
    long file_id;
    boolean isSelectedType = false;
    boolean isSelectedCatCost = false;
    long type_id;
    long cat_id;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    SmetaOpenHelper mSmetaOpenHelper;

    @Override
    public void catAndClickTransmit(long cat_cost_mat_id, boolean isSelectedCatCost) {

        Log.d(TAG, "//  SmetasMatCost  catAndClickTransmit  // " );
        this.isSelectedCatCost = isSelectedCatCost;
        this.cat_id = cat_cost_mat_id;
        Log.d(TAG, "SmetasMatCost  catAndClickTransmit cat_id =" +
                cat_id + "  isSelectedCatCost = " + isSelectedCatCost);

        updateAdapter(1);
    }

    @Override
    public void typeAndClickTransmit(long type_mat_id, boolean isSelectedTypeMat) {
        Log.d(TAG, "//  SmetasMatCost  typeAndClickTransmit  // " );
        this.isSelectedType = isSelectedTypeMat;
        this.type_id = type_mat_id;
        Log.d(TAG, "SmetasMatCost  typeAndClickTransmit type_id =" +
                type_id + "  isSelectedType = " + isSelectedType);

        updateAdapter(2);
    }


    @Override
    public void catTypeMatCostNameTransmit(
            String catName, String typeName, String matName, String costOfMat, String unit) {
        int position = mViewPager.getCurrentItem();
        Log.d(TAG, " ++++++++SmetasMatCost  catTypeMatCostNameTransmit ++++++");
        switch (position){
            case 0:
                Log.d(TAG, "++++++++ SmetasMatCost  catTypeMatCostNameTransmit ++++++ case 0");

                long newCatMatNameId = mSmetaOpenHelper.insertCatMatName(catName);
                Log.d(TAG, "catTypeMatCostNameTransmit - catName = " + catName +
                        " typeName=" + typeName + " matName=" + matName +  " newCatMatNameId=" + newCatMatNameId);

                // обновляем адаптер
                updateAdapter(0);
                break;

            case 1:
                Log.d(TAG, "++++++++ SmetasMatCost  catTypeMatCostNameTransmit ++++++ case 1");

                //определяем id категории (будет type_category_Id в таблице типов) по её имени (блин, это же cat_id )
                long type_category_Id = mSmetaOpenHelper.getCatIdFromCategoryMatName(catName);

                long newTypeMatNameId = mSmetaOpenHelper.insertTypeMatName(typeName, type_category_Id);
                Log.d(TAG, "catTypeMatCostNameTransmit - catName = " + catName +
                        " typeName=" + typeName + " matName=" + matName +
                        " newTypeMatNameId=" + newTypeMatNameId);
                // обновляем адаптер
                updateAdapter(1);
                break;

            case 2:
                Log.d(TAG, "++++++++ SmetasMatCost  catTypeMatCostNameTransmit ++++++ case 2");

                float cost = Float.parseFloat(costOfMat);

                long unit_mat_id = mSmetaOpenHelper.getIdFromUnitMatName(unit);
                Log.d(TAG, "SmetasMatCost  unit_mat_id = " +
                        unit_mat_id + " cost = " + cost + " unit = " + unit
                        + " matName = " + matName  + " type_id = " + type_id) ;

                //Вставляем новый материал в таблице Mat
                long matID = mSmetaOpenHelper.insertMatName(matName, type_id);
                //обновляем цену материала с единицами измерения
                mSmetaOpenHelper.insertCostMat(matID, cost, unit_mat_id);

                // обновляем адаптер
                updateAdapter(2);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smetas_mat_cost);
        Log.d(TAG, "//  SmetasMatCost onCreate // " );

        file_id = getIntent().getLongExtra(P.ID_FILE,-1);
        Log.d(TAG, "SmetasMatCost onCreate file_id =" + file_id);

        mSmetaOpenHelper = new SmetaOpenHelper(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_smetas_mat);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //navigation.setSelectedItemId(R.id.navigation_smetas_smetas_mat_cost);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMat);
        setSupportActionBar(toolbar);
        //показываем заголовокмв заголовке экрана
        toolbar.setTitle(R.string.title_activity_SmetasMatCost);
        toolbar.setTitleTextColor(Color.GREEN);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //средняя вкладка открыта
        mViewPager.setCurrentItem(1);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsMatCost);
        tabLayout.setTabTextColors(Color.WHITE, Color.GREEN);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                invalidateOptionsMenu();
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cost_mat, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d(TAG, " ))))))))SmetasMatCost  onPrepareOptionsMenu(((((((( ///");
        ActionBar acBar = getSupportActionBar();
        int position = mViewPager.getCurrentItem();
        Log.d(TAG, " ))))))))SmetasMatCost  onPrepareOptionsMenu(((((((( position = " + position +
                 "  isSelectedType = " + isSelectedType);
        switch (position){
            case 0:
                Log.d(TAG, " ))))))))SmetasMatCost  onPrepareOptionsMenu case 0");
                menu.findItem(R.id.action_add).setVisible(true);
                break;
            case 1:
                Log.d(TAG, " ))))))))SmetasMatCost  onPrepareOptionsMenu case 1");
                menu.findItem(R.id.action_add).setVisible(isSelectedCatCost);
                break;
            case 2:
                Log.d(TAG, " ))))))))SmetasMatCost  onPrepareOptionsMenu case 2");
                menu.findItem(R.id.action_add).setVisible(isSelectedType);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.action_add){
            int position = mViewPager.getCurrentItem();
            switch (position){
                case 0:
                    Log.d(TAG, " ))))))))SmetasMatCost  onOptionsItemSelected case 0");
                    DialogFragment saveCostCat = DialogSaveCost.NewInstance(
                            true, false, -1, -1);
                    saveCostCat.show(getSupportFragmentManager(),"saveCostCat");
                    break;

                case 1:
                    Log.d(TAG, " ))))))))SmetasMatCost  onOptionsItemSelected case 1");
                    DialogFragment saveCostType = DialogSaveCost.NewInstance(
                            false, true, cat_id, -1);
                    saveCostType.show(getSupportFragmentManager(),"saveCostType");
                    break;

                case 2:
                    Log.d(TAG, " ))))))))SmetasMatCost  onOptionsItemSelected case 2");
                    DialogFragment saveCostMat = DialogSaveCost.NewInstance(
                            false, false, cat_id, type_id);
                    saveCostMat.show(getSupportFragmentManager(),"saveCostMat");
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }



    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "####### SmetasMatCost  Fragment getItem: ####### " );
            switch (position){
                case 0:
                    Log.d(TAG, "####### SmetasMatCost  Fragment getItem case 0: ####### " );
                    SmetasMatTab0Cat  smetasMatTab0Cat = SmetasMatTab0Cat.
                            NewInstance(file_id, position);
                    return smetasMatTab0Cat;
                case 1:
                    Log.d(TAG, "####### SmetasMatCost  Fragment getItem case 1: ####### " );
                    SmetasMatTab1Type  smetasMatTab1Type = SmetasMatTab1Type.
                            NewInstance(file_id, position,isSelectedCatCost, cat_id);
                    return smetasMatTab1Type;

                case 2:
                    Log.d(TAG, "####### SmetasMatCost  Fragment getItem case 2/1: #######" );
                    //передаём во фрагмент данные (и способ их обработки) в зависимости от isSelectedType
                    SmetasMatTab2Mat smetasMatTab2Mat = SmetasMatTab2Mat.
                            NewInstance(file_id, position, isSelectedType, type_id);
                    Log.d(TAG, " ####### SmetasMatCost  Fragment getItem case 2/2: isSelectedType = ####### " +
                            isSelectedType + "  type_id = " +  type_id + "  file_id = " +  file_id +
                            "  position = " +  position);
                    return smetasMatTab2Mat;
                default:
                    return null;
            }
    }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home_smetas_mat_cost:
                    // Для данного варианта в манифесте указан режим singlTask для активности MainActivity
                    Intent intentHome = new Intent(SmetasMatCost.this, MainActivity.class);
                    // установка флагов- создаём новую задачу и убиваем старую вместе
                    // со всеми предыдущими переходами - работает дёргано по сравнению с манифестом
                    //intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    //      Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentHome);
                    return true;
                case R.id.navigation_smetas_smetas_mat_cost:
                    if (file_id == -1){
                        // Для данного варианта в манифесте указан режим singlTask для активности ListOfSmetasNames
                        Intent intent_smetas = new Intent(SmetasMatCost.this, ListOfSmetasNames.class);
                        startActivity(intent_smetas);
                        Log.d(TAG, "1 SmetasMatCost onNavigationItemSelected file_id =" + file_id);
                    }else {
                        // Для данного варианта в манифесте указан режим singlTask для активности ListOfSmetasNames
                        Intent intent = new Intent(SmetasMatCost.this, Smetas.class);
                        intent.putExtra(P.ID_FILE, file_id);
                        startActivity(intent);
                        Log.d(TAG, "2 SmetasMatCost onNavigationItemSelected file_id =" + file_id);
                    }
                    return true;
                case R.id.navigation_smetas_mat_cost:
                    // Для данного варианта в манифесте указан режим singlTask для активности ListOfSmetasNames
                    Intent intent_smetas = new Intent(SmetasMatCost.this, ListOfSmetasNames.class);
                    startActivity(intent_smetas);
                    return true;
            }
            return false;
        }
    };

    private void updateAdapter(int item){
        // обновляем соседнюю вкладку  материалов и показываем её
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(item);
    }
}
