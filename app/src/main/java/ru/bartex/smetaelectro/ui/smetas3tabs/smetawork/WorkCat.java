package ru.bartex.smetaelectro.ui.smetas3tabs.smetawork;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.CategoryWork;
import ru.bartex.smetaelectro.ui.smetas3tabs.abstractfrag.AbstrSmetasWorkFrag;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.SmetasWorkRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkCat extends AbstrSmetasWorkFrag {


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
        Log.d(TAG, "//  AbstrSmetasCatFrag onCreate // " );
        file_id = getArguments().getLong(P.ID_FILE);
        position = getArguments().getInt(P.TAB_POSITION);
        Log.d(TAG, "AbstrSmetasCatFrag onCreate file_id = " + file_id );
    }

    @Override
    public SmetasWorkRecyclerAdapter getSmetasCatRecyclerAdapter() {
        Log.d(TAG, "//  WorkCat getSmetasCatRecyclerAdapter file_id =  "  + file_id );
        return new SmetasWorkRecyclerAdapter(
                database, file_id, position, false,0, false, 0);
    }

    @Override
    public SmetasWorkRecyclerAdapter.OnClickOnNamekListener getOnClickOnNamekListener() {
        return new SmetasWorkRecyclerAdapter.OnClickOnNamekListener() {
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
