package ru.bartex.smetaelectro;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.FW;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;

public class SmetaListStructured extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smeta_list_structured);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_smetas_list);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mSmetaOpenHelper = new SmetaOpenHelper(this);
        file_id = getIntent().getExtras().getLong(P.ID_FILE);
        position_tab = getIntent().getExtras().getInt(P.TAB_POSITION);
        Log.d(TAG, "SmetaListStructured - onCreate file_id = " + file_id  + "  position_tab = " + position_tab);

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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // Для данного варианта в манифесте указан режим singlTask для активности MainActivity
                    Intent intentHome = new Intent(SmetaListStructured.this, MainActivity.class);
                    // установка флагов- создаём новую задачу и убиваем старую вместе
                    // со всеми предыдущими переходами - работает дёргано по сравнению с манифестом
                    //intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    //      Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentHome);
                    return true;
                case R.id.navigation_smetas:
                    // Для данного варианта в манифесте указан режим singlTask для активности ListOfSmetasNames
                    Intent intent_smetas = new Intent(SmetaListStructured.this, ListOfSmetasNames.class);
                    startActivity(intent_smetas);
                    return true;
                case R.id.navigation_costs:
                    if (position_tab == 0){
                        Intent intent_costs_work = new Intent(SmetaListStructured.this, SmetasWorkCost.class);
                        startActivity(intent_costs_work);
                    }else if (position_tab == 1){
                        Intent intent_costs_mat = new Intent(SmetaListStructured.this, SmetasMatCost.class);
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

                Log.d(TAG, "SmetaListStructured - updateAdapter /////////////////////");
                //Массив категорий работ для сметы с file_id
                String[] cat_name = mSmetaOpenHelper.getCategoryNamesFW(file_id);
                Log.d(TAG, "SmetaListStructured - updateAdapter  cat_name.length = " + cat_name.length);
                //массив типов работ для сметы с file_id
                //String[] type_name = mSmetaOpenHelper.getTypeNamesFW(file_id);
                String[] type_name = mSmetaOpenHelper.getTypeNamesFWSort(file_id);
                Log.d(TAG, "SmetaListStructured - updateAdapter  type_name.length = " + type_name.length);

                //получаем суммарную стоимомть работ  по смете с id файла file_id
                work_summas = mSmetaOpenHelper.getSummaOfWork(file_id);

                int length = type_name.length;
                if (length >0){
                    for (int i = 0; i<length; i++){
                        mm = new HashMap<>();
                        mm.put("typeName", (i+1) + " " + type_name[i]);
                        data.add(mm);
                        Log.d(TAG, "SmetaListStructured - updateAdapter  data.size()1 = " + data.size());

                        long type_id = mSmetaOpenHelper.getIdFromTypeName(type_name[i]);
                        Log.i(TAG, "SmetaOpenHelper.getNameOfTypesAndWorkStructured type_name[i] = " +
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
                            Log.d(TAG, "SmetaListStructured - updateAdapter  data.size()2 = " + data.size());
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
                Log.d(TAG, "SmetaListStructured - updateAdapter  totalSumma = " + totalSumma);
                ((TextView)footer.findViewById(R.id.base_text)).
                        setText(String.format(Locale.ENGLISH,"За работу: %.0f руб", totalSumma ));
                mListViewNames.addFooterView(footer, null, false);
                Log.d(TAG, "*********  addFooterView getFooterViewsCount1 = " +
                        mListViewNames.getFooterViewsCount());
                //***************************Header and Footer***************
                break;

            case 1:

                data = new  ArrayList<Map<String, String>>();

                Log.d(TAG, "SmetaListStructured - updateAdapter /////////////////////");
                //Массив категорий материалов для сметы с file_id
                String[] cat_mat_name = mSmetaOpenHelper.getCategoryNamesFM(file_id);
                Log.d(TAG, "SmetaListStructured - updateAdapter  cat_mat_name.length = " + cat_mat_name.length);
                //массив типов материалов для сметы с file_id
                //String[] type_mat_name = mSmetaOpenHelper.getTypeNamesFM(file_id);
                String[] type_mat_name = mSmetaOpenHelper.getTypeNamesFMSort(file_id);
                Log.d(TAG, "SmetaListStructured - updateAdapter  type_name.length = " + type_mat_name.length);

                //получаем суммарную стоимомть материалов  по смете с id файла file_id
                mat_summas = mSmetaOpenHelper.getSummaOfMat(file_id);

                int length_mat = type_mat_name.length;
                if (length_mat >0){
                    for (int i = 0; i<length_mat; i++){
                        mm = new HashMap<>();
                        mm.put("typeName", (i+1) + " " + type_mat_name[i]);
                        data.add(mm);
                        Log.d(TAG, "SmetaListStructured - updateAdapter  data.size()1 = " + data.size());

                        long type_mat_id = mSmetaOpenHelper.getIdFromMatTypeName(type_mat_name[i]);
                        Log.i(TAG, "SmetaOpenHelper.getNameOfTypesAndWorkStructured type_mat_name[i] = " +
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
                            Log.d(TAG, "SmetaListStructured - updateAdapter  data.size()2 = " + data.size());
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
                Log.d(TAG, "SmetasTab1Rabota - updateAdapter  totalSumma = " + totalSumma);
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
                    Log.d(TAG, "SmetaListStructured - MyViewBinder s = " + s);
                    if (s == null) {
                        view.setVisibility(View.GONE);
                    } else {
                        view.setVisibility(View.VISIBLE);
                    }
                    //для продолжения обработки - текстовое поле - ставим false
                    return false;

                case R.id.llWork:
                    Log.d(TAG, "SmetaListStructured - MyViewBinder view.getId() == R.id.llWork /////");
                    String s1 = (String) data;
                    //Log.d(TAG, "SmetaListStructured - MyViewBinder s = " + s1 );
                    if (s1 == null) {
                        view.setVisibility(View.VISIBLE);
                        Log.d(TAG, "SmetaListStructured - MyViewBinder s == null  View.VISIBLE");
                    } else {
                        view.setVisibility(View.GONE);
                        Log.d(TAG, "SmetaListStructured - MyViewBinder s == null View.GONE");
                    }
                    //так как адаптер не умеет обрабатывать LinearLayout, ставим true. прекращая обработку
                    return true;
            }
            //чтобы продолжать обрабатывать другие View, ставим  false
            return false;
        }
    }

    /*

    //можно замутить класс для изменения свойств в setViewText или setViewImage
    // но проще цветной текст сделать в макете
    class MySimpleAdapter extends SimpleAdapter {

        public MySimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource,
                               String[] from, int[] to) {
            super(context, data, resource, from, to);
        }
        @Override
        public void setViewText(TextView v, String text) {
            // метод супер-класса, который вставляет текст
            super.setViewText(v, text);

            // если нужный нам TextView
            if (v.getId() == R.id.base_text_type) {
                v.setTextColor(Color.BLUE);
            }else {
                v.setTextColor(Color.BLACK);
            }
        }
    }

// вариант для вывода сметы с хедером и футером в активности Smetas
    public void updateAdapter() {

        //Массив категорий работ для сметы с file_id
        String[] cat_name = mSmetaOpenHelper.getCategoryNamesFW(file_id);
        Log.d(TAG, "SmetasTab1Rabota - updateAdapter  cat_name.length = " + cat_name.length);
        //массив типов работ для сметы с file_id
        String[] type_name = mSmetaOpenHelper.getTypeNamesFW(file_id);
        Log.d(TAG, "SmetasTab1Rabota - updateAdapter  type_name.length = " + type_name.length);
        //Массив работ в файле с file_id
        String[] work_name = mSmetaOpenHelper.getNameOfWork(file_id);
        //Массив расценок для работ в файле с file_id
        float[] work_cost = mSmetaOpenHelper.getCostOfWork(file_id);
        //Массив количества работ для работ в файле с file_id
        float[] work_amount = mSmetaOpenHelper.getAmountOfWork(file_id);
        //Массив единиц измерения для работ в файле с file_id
        String[] work_units = mSmetaOpenHelper.getUnitsOfWork(file_id);
        //Массив стоимости работы  для работ в файле с file_id
        work_summa = mSmetaOpenHelper.getSummaOfWork(file_id);

        //Список с данными для адаптера
        data = new ArrayList<Map<String, Object>>(work_name.length);

        for (int j=0; j<cat_name.length; j++){
            //добавляем хедер
            header = getActivity().getLayoutInflater().inflate(R.layout.list_item_single, null);
            ((TextView)header.findViewById(R.id.base_text)).setText((j+1) +" "+ cat_name[j]);
            lvSmetasRabota.addHeaderView(header, null, false);

            for (int i = 0; i < work_name.length; i++) {
                Log.d(TAG, "SmetasTab1Rabota - updateAdapter  work_name = " + work_name[i]);

                m = new HashMap<>();
                m.put(P.WORK_NUMBER, (j+1) + "." +(i + 1));
                m.put(P.WORK_NAME, work_name[i]);
                m.put(P.WORK_COST, work_cost[i]);
                m.put(P.WORK_AMOUNT, work_amount[i]);
                m.put(P.WORK_UNITS, work_units[i]);
                m.put(P.WORK_SUMMA, work_summa[i]);
                data.add(m);
            }
            //добавляем футер
            footer = getActivity().getLayoutInflater().inflate(R.layout.list_item_single, null);
            ((TextView)footer.findViewById(R.id.base_text)).setText("Итоги");
            lvSmetasRabota.addFooterView(footer);
        }
        String[] from = new String[]{P.WORK_NUMBER, P.WORK_NAME, P.WORK_COST, P.WORK_AMOUNT,
                P.WORK_UNITS, P.WORK_SUMMA};
        int[] to = new int[]{R.id.tvNumberOfLine, R.id.base_text, R.id.tvCost, R.id.tvAmount,
                R.id.tvUnits, R.id.tvSumma};
        sara = new SimpleAdapter(getActivity(), data, R.layout.list_item_complex, from, to);
        lvSmetasRabota.setAdapter(sara);
    }
*/

/*
 //это метод для адаптера ниже. Используется для вывода сметв в активности SmetaListStructured
    //получаем имена работ  по смете с id файла file_id
    public ArrayList<String>  getNameOfTypesAndWorkStructured(long file_id){

        String select_type_name_structured =
                " select  distinct " + FW.FW_TYPE_NAME  +
                        " from " + FW.TABLE_NAME +
                        " where " +  FW.FW_FILE_ID  + " = " +  String.valueOf(file_id)+
                        " order by " + FW.FW_TYPE_ID ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select_type_name_structured, null);
        Log.i(TAG, "TempDBHelper.getNameOfTypesAndWorkStructured cursor.getCount()  " + cursor.getCount());

        ArrayList<String> arrayList = new ArrayList<>();
        String[] type_name = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            type_name[position] = cursor.getString(cursor.getColumnIndex(FW.FW_TYPE_NAME));
            arrayList.add((position+1) + " " + type_name[position]);
            long type_id = this.getIdFromTypeName(type_name[position]);
            Log.i(TAG, "SmetaOpenHelper.getNameOfTypesAndWorkStructured type_name[position] = " +
                    type_name[position] + " type_id = " + type_id);

            String select_work_name_structured =
                    " select distinct " + FW.FW_WORK_NAME  +
                            " from " + FW.TABLE_NAME +
                            " where " +  FW.FW_FILE_ID  + " = " +  String.valueOf(file_id)+
                            " and " +  FW.FW_TYPE_ID + " = " +  String.valueOf(type_id);
            Cursor cursor1 = db.rawQuery(select_work_name_structured, null);
            Log.i(TAG, "TempDBHelper.getNameOfTypesAndWorkStructured cursor1.getCount()  " + cursor1.getCount());

            String[] work_name = new String[cursor1.getCount()];
            while (cursor1.moveToNext()){
                int position1 = cursor1.getPosition();
                work_name[position1] = cursor1.getString(cursor1.getColumnIndex(FW.FW_WORK_NAME));
                arrayList.add("   " + (position+1)+ "." + (position1+1) + " " + work_name[position1]);
                Log.i(TAG, "SmetaOpenHelper.getNameOfTypesAndWorkStructured work_name[position1] = " + work_name[position1]);
            }
            cursor1.close();
        }
        cursor.close();

        return arrayList;
    }

*/

/*
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

    //данные для адаптера готовятся в getNameOfTypesAndWorkStructured в виде ArrayList<String>
    public void updateAdapter2() {
        Log.d(TAG, "SmetaListStructured - updateAdapter ...");
        //Курсор с именами файлов
        ArrayList<String> work_type_name = mSmetaOpenHelper.getNameOfTypesAndWorkStructured(file_id);
        //Список с данными для адаптера
        data = new ArrayList<Map<String, Object>>(work_type_name.size());

        for (int i = 0; i< work_type_name.size(); i++){
            Log.d(TAG, "SmetaListStructured - updateAdapter  name_file = " + work_type_name.get(i));
            m = new HashMap<>();
            m.put(P.TYPE_WORK_NAME,work_type_name.get(i));
            data.add(m);
        }
        String[] from = new String[]{P.TYPE_WORK_NAME};
        int[] to = new int[]{ R.id.base_text};
        sara =  new SimpleAdapter(this, data, R.layout.list_item_single_small, from, to);
        mListViewNames.setAdapter(sara);
    }
*/

}





