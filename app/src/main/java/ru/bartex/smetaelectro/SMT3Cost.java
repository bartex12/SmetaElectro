package ru.bartex.smetaelectro;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.Mat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;

/**
 * A simple {@link Fragment} subclass.
 */
public class SMT3Cost extends SmetasMWTab {


    public SMT3Cost() {
        // Required empty public constructor
    }

    public static SMT3Cost NewInstance(
            long file_id, int position, boolean isSelectedType, long type_id) {
        Log.d(TAG, "//  SMT3Cost NewInstance // " );
        SMT3Cost fragment = new SMT3Cost();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        args.putBoolean(P.IS_SELECTED_TYPE, isSelectedType);
        args.putLong(P.ID_TYPE, type_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void updateAdapter() {
        Log.d(TAG, "//  SMT3Cost updateAdapter // " );
        Cursor cursor;
        if (isSelectedType){
            Log.d(TAG, "SMT3Cost updateAdapter isSelectedType = true " );
            //Курсор с именами   материалов из таблицы Mat для type_id
            cursor = mSmetaOpenHelper.getMatNamesOneType(type_id);
        }else {
            Log.d(TAG, "SMT3Cost updateAdapter isSelectedType = false " );
            //Курсор с именами  всех материалов из таблицы Mat
            cursor = mSmetaOpenHelper.getMatNamesAllTypes();
        }
        //Список с данными для адаптера
        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        while (cursor.moveToNext()) {
            //смотрим значение текущей строки курсора
            String name_type = cursor.getString(cursor.getColumnIndex(Mat.MAT_NAME));
            Log.d(TAG, "SMT3Cost - updateAdapter  name_type = " + name_type);
            m = new HashMap<>();
            m.put(P.ATTR_TYPE_NAME,name_type);
            data.add(m);
        }
        String[] from = new String[]{P.ATTR_TYPE_NAME};
        int[] to = new int[]{R.id.base_text};
        sara =  new SimpleAdapter(getActivity(), data, R.layout.list_item_single_mat, from, to);
       listView.setAdapter(sara);
    }

    @Override
    public void sendIntent(String name) {
        //находим id материала по имени материала
        final long mat_id = mSmetaOpenHelper.getIdFromMatName(name);
        Log.d(TAG, "SMT3Cost - onItemClick  mat_id = " + mat_id +
                "  name = " + name);
        // проверяем есть ли такой  материал в FM для файла с file_id
        final boolean isMat = mSmetaOpenHelper.isMatInFM(file_id, mat_id);
        Log.d(TAG, "SMT3Cost - onItemClick  isMat = " + isMat);

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

}
