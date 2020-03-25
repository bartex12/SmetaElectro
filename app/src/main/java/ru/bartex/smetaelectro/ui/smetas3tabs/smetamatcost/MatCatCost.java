package ru.bartex.smetaelectro.ui.smetas3tabs.smetamatcost;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ui.smetas3tabs.abstractfrag.AbstrSmetasWorkCostFrag;
import ru.bartex.smetaelectro.ui.smetas3tabs.abstractfrag.todoit.AbstrSmetasCatFrag;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.SmetasCostRecyclerAdapter;
import ru.bartex.smetaelectro.w_hlam.SmetasMatRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatCatCost extends AbstrSmetasWorkCostFrag {


    public MatCatCost() {
        // Required empty public constructor
    }

    public static MatCatCost newInstance(long file_id, int position){
        Log.d(TAG, "//  MatCatCost newInstance // " );
        MatCatCost fragment = new MatCatCost();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putLong(P.TAB_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void updateAdapter() {
//        //Курсор с именами категорий материалов
//        Cursor cursor = CategoryMat.getCursorNames(database);
//        //Список с данными для адаптера
//        data = new ArrayList<Map<String, Object>>(cursor.getCount());
//        while (cursor.moveToNext()) {
//            //смотрим значение текущей строки курсора
//            String name_cat_cost = cursor.getString(cursor.getColumnIndex(CategoryMat.CATEGORY_MAT_NAME));
//            Log.d(TAG, "MatCatCost - updateAdapter  name_cat_cost = " + name_cat_cost);
//            m = new HashMap<>();
//            m.put(P.ATTR_CAT_MAT_NAME,name_cat_cost);
//            data.add(m);
//        }
//        String[] from = new String[]{P.ATTR_CAT_MAT_NAME};
//        int[] to = new int[]{R.id.base_text};
//        sara =  new SimpleAdapter(getActivity(), data, R.layout.list_item_single_mat, from, to);
//        listView.setAdapter(sara);
//    }

//    @Override
//    public long getCatId(String catName) {
//        return CategoryMat.getIdFromName(database, catName);
//    }

    @Override
    public SmetasCostRecyclerAdapter getSmetasCostRecyclerAdapter() {

        return null;
    }

    @Override
    public SmetasCostRecyclerAdapter.OnClickOnNamekListener getOnClickOnNamekListener() {
        return null;
    }
}
