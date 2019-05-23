package ru.bartex.smetaelectro;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;

public class BehaviorWorkOrMat_Work implements BehaviorWorkOrMat {

    public static final String TAG = "33333";
    ListView lvSmetas;
    SmetaOpenHelper mSmetaOpenHelper;
    ArrayList<Map<String, Object>> data;
    Map<String, Object> m;
    SimpleAdapter sara;
    float[] summa; //массив стоимости
    float totalSumma; // общая стоимость

    long file_id;

    View header;
    View footer;

    Context context;

    public BehaviorWorkOrMat_Work(Context context, SmetaOpenHelper mSmetaOpenHelper, ListView lvSmetas, long file_id){
        Log.d(TAG, "//BehaviorWorkOrMat_Work Конструктор // " );
        this.mSmetaOpenHelper = mSmetaOpenHelper;
        this.lvSmetas = lvSmetas;
        this.file_id = file_id;
        this.context = context;
        Log.d(TAG, "BehaviorWorkOrMat_Work Конструктор  mSmetaOpenHelper ="+ mSmetaOpenHelper+
                "  lvSmetas = " + lvSmetas);
    }

    @Override
    public void updateAdapter(Context context) {
        Log.d(TAG, "//BehaviorWorkOrMat_Work updateAdapter // " );
        //Массив категорий материалов для сметы с file_id
        String[] cat_name = mSmetaOpenHelper.getCategoryNamesFW(file_id);
        Log.d(TAG, "BehaviorWorkOrMat_Work - updateAdapter  cat_name.length = " + cat_name.length);
        //массив типов материалов для сметы с file_id
        String[] type_name = mSmetaOpenHelper.getTypeNamesFW(file_id);
        Log.d(TAG, "BehaviorWorkOrMat_Work - updateAdapter  type_name.length = " + type_name.length);
        //Массив материалов в файле с file_id
        String[] work_name = mSmetaOpenHelper.getNameOfWork(file_id);
        //Массив цен для материалов в файле с file_id
        float[] work_cost = mSmetaOpenHelper.getCostOfWork(file_id);
        //Массив количества материалов для материалов в файле с file_id
        float[] work_amount = mSmetaOpenHelper.getAmountOfWork(file_id);
        //Массив единиц измерения для материалов в файле с file_id
        String[] work_units = mSmetaOpenHelper.getUnitsOfWork(file_id);
        //Массив стоимости материалов  для материалов в файле с file_id
        summa = mSmetaOpenHelper.getSummaOfWork(file_id);

        //Список с данными для адаптера
        data = new ArrayList<Map<String, Object>>(work_name.length);

        for (int i = 0; i < work_name.length; i++) {
            Log.d(TAG, "BehaviorWorkOrMat_Work - updateAdapter  work_name = " + work_name[i]);

            m = new HashMap<>();
            m.put(P.WORK_NUMBER, (i + 1));
            m.put(P.WORK_NAME, work_name[i]);
            m.put(P.WORK_COST, work_cost[i]);
            m.put(P.WORK_AMOUNT, work_amount[i]);
            m.put(P.WORK_UNITS, work_units[i]);
            m.put(P.WORK_SUMMA, summa[i]);
            data.add(m);
        }
        String[] from = new String[]{P.WORK_NUMBER, P.WORK_NAME, P.WORK_COST, P.WORK_AMOUNT,
                P.WORK_UNITS, P.WORK_SUMMA};
        int[] to = new int[]{R.id.tvNumberOfLine, R.id.base_text, R.id.tvCost, R.id.tvAmount,
                R.id.tvUnits, R.id.tvSumma};

        lvSmetas.removeHeaderView(header);
        //добавляем хедер
        header = LayoutInflater.from(context).inflate(R.layout.list_item_single, null);
        String fileName = mSmetaOpenHelper.getFileNameById(file_id);
        ((TextView)header.findViewById(R.id.base_text)).setText(
                String.format(Locale.ENGLISH,"Смета на работу:   %s", fileName));
        lvSmetas.addHeaderView(header, null, false);
        Log.d(TAG, "***********getHeaderViewsCount*********** = " +
                lvSmetas.getHeaderViewsCount());

        lvSmetas.removeFooterView(footer);
        Log.d(TAG, "*********  removeFooterView2  ********* ");
        //добавляем футер
        footer = LayoutInflater.from(context).inflate(R.layout.list_item_single, null);
        totalSumma = P.updateTotalSumma(summa);
        Log.d(TAG, "SmetasFrag - updateAdapter  totalSumma = " + totalSumma);
        ((TextView)footer.findViewById(R.id.base_text)).
                setText(String.format(Locale.ENGLISH,"За работу: %.0f руб", totalSumma ));
        lvSmetas.addFooterView(footer, null, false);
        Log.d(TAG, "*********  addFooterView getFooterViewsCount1 = " +
                lvSmetas.getFooterViewsCount());

        sara = new SimpleAdapter(context, data, R.layout.list_item_complex, from, to);
        lvSmetas.setAdapter(sara);
    }
}
