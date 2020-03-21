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
public abstract class AbstrSmetasNameFrag extends Fragment {

    public static final String TAG = "33333";
    public ListView listView;
    public long file_id;
    public int position;
    public SimpleAdapter sara;
    public ArrayList<Map<String, Object>> data;
    public Map<String, Object> m;

    public SQLiteDatabase database;
    public boolean isSelectedType;
    public long type_id;

    public RecyclerView recyclerView;
    public SmetasCatRecyclerAdapter adapter;


    public AbstrSmetasNameFrag() {
        // Required empty public constructor
    }

    public abstract SmetasCatRecyclerAdapter getSmetasCatRecyclerAdapter();
    public abstract SmetasCatRecyclerAdapter.OnClickOnNamekListener getOnClickOnNamekListener();

//    public abstract  void updateAdapter();
//    public abstract  void sendIntent(String name);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "//  AbstrSmetasNameFrag onAttach // " );
        database = new SmetaOpenHelper(context).getWritableDatabase();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "//  AbstrSmetasNameFrag onCreate // " );
        file_id = getArguments().getLong(P.ID_FILE);
        position = getArguments().getInt(P.TAB_POSITION);
        isSelectedType = getArguments().getBoolean(P.IS_SELECTED_TYPE);
        type_id = getArguments().getLong(P.ID_TYPE);
        Log.d(TAG, "AbstrSmetasNameFrag onCreate isSelectedType = " + isSelectedType +
                " file_id = " + file_id +" position = " + position+ " type_id = " + type_id);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "//  AbstrSmetasCatFrag onCreateView // " );
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_work_cat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "// AbstrSmetasCatFrag onViewCreated // " );
        initRecycler(view);
        //объявляем о регистрации контекстного меню
        registerForContextMenu(recyclerView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        database.close();
        Log.d(TAG, "--------  AbstrSmetasCatFrag onDestroy -------" );
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

    public SmetasCatRecyclerAdapter getAdapter(){
        return adapter;
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        Log.d(TAG, "//  AbstrSmetasNameFrag onCreateView // " );
//        View rootView = inflater.inflate(R.layout.fragment_tabs_for_works_and_materials, container, false);
//        listView = rootView.findViewById(R.id.listViewFragmentTabs);
//
//        //находим View, которое выводит текст Список пуст
//        View empty = rootView.findViewById(android.R.id.empty);
//        TextView tvEmpty = (TextView)empty;
//        tvEmpty.setText(R.string.list_empty_tab);
//        listView.setEmptyView(empty);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                parent.setSelection(position);
//                //находим имя работы в адаптере
//                TextView tv = view.findViewById(R.id.base_text);
//                String work_name = tv.getText().toString();
//
//                sendIntent(work_name);
//            }
//        });
//        return rootView;
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.d(TAG, "//  AbstrSmetasNameFrag onResume // " );
//        updateAdapter();
//        //объявляем о регистрации контекстного меню здесь, но как то это всё работает из SmetaMat?!
//        registerForContextMenu(listView);
//    }

}
