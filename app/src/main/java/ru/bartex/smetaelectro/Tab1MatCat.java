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
public class Tab1MatCat extends Tab1SmetasCatAbstrFrag {


    public Tab1MatCat() {
        // Required empty public constructor
    }

    public static Tab1MatCat NewInstance(long file_id, int position){
        Log.d(TAG, "//  Tab1MatCat NewInstance // " );
        Tab1MatCat fragment = new Tab1MatCat();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putLong(P.TAB_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public long getCatId(String catName) {
        long cat_id = mSmetaOpenHelper.getCatIdFromCategoryMatName(catName);
        return cat_id;
    }

    @Override
    public void updateAdapter() {
        Log.d(TAG, "//  Tab1MatCat updateAdapter // " );
        //Курсор с именами категорий из таблицы категорий CategoryMat
        Cursor cursor = mSmetaOpenHelper.getMatCategoryNames();
        //Строковый массив с именами категорий из таблицы FM для файла с file_id
        String[] catMatNamesFM = mSmetaOpenHelper.getCategoryNamesFM(file_id);

        //Список с данными для адаптера
        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        Log.d(TAG, " Tab1MatCat updateAdapter Всего категорий материалов = "+ cursor.getCount() );
        while (cursor.moveToNext()) {
            //смотрим значение текущей строки курсора
            String name_cat_mat = cursor.getString(cursor.getColumnIndex(CategoryMat.CATEGORY_MAT_NAME));

            boolean check_mark = false;
            for (int i = 0; i<catMatNamesFM.length; i++){
                if (name_cat_mat.equalsIgnoreCase(catMatNamesFM[i])){
                    check_mark = true;
                    //если есть совпадение, прекращаем перебор
                    break;
                }
            }
            Log.d(TAG, " Tab1MatCat updateAdapter tipe_mat_name  = " +
                    (cursor.getPosition()+1) + "  " + name_cat_mat + "  check_mark = " + check_mark);
            m = new HashMap<>();
            m.put(P.ATTR_CATEGORY_MARK,check_mark);
            m.put(P.ATTR_CATEGORY_NAME,name_cat_mat);
            data.add(m);
        }
        Log.d(TAG, " Tab1MatCat updateAdapter data.size()  = "+ data.size() );
        String[] from = new String[]{P.ATTR_CATEGORY_MARK,P.ATTR_CATEGORY_NAME};
        int[] to = new int[]{R.id.checkBoxTwoMat, R.id.base_text};

        sara =  new SimpleAdapter(getActivity(), data, R.layout.list_item_two_mat, from, to);
        listView.setAdapter(sara);
    }
}
