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

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.FM;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.FileWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.TableControllerSmeta;

public class BehaviorWorkOrMat_Mat implements BehaviorWorkOrMat {

    public static final String TAG ="33333";

    long file_id;
    ListView lvSmetasMaterials;
    TableControllerSmeta tableControllerSmeta;
    float[] mat_summa;
    float totalSumma; // общая стоимость материалов по смете
    ArrayList<Map<String, Object>> data;
    HashMap<String, Object> m;
    SimpleAdapter sara;

    View header;
    View footer;

    Context context;

    public BehaviorWorkOrMat_Mat(Context context,
                                 TableControllerSmeta tableControllerSmeta,
                                 ListView lvSmetas, long file_id){
        this.tableControllerSmeta = tableControllerSmeta;
        this.lvSmetasMaterials = lvSmetas;
        this.file_id = file_id;
        this.context = context;
    }

    @Override
    public void updateAdapter(Context context) {
        Log.d(TAG, "//SmetasTab2Materialy updateAdapter // " );
        //Массив категорий материалов для сметы с file_id
        String[] cat_mat_name = tableControllerSmeta.getArrayCategory(file_id, FM.TABLE_NAME);
        Log.d(TAG, "SmetasTab2Materialy - updateAdapter  cat_name.length = " + cat_mat_name.length);
        //массив типов материалов для сметы с file_id
        String[] type_mat_name = tableControllerSmeta.getTypeNames(file_id, FM.TABLE_NAME);
        Log.d(TAG, "SmetasTab2Materialy - updateAdapter  type_name.length = " + type_mat_name.length);
        //Массив материалов в файле с file_id
        String[] mat_name = tableControllerSmeta.getArrayNames(file_id, FM.TABLE_NAME);
        //Массив цен для материалов в файле с file_id
        float[] mat_cost = tableControllerSmeta.getArrayCost(file_id, FM.TABLE_NAME);
        //Массив количества работ для работ в файле с file_id
        float[] mat_amount = tableControllerSmeta.getArrayAmount(file_id, FM.TABLE_NAME);
        //Массив единиц измерения для материалов в файле с file_id
        String[] mat_units = tableControllerSmeta.getArrayUnit(file_id, FM.TABLE_NAME);
        //Массив стоимости материалов  для работ в файле с file_id
        mat_summa = tableControllerSmeta.getArraySumma(file_id, FM.TABLE_NAME);

        //Список с данными для адаптера
        data = new ArrayList<Map<String, Object>>(mat_name.length);

        for (int i = 0; i < mat_name.length; i++) {
            Log.d(TAG, "SmetasTab2Materialy - updateAdapter  mat_name = " + mat_name[i]);

            m = new HashMap<>();
            m.put(P.MAT_NUMBER, (i + 1));
            m.put(P.MAT_NAME, mat_name[i]);
            m.put(P.MAT_COST, mat_cost[i]);
            m.put(P.MAT_AMOUNT, mat_amount[i]);
            m.put(P.MAT_UNITS, mat_units[i]);
            m.put(P.MAT_SUMMA, String.format(Locale.ENGLISH,"%.2f", mat_summa[i]));

            Log.d(TAG, "SmetasTab2Materialy - updateAdapter  i+1 = "
                    + (i+1) + " mat_units[i] = " + mat_units[i] );
            data.add(m);
        }

        String[] from = new String[]{P.MAT_NUMBER, P.MAT_NAME, P.MAT_COST, P.MAT_AMOUNT,
                P.MAT_UNITS, P.MAT_SUMMA};
        int[] to = new int[]{R.id.tvNumberOfLine, R.id.base_text, R.id.tvCost, R.id.tvAmount,
                R.id.tvUnits, R.id.tvSumma};

        //***************************Header and Footer***************
        lvSmetasMaterials.removeHeaderView(header);
        //добавляем хедер
        header = LayoutInflater.from(context).inflate(R.layout.list_item_single, null);
        String fileName = tableControllerSmeta.getNameFromId(file_id, FileWork.TABLE_NAME);
        ((TextView)header.findViewById(R.id.base_text)).setText(
                String.format(Locale.ENGLISH,"Смета на материалы:   %s", fileName));
        lvSmetasMaterials.addHeaderView(header, null, false);
        Log.d(TAG, "***********getHeaderViewsCount*********** = " +
                lvSmetasMaterials.getHeaderViewsCount());

        lvSmetasMaterials.removeFooterView(footer);
        Log.d(TAG, "*********  removeFooterView2  ********* ");
        //добавляем футер
        footer = LayoutInflater.from(context).inflate(R.layout.list_item_single, null);
        totalSumma = P.updateTotalSumma(mat_summa);
        Log.d(TAG, "SmetasTab1Rabota - updateAdapter  totalSumma = " + totalSumma);
        ((TextView)footer.findViewById(R.id.base_text)).
                setText(String.format(Locale.ENGLISH,"За материалы: %.0f руб", totalSumma ));
        lvSmetasMaterials.addFooterView(footer, null, false);
        Log.d(TAG, "*********  addFooterView getFooterViewsCount1 = " +
                lvSmetasMaterials.getFooterViewsCount());
        //***************************Header and Footer***************

        sara = new SimpleAdapter(context, data, R.layout.list_item_complex, from, to);
        lvSmetasMaterials.setAdapter(sara);
    }
}
