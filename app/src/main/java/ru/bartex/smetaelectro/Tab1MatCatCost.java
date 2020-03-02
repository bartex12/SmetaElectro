package ru.bartex.smetaelectro;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.CategoryMat;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab1MatCatCost extends Tab1SmetasCatAbstrFrag {


    public Tab1MatCatCost() {
        // Required empty public constructor
    }

    public static Tab1MatCatCost NewInstance(long file_id, int position){
        Log.d(TAG, "//  Tab1MatCatCost NewInstance // " );
        Tab1MatCatCost fragment = new Tab1MatCatCost();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putLong(P.TAB_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void updateAdapter() {
        //Курсор с именами категорий материалов
        Cursor cursor = tableControllerSmeta.getCursorNames(CategoryMat.TABLE_NAME);
        //Список с данными для адаптера
        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        while (cursor.moveToNext()) {
            //смотрим значение текущей строки курсора
            String name_cat_cost = cursor.getString(cursor.getColumnIndex(CategoryMat.CATEGORY_MAT_NAME));
            Log.d(TAG, "Tab1MatCatCost - updateAdapter  name_cat_cost = " + name_cat_cost);
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
        long cat_id = CategoryMat.getIdFromName(database, catName);
        return cat_id;
    }
}
