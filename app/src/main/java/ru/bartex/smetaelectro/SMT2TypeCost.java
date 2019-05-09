package ru.bartex.smetaelectro;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.TypeMat;

/**
 * A simple {@link Fragment} subclass.
 */
public class SMT2TypeCost extends SmetasTabType {

    public static SMT2TypeCost NewInstance(long file_id, int position, boolean isSelectedCat, long cat_id){
        Log.d(TAG, "//  SMT2TypeCost NewInstance // " );
        SMT2TypeCost fragment = new SMT2TypeCost();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        args.putBoolean(P.IS_SELECTED_CAT, isSelectedCat);
        args.putLong(P.ID_CATEGORY, cat_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void updateAdapter() {
        Log.d(TAG, "//  SMT2TypeCost updateAdapter // " );
        Cursor cursor;
        if (isSelectedCat){
            Log.d(TAG, "SMT2TypeCost updateAdapter isSelectedCat = true " );
            //Курсор  с названиями типов материалов для cat_id
            cursor = mSmetaOpenHelper.getTypeNamesOneCategory(cat_id);
        }else{
            Log.d(TAG, "SMT2TypeCost updateAdapter isSelectedCat = false " );
            //получаем курсор с названиями типов материалов по всем категориям
            cursor = mSmetaOpenHelper.getTypeMatNamesAllCategories();
        }
        //Список с данными для адаптера
        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        while (cursor.moveToNext()) {
            //смотрим значение текущей строки курсора
            String name_type = cursor.getString(cursor.getColumnIndex(TypeMat.TYPE_MAT_NAME));
            Log.d(TAG, "SMT2TypeCost - updateAdapter  name_type = " + name_type);
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
    public long getTypeId(String typeName) {
        long type_id = mSmetaOpenHelper.getIdFromMatTypeName(typeName);
        return type_id;
    }

    @Override
    public long getCatId(long type_id) {
        long cat_id = mSmetaOpenHelper.getCatIdFromTypeMat(type_id);
        return cat_id;
    }

    public SMT2TypeCost() {
        // Required empty public constructor
    }

}
