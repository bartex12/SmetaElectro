package ru.bartex.smetaelectro;


import android.content.Context;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.Mat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.TypeMat;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmetasMatTab2Mat extends Fragment {

    public static final String TAG = "33333";
    long file_id;
    int position;
    boolean isSelectedType;
    long type_id;

    ListView mListView;
    SmetaOpenHelper mSmetaOpenHelper;
    ArrayList<Map<String, Object>> data;
    HashMap<String, Object> m;
    SimpleAdapter sara;
    public SmetasMatTab2Mat() {
        // Required empty public constructor
    }

    public static SmetasMatTab2Mat NewInstance(
            long file_id, int position, boolean isSelectedType, long type_id) {
        Log.d(TAG, "//  SmetasMatTab2Mat NewInstance // " );
        SmetasMatTab2Mat fragment = new SmetasMatTab2Mat();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        args.putBoolean(P.IS_SELECTED_TYPE, isSelectedType);
        args.putLong(P.ID_TYPE_MAT, type_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "//  SmetasMatTab2Mat onAttach // " );
        mSmetaOpenHelper = new SmetaOpenHelper(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        file_id = getArguments().getLong(P.ID_FILE);
        position = getArguments().getInt(P.TAB_POSITION);
        isSelectedType = getArguments().getBoolean(P.IS_SELECTED_TYPE);
        type_id = getArguments().getLong(P.ID_TYPE_MAT);
        Log.d(TAG, "SmetasMatTab2Mat onCreate isSelectedType = " + isSelectedType +
                " file_id = " + file_id +" position = " + position+ " type_id = " + type_id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "//  SmetasMatTab2Mat onCreateView // " );
        View view = inflater.inflate(
                R.layout.fragment_tabs_for_works_and_materials, container, false);
        mListView = view.findViewById(R.id.listViewFragmentTabs);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //находим имя материала в адаптере
                TextView tv = view.findViewById(R.id.base_text);
                String mat_name = tv.getText().toString();

                //находим id материала по имени материала
                final long mat_id = mSmetaOpenHelper.getIdFromMatName(mat_name);
                Log.d(TAG, "SmetasMatTab2Mat - onItemClick  mat_id = " + mat_id +
                        "  mat_name = " + mat_name);
                // проверяем есть ли такой  материал в FM для файла с file_id
                final boolean isMat = mSmetaOpenHelper.isWorkInFM(file_id, mat_id);
                Log.d(TAG, "SmetasMatTab2Mat - onItemClick  isMat = " + isMat);

                //ищем id категории материалов, зная id типа
                long cat_id = mSmetaOpenHelper.getCatIdFromTypeMat(type_id);

                Intent intent = new Intent(getActivity(), CostMatDetail.class);
                intent.putExtra(P.ID_FILE, file_id);
                intent.putExtra(P.ID_CATEGORY_MAT, cat_id);
                intent.putExtra(P.ID_TYPE_MAT, type_id);
                intent.putExtra(P.ID_MAT, mat_id);
                intent.putExtra(P.IS_MAT, isMat);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "//  SmetasMatTab2Mat onResume // " );
        updateAdapter();
    }

    public void updateAdapter(){

        Log.d(TAG, "//  SmetasMatTab2Mat updateAdapter // " );
        Cursor cursor;
        if (isSelectedType){
            Log.d(TAG, "SmetasMatTab2Mat updateAdapter isSelectedType = true " );
            //Курсор с именами  всех материалов из таблицы Mat
            cursor = mSmetaOpenHelper.getMatNamesOneType(type_id);
        }else {
            Log.d(TAG, "SmetasMatTab2Mat updateAdapter isSelectedType = false " );
            //Курсор с именами  всех материалов из таблицы Mat
            cursor = mSmetaOpenHelper.getMatNamesAllTypes();
        }
        //Список с данными для адаптера
        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        while (cursor.moveToNext()) {
            //смотрим значение текущей строки курсора
            String name_type = cursor.getString(cursor.getColumnIndex(Mat.MAT_NAME));
            Log.d(TAG, "SmetasMatTab2Mat - updateAdapter  name_type = " + name_type);
            m = new HashMap<>();
            m.put(P.ATTR_TYPE_NAME,name_type);
            data.add(m);
        }
        String[] from = new String[]{P.ATTR_TYPE_NAME};
        int[] to = new int[]{R.id.base_text};
        sara =  new SimpleAdapter(getActivity(), data, R.layout.list_item_single_mat, from, to);
        mListView.setAdapter(sara);
    }
}
