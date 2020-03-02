package ru.bartex.smetaelectro;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.FW;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.TypeWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.Work;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab3Work extends Tab3SmetasWorkMatAbstrFrag {


    public Tab3Work() {
        // Required empty public constructor
    }

    public static Tab3Work NewInstance(
            long file_id, int position, boolean isSelectedType, long type_id){
        Log.d(TAG, "//  Tab3Work NewInstance // " );
        Tab3Work fragment = new Tab3Work();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        args.putBoolean(P.IS_SELECTED_TYPE, isSelectedType);
        args.putLong(P.ID_TYPE, type_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void updateAdapter() {
        Log.d(TAG, "//  Tab3Work updateAdapter // " );
        Cursor cursor;
        if (isSelectedType){
            Log.d(TAG, "Tab3Work updateAdapter isSelectedType = true " );
            //Курсор с именами работ с типом type_id
            cursor = tableControllerSmeta.getNamesFromCatId(type_id, Work.TABLE_NAME);
        }else {
            Log.d(TAG, "Tab3Work updateAdapter isSelectedType = false " );
            //Курсор с именами  всех материалов из таблицы Mat
            cursor = tableControllerSmeta.getNamesAllTypes(Work.TABLE_NAME);
        }
        //Строковый массив с именами работы из таблицы FW для файла с file_id
        String[] workNamesFW = tableControllerSmeta.getNamesFWFM(file_id, FW.TABLE_NAME);

        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        Log.d(TAG, " Tab3Work updateAdapter Всего материалов = "+ cursor.getCount() );

        while (cursor.moveToNext()){
            String mat_name = cursor.getString(cursor.getColumnIndex(Work.WORK_NAME));

            boolean check_mark = false;
            for (int i=0; i<workNamesFW.length; i++){
                if (workNamesFW[i].equals(mat_name)){
                    check_mark = true;
                    //если есть совпадение, прекращаем перебор
                    break;
                }
            }
            Log.d(TAG, " Tab3Work updateAdapter mat_name  = " +
                    (cursor.getPosition()+1) + "  " + mat_name + "  check_mark = " + check_mark);
            m =new HashMap<>();
            m.put(P.ATTR_MAT_NAME, mat_name);
            m.put(P.ATTR_MAT_MARK, check_mark);
            data.add(m);
        }
        Log.d(TAG, " Tab3Work updateAdapter data.size()  = "+ data.size() );
        String[] from = new String[]{P.ATTR_MAT_NAME, P.ATTR_MAT_MARK};
        int [] to = new int[]{R.id.base_text, R.id.checkBoxTwoMat};

        sara = new SimpleAdapter(getActivity(), data, R.layout.list_item_two_mat, from, to);
        listView.setAdapter(sara);
    }

    @Override
    public void sendIntent(String name) {
        //находим id работы по имени работы
        final long work_id = Work.getIdFromName(database, name);
        Log.d(TAG, "Tab3Work - onItemClick  work_id = " + work_id +
                "  work_name = " + name);
        // проверяем есть ли такая  работа в FW для файла с file_id
        final boolean isWork = tableControllerSmeta.isWorkMatInFWFM(file_id, work_id, FW.TABLE_NAME);
        Log.d(TAG, "Tab3Work - onItemClick  isWork = " + isWork);

        //ищем id категории работы, зная id типа
        long cat_id = TypeWork.getCatIdFromTypeId(database, type_id);

        Intent intent = new Intent(getActivity(), DetailSmetaLine.class);
        intent.putExtra(P.ID_FILE_DEFAULT, file_id);
        intent.putExtra(P.ID_CATEGORY, cat_id);
        intent.putExtra(P.ID_TYPE, type_id);
        intent.putExtra(P.ID_WORK, work_id);
        intent.putExtra(P.IS_WORK, isWork);
        startActivity(intent);
    }

}
