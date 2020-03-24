package ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkcost;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
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
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import ru.bartex.smetaelectro.CSVWriter;
import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.CategoryWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.CostWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.FW;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.TypeWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.Unit;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.Work;
import ru.bartex.smetaelectro.ui.dialogs.DialogSaveCostWork;
import ru.bartex.smetaelectro.ui.main.MainActivity;
import ru.bartex.smetaelectro.ui.smetabefore.ListOfSmetasNames;
import ru.bartex.smetaelectro.ui.smetas2tabs.SmetasTab;
import ru.bartex.smetaelectro.ui.smetas3tabs.abstractfrag.AbstrSmetasWorkCostFrag;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetawork.WorkCat;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetawork.WorkName;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetawork.WorkType;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkpageadapter.SmetasWorkPagerAdapter;

public class SmetasWorkCost extends AppCompatActivity implements
        DialogSaveCostWork.OnCatTypeMatCostNameListener,
        AbstrSmetasWorkCostFrag.OnClickCatListener,
        AbstrSmetasWorkCostFrag.OnClickTypekListener{

    public static final String TAG = "33333";
    long file_id;
    boolean isSelectedType = false;
    boolean isSelectedCat =  false;
    long type_id;
    long cat_id;

    private SQLiteDatabase database;
    private ViewPager mViewPager;
    private SmetasWorkPagerAdapter adapter;
    private Fragment tab1WorkCatCost, tab2WorkTypeCost, tab3WorkWorkCost ;

    // private SectionsPagerAdapter mSectionsPagerAdapter;
    File fileWork; //имя файла с данными по смете на работы

    @Override
    public void catAndClickTransmit(long cat_id, boolean isSelectedCat) {
        Log.d(TAG, "//  SmetasWorkCost  catAndClickTransmit  // " );
        this.isSelectedCat = isSelectedCat;
        this.cat_id = cat_id;
        Log.d(TAG, "SmetasWorkCost  catAndClickTransmit cat_id =" +
                cat_id + "  isSelectedCat = " + isSelectedCat);

        adapter. updateWorkCostType(cat_id);
        updatePageAdapter(1);
    }

    @Override
    public void typeAndClickTransmit(long cat_mat_id, long type_mat_id, boolean isSelectedTypeMat) {
        Log.d(TAG, "//  SmetasWorkCost  typeAndCatTransmit  // " );
        this.isSelectedType = isSelectedTypeMat;
        this.type_id = type_mat_id;
        this.cat_id = cat_mat_id;
        Log.d(TAG, "SmetasWorkCost  typeAndCatTransmit cat_id ="  +
                cat_id + "  type_id" + type_id + "  isSelectedType = " + isSelectedType);

        adapter.updateWorkCostName(cat_id, type_id);
        updatePageAdapter(2);
    }

    @Override
    public void catTypeMatCostNameTransmit(
            String catName, String typeName, String matName, String costOfMat, String unit) {
        int position = mViewPager.getCurrentItem();
        Log.d(TAG, " ++++++++SmetasWorkCost  catTypeMatCostNameTransmit ++++++");
        switch (position){
            case 0:
                Log.d(TAG, "++++++++ SmetasWorkCost  catTypeMatCostNameTransmit ++++++ case 0");

                long newCatWorkCostId = CategoryWork.insertCategory(database, catName);
                Log.d(TAG, "catTypeMatCostNameTransmit - workName = " + matName +
                        " typeName=" + typeName + " catName=" + catName +  " newCatWorkCostId=" + newCatWorkCostId);

                // обновляем вкладку категорий работы и показываем её
                updatePageAdapter(0);
                break;

            case 1:
                Log.d(TAG, "++++++++ SmetasWorkCost  catTypeMatCostNameTransmit ++++++ case 1");
                //определяем id категории по её имени
                long type_category_Id = CategoryWork.
                        getIdFromName(database, catName);
                long newTypeNameId = TypeWork.insertTypeCatName(database,
                        typeName, type_category_Id);
                Log.d(TAG, "catTypeMatCostNameTransmit - workName = " + matName +
                        " typeName=" + typeName + " catName=" + catName +  " newTypeNameId=" + newTypeNameId);
                // обновляем вкладку типов работы и показываем её
                updatePageAdapter(1);
                break;

            case 2:
                Log.d(TAG, "++++++++ SmetasWorkCost  catTypeMatCostNameTransmit ++++++ case 2");

                float cost = Float.parseFloat(costOfMat);

                long unit_work_id = Unit.getIdFromName(database, unit);
                Log.d(TAG, "SmetasWorkCost  unit_work_id = " +
                        unit_work_id + " cost = " + cost + " unit = " + unit
                        + " workName = " + matName  + " type_id = " + type_id) ;

                //Вставляем новую работу в таблицу
                long workID = Work.insertTypeCatName(database, matName, type_id);
                //обновляем цену работы с единицами измерения
                CostWork.insertCost(database, workID, cost, unit_work_id);

                // обновляем адаптер
                updatePageAdapter(2);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smetas_work_cost);
        Log.d(TAG, " ))))))))SmetasWorkCost  onCreate((((((((  *****************");

        initDB();

        file_id = getIntent().getLongExtra(P.ID_FILE,-1);
        Log.d(TAG, "SmetasWorkCost onCreate file_id =" + file_id);

        initBottomNavigation();
        initToolbar();
        // фрагменты инициализируются здесь, чтобы не менять их при возврате из деталей
        createTabFrags();
        // адаптер, ViewPager инициализируются в onResume,
        // чтобы при возврате на SmetasWork из деталей происходило обновление пунктов списка

//
//
//        //в макете стоит GONE
//        FloatingActionButton fab = findViewById(R.id.fab_smetas_work);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//
//        //добираемся до списка фрагмента ___________пока нет_____________
//        //http://qaru.site/questions/2399151/get-child-views-of-the-current-selected-items-in-viewpager
//        View view = mViewPager.getChildAt(mViewPager.getCurrentItem());
//        Log.d(TAG, " SmetasWorkCost  onCreate mViewPager.getCurrentItem() = " +
//                mViewPager.getCurrentItem() + "  view = " + view );

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
        tab1WorkCatCost = WorkCatCost.newInstance(file_id, 0);
        tab2WorkTypeCost = WorkTypeCost.newInstance(file_id, 1, false, 0);
        tab3WorkWorkCost = WorkNameCost.newInstance(file_id, 2, false, 0);
    }

    private void initPageAdapter() {
        //Log.d(TAG, "//SmetasWork-initPageAdapter");
        //здесь используется вариант  добавления фрагментов из активити
        adapter = new SmetasWorkPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(tab1WorkCatCost, "Категория" );
        adapter.addFragment(tab2WorkTypeCost, "Тип" );
        adapter.addFragment(tab3WorkWorkCost, "Название" );
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
        //показываем заголовок в заголовке экрана
        toolbar.setTitle(R.string.title_activity_SmetasWorkCost);
        toolbar.setTitleTextColor(Color.GREEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, " ))))))))SmetasWorkCost  onCreateOptionsMenu(((((((( ///");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_work_cost, menu);
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

        if (id == R.id.action_settings) {
            return true;

        }else if (id == R.id.action_send){
            ExportDatabaseCSVTask task=new ExportDatabaseCSVTask();
            task.execute();
            return true;

        }else if (id == R.id.action_add){
            int position = mViewPager.getCurrentItem();
            Log.d(TAG, " ))))))))SmetasWorkCost  onOptionsItemSelected(((((((( position = " + position );
            switch (position){

                case 0:
                    Log.d(TAG, " ))))))))SmetasMatCost  onOptionsItemSelected case 0");
                    DialogFragment saveCostCat = DialogSaveCostWork.NewInstance(
                            true, false, -1, -1);
                    saveCostCat.show(getSupportFragmentManager(),"saveCostCat");
                    break;

                case 1:
                    Log.d(TAG, " ))))))))SmetasMatCost  onOptionsItemSelected case 1");
                    DialogFragment saveCostType = DialogSaveCostWork.NewInstance(
                            false, true, cat_id, -1);
                    saveCostType.show(getSupportFragmentManager(),"saveCostType");
                    break;

                case 2:
                    Log.d(TAG, " ))))))))SmetasMatCost  onOptionsItemSelected case 2");
                    DialogFragment saveCostMat = DialogSaveCostWork.NewInstance(
                            false, false, cat_id, type_id);
                    saveCostMat.show(getSupportFragmentManager(),"saveCostMat");
                    break;
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    //создаём контекстное меню для списка (сначала регистрация нужна  - здесь в onResume)
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        menu.add(0, P.DELETE_ITEM_SMETA, 0, "Удалить пункт");
//        menu.add(0, P.CANCEL, 0, "Отмена");
//
//        // получаем инфу о пункте списка
//        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
//
//        switch (mViewPager.getCurrentItem()){
//            case 0:
//                Log.d(TAG, "SmetasWorkCost c  case 0:");
//                //получаем имя категории из строки списка категории
//                TextView tvName = acmi.targetView.findViewById(R.id.base_text);
//                String name = tvName.getText().toString();
//                //находим id категории по имени категории
//                long cat_id = CategoryWork.getIdFromName(database, name);
//                //находим количество строк типов работы для cat_id
//                int countLineType = TypeWork.getCountLine(database, cat_id);
//                Log.d(TAG, "SmetasWorkCost onCreateContextMenu - countLineType = " + countLineType);
//                if(countLineType > 0) {
//                    menu.findItem(P.DELETE_ITEM_SMETA).setEnabled(false);
//                }
//                break;
//            case 1:
//                Log.d(TAG, "SmetasWorkCost onCreateContextMenu  case 1:");
//                //получаем имя типа из строки списка типов
//                TextView tvType = acmi.targetView.findViewById(R.id.base_text);
//                String typeName = tvType.getText().toString();
//                //находим id типа по имени типа
//                long type_id = TypeWork.getIdFromName(database, typeName);
//                //находим количество строк видов работы для type_id
//                int countLineWork = Work.getCountLine(database, type_id);
//                Log.d(TAG, "SmetasWorkCost onCreateContextMenu - countLineWork = " + countLineWork);
//                if(countLineWork > 0) {
//                    menu.findItem(P.DELETE_ITEM_SMETA).setEnabled(false); //так лучше
//                    //menu.findItem(P.DELETE_ID).setVisible(false);
//                }
//                break;
//
//            case 2:
//                Log.d(TAG, "SmetasWorkCost onCreateContextMenu  case 2:");
//                //получаем имя работы  из строки списка видов работ
//                TextView tvWork = acmi.targetView.findViewById(R.id.base_text);
//                String workName = tvWork.getText().toString();
//                //находим id вида  по имени вида работ
//                long work_id = Work.getIdFromName(database, workName);
//                //находим количество строк видов работы в таблице FW для work_id
//                int countLineWorkFW = FW.getCountLine(database, work_id);
//                //находим количество строк расценок работы в таблице CostWork для work_id
//                int countCostLineWork = CostWork.getCountLine(database, work_id);
//                Log.d(TAG, "SmetasWorkCost onCreateContextMenu - countLineWorkFW = " + countLineWorkFW +
//                        " countCostLineWork =" + countCostLineWork);
//
//               // mSmetaOpenHelper.displayTableCost();
//
//                if(countLineWorkFW > 0) {
//                    menu.findItem(P.DELETE_ITEM_SMETA).setEnabled(false); //так лучше
//                    //menu.findItem(P.DELETE_ID).setVisible(false);
//                }
//                break;
//        }
//    }
//
//    //практически полное повторение метода из SmetasWork
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//
//        final AdapterView.AdapterContextMenuInfo acmi =
//                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//
//        //если удалить из контекстного меню
//        if (item.getItemId() == P.DELETE_ITEM_SMETA) {
//
//            Log.d(TAG, "SmetasWorkCost P.DELETE_ITEM_SMETA");
//            AlertDialog.Builder builder = new AlertDialog.Builder(SmetasWorkCost.this);
//            builder.setTitle(R.string.Delete_Item);
//            builder.setPositiveButton(R.string.DeleteNo, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                }
//            });
//            builder.setNegativeButton(R.string.DeleteYes, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Log.d(TAG, "SmetasWorkCost P.DELETE_ITEM_SMETA acmi.position  =" +
//                            (acmi.position));
//
//                    TextView tv = acmi.targetView.findViewById(R.id.base_text);
//                    String name = tv.getText().toString();
//                    switch (mViewPager.getCurrentItem()){
//                        case 0:
//                            Log.d(TAG, "SmetasWorkCost onContextItemSelected  P.DELETE_ID case 0");
//                            TextView tvCat = acmi.targetView.findViewById(R.id.base_text);
//                            String cat_name = tvCat.getText().toString();
//                            //находим id по имени файла
//                            long cat_id = CategoryWork.getIdFromName(database, cat_name);
//                            Log.d(TAG, "SmetasWorkCost onContextItemSelected case P.DELETE_ID" +
//                                    " cat_name = " + cat_name + " cat_id =" + cat_id);
//                            //Удаляем файл из таблицы CategoryWork когда в категории нет типов
//                            CategoryWork.deleteObject(database, cat_id);
//                            // обновляем соседнюю вкладку категорий работы и показываем её
//                            updatePageAdapter(0);
//                            break;
//
//                        case 1:
//                            Log.d(TAG, "SmetasWorkCost onContextItemSelected  P.DELETE_ID case 1");
//                            TextView tvType = acmi.targetView.findViewById(R.id.base_text);
//                            String type_name = tvType.getText().toString();
//                            //находим id по имени файла
//                            long type_id = TypeWork.getIdFromName(database, type_name);
//                            Log.d(TAG, "SmetasWorkCost onContextItemSelected case P.DELETE_ID" +
//                                    " type_name = " + type_name + " type_id =" + type_id);
//                            //Удаляем файл из таблицы TypeWork когда в типе нет видов работ
//                            TypeWork.deleteObject(database, type_id);
//
//                            //после удаления в типе работ не даём появиться + в тулбаре
//                            isSelectedCat = false;
//                            // обновляем соседнюю вкладку типов работы и показываем её
//                            updatePageAdapter(0);
//                            break;
//                        case 2:
//                            Log.d(TAG, "SmetasWorkCost P.DELETE_ITEM_SMETA case 2");
//                            //находим id по имени работы
//                            long work_id = Work.getIdFromName(database, name);
//                            Log.d(TAG, "SmetasWorkCost onContextItemSelected file_id = " +
//                                    file_id + " work_id =" + work_id+ " work_name =" + name);
//
//                            CostWork.displayTable(database);
//                            //Удаляем запись из таблицы Work когда в таблице FW нет такой  работы
//                            // проверка в onCreateContextMenu
//                            Work.deleteObject(database, work_id);
//                            //Удаляем запись из таблицы CostWork когда в таблице FW нет такой  работы
//                            CostWork.deleteObject(database, work_id);
//                            // проверка в onCreateContextMenu
//                            CostWork.displayTable(database);
//                            //после удаления в работах не даём появиться + в тулбаре
//                            isSelectedType = false;
//                            //обновляем данные списка фрагмента активности
//                            updatePageAdapter(1);
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
//        return super.onContextItemSelected(item);
//    }

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
                        Intent intent = new Intent(SmetasWorkCost.this, SmetasTab.class);
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



    public class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(SmetasWorkCost.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Exporting database...");
            this.dialog.show();
        }

        protected Boolean doInBackground(final String... args) {
            //получаем путь к предопределённой папке для документов
            //такой код в андроид 7 не работает путь: /storage/emulated/0/Documents
            //File exportDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            //а вот такой - работает  путь:  /storage/emulated/0/Android/data/ru.bartex.smetaelectro/files/Documents
            //File exportDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

            File exportDir ;
            if (Build.VERSION.SDK_INT >= 24){
                Log.d(TAG, "Build.VERSION >= 24");
                exportDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            }else {
                Log.d(TAG, "Build.VERSION < 24");
                String path = Environment.getExternalStorageDirectory() + "/SmetaElectro";
                exportDir = new File (path);
            }
            Log.d(TAG, "Build.VERSION.SDK_INT = " + Build.VERSION.SDK_INT );
            Log.d(TAG, "exportDir.getAbsolutePath = " + exportDir.getAbsolutePath());

            if(!exportDir.exists()) {
                exportDir.mkdirs();
            }

            fileWork = new File(exportDir, "Rascenki_na_rabotu.csv");

            try {
                fileWork.createNewFile();

                CSVWriter csvWrite = new CSVWriter(new FileWriter(fileWork));



                String selectWorkName = " SELECT " + Work.WORK_NAME +
                        " FROM " +  Work.TABLE_NAME;
                Cursor curName = database.rawQuery(selectWorkName, null);
                Log.d(TAG, "SmetasWorkCost - doInBackground curName.getCount= " + curName.getCount());

                String [] titleNames = new String[]{"Название работы","Цена, руб"};
                csvWrite.writeNext(titleNames);

                Cursor curCost = null;

                while(curName.moveToNext()) {

                    String[] mySecondStringArray = new String[2];

                    // Узнаем индекс каждого столбца
                    int idColumnIndex = curName.getColumnIndex(Work.WORK_NAME);
                    // Используем индекс для получения строки или числа
                    String currentWorkName = curName.getString(idColumnIndex);
                    long workId = Work.getIdFromName(database, currentWorkName);

                    String selectWorkCost = " SELECT " + CostWork.COST_COST +
                            " FROM " +  CostWork.TABLE_NAME  +
                            " WHERE " + CostWork.COST_WORK_ID + " = " + workId;
                    curCost = database.rawQuery(selectWorkCost, null);
                    Log.d(TAG, "SmetasWorkCost - doInBackground curCost.getCount= " + curCost.getCount());

                    if (curCost.getCount() != 0) {
                        curCost.moveToFirst();
                        // Узнаем индекс каждого столбца
                        int idColumnIndexCost = curCost.getColumnIndex(CostWork.COST_COST);
                        // Используем индекс для получения числа
                        float currentCostFloat = curCost.getFloat(idColumnIndexCost);
                        //переводим число в строку
                        String currentCost = String.valueOf(currentCostFloat);

                        mySecondStringArray[0] = currentWorkName;
                        mySecondStringArray[1] = currentCost;

                        csvWrite.writeNext(mySecondStringArray);
                    }
                }

                csvWrite.close();
                curName.close();
                if (curCost!=null){
                    curCost.close();
                }

                return true;
            } catch (IOException e) {
                Log.e("SmetasWorkCost", e.getMessage(), e);
                return false;
            }
        }

        protected void onPostExecute(final Boolean success) {
            if (this.dialog.isShowing()) { this.dialog.dismiss(); }
            if (success) {
                Toast.makeText(SmetasWorkCost.this, "Export successful!", Toast.LENGTH_SHORT).show();

                //чтобы не крэшилось приложение при вызове
                // из-за использования file:// а не content:// в Uri для API>24
                StrictMode.VmPolicy.Builder builder1 = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder1.build());

                Uri u1  =   Uri.fromFile(fileWork);
                Log.d(TAG, "SmetasWorkCost -  case R.id.action_send:  Uri u1 = " + u1);

                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Person Details");
                sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
                sendIntent.setType("text/html");
                startActivity(sendIntent);

            } else {
                Toast.makeText(SmetasWorkCost.this, "Export failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updatePageAdapter(int currentItem){
        mViewPager.setCurrentItem(currentItem);
        adapter.notifyDataSetChanged();
    }
}
