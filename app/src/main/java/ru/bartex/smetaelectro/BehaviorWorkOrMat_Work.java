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

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.FW;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.FileWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.TableControllerSmeta;

public class BehaviorWorkOrMat_Work implements BehaviorWorkOrMat {

    public static final String TAG = "33333";
    ListView lvSmetas;
    TableControllerSmeta tableControllerSmeta;
    ArrayList<Map<String, Object>> data;
    Map<String, Object> m;
    SimpleAdapter sara;
    float[] summa; //массив стоимости
    float totalSumma; // общая стоимость

    long file_id;

    View header;
    View footer;

    Context context;

    public BehaviorWorkOrMat_Work(Context context,
                                  TableControllerSmeta tableControllerSmeta, ListView lvSmetas,
                                  long file_id){
        Log.d(TAG, "//BehaviorWorkOrMat_Work Конструктор // " );
        this.tableControllerSmeta = tableControllerSmeta;
        this.lvSmetas = lvSmetas;
        this.file_id = file_id;
        this.context = context;
        Log.d(TAG, "BehaviorWorkOrMat_Work Конструктор  tableControllerSmeta ="+ tableControllerSmeta+
                "  lvSmetas = " + lvSmetas);
    }

    @Override
    public void updateAdapter(Context context) {
        Log.d(TAG, "//BehaviorWorkOrMat_Work updateAdapter // " );
        //Массив категорий материалов для сметы с file_id
        String[] cat_name = tableControllerSmeta.getArrayCategory(file_id, FW.TABLE_NAME);
        Log.d(TAG, "BehaviorWorkOrMat_Work - updateAdapter  cat_name.length = " + cat_name.length);
        //массив типов материалов для сметы с file_id
        String[] type_name = tableControllerSmeta.getTypeNames(file_id, FW.TABLE_NAME);
        Log.d(TAG, "BehaviorWorkOrMat_Work - updateAdapter  type_name.length = " + type_name.length);
        Log.d(TAG, "BehaviorWorkOrMat_Work - updateAdapter  file_id= " + file_id);
        //Массив материалов в файле с file_id
        String[] work_name = tableControllerSmeta.getArrayNames(file_id, FW.TABLE_NAME);
        //Массив цен для материалов в файле с file_id
        float[] work_cost = tableControllerSmeta.getArrayCost(file_id, FW.TABLE_NAME);
        //Массив количества материалов для материалов в файле с file_id
        float[] work_amount = tableControllerSmeta.getArrayAmount(file_id, FW.TABLE_NAME);
        //Массив единиц измерения для материалов в файле с file_id
        String[] work_units = tableControllerSmeta.getArrayUnit(file_id, FW.TABLE_NAME);
        //Массив стоимости материалов  для материалов в файле с file_id
        summa = tableControllerSmeta.getArraySumma(file_id, FW.TABLE_NAME);

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
            m.put(P.WORK_SUMMA, String.format(Locale.ENGLISH,"%.2f", summa[i]));
            data.add(m);

        }
        String[] from = new String[]{P.WORK_NUMBER, P.WORK_NAME, P.WORK_COST, P.WORK_AMOUNT,
                P.WORK_UNITS, P.WORK_SUMMA};
        int[] to = new int[]{R.id.tvNumberOfLine, R.id.base_text, R.id.tvCost, R.id.tvAmount,
                R.id.tvUnits, R.id.tvSumma};

        lvSmetas.removeHeaderView(header);
        //добавляем хедер
        header = LayoutInflater.from(context).inflate(R.layout.list_item_single, null);
        String fileName = tableControllerSmeta.getNameFromId(file_id, FileWork.TABLE_NAME);
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
