package ru.bartex.smetaelectro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
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

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.FM;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.FW;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.FileWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.TableControllerSmeta;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.TypeMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.TypeWork;

public class ListOfSmetasStructured extends AppCompatActivity {

    public static final String TAG = "33333";

    LinearLayout llWork;
    ListView mListViewNames;
    TableControllerSmeta tableControllerSmeta;
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

        tableControllerSmeta = new TableControllerSmeta(this);

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

        String fileName = tableControllerSmeta.getNameFromId(file_id, FileWork.TABLE_NAME);
        switch (position_tab){
            case 0:
                data = new  ArrayList<Map<String, String>>();

                Log.d(TAG, "ListOfSmetasStructured - updateAdapter case 0 /////////////////////");
                //Массив категорий работ для сметы с file_id
                String[] cat_name = tableControllerSmeta.getArrayCategory(file_id, FW.TABLE_NAME);
                Log.d(TAG, "ListOfSmetasStructured - updateAdapter  cat_name.length = " + cat_name.length);
                //массив типов работ для сметы с file_id
                String[] type_name = tableControllerSmeta.getTypeNamesSort(file_id,FW.TABLE_NAME);
                Log.d(TAG, "ListOfSmetasStructured - updateAdapter  type_name.length = " + type_name.length);

                //получаем суммарную стоимомть работ  по смете с id файла file_id
                work_summas = tableControllerSmeta.getArraySumma(file_id, FW.TABLE_NAME);

                int length = type_name.length;
                if (length >0){
                    for (int i = 0; i<length; i++){
                        mm = new HashMap<>();
                        mm.put("typeName", (i+1) + " " + type_name[i]);
                        data.add(mm);
                        Log.d(TAG, "ListOfSmetasStructured - updateAdapter  data.size()1 = " + data.size());

                        long type_id = tableControllerSmeta.
                                getIdFromName(type_name[i], TypeWork.TABLE_NAME);
                        Log.i(TAG, "ListOfSmetasStructured updateAdapter type_name[i] = " +
                                type_name[i] + " type_id = " + type_id);

                        //получаем имена работ  по смете с id файла file_id и id типа type_id
                        String[] work_names = tableControllerSmeta.
                                getArrayNamesSelectedType(file_id, type_id, FW.TABLE_NAME);
                        //получаем расценки работ  по смете с id файла file_idи id типа type_id
                        float[] work_cost = tableControllerSmeta.getArrayCostSelectedType(
                                file_id, type_id, FW.TABLE_NAME);
                        //получаем количество работ  по смете с id файла file_id и id типа type_id
                        float[] work_amount = tableControllerSmeta.
                                getArrayAmountSelectedType(file_id, type_id, FW.TABLE_NAME);
                        //получаем единицы измерения для  работ  по смете с id файла file_id
                        String[]  work_units = tableControllerSmeta.
                                getArrayUnitSelectedType(file_id, type_id, FW.TABLE_NAME);
                        //получаем стоимомть работ  по смете с id файла file_id и id типа type_id
                        float[] work_summa =  tableControllerSmeta.
                                getArraySummaSelectedType(file_id, type_id, FW.TABLE_NAME );

                        for (int k = 0; k<work_names.length; k++){
                            mm = new HashMap<>();
                            //mm.put("typeName", (i+1) + " " + type_name[i]);
                            mm.put("workName", "   " + (i+1)+ "." + (k+1) + " " + work_names[k]);
                            mm.put("workCost", Float.toString(work_cost[k]));
                            mm.put("workАmount", Float.toString(work_amount[k]));
                            mm.put("workUnit", work_units[k]);
                            mm.put("workSumma", String.format(Locale.ENGLISH,"%.2f", work_summa[k]));

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
                String[] cat_mat_name =tableControllerSmeta.getArrayCategory(file_id, FM.TABLE_NAME);
                Log.d(TAG, "ListOfSmetasStructured - updateAdapter  cat_mat_name.length = " + cat_mat_name.length);
                //массив типов материалов для сметы с file_id
                //String[] type_mat_name = mSmetaOpenHelper.getTypeNamesFM(file_id);
                String[] type_mat_name = tableControllerSmeta.getTypeNamesSort(file_id, FM.TABLE_NAME);
                Log.d(TAG, "ListOfSmetasStructured - updateAdapter  type_name.length = " + type_mat_name.length);

                //получаем суммарную стоимомть материалов  по смете с id файла file_id
                mat_summas = tableControllerSmeta.getArraySumma(file_id, FM.TABLE_NAME);

                int length_mat = type_mat_name.length;
                if (length_mat >0){
                    for (int i = 0; i<length_mat; i++){
                        mm = new HashMap<>();
                        mm.put("typeName", (i+1) + " " + type_mat_name[i]);
                        data.add(mm);
                        Log.d(TAG, "ListOfSmetasStructured - updateAdapter  data.size()1 = " + data.size());

                        long type_mat_id = tableControllerSmeta.
                                getIdFromName(type_mat_name[i], TypeMat.TABLE_NAME);
                        Log.i(TAG, "ListOfSmetasStructured updateAdapter type_mat_name[i] = " +
                                type_mat_name[i] + " type_mat_id = " + type_mat_id);

                        //получаем имена материалов  по смете с id файла file_id и id типа type_mat_name
                        String[] mat_names = tableControllerSmeta.
                                getArrayNamesSelectedType(file_id, type_mat_id, FM.TABLE_NAME);
                        //получаем расценки материалов  по смете с id файла file_idи id типа type_id
                        float[] mat_cost = tableControllerSmeta.getArrayCostSelectedType(
                                file_id, type_mat_id, FM.TABLE_NAME);
                        //получаем количество работ  по смете с id файла file_id и id типа type_id
                        float[] mat_amount = tableControllerSmeta.
                                getArrayAmountSelectedType(file_id, type_mat_id, FM.TABLE_NAME);
                        //получаем единицы измерения для  работ  по смете с id файла file_id
                        String[]  mat_units = tableControllerSmeta.
                                getArrayUnitSelectedType(file_id, type_mat_id, FM.TABLE_NAME);
                        //получаем стоимомть работ  по смете с id файла file_id и id типа type_id
                        float[] mat_summa =  tableControllerSmeta.
                                getArraySummaSelectedType(file_id, type_mat_id, FM.TABLE_NAME);

                        for (int k = 0; k<mat_names.length; k++){
                            mm = new HashMap<>();
                            //mm.put("typeName", (i+1) + " " + type_name[i]);
                            mm.put("workName", "   " + (i+1)+ "." + (k+1) + " " + mat_names[k]);
                            mm.put("workCost", Float.toString(mat_cost[k]));
                            mm.put("workАmount", Float.toString(mat_amount[k]));
                            mm.put("workUnit", mat_units[k]);
                            mm.put("workSumma", String.format(Locale.ENGLISH,"%.2f", mat_summa[k]));
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

            //получаем путь к предопределённой папке для документов
            //такой код в андроид 7 не работает путь: /storage/emulated/0/Documents
            //File exportDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            //а вот такой - работает  путь:  /storage/emulated/0/Android/data/ru.bartex.smetaelectro/files/Documents
            //File exportDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

            File exportDir = null;
            if (Build.VERSION.SDK_INT >= 24){
                Log.d(TAG, "Build.VERSION >= 24");
                //папка по адресу
                exportDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                //папка по адресу /data/user/0/ru.bartex.smetaelectro/files
                //exportDir = getApplicationContext().getFilesDir();
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

            if (position_tab==0){
                fileWork = new File(exportDir, "Smeta_na_rabotu.csv");
            }else if (position_tab == 1){
                fileWork = new File(exportDir, "Smeta_na_materialy.csv");
            }
            Log.d(TAG, "ListOfSmetasStructured - doInBackground fileWork = " + fileWork);

            try {
               fileWork.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(fileWork));

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
                    curTypeSort =  tableControllerSmeta.getTypeNamesStructured(file_id, FW.TABLE_NAME);

                    //для каждого имени типа работы
                while (curTypeSort.moveToNext()) {
                    //получаем имя типа работы из курсора
                    String typeName = curTypeSort.getString(curTypeSort.getColumnIndex(FW.FW_TYPE_NAME));
                    //вставляем имя типа в строковый массив
                    String[] typeNameArray = new String[]{"     "+typeName, "", "", ""};
                    //выводим имя типа работы в таблицу - надо сделать через настройки вывод типа
                    //csvWrite.writeNext(typeNameArray);

                    //получаем id типа работы ...
                    long type_id = tableControllerSmeta.
                            getIdFromName(typeName, TypeWork.TABLE_NAME);
                    Log.i(TAG, "ListOfSmetasStructured doInBackground position_tab=0 type_name = " +
                            typeName + " type_id = " + type_id);

                    //получаем курсор с данными сметы с file_id для типа работ с type_id
                    curStroka = tableControllerSmeta.getDataSortStructured(file_id,type_id, FW.TABLE_NAME);

                    //...выводим все строки с названием, ценой, количеством и суммой
                    while (curStroka.moveToNext()) {

                        String[] myStrokaStringArray = new String[curStroka.getColumnNames().length];
                        for (int i = 0; i < curStroka.getColumnNames().length; i++) {
                            myStrokaStringArray[i] = curStroka.getString(i);
                        }
                        csvWrite.writeNext(myStrokaStringArray);
                    }
                    //пишем пустую строку - если делать вывод типа
                    //csvWrite.writeNext(pusto);
                }
                    //если вошли с вкладки материалы
                    }else if (position_tab == 1){

                    //получаем курсор с именами типов материала
                    curTypeSort =  tableControllerSmeta.getTypeNamesStructured(file_id, FM.TABLE_NAME);

                    //для каждого имени типа материала
                    while (curTypeSort.moveToNext()) {

                        //получаем имя типа материала из курсора
                        String typeName = curTypeSort.getString(curTypeSort.getColumnIndex(FM.FM_MAT_TYPE_NAME));
                        //вставляем имя типа в строковый массив
                        String[] typeNameArray = new String[]{"     "+typeName, "", "", ""};
                        //выводим имя типа материала в таблицу - надо сделать через настройки вывод типа
                        //csvWrite.writeNext(typeNameArray);

                        //получаем id типа материала...
                        long type_id =tableControllerSmeta.getIdFromName(typeName, TypeMat.TABLE_NAME);
                        Log.i(TAG, "ListOfSmetasStructured doInBackground position_tab=1 type_name = " +
                                typeName + " type_id = " + type_id);

                        //получаем курсор с данными сметы с file_id для типа материала с type_id
                        curStroka =  tableControllerSmeta.getDataSortStructured(file_id, type_id, FM.TABLE_NAME);

                        //...выводим все строки с названием, ценой, количеством и суммой
                        while(curStroka.moveToNext()) {

                            String[] myStrokaStringArray = new String[curStroka.getColumnNames().length];
                            for(int i=0; i<curStroka.getColumnNames().length; i++)
                            {
                                myStrokaStringArray[i] =curStroka.getString(i);
                            }
                            csvWrite.writeNext(myStrokaStringArray);
                        }
                        //пишем пустую строку - если делать вывод типа
                        //csvWrite.writeNext(pusto);
                    }
                }

                csvWrite.writeNext(pusto);
                csvWrite.writeNext(summarno);

                csvWrite.close();
                curStroka.close();
                curTypeSort.close();

/*
                // +++++++++++++  ПЕРЕВОД В Excel  ++++++++++++++ start
                //рабочий вариант, но  во всех программах не работает кодировка
                ArrayList arList=null;
                ArrayList al=null;

                //нужна папка /data/user/0/ru.bartex.smetaelectro/files

                //сейчас путь /storage/emulated/0/Android/data/ru.bartex.smetaelectro/files/Documents/Smeta_na_rabotu.csv
                String inFilePath =getApplicationContext().
                        getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+"/Smeta_na_rabotu.csv";
                Log.d(TAG, "ListOfSmetasStructured - doInBackground inFilePath = " + inFilePath);
                //здесь путь  /storage/emulated/0/Android/data/ru.bartex.smetaelectro/files/Documents/test.xls
                String outFilePath = getApplicationContext().
                        getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) +"/test.xls";
                Log.d(TAG, "ListOfSmetasStructured - doInBackground outFilePath = " + outFilePath);
                String thisLine;
                int count=0;

                try {
                Log.d(TAG,"try1");

                // открываем поток для чтения
                //InputStream is = openFileInput(inFilePath);
                //Log.d(TAG,"InputStream is = " + is);
                //InputStreamReader isr = new InputStreamReader(is);
                // Log.d(TAG,"InputStreamReader isr = " + isr);
                //BufferedReader br = new BufferedReader(isr);
                //Log.d(TAG,"BufferedReader br = " + br);

                int i=0;

                FileInputStream fis = new FileInputStream(fileWork);
                Log.d(TAG,"FileInputStream fis = " + fis);
                DataInputStream myInput = new DataInputStream(fis);
                Log.d(TAG,"DataInputStream myInput = " + myInput);

                arList = new ArrayList();
                Log.d(TAG,"myInput.readLine() = " + myInput.readLine());
                while ((thisLine = myInput.readLine()) != null)
                {
                    al = new ArrayList();
                    String strar[] = thisLine.split(",");
                    for(int j=0;j<strar.length;j++)
                    {
                        al.add(strar[j]);
                        Log.d(TAG,"try1 al.size() = " + al.size());
                    }
                    arList.add(al);
                    Log.d(TAG,"try1 arList.size() = " + arList.size());
                    System.out.println();
                    i++;
                }
                 } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "shit");
                }
                try
                {
                    Log.d(TAG,"try2");
                    HSSFWorkbook hwb = new HSSFWorkbook();
                    HSSFSheet sheet = hwb.createSheet("new sheet");
                    Log.d(TAG,"arList.size() = " + arList.size());
                    for(int k=0;k<arList.size();k++)
                    {
                        ArrayList ardata = (ArrayList)arList.get(k);
                        HSSFRow row = sheet.createRow((short) 0+k);
                        Log.d(TAG,"ardata.size() = " + ardata.size());
                        for(int p=0;p<ardata.size();p++)
                        {
                            HSSFCell cell = row.createCell((short) p);
                            String data = ardata.get(p).toString();
                            if(data.startsWith("=")){
                                cell.setCellType(Cell.CELL_TYPE_STRING);
                                data=data.replaceAll("\"", "");
                                data=data.replaceAll("=", "");
                                cell.setCellValue(data);
                            }else if(data.startsWith("\"")){
                                data=data.replaceAll("\"", "");
                                cell.setCellType(Cell.CELL_TYPE_STRING);
                                cell.setCellValue(data);
                            }else{
                                data=data.replaceAll("\"", "");
                                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                                cell.setCellValue(data);
                            }
                        }
                        System.out.println();
                    }
                    FileOutputStream fileOut = new FileOutputStream(outFilePath);
                    hwb.write(fileOut);
                    fileOut.close();
                    Log.d(TAG,"try3");
                } catch ( Exception ex ) {
                    ex.printStackTrace();
                }
                // +++++++++++++  ПЕРЕВОД В Excel  ++++++++++++++ end
                */

                Log.d(TAG,"try4");
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

                //чтобы не крэшилось приложение при вызове
                // из-за использования file:// а не content:// в Uri для API>24
                StrictMode.VmPolicy.Builder builder1 = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder1.build());

                //ниже строки для формата Excel который пока не работает
                //String outFilePath = getApplicationContext().
                //       getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) +"/test.xls";
                //File file = new File(outFilePath);
                //Uri u1  =   Uri.fromFile(file);

                Uri u1  =   Uri.fromFile(fileWork);

                Log.d(TAG, "ListOfSmetasStructured -  onPostExecute  Uri u1 = " + u1);

                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Smeta Details");
                sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
                sendIntent.setType("text/html");
                startActivity(sendIntent);

            } else {
                Toast.makeText(ListOfSmetasStructured.this, "Export failed", Toast.LENGTH_SHORT).show();
            }

        }
    }

}





