package ru.bartex.smetaelectro.ui.smetas3tabs.smetawork;


import android.content.Intent;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.widget.Toast;
import ru.bartex.smetaelectro.ui.smetas2tabs.detailes.DetailSmetaLine;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.FW;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.TypeWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.Work;
import ru.bartex.smetaelectro.ui.smetas3tabs.abstractfrag.AbstrSmetasFrag;
import ru.bartex.smetaelectro.ui.smetas3tabs.abstractfrag.AbstrSmetasNameFrag;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.SmetasCatRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkName extends AbstrSmetasNameFrag {

//    public boolean isSelectedType;
//    public long type_id;

    public WorkName() {
        // Required empty public constructor
    }

    public static WorkName newInstance(
            long file_id, int position, boolean isSelectedType, long type_id){
        Log.d(TAG, "//  WorkName newInstance // " );
        WorkName fragment = new WorkName();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        args.putBoolean(P.IS_SELECTED_TYPE, isSelectedType);
        args.putLong(P.ID_TYPE, type_id);
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Log.d(TAG, "//  AbstrSmetasNameFrag onCreate // " );
//        file_id = getArguments().getLong(P.ID_FILE);
//        position = getArguments().getInt(P.TAB_POSITION);
//        isSelectedType = getArguments().getBoolean(P.IS_SELECTED_TYPE);
//        type_id = getArguments().getLong(P.ID_TYPE);
//        Log.d(TAG, "AbstrSmetasNameFrag onCreate isSelectedType = " + isSelectedType +
//                " file_id = " + file_id +" position = " + position+ " type_id = " + type_id);
//    }

    @Override
    public SmetasCatRecyclerAdapter getSmetasCatRecyclerAdapter() {
        Log.d(TAG, "//  WorkName getSmetasCatRecyclerAdapter file_id =  "  + file_id );
        return new SmetasCatRecyclerAdapter(
                database, file_id, position, false,0, isSelectedType, type_id);
    }

    @Override
    public SmetasCatRecyclerAdapter.OnClickOnNamekListener getOnClickOnNamekListener() {
        return new SmetasCatRecyclerAdapter.OnClickOnNamekListener() {
            @Override
            public void nameTransmit(String name) {
                Log.d(TAG, "//  WorkType nameTransmit name =  "  + name );
                Toast.makeText(getActivity(), " щелчок на списке наименований ",
                        Toast.LENGTH_SHORT).show();

                sendIntent(name);
            }
        };
    }

    private void sendIntent(String name) {
        //находим id работы по имени работы
        final long work_id = Work.getIdFromName(database, name);
        Log.d(TAG, "WorkName - onItemClick  work_id = " + work_id +
                "  work_name = " + name);
        // проверяем есть ли такая  работа в FW для файла с file_id
        final boolean isWork = FW.isWorkInFW(database, file_id, work_id);
        Log.d(TAG, "WorkName - onItemClick  isWork = " + isWork);

        //ищем id категории работы, зная id типа
        long cat_id = TypeWork.getCatIdFromTypeId(database, type_id);

        Intent intent = new Intent(getActivity(), DetailSmetaLine.class);
        intent.putExtra(P.ID_FILE_DEFAULT, file_id);
        intent.putExtra(P.ID_CATEGORY, cat_id);
        intent.putExtra(P.ID_TYPE, type_id);
        intent.putExtra(P.ID_WORK, work_id);
        intent.putExtra(P.IS_WORK, isWork);
        startActivity(intent);
    }

}
