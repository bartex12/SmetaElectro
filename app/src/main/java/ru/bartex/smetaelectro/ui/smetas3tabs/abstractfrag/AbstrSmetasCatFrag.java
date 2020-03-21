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
import ru.bartex.smetaelectro.ui.smetas2tabs.SmetasTabRecyclerAdapter;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.SmetasCatRecyclerAdapter;

public abstract class AbstrSmetasCatFrag extends Fragment {

    public static final String TAG = "33333";
    public ListView listView;
    public long file_id;
    public int position;
    public SimpleAdapter sara;
    public ArrayList<Map<String, Object>> data;
    public Map<String, Object> m;

    public SQLiteDatabase database;
    public RecyclerView  recyclerView;
    public SmetasCatRecyclerAdapter adapter;
    private OnClickCatListener onClickCatListener;

    //public abstract  void updateAdapter();
    //public abstract  long getCatId(String catName);

    public interface OnClickCatListener{
        void catAndClickTransmit(long cat_id, boolean isSelectedCat);
    }

    public abstract SmetasCatRecyclerAdapter getSmetasCatRecyclerAdapter();
    public abstract SmetasCatRecyclerAdapter.OnClickOnLineListener getOnClickOnLineListener();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "//  AbstrSmetasCatFrag onAttach // " );
        onClickCatListener = (OnClickCatListener)context;
        database = new SmetaOpenHelper(context).getWritableDatabase();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "//  AbstrSmetasCatFrag onCreate // " );
        file_id = getArguments().getLong(P.ID_FILE);
        position = getArguments().getInt(P.TAB_POSITION);
        Log.d(TAG, "AbstrSmetasCatFrag onCreate file_id = " + file_id );
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "//  AbstrSmetasCatFrag onCreateView // " );

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_work_cat, container, false);

//        // Inflate the layout for this fragment
//        View view = inflater.inflate(
//                R.layout.fragment_tabs_for_works_and_materials, container, false);
//        listView = view.findViewById(R.id.listViewFragmentTabs);
//
//        //находим View, которое выводит текст Список пуст
//        View empty = view.findViewById(android.R.id.empty);
//        TextView tvEmpty = (TextView)empty;
//        tvEmpty.setText(R.string.list_empty_tab);
//        listView.setEmptyView(empty);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d(TAG, "//  AbstrSmetasCatFrag onItemClick // " );
//                TextView tv_smeta_item = view.findViewById(R.id.base_text);
//                String smeta_item_name = tv_smeta_item.getText().toString();
//
//                long cat_id = getCatId(smeta_item_name);
//                Log.d(TAG, "AbstrSmetasCatFrag onItemClick  cat_id = " + cat_id);
//
//                onClickCatListener.catAndClickTransmit(cat_id, true);
//            }
//        });
//        return view;
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
    public void onResume() {
        super.onResume();
        Log.d(TAG, "//  AbstrSmetasCatFrag onResume // " );
        //updateAdapter();

//        //объявляем о регистрации контекстного меню здесь, но как то это всё работает из SmetaMat?!
//        registerForContextMenu(listView);
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
        SmetasCatRecyclerAdapter.OnClickOnLineListener listener =
                getOnClickOnLineListener();
        adapter.setClickOnLineListener(listener);
        recyclerView.setAdapter(adapter);
    }

    public SmetasCatRecyclerAdapter getAdapter(){
        return adapter;
    }
}
