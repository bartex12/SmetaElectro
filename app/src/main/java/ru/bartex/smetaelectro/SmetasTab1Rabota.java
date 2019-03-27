package ru.bartex.smetaelectro;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.CategoryWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.FW;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.FileWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.TypeWork;

public class SmetasTab1Rabota extends Fragment {

    public static final String TAG = "33333";

    ListView lvSmetasRabota;
    TextView tvSumma;
    SmetaOpenHelper mSmetaOpenHelper;
    ArrayList<Map<String, Object>> data;
    Map<String,Object> m;
    SimpleAdapter sara;
    float[] work_summa; //массив стоимости работ

    long file_id;

    public static SmetasTab1Rabota newInstance(long file_id) {
        SmetasTab1Rabota fragment = new SmetasTab1Rabota();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "SmetasTab1Rabota: onCreate  ");
        //получаем id файла из аргументов
        file_id = getArguments().getLong(P.ID_FILE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSmetaOpenHelper = new SmetaOpenHelper(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_smetas_tab1_rabota, container, false);
        tvSumma = rootView.findViewById(R.id.tvSumma);
        lvSmetasRabota =  rootView.findViewById(R.id.listViewSmetasRabota);
        lvSmetasRabota.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateAdapter();
        float totalSumma = 0;
        for (int i = 0; i<work_summa.length; i++){
            totalSumma += work_summa[i];
        }
        tvSumma.setText("" + totalSumma);
    }


    public void updateAdapter() {

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

        for (int i = 0; i< work_name.length; i++){
            Log.d(TAG, "SmetasTab1Rabota - updateAdapter  name_file = " + work_name[i]);
            m = new HashMap<>();
            m.put(P.WORK_NUMBER,i+1);
            m.put(P.WORK_NAME,work_name[i]);
            m.put(P.WORK_COST,work_cost[i]);
            m.put(P.WORK_AMOUNT,work_amount[i]);
            m.put(P.WORK_UNITS,work_units[i]);
            m.put(P.WORK_SUMMA,work_summa[i]);
            data.add(m);
        }
        String[] from = new String[]{P.WORK_NUMBER,P.WORK_NAME,P.WORK_COST,P.WORK_AMOUNT,
                P.WORK_UNITS,P.WORK_SUMMA};
        int[] to = new int[]{ R.id.tvNumberOfLine, R.id.base_text,R.id.tvCost,R.id.tvAmount,
                R.id.tvUnits,R.id.tvSumma};
        sara =  new SimpleAdapter(getActivity(), data, R.layout.list_item_complex, from, to);
        lvSmetasRabota.setAdapter(sara);
    }
}
