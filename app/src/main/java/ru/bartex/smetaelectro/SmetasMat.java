package ru.bartex.smetaelectro;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.SmetaOpenHelper;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.CategoryMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.CostMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.FM;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.Mat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.TypeMat;
import ru.bartex.smetaelectro.ui.dialogs.DialogSaveNameAbstract;
import ru.bartex.smetaelectro.ui.dialogs.DialogSaveNameCat;
import ru.bartex.smetaelectro.ui.dialogs.DialogSaveNameType;
import ru.bartex.smetaelectro.ui.dialogs.DialogSaveNameWork;
import ru.bartex.smetaelectro.ui.main.MainActivity;

public class SmetasMat extends AppCompatActivity implements
        Tab2SmetasTypeAbstrFrag.OnClickTypekListener, Tab1SmetasCatAbstrFrag.OnClickCatListener,
        DialogSaveNameAbstract.WorkCategoryTypeNameListener{

    public static final String TAG = "33333";
    long file_id;
    boolean isSelectedType = false;
    boolean isSelectedCat =  false;
    long type_id;
    long cat_id;

    private SQLiteDatabase database;
    Menu menu;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    public void catAndClickTransmit(long cat_mat_id, boolean isSelectedCatMat) {
        Log.d(TAG, "//  SmetasMat  catAndClickTransmit  // " );
        this.isSelectedCat = isSelectedCatMat;
        this.cat_id = cat_mat_id;
        Log.d(TAG, "SmetasMat  catAndClickTransmit cat_id =" +
                cat_id + "  isSelectedCat = " + isSelectedCat);
        //гениально простой способ заставить обновляться соседнюю вкладку
        //http://qaru.site/questions/683149/my-fragments-in-viewpager-tab-dont-refresh
        updateAdapter(1);
    }

    @Override
    public void typeAndClickTransmit(long cat_mat_id, long type_mat_id, boolean isSelectedTypeMat) {
        Log.d(TAG, "//  SmetasMat  typeAndCatTransmit  // " );
        this.isSelectedType = isSelectedTypeMat;
        this.type_id = type_mat_id;
        this.cat_id = cat_mat_id;
        Log.d(TAG, "SmetasMat  typeAndCatTransmit cat_id ="  +
                cat_id + "  type_id" + type_id + "  isSelectedType = " + isSelectedType);

        // обновляем соседнюю вкладку типов материалов и показываем её
        updateAdapter(2);
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

                // обновляем адаптер
                updateAdapter(0);
                break;

            case 1:
                Log.d(TAG, "++++++++ SmetasMat  workCategoryTypeNameTransmit ++++++ case 1");
                //определяем id категории по её имени
                long type_category_Id = CategoryMat.getIdFromName(database, catName);
                long newTypeWorkNameId = TypeMat.insertTypeCatName(database,
                        typeName, type_category_Id);
                Log.d(TAG, "workCategoryTypeNameTransmit - workName = " + workName +
                        " typeName=" + typeName + " catName=" + catName +  " newTypeMatNameId=" + newTypeWorkNameId);
                // обновляем адаптер
                updateAdapter(1);
                break;

            case 2:
                Log.d(TAG, "++++++++ SmetasMat  workCategoryTypeNameTransmit ++++++ case 2");
                //определяем id типа по его имени
                long work_type_Id = TypeMat.getIdFromName(database, typeName);
                long newWorkNameId = Mat.insertTypeCatName(database, workName, work_type_Id);
                Log.d(TAG, "workCategoryTypeNameTransmit - workName = " + workName +
                        " typeName=" + typeName + " catName=" + catName +  " newMatNameId=" + newWorkNameId);

                // обновляем адаптер
                updateAdapter(2);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smetas_mat);

        initDB();

        file_id = getIntent().getExtras().getLong(P.ID_FILE);
        Log.d(TAG, " ))))))))SmetasMat  onCreate((((((((  file_id = " +  file_id);

        BottomNavigationView navigation = findViewById(R.id.navigation_smetas_mat);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Toolbar toolbar = findViewById(R.id.toolbarMat);
        setSupportActionBar(toolbar);
        //показываем заголовокмв заголовке экрана
        toolbar.setTitle(R.string.title_activity_SmetasMat);
        toolbar.setTitleTextColor(Color.GREEN);

        //Создаём адаптер для фрагментов
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Привязываем ViewPager к адаптеру
        mViewPager = findViewById(R.id.containerMat);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //средняя вкладка открыта
        mViewPager.setCurrentItem(1);
       // mViewPager.setOffscreenPageLimit(0);

        TabLayout tabLayout = findViewById(R.id.tabsMat);
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

        FloatingActionButton fab = findViewById(R.id.fab);
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
        Log.d(TAG, " SmetasMat  onCreate mViewPager.getCurrentItem() = " +
                mViewPager.getCurrentItem() + "  view = " + view );
        //ListView mListView = view.findViewById(R.id.listViewFragmentTabs);
        //ListView mListView = mViewPager.getRootView().findViewById(R.id.listViewFragmentTabs);
        //объявляем о регистрации контекстного меню
        //registerForContextMenu(mListView);
        Log.d(TAG, " ))))))))SmetasMat  onCreate((((((((  **************************");
    }

    private void initDB() {
        //
        database = new SmetaOpenHelper(this).getWritableDatabase();
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
        this.menu =menu;
        Log.d(TAG, " ))))))))SmetasMat  onPrepareOptionsMenu(((((((( ///");
        ActionBar acBar = getSupportActionBar();
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
                    if (isSelectedCat){
                        DialogFragment saveType = DialogSaveNameType.newInstance(cat_id, false);
                        saveType.show(getSupportFragmentManager(), "saveTypeName");
                    }
                    break;
                case 2:
                    //long cat_id = mSmetaOpenHelper.getCatIdFromTypeMat(type_id);
                    Log.d(TAG, " ))))))))SmetasMat  onOptionsItemSelected case 2");
                    Log.d(TAG, " SmetasMat  onOptionsItemSelected case 2 cat_id = " + cat_id +
                            "  type_id = " + type_id);
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
        menu.add(0, P.SPECIFIC_ID, 0, R.string.action_detail);
        menu.add(0, P.CHANGE_NAME_ID, 0, R.string.action_change_name);
        menu.add(0, P.DELETE_ID, 0, R.string.action_delete);
        menu.add(0, P.CANCEL, 0, R.string.action_cancel);

        // получаем инфу о пункте списка
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;

        switch (mViewPager.getCurrentItem()){
            case 0:
                //получаем имя категории из строки списка категории
                TextView tvName = acmi.targetView.findViewById(R.id.base_text);
                String name = tvName.getText().toString();
                //находим id категории по имени категории
                long cat_mat_id = CategoryMat.getIdFromName(database, name);
                //находим количество строк типов материала для cat_mat_id
                int countType_mat = TypeMat.getCountLine(database, cat_mat_id);
                Log.d(TAG, "onCreateContextMenu - countType = " + countType_mat);
                if(countType_mat > 0) {
                    menu.findItem(P.DELETE_ID).setEnabled(false);
                }
                break;
            case 1:
                //получаем имя типа из строки списка типов материала
                TextView tvType = acmi.targetView.findViewById(R.id.base_text);
                String typeMatName = tvType.getText().toString();
                //находим id типа по имени типа
                long type_mat_id = TypeMat.getIdFromName(database, typeMatName);
                //находим количество строк видов материала для type_mat_id
                int countLineMat = Mat.getCountLine(database, type_mat_id);
                Log.d(TAG, "onContextItemSelected - countLineMat = " + countLineMat);
                if(countLineMat > 0) {
                    menu.findItem(P.DELETE_ID).setEnabled(false);
                }
                break;
            case 2:
                //получаем имя материала  из строки списка видов материала
                TextView tvMat = acmi.targetView.findViewById(R.id.base_text);
                String matName = tvMat.getText().toString();
                //находим id вида  по имени вида материала
                long mat_id = Mat.getIdFromName(database, matName);
                //находим количество строк видов материала в таблице FM для mat_id
                int countLineWorkFM = FM.getCountLine(database, mat_id);
                Log.d(TAG, "SmetasMat onContextItemSelected - countLineWorkFM = " + countLineWorkFM);
                //mSmetaOpenHelper.displayTableCost();
                if(countLineWorkFM > 0) {
                    menu.findItem(P.DELETE_ID).setEnabled(false);
                }
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        // получаем инфу о пункте списка
        final AdapterView.AdapterContextMenuInfo acmi =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int id = item.getItemId();
        Log.d(TAG, "SmetasMat onContextItemSelected id = " + id +
                " acmi.position = " + acmi.position + " acmi.id = " +acmi.id);

        switch (id){
            case P.SPECIFIC_ID:
                Log.d(TAG, "SmetasMat onContextItemSelected case P.SPECIFIC_ID");

                switch (mViewPager.getCurrentItem()){
                    case 0:
                        //получаем имя категории из строки списка категории
                        TextView tvName = acmi.targetView.findViewById(R.id.base_text);
                        final String name = tvName.getText().toString();
                        //находим id категории по имени категории
                        final long cat_mat_id = CategoryMat.getIdFromName(database, name);
                        Log.d(TAG, "SmetasMat onContextItemSelected  " +
                                " name = " + name +  " cat_mat_id =" + cat_mat_id);

                        //отправляем интент с id категории
                        Intent intentSpecificCat = new Intent(
                                SmetasMat.this, SpecificCategoryMat.class);
                        intentSpecificCat.putExtra(P.ID_CATEGORY_MAT, cat_mat_id);
                        startActivity(intentSpecificCat);
                        break;
                    case 1:

                        //получаем имя типа из строки списка типов материала
                        TextView tvSpecificTypeMat = acmi.targetView.findViewById(R.id.base_text);
                        String type_mat_name_specific = tvSpecificTypeMat.getText().toString();
                        //находим id по имени типа
                        long type_mat_id_specific = TypeMat.getIdFromName(database, type_mat_name_specific);
                        Log.d(TAG, "SmetasMat onContextItemSelected case P.SPECIFIC_ID " +
                                "type_mat_name_specific = " + type_mat_name_specific +
                                " type_mat_id_specific =" + type_mat_id_specific);
                        //отправляем интент с id типа
                        Intent intentSpecificTypeMat = new Intent(SmetasMat.this, SpesificTypeMat.class);
                        intentSpecificTypeMat.putExtra(P.ID_TYPE_MAT, type_mat_id_specific);
                        startActivity(intentSpecificTypeMat);
                        break;

                    case 2:

                        Log.d(TAG, "SmetasMat onContextItemSelected case P.SPECIFIC_ID");
                        //получаем имя типа из строки списка материала
                        TextView tvSpecificMat = acmi.targetView.findViewById(R.id.base_text);
                        String mat_name_specific = tvSpecificMat.getText().toString();
                        //находим id по имени типа
                        long mat_id_specific = Mat.getIdFromName(database, mat_name_specific);
                        Log.d(TAG, "SmetasMat onContextItemSelected case P.SPECIFIC_ID " +
                                "mat_name_specific = " + mat_name_specific +  " mat_id_specific =" + mat_id_specific);
                        //отправляем интент с id материала
                        Intent intentSpecificMat = new Intent(SmetasMat.this, SpesificMat.class);
                        intentSpecificMat.putExtra(P.ID_MAT, mat_id_specific);
                        startActivity(intentSpecificMat);
                        break;
                }
                return true;

            case P.CHANGE_NAME_ID:
                Log.d(TAG, "SmetasMat onContextItemSelected case P.CHANGE_NAME_ID");
                switch (mViewPager.getCurrentItem()){
                    case 0:

                        //получаем имя категории из строки списка категории
                        TextView tvName = acmi.targetView.findViewById(R.id.base_text);
                        final String name = tvName.getText().toString();
                        //находим id категории по имени категории
                        final long cat_mat_id = CategoryMat.getIdFromName(database, name);
                        Log.d(TAG, "SmetasMat onContextItemSelected  " +
                                " name = " + name +  " cat_mat_id =" + cat_mat_id);

                        Intent intentCat = new Intent(
                                SmetasMat.this, ChangeDataCategoryMat.class);
                        intentCat.putExtra(P.ID_CATEGORY_MAT, cat_mat_id);
                        startActivity(intentCat);
                        break;

                    case 1:
                        //получаем имя типа из строки списка типов материала
                        TextView tvType = acmi.targetView.findViewById(R.id.base_text);
                        String tvTypeMatName = tvType.getText().toString();
                        //находим id по имени типа
                        long type_mat_id = TypeMat.getIdFromName(database, tvTypeMatName);
                        Log.d(TAG, "SmetasMat onContextItemSelected case P.SPECIFIC_ID " +
                                "tvTypeMatName = " + tvTypeMatName +
                                " type_mat_id =" + type_mat_id);

                        Intent intentType = new Intent(
                                SmetasMat.this, ChangeDataTypeMat.class);
                        intentType.putExtra(P.ID_TYPE_MAT, type_mat_id);
                        startActivity(intentType);
                        break;
                    case 2:
                        //получаем имя материала из строки списка типов материала
                        TextView tvChangWork = acmi.targetView.findViewById(R.id.base_text);
                        String mat_name_chang = tvChangWork.getText().toString();
                        //находим id по имени материала
                        long mat_id_Change = Mat.getIdFromName(database, mat_name_chang);
                        Log.d(TAG, "SmetasMat onContextItemSelected  case P.CHANGE_NAME_ID " +
                                "mat_name_chang = " + mat_name_chang + " mat_id_Change =" + mat_id_Change);
                        //отправляем интент с id материала
                        Intent intent = new Intent(SmetasMat.this, ChangeDataMat.class);
                        intent.putExtra(P.ID_MAT, mat_id_Change);
                        startActivity(intent);
                        break;
                }
                return true;

            case P.DELETE_ID:
                Log.d(TAG, "SmetasMat onContextItemSelected case P.DELETE_ID");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.Delete);
                builder.setPositiveButton(R.string.DeleteNo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton(R.string.DeleteYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (mViewPager.getCurrentItem()){
                            case 0:
                                //получаем имя категории из строки списка категории
                                TextView tvName = acmi.targetView.findViewById(R.id.base_text);
                                final String name = tvName.getText().toString();
                                //находим id категории по имени категории
                                final long cat_mat_id = CategoryMat.getIdFromName(database, name);
                                Log.d(TAG, "SmetasMat onContextItemSelected  P.DELETE_ID  case 0" +
                                        " name = " + name +  " cat_mat_id =" + cat_mat_id);
                                //Удаляем файл из таблицы CategoryMat когда в категории нет типов
                                //это проверили в onCreateContextMenu
                                CategoryMat.deleteObject(database, cat_mat_id);

                                // обновляем соседнюю вкладку типов материалов и показываем её
                                updateAdapter(0);
                                break;

                            case 1:
                                //получаем имя типа из строки списка типов
                                TextView tvType = acmi.targetView.findViewById(R.id.base_text);
                                final String type = tvType.getText().toString();
                                //находим id типа по имени типа
                                final long type_mat_id = TypeMat.getIdFromName(database, type);
                                Log.d(TAG, "SmetasMat onContextItemSelected  P.DELETE_ID  case 1" +
                                        " type = " + type +  " type_mat_id =" + type_mat_id);
                                //Удаляем файл из таблицы CategoryMat когда в категории нет типов
                                //это проверили в onCreateContextMenu
                                TypeMat.deleteObject(database, type_mat_id);

                                //после удаления в типемматериалов не даём появиться + в тулбаре
                                isSelectedCat = false;

                                // обновляем соседнюю вкладку типов материалов и показываем её
                                updateAdapter(0);
                                break;

                            case 2:
                                //получаем имя материала из строки списка материала
                                TextView tvmat = acmi.targetView.findViewById(R.id.base_text);
                                final String mat = tvmat.getText().toString();
                                //находим id типа по имени типа
                                final long mat_id = Mat.getIdFromName(database, mat);
                                Log.d(TAG, "SmetasMat onContextItemSelected  P.DELETE_ID  case 1" +
                                        " mat = " + mat +  " mat_id =" + mat_id);
                                //Удаляем файл из таблицы CategoryMat когда в категории нет типов
                                //это проверили в onCreateContextMenu
                                Mat.deleteObject(database, mat_id);
                                //Удаляем запись из таблицы CostWork когда в таблице FW нет такой  работы
                                // проверка в onCreateContextMenu
                                CostMat.deleteObject(database, mat_id);
                                //после удаления в материалах не даём появиться + в тулбаре
                                isSelectedType = false;

                                // обновляем соседнюю вкладку типов материалов и показываем её
                                updateAdapter(1);
                                break;
                        }
                    }
                });
                builder.show();
                return true;

            case P.CANCEL:
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

            Log.d(TAG, " ))))))))SmetasMat Fragment getItem ((((((((");
            switch (position){
                case 0:
                    Log.d(TAG, "SmetasMat  Fragment getItem case 0: " );
                    Tab1MatCat tab1Category = Tab1MatCat.NewInstance(file_id,position);
                    Log.d(TAG, "SmetasMat  Fragment getItem case 0: file_id = " +
                            file_id + "  position = " +  position);
                    return tab1Category;
                case 1:
                    Log.d(TAG, "SmetasMat  Fragment getItem case 1/1: " );
                    Tab2MatType tab2Type = Tab2MatType.NewInstance(
                            file_id, position, isSelectedCat, cat_id);
                    Log.d(TAG, "SmetasMat  Fragment getItem case 1/2: isSelectedCat = " +
                            isSelectedCat + "  cat_id = " +  cat_id + "  file_id = " +  file_id +
                            "  position = " +  position);
                    return tab2Type;
                case 2:
                    Log.d(TAG, "SmetasMat  Fragment getItem case 2/1: " );
                    //передаём во фрагмент данные (и способ их обработки) в зависимости от isSelectedType
                    Tab3Mat tab3Mat = Tab3Mat.NewInstance(
                            file_id, position, isSelectedType, type_id);
                    Log.d(TAG, "SmetasMat  Fragment getItem case 2/2: isSelectedType = " +
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
                    Intent intent = new Intent(SmetasMat.this, Smetas.class);
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

    private void updateAdapter(int currentItem){
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(currentItem);
        mSectionsPagerAdapter.notifyDataSetChanged();
    }
}
