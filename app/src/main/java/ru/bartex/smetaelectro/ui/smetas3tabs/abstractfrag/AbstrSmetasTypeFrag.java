package ru.bartex.smetaelectro.ui.smetas3tabs.abstractfrag;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.SmetaOpenHelper;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.SmetasCatRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class AbstrSmetasTypeFrag extends Fragment {

    public static final String TAG = "33333";
    public ListView listView;
    public long file_id;
    public int position;

    public SQLiteDatabase database;
    public boolean isSelectedCat;
    public long cat_id;

    public RecyclerView recyclerView;
    public SmetasCatRecyclerAdapter adapter;
    public OnClickTypekListener onClickTypeListener;

    public abstract SmetasCatRecyclerAdapter getSmetasCatRecyclerAdapter();
    public abstract SmetasCatRecyclerAdapter.OnClickOnNamekListener getOnClickOnNamekListener();

    public interface OnClickTypekListener{
        void typeAndClickTransmit(long cat_id, long type_id, boolean isSelectedType);
    }

    public AbstrSmetasTypeFrag() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "//  AbstrSmetasTypeFrag onAttach // " );
        onClickTypeListener = (OnClickTypekListener)context;
        database = new SmetaOpenHelper(context).getWritableDatabase();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "//  AbstrSmetasTypeFrag onCreate // " );
        file_id = getArguments().getLong(P.ID_FILE);
        position = getArguments().getInt(P.TAB_POSITION);
        isSelectedCat =getArguments().getBoolean(P.IS_SELECTED_CAT);
        cat_id = getArguments().getLong(P.ID_CATEGORY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //Log.d(TAG, "//  AbstrSmetasTypeFrag onCreateView // " );
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_work_cat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "// AbstrSmetasTypeFrag onViewCreated // " );
        Log.d(TAG, "// AbstrSmetasTypeFrag onViewCreated *  cat_id =" +
                cat_id + "  isSelectedCat = " + isSelectedCat);
        initRecycler(view);
        //объявляем о регистрации контекстного меню
        registerForContextMenu(recyclerView);

        Log.d(TAG, "// AbstrSmetasTypeFrag onViewCreated ** cat_id =" +
                cat_id + "  isSelectedCat = " + isSelectedCat);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        database.close();
        Log.d(TAG, "--------  AbstrSmetasTypeFrag onDestroy -------" );
    }
    private void initRecycler(View view) {
        recyclerView = view.findViewById(R.id.recycler_work_cat);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        //абстр метод - реализация в каждом фрагменте
        adapter = getSmetasCatRecyclerAdapter();
        //абстр метод - реализация в каждом фрагменте
        SmetasCatRecyclerAdapter.OnClickOnNamekListener listener =
                getOnClickOnNamekListener();
        adapter.setOnClickOnNamekListener(listener);
        recyclerView.setAdapter(adapter);
    }
    public SmetasCatRecyclerAdapter getAdapter() {
        return adapter;
    }
}
