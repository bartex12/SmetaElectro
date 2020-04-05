package ru.bartex.smetaelectro.ui.smetas3tabs.cost.workcost;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;


import ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.database.work.TypeWork;
import ru.bartex.smetaelectro.database.work.Work;
import ru.bartex.smetaelectro.ui.smetas3tabs.workmat.work.detwork.DetailCost;
import ru.bartex.smetaelectro.ui.smetas3tabs.cost.AbstrSmetasCostFrag;
import ru.bartex.smetaelectro.ui.smetas3tabs.adapters.KindCost;
import ru.bartex.smetaelectro.ui.smetas3tabs.adapters.SmetasCostRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkNameCost extends AbstrSmetasCostFrag {

    public boolean isSelectedType;
    public long type_id;

    public WorkNameCost() {
        // Required empty public constructor
    }

    public static WorkNameCost newInstance(
            long file_id, int position, boolean isSelectedType, long type_id){
        Log.d(TAG, "//  WorkNameCost newInstance // " );
        WorkNameCost fragment = new WorkNameCost();
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
        Log.d(TAG, "//  WorkNameCost onCreate // " );
        file_id = getArguments().getLong(P.ID_FILE);
        position = getArguments().getInt(P.TAB_POSITION);
        isSelectedType = getArguments().getBoolean(P.IS_SELECTED_TYPE);
        type_id = getArguments().getLong(P.ID_TYPE);
        Log.d(TAG, "WorkNameCost onCreate isSelectedType = " + isSelectedType +
                " file_id = " + file_id +" position = " + position+ " type_id = " + type_id);
    }

    @Override
    public SmetasCostRecyclerAdapter getSmetasCostRecyclerAdapter() {
        Log.d(TAG, "//  WorkNameCost getSmetasCostRecyclerAdapter file_id =  "  + file_id );
        return new SmetasCostRecyclerAdapter(
                database, KindCost.COST_WORK, file_id, position,
                false,0, isSelectedType, type_id);
    }

    @Override
    public SmetasCostRecyclerAdapter.OnClickOnNamekListener getOnClickOnNamekListener() {
        return new SmetasCostRecyclerAdapter.OnClickOnNamekListener() {
            @Override
            public void nameTransmit(String name) {
                Log.d(TAG, "//  WorkNameCost nameTransmit name =  "  + name );
                //вызываем DetailCost
                sendIntent(name);
            }
        };
    }

        private void sendIntent(String name) {
            // ВСЕ параметры нужно пересчитывать, так как они изменяются
            // при щелчке на строке списка

            //находим id по имени работы
            long work_id = Work.getIdFromName(database, name);
            //находим id типа работы по имени работы
            long type_id = Work.getTypeIdFromName(database, name);
            Log.d(TAG, "//  WorkNameCost sendIntent type_id =  "  + type_id );
            //ищем id категории работы, зная id типа
            long cat_id = TypeWork.getCatIdFromTypeId(database, type_id);

        Intent intent = new Intent(getActivity(), DetailCost.class);
        intent.putExtra(P.ID_CATEGORY, cat_id);
        intent.putExtra(P.ID_TYPE, type_id);
        intent.putExtra(P.ID_WORK, work_id);
        startActivity(intent);
    }
}
