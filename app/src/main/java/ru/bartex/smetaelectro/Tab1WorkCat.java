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
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.FW;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab1WorkCat extends Tab1SmetasCatAbstrFrag {


    public Tab1WorkCat() {
        // Required empty public constructor
    }

    public static Tab1WorkCat NewInstance(long file_id, int position){
        Log.d(TAG, "//  Tab1WorkCat NewInstance // " );
        Tab1WorkCat fragment = new Tab1WorkCat();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void updateAdapter() {
        Log.d(TAG, "//  Tab1WorkCat updateAdapter // " );
        //Курсор с именами категорий из таблицы категорий CategoryMat
        Cursor cursor = tableControllerSmeta.getCursorNames(CategoryWork.TABLE_NAME);
        //Строковый массив с именами категорий из таблицы FM для файла с file_id
        String[] catMatNamesFW = tableControllerSmeta.getArrayCategory(file_id, FW.TABLE_NAME);

        //Список с данными для адаптера
        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        Log.d(TAG, " Tab1WorkCat updateAdapter Всего категорий материалов = "+ cursor.getCount() );
        while (cursor.moveToNext()) {
            //смотрим значение текущей строки курсора
            String name_cat = cursor.getString(cursor.getColumnIndex(CategoryWork.CATEGORY_NAME));

            boolean check_mark = false;
            for (int i = 0; i<catMatNamesFW.length; i++){
                if (name_cat.equalsIgnoreCase(catMatNamesFW[i])){
                    check_mark = true;
                    //если есть совпадение, прекращаем перебор
                    break;
                }
            }
            Log.d(TAG, " Tab1WorkCat updateAdapter tipe_mat_name  = " +
                    (cursor.getPosition()+1) + " name_cat " + name_cat + "  check_mark = " + check_mark);
            m = new HashMap<>();
            m.put(P.ATTR_CATEGORY_MARK,check_mark);
            m.put(P.ATTR_CATEGORY_NAME,name_cat);
            data.add(m);
        }
        Log.d(TAG, " Tab1WorkCat updateAdapter data.size()  = "+ data.size() );
        String[] from = new String[]{P.ATTR_CATEGORY_MARK,P.ATTR_CATEGORY_NAME};
        int[] to = new int[]{R.id.checkBoxTwoMat, R.id.base_text};

        sara =  new SimpleAdapter(getActivity(), data, R.layout.list_item_two_mat, from, to);
        listView.setAdapter(sara);
    }

    @Override
    public long getCatId(String catName) {
        long cat_id = tableControllerSmeta.getIdFromName(catName, CategoryWork.TABLE_NAME);
        return cat_id;
    }
}
