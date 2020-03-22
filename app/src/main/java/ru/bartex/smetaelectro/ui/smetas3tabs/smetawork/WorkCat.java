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
import ru.bartex.smetaelectro.ui.smetas2tabs.SmetasTabRecyclerAdapter;
import ru.bartex.smetaelectro.ui.smetas3tabs.abstractfrag.AbstrSmetasCatFrag;
import ru.bartex.smetaelectro.ui.smetas3tabs.abstractfrag.AbstrSmetasFrag;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.SmetasCatRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkCat extends AbstrSmetasCatFrag {


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

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Log.d(TAG, "//  AbstrSmetasCatFrag onCreate // " );
//        file_id = getArguments().getLong(P.ID_FILE);
//        position = getArguments().getInt(P.TAB_POSITION);
//        Log.d(TAG, "AbstrSmetasCatFrag onCreate file_id = " + file_id );
//    }

    @Override
    public SmetasCatRecyclerAdapter getSmetasCatRecyclerAdapter() {
        Log.d(TAG, "//  WorkCat getSmetasCatRecyclerAdapter file_id =  "  + file_id );
        return new SmetasCatRecyclerAdapter(
                database, file_id, position, false,0, false, 0);
    }

    @Override
    public SmetasCatRecyclerAdapter.OnClickOnNamekListener getOnClickOnNamekListener() {
        return new SmetasCatRecyclerAdapter.OnClickOnNamekListener() {
            @Override
            public void nameTransmit(String name) {
                Log.d(TAG, "//  WorkCat nameTransmit name =  "  + name );
                Toast.makeText(getActivity(), " щелчок на списке категорий ",
                        Toast.LENGTH_SHORT).show();
                long cat_id = CategoryWork.getIdFromName(database, name);
                Log.d(TAG, "//  WorkCat nameTransmit cat_id =  "  + cat_id );

                onClickCatListener.catAndClickTransmit(cat_id, true);
            }
        };
    }
}
