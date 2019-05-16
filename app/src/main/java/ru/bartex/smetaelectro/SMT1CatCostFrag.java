package ru.bartex.smetaelectro;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.CategoryMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;

/**
 * A simple {@link Fragment} subclass.
 */
public class SMT1CatCostFrag extends SmetasTabCatAbstrFrag {


    public SMT1CatCostFrag() {
        // Required empty public constructor
    }

    public static SMT1CatCostFrag NewInstance(long file_id, int position){
        Log.d(TAG, "//  SMT1CatCostFrag NewInstance // " );
        SMT1CatCostFrag fragment = new SMT1CatCostFrag();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putLong(P.TAB_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void updateAdapter() {
        //Курсор с именами категорий материалов
        Cursor cursor = mSmetaOpenHelper.getMatCategoryNames();
        //Список с данными для адаптера
        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        while (cursor.moveToNext()) {
            //смотрим значение текущей строки курсора
            String name_cat_cost = cursor.getString(cursor.getColumnIndex(CategoryMat.CATEGORY_MAT_NAME));
            Log.d(TAG, "SMT1CatCostFrag - updateAdapter  name_cat_cost = " + name_cat_cost);
            m = new HashMap<>();
            m.put(P.ATTR_CAT_MAT_NAME,name_cat_cost);
            data.add(m);
        }
        String[] from = new String[]{P.ATTR_CAT_MAT_NAME};
        int[] to = new int[]{R.id.base_text};
        sara =  new SimpleAdapter(getActivity(), data, R.layout.list_item_single_mat, from, to);
        listView.setAdapter(sara);
    }

    @Override
    public long getCatId(String catName) {
        long cat_id = mSmetaOpenHelper.getCatIdFromCategoryMatName(catName);
        return cat_id;
    }
}
