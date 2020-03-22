package ru.bartex.smetaelectro.ui.smetas3tabs.smetawork;


import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.CategoryWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.FW;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.TypeWork;
import ru.bartex.smetaelectro.ui.smetas3tabs.abstractfrag.AbstrSmetasFrag;
import ru.bartex.smetaelectro.ui.smetas3tabs.abstractfrag.AbstrSmetasTypeFrag;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.SmetasCatRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkType extends AbstrSmetasTypeFrag {

//    public boolean isSelectedCat;
//    public long cat_id;

    public static WorkType newInstance(
            long file_id, int position, boolean isSelectedCat, long cat_id){
        Log.d(TAG, "//  WorkType newInstance // " );
        WorkType fragment = new WorkType();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        args.putBoolean(P.IS_SELECTED_CAT, isSelectedCat);
        args.putLong(P.ID_CATEGORY, cat_id);
        fragment.setArguments(args);
        return fragment;
    }

    public WorkType() {
        // Required empty public constructor
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Log.d(TAG, "//  AbstrSmetasTypeFrag onCreate // " );
//        file_id = getArguments().getLong(P.ID_FILE);
//        position = getArguments().getInt(P.TAB_POSITION);
//        isSelectedCat =getArguments().getBoolean(P.IS_SELECTED_CAT);
//        cat_id = getArguments().getLong(P.ID_CATEGORY);
//    }

    @Override
    public SmetasCatRecyclerAdapter getSmetasCatRecyclerAdapter() {
        Log.d(TAG, "//  WorkType getSmetasCatRecyclerAdapter file_id =  "  + file_id );
        return new SmetasCatRecyclerAdapter(
                database, file_id, position, isSelectedCat, cat_id, false, 0);

    }

    @Override
    public SmetasCatRecyclerAdapter.OnClickOnNamekListener getOnClickOnNamekListener() {
        return new SmetasCatRecyclerAdapter.OnClickOnNamekListener() {
            @Override
            public void nameTransmit(String name) {
                Log.d(TAG, "//  WorkType nameTransmit name =  "  + name );
                Toast.makeText(getActivity(), " щелчок на списке типов ",
                        Toast.LENGTH_SHORT).show();
                long type_id = TypeWork.getIdFromName(database, name);
                long cat_id = TypeWork.getCatIdFromTypeId(database, type_id);
                Log.d(TAG, "WorkType nameTransmit  cat_id = " +cat_id + "  type_id = " + type_id);
//                adapter.updateRecyclerAdapter(database, file_id,2,true,
//                        cat_id, true, type_id );
//                adapter.notifyDataSetChanged();

                onClickTypeListener.typeAndClickTransmit(cat_id, type_id, true);
            }
        };
    }
}
