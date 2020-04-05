package ru.bartex.smetaelectro.ui.smetas3tabs.workmat.mat;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;

import ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.database.mat.TypeMat;
import ru.bartex.smetaelectro.ui.smetas3tabs.workmat.AbstrSmetasFrag;
import ru.bartex.smetaelectro.ui.smetas3tabs.adapters.KindWork;
import ru.bartex.smetaelectro.ui.smetas3tabs.adapters.SmetasWorkRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatType extends AbstrSmetasFrag {

    public boolean isSelectedCat;
    public long cat_id;

    public MatType() {
        // Required empty public constructor
    }

    public static MatType newInstance(long file_id, int position, boolean isSelectedCat, long cat_id){
        Log.d(TAG, "//  MatType newInstance // " );
        MatType fragment = new MatType();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        args.putBoolean(P.IS_SELECTED_CAT, isSelectedCat);
        args.putLong(P.ID_CATEGORY, cat_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "//  MatType onCreate // " );
        file_id = getArguments().getLong(P.ID_FILE);
        position = getArguments().getInt(P.TAB_POSITION);
        isSelectedCat =getArguments().getBoolean(P.IS_SELECTED_CAT);
        cat_id = getArguments().getLong(P.ID_CATEGORY);
    }

    @Override
    public SmetasWorkRecyclerAdapter getSmetasWorkRecyclerAdapter() {
        Log.d(TAG, "//  MatType getSmetasCatRecyclerAdapter file_id =  "  + file_id );
        return new SmetasWorkRecyclerAdapter(
                database, KindWork.MAT, file_id, position, isSelectedCat, cat_id, false, 0);
    }

    @Override
    public SmetasWorkRecyclerAdapter.OnClickOnNamekListener getOnClickOnNamekListener() {
        return new SmetasWorkRecyclerAdapter.OnClickOnNamekListener() {
            @Override
            public void nameTransmit(String name) {
                Log.d(TAG, "//  MatType nameTransmit name =  "  + name );
                long type_id = TypeMat.getIdFromName(database, name);
                long cat_id = TypeMat.getCatIdFromTypeId(database, type_id);
                Log.d(TAG, "MatType nameTransmit  cat_id = " +cat_id + "  type_id = " + type_id);

                onClickTypeListener.typeAndClickTransmit(cat_id, type_id, true);
            }
        };
    }

}