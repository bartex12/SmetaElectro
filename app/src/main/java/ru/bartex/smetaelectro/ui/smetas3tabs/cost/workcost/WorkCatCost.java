package ru.bartex.smetaelectro.ui.smetas3tabs.cost.workcost;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.CategoryWork;
import ru.bartex.smetaelectro.ui.smetas3tabs.cost.AbstrSmetasCostFrag;
import ru.bartex.smetaelectro.ui.smetas3tabs.adapters.KindCost;
import ru.bartex.smetaelectro.ui.smetas3tabs.adapters.SmetasCostRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkCatCost extends AbstrSmetasCostFrag {


    public WorkCatCost() {
        // Required empty public constructor
    }

    public static WorkCatCost newInstance(long file_id, int position){
        Log.d(TAG, "//  WorkCatCost newInstance // " );
        WorkCatCost fragment = new WorkCatCost();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "//  WorkCatCost onCreate // " );
        file_id = getArguments().getLong(P.ID_FILE);
        position = getArguments().getInt(P.TAB_POSITION);
        Log.d(TAG, "WorkCatCost onCreate file_id = " + file_id );
    }

    @Override
    public SmetasCostRecyclerAdapter getSmetasCostRecyclerAdapter() {
        Log.d(TAG, "//  WorkCatCost getSmetasCostRecyclerAdapter file_id =  "  + file_id );
        return new SmetasCostRecyclerAdapter(
                database,  KindCost.COST_WORK, file_id, position,
                false,0, false, 0);
    }

    @Override
    public SmetasCostRecyclerAdapter.OnClickOnNamekListener getOnClickOnNamekListener() {
        return new SmetasCostRecyclerAdapter.OnClickOnNamekListener() {
            @Override
            public void nameTransmit(String name) {
                Log.d(TAG, "//  WorkCatCost nameTransmit name =  "  + name );
                long cat_id = CategoryWork.getIdFromName(database, name);
                Log.d(TAG, "//  WorkCatCost nameTransmit cat_id =  "  + cat_id );

                onClickCatListener.catAndClickTransmit(cat_id, true);
            }
        };
    }
}
