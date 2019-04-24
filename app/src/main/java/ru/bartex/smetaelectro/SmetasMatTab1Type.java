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
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.TypeMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.TypeWork;


/**
 * A simple {@link Fragment} subclass.
 */
public class SmetasMatTab1Type extends Fragment {

    public static final String TAG = "33333";
    long file_id;
    int position;

    ListView mListView;
    SmetaOpenHelper mSmetaOpenHelper;
    ArrayList<Map<String, Object>> data;
    HashMap<String, Object> m;
    SimpleAdapter sara;

    public SmetasMatTab1Type() {
        // Required empty public constructor
    }

    public interface OnClickTypeMatListener{
        void typeAndClickTransmit(long type_mat_id, boolean isSelectedType);
    }
    OnClickTypeMatListener onClickTypeMatListener;

    public static SmetasMatTab1Type NewInstance(long file_id, int position){
        SmetasMatTab1Type fragment = new SmetasMatTab1Type();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        file_id = getArguments().getLong(P.ID_FILE);
        position = getArguments().getInt(P.TAB_POSITION);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSmetaOpenHelper = new SmetaOpenHelper(context);
        onClickTypeMatListener = (OnClickTypeMatListener)context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(
                R.layout.fragment_tabs_for_works_and_materials, container, false);
        mListView = view.findViewById(R.id.listViewFragmentTabs);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "//  SmetasMatTab1Type onItemClick // " );
                TextView tv_smeta_item = view.findViewById(R.id.base_text);
                String smeta_item_name = tv_smeta_item.getText().toString();

                long type_id = mSmetaOpenHelper.getIdFromMatTypeName(smeta_item_name);
                Log.d(TAG, "SmetasMatTab1Type onItemClick  type_id = " + type_id);

                onClickTypeMatListener.typeAndClickTransmit(type_id, true);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateAdapter();
    }

    public void updateAdapter(){
        //Курсор с именами типов материалов
        Cursor cursor = mSmetaOpenHelper.getTypeMatNamesAllCategories();
        //Список с данными для адаптера
        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        while (cursor.moveToNext()) {
            //смотрим значение текущей строки курсора
            String name_type = cursor.getString(cursor.getColumnIndex(TypeMat.TYPE_MAT_NAME));
            Log.d(TAG, "SmetasMatTab1Type - updateAdapter  name_type = " + name_type);
            m = new HashMap<>();
            m.put(P.ATTR_TYPE_NAME,name_type);
            data.add(m);
        }
        String[] from = new String[]{P.ATTR_TYPE_NAME};
        int[] to = new int[]{R.id.base_text};
        sara =  new SimpleAdapter(getActivity(), data, R.layout.list_item_single_mat, from, to);
        mListView.setAdapter(sara);
    }
}
