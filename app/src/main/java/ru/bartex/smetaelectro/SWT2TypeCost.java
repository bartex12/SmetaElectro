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
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.TypeWork;

/**
 * A simple {@link Fragment} subclass.
 */
public class SWT2TypeCost extends SmetasTabType {

    public static SWT2TypeCost NewInstance(
            long file_id, int position, boolean isSelectedCat, long cat_id){
        Log.d(TAG, "//  SWT2TypeCost NewInstance // " );
        SWT2TypeCost fragment = new SWT2TypeCost();
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
        Log.d(TAG, "//  SWT2TypeCost updateAdapter // " );

        Cursor cursor;
        if (isSelectedCat){
            Log.d(TAG, "SWT2TypeCost updateAdapter isSelectedCat = true " );
            //курсор с именами типов работы для категорий с cat_id
            cursor = mSmetaOpenHelper.getTypeNames(cat_id);
        }else {
            Log.d(TAG, "SWT2TypeCost updateAdapter isSelectedCat = false " );
            //получаем курсор с названиями типов работ по всем категориям
            cursor = mSmetaOpenHelper.getTypeWorkNamesAllCategories();
        }
        //Строковый массив с именами типов из таблицы FW для файла с file_id
        String[] typetNamesFW = mSmetaOpenHelper.getTypeNamesFW(file_id);

        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        Log.d(TAG, " SWT2TypeCost updateAdapter Всего типов материалов = "+ cursor.getCount() );

        while (cursor.moveToNext()){
            String tipe_mat_name = cursor.getString(cursor.getColumnIndex(TypeWork.TYPE_NAME));
            m =new HashMap<>();
            m.put(P.ATTR_TYPE_MAT_NAME,tipe_mat_name);
            data.add(m);
        }
        Log.d(TAG, " SWT2TypeCost updateAdapter data.size()  = "+ data.size() );
        String[] from = new String[]{P.ATTR_TYPE_MAT_NAME};
        int [] to = new int[]{R.id.base_text};

        sara = new SimpleAdapter(getActivity(), data, R.layout.list_item_single_mat, from, to);
        listView.setAdapter(sara);
    }

    @Override
    public long getTypeId(String typeName) {
        long type_id = mSmetaOpenHelper.getIdFromTypeName(typeName);
        return type_id;
    }

    @Override
    public long getCatId(long type_id) {
        long cat_id = mSmetaOpenHelper.getCatIdFromTypeWork(type_id);
        return cat_id;
    }

    public SWT2TypeCost() {
        // Required empty public constructor
    }

}
