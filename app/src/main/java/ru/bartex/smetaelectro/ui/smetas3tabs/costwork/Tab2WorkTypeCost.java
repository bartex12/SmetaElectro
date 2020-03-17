package ru.bartex.smetaelectro.ui.smetas3tabs.costwork;


import android.database.Cursor;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.FW;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.TypeWork;
import ru.bartex.smetaelectro.ui.smetas3tabs.abstractfrag.AbstrSmetasTypeFrag;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab2WorkTypeCost extends AbstrSmetasTypeFrag {

    public static Tab2WorkTypeCost NewInstance(
            long file_id, int position, boolean isSelectedCat, long cat_id){
        Log.d(TAG, "//  Tab2WorkTypeCost newInstance // " );
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
            cursor = TypeWork.getNamesFromCatId(database, cat_id);
        }else {
            Log.d(TAG, "Tab2WorkTypeCost updateAdapter isSelectedCat = false " );
            //получаем курсор с названиями типов работ по всем категориям
            cursor =TypeWork.getCursorNames(database );
        }
        //Строковый массив с именами типов из таблицы FW для файла с file_id
        String[] typetNamesFW = FW.getTypeNames(database, file_id);

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
        return TypeWork.getIdFromName(database, typeName);
    }

    @Override
    public long getCatId(long type_id) {
        return TypeWork.getCatIdFromTypeId(database, type_id);
    }

    public Tab2WorkTypeCost() {
        // Required empty public constructor
    }

}
