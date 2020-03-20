package ru.bartex.smetaelectro.ui.smetas2tabs;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.FW;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.Work;
import ru.bartex.smetaelectro.ui.smetas2tabs.detailes.DetailSmetaLine;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetawork.SmetasWork;
import ru.bartex.smetaelectro.whlam.RecyclerAdapter_Test;

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

//    //создаём контекстное меню для списка
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        getActivity().getMenuInflater()
//                .inflate(R.menu.context_menu_smetas_of_work_mat, menu);
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        handleMenuItemClick(item);
//        //return super.onContextItemSelected(item);
//        return true;
//    }
//
//    //TODO не раюботает удаление правильно - на материалах крэш
//    //обработка для контекстного меню
//    private void handleMenuItemClick(MenuItem item) {
//        int id = item.getItemId();
//        switch (id) {
//            case R.id.menu_delete_smetas_item: {
//                ViewPager viewPager=  getActivity().findViewById(R.id.container_smetas_tab);
//                int currentItem = viewPager.getCurrentItem();
//                this.getAdapter().removeElement(currentItem);
//                Log.d(TAG, "ХХ AbstrSmetasTab handleMenuItemClick pos = " +
//                        viewPager.getCurrentItem());
//
//                //updateAdapter(viewPager, currentItem);
//                break;
//            }
//            case R.id.menu_cancel_smetas_item: {
//                break;
//            }
//        }
//    }

//    private void updateAdapter(ViewPager viewPager, int currentItem) {
//        Log.d(TAG, "ХХХХ AbstrSmetasTab updateAdapter");
//        SmetasTabPagerAdapter pageAdapter =
//                new SmetasTabPagerAdapter(getActivity().getSupportFragmentManager());
//        viewPager.setAdapter(pageAdapter);
//        viewPager.setCurrentItem(currentItem);
//        pageAdapter.notifyDataSetChanged();
//    }

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
