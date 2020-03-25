package ru.bartex.smetaelectro.ui.smetas3tabs.smetamat;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.FM;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.Mat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.TypeMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.Work;
import ru.bartex.smetaelectro.ui.smetas2tabs.detailes.DetailSmetaMatLine;
import ru.bartex.smetaelectro.ui.smetas3tabs.abstractfrag.AbstrSmetasFrag;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.KindWork;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.SmetasWorkRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatName extends AbstrSmetasFrag {

    public boolean isSelectedType;
    public long type_id;

    public MatName() {
        // Required empty public constructor
    }

    public static MatName newInstance(
            long file_id, int position, boolean isSelectedType, long type_id){
        Log.d(TAG, "//  MatName newInstance // " );
        MatName fragment = new MatName();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        args.putBoolean(P.IS_SELECTED_TYPE, isSelectedType);
        args.putLong(P.ID_TYPE, type_id);
        fragment.setArguments(args);
        Log.d(TAG, "MatName  newInstance isSelectedType = " +
                isSelectedType + "  type_id = " +  type_id + "  file_id = " +  file_id +
                "  position = " +  position);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "//  MatName onCreate // " );
        file_id = getArguments().getLong(P.ID_FILE);
        position = getArguments().getInt(P.TAB_POSITION);
        isSelectedType = getArguments().getBoolean(P.IS_SELECTED_TYPE);
        type_id = getArguments().getLong(P.ID_TYPE);
        Log.d(TAG, "MatName onCreate isSelectedType = " + isSelectedType +
                " file_id = " + file_id +" position = " + position+ " type_id = " + type_id);
    }

    @Override
    public SmetasWorkRecyclerAdapter getSmetasWorkRecyclerAdapter() {
        Log.d(TAG, "//  MatName getSmetasCatRecyclerAdapter file_id =  "  + file_id );
        return new SmetasWorkRecyclerAdapter(
                database, KindWork.MAT, file_id,
                position, false,0, isSelectedType, type_id);
    }

    @Override
    public SmetasWorkRecyclerAdapter.OnClickOnNamekListener getOnClickOnNamekListener() {
        return new SmetasWorkRecyclerAdapter.OnClickOnNamekListener() {
            @Override
            public void nameTransmit(String name) {
                Log.d(TAG, "//  MatName nameTransmit name =  "  + name );
                //вызываем DetailSmetaMatLine
                sendIntent(name);
            }
        };
    }

    private void sendIntent(String name) {
        // ВСЕ параметры нужно пересчитывать, так как они изменяются
        // при щелчке на строке списка

        //находим id материала по имени материала
        final long mat_id = Mat.getIdFromName(database, name);
        //находим id типа работы по имени работы
        long type_id = Mat.getTypeIdFromName(database, name);
        Log.d(TAG, "//  WorkName sendIntent type_id =  "  + type_id );
        //ищем id категории материалов, зная id типа
        long cat_id = TypeMat.getCatIdFromTypeId(database, type_id);
        // проверяем есть ли такой  материал в FM для файла с file_id
        final boolean isMat = FM.isMatInFM(database, file_id, mat_id);

        Intent intent = new Intent(getActivity(), DetailSmetaMatLine.class);
        intent.putExtra(P.ID_FILE, file_id);
        intent.putExtra(P.ID_CATEGORY_MAT, cat_id);
        intent.putExtra(P.ID_TYPE_MAT, type_id);
        intent.putExtra(P.ID_MAT, mat_id);
        intent.putExtra(P.IS_MAT, isMat);
        startActivity(intent);
    }
}
