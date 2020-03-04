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
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.FM;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.Mat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.TypeMat;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab3Mat extends Tab3SmetasWorkMatAbstrFrag {


    public Tab3Mat() {
        // Required empty public constructor
    }

    public static Tab3Mat NewInstance(
            long file_id, int position, boolean isSelectedType, long type_id){
        Log.d(TAG, "//  Tab3Mat NewInstance // " );
        Tab3Mat fragment = new Tab3Mat();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        args.putBoolean(P.IS_SELECTED_TYPE, isSelectedType);
        args.putLong(P.ID_TYPE, type_id);
        fragment.setArguments(args);
        Log.d(TAG, "Tab3Mat  NewInstance isSelectedType = " +
                isSelectedType + "  type_id = " +  type_id + "  file_id = " +  file_id +
                "  position = " +  position);
        return fragment;
    }

    @Override
    public void updateAdapter() {
        Log.d(TAG, "//  Tab3Mat updateAdapter // " );
        Cursor cursor;
        if (isSelectedType){
            Log.d(TAG, "Tab3Mat updateAdapter isSelectedType = true " );
            //Курсор с именами  всех материалов из таблицы Mat
            cursor = Mat.getNamesFromCatId(database, type_id);
        }else {
            Log.d(TAG, "Tab3Mat updateAdapter isSelectedType = false " );
            //Курсор с именами  всех материалов из таблицы Mat
            cursor = Mat.getNamesAllTypes(database);
        }

        //Строковый массив с именами типов материалов из таблицы FW для файла с file_id
        String[] matNamesFM = FM.getNames_FM(database, file_id);

        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        Log.d(TAG, " Tab3Mat updateAdapter Всего материалов = "+ cursor.getCount() );
        while (cursor.moveToNext()){
            String mat_name = cursor.getString(cursor.getColumnIndex(Mat.MAT_NAME));

            boolean check_mark = false;
            for (int i=0; i<matNamesFM.length; i++){
                if (matNamesFM[i].equals(mat_name)){
                    check_mark = true;
                    //если есть совпадение, прекращаем перебор
                    break;
                }
            }
            Log.d(TAG, " Tab3Mat updateAdapter mat_name  = " +
                    (cursor.getPosition()+1) + "  " + mat_name + "  check_mark = " + check_mark);
            m =new HashMap<>();
            m.put(P.ATTR_MAT_NAME, mat_name);
            m.put(P.ATTR_MAT_MARK, check_mark);
            data.add(m);
        }
        Log.d(TAG, " Tab3Mat updateAdapter data.size()  = "+ data.size() );
        String[] from = new String[]{P.ATTR_MAT_NAME, P.ATTR_MAT_MARK};
        int [] to = new int[]{R.id.base_text, R.id.checkBoxTwoMat};

        sara = new SimpleAdapter(getActivity(), data, R.layout.list_item_two_mat, from, to);
        listView.setAdapter(sara);
    }

    @Override
    public void sendIntent(String name) {
//находим id материала по имени материала
        final long mat_id = Mat.getIdFromName(database, name);
        Log.d(TAG, "Tab3Mat - listView.onItemClick  mat_id = " + mat_id +
                "  name = " + name);
        // проверяем есть ли такой  материал в FM для файла с file_id
        final boolean isMat = tableControllerSmeta.isWorkMatInFWFM(file_id, mat_id, FM.TABLE_NAME);
        Log.d(TAG, "Tab3Mat - onItemClick  isMat = " + isMat);
        Log.d(TAG, "Tab3Mat - onItemClick  type_id = " + type_id);
        //ищем id категории материалов, зная id типа
        long cat_id = TypeMat.getCatIdFromTypeId(database, type_id);
        Log.d(TAG, "Tab3Mat - listView.onItemClick  file_id = " + file_id +
                "  cat_id = " + cat_id + "  type_id = " + type_id +
                "  mat_id = " + mat_id + "  isMat = " + isMat);
        Intent intent = new Intent(getActivity(), DetailSmetaMatLine.class);
        intent.putExtra(P.ID_FILE, file_id);
        intent.putExtra(P.ID_CATEGORY_MAT, cat_id);
        intent.putExtra(P.ID_TYPE_MAT, type_id);
        intent.putExtra(P.ID_MAT, mat_id);
        intent.putExtra(P.IS_MAT, isMat);
        startActivity(intent);
    }

}
