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

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.Mat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;


public class SmetasMatTab extends Fragment {
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

    ViewPager viewPager;

    public interface OnClickTypeMatListener{
        void typeAndCatTransmit(long cat_mat_id,long type_mat_id, boolean isSelectedType);
    }
    SmetasMatTab.OnClickTypeMatListener onClickTypeMatListener;


    public static SmetasMatTab NewInstance(
            long file_id, int position, boolean isSelectedType, long type_id){
        SmetasMatTab fragment = new SmetasMatTab();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        args.putBoolean(P.IS_SELECTED_TYPE, isSelectedType);
        args.putLong(P.ID_TYPE_MAT, type_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "//  SmetasMatTab onAttach // " );
        this.context =context;
        mSmetaOpenHelper = new SmetaOpenHelper(context);
        //получаем  ViewPager viewPager
        viewPager = getActivity().findViewById(R.id.containerMat);
        onClickTypeMatListener = (SmetasMatTab.OnClickTypeMatListener)context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "//  SmetasMatTab onCreate // " );
        file_id = getArguments().getLong(P.ID_FILE);
        position = getArguments().getInt(P.TAB_POSITION);
        isSelectedType = getArguments().getBoolean(P.IS_SELECTED_TYPE);
        type_id = getArguments().getLong(P.ID_TYPE_MAT);
        Log.d(TAG, "SmetasMatTab onCreate isSelectedType = " + isSelectedType );

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "//  SmetasMatTab onCreateView // " );
        View rootView = inflater.inflate(R.layout.fragment_smetas_mat_tab, container, false);
        listView = rootView.findViewById(R.id.listViewMat);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "//  SmetasMatTab onItemClick // " );
                TextView tv_smeta_item = view.findViewById(R.id.base_text_two_mat);
                String smeta_item_name = tv_smeta_item.getText().toString();

                long type_id = mSmetaOpenHelper.getIdFromMatTypeName(smeta_item_name);
                long cat_id = mSmetaOpenHelper.getCatIdFromTypeMat(type_id);
                Log.d(TAG, "SmetasMatTab onItemClick  type_id = " + type_id);

                onClickTypeMatListener.typeAndCatTransmit(cat_id, type_id, true);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "//  SmetasMatTab onResume // " );
        updateAdapter();
    }

    public void updateAdapter(){

        switch (position){

            case 0:

                break;

            case 1:

                break;

            case 2:

                break;

        }
        Log.d(TAG, "//  SmetasMatTab updateAdapter // " );
        Cursor cursor;
        if (isSelectedType){
            Log.d(TAG, "SmetasMatTab updateAdapter isSelectedType = true " );
            //Курсор с именами  всех материалов из таблицы Mat
            cursor = mSmetaOpenHelper.getMatNamesOneType(type_id);
        }else {
            Log.d(TAG, "SmetasMatTab updateAdapter isSelectedType = false " );
            //Курсор с именами  всех материалов из таблицы Mat
            cursor = mSmetaOpenHelper.getMatNamesAllTypes();
        }

        //Строковый массив с именами типов материалов из таблицы FW для файла с file_id
        String[] matNamesFW = mSmetaOpenHelper.getMatNamesFM(file_id);

        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        Log.d(TAG, " SmetasMatTab updateAdapter cursor.getCount() = "+ cursor.getCount() );
        while (cursor.moveToNext()){
            String mat_name = cursor.getString(cursor.getColumnIndex(Mat.MAT_NAME));
            Log.d(TAG, " SmetasMatTab updateAdapter mat_name  = "+
                    (cursor.getPosition()+1) + "  "+ mat_name );
            boolean check_mark = false;
            for (int i=0; i<matNamesFW.length; i++){
                if (matNamesFW[i].equals(mat_name)){
                    check_mark = true;
                }else {
                    check_mark = false;
                }
            }
            m =new HashMap<>();
            m.put(P.ATTR_MAT_NAME, mat_name);
            m.put(P.ATTR_MAT_MARK, check_mark);
            data.add(m);
        }
        Log.d(TAG, " SmetasMatTab updateAdapter data.size()  = "+ data.size() );
        String[] from = new String[]{P.ATTR_MAT_NAME, P.ATTR_MAT_MARK};
        int [] to = new int[]{R.id.base_text_two_mat, R.id.checkBoxTwoMat};

        sara = new SimpleAdapter(context, data, R.layout.list_item_two_mat_small, from, to);
        listView.setAdapter(sara);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "--------  SmetasMatTab onDestroy -------" );
    }
}
