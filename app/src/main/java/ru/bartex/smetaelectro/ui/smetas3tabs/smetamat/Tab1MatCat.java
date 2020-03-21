package ru.bartex.smetaelectro.ui.smetas3tabs.smetamat;


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
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.CategoryMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.FM;
import ru.bartex.smetaelectro.ui.smetas3tabs.abstractfrag.AbstrSmetasCatFrag;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.SmetasCatRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab1MatCat extends AbstrSmetasCatFrag {


    public Tab1MatCat() {
        // Required empty public constructor
    }

    public static Tab1MatCat NewInstance(long file_id, int position){
        Log.d(TAG, "//  Tab1MatCat newInstance // " );
        Tab1MatCat fragment = new Tab1MatCat();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putLong(P.TAB_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public long getCatId(String catName) {
//        return CategoryMat.getIdFromName(database, catName);
//    }

//    @Override
//    public void updateAdapter() {
//        Log.d(TAG, "//  Tab1MatCat updateAdapter // " );
//        //Курсор с именами категорий из таблицы категорий CategoryMat
//        Cursor cursor = CategoryMat.getCursorNames(database);
//        //Строковый массив с именами категорий из таблицы FM для файла с file_id
//        String[] catMatNamesFM =FM.getArrayCategory(database, file_id);
//
//        //Список с данными для адаптера
//        data = new ArrayList<Map<String, Object>>(cursor.getCount());
//        Log.d(TAG, " Tab1MatCat updateAdapter Всего категорий материалов = "+ cursor.getCount() );
//        while (cursor.moveToNext()) {
//            //смотрим значение текущей строки курсора
//            String name_cat_mat = cursor.getString(cursor.getColumnIndex(CategoryMat.CATEGORY_MAT_NAME));
//
//            boolean check_mark = false;
//            for (String s : catMatNamesFM) {
//                if (name_cat_mat.equalsIgnoreCase(s)) {
//                    check_mark = true;
//                    //если есть совпадение, прекращаем перебор
//                    break;
//                }
//            }
//            Log.d(TAG, " Tab1MatCat updateAdapter tipe_mat_name  = " +
//                    (cursor.getPosition()+1) + "  " + name_cat_mat + "  check_mark = " + check_mark);
//            m = new HashMap<>();
//            m.put(P.ATTR_CATEGORY_MARK,check_mark);
//            m.put(P.ATTR_CATEGORY_NAME,name_cat_mat);
//            data.add(m);
//        }
//        Log.d(TAG, " Tab1MatCat updateAdapter data.size()  = "+ data.size() );
//        String[] from = new String[]{P.ATTR_CATEGORY_MARK,P.ATTR_CATEGORY_NAME};
//        int[] to = new int[]{R.id.checkBoxTwoMat, R.id.base_text};
//
//        sara =  new SimpleAdapter(getActivity(), data, R.layout.list_item_two_mat, from, to);
//        listView.setAdapter(sara);
//    }

    @Override
    public SmetasCatRecyclerAdapter getSmetasCatRecyclerAdapter() {
        return null;
    }

    @Override
    public SmetasCatRecyclerAdapter.OnClickOnNamekListener getOnClickOnNamekListener() {
        return null;
    }
}
