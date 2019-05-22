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
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.TypeMat;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab2MatType extends Tab2SmetasTypeAbstrFrag {

    public static Tab2MatType NewInstance(long file_id, int position, boolean isSelectedCat, long cat_id){
        Log.d(TAG, "//  Tab2MatType NewInstance // " );
        Tab2MatType fragment = new Tab2MatType();
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
        Log.d(TAG, "//  Tab2MatType updateAdapter // " );

        Cursor cursor;
        if (isSelectedCat){
            Log.d(TAG, "Tab2MatType updateAdapter isSelectedCat = true " );
            //Курсор  с названиями типов материалов для cat_id
            cursor = mSmetaOpenHelper.getTypeNamesOneCategory(cat_id);
        }else {
            Log.d(TAG, "Tab2MatType updateAdapter isSelectedCat = false " );
            //получаем курсор с названиями типов материалов по всем категориям
            cursor = mSmetaOpenHelper.getTypeMatNamesAllCategories();
        }
        //Строковый массив с именами типов материалов из таблицы FM для файла с file_id
        String[] typetMatNamesFM = mSmetaOpenHelper.getTypeNamesFM(file_id);

        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        Log.d(TAG, " Tab2MatType updateAdapter Всего типов материалов = "+ cursor.getCount() );

        while (cursor.moveToNext()){
            String tipe_mat_name = cursor.getString(cursor.getColumnIndex(TypeMat.TYPE_MAT_NAME));
            boolean check_mark = false;
            for (int i=0; i<typetMatNamesFM.length; i++){
                if (typetMatNamesFM[i].equals(tipe_mat_name)){
                    check_mark = true;
                    //если есть совпадение, прекращаем перебор
                    break;
                }
            }
            Log.d(TAG, " Tab2MatType updateAdapter tipe_mat_name  = " +
                    (cursor.getPosition()+1) + "  " + tipe_mat_name + "  check_mark = " + check_mark);
            m =new HashMap<>();
            m.put(P.ATTR_TYPE_MAT_NAME,tipe_mat_name);
            m.put(P.ATTR_TYPE_MAT_MARK, check_mark);
            data.add(m);
        }
        Log.d(TAG, " Tab2MatType updateAdapter data.size()  = "+ data.size() );
        String[] from = new String[]{P.ATTR_TYPE_MAT_NAME, P.ATTR_TYPE_MAT_MARK};
        int [] to = new int[]{R.id.base_text, R.id.checkBoxTwoMat};

        sara = new SimpleAdapter(getActivity(), data, R.layout.list_item_two_mat, from, to);
        listView.setAdapter(sara);
    }

    @Override
    public long getTypeId(String typeName) {
        long type_id = mSmetaOpenHelper.getIdFromMatTypeName(typeName);
        return type_id;
    }

    @Override
    public long getCatId(long type_id) {
        long cat_id = mSmetaOpenHelper.getCatIdFromTypeMat(type_id);
        return cat_id;
    }

    public Tab2MatType() {
        // Required empty public constructor
    }
}