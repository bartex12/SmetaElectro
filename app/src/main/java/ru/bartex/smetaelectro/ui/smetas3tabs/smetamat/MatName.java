package ru.bartex.smetaelectro.ui.smetas3tabs.smetamat;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.FM;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.Mat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.TypeMat;
import ru.bartex.smetaelectro.ui.smetas2tabs.detailes.DetailSmetaMatLine;
import ru.bartex.smetaelectro.ui.smetas3tabs.abstractfrag.AbstrSmetasNameFrag;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.SmetasMatRecyclerAdapter;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.SmetasWorkRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatName extends AbstrSmetasNameFrag {


    public MatName() {
        // Required empty public constructor
    }



    public static MatName newInstance(
            long file_id, int position, boolean isSelectedType, long type_id){
        Log.d(TAG, "//  MatName newInstance // " );
        MatName fragment = new MatName();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        args.putBoolean(P.IS_SELECTED_TYPE, isSelectedType);
        args.putLong(P.ID_TYPE, type_id);
        fragment.setArguments(args);
        Log.d(TAG, "MatName  newInstance isSelectedType = " +
                isSelectedType + "  type_id = " +  type_id + "  file_id = " +  file_id +
                "  position = " +  position);
        return fragment;
    }

    @Override
    public SmetasMatRecyclerAdapter getSmetasCatRecyclerAdapter() {
        Log.d(TAG, "//  MatName getSmetasCatRecyclerAdapter file_id =  "  + file_id );
        return new SmetasMatRecyclerAdapter(
                database, file_id, position, false,0, isSelectedType, type_id);
    }

    @Override
    public SmetasMatRecyclerAdapter.OnClickOnNamekListener getOnClickOnNamekListener() {
        return new SmetasMatRecyclerAdapter.OnClickOnNamekListener() {
            @Override
            public void nameTransmit(String name) {
                Log.d(TAG, "//  MatName nameTransmit name =  "  + name );
                Toast.makeText(getActivity(), " щелчок на списке наименований ",
                        Toast.LENGTH_SHORT).show();

                sendIntent(name);
            }
        };
    }

    private void sendIntent(String name) {
        //находим id материала по имени материала
        final long mat_id = Mat.getIdFromName(database, name);
        Log.d(TAG, "MatName - onItemClick  mat_id = " + mat_id + " mat_name = " + name);
        //находим id типа материала по имени материала
        long type_id = Mat.getTypeIdFromName(database, name);
        Log.d(TAG, "MatName - onItemClick  type_id = " + type_id);
        //ищем id категории материалов, зная id типа
        long cat_id = TypeMat.getCatIdFromTypeId(database, type_id);
        Log.d(TAG, "MatName - onItemClick  cat_id = " + cat_id);
        // проверяем есть ли такой  материал в FM для файла с file_id
        final boolean isMat = FM.isMatInFM(database, file_id, mat_id);
        Log.d(TAG, "MatName - onItemClick  isMat = " + isMat);

        Intent intent = new Intent(getActivity(), DetailSmetaMatLine.class);
        intent.putExtra(P.ID_FILE, file_id);
        intent.putExtra(P.ID_CATEGORY_MAT, cat_id);
        intent.putExtra(P.ID_TYPE_MAT, type_id);
        intent.putExtra(P.ID_MAT, mat_id);
        intent.putExtra(P.IS_MAT, isMat);
        startActivity(intent);
    }
}
