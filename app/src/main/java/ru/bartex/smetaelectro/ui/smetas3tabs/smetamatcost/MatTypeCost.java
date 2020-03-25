package ru.bartex.smetaelectro.ui.smetas3tabs.smetamatcost;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ui.smetas3tabs.abstractfrag.AbstrSmetasWorkCostFrag;
import ru.bartex.smetaelectro.ui.smetas3tabs.abstractfrag.todoit.AbstrSmetasTypeFrag;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.SmetasCostRecyclerAdapter;
import ru.bartex.smetaelectro.w_hlam.SmetasMatRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatTypeCost extends AbstrSmetasWorkCostFrag {

    public MatTypeCost() {
        // Required empty public constructor
    }

    public static MatTypeCost newInstance(long file_id, int position, boolean isSelectedCat, long cat_id){
        Log.d(TAG, "//  MatTypeCost newInstance // " );
        MatTypeCost fragment = new MatTypeCost();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        args.putBoolean(P.IS_SELECTED_CAT, isSelectedCat);
        args.putLong(P.ID_CATEGORY, cat_id);
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void updateAdapter() {
//        Log.d(TAG, "//  MatTypeCost updateAdapter // " );
//        Cursor cursor;
//        if (isSelectedCat){
//            Log.d(TAG, "MatTypeCost updateAdapter isSelectedCat = true " );
//            //Курсор  с названиями типов материалов для cat_id
//            cursor = TypeMat.getNamesFromCatId(database, cat_id);
//        }else{
//            Log.d(TAG, "MatTypeCost updateAdapter isSelectedCat = false " );
//            //получаем курсор с названиями типов материалов по всем категориям
//            cursor =TypeMat.getCursorNames(database);
//        }
//        //Список с данными для адаптера
//        data = new ArrayList<Map<String, Object>>(cursor.getCount());
//        while (cursor.moveToNext()) {
//            //смотрим значение текущей строки курсора
//            String name_type = cursor.getString(cursor.getColumnIndex(TypeMat.TYPE_MAT_NAME));
//            Log.d(TAG, "MatTypeCost - updateAdapter  name_type = " + name_type);
//            m = new HashMap<>();
//            m.put(P.ATTR_TYPE_NAME,name_type);
//            data.add(m);
//        }
//        String[] from = new String[]{P.ATTR_TYPE_NAME};
//        int[] to = new int[]{R.id.base_text};
//        sara =  new SimpleAdapter(getActivity(), data, R.layout.list_item_single_mat, from, to);
//        listView.setAdapter(sara);
//    }
//
//    @Override
//    public long getTypeId(String typeName) {
//        return TypeMat.getIdFromName(database, typeName);
//    }
//
//    @Override
//    public long getCatId(long type_id) {
//        return TypeMat.getCatIdFromTypeId(database, type_id);
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
