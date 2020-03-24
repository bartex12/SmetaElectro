package ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkcost;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.TypeWork;
import ru.bartex.smetaelectro.ui.smetas3tabs.abstractfrag.AbstrSmetasWorkCostFrag;
import ru.bartex.smetaelectro.ui.smetas3tabs.abstractfrag.todoit.AbstrSmetasTypeFrag;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.Kind;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.SmetasCostRecyclerAdapter;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.SmetasWorkRecyclerAdapter;
import ru.bartex.smetaelectro.w_hlam.SmetasMatRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkTypeCost extends AbstrSmetasWorkCostFrag {

    public boolean isSelectedCat;
    public long cat_id;

    public WorkTypeCost() {
        // Required empty public constructor
    }

    public static WorkTypeCost newInstance(
            long file_id, int position, boolean isSelectedCat, long cat_id){
        Log.d(TAG, "//  WorkTypeCost newInstance // " );
        WorkTypeCost fragment = new WorkTypeCost();
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
        Log.d(TAG, "//  WorkTypeCost onCreate // " );
        file_id = getArguments().getLong(P.ID_FILE);
        position = getArguments().getInt(P.TAB_POSITION);
        isSelectedCat =getArguments().getBoolean(P.IS_SELECTED_CAT);
        cat_id = getArguments().getLong(P.ID_CATEGORY);
    }

    @Override
    public SmetasCostRecyclerAdapter getSmetasCostRecyclerAdapter() {
        Log.d(TAG, "//  WorkTypeCost getSmetasCostRecyclerAdapter file_id =  "  + file_id );
        return new SmetasCostRecyclerAdapter(
                database, Kind.COST_WORK, file_id,
                position, isSelectedCat, cat_id, false, 0);
    }

    @Override
    public SmetasCostRecyclerAdapter.OnClickOnNamekListener getOnClickOnNamekListener() {
        return new SmetasCostRecyclerAdapter.OnClickOnNamekListener() {
            @Override
            public void nameTransmit(String name) {
                Log.d(TAG, "//  WorkTypeCost nameTransmit name =  "  + name );
                Toast.makeText(getActivity(), " щелчок на списке типов ",
                        Toast.LENGTH_SHORT).show();
                long type_id = TypeWork.getIdFromName(database, name);
                long cat_id = TypeWork.getCatIdFromTypeId(database, type_id);
                Log.d(TAG, "WorkTypeCost nameTransmit  cat_id = " +cat_id + "  type_id = " + type_id);

                onClickTypeListener.typeAndClickTransmit(cat_id, type_id, true);
            }
        };
    }

}
