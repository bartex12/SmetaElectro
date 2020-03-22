package ru.bartex.smetaelectro.ui.smetas3tabs.smetamat;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.CategoryMat;
import ru.bartex.smetaelectro.ui.smetas3tabs.abstractfrag.AbstrSmetasCatFrag;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.SmetasMatRecyclerAdapter;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.SmetasWorkRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatCat extends AbstrSmetasCatFrag {


    public MatCat() {
        // Required empty public constructor
    }

    public static MatCat newInstance(long file_id, int position){
        Log.d(TAG, "//  MatCat newInstance // " );
        MatCat fragment = new MatCat();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putLong(P.TAB_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public SmetasMatRecyclerAdapter getSmetasCatRecyclerAdapter() {

        Log.d(TAG, "//  MatCat getSmetasCatRecyclerAdapter file_id =  "  + file_id );
        return new SmetasMatRecyclerAdapter(
                database, file_id, position, false,0, false, 0);
    }

    @Override
    public SmetasMatRecyclerAdapter.OnClickOnNamekListener getOnClickOnNamekListener() {
        return new SmetasMatRecyclerAdapter.OnClickOnNamekListener() {
            @Override
            public void nameTransmit(String name) {
                Log.d(TAG, "//  MatCat nameTransmit name =  "  + name );
                Toast.makeText(getActivity(), " щелчок на списке категорий ",
                        Toast.LENGTH_SHORT).show();
                long cat_id = CategoryMat.getIdFromName(database, name);
                Log.d(TAG, "//  MatCat nameTransmit cat_id =  "  + cat_id );

                onClickCatListener.catAndClickTransmit(cat_id, true);
            }
        };
    }
}
