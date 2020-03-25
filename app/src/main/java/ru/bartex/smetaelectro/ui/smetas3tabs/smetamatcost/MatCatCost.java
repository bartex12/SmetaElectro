package ru.bartex.smetaelectro.ui.smetas3tabs.smetamatcost;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.CategoryMat;
import ru.bartex.smetaelectro.ui.smetas3tabs.abstractfrag.AbstrSmetasCostFrag;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.KindCost;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.SmetasCostRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatCatCost extends AbstrSmetasCostFrag {


    public MatCatCost() {
        // Required empty public constructor
    }

    public static MatCatCost newInstance(long file_id, int position){
        Log.d(TAG, "//  MatCatCost newInstance // " );
        MatCatCost fragment = new MatCatCost();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putLong(P.TAB_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "//  MatCatCost onCreate // " );
        file_id = getArguments().getLong(P.ID_FILE);
        position = getArguments().getInt(P.TAB_POSITION);
        Log.d(TAG, "MatCatCost onCreate file_id = " + file_id );
    }

    @Override
    public SmetasCostRecyclerAdapter getSmetasCostRecyclerAdapter() {
        Log.d(TAG, "//  MatCatCost getSmetasCostRecyclerAdapter file_id =  "  + file_id );
        return new SmetasCostRecyclerAdapter(
                database, KindCost.COST_MAT, file_id, position,
                false,0, false, 0);
    }

    @Override
    public SmetasCostRecyclerAdapter.OnClickOnNamekListener getOnClickOnNamekListener() {
        return new SmetasCostRecyclerAdapter.OnClickOnNamekListener() {
            @Override
            public void nameTransmit(String name) {
                Log.d(TAG, "//  MatCatCost nameTransmit name =  "  + name );
                long cat_id = CategoryMat.getIdFromName(database, name);
                Log.d(TAG, "//  MatCatCost nameTransmit cat_id =  "  + cat_id );

                onClickCatListener.catAndClickTransmit(cat_id, true);
            }
        };
    }
}
