package ru.bartex.smetaelectro.ui.smetas3tabs.smetamat;

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
import ru.bartex.smetaelectro.ui.smetas3tabs.smetamatcost.SmetasMatCost;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkpageadapter.SmetasWorkPagerAdapter;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.KindWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.SmetaOpenHelper;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.CategoryMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.Mat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.TypeMat;
import ru.bartex.smetaelectro.ui.dialogs.DialogSaveNameAbstract;
import ru.bartex.smetaelectro.ui.dialogs.DialogSaveNameCat;
import ru.bartex.smetaelectro.ui.dialogs.DialogSaveNameType;
import ru.bartex.smetaelectro.ui.dialogs.DialogSaveNameWork;
import ru.bartex.smetaelectro.ui.main.MainActivity;
import ru.bartex.smetaelectro.ui.smetas2tabs.SmetasTab;

public class SmetasMat extends AppCompatActivity implements
        AbstrSmetasFrag.OnClickTypekListener, AbstrSmetasFrag.OnClickCatListener,
        DialogSaveNameAbstract.WorkCategoryTypeNameListener{

    public static final String TAG = "33333";
    long file_id;
    boolean isSelectedType = false;
    boolean isSelectedCat =  false;
    long type_id;
    long cat_id;

    private SQLiteDatabase database;
    private SmetasWorkPagerAdapter adapter;
    private Fragment tab1MatCat, tab2MatType, tab3MatWork ;
    private ViewPager mViewPager;

    @Override
    public void catAndClickTransmit(long cat_mat_id, boolean isSelectedCatMat) {
        Log.d(TAG, "//  SmetasMat  catAndClickTransmit  // " );
        this.isSelectedCat = isSelectedCatMat;
        this.cat_id = cat_mat_id;
        Log.d(TAG, "SmetasMat  catAndClickTransmit cat_id =" +
                cat_id + "  isSelectedCat = " + isSelectedCat);

        adapter. updateMatType(cat_id);
        updatePageAdapter(1);
    }

    @Override
    public void typeAndClickTransmit(long cat_mat_id, long type_mat_id, boolean isSelectedTypeMat) {
        Log.d(TAG, "//  SmetasMat  typeAndCatTransmit  // " );
        this.isSelectedType = isSelectedTypeMat;
        this.type_id = type_mat_id;
        this.cat_id = cat_mat_id;
        Log.d(TAG, "SmetasMat  typeAndCatTransmit cat_id ="  +
                cat_id + "  type_id" + type_id + "  isSelectedType = " + isSelectedType);

        adapter. updateMatName(cat_id, type_id);
        updatePageAdapter(2);
    }

    @Override
    public void workCategoryTypeNameTransmit(String workName, String typeName, String catName) {
        int position = mViewPager.getCurrentItem();
        Log.d(TAG, " ++++++++SmetasMat  workCategoryTypeNameTransmit ++++++");
        switch (position){
            case 0:
                Log.d(TAG, "++++++++ SmetasMat  workCategoryTypeNameTransmit ++++++ case 0");

                long newCatMatNameId = CategoryMat.insertCategory(database, catName);
                Log.d(TAG, "workCategoryTypeNameTransmit - workName = " + workName +
                        " typeName=" + typeName + " catName=" + catName +  " newCatMatNameId=" + newCatMatNameId);

                adapter.updateMatCategoryAdd();
                // обновляем адаптер
                updatePageAdapter(0);
                break;

            case 1:
                Log.d(TAG, "++++++++ SmetasMat  workCategoryTypeNameTransmit ++++++ case 1");
                //определяем id категории по её имени
                long category_Id = CategoryMat.getIdFromName(database, catName);
                long newTypeWorkNameId = TypeMat.insertTypeCatName(database,
                        typeName, category_Id);
                Log.d(TAG, "workCategoryTypeNameTransmit - workName = " + workName +
                        " typeName=" + typeName + " catName=" + catName +  " newTypeMatNameId=" + newTypeWorkNameId);

                adapter. updateMatTypeAdd(category_Id);
                // обновляем адаптер
                updatePageAdapter(1);
                break;

            case 2:
                Log.d(TAG, "++++++++ SmetasMat  workCategoryTypeNameTransmit ++++++ case 2");
                //определяем id типа по его имени
                long type_Id = TypeMat.getIdFromName(database, typeName);
                long newWorkNameId = Mat.insertTypeCatName(database, workName, type_Id);
                Log.d(TAG, "workCategoryTypeNameTransmit - workName = " + workName +
                        " typeName=" + typeName + " catName=" + catName +  " newMatNameId=" + newWorkNameId);

                adapter. updateMatNameAdd(type_Id);
                // обновляем адаптер
                updatePageAdapter(2);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smetas_mat);
        Log.d(TAG, "//SmetasMat-onCreate");

        initDB();

        file_id = getIntent().getExtras().getLong(P.ID_FILE);
        Log.d(TAG, "//SmetasMat  onCreate file_id = " +  file_id);

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
        tab1MatCat = MatCat.newInstance(file_id, 0);
        tab2MatType = MatType.newInstance(file_id, 1, false, 0);
        tab3MatWork = MatName.newInstance(file_id, 2, false, 0);
    }

    private void initPageAdapter() {
        //Log.d(TAG, "//SmetasWork-initPageAdapter");
        //здесь используется вариант  добавления фрагментов из активити
        adapter = new SmetasWorkPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(tab1MatCat, "Категория" );
        adapter.addFragment(tab2MatType, "Тип" );
        adapter.addFragment(tab3MatWork, "Название" );
    }

    private void initViewPager() {
        // Log.d(TAG, "//SmetasWork-initViewPager");
        mViewPager = findViewById(R.id.container_smetas_mat);
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
        TabLayout tabs = findViewById(R.id.tabs_smetas_mat);
        tabs.setTabTextColors(Color.WHITE, Color.GREEN);
        tabs.setupWithViewPager(mViewPager);
    }

    private void initDB() {
        database = new SmetaOpenHelper(this).getWritableDatabase();
    }

    private void initBottomNavigation() {
        BottomNavigationView navigation = findViewById(R.id.navigation_smetas_mat);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_smetas_mat);
        setSupportActionBar(toolbar);
        //показываем заголовок в toolbar экрана
        toolbar.setTitle(R.string.title_activity_SmetasMat);
        toolbar.setTitleTextColor(Color.GREEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, " ))))))))SmetasMat  onCreateOptionsMenu(((((((( ///");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_smeta_mat, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d(TAG, " ))))))))SmetasMat  onPrepareOptionsMenu(((((((( ///");
        int position = mViewPager.getCurrentItem();
        Log.d(TAG, " ))))))))SmetasMat  onPrepareOptionsMenu(((((((( position = " + position +
                "  isSelectedCat = " + isSelectedCat + "  isSelectedType = " + isSelectedType);
        switch (position){
            case 0:
                Log.d(TAG, " ))))))))SmetasMat  onPrepareOptionsMenu case 0");
                menu.findItem(R.id.action_add).setVisible(true);
                break;
            case 1:
                Log.d(TAG, " ))))))))SmetasMat  onPrepareOptionsMenu case 1");
                menu.findItem(R.id.action_add).setVisible(isSelectedCat);
                break;
            case 2:
                Log.d(TAG, " ))))))))SmetasMat  onPrepareOptionsMenu case 2");
                menu.findItem(R.id.action_add).setVisible(isSelectedType);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, " ))))))))SmetasMat  onOptionsItemSelected(((((((( ///");
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;

        }else if (id == R.id.action_add){
            int position = mViewPager.getCurrentItem();
            Log.d(TAG, " ))))))))SmetasMat  onOptionsItemSelected(((((((( position = " + position );
            switch (position){
                case 0:
                    Log.d(TAG, " ))))))))SmetasMat  onOptionsItemSelected case 0");
                    DialogFragment saveCat = DialogSaveNameCat.newInstance(false);
                    saveCat.show(getSupportFragmentManager(),"SaveCatName");
                    break;
                case 1:
                    Log.d(TAG, " ))))))))SmetasMat  onOptionsItemSelected case 1");
                    DialogFragment saveType = DialogSaveNameType.newInstance(cat_id, false);
                    saveType.show(getSupportFragmentManager(), "saveTypeName");
                    break;
                case 2:
                    Log.d(TAG, " ))))))))SmetasMat  onOptionsItemSelected case 2");
                    DialogFragment saveMat = DialogSaveNameWork.newInstance(cat_id, type_id, false);
                    saveMat.show(getSupportFragmentManager(), "SaveMatName");
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
                adapter.showDetails(mViewPager.getCurrentItem(), KindWork.MAT);
                break;

            case R.id.menu_change_name:
                adapter.changeName(mViewPager.getCurrentItem(), KindWork.MAT);
                break;

            case R.id.menu_delete:
                adapter.deleteItem(mViewPager.getCurrentItem(), KindWork.MAT);
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
                    Intent intentHome = new Intent(SmetasMat.this, MainActivity.class);
                    // установка флагов- создаём новую задачу и убиваем старую вместе
                    // со всеми предыдущими переходами - работает дёргано по сравнению с манифестом
                    //intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    //      Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentHome);
                    return true;
                case R.id.navigation_smetas_smetas_mat:
                    // Для данного варианта в манифесте указан режим singlTask для активности ListOfSmetasNames
                    Intent intent = new Intent(SmetasMat.this, SmetasTab.class);
                    intent.putExtra(P.ID_FILE, file_id);
                    startActivity(intent);
                    return true;
                case R.id.navigation_costs_smetas_mat:
                    Intent intentCostMat = new Intent(SmetasMat.this, SmetasMatCost.class);
                    intentCostMat.putExtra(P.ID_FILE, file_id);
                    startActivity(intentCostMat);
                    return true;
            }
            return false;
        }
    };

    private void updatePageAdapter(int currentItem){
        mViewPager.setCurrentItem(currentItem);
        adapter.notifyDataSetChanged();
    }
}
