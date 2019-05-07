package ru.bartex.smetaelectro;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
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
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.TypeWork;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmetasWorkCostTab2Type extends Fragment {


    public SmetasWorkCostTab2Type() {
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
    boolean isSelectedCat;
    long cat_id;

    public interface OnClickTypeWorkListener{
        void typeAndClickTransmit(long cat_id, long type_mat_id, boolean isSelectedType);
    }
    SmetasWorkCostTab2Type.OnClickTypeWorkListener onClickTypeWorkListener;


    public static SmetasWorkCostTab2Type NewInstance(
            long file_id, int position, boolean isSelectedCat, long cat_id){
        Log.d(TAG, "//  SmetasWorkCostTab2Type NewInstance // " );
        SmetasWorkCostTab2Type fragment = new SmetasWorkCostTab2Type();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        args.putBoolean(P.IS_SELECTED_CAT, isSelectedCat);
        args.putLong(P.ID_CATEGORY, cat_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "//  SmetasWorkCostTab2Type onAttach // " );
        this.context =context;
        mSmetaOpenHelper = new SmetaOpenHelper(context);
        onClickTypeWorkListener = (SmetasWorkCostTab2Type.OnClickTypeWorkListener)context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "//  SmetasWorkCostTab2Type onCreate // " );
        file_id = getArguments().getLong(P.ID_FILE);
        position = getArguments().getInt(P.TAB_POSITION);
        isSelectedCat =getArguments().getBoolean(P.IS_SELECTED_CAT);
        cat_id = getArguments().getLong(P.ID_CATEGORY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "//  SmetasWorkCostTab2Type onCreateView // " );
        View rootView = inflater.inflate(R.layout.fragment_tabs_for_works_and_materials, container, false);
        listView = rootView.findViewById(R.id.listViewFragmentTabs);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "//  SmetasWorkCostTab2Type onItemClick // " );
                TextView tv_smeta_item = view.findViewById(R.id.base_text);
                String smeta_item_name = tv_smeta_item.getText().toString();

                long type_id = mSmetaOpenHelper.getIdFromTypeName(smeta_item_name);
                long cat_id = mSmetaOpenHelper.getCatIdFromTypeWork(type_id);
                Log.d(TAG, "SmetasWorkCostTab2Type onItemClick  cat_id = " + cat_id + "  type_id = " + type_id);

                onClickTypeWorkListener.typeAndClickTransmit(cat_id, type_id, true);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "//  SmetasWorkCostTab2Type onResume // " );

        updateAdapter();

        //объявляем о регистрации контекстного меню здесь, но как то это всё работает из SmetaMat?!
        registerForContextMenu(listView);
    }

    public void updateAdapter(){
        Log.d(TAG, "//  SmetasWorkCostTab2Type updateAdapter // " );

        Cursor cursor;
        if (isSelectedCat){
            Log.d(TAG, "SmetasWorkCostTab2Type updateAdapter isSelectedCat = true " );
            //курсор с именами типов работы для категорий с cat_id
            cursor = mSmetaOpenHelper.getTypeNames(cat_id);
        }else {
            Log.d(TAG, "SmetasWorkCostTab2Type updateAdapter isSelectedCat = false " );
            //получаем курсор с названиями типов работ по всем категориям
            cursor = mSmetaOpenHelper.getTypeWorkNamesAllCategories();
        }
        //Строковый массив с именами типов из таблицы FW для файла с file_id
        String[] typetNamesFW = mSmetaOpenHelper.getTypeNamesFW(file_id);

        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        Log.d(TAG, " SmetasWorkCostTab2Type updateAdapter Всего типов материалов = "+ cursor.getCount() );

        while (cursor.moveToNext()){
            String tipe_mat_name = cursor.getString(cursor.getColumnIndex(TypeWork.TYPE_NAME));
            m =new HashMap<>();
            m.put(P.ATTR_TYPE_MAT_NAME,tipe_mat_name);
            data.add(m);
        }
        Log.d(TAG, " SmetasWorkCostTab2Type updateAdapter data.size()  = "+ data.size() );
        String[] from = new String[]{P.ATTR_TYPE_MAT_NAME};
        int [] to = new int[]{R.id.base_text};

        sara = new SimpleAdapter(getActivity(), data, R.layout.list_item_single_mat, from, to);
        listView.setAdapter(sara);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "--------  SmetasWorkCostTab2Type onDestroy -----------" );
    }


}
