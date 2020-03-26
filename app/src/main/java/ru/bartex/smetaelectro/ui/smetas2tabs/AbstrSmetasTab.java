package ru.bartex.smetaelectro.ui.smetas2tabs;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.SmetaOpenHelper;
import ru.bartex.smetaelectro.ui.smetas2tabs.adapters.SmetasTabRecyclerAdapter;


public abstract class AbstrSmetasTab extends Fragment {

    public static final String TAG = "33333";
    public long file_id;
    public int positionItem;
    public SQLiteDatabase database;

    public RecyclerView recyclerView;
    public SmetasTabRecyclerAdapter adapter;

    public AbstrSmetasTab() {
        // Required empty public constructor
    }

    public abstract SmetasTabRecyclerAdapter getSmetasTabRecyclerAdapter();
    public abstract SmetasTabRecyclerAdapter.OnClickOnLineListener getOnClickOnLineListener();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        database = new SmetaOpenHelper(context).getWritableDatabase();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "// AbstrSmetasTab onCreate // " );
        //получаем id файла из аргументов
        file_id = getArguments().getLong(P.ID_FILE);
        positionItem = getArguments().getInt(P.TAB_POSITION);
        Log.d(TAG, "AbstrSmetasTab onCreate  file_id = " + file_id + "  positionItem = " +  positionItem);
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
        Log.d(TAG, "// AbstrSmetasTab onViewCreated // " );
        initRecycler(view);
        //объявляем о регистрации контекстного меню
        registerForContextMenu(recyclerView);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "// AbstrSmetasTab onStop // " );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        database.close();
    }

    private void initRecycler(View view) {
        recyclerView = view.findViewById(R.id.recycler_work_tab);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        //получаем адаптер - абстрактный метод
        adapter = getSmetasTabRecyclerAdapter();
        // получаем слушатель щелчков на списке смете работ  - абстрактный метод
        SmetasTabRecyclerAdapter.OnClickOnLineListener workListener =
                getOnClickOnLineListener();
        adapter.setOnClickOnLineListener(workListener);
        recyclerView.setAdapter(adapter);
    }

    public SmetasTabRecyclerAdapter getAdapter(){
        return adapter;
    }



}
