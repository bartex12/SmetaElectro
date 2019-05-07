package ru.bartex.smetaelectro;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.Work;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmetasWorkCostTab3Work extends Fragment {


    public SmetasWorkCostTab3Work() {
        // Required empty public constructor
    }

    public static final String TAG = "33333";
    ListView listView;
    long file_id;
    int position;
    SimpleAdapter sara;
    ArrayList<Map<String, Object>> data;
    Map<String, Object> m;

    SmetaOpenHelper mSmetaOpenHelper;
    Context context;
    boolean isSelectedType;
    long type_id;


    public static SmetasWorkCostTab3Work NewInstance(
            long file_id, int position, boolean isSelectedType, long type_id){
        Log.d(TAG, "//  SmetasWorkCostTab3Work NewInstance // " );
        SmetasWorkCostTab3Work fragment = new SmetasWorkCostTab3Work();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        args.putBoolean(P.IS_SELECTED_TYPE, isSelectedType);
        args.putLong(P.ID_TYPE, type_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "//  SmetasWorkCostTab3Work onAttach // " );
        this.context =context;
        mSmetaOpenHelper = new SmetaOpenHelper(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "//  SmetasWorkCostTab3Work onCreate // " );
        file_id = getArguments().getLong(P.ID_FILE);
        position = getArguments().getInt(P.TAB_POSITION);
        isSelectedType = getArguments().getBoolean(P.IS_SELECTED_TYPE);
        type_id = getArguments().getLong(P.ID_TYPE);
        Log.d(TAG, "SmetasWorkCostTab3Work onCreate isSelectedType = " + isSelectedType +
                " file_id = " + file_id +" position = " + position+ " type_id = " + type_id);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "//  SmetasWorkCostTab3Work onCreateView // " );
        View rootView = inflater.inflate(R.layout.fragment_tabs_for_works_and_materials, container, false);
        listView = rootView.findViewById(R.id.listViewFragmentTabs);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parent.setSelection(position);
                //находим имя  работы в адаптере
                TextView tv = view.findViewById(R.id.base_text);
                String work_name = tv.getText().toString();

                //находим id по имени работы
                long work_id = mSmetaOpenHelper.getIdFromWorkName(work_name);
                //ищем id категории работы, зная id типа
                long cat_id = mSmetaOpenHelper.getCatIdFromTypeWork(type_id);

                Intent intent = new Intent(getActivity(), CostDetail.class);
                intent.putExtra(P.ID_CATEGORY, cat_id);
                intent.putExtra(P.ID_TYPE, type_id);
                intent.putExtra(P.ID_WORK, work_id);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "//  SmetasWorkCostTab3Work onResume // " );
        updateAdapter();

        //объявляем о регистрации контекстного меню здесь, но как то это всё работает из SmetaMat?!
        registerForContextMenu(listView);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        // получаем инфу о пункте списка
        final AdapterView.AdapterContextMenuInfo acmi =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int id = item.getItemId();
        Log.d(TAG, "SmetasWorkCostTab3Work onContextItemSelected id = " + id +
                " acmi.position = " + acmi.position + " acmi.id = " +acmi.id);

        return super.onContextItemSelected(item);
    }

    public void updateAdapter(){
        Log.d(TAG, "//  SmetasWorkCostTab3Work updateAdapter // " );
        Cursor cursor;
        if (isSelectedType){
            Log.d(TAG, "SmetasWorkCostTab3Work updateAdapter isSelectedType = true " );
            //Курсор с именами работ с типом type_id
            cursor = mSmetaOpenHelper.getWorkNames(type_id);
        }else {
            Log.d(TAG, "SmetasWorkCostTab3Work updateAdapter isSelectedType = false " );
            //Курсор с именами  всех материалов из таблицы Mat
            cursor = mSmetaOpenHelper.getWorkNamesAllTypes();
        }
        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        Log.d(TAG, " SmetasWorkCostTab3Work updateAdapter Всего материалов = "+ cursor.getCount() );

        while (cursor.moveToNext()){
            String mat_name = cursor.getString(cursor.getColumnIndex(Work.WORK_NAME));
            m =new HashMap<>();
            m.put(P.ATTR_MAT_NAME, mat_name);
            data.add(m);
        }
        Log.d(TAG, " SmetasWorkCostTab3Work updateAdapter data.size()  = "+ data.size() );
        String[] from = new String[]{P.ATTR_MAT_NAME};
        int [] to = new int[]{R.id.base_text};

        sara = new SimpleAdapter(getActivity(), data, R.layout.list_item_single_mat, from, to);
        listView.setAdapter(sara);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "--------  SmetasWorkCostTab3Work onDestroy -------" );
    }
}
