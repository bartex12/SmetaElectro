package ru.bartex.smetaelectro;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
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
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.CategoryMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.CostMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.Mat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.TableControllerSmeta;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.TypeMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.UnitMat;

public class SmetasMatCost extends AppCompatActivity implements
        Tab1SmetasCatAbstrFrag.OnClickCatListener, Tab2SmetasTypeAbstrFrag.OnClickTypekListener,
        DialogSaveCostMat.OnCatTypeMatCostNameListener{

    public static final String TAG = "33333";
    long file_id;
    boolean isSelectedType = false;
    boolean isSelectedCatCost = false;
    long type_id;
    long cat_id;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    SmetaOpenHelper mSmetaOpenHelper;
    TableControllerSmeta tableControllerSmeta;
    File fileWork; //имя файла с данными по смете на работы

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
    public void typeAndClickTransmit(long cat_id,long type_id, boolean isSelectedType) {
        Log.d(TAG, "//  SmetasMatCost  typeAndClickTransmit  // " );
        this.isSelectedType = isSelectedType;
        this.type_id = type_id;
        this.cat_id = cat_id;
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
                long type_category_Id = tableControllerSmeta.getIdFromName(catName, CategoryMat.TABLE_NAME);

                long newTypeMatNameId = tableControllerSmeta.insertTypeCatName(
                        typeName, type_category_Id,TypeMat.TABLE_NAME);
                Log.d(TAG, "catTypeMatCostNameTransmit - catName = " + catName +
                        " typeName=" + typeName + " matName=" + matName +
                        " newTypeMatNameId=" + newTypeMatNameId);
                // обновляем адаптер
                updateAdapter(1);
                break;

            case 2:
                Log.d(TAG, "++++++++ SmetasMatCost  catTypeMatCostNameTransmit ++++++ case 2");

                float cost = Float.parseFloat(costOfMat);

                long unit_mat_id = tableControllerSmeta.getIdFromName(unit, UnitMat.TABLE_NAME);
                Log.d(TAG, "SmetasMatCost  unit_mat_id = " +
                        unit_mat_id + " cost = " + cost + " unit = " + unit
                        + " matName = " + matName  + " type_id = " + type_id) ;

                //Вставляем новый материал в таблице Mat
                long matID = mSmetaOpenHelper.insertMatName(matName, type_id);
                //обновляем цену материала с единицами измерения
                tableControllerSmeta.insertCost(matID, cost, unit_mat_id, CostMat.TABLE_NAME);

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
        tableControllerSmeta  = new TableControllerSmeta(this);

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
        getMenuInflater().inflate(R.menu.menu_work_cost, menu);
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
        }else if (id == R.id.action_send){
            ExportDatabaseCSVTask task=new ExportDatabaseCSVTask();
            task.execute();
            return true;
        }else if (id == R.id.action_add){
            int position = mViewPager.getCurrentItem();
            switch (position){
                case 0:
                    Log.d(TAG, " ))))))))SmetasMatCost  onOptionsItemSelected case 0");
                    DialogFragment saveCostCat = DialogSaveCostMat.NewInstance(
                            true, false, -1, -1);
                    saveCostCat.show(getSupportFragmentManager(),"saveCostCat");
                    break;

                case 1:
                    Log.d(TAG, " ))))))))SmetasMatCost  onOptionsItemSelected case 1");
                    DialogFragment saveCostType = DialogSaveCostMat.NewInstance(
                            false, true, cat_id, -1);
                    saveCostType.show(getSupportFragmentManager(),"saveCostType");
                    break;

                case 2:
                    Log.d(TAG, " ))))))))SmetasMatCost  onOptionsItemSelected case 2");
                    DialogFragment saveCostMat = DialogSaveCostMat.NewInstance(
                            false, false, cat_id, type_id);
                    saveCostMat.show(getSupportFragmentManager(),"saveCostMat");
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //создаём контекстное меню для списка
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
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
                long cat_mat_id = tableControllerSmeta.getIdFromName(name, CategoryMat.TABLE_NAME);
                //находим количество строк типов материала для cat_mat_id
                int countType_mat = mSmetaOpenHelper.getCountTypeMat(cat_mat_id);
                Log.d(TAG, "SmetasMatCost onCreateContextMenu - countType = " + countType_mat);
                if(countType_mat > 0) {
                    menu.findItem(P.DELETE_ID).setEnabled(false);
                }
                break;
            case 1:
                //получаем имя типа из строки списка типов материала
                TextView tvType = acmi.targetView.findViewById(R.id.base_text);
                String typeMatName = tvType.getText().toString();
                //находим id типа по имени типа
                long type_mat_id = tableControllerSmeta.getIdFromName(typeMatName, TypeMat.TABLE_NAME);
                //находим количество строк видов материала для type_mat_id
                int countLineMat = mSmetaOpenHelper.getCountLineMat(type_mat_id);
                Log.d(TAG, "SmetasMatCost onContextItemSelected - countLineMat = " + countLineMat);
                if(countLineMat > 0) {
                    menu.findItem(P.DELETE_ID).setEnabled(false);
                }
                break;
            case 2:
                //получаем имя материала  из строки списка видов материала
                TextView tvMat = acmi.targetView.findViewById(R.id.base_text);
                String matName = tvMat.getText().toString();
                //находим id вида  по имени вида материала
                long mat_id = tableControllerSmeta.getIdFromName(matName, Mat.TABLE_NAME);
                //находим количество строк видов материала в таблице FM для mat_id
                int countLineWorkFM = mSmetaOpenHelper.getCountLineMatInFM(mat_id);
                Log.d(TAG, "SmetasMatCost onContextItemSelected - countLineWorkFM = " + countLineWorkFM);
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
        Log.d(TAG, "SmetasMatCost onContextItemSelected id = " + id +
                " acmi.position = " + acmi.position + " acmi.id = " +acmi.id);

        switch (id){
            case P.DELETE_ID:
                Log.d(TAG, "SmetasMatCost onContextItemSelected case P.DELETE_ID");
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
                                final long cat_mat_id = tableControllerSmeta.getIdFromName(name, CategoryMat.TABLE_NAME);
                                Log.d(TAG, "SmetasMatCost onContextItemSelected  P.DELETE_ID  case 0" +
                                        " name = " + name +  " cat_mat_id =" + cat_mat_id);
                                //Удаляем файл из таблицы CategoryMat когда в категории нет типов
                                //это проверили в onCreateContextMenu
                                tableControllerSmeta.deleteObject(cat_mat_id, CategoryMat.TABLE_NAME);

                                // обновляем соседнюю вкладку типов материалов и показываем её
                                updateAdapter(0);
                                break;

                            case 1:
                                //получаем имя типа из строки списка типов
                                TextView tvType = acmi.targetView.findViewById(R.id.base_text);
                                final String type = tvType.getText().toString();
                                //находим id типа по имени типа
                                final long type_mat_id = tableControllerSmeta.getIdFromName(type, TypeMat.TABLE_NAME);
                                Log.d(TAG, "SmetasMatCost onContextItemSelected  P.DELETE_ID  case 1" +
                                        " type = " + type +  " type_mat_id =" + type_mat_id);
                                //Удаляем файл из таблицы CategoryMat когда в категории нет типов
                                //это проверили в onCreateContextMenu
                                tableControllerSmeta.deleteObject(type_mat_id, TypeMat.TABLE_NAME);

                                //после удаления в типемматериалов не даём появиться + в тулбаре
                                isSelectedCatCost = false;

                                // обновляем соседнюю вкладку типов материалов и показываем её
                                updateAdapter(0);
                                break;

                            case 2:
                                //получаем имя материала из строки списка материала
                                TextView tvmat = acmi.targetView.findViewById(R.id.base_text);
                                final String mat = tvmat.getText().toString();
                                //находим id типа по имени типа
                                final long mat_id = tableControllerSmeta.getIdFromName(mat, Mat.TABLE_NAME);
                                Log.d(TAG, "SmetasMatCost onContextItemSelected  P.DELETE_ID  case 1" +
                                        " mat = " + mat +  " mat_id =" + mat_id);
                                //Удаляем файл из таблицы CategoryMat когда в категории нет типов
                                //это проверили в onCreateContextMenu
                                tableControllerSmeta.deleteObject(mat_id, Mat.TABLE_NAME);
                                //Удаляем запись из таблицы CostWork когда в таблице FW нет такой  работы
                                // проверка в onCreateContextMenu
                                tableControllerSmeta.deleteObject(mat_id, CostMat.TABLE_NAME);
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
            Log.d(TAG, "####### SmetasMatCost  Fragment getItem: ####### " );
            switch (position){
                case 0:
                    Log.d(TAG, "####### SmetasMatCost  Fragment getItem case 0: ####### " );
                    Tab1MatCatCost smetasMatTab0Cat = Tab1MatCatCost.
                            NewInstance(file_id, position);
                    return smetasMatTab0Cat;
                case 1:
                    Log.d(TAG, "####### SmetasMatCost  Fragment getItem case 1: ####### " );
                    Tab2MatTypeCost smetasMatTab1Type = Tab2MatTypeCost.
                            NewInstance(file_id, position,isSelectedCatCost, cat_id);
                    return smetasMatTab1Type;

                case 2:
                    Log.d(TAG, "####### SmetasMatCost  Fragment getItem case 2/1: #######" );
                    //передаём во фрагмент данные (и способ их обработки) в зависимости от isSelectedType
                    Tab3MatCost smetasMatTab2Mat = Tab3MatCost.NewInstance(file_id, position, isSelectedType, type_id);
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

    public class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(SmetasMatCost.this);

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

            File exportDir = null;
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

            fileWork = new File(exportDir, "Rascenki_na_materialy.csv");

            try {
                fileWork.createNewFile();

                CSVWriter csvWrite = new CSVWriter(new FileWriter(fileWork));

                SQLiteDatabase db = mSmetaOpenHelper.getReadableDatabase();

                String selectMatName = " SELECT " + Mat.MAT_NAME +
                        " FROM " +  Mat.TABLE_NAME;
                Cursor curName = db.rawQuery(selectMatName, null);
                Log.d(TAG, "SmetasMatCost - doInBackground curName.getCount= " + curName.getCount());

                String [] titleNames = new String[]{"Название материала","Цена, руб"};
                csvWrite.writeNext(titleNames);

                Cursor curCost = null;

                while(curName.moveToNext()) {

                    String[] mySecondStringArray = new String[2];

                    // Узнаем индекс каждого столбца
                    int idColumnIndex = curName.getColumnIndex(Mat.MAT_NAME );
                    // Используем индекс для получения строки или числа
                    String currentMatName = curName.getString(idColumnIndex);

                    long workId = tableControllerSmeta.getIdFromName(currentMatName, Mat.TABLE_NAME);

                    String selectMatCost = " SELECT " + CostMat.COST_MAT_COST +
                            " FROM " +  CostMat.TABLE_NAME  +
                            " WHERE " + CostMat.COST_MAT_ID  + " = " + String.valueOf(workId);
                    curCost = db.rawQuery(selectMatCost, null);
                    Log.d(TAG, "SmetasMatCost - doInBackground curCost.getCount= " + curCost.getCount());

                    if (curCost.getCount() != 0) {
                        curCost.moveToFirst();
                        // Узнаем индекс каждого столбца
                        int idColumnIndexCost = curCost.getColumnIndex(CostMat.COST_MAT_COST);
                        // Используем индекс для получения числа
                        float currentCostFloat = curCost.getFloat(idColumnIndexCost);
                        //переводим число в строку
                        String currentCost = String.valueOf(currentCostFloat);

                        mySecondStringArray[0] = currentMatName;
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
                Log.e("SmetasMatCost", e.getMessage(), e);
                return false;
            }
        }

        protected void onPostExecute(final Boolean success) {
            if (this.dialog.isShowing()) { this.dialog.dismiss(); }
            if (success) {
                Toast.makeText(SmetasMatCost.this, "Export successful!", Toast.LENGTH_SHORT).show();

                //чтобы не крэшилось приложение при вызове
                // из-за использования file:// а не content:// в Uri для API>24
                StrictMode.VmPolicy.Builder builder1 = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder1.build());

                Uri u1  =   Uri.fromFile(fileWork);
                Log.d(TAG, "SmetasMatCost -  case R.id.action_send:  Uri u1 = " + u1);

                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Person Details");
                sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
                sendIntent.setType("text/html");
                startActivity(sendIntent);

            } else {
                Toast.makeText(SmetasMatCost.this, "Export failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
