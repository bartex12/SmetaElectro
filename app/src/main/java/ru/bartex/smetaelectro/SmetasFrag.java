package ru.bartex.smetaelectro;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.TableControllerSmeta;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmetasFrag extends Fragment {


    public SmetasFrag() {
        // Required empty public constructor
    }

    public static final String TAG = "33333";

    ListView lvSmetas;
    SmetaOpenHelper mSmetaOpenHelper;
    TableControllerSmeta tableControllerSmeta;
    ArrayList<Map<String, Object>> data;
    Map<String, Object> m;
    SimpleAdapter sara;

    long file_id;
    int positionItem;

    ViewPager viewPager;
    BehaviorWorkOrMat behaviorWorkOrMat;

    public void performUpdateAdapter(Context context){
        behaviorWorkOrMat.updateAdapter(context);
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
    public void onAttach(Context context) {
        super.onAttach(context);
        //получаем  ViewPager viewPager
        viewPager = getActivity().findViewById(R.id.container);
        mSmetaOpenHelper = new SmetaOpenHelper(context);
        tableControllerSmeta = new TableControllerSmeta(context);
        Log.d(TAG, "// SmetasFrag onAttach  viewPager = " + viewPager);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "// SmetasFrag onCreateView // " );
        View rootView = inflater.inflate(R.layout.fragment_tabs_for_works_and_materials, container, false);
        lvSmetas = rootView.findViewById(R.id.listViewFragmentTabs);

        //находим View, которое выводит текст Список пуст
        View empty = rootView.findViewById(android.R.id.empty);
        lvSmetas.setEmptyView(empty);

        lvSmetas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_smeta_item = view.findViewById(R.id.base_text);
                String smeta_item_name = tv_smeta_item.getText().toString();
                switch (viewPager.getCurrentItem()){
                    //switch (positionItem){
                    case 0:
                        long work_id = mSmetaOpenHelper.getIdFromWorkName(smeta_item_name);
                        long type_id = mSmetaOpenHelper.getTypeIdWork(file_id, work_id);
                        long cat_id = mSmetaOpenHelper.getCateIdWork(file_id, work_id);

                        Intent intent_work = new Intent(getActivity(), DetailSmetaLine.class);
                        intent_work.putExtra(P.ID_FILE_DEFAULT, file_id);
                        intent_work.putExtra(P.ID_CATEGORY, cat_id);
                        intent_work.putExtra(P.ID_TYPE, type_id);
                        intent_work.putExtra(P.ID_WORK, work_id);
                        intent_work.putExtra(P.IS_WORK, true);  // такая работа есть
                        startActivity(intent_work);
                        break;

                    case 1:
                        long mat_id = mSmetaOpenHelper.getIdFromMatName(smeta_item_name);
                        long type_mat_id = mSmetaOpenHelper.getTypeIdMat(file_id, mat_id);
                        long cat_mat_id = mSmetaOpenHelper.getCateIdMat(file_id, mat_id);

                        Intent intent_mat = new Intent(getActivity(), DetailSmetaMatLine.class);
                        intent_mat.putExtra(P.ID_FILE, file_id);
                        intent_mat.putExtra(P.ID_CATEGORY_MAT, cat_mat_id);
                        intent_mat.putExtra(P.ID_TYPE_MAT, type_mat_id);
                        intent_mat.putExtra(P.ID_MAT, mat_id);
                        intent_mat.putExtra(P.IS_MAT, true);  // такой материал есть
                        startActivity(intent_mat);
                        break;
                }
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "//  SmetasFrag onResume // " );
        //обновляем данные списка фрагмента активности
        performUpdateAdapter(getActivity());
        //объявляем о регистрации контекстного меню
        //странно, но работа с контекстным меню может прроисходить  как в активности Smetas, так и здесь
        //но если сдесь, то не обновляется адаптер материалов при удалении пункта сметы,
        // поэтому приходится делать в Smetas
        registerForContextMenu(lvSmetas);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, P.DELETE_ITEM_SMETA, 0, "Удалить пункт");
        menu.add(0, P.CANCEL, 0, "Отмена");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        final AdapterView.AdapterContextMenuInfo acmi =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        //если удалить из контекстного меню
        if (item.getItemId() == P.DELETE_ITEM_SMETA) {

            Log.d(TAG, "SmetasFrag P.DELETE_ITEM_SMETA");
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.Delete_Item);
            builder.setPositiveButton(R.string.DeleteNo, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setNegativeButton(R.string.DeleteYes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG, "SmetasFrag P.DELETE_ITEM_SMETA acmi.position  =" +
                            (acmi.position));

                    TextView tv = acmi.targetView.findViewById(R.id.base_text);
                    String name = tv.getText().toString();
                    switch (viewPager.getCurrentItem()){
                        //switch (positionItem){
                        case 0:
                            Log.d(TAG, "SmetasFrag P.DELETE_ITEM_SMETA case 0");
                            //находим id по имени работы
                            long work_id = mSmetaOpenHelper.getIdFromWorkName(name);
                            Log.d(TAG, "SmetasFrag onContextItemSelected file_id = " +
                                    file_id + " work_id =" + work_id+ " work_name =" + name);

                            //удаляем пункт сметы из таблицы FW
                            mSmetaOpenHelper.deleteWorkItemFromFW(file_id, work_id);
                            //обновляем данные списка фрагмента активности
                            performUpdateAdapter(getActivity());
                            break;

                        case 1:
                            Log.d(TAG, "SmetasFrag P.DELETE_ITEM_SMETA case 1");
                            //находим id по имени работы
                            long mat_id = mSmetaOpenHelper.getIdFromMatName(name);
                            Log.d(TAG, "SmetasFrag onContextItemSelected file_id = " +
                                    file_id + " mat_id =" + mat_id + " mat_name =" + name);

                            //mSmetaOpenHelper.displayFM();
                            //удаляем пункт сметы из таблицы FM
                            mSmetaOpenHelper.deleteMatItemFromFM(file_id, mat_id);
                            //mSmetaOpenHelper.displayFM();

                            //обновляем данные списка фрагмента активности
                            performUpdateAdapter(getActivity());
                            break;
                    }
                }
            });
            builder.show();
            return true;
            //если изменить из контекстного меню
        } else if (item.getItemId() == P.CANCEL) {
            //getActivity().finish();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "//SmetasFrag onPause // " );
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "//SmetasFrag onStop // " );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "//SmetasFrag onDestroy // " );
    }
}
