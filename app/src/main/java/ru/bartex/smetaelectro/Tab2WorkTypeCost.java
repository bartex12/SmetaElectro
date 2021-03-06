package ru.bartex.smetaelectro;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.FW;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.TypeWork;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab2WorkTypeCost extends Tab2SmetasTypeAbstrFrag {

    public static Tab2WorkTypeCost NewInstance(
            long file_id, int position, boolean isSelectedCat, long cat_id){
        Log.d(TAG, "//  Tab2WorkTypeCost NewInstance // " );
        Tab2WorkTypeCost fragment = new Tab2WorkTypeCost();
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
        Log.d(TAG, "//  Tab2WorkTypeCost updateAdapter // " );

        Cursor cursor;
        if (isSelectedCat){
            Log.d(TAG, "Tab2WorkTypeCost updateAdapter isSelectedCat = true " );
            //курсор с именами типов работы для категорий с cat_id
            cursor = tableControllerSmeta.getNamesFromCatId(cat_id, TypeWork.TABLE_NAME);
        }else {
            Log.d(TAG, "Tab2WorkTypeCost updateAdapter isSelectedCat = false " );
            //получаем курсор с названиями типов работ по всем категориям
            cursor =tableControllerSmeta.getCursorNames(TypeWork.TABLE_NAME );
        }
        //Строковый массив с именами типов из таблицы FW для файла с file_id
        String[] typetNamesFW = tableControllerSmeta.getTypeNames(file_id, FW.TABLE_NAME);

        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        Log.d(TAG, " Tab2WorkTypeCost updateAdapter Всего типов материалов = "+ cursor.getCount() );

        while (cursor.moveToNext()){
            String tipe_mat_name = cursor.getString(cursor.getColumnIndex(TypeWork.TYPE_NAME));
            m =new HashMap<>();
            m.put(P.ATTR_TYPE_MAT_NAME,tipe_mat_name);
            data.add(m);
        }
        Log.d(TAG, " Tab2WorkTypeCost updateAdapter data.size()  = "+ data.size() );
        String[] from = new String[]{P.ATTR_TYPE_MAT_NAME};
        int [] to = new int[]{R.id.base_text};

        sara = new SimpleAdapter(getActivity(), data, R.layout.list_item_single_mat, from, to);
        listView.setAdapter(sara);
    }

    @Override
    public long getTypeId(String typeName) {
        long type_id = tableControllerSmeta.
                getIdFromName(typeName, TypeWork.TABLE_NAME);
        return type_id;
    }

    @Override
    public long getCatId(long type_id) {
        long cat_id = tableControllerSmeta.getCatIdFromTypeId(type_id, TypeWork.TABLE_NAME);
        return cat_id;
    }

    public Tab2WorkTypeCost() {
        // Required empty public constructor
    }

}
