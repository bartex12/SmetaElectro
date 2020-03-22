package ru.bartex.smetaelectro.ui.smetas3tabs.costwork;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ui.smetas3tabs.abstractfrag.todoit.AbstrSmetasCatFrag;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.SmetasMatRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab1WorkCatCost extends AbstrSmetasCatFrag {


    public Tab1WorkCatCost() {
        // Required empty public constructor
    }

    public static Tab1WorkCatCost NewInstance(long file_id, int position){
        Log.d(TAG, "//  Tab1WorkCatCost newInstance // " );
        Tab1WorkCatCost fragment = new Tab1WorkCatCost();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void updateAdapter() {
//        Log.d(TAG, "//  Tab1WorkCatCost updateAdapter // " );
//        //Курсор с именами категорий из таблицы категорий CategoryMat
//        Cursor cursor = CategoryWork.getCursorNames(database);
//        //Список с данными для адаптера
//        data = new ArrayList<Map<String, Object>>(cursor.getCount());
//        Log.d(TAG, " Tab1WorkCatCost updateAdapter Всего категорий  = "+ cursor.getCount() );
//        while (cursor.moveToNext()) {
//            //смотрим значение текущей строки курсора
//            String name_cat = cursor.getString(cursor.getColumnIndex(CategoryWork.CATEGORY_NAME));
//            m = new HashMap<>();
//            m.put(P.ATTR_CATEGORY_NAME,name_cat);
//            data.add(m);
//        }
//        Log.d(TAG, " Tab1WorkCatCost updateAdapter data.size()  = "+ data.size() );
//        String[] from = new String[]{P.ATTR_CATEGORY_NAME};
//        int[] to = new int[]{R.id.base_text};
//
//        sara =  new SimpleAdapter(getActivity(), data, R.layout.list_item_single_mat, from, to);
//        listView.setAdapter(sara);
//    }

//    @Override
//    public long getCatId(String catName) {
//        return CategoryWork.getIdFromName(database, catName);
//    }

    @Override
    public SmetasMatRecyclerAdapter getSmetasCatRecyclerAdapter() {
        return null;
    }

    @Override
    public SmetasMatRecyclerAdapter.OnClickOnNamekListener getOnClickOnNamekListener() {
        return null;
    }
}
