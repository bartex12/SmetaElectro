package ru.bartex.smetaelectro.ui.smetas3tabs.smetawork;


import android.database.Cursor;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.CategoryWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.FW;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.TypeWork;
import ru.bartex.smetaelectro.ui.smetas3tabs.abstractfrag.AbstrSmetasTypeFrag;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.SmetasCatRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkType extends AbstrSmetasTypeFrag {


    public static WorkType newInstance(
            long file_id, int position, boolean isSelectedCat, long cat_id){
        Log.d(TAG, "//  WorkType newInstance // " );
        WorkType fragment = new WorkType();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        args.putBoolean(P.IS_SELECTED_CAT, isSelectedCat);
        args.putLong(P.ID_CATEGORY, cat_id);
        fragment.setArguments(args);
        return fragment;
    }

    public WorkType() {
        // Required empty public constructor
    }


//    @Override
//    public void updateAdapter() {
//        Log.d(TAG, "//  WorkType updateAdapter // " );
//
//        Cursor cursor;
//        if (isSelectedCat){
//            Log.d(TAG, "WorkType updateAdapter isSelectedCat = true " );
//            //курсор с именами типов работы для категорий с cat_id
//            cursor = TypeWork.getNamesFromCatId(database, cat_id);
//        }else {
//            Log.d(TAG, "WorkType updateAdapter isSelectedCat = false " );
//            //получаем курсор с названиями типов работ по всем категориям
//            cursor = TypeWork.getCursorNames(database);
//        }
//        //Строковый массив с именами типов из таблицы FW для файла с file_id
//        String[] typetNamesFW = FW.getTypeNames(database, file_id);
//
//        data = new ArrayList<Map<String, Object>>(cursor.getCount());
//        Log.d(TAG, " WorkType updateAdapter Всего типов материалов = "+ cursor.getCount() );
//
//        while (cursor.moveToNext()){
//            String tipe_mat_name = cursor.getString(cursor.getColumnIndex(TypeWork.TYPE_NAME));
//            boolean check_mark = false;
//            for (String s : typetNamesFW) {
//                if (s.equals(tipe_mat_name)) {
//                    check_mark = true;
//                    //если есть совпадение, прекращаем перебор
//                    break;
//                }
//            }
//            Log.d(TAG, " WorkType updateAdapter tipe_mat_name  = " +
//                    (cursor.getPosition()+1) + "  " + tipe_mat_name + "  check_mark = " + check_mark);
//            m =new HashMap<>();
//            m.put(P.ATTR_TYPE_MAT_NAME,tipe_mat_name);
//            m.put(P.ATTR_TYPE_MAT_MARK, check_mark);
//            data.add(m);
//        }
//        Log.d(TAG, " WorkType updateAdapter data.size()  = "+ data.size() );
//        String[] from = new String[]{P.ATTR_TYPE_MAT_NAME, P.ATTR_TYPE_MAT_MARK};
//        int [] to = new int[]{R.id.base_text, R.id.checkBoxTwoMat};
//
//        sara = new SimpleAdapter(getActivity(), data, R.layout.list_item_two_mat, from, to);
//        listView.setAdapter(sara);
//    }
//
//    @Override
//    public long getTypeId(String typeName) {
//        return TypeWork.getIdFromName(database, typeName);
//    }
//
//    @Override
//    public long getCatId(long type_id) {
//        return TypeWork.getCatIdFromTypeId(database, type_id);
//    }

    @Override
    public SmetasCatRecyclerAdapter getSmetasCatRecyclerAdapter() {
        Log.d(TAG, "//  WorkType getSmetasCatRecyclerAdapter file_id =  "  + file_id );
        return new SmetasCatRecyclerAdapter(
                database, file_id, 1, false,0, false, 0);

    }

    @Override
    public SmetasCatRecyclerAdapter.OnClickOnNamekListener getOnClickOnNamekListener() {
        return new SmetasCatRecyclerAdapter.OnClickOnNamekListener() {
            @Override
            public void nameTransmit(String name) {
                Log.d(TAG, "//  WorkType nameTransmit name =  "  + name );
                Toast.makeText(getActivity(), " щелчок на списке типов ",
                        Toast.LENGTH_SHORT).show();
                long type_id = TypeWork.getIdFromName(database, name);
                long cat_id = TypeWork.getCatIdFromTypeId(database, type_id);
                Log.d(TAG, "WorkType nameTransmit  cat_id = " +cat_id + "  type_id = " + type_id);
                adapter.updateRecyclerAdapter(database, file_id,2,true,
                        cat_id, true, type_id );
                adapter.notifyDataSetChanged();

                onClickTypeListener.typeAndClickTransmit(cat_id, type_id, true);
            }
        };
    }
}
