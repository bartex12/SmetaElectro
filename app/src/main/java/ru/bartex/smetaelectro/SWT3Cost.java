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

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.Work;

/**
 * A simple {@link Fragment} subclass.
 */
public class SWT3Cost extends SmetasMWTab {


    public SWT3Cost() {
        // Required empty public constructor
    }

    public static SWT3Cost NewInstance(
            long file_id, int position, boolean isSelectedType, long type_id){
        Log.d(TAG, "//  SWT3Cost NewInstance // " );
        SWT3Cost fragment = new SWT3Cost();
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
        Log.d(TAG, "//  SWT3Cost updateAdapter // " );
        Cursor cursor;
        if (isSelectedType){
            Log.d(TAG, "SWT3Cost updateAdapter isSelectedType = true " );
            //Курсор с именами работ с типом type_id
            cursor = mSmetaOpenHelper.getWorkNames(type_id);
        }else {
            Log.d(TAG, "SWT3Cost updateAdapter isSelectedType = false " );
            //Курсор с именами  всех материалов из таблицы Mat
            cursor = mSmetaOpenHelper.getWorkNamesAllTypes();
        }
        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        Log.d(TAG, " SWT3Cost updateAdapter Всего материалов = "+ cursor.getCount() );

        while (cursor.moveToNext()){
            String mat_name = cursor.getString(cursor.getColumnIndex(Work.WORK_NAME));
            m =new HashMap<>();
            m.put(P.ATTR_MAT_NAME, mat_name);
            data.add(m);
        }
        Log.d(TAG, " SWT3Cost updateAdapter data.size()  = "+ data.size() );
        String[] from = new String[]{P.ATTR_MAT_NAME};
        int [] to = new int[]{R.id.base_text};

        sara = new SimpleAdapter(getActivity(), data, R.layout.list_item_single_mat, from, to);
        listView.setAdapter(sara);
    }

    @Override
    public void sendIntent(String name) {
//находим id по имени работы
        long work_id = mSmetaOpenHelper.getIdFromWorkName(name);
        //ищем id категории работы, зная id типа
        long cat_id = mSmetaOpenHelper.getCatIdFromTypeWork(type_id);

        Intent intent = new Intent(getActivity(), CostDetail.class);
        intent.putExtra(P.ID_CATEGORY, cat_id);
        intent.putExtra(P.ID_TYPE, type_id);
        intent.putExtra(P.ID_WORK, work_id);
        startActivity(intent);
    }


}
