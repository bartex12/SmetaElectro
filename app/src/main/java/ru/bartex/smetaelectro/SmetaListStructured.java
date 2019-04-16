package ru.bartex.smetaelectro;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.FW;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;

public class SmetaListStructured extends AppCompatActivity {

    public static final String TAG = "33333";
    ListView mListViewNames;
    SmetaOpenHelper mSmetaOpenHelper;
    ArrayList<Map<String, Object>> data;
    Map<String,Object> m;
    SimpleAdapter sara;
    Button newSmeta;
    long file_id;

    ArrayList<String> arrayListType = new ArrayList<>();
    ArrayList<Map<String, Object>> dataWork;
    Map<String,Object> mm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smeta_list_structured);

        mSmetaOpenHelper = new SmetaOpenHelper(this);
        file_id = getIntent().getExtras().getLong(P.ID_FILE);

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
/*
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
    public void updateAdapter() {
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

    public void updateAdapter2() {
        //массив типов работ для сметы с file_id
        String[] type_name = mSmetaOpenHelper.getTypeNamesFW(file_id);

        for (int i = 0; i<type_name.length; i++){
            arrayListType.add((i+1) + " " + type_name[i]);
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

            //список для имён, цены и других параметров работы
            dataWork = new ArrayList<Map<String, Object>>(work_names.length);
            for (int k = 0; k<work_names.length; k++){
                mm = new HashMap<>();
                mm.put("work_names", work_names[k]);
                mm.put("work_cost", work_cost[k]);
                mm.put("work_amount", work_amount[k]);
                mm.put("work_units", work_units[k]);
                mm.put("work_summa", work_summa[k]);

                dataWork.add(mm);
            }

            }
        }

    }


