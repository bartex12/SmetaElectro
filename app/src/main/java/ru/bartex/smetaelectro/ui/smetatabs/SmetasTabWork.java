package ru.bartex.smetaelectro.ui.smetatabs;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.bartex.smetaelectro.DetailSmetaLine;
import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.files.FileWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.FW;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.Work;

//класс - фрагмент для вкладки Работа
public class SmetasTabWork extends Fragment {

    public static final String TAG = "33333";
    private long file_id;
    private int positionItem;
    private SQLiteDatabase database;
    private float[] summa; //массив стоимости
    private float totalSumma; // общая стоимость

    RecyclerView recyclerView;
    SmetasRecyclerWorkAdapter adapter;
    RecyclerAdapter_Test adapterTest;


    public static SmetasTabWork newInstance(long file_id, int position) {
        SmetasTabWork fragment = new SmetasTabWork();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    public SmetasTabWork() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        database = new SmetaOpenHelper(context).getWritableDatabase();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "// SmetasTabWork onCreate // " );
        //получаем id файла из аргументов
        file_id = getArguments().getLong(P.ID_FILE);
        positionItem = getArguments().getInt(P.TAB_POSITION);
        Log.d(TAG, "SmetasTabWork onCreate  file_id = " + file_id + "  positionItem = " +  positionItem);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_work_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initViews(view);
        initRecycler(view);
        //объявляем о регистрации контекстного меню
        registerForContextMenu(recyclerView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        database.close();
    }

//    private void initViews(View view) {
//        TextView header = view.findViewById(R.id.header_work_tab);
//        String fileName = FileWork.getNameFromId(database, file_id);
//        header.setText(String.format(Locale.getDefault(),"Смета: %s", fileName));
//
//        TextView footer= view.findViewById(R.id.footer_work_tab);
//        summa = FW.getArraySumma(database, file_id);
//        totalSumma = P.updateTotalSumma(summa);
//        footer.setText(String.format(Locale.getDefault(),"За работу: %.0f руб", totalSumma ));
//    }

    private void initRecycler(View view) {
        recyclerView = view.findViewById(R.id.recycler_work_tab);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        SmetasRecyclerWorkAdapter.OnClickOnWorkListener workListener =
                new SmetasRecyclerWorkAdapter.OnClickOnWorkListener() {
                    @Override
                    public void onClickOnMatListener(String nameItem) {
                        long work_id = Work.getIdFromName(database, nameItem);
                        long type_id = FW.getTypeId_FW(database, file_id, work_id);
                        long cat_id = FW.getCatId_FW(database, file_id, work_id);

                        Intent intent_work = new Intent(getActivity(), DetailSmetaLine.class);
                        intent_work.putExtra(P.ID_FILE_DEFAULT, file_id);
                        intent_work.putExtra(P.ID_CATEGORY, cat_id);
                        intent_work.putExtra(P.ID_TYPE, type_id);
                        intent_work.putExtra(P.ID_WORK, work_id);
                        intent_work.putExtra(P.IS_WORK, true);  // такая работа есть
                        startActivity(intent_work);
                    }
                };
        adapter = new SmetasRecyclerWorkAdapter(database, file_id);
        //adapterTest = new RecyclerAdapter_Test();
        adapter.setOnClickOnWorkListener(workListener);
        //recyclerView.setAdapter(adapterTest);
        recyclerView.setAdapter(adapter);
    }

    public SmetasRecyclerWorkAdapter getAdapter(){
        return adapter;
    }

}
