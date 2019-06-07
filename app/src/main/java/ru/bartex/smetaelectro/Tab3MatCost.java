package ru.bartex.smetaelectro;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.FM;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.Mat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.TypeMat;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab3MatCost extends Tab3SmetasWorkMatAbstrFrag {


    public Tab3MatCost() {
        // Required empty public constructor
    }

    public static Tab3MatCost NewInstance(
            long file_id, int position, boolean isSelectedType, long type_id) {
        Log.d(TAG, "//  Tab3MatCost NewInstance // " );
        Tab3MatCost fragment = new Tab3MatCost();
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
        Log.d(TAG, "//  Tab3MatCost updateAdapter // " );
        Cursor cursor;
        if (isSelectedType){
            Log.d(TAG, "Tab3MatCost updateAdapter isSelectedType = true " );
            //Курсор с именами   материалов из таблицы Mat для type_id
            cursor = tableControllerSmeta.getNamesFromCatId(type_id, Mat.TABLE_NAME);
        }else {
            Log.d(TAG, "Tab3MatCost updateAdapter isSelectedType = false " );
            //Курсор с именами  всех материалов из таблицы Mat
            cursor = tableControllerSmeta.getNamesAllTypes(Mat.TABLE_NAME);
        }
        //Список с данными для адаптера
        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        while (cursor.moveToNext()) {
            //смотрим значение текущей строки курсора
            String name_type = cursor.getString(cursor.getColumnIndex(Mat.MAT_NAME));
            Log.d(TAG, "Tab3MatCost - updateAdapter  name_type = " + name_type);
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
        final long mat_id = tableControllerSmeta.getIdFromName(name, Mat.TABLE_NAME);
        Log.d(TAG, "Tab3MatCost - onItemClick  mat_id = " + mat_id +
                "  name = " + name);
        // проверяем есть ли такой  материал в FM для файла с file_id
        final boolean isMat = tableControllerSmeta.isWorkMatInFWFM(file_id, mat_id, FM.TABLE_NAME);
        Log.d(TAG, "Tab3MatCost - onItemClick  isMat = " + isMat);

        //ищем id категории материалов, зная id типа
        long cat_id = tableControllerSmeta.getCatIdFromTypeId(type_id, TypeMat.TABLE_NAME);

        Intent intent = new Intent(getActivity(), CostMatDetail.class);
        intent.putExtra(P.ID_FILE, file_id);
        intent.putExtra(P.ID_CATEGORY_MAT, cat_id);
        intent.putExtra(P.ID_TYPE_MAT, type_id);
        intent.putExtra(P.ID_MAT, mat_id);
        intent.putExtra(P.IS_MAT, isMat);
        startActivity(intent);
    }

}
