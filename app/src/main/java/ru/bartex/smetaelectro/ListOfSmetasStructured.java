package ru.bartex.smetaelectro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.FM;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.FW;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;

public class ListOfSmetasStructured extends AppCompatActivity {

    public static final String TAG = "33333";

    LinearLayout llWork;
    ListView mListViewNames;
    SmetaOpenHelper mSmetaOpenHelper;
    SimpleAdapter sara;
    long file_id;
    View header;
    View footer;
    int position_tab;

    float[] work_summas; //массив стоимости работ
    float totalSumma; // общая стоимость работ по смете
    float[]  mat_summas; //массив стоимости материалов
    float totalSummaMat; // общая стоимость материалов по смете

    ArrayList<Map<String, String>> data;
    //ArrayList<Map<String, String>> dataMat;
    //список для имён, цены и других параметров работы
    Map<String,String> mm;

    File fileWork; //имя файла с данными по смете на работы

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smeta_list_structured);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_smetas_list);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mSmetaOpenHelper = new SmetaOpenHelper(this);
        file_id = getIntent().getExtras().getLong(P.ID_FILE);
        position_tab = getIntent().getExtras().getInt(P.TAB_POSITION);
        Log.d(TAG, "ListOfSmetasStructured - onCreate file_id = " + file_id  + "  position_tab = " + position_tab);

        llWork = findViewById(R.id.llWork);

        mListViewNames = findViewById(R.id.listViewSmetasRabota);
        mListViewNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_of_smetas_structured, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_send:
                ExportDatabaseCSVTask task=new ExportDatabaseCSVTask();
                task.execute();
                return true;

            case R.id.action_settings:

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // Для данного варианта в манифесте указан режим singlTask для активности MainActivity
                    Intent intentHome = new Intent(ListOfSmetasStructured.this, MainActivity.class);
                    // установка флагов- создаём новую задачу и убиваем старую вместе
                    // со всеми предыдущими переходами - работает дёргано по сравнению с манифестом
                    //intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    //      Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentHome);
                    return true;
                case R.id.navigation_smetas:
                    // Для данного варианта в манифесте указан режим singlTask для активности ListOfSmetasNames
                    Intent intent_smetas = new Intent(ListOfSmetasStructured.this, ListOfSmetasNames.class);
                    startActivity(intent_smetas);
                    return true;
                case R.id.navigation_costs:
                    if (position_tab == 0){
                        Intent intent_costs_work = new Intent(ListOfSmetasStructured.this, SmetasWorkCost.class);
                        startActivity(intent_costs_work);
                    }else if (position_tab == 1){
                        Intent intent_costs_mat = new Intent(ListOfSmetasStructured.this, SmetasMatCost.class);
                        intent_costs_mat.putExtra(P.ID_FILE, file_id);
                        startActivity(intent_costs_mat);
                    }

                    return true;
            }
            return false;
        }
    };

    public void updateAdapter() {

        String fileName = mSmetaOpenHelper.getFileNameById(file_id);
        switch (position_tab){
            case 0:
                data = new  ArrayList<Map<String, String>>();

                Log.d(TAG, "ListOfSmetasStructured - updateAdapter case 0 /////////////////////");
                //Массив категорий работ для сметы с file_id
                String[] cat_name = mSmetaOpenHelper.getCategoryNamesFW(file_id);
                Log.d(TAG, "ListOfSmetasStructured - updateAdapter  cat_name.length = " + cat_name.length);
                //массив типов работ для сметы с file_id
                String[] type_name = mSmetaOpenHelper.getTypeNamesFWSort(file_id);
                Log.d(TAG, "ListOfSmetasStructured - updateAdapter  type_name.length = " + type_name.length);

                //получаем суммарную стоимомть работ  по смете с id файла file_id
                work_summas = mSmetaOpenHelper.getSummaOfWork(file_id);

                int length = type_name.length;
                if (length >0){
                    for (int i = 0; i<length; i++){
                        mm = new HashMap<>();
                        mm.put("typeName", (i+1) + " " + type_name[i]);
                        data.add(mm);
                        Log.d(TAG, "ListOfSmetasStructured - updateAdapter  data.size()1 = " + data.size());

                        long type_id = mSmetaOpenHelper.getIdFromTypeName(type_name[i]);
                        Log.i(TAG, "ListOfSmetasStructured updateAdapter type_name[i] = " +
                                type_name[i] + " type_id = " + type_id);

                        //получаем имена работ  по смете с id файла file_id и id типа type_id
                        String[] work_names = mSmetaOpenHelper.getNameOfWorkSelectedType(file_id, type_id);
                        //получаем расценки работ  по смете с id файла file_idи id типа type_id
                        float[] work_cost = mSmetaOpenHelper.getCostOfWorkSelectedType(file_id, type_id);
                        //получаем количество работ  по смете с id файла file_id и id типа type_id
                        float[] work_amount = mSmetaOpenHelper.getAmountOfWorkSelectedType(file_id, type_id);
                        //получаем единицы измерения для  работ  по смете с id файла file_id
                        String[]  work_units = mSmetaOpenHelper.getUnitsOfWorkSelectedType(file_id, type_id);
                        //получаем стоимомть работ  по смете с id файла file_id и id типа type_id
                        float[] work_summa =  mSmetaOpenHelper.getSummaOfWorkSelectedType(file_id, type_id);

                        for (int k = 0; k<work_names.length; k++){
                            mm = new HashMap<>();
                            //mm.put("typeName", (i+1) + " " + type_name[i]);
                            mm.put("workName", "   " + (i+1)+ "." + (k+1) + " " + work_names[k]);
                            mm.put("workCost", Float.toString(work_cost[k]));
                            mm.put("workАmount", Float.toString(work_amount[k]));
                            mm.put("workUnit", work_units[k]);
                            mm.put("workSumma", Float.toString(work_summa[k]));
                            data.add(mm);
                            Log.d(TAG, "ListOfSmetasStructured - updateAdapter  data.size()2 = " + data.size());
                        }
                    }
                }
                //***************************Header and Footer***************
                mListViewNames.removeHeaderView(header);
                //добавляем хедер
                header = getLayoutInflater().inflate(R.layout.list_item_single, null);
                ((TextView)header.findViewById(R.id.base_text)).setText(
                        String.format(Locale.ENGLISH,"Смета на работу:   %s", fileName));
                mListViewNames.addHeaderView(header, null, false);
                Log.d(TAG, "***********getHeaderViewsCount*********** = " +
                        mListViewNames.getHeaderViewsCount());
                //***********************************************************
                mListViewNames.removeFooterView(footer);
                Log.d(TAG, "*********  removeFooterView2  ********* ");
                //добавляем футер
                footer = getLayoutInflater().inflate(R.layout.list_item_single, null);
                totalSumma = P.updateTotalSumma(work_summas);
                Log.d(TAG, "ListOfSmetasStructured - updateAdapter  totalSumma = " + totalSumma);
                ((TextView)footer.findViewById(R.id.base_text)).
                        setText(String.format(Locale.ENGLISH,"За работу: %.0f руб", totalSumma ));
                mListViewNames.addFooterView(footer, null, false);
                Log.d(TAG, "*********  addFooterView getFooterViewsCount1 = " +
                        mListViewNames.getFooterViewsCount());
                //***************************Header and Footer***************
                break;

            case 1:

                data = new  ArrayList<Map<String, String>>();

                Log.d(TAG, "ListOfSmetasStructured - updateAdapter  case 1 /////////////////////");
                //Массив категорий материалов для сметы с file_id
                String[] cat_mat_name = mSmetaOpenHelper.getCategoryNamesFM(file_id);
                Log.d(TAG, "ListOfSmetasStructured - updateAdapter  cat_mat_name.length = " + cat_mat_name.length);
                //массив типов материалов для сметы с file_id
                //String[] type_mat_name = mSmetaOpenHelper.getTypeNamesFM(file_id);
                String[] type_mat_name = mSmetaOpenHelper.getTypeNamesFMSort(file_id);
                Log.d(TAG, "ListOfSmetasStructured - updateAdapter  type_name.length = " + type_mat_name.length);

                //получаем суммарную стоимомть материалов  по смете с id файла file_id
                mat_summas = mSmetaOpenHelper.getSummaOfMat(file_id);

                int length_mat = type_mat_name.length;
                if (length_mat >0){
                    for (int i = 0; i<length_mat; i++){
                        mm = new HashMap<>();
                        mm.put("typeName", (i+1) + " " + type_mat_name[i]);
                        data.add(mm);
                        Log.d(TAG, "ListOfSmetasStructured - updateAdapter  data.size()1 = " + data.size());

                        long type_mat_id = mSmetaOpenHelper.getIdFromMatTypeName(type_mat_name[i]);
                        Log.i(TAG, "ListOfSmetasStructured updateAdapter type_mat_name[i] = " +
                                type_mat_name[i] + " type_mat_id = " + type_mat_id);

                        //получаем имена материалов  по смете с id файла file_id и id типа type_mat_name
                        String[] mat_names = mSmetaOpenHelper.getNameOfMatkSelectedType(file_id, type_mat_id);
                        //получаем расценки материалов  по смете с id файла file_idи id типа type_id
                        float[] mat_cost = mSmetaOpenHelper.getCostOfMatSelectedType(file_id, type_mat_id);
                        //получаем количество работ  по смете с id файла file_id и id типа type_id
                        float[] mat_amount = mSmetaOpenHelper.getAmountOfMatSelectedType(file_id, type_mat_id);
                        //получаем единицы измерения для  работ  по смете с id файла file_id
                        String[]  mat_units = mSmetaOpenHelper.getUnitsOfMatSelectedType(file_id, type_mat_id);
                        //получаем стоимомть работ  по смете с id файла file_id и id типа type_id
                        float[] mat_summa =  mSmetaOpenHelper.getSummaOfMatSelectedType(file_id, type_mat_id);

                        for (int k = 0; k<mat_names.length; k++){
                            mm = new HashMap<>();
                            //mm.put("typeName", (i+1) + " " + type_name[i]);
                            mm.put("workName", "   " + (i+1)+ "." + (k+1) + " " + mat_names[k]);
                            mm.put("workCost", Float.toString(mat_cost[k]));
                            mm.put("workАmount", Float.toString(mat_amount[k]));
                            mm.put("workUnit", mat_units[k]);
                            mm.put("workSumma", Float.toString(mat_summa[k]));
                            data.add(mm);
                            Log.d(TAG, "ListOfSmetasStructured - updateAdapter  data.size()2 = " + data.size());
                        }
                    }
                }
                //***************************Header and Footer***************
                mListViewNames.removeHeaderView(header);
                //добавляем хедер
                header = getLayoutInflater().inflate(R.layout.list_item_single, null);
                ((TextView)header.findViewById(R.id.base_text)).setText(
                        String.format(Locale.ENGLISH,"Смета на материалы:   %s", fileName));
                mListViewNames.addHeaderView(header, null, false);
                Log.d(TAG, "***********getHeaderViewsCount*********** = " +
                        mListViewNames.getHeaderViewsCount());
                //***********************************************************
                mListViewNames.removeFooterView(footer);
                Log.d(TAG, "*********  removeFooterView2  ********* ");
                //добавляем футер
                footer = getLayoutInflater().inflate(R.layout.list_item_single, null);
                totalSummaMat = P.updateTotalSumma(mat_summas);
                Log.d(TAG, "ListOfSmetasStructured - updateAdapter  totalSumma = " + totalSumma);
                ((TextView)footer.findViewById(R.id.base_text)).
                        setText(String.format(Locale.ENGLISH,"За материалы: %.0f руб", totalSummaMat ));
                mListViewNames.addFooterView(footer, null, false);
                Log.d(TAG, "*********  addFooterView getFooterViewsCount1 = " +
                        mListViewNames.getFooterViewsCount());
                //***************************Header and Footer***************
                break;
                default:
                    break;
        }

        // список атрибутов элементов для чтения
        String[] from = new String[]{"typeName","workName", "workCost", "workАmount",
                "workUnit", "workSumma", "typeName"};
        // список ID view-элементов, в которые будет помещены атрибуты элементов
        int[] to = new int[]{R.id.base_text_type, R.id.base_text, R.id.tvCost, R.id.tvAmount,
                R.id.tvUnits, R.id.tvSumma, R.id.llWork};

        // создаем адаптер
        sara =  new SimpleAdapter(this, data, R.layout.list_item_complex_group, from, to);
        // Указываем адаптеру свой биндер
        sara.setViewBinder(new MyViewBinder());
        mListViewNames.setAdapter(sara);
    }

    class MyViewBinder implements SimpleAdapter.ViewBinder {

        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {

            switch (view.getId()) {
                case R.id.base_text_type:
                    String s = (String) data;
                    Log.d(TAG, "ListOfSmetasStructured - MyViewBinder s = " + s);
                    if (s == null) {
                        view.setVisibility(View.GONE);
                    } else {
                        view.setVisibility(View.VISIBLE);
                    }
                    //для продолжения обработки - текстовое поле - ставим false
                    return false;

                case R.id.llWork:
                    Log.d(TAG, "ListOfSmetasStructured - MyViewBinder view.getId() == R.id.llWork /////");
                    String s1 = (String) data;
                    //Log.d(TAG, "ListOfSmetasStructured - MyViewBinder s = " + s1 );
                    if (s1 == null) {
                        view.setVisibility(View.VISIBLE);
                        Log.d(TAG, "ListOfSmetasStructured - MyViewBinder s == null  View.VISIBLE");
                    } else {
                        view.setVisibility(View.GONE);
                        Log.d(TAG, "ListOfSmetasStructured - MyViewBinder s == null View.GONE");
                    }
                    //так как адаптер не умеет обрабатывать LinearLayout, ставим true. прекращая обработку
                    return true;
            }
            //чтобы продолжать обрабатывать другие View, ставим  false
            return false;
        }
    }

    public class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {

        private final ProgressDialog dialog = new ProgressDialog(ListOfSmetasStructured.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Exporting database...");
            this.dialog.show();
        }

        protected Boolean doInBackground(final String... args) {
            String currentDBPath = "/data/"+ "your Package name" +"/databases/abc.db";
            File dbFile = getDatabasePath(""+currentDBPath);

            Log.d(TAG, "ListOfSmetasStructured - doInBackground currentDBPath = " + dbFile);
            File exportDir = new File(Environment.getExternalStorageDirectory(), "/Folder_Name/");

            if (!exportDir.exists()) { exportDir.mkdirs(); }
            if (position_tab==0){
                fileWork = new File(exportDir, "Smeta_na_rabotu.csv");
            }else if (position_tab == 1){
                fileWork = new File(exportDir, "Smeta_na_materialy.csv");
            }

            try {
                fileWork.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(fileWork));

                SQLiteDatabase db = mSmetaOpenHelper.getReadableDatabase();

                Cursor curStroka = null;
                Cursor curTypeSort = null;
                String[] titleSmeta = new String[]{};
                String[] pusto = new String[]{"","","",""};
                String[] summarno = new String[]{};

                //если вошли с вкладки работы
    if (position_tab==0){
        titleSmeta = new String[]{"          Смета на работу.", "", "", ""};
        summarno = new String[]{"     Итого за работу:", "","",
                String.format(Locale.ENGLISH,"%.0f руб", totalSumma)};
        //если вошли с вкладки материалы
    }else if (position_tab == 1){
        titleSmeta = new String[]{"          Смета на материалы.", "", "", ""};
        summarno = new String[]{"     Итого за материалы:", "","",
                String.format(Locale.ENGLISH,"%.0f руб", totalSummaMat)};
    }
                String [] titleNames = new String[]{"Название","Цена","Кол-во","Сумма"};
                //пишем заголовки таблицы
               //csvWrite.writeNext(curCSV.getColumnNames());
                csvWrite.writeNext(titleNames);
                //пишем смета на работу/материалы
                csvWrite.writeNext(titleSmeta);
                //пишем пустую строку
                csvWrite.writeNext(pusto);

                //если вошли с вкладки работы
                if (position_tab==0){

                    //получаем курсор с именами типов работы
                    curTypeSort = db.query(
                            true,
                            FW.TABLE_NAME,   // таблица
                            new String[]{FW.FW_TYPE_NAME, FW.FW_TYPE_ID},            // столбцы
                            FW.FW_FILE_ID  + "=?",                  // столбцы для условия WHERE
                            new String[]{String.valueOf(file_id)},                  // значения для условия WHERE
                            null,                  // Don't group the rows
                            null,                  // Don't filter by row groups
                            FW.FW_TYPE_ID,                 // порядок сортировки
                            null);

                    Log.i(TAG, "ListOfSmetasStructured doInBackground position_tab=0 curTypeSort.getCount() = " +
                            curTypeSort.getCount());

                    //для каждого имени типа работы
                while (curTypeSort.moveToNext()) {
                    //получаем имя типа работы из курсора
                    String typeName = curTypeSort.getString(curTypeSort.getColumnIndex(FW.FW_TYPE_NAME));
                    //вставляем имя типа в строковый массив
                    String[] typeNameArray = new String[]{"     "+typeName, "", "", ""};
                    //выводим имя типа работы в таблицу - надо сделать через настройки вывод типа
                    //csvWrite.writeNext(typeNameArray);

                    //получаем id типа работы ...
                    long type_id = mSmetaOpenHelper.getIdFromTypeName(typeName);
                    Log.i(TAG, "ListOfSmetasStructured doInBackground position_tab=0 type_name = " +
                            typeName + " type_id = " + type_id);

                    //получаем курсор с данными сметы с file_id для типа работ с type_id
                    curStroka = db.query(
                            FW.TABLE_NAME,   // таблица
                            new String[]{FW.FW_WORK_NAME, FW.FW_COST, FW.FW_COUNT, FW.FW_SUMMA},  // столбцы
                            FW.FW_FILE_ID + "=?" + " AND " + FW.FW_TYPE_ID + "=?", // столбцы для условия WHERE
                            new String[]{String.valueOf(file_id), String.valueOf(type_id)}, // значения для условия WHERE
                            null,                  // Don't group the rows
                            null,                  // Don't filter by row groups
                            FW.FW_WORK_ID);                   // порядок сортировки
                    Log.i(TAG, "ListOfSmetasStructured doInBackground position_tab=0 curStroka.getCount() = " +
                            curStroka.getCount());

                    //...выводим все строки с названием, ценой, количеством и суммой
                    while (curStroka.moveToNext()) {

                        String[] myStrokaStringArray = new String[curStroka.getColumnNames().length];
                        for (int i = 0; i < curStroka.getColumnNames().length; i++) {
                            myStrokaStringArray[i] = curStroka.getString(i);
                        }
                        csvWrite.writeNext(myStrokaStringArray);
                    }
                    //пишем пустую строку
                    csvWrite.writeNext(pusto);
                }
                    //если вошли с вкладки материалы
                    }else if (position_tab == 1){

                    //получаем курсор с именами типов материала
                        curTypeSort = db.query(
                                true,
                                FM.TABLE_NAME,   // таблица
                                new String[]{FM.FM_MAT_TYPE_NAME, FM.FM_MAT_TYPE_ID}, // столбцы
                                FM.FM_FILE_ID  + "=?", // столбцы для условия WHERE
                                new String[]{String.valueOf(file_id)}, // значения для условия WHERE
                                null,                  // Don't group the rows
                                null,                  // Don't filter by row groups
                                FM.FM_MAT_TYPE_ID,              // порядок сортировки
                                null);
                        Log.i(TAG, "ListOfSmetasStructured doInBackground position_tab=1 curTypeSort.getCount() = " +
                                curTypeSort.getCount());

                    //для каждого имени типа материала
                    while (curTypeSort.moveToNext()) {

                        //получаем имя типа материала из курсора
                        String typeName = curTypeSort.getString(curTypeSort.getColumnIndex(FM.FM_MAT_TYPE_NAME));
                        //вставляем имя типа в строковый массив
                        String[] typeNameArray = new String[]{"     "+typeName, "", "", ""};
                        //выводим имя типа материала в таблицу - надо сделать через настройки вывод типа
                        //csvWrite.writeNext(typeNameArray);

                        //получаем id типа материала...
                        long type_id = mSmetaOpenHelper.getIdFromMatTypeName(typeName);
                        Log.i(TAG, "ListOfSmetasStructured doInBackground position_tab=1 type_name = " +
                                typeName + " type_id = " + type_id);

                        //получаем курсор с данными сметы с file_id для типа материала с type_id
                        curStroka = db.query(
                                FM.TABLE_NAME,   // таблица
                                new String[]{FM.FM_MAT_NAME,FM.FM_MAT_COST,FM.FM_MAT_COUNT,FM.FM_MAT_SUMMA},// столбцы
                                FM.FM_FILE_ID + "=?"+ " AND " + FM.FM_MAT_TYPE_ID + "=?",// столбцы для условия WHERE
                                new String[]{String.valueOf(file_id), String.valueOf(type_id)}, // значения для условия WHERE
                                null,                  // Don't group the rows
                                null,                  // Don't filter by row groups
                                FM.FM_MAT_ID);                   // порядок сортировки
                        Log.i(TAG, "ListOfSmetasStructured doInBackground position_tab=1 curStroka.getCount() = " +
                                curStroka.getCount());

                        //...выводим все строки с названием, ценой, количеством и суммой
                        while(curStroka.moveToNext()) {

                            String[] myStrokaStringArray = new String[curStroka.getColumnNames().length];
                            for(int i=0; i<curStroka.getColumnNames().length; i++)
                            {
                                myStrokaStringArray[i] =curStroka.getString(i);
                            }
                            csvWrite.writeNext(myStrokaStringArray);
                        }
                        //пишем пустую строку
                        csvWrite.writeNext(pusto);
                    }
                }

                csvWrite.writeNext(pusto);
                csvWrite.writeNext(summarno);

                csvWrite.close();
                curStroka.close();
                curTypeSort.close();

                return true;

            } catch (IOException e) {
                Log.e("ListOfSmetasStructured", e.getMessage(), e);
                return false;
            }
        }

        protected void onPostExecute(final Boolean success) {
            if (this.dialog.isShowing()) { this.dialog.dismiss(); }
            if (success) {
                Toast.makeText(ListOfSmetasStructured.this, "Export successful!", Toast.LENGTH_SHORT).show();

                Uri u1  =   Uri.fromFile(fileWork);
                Log.d(TAG, "ListOfSmetasStructured -  onPostExecute  Uri u1 = " + u1);

                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Person Details");
                sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
                sendIntent.setType("text/html");
                startActivity(sendIntent);

            } else {
                Toast.makeText(ListOfSmetasStructured.this, "Export failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

/*
    //для примера оставляю
    public void updateExpandableAdapter() {

        //Массив категорий работ для сметы с file_id
        //String[] cat_name = mSmetaOpenHelper.getCategoryNamesFW(file_id);

        //массив типов работ для сметы с file_id
        String[] type_name = mSmetaOpenHelper.getTypeNamesFW(file_id);
        Log.d(TAG, "SmetasTab1Rabota - updateAdapter  type_name.length = " + type_name.length);

        //создаём список групп
        groupData = new ArrayList<Map<String,String>>();

        for (String type:type_name) {
            mm = new HashMap<String, String>();
            mm.put("groupName", type);
            groupData.add(mm);
        }
        // список атрибутов групп для чтения
        String[] groupFrom = new String[]{"groupName"};
        // список ID view-элементов, в которые будет помещены атрибуты групп
        int[] groupTo = new int[]{android.R.id.text1};

        //сначала создаём список групп элементов, чтобы в него добавлять списки элементов
        childData = new ArrayList<ArrayList<Map<String,String>>>();

        //теперь создаём список элементов для всех групп
        for (int i=0; i< type_name.length;i++) {

            childDataItem = new ArrayList<Map<String, String>>();

            //массив типов работ для сметы с file_id
            long tipe_id = mSmetaOpenHelper.getIdFromTypeName(type_name[i]);
            //Массив работ в файле с file_id и типом tipe_id
            String[] workNames = mSmetaOpenHelper.getNameOfWorkSelectedType(file_id,tipe_id );
            //Массив расценок для работ в файле с file_id и типом tipe_id
            float[] workCost = mSmetaOpenHelper.getCostOfWorkSelectedType(file_id, tipe_id );
            //Массив количества работ для работ в файле с file_id и типом tipe_id
            float[] workАmount = mSmetaOpenHelper.getAmountOfWorkSelectedType(file_id, tipe_id );
            //Массив единиц измерения для работ в файле с file_id и типом tipe_id
            String[] workUnits = mSmetaOpenHelper.getUnitsOfWorkSelectedType(file_id, tipe_id );
            //Массив стоимости работы  для работ в файле с file_id и типом tipe_id
            float[] workSumma = mSmetaOpenHelper.getSummaOfWorkSelectedType(file_id, tipe_id );
            //Массив стоимости работы  для работ в файле с file_id
            work_summa = mSmetaOpenHelper.getSummaOfWork(file_id);

            for (int j = 0; j < workNames.length; j++) {
                mm = new HashMap<String, String>();
                mm.put("workName", workNames[j]);
                mm.put("workCost", Float.toString(workCost[j]));
                mm.put("workАmount", Float.toString(workАmount[j]));
                mm.put("workUnit", workUnits[j]);
                mm.put("workSumma", Float.toString(workSumma[j]));
                childDataItem.add(mm);
            }
            childData.add(childDataItem);
        }
        // список атрибутов элементов для чтения
        String[] childFrom = new String[]{"workName", "workCost", "workАmount", "workUnit", "workSumma"};
        // список ID view-элементов, в которые будет помещены атрибуты элементов
        int[] childTo = new int[]{R.id.base_text, R.id.tvCost, R.id.tvAmount, R.id.tvUnits, R.id.tvSumma};

        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                getActivity(),
                groupData,
                android.R.layout.simple_expandable_list_item_1,
                groupFrom,groupTo,
                childData,
                R.layout.list_item_complex, //можно свой макет с android:id="@android:id/text1"
                childFrom,childTo);

        lvSmetasRabota.setAdapter(adapter);
        //разворачиваем все группы
        for(int i=0; i < adapter.getGroupCount(); i++)
            lvSmetasRabota.expandGroup(i);
    }
*/

}





