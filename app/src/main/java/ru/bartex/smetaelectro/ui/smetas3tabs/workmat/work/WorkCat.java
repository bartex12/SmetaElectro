package ru.bartex.smetaelectro.ui.smetas3tabs.workmat.work;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;

import ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.database.work.CategoryWork;
import ru.bartex.smetaelectro.ui.smetas3tabs.workmat.AbstrSmetasFrag;
import ru.bartex.smetaelectro.ui.smetas3tabs.adapters.KindWork;
import ru.bartex.smetaelectro.ui.smetas3tabs.adapters.SmetasWorkRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkCat extends AbstrSmetasFrag {


    public WorkCat() {
        // Required empty public constructor
    }

    public static WorkCat newInstance(long file_id, int position){
        Log.d(TAG, "//  WorkCat newInstance // " );
        WorkCat fragment = new WorkCat();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "//  WorkCat onCreate // " );
        file_id = getArguments().getLong(P.ID_FILE);
        position = getArguments().getInt(P.TAB_POSITION);
        Log.d(TAG, "WorkCat onCreate file_id = " + file_id );
    }

    @Override
    public SmetasWorkRecyclerAdapter getSmetasWorkRecyclerAdapter() {
        Log.d(TAG, "//  WorkCat getSmetasCatRecyclerAdapter file_id =  "  + file_id );
        return new SmetasWorkRecyclerAdapter(
                database, KindWork.WORK, file_id, position,
                false,0, false, 0);
    }

    @Override
    public SmetasWorkRecyclerAdapter.OnClickOnNamekListener getOnClickOnNamekListener() {
        return new SmetasWorkRecyclerAdapter.OnClickOnNamekListener() {
            @Override
            public void nameTransmit(String name) {
                Log.d(TAG, "//  WorkCat nameTransmit name =  "  + name );
                long cat_id = CategoryWork.getIdFromName(database, name);
                Log.d(TAG, "//  WorkCat nameTransmit cat_id =  "  + cat_id );

                onClickCatListener.catAndClickTransmit(cat_id, true);
            }
        };
    }
}