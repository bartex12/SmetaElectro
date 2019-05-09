package ru.bartex.smetaelectro;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.CategoryWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;

/**
 * A simple {@link Fragment} subclass.
 */
public class SWT1CostCat extends SmetasCatTab {


    public SWT1CostCat() {
        // Required empty public constructor
    }

    public static SWT1CostCat NewInstance(long file_id, int position){
        Log.d(TAG, "//  SWT1CostCat NewInstance // " );
        SWT1CostCat fragment = new SWT1CostCat();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void updateAdapter() {
        Log.d(TAG, "//  SWT1CostCat updateAdapter // " );
        //Курсор с именами категорий из таблицы категорий CategoryMat
        Cursor cursor = mSmetaOpenHelper.getCategoryNames();
        //Список с данными для адаптера
        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        Log.d(TAG, " SWT1CostCat updateAdapter Всего категорий  = "+ cursor.getCount() );
        while (cursor.moveToNext()) {
            //смотрим значение текущей строки курсора
            String name_cat = cursor.getString(cursor.getColumnIndex(CategoryWork.CATEGORY_NAME));
            m = new HashMap<>();
            m.put(P.ATTR_CATEGORY_NAME,name_cat);
            data.add(m);
        }
        Log.d(TAG, " SWT1CostCat updateAdapter data.size()  = "+ data.size() );
        String[] from = new String[]{P.ATTR_CATEGORY_NAME};
        int[] to = new int[]{R.id.base_text};

        sara =  new SimpleAdapter(getActivity(), data, R.layout.list_item_single_mat, from, to);
        listView.setAdapter(sara);
    }

    @Override
    public long getCatId(String catName) {
        long cat_id = mSmetaOpenHelper.getIdFromCategoryName(catName);
        return cat_id;
    }

}
