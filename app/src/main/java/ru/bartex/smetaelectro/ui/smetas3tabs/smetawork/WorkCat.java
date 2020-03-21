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
import ru.bartex.smetaelectro.ui.smetas2tabs.SmetasTabRecyclerAdapter;
import ru.bartex.smetaelectro.ui.smetas3tabs.abstractfrag.AbstrSmetasCatFrag;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.SmetasCatRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkCat extends AbstrSmetasCatFrag {


    public WorkCat() {
        // Required empty public constructor
    }

    public static WorkCat newInstance(long file_id, int position){
        Log.d(TAG, "//  WorkCat newInstance // " );
        WorkCat fragment = new WorkCat();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public SmetasCatRecyclerAdapter getSmetasCatRecyclerAdapter() {
        Log.d(TAG, "//  WorkCat getSmetasCatRecyclerAdapter file_id =  "  + file_id );
        return new SmetasCatRecyclerAdapter(
                database, file_id, 0, false,0, false, 0);
    }

    @Override
    public SmetasCatRecyclerAdapter.OnClickOnNamekListener getOnClickOnNamekListener() {
        return new SmetasCatRecyclerAdapter.OnClickOnNamekListener() {
            @Override
            public void nameTransmit(String name) {
                Log.d(TAG, "//  WorkCat nameTransmit name =  "  + name );
                Toast.makeText(getActivity(), " щелчок на списке категорий ",
                        Toast.LENGTH_SHORT).show();
                long cat_id = CategoryWork.getIdFromName(database, name);
                Log.d(TAG, "//  WorkCat nameTransmit cat_id =  "  + cat_id );

                adapter.updateRecyclerAdapter(database, file_id,1,true,
                        cat_id, false, 0 );
                adapter.notifyDataSetChanged();

                onClickCatListener.catAndClickTransmit(cat_id, true);

            }
        };
    }


//    @Override
//    public void updateAdapter() {
//        Log.d(TAG, "//  WorkCat updateAdapter // " );
//        //Курсор с именами категорий из таблицы категорий CategoryMat
//        Cursor cursor = CategoryWork.getCursorNames(database);
//        //Строковый массив с именами категорий из таблицы FM для файла с file_id
//        String[] catMatNamesFW = FW.getArrayCategory(database, file_id);
//
//        //Список с данными для адаптера
//        data = new ArrayList<Map<String, Object>>(cursor.getCount());
//        Log.d(TAG, " WorkCat updateAdapter Всего категорий материалов = "+ cursor.getCount() );
//        while (cursor.moveToNext()) {
//            //смотрим значение текущей строки курсора
//            String name_cat = cursor.getString(cursor.getColumnIndex(CategoryWork.CATEGORY_NAME));
//
//            boolean check_mark = false;
//            for (int i = 0; i<catMatNamesFW.length; i++){
//                if (name_cat.equalsIgnoreCase(catMatNamesFW[i])){
//                    check_mark = true;
//                    //если есть совпадение, прекращаем перебор
//                    break;
//                }
//            }
//            Log.d(TAG, " WorkCat updateAdapter tipe_mat_name  = " +
//                    (cursor.getPosition()+1) + " name_cat " + name_cat + "  check_mark = " + check_mark);
//            m = new HashMap<>();
//            m.put(P.ATTR_CATEGORY_MARK,check_mark);
//            m.put(P.ATTR_CATEGORY_NAME,name_cat);
//            data.add(m);
//        }
//        Log.d(TAG, " WorkCat updateAdapter data.size()  = "+ data.size() );
//        String[] from = new String[]{P.ATTR_CATEGORY_MARK,P.ATTR_CATEGORY_NAME};
//        int[] to = new int[]{R.id.checkBoxTwoMat, R.id.base_text};
//
//        sara =  new SimpleAdapter(getActivity(), data, R.layout.list_item_two_mat, from, to);
//        listView.setAdapter(sara);
//    }
//
//    @Override
//    public long getCatId(String catName) {
//        long cat_id = CategoryWork.getIdFromName(database, catName);
//        return cat_id;
//    }
}
