package ru.bartex.smetaelectro.ui.smetas3tabs.smetamatcost;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.Mat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.TypeMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.TypeWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.Work;
import ru.bartex.smetaelectro.ui.smetas2tabs.detailes.detailscost.CostMatDetail;
import ru.bartex.smetaelectro.ui.smetas2tabs.detailes.detailscost.DetailCost;
import ru.bartex.smetaelectro.ui.smetas3tabs.abstractfrag.AbstrSmetasWorkCostFrag;
import ru.bartex.smetaelectro.ui.smetas3tabs.abstractfrag.todoit.AbstrSmetasNameFrag;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetamat.MatType;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.KindCost;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.KindWork;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.SmetasCostRecyclerAdapter;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.SmetasWorkRecyclerAdapter;
import ru.bartex.smetaelectro.w_hlam.SmetasMatRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatNameCost extends AbstrSmetasWorkCostFrag {

    public boolean isSelectedType;
    public long type_id;

    public MatNameCost() {
        // Required empty public constructor
    }

    public static MatNameCost newInstance(
            long file_id, int position, boolean isSelectedType, long type_id) {
        Log.d(TAG, "//  MatNameCost newInstance // " );
        MatNameCost fragment = new MatNameCost();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        args.putBoolean(P.IS_SELECTED_TYPE, isSelectedType);
        args.putLong(P.ID_TYPE, type_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "//  MatNameCost onCreate // " );
        file_id = getArguments().getLong(P.ID_FILE);
        position = getArguments().getInt(P.TAB_POSITION);
        isSelectedType = getArguments().getBoolean(P.IS_SELECTED_TYPE);
        type_id = getArguments().getLong(P.ID_TYPE);
        Log.d(TAG, "MatNameCost onCreate isSelectedType = " + isSelectedType +
                " file_id = " + file_id +" position = " + position+ " type_id = " + type_id);
    }

//    @Override
//    public void updateAdapter() {
//        Log.d(TAG, "//  MatNameCost updateAdapter // " );
//        Cursor cursor;
//        if (isSelectedType){
//            Log.d(TAG, "MatNameCost updateAdapter isSelectedType = true " );
//            //Курсор с именами   материалов из таблицы Mat для type_id
//            cursor = Mat.getNamesFromCatId(database, type_id);
//        }else {
//            Log.d(TAG, "MatNameCost updateAdapter isSelectedType = false " );
//            //Курсор с именами  всех материалов из таблицы Mat
//            cursor = Mat.getNamesAllTypes(database);
//        }
//        //Список с данными для адаптера
//        data = new ArrayList<Map<String, Object>>(cursor.getCount());
//        while (cursor.moveToNext()) {
//            //смотрим значение текущей строки курсора
//            String name_type = cursor.getString(cursor.getColumnIndex(Mat.MAT_NAME));
//            Log.d(TAG, "MatNameCost - updateAdapter  name_type = " + name_type);
//            m = new HashMap<>();
//            m.put(P.ATTR_TYPE_NAME,name_type);
//            data.add(m);
//        }
//        String[] from = new String[]{P.ATTR_TYPE_NAME};
//        int[] to = new int[]{R.id.base_text};
//        sara =  new SimpleAdapter(getActivity(), data, R.layout.list_item_single_mat, from, to);
//       listView.setAdapter(sara);
//    }
//
//    @Override
//    public void sendIntent(String name) {
//        //находим id материала по имени материала
//        final long mat_id = Mat.getIdFromName(database, name);
//        Log.d(TAG, "MatNameCost - onItemClick  mat_id = " + mat_id +
//                "  name = " + name);
//        // проверяем есть ли такой  материал в FM для файла с file_id
//        final boolean isMat = FM.isMatInFM(database, file_id, mat_id);
//        Log.d(TAG, "MatNameCost - onItemClick  isMat = " + isMat);
//
//        //ищем id категории материалов, зная id типа
//        long cat_id = TypeMat.getCatIdFromTypeId(database, type_id);
//
//        Intent intent = new Intent(getActivity(), CostMatDetail.class);
//        intent.putExtra(P.ID_FILE, file_id);
//        intent.putExtra(P.ID_CATEGORY_MAT, cat_id);
//        intent.putExtra(P.ID_TYPE_MAT, type_id);
//        intent.putExtra(P.ID_MAT, mat_id);
//        intent.putExtra(P.IS_MAT, isMat);
//        startActivity(intent);
//    }

    @Override
    public SmetasCostRecyclerAdapter getSmetasCostRecyclerAdapter() {
        Log.d(TAG, "//  MatNameCost getSmetasCostRecyclerAdapter file_id =  "  + file_id );
        return new SmetasCostRecyclerAdapter(
                database, KindCost.COST_MAT, file_id,
                position, false,0, isSelectedType, type_id);
    }

    @Override
    public SmetasCostRecyclerAdapter.OnClickOnNamekListener getOnClickOnNamekListener() {
        return new SmetasCostRecyclerAdapter.OnClickOnNamekListener() {
            @Override
            public void nameTransmit(String name) {
                Log.d(TAG, "//  MatNameCost nameTransmit name =  "  + name );
                //вызываем CostMatDetail
                sendIntent(name);
            }
        };
    }

    private void sendIntent(String name) {
        //находим id по имени работы
        long mat_id = Mat.getIdFromName(database, name);
        //ищем id категории работы, зная id типа
        long cat_id = TypeMat.getCatIdFromTypeId(database, type_id);

        Intent intent = new Intent(getActivity(), CostMatDetail.class);
        intent.putExtra(P.ID_CATEGORY_MAT, cat_id);
        intent.putExtra(P.ID_TYPE_MAT, type_id);
        intent.putExtra(P.ID_MAT, mat_id);
        startActivity(intent);
    }
}
