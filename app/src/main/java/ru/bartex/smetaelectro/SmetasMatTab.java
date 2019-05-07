package ru.bartex.smetaelectro;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.CategoryMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.Mat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;


public abstract class SmetasMatTab extends Fragment {
    public static final String TAG = "33333";
    ListView listView;
    long file_id;
    int position;
    SimpleAdapter sara;
    ArrayList<Map<String, Object>> data;
    Map<String, Object> m;

    SmetaOpenHelper mSmetaOpenHelper;
    boolean isSelectedType;
    long type_id;

    public abstract  void updateAdapter();

    public interface OnClickCatListener{
        void catAndClickTransmit(long cat_id, boolean isSelectedCat);
    }
    OnClickCatListener onClickCatListener;

     /*
    public static SmetasMatTab NewInstance(long file_id, int position){
        Log.d(TAG, "//  SmetasMatTab NewInstance // " );
        SmetasMatTab fragment = new SmetasMatTab();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }
    */

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "//  SmetasMatTab onAttach // " );
        mSmetaOpenHelper = new SmetaOpenHelper(context);
        onClickCatListener = (OnClickCatListener)context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "//  SmetasMatTab onCreate // " );
        file_id = getArguments().getLong(P.ID_FILE);
        position = getArguments().getInt(P.TAB_POSITION);
        Log.d(TAG, "SmetasMatTab onCreate isSelectedType = " + isSelectedType );

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "//  SmetasMatTab onCreateView // " );
        // Inflate the layout for this fragment
        View view = inflater.inflate(
                R.layout.fragment_tabs_for_works_and_materials, container, false);
        listView = view.findViewById(R.id.listViewFragmentTabs);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "//  SmetasMatTab onItemClick // " );
                TextView tv_smeta_item = view.findViewById(R.id.base_text);
                String smeta_item_name = tv_smeta_item.getText().toString();

                long cat_id = mSmetaOpenHelper.getCatIdFromCategoryMatName(smeta_item_name);
                Log.d(TAG, "SmetasMatTab onItemClick  cat_id = " + cat_id);

                onClickCatListener.catAndClickTransmit(cat_id, true);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "//  SmetasMatTab onResume // " );
        updateAdapter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "--------  SmetasMatTab onDestroy -------" );
    }


    /*public void updateAdapter(){
        //Курсор с именами категорий материалов
        Cursor cursor = mSmetaOpenHelper.getMatCategoryNames();
        //Список с данными для адаптера
        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        while (cursor.moveToNext()) {
            //смотрим значение текущей строки курсора
            String name_cat_cost = cursor.getString(cursor.getColumnIndex(CategoryMat.CATEGORY_MAT_NAME));
            Log.d(TAG, "SmetasMatTab0Cat - updateAdapter  name_cat_cost = " + name_cat_cost);
            m = new HashMap<>();
            m.put(P.ATTR_CAT_MAT_NAME,name_cat_cost);
            data.add(m);
        }
        String[] from = new String[]{P.ATTR_CAT_MAT_NAME};
        int[] to = new int[]{R.id.base_text};
        sara =  new SimpleAdapter(getActivity(), data, R.layout.list_item_single_mat, from, to);
        listView.setAdapter(sara);
    }
    */

}
