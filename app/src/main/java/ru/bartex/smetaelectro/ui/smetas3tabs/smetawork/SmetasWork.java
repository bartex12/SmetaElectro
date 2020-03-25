package ru.bartex.smetaelectro.ui.smetas3tabs.smetawork;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import ru.bartex.smetaelectro.ui.smetas3tabs.abstractfrag.AbstrSmetasFrag;
import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkcost.SmetasWorkCost;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkpageadapter.SmetasWorkPagerAdapter;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.KindWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.CategoryWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.TypeWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.Work;
import ru.bartex.smetaelectro.ui.dialogs.DialogSaveNameAbstract;
import ru.bartex.smetaelectro.ui.dialogs.DialogSaveNameCat;
import ru.bartex.smetaelectro.ui.dialogs.DialogSaveNameType;
import ru.bartex.smetaelectro.ui.dialogs.DialogSaveNameWork;
import ru.bartex.smetaelectro.ui.main.MainActivity;
import ru.bartex.smetaelectro.ui.smetas2tabs.SmetasTab;


public class SmetasWork extends AppCompatActivity implements
        AbstrSmetasFrag.OnClickTypekListener,
        AbstrSmetasFrag.OnClickCatListener,
        DialogSaveNameAbstract.WorkCategoryTypeNameListener {

    private static final String TAG = "33333";
    private long file_id;
    private boolean isSelectedType = false;
    private boolean isSelectedCat =  false;
    private long type_id;
    private long cat_id;

    private ViewPager mViewPager;
    private SQLiteDatabase database;
    private SmetasWorkPagerAdapter adapter;
    private Fragment tab1WorkCat, tab2WorkType, tab3WorkWork ;

    @Override
    public void catAndClickTransmit(long cat_id, boolean isSelectedCat) {
        Log.d(TAG, "/***/  SmetasWork  catAndClickTransmit  // " );
        this.isSelectedCat = isSelectedCat;
        this.cat_id = cat_id;
        Log.d(TAG, "/***/ SmetasWork  catAndClickTransmit cat_id =" +
                cat_id + "  isSelectedCat = " + isSelectedCat);

        adapter. updateWorkType(cat_id);
        updatePageAdapter(1);

    }

    @Override
    public void typeAndClickTransmit(long cat_id, long type_id, boolean isSelectedType) {
        Log.d(TAG, "//  SmetasWork  typeAndCatTransmit  // " );
        this.isSelectedType = isSelectedType;
        this.type_id = type_id;
        this.cat_id = cat_id;
        Log.d(TAG, "SmetasWork  typeAndCatTransmit cat_id ="  +
                cat_id + "  type_id" + type_id + "  isSelectedType = " + isSelectedType);

        adapter. updateWorkName(cat_id, type_id);
        updatePageAdapter(2);
    }

    @Override
    public void workCategoryTypeNameTransmit(String workName, String typeName, String catName) {
        int position = mViewPager.getCurrentItem();
        Log.d(TAG, " ++++++++SmetasWork  workCategoryTypeNameTransmit ++++++");
        switch (position){
            case 0:
                Log.d(TAG, "++++++++ SmetasWork  workCategoryTypeNameTransmit ++++++ case 0");
                long newCatMatNameId = CategoryWork.insertCategory(database, catName);
                Log.d(TAG, "workCategoryTypeNameTransmit - workName = " + workName +
                        " typeName=" + typeName + " catName=" + catName +  " newCatMatNameId=" + newCatMatNameId);

                adapter.updateWorkCategoryAdd();
                // обновляем вкладку категорий работы и показываем её
                updatePageAdapter(0);
                break;

            case 1:
                Log.d(TAG, "++++++++ SmetasWork  workCategoryTypeNameTransmit ++++++ case 1");
                //определяем id категории по её имени
                long category_Id = CategoryWork.getIdFromName(database, catName);
                long newTypeNameId = TypeWork.insertTypeCatName(database, typeName, category_Id);
                Log.d(TAG, "workCategoryTypeNameTransmit - workName = " + workName +
                        " typeName=" + typeName + " catName=" + catName +  " newTypeNameId=" + newTypeNameId);

                adapter. updateWorkTypeAdd(category_Id);
                // обновляем вкладку типов работы и показываем её
                updatePageAdapter(1);
                break;

            case 2:
                Log.d(TAG, "++++++++ SmetasWork  workCategoryTypeNameTransmit ++++++ case 2");
                //определяем id типа по его имени
                long type_Id = TypeWork.getIdFromName(database, typeName);
                long newWorkNameId = Work.insertTypeCatName(database,  workName, type_Id);
                Log.d(TAG, "workCategoryTypeNameTransmit - workName = " + workName +
                        " typeName=" + typeName + " catName=" + catName +  " newWorkNameId=" + newWorkNameId);

                adapter. updateWorkNameAdd(type_Id);
                // обновляем  вкладку  работы и показываем её
                updatePageAdapter(2);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smetas_work);
        Log.d(TAG, "//SmetasWork-onCreate");

        initDB();

        file_id = getIntent().getExtras().getLong(P.ID_FILE);
        Log.d(TAG, "//SmetasWork  onCreate file_id = " +  file_id);

        initBottomNavigation();
        initToolbar();
        // фрагменты инициализируются здесь, чтобы не менять их при возврате из деталей
        createTabFrags();
        // адаптер, ViewPager инициализируются в onResume,
        // чтобы при возврате на SmetasWork из деталей происходило обновление пунктов списка
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "//SmetasWork-onResume");
        initPageAdapter();
        initViewPager();
        Log.d(TAG, "//SmetasWork-onResume currentTabItem = " + mViewPager.getCurrentItem());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        database.close();
    }

    private void createTabFrags() {
        //Log.d(TAG, "//SmetasWork-createTabFrags");
        //создаём фрагменты
        tab1WorkCat = WorkCat.newInstance(file_id, 0);
        tab2WorkType = WorkType.newInstance(file_id, 1, false, 0);
        tab3WorkWork = WorkName.newInstance(file_id, 2, false, 0);
    }

    private void initPageAdapter() {
        //Log.d(TAG, "//SmetasWork-initPageAdapter");
        //здесь используется вариант  добавления фрагментов из активити
        adapter = new SmetasWorkPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(tab1WorkCat, "Категория" );
        adapter.addFragment(tab2WorkType, "Тип" );
        adapter.addFragment(tab3WorkWork, "Название" );
    }

    private void initViewPager() {
       // Log.d(TAG, "//SmetasWork-initViewPager");
        mViewPager = findViewById(R.id.container_smetas_work);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(1);

        //добавляем слушатель для mViewPager, отслеживающий смену вкладки в ViewPager,
        // это нужно, чтобы организовать правильную работу меню тулбара в зависимости от действий с вкладками
        //видимо, вызывается метод onPrepareOptionsMenu при смене вкладки
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

        //если это не сделать, то названия вкладок не будут отображаться,
        // хотя слайер и будет работать
        TabLayout tabs = findViewById(R.id.tabs_smetas_work);
        tabs.setTabTextColors(Color.WHITE, Color.GREEN);
        tabs.setupWithViewPager(mViewPager);
    }

    private void initDB() {
        //
        database = new SmetaOpenHelper(this).getWritableDatabase();
    }

    private void initBottomNavigation() {
        BottomNavigationView navigation = findViewById(R.id.navigation_smetas_work);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_smetas_work);
        setSupportActionBar(toolbar);
        //показываем заголовок в toolbar экрана
        toolbar.setTitle(R.string.title_activity_SmetasWork);
        toolbar.setTitleTextColor(Color.GREEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, " ))))))))SmetasWork  onCreateOptionsMenu(((((((( ///");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_smeta_mat, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d(TAG, " ))))))))SmetasWork  onPrepareOptionsMenu(((((((( ///");

        int position = mViewPager.getCurrentItem();
        Log.d(TAG, " ))))))))SmetasWork  onPrepareOptionsMenu(((((((( position = " + position +
                "  isSelectedCat = " + isSelectedCat + "  isSelectedType = " + isSelectedType);
        switch (position){
            case 0:
                Log.d(TAG, " ))))))))SmetasWork  onPrepareOptionsMenu case 0");
                menu.findItem(R.id.action_add).setVisible(true);
                break;
            case 1:
                Log.d(TAG, " ))))))))SmetasWork  onPrepareOptionsMenu case 1");
                menu.findItem(R.id.action_add).setVisible(isSelectedCat);
                break;
            case 2:
                Log.d(TAG, " ))))))))SmetasWork  onPrepareOptionsMenu case 2");
                menu.findItem(R.id.action_add).setVisible(isSelectedType);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, " ))))))))SmetasWork  onOptionsItemSelected(((((((( ///");
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;

        }else if (id == R.id.action_add){
            int position = mViewPager.getCurrentItem();
            Log.d(TAG, " ))))))))SmetasWork  onOptionsItemSelected(((((((( position = " + position );
            switch (position){
                case 0:
                    Log.d(TAG, " ))))))))SmetasWork  onOptionsItemSelected case 0");
                    DialogFragment saveCat = DialogSaveNameCat.newInstance(true);
                    saveCat.show(getSupportFragmentManager(),"SaveCatName");
                    break;
                case 1:
                    Log.d(TAG, " ))))))))SmetasWork  onOptionsItemSelected case 1");
                    DialogFragment saveType = DialogSaveNameType.newInstance(cat_id, true);
                    saveType.show(getSupportFragmentManager(), "saveType");
                    break;
                case 2:
                    Log.d(TAG, " ))))))))SmetasWork  onOptionsItemSelected case 2");
                    DialogFragment saveMat = DialogSaveNameWork.newInstance(cat_id, type_id, true);
                    saveMat.show(getSupportFragmentManager(), "SaveWorkName");
                    break;
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //создаём контекстное меню для списка
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu_list_of_smetas, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        handleMenuItemClick(item);
        return true;
    }

    private  void handleMenuItemClick(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.menu_detail:
                adapter.showDetails(mViewPager.getCurrentItem(), KindWork.WORK);
                break;

            case R.id.menu_change_name:
                adapter.changeName(mViewPager.getCurrentItem(), KindWork.WORK);
                break;

            case R.id.menu_delete:
                adapter.deleteItem(mViewPager.getCurrentItem(), KindWork.WORK);
                break;

            case R.id.menu_cancel:

                break;
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home_smetas_mat:
                    // Для данного варианта в манифесте указан режим singlTask для активности MainActivity
                    Intent intentHome = new Intent(SmetasWork.this, MainActivity.class);
                    // установка флагов- создаём новую задачу и убиваем старую вместе
                    // со всеми предыдущими переходами - работает дёргано по сравнению с манифестом
                    //intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    //      Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentHome);
                    return true;
                case R.id.navigation_smetas_smetas_mat:
                    // Для данного варианта в манифесте указан режим singlTask для активности ListOfSmetasNames
                    Intent intent = new Intent(SmetasWork.this, SmetasTab.class);
                    intent.putExtra(P.ID_FILE, file_id);
                    startActivity(intent);
                    return true;
                case R.id.navigation_costs_smetas_mat:
                    Intent intentCostMat = new Intent(SmetasWork.this, SmetasWorkCost.class);
                    intentCostMat.putExtra(P.ID_FILE, file_id);
                    startActivity(intentCostMat);
                    return true;
            }
            return false;
        }
    };

    private void updatePageAdapter(int currentItem){
    //adapter = new SmetasWorkPagerAdapter(getSupportFragmentManager());
    //mViewPager.setAdapter(adapter);
    mViewPager.setCurrentItem(currentItem);
    adapter.notifyDataSetChanged();
    }

}

