package ru.bartex.smetaelectro.ui.smetas3tabs.abstractfrag;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.SmetaOpenHelper;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.SmetasWorkRecyclerAdapter;

public abstract class AbstrSmetasFrag extends Fragment {

    public static final String TAG = "33333";
    public long file_id;
    public int position;


    public SQLiteDatabase database;
    public RecyclerView  recyclerView;
    public SmetasWorkRecyclerAdapter adapter;
    public OnClickCatListener onClickCatListener;
    public OnClickTypekListener onClickTypeListener;

    public interface OnClickCatListener{
        void catAndClickTransmit(long _id, boolean isSelected);
    }

    public interface OnClickTypekListener{
        void typeAndClickTransmit(long cat_id, long type_id, boolean isSelectedType);
    }

    public abstract SmetasWorkRecyclerAdapter getSmetasWorkRecyclerAdapter();
    public abstract SmetasWorkRecyclerAdapter.OnClickOnNamekListener getOnClickOnNamekListener();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "//  AbstrSmetasFrag onAttach // " );
        //передаём ссылку на активити, котрая реализует этот интерфейс
        onClickCatListener = (OnClickCatListener)context;
        onClickTypeListener = (OnClickTypekListener)context;
        database = new SmetaOpenHelper(context).getWritableDatabase();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //Log.d(TAG, "//  AbstrSmetasCatFrag onCreateView // " );
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_work_cat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "// AbstrSmetasFrag onViewCreated // " );
        initRecycler(view);
        //объявляем о регистрации контекстного меню
        registerForContextMenu(recyclerView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        database.close();
        Log.d(TAG, "--------  AbstrSmetasFrag onDestroy -------" );
    }

    private void initRecycler(View view) {
        recyclerView = view.findViewById(R.id.recycler_work_cat);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        //абстр метод - реализация в каждом фрагменте
        adapter = getSmetasWorkRecyclerAdapter();
        //абстр метод - реализация в каждом фрагменте
        SmetasWorkRecyclerAdapter.OnClickOnNamekListener listener =
                getOnClickOnNamekListener();
        adapter.setOnClickOnNamekListener(listener);
        recyclerView.setAdapter(adapter);
    }

    public SmetasWorkRecyclerAdapter getAdapter() {
        return adapter;
    }
}
