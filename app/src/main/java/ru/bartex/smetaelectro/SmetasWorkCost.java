package ru.bartex.smetaelectro;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;

public class SmetasWorkCost extends AppCompatActivity implements DialogSaveName.WorkCategoryTypeNameListener,
        SmetasTabCatAbstrFrag.OnClickCatListener, SmetasTabTypeAbstrFrag.OnClickTypekListener{

    public static final String TAG = "33333";
    long file_id;
    boolean isSelectedType = false;
    boolean isSelectedCat =  false;
    long type_id;
    long cat_id;
    SmetaOpenHelper mSmetaOpenHelper;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    public void catAndClickTransmit(long cat_id, boolean isSelectedCat) {
        Log.d(TAG, "//  SmetasWorkCost  catAndClickTransmit  // " );
        this.isSelectedCat = isSelectedCat;
        this.cat_id = cat_id;
        Log.d(TAG, "SmetasWorkCost  catAndClickTransmit cat_id =" +
                cat_id + "  isSelectedCat = " + isSelectedCat);
        //гениально простой способ заставить обновляться соседнюю вкладку
        //http://qaru.site/questions/683149/my-fragments-in-viewpager-tab-dont-refresh
        updateAdapter(1);
    }

    @Override
    public void typeAndClickTransmit(long cat_mat_id, long type_mat_id, boolean isSelectedTypeMat) {
        Log.d(TAG, "//  SmetasWorkCost  typeAndCatTransmit  // " );
        this.isSelectedType = isSelectedTypeMat;
        this.type_id = type_mat_id;
        this.cat_id = cat_mat_id;
        Log.d(TAG, "SmetasWorkCost  typeAndCatTransmit cat_id ="  +
                cat_id + "  type_id" + type_id + "  isSelectedType = " + isSelectedType);

        // обновляем соседнюю вкладку типов материалов и показываем её
        updateAdapter(2);
    }

    @Override
    public void workCategoryTypeNameTransmit(String workName, String typeName, String catName) {
        int position = mViewPager.getCurrentItem();
        Log.d(TAG, " ++++++++SmetasWorkCost  workCategoryTypeNameTransmit ++++++");
        switch (position){
            case 0:
                Log.d(TAG, "++++++++ SmetasWorkCost  workCategoryTypeNameTransmit ++++++ case 0");

                long newCatWorkCostId = mSmetaOpenHelper.insertCatName(catName);
                Log.d(TAG, "workCategoryTypeNameTransmit - workName = " + workName +
                        " typeName=" + typeName + " catName=" + catName +  " newCatWorkCostId=" + newCatWorkCostId);

                // обновляем вкладку категорий работы и показываем её
                updateAdapter(0);
                break;

            case 1:
                Log.d(TAG, "++++++++ SmetasWorkCost  workCategoryTypeNameTransmit ++++++ case 1");
                //определяем id категории по её имени
                long type_category_Id = mSmetaOpenHelper.getIdFromCategoryName(catName);
                long newTypeNameId = mSmetaOpenHelper.insertTypeName(typeName, type_category_Id);
                Log.d(TAG, "workCategoryTypeNameTransmit - workName = " + workName +
                        " typeName=" + typeName + " catName=" + catName +  " newTypeNameId=" + newTypeNameId);
                // обновляем вкладку типов работы и показываем её
                updateAdapter(1);
                break;

            case 2:
                Log.d(TAG, "++++++++ SmetasWorkCost  workCategoryTypeNameTransmit ++++++ case 2");
                //определяем id типа по его имени
                long work_type_Id = mSmetaOpenHelper.getIdFromTypeName(typeName);
                long newWorkNameId = mSmetaOpenHelper.insertWorkName(workName, work_type_Id);
                Log.d(TAG, "workCategoryTypeNameTransmit - workName = " + workName +
                        " typeName=" + typeName + " catName=" + catName +  " newWorkNameId=" + newWorkNameId);

                // обновляем  вкладку работы и показываем её
                updateAdapter(2);
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smetas_work_cost);

        file_id = getIntent().getLongExtra(P.ID_FILE,-1);
        Log.d(TAG, "SmetasWorkCost onCreate file_id =" + file_id);

        mSmetaOpenHelper = new SmetaOpenHelper(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_smetas_mat);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarWork);
        setSupportActionBar(toolbar);
        //показываем заголовок в заголовке экрана
        toolbar.setTitle(R.string.title_activity_SmetasWorkCost);
        toolbar.setTitleTextColor(Color.GREEN);

        //Создаём адаптер для фрагментов
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Привязываем ViewPager к адаптеру
        mViewPager = (ViewPager) findViewById(R.id.containerWork);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //средняя вкладка открыта
        mViewPager.setCurrentItem(1);
        // mViewPager.setOffscreenPageLimit(0);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsWork);
        tabLayout.setTabTextColors(Color.WHITE, Color.GREEN);
        //добавляем слушатель для tabLayout из трёх вкладок, который добавлен в макет
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //добавляем слушатель нажатий на заголовки вкладок
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        //добавляем слушатель для mViewPager, отслеживающий смену вкладки в ViewPager,
        // это нужно, чтобы организовать правильную работу меню тулбара в зависимости от действий с вкладками
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

        //в макете стоит GONE
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabWork);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //добираемся до списка фрагмента ___________пока нет_____________
        //http://qaru.site/questions/2399151/get-child-views-of-the-current-selected-items-in-viewpager
        View view = mViewPager.getChildAt(mViewPager.getCurrentItem());
        Log.d(TAG, " SmetasWorkCost  onCreate mViewPager.getCurrentItem() = " +
                mViewPager.getCurrentItem() + "  view = " + view );

        Log.d(TAG, " ))))))))SmetasWorkCost  onCreate((((((((  **************************");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, " ))))))))SmetasWorkCost  onCreateOptionsMenu(((((((( ///");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_smeta_mat, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d(TAG, " ))))))))SmetasWorkCost  onPrepareOptionsMenu(((((((( ///");
        int position = mViewPager.getCurrentItem();
        Log.d(TAG, " ))))))))SmetasWorkCost  onPrepareOptionsMenu(((((((( position = " + position +
                "  isSelectedCat = " + isSelectedCat + "  isSelectedType = " + isSelectedType);
        switch (position){
            case 0:
                Log.d(TAG, " ))))))))SmetasWorkCost  onPrepareOptionsMenu case 0");
                menu.findItem(R.id.action_add).setVisible(true);
                break;
            case 1:
                Log.d(TAG, " ))))))))SmetasWorkCost  onPrepareOptionsMenu case 1");
                menu.findItem(R.id.action_add).setVisible(isSelectedCat);
                break;
            case 2:
                Log.d(TAG, " ))))))))SmetasWorkCost  onPrepareOptionsMenu case 2");
                menu.findItem(R.id.action_add).setVisible(isSelectedType);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, " ))))))))SmetasWorkCost  onOptionsItemSelected(((((((( ///");
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;

        }else if (id == R.id.action_add){
            int position = mViewPager.getCurrentItem();
            Log.d(TAG, " ))))))))SmetasWorkCost  onOptionsItemSelected(((((((( position = " + position );
            switch (position){
                case 0:
                    Log.d(TAG, " ))))))))SmetasWorkCost  onOptionsItemSelected case 0");
                    DialogFragment saveCat = DialogSaveNameCat.newInstance(true);
                    saveCat.show(getSupportFragmentManager(),"SaveCatName");
                    break;
                case 1:
                    Log.d(TAG, " ))))))))SmetasWorkCost  onOptionsItemSelected case 1");
                    if (isSelectedCat){
                        DialogFragment saveType = DialogSaveNameType.newInstance(cat_id, true);
                        saveType.show(getSupportFragmentManager(), "saveType");
                    }
                    break;
                case 2:
                    Log.d(TAG, " ))))))))SmetasWorkCost  onOptionsItemSelected case 2");
                    DialogFragment saveMat = DialogSaveNameWork.newInstance(cat_id, type_id, true);
                    saveMat.show(getSupportFragmentManager(), "SaveWorkName");
                    break;
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //создаём контекстное меню для списка (сначала регистрация нужна  - здесь в onResume)
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, P.DELETE_ITEM_SMETA, 0, "Удалить пункт");
        menu.add(0, P.CANCEL, 0, "Отмена");

        // получаем инфу о пункте списка
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;

        switch (mViewPager.getCurrentItem()){
            case 0:
                Log.d(TAG, "SmetasWorkCost c  case 0:");
                //получаем имя категории из строки списка категории
                TextView tvName = acmi.targetView.findViewById(R.id.base_text);
                String name = tvName.getText().toString();
                //находим id категории по имени категории
                long cat_id = mSmetaOpenHelper.getIdFromCategoryName(name);
                //находим количество строк типов работы для cat_id
                int countLineType = mSmetaOpenHelper.getCountType(cat_id);
                Log.d(TAG, "SmetasWorkCost onCreateContextMenu - countLineType = " + countLineType);
                if(countLineType > 0) {
                    menu.findItem(P.DELETE_ITEM_SMETA).setEnabled(false);
                }
                break;
            case 1:
                Log.d(TAG, "SmetasWorkCost onCreateContextMenu  case 1:");
                //получаем имя типа из строки списка типов
                TextView tvType = acmi.targetView.findViewById(R.id.base_text);
                String typeName = tvType.getText().toString();
                //находим id типа по имени типа
                long type_id = mSmetaOpenHelper.getIdFromTypeName(typeName);
                //находим количество строк видов работы для type_id
                int countLineWork = mSmetaOpenHelper.getCountLineWork(type_id);
                Log.d(TAG, "SmetasWorkCost onCreateContextMenu - countLineWork = " + countLineWork);
                if(countLineWork > 0) {
                    menu.findItem(P.DELETE_ITEM_SMETA).setEnabled(false); //так лучше
                    //menu.findItem(P.DELETE_ID).setVisible(false);
                }
                break;

            case 2:
                Log.d(TAG, "SmetasWorkCost onCreateContextMenu  case 2:");
                //получаем имя работы  из строки списка видов работ
                TextView tvWork = acmi.targetView.findViewById(R.id.base_text);
                String workName = tvWork.getText().toString();
                //находим id вида  по имени вида работ
                long work_id = mSmetaOpenHelper.getIdFromWorkName(workName);
                //находим количество строк видов работы в таблице FW для work_id
                int countLineWorkFW = mSmetaOpenHelper.getCountLineWorkInFW(work_id);
                //находим количество строк расценок работы в таблице CostWork для work_id
                int countCostLineWork = mSmetaOpenHelper.getCountLineWorkInCost(work_id);
                Log.d(TAG, "SmetasWorkCost onCreateContextMenu - countLineWorkFW = " + countLineWorkFW +
                        " countCostLineWork =" + countCostLineWork);

               // mSmetaOpenHelper.displayTableCost();

                if(countLineWorkFW > 0) {
                    menu.findItem(P.DELETE_ITEM_SMETA).setEnabled(false); //так лучше
                    //menu.findItem(P.DELETE_ID).setVisible(false);
                }
                break;
        }
    }

    //практически полное повторение метода из SmetasWork
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        final AdapterView.AdapterContextMenuInfo acmi =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        //если удалить из контекстного меню
        if (item.getItemId() == P.DELETE_ITEM_SMETA) {

            Log.d(TAG, "SmetasWorkCost P.DELETE_ITEM_SMETA");
            AlertDialog.Builder builder = new AlertDialog.Builder(SmetasWorkCost.this);
            builder.setTitle(R.string.Delete_Item);
            builder.setPositiveButton(R.string.DeleteNo, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setNegativeButton(R.string.DeleteYes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG, "SmetasWorkCost P.DELETE_ITEM_SMETA acmi.position  =" +
                            (acmi.position));

                    TextView tv = acmi.targetView.findViewById(R.id.base_text);
                    String name = tv.getText().toString();
                    switch (mViewPager.getCurrentItem()){
                        case 0:
                            Log.d(TAG, "SmetasWorkCost onContextItemSelected  P.DELETE_ID case 0");
                            TextView tvCat = acmi.targetView.findViewById(R.id.base_text);
                            String cat_name = tvCat.getText().toString();
                            //находим id по имени файла
                            long cat_id = mSmetaOpenHelper.getIdFromCategoryName(cat_name);
                            Log.d(TAG, "SmetasWorkCost onContextItemSelected case P.DELETE_ID" +
                                    " cat_name = " + cat_name + " cat_id =" + cat_id);
                            //Удаляем файл из таблицы CategoryWork когда в категории нет типов
                            mSmetaOpenHelper.deleteCategory(cat_id);
                            // обновляем соседнюю вкладку категорий работы и показываем её
                            updateAdapter(0);
                            break;

                        case 1:
                            Log.d(TAG, "SmetasWorkCost onContextItemSelected  P.DELETE_ID case 1");
                            TextView tvType = acmi.targetView.findViewById(R.id.base_text);
                            String type_name = tvType.getText().toString();
                            //находим id по имени файла
                            long type_id = mSmetaOpenHelper.getIdFromTypeName(type_name);
                            Log.d(TAG, "SmetasWorkCost onContextItemSelected case P.DELETE_ID" +
                                    " type_name = " + type_name + " type_id =" + type_id);
                            //Удаляем файл из таблицы TypeWork когда в типе нет видов работ
                            mSmetaOpenHelper.deleteType(type_id);

                            //после удаления в типе работ не даём появиться + в тулбаре
                            isSelectedCat = false;
                            // обновляем соседнюю вкладку типов работы и показываем её
                            updateAdapter(0);
                            break;
                        case 2:
                            Log.d(TAG, "SmetasWorkCost P.DELETE_ITEM_SMETA case 2");
                            //находим id по имени работы
                            long work_id = mSmetaOpenHelper.getIdFromWorkName(name);
                            Log.d(TAG, "SmetasWorkCost onContextItemSelected file_id = " +
                                    file_id + " work_id =" + work_id+ " work_name =" + name);

                            mSmetaOpenHelper.displayTableCost();
                            //Удаляем запись из таблицы Work когда в таблице FW нет такой  работы
                            // проверка в onCreateContextMenu
                            mSmetaOpenHelper.deleteWork(work_id);
                            //Удаляем запись из таблицы CostWork когда в таблице FW нет такой  работы
                            mSmetaOpenHelper.deleteCostOfWork(work_id);
                            // проверка в onCreateContextMenu
                            mSmetaOpenHelper.displayTableCost();
                            //после удаления в работах не даём появиться + в тулбаре
                            isSelectedType = false;
                            //обновляем данные списка фрагмента активности
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


    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Log.d(TAG, " ))))))))SmetasWorkCost Fragment getItem ((((((((");
            switch (position){
                case 0:
                    Log.d(TAG, "SmetasWorkCost  Fragment getItem case 0: " );
                    SWT1CatCost tab1Category = SWT1CatCost.NewInstance(
                            file_id,position);
                    Log.d(TAG, "SmetasWorkCost  Fragment getItem case 0: file_id = " +
                            file_id + "  position = " +  position);
                    return tab1Category;
                case 1:
                    Log.d(TAG, "SmetasWorkCost  Fragment getItem case 1/1: " );
                    SWT2TypeCost tab2Type = SWT2TypeCost.NewInstance(
                            file_id, position, isSelectedCat, cat_id);
                    Log.d(TAG, "SmetasWorkCost  Fragment getItem case 1/2: isSelectedCat = " +
                            isSelectedCat + "  cat_id = " +  cat_id + "  file_id = " +  file_id +
                            "  position = " +  position);
                    return tab2Type;
                case 2:
                    Log.d(TAG, "SmetasWorkCost  Fragment getItem case 2/1: " );
                    //передаём во фрагмент данные (и способ их обработки) в зависимости от isSelectedType
                    SWT3Cost tab3Mat = SWT3Cost.NewInstance(
                            file_id, position, isSelectedType, type_id);
                    Log.d(TAG, "SmetasWorkCost  Fragment getItem case 2/2: isSelectedType = " +
                            isSelectedType + "  type_id = " +  type_id + "  file_id = " +  file_id +
                            "  position = " +  position);
                    return tab3Mat;
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
                    Intent intentHome = new Intent(SmetasWorkCost.this, MainActivity.class);
                    // установка флагов- создаём новую задачу и убиваем старую вместе
                    // со всеми предыдущими переходами - работает дёргано по сравнению с манифестом
                    //intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    //      Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentHome);
                    return true;
                case R.id.navigation_smetas_smetas_mat_cost:
                    if (file_id == -1){
                        // Для данного варианта в манифесте указан режим singlTask для активности ListOfSmetasNames
                        Intent intent_smetas = new Intent(SmetasWorkCost.this, ListOfSmetasNames.class);
                        startActivity(intent_smetas);
                        Log.d(TAG, "1 SmetasMatCost onNavigationItemSelected file_id =" + file_id);
                    }else {
                        // Для данного варианта в манифесте указан режим singlTask для активности ListOfSmetasNames
                        Intent intent = new Intent(SmetasWorkCost.this, Smetas.class);
                        intent.putExtra(P.ID_FILE, file_id);
                        startActivity(intent);
                        Log.d(TAG, "2 SmetasMatCost onNavigationItemSelected file_id =" + file_id);
                    }
                    return true;
                case R.id.navigation_smetas_mat_cost:
                    Intent intentCostMat = new Intent(SmetasWorkCost.this, ListOfSmetasNames.class);
                    intentCostMat.putExtra(P.ID_FILE, file_id);
                    startActivity(intentCostMat);
                    return true;
            }
            return false;
        }
    };

    private void updateAdapter(int currentItem){
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(currentItem);
        mSectionsPagerAdapter.notifyDataSetChanged();
    }

}
