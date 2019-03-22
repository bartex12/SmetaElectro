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
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.FileWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.TypeWork;

public class SmetasTab1Rabota extends Fragment {

    public static final String TAG = "33333";

    ListView lvSmetasRabota;
    SmetaOpenHelper mSmetaOpenHelper;
    ArrayList<Map<String, Object>> data;
    Map<String,Object> m;
    SimpleAdapter sara;

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
    }


    public void updateAdapter() {
        //потом будет более сложный макет списка = с позицией, названием работы ценой, количеством,
        //суммой, типом и категорией работы

        //Курсор с позициями сметы на работу
        String[] work_name = mSmetaOpenHelper.getSmetaWork(file_id);
        //Список с данными для адаптера
        data = new ArrayList<Map<String, Object>>(work_name.length);

        for (int i = 0; i< work_name.length; i++){
            Log.d(TAG, "SmetasTab1Rabota - updateAdapter  name_file = " + work_name[i]);
            m = new HashMap<>();
            m.put(P.FILENAME_DEFAULT,work_name[i]);
            data.add(m);
        }
        String[] from = new String[]{P.FILENAME_DEFAULT};
        int[] to = new int[]{ R.id.base_text};
        sara =  new SimpleAdapter(getActivity(), data, R.layout.list_item_single, from, to);
        lvSmetasRabota.setAdapter(sara);
    }
}
