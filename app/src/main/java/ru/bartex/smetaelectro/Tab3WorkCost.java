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

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.TypeWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.Work;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab3WorkCost extends Tab3SmetasWorkMatAbstrFrag {


    public Tab3WorkCost() {
        // Required empty public constructor
    }

    public static Tab3WorkCost NewInstance(
            long file_id, int position, boolean isSelectedType, long type_id){
        Log.d(TAG, "//  Tab3WorkCost NewInstance // " );
        Tab3WorkCost fragment = new Tab3WorkCost();
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
        Log.d(TAG, "//  Tab3WorkCost updateAdapter // " );
        Cursor cursor;
        if (isSelectedType){
            Log.d(TAG, "Tab3WorkCost updateAdapter isSelectedType = true " );
            //Курсор с именами работ с типом type_id
            cursor =tableControllerSmeta.getNamesFromCatId(type_id, Work.TABLE_NAME);
        }else {
            Log.d(TAG, "Tab3WorkCost updateAdapter isSelectedType = false " );
            //Курсор с именами  всех работ из таблицы Work
            cursor = tableControllerSmeta.getNamesAllTypes(Work.TABLE_NAME);
        }
        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        Log.d(TAG, " Tab3WorkCost updateAdapter Всего материалов = "+ cursor.getCount() );

        while (cursor.moveToNext()){
            String mat_name = cursor.getString(cursor.getColumnIndex(Work.WORK_NAME));
            m =new HashMap<>();
            m.put(P.ATTR_MAT_NAME, mat_name);
            data.add(m);
        }
        Log.d(TAG, " Tab3WorkCost updateAdapter data.size()  = "+ data.size() );
        String[] from = new String[]{P.ATTR_MAT_NAME};
        int [] to = new int[]{R.id.base_text};

        sara = new SimpleAdapter(getActivity(), data, R.layout.list_item_single_mat, from, to);
        listView.setAdapter(sara);
    }

    @Override
    public void sendIntent(String name) {
//находим id по имени работы
        long work_id = tableControllerSmeta.
                getIdFromName(name, Work.TABLE_NAME);
        //ищем id категории работы, зная id типа
        long cat_id = tableControllerSmeta.getCatIdFromTypeId(type_id, TypeWork.TABLE_NAME);

        Intent intent = new Intent(getActivity(), DetailCost.class);
        intent.putExtra(P.ID_CATEGORY, cat_id);
        intent.putExtra(P.ID_TYPE, type_id);
        intent.putExtra(P.ID_WORK, work_id);
        startActivity(intent);
    }


}
