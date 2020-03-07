package ru.bartex.smetaelectro.ui.smetatabs;

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

public class SmetasWorkTab extends Fragment {

    public static final String TAG = "33333";
    private long file_id;
    private int positionItem;
    private SQLiteDatabase database;


    public static SmetasWorkTab newInstance(long file_id, int position) {
        SmetasWorkTab fragment = new SmetasWorkTab();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    public SmetasWorkTab() {
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
        Log.d(TAG, "// SmetasFrag onCreate // " );
        //получаем id файла из аргументов
        file_id = getArguments().getLong(P.ID_FILE);
        positionItem = getArguments().getInt(P.TAB_POSITION);
        Log.d(TAG, "SmetasFrag onCreate  file_id = " + file_id + "  positionItem = " +  positionItem);
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

        initRecycler(view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        database.close();
    }

    private void initRecycler(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_work_tab);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        SmetasRecyclerAdapter adapter = new SmetasRecyclerAdapter(database, file_id);
        recyclerView.setAdapter(adapter);
    }

}
