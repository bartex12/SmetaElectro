package ru.bartex.smetaelectro;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.TypeWork;

/**
 * A simple {@link Fragment} subclass.
 */
public class SWT2Type extends SmetasTabType {


    public static SWT2Type NewInstance(
            long file_id, int position, boolean isSelectedCat, long cat_id){
        Log.d(TAG, "//  SmetasWorkTab2Type NewInstance // " );
        SWT2Type fragment = new SWT2Type();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        args.putBoolean(P.IS_SELECTED_CAT, isSelectedCat);
        args.putLong(P.ID_CATEGORY, cat_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void updateAdapter() {
        Log.d(TAG, "//  SWT2Type updateAdapter // " );

        Cursor cursor;
        if (isSelectedCat){
            Log.d(TAG, "SWT2Type updateAdapter isSelectedCat = true " );
            //курсор с именами типов работы для категорий с cat_id
            cursor = mSmetaOpenHelper.getTypeNames(cat_id);
        }else {
            Log.d(TAG, "SWT2Type updateAdapter isSelectedCat = false " );
            //получаем курсор с названиями типов работ по всем категориям
            cursor = mSmetaOpenHelper.getTypeWorkNamesAllCategories();
        }
        //Строковый массив с именами типов из таблицы FW для файла с file_id
        String[] typetNamesFW = mSmetaOpenHelper.getTypeNamesFW(file_id);

        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        Log.d(TAG, " SWT2Type updateAdapter Всего типов материалов = "+ cursor.getCount() );

        while (cursor.moveToNext()){
            String tipe_mat_name = cursor.getString(cursor.getColumnIndex(TypeWork.TYPE_NAME));
            boolean check_mark = false;
            for (int i=0; i<typetNamesFW.length; i++){
                if (typetNamesFW[i].equals(tipe_mat_name)){
                    check_mark = true;
                    //если есть совпадение, прекращаем перебор
                    break;
                }
            }
            Log.d(TAG, " SWT2Type updateAdapter tipe_mat_name  = " +
                    (cursor.getPosition()+1) + "  " + tipe_mat_name + "  check_mark = " + check_mark);
            m =new HashMap<>();
            m.put(P.ATTR_TYPE_MAT_NAME,tipe_mat_name);
            m.put(P.ATTR_TYPE_MAT_MARK, check_mark);
            data.add(m);
        }
        Log.d(TAG, " SWT2Type updateAdapter data.size()  = "+ data.size() );
        String[] from = new String[]{P.ATTR_TYPE_MAT_NAME, P.ATTR_TYPE_MAT_MARK};
        int [] to = new int[]{R.id.base_text, R.id.checkBoxTwoMat};

        sara = new SimpleAdapter(getActivity(), data, R.layout.list_item_two_mat, from, to);
        listView.setAdapter(sara);
    }

    @Override
    public long getTypeId(String typeName) {
        long type_id = mSmetaOpenHelper.getIdFromTypeName(typeName);
        return type_id;
    }

    @Override
    public long getCatId(long type_id) {
        long cat_id = mSmetaOpenHelper.getCatIdFromTypeWork(type_id);
        return cat_id;
    }

    public SWT2Type() {
        // Required empty public constructor
    }




}
