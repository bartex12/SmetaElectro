package ru.bartex.smetaelectro;

import android.content.Context;
import android.content.Intent;
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

public class SmetasMatTab2Type extends Fragment {

    public static final String TAG = "33333";
    ListView listView;
    long file_id;
    int position;
    SimpleAdapter sara;
    ArrayList<Map<String, Object>> data;
    Map<String, Object> m;
    SmetaOpenHelper mSmetaOpenHelper;
    Context context;

    public interface OnClickTypeMatListener{
        void typeAndCatTransmit(long cat_mat_id,long type_mat_id, boolean isSelectedType);
    }
    OnClickTypeMatListener onClickTypeMatListener;


    public static SmetasMatTab2Type NewInstance(long file_id, int position){
        Log.d(TAG, "//  SmetasMatTab2Type NewInstance // " );
        SmetasMatTab2Type fragment = new SmetasMatTab2Type();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putLong(P.TAB_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "//  SmetasMatTab2Type onAttach // " );
        this.context =context;
        mSmetaOpenHelper = new SmetaOpenHelper(context);
        onClickTypeMatListener = (OnClickTypeMatListener)context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "//  SmetasMatTab2Type onCreate // " );
        file_id = getArguments().getLong(P.ID_FILE);
        position = getArguments().getInt(P.TAB_POSITION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "//  SmetasMatTab2Type onCreateView // " );
        View rootView = inflater.inflate(R.layout.fragment_smetas_mat_tab2_type, container, false);
        listView = rootView.findViewById(R.id.listViewSmetasMatType);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "//  SmetasMatTab2Type onItemClick // " );
                TextView tv_smeta_item = view.findViewById(R.id.base_text_two_mat);
                String smeta_item_name = tv_smeta_item.getText().toString();

                long type_id = mSmetaOpenHelper.getIdFromMatTypeName(smeta_item_name);
                long cat_id = mSmetaOpenHelper.getCatIdFromTypeMat(type_id);
                Log.d(TAG, "SmetasMatTab2Type onItemClick  type_id = " + type_id);

                onClickTypeMatListener.typeAndCatTransmit(cat_id, type_id, true);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "//  SmetasMatTab2Type onResume // " );
        //adapterOfTypeMat = new AdapterOfTypeMat(context, file_id, listView);
        //adapterOfTypeMat.updateAdapter();

        updateAdapter();
    }

    public void updateAdapter(){
        Log.d(TAG, "//  SmetasMatTab2Type updateAdapter // " );

        //Курсор с именами категорий материалов из таблицы категорий CategoryMat
        //Cursor cursor1 = smetaOpenHelper.getTypeMatNames(cat_mat_id);
        Cursor cursor = mSmetaOpenHelper.getTypeMatNamesAllTypes();
        //Строковый массив с именами типов материалов из таблицы FM для файла с file_id
        String[] typetMatNamesFM = mSmetaOpenHelper.getTypeNamesFM(file_id);

        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        Log.d(TAG, " SmetasMatTab2Type updateAdapter cursor.getCount() = "+ cursor.getCount() );
        while (cursor.moveToNext()){
            String tipe_mat_name = cursor.getString(cursor.getColumnIndex(TypeMat.TYPE_MAT_NAME));
            Log.d(TAG, " SmetasMatTab2Type updateAdapter tipe_mat_name  = " + tipe_mat_name );
            boolean check_mark = false;
            for (int i=0; i<typetMatNamesFM.length; i++){
                if (typetMatNamesFM[i].equals(tipe_mat_name)){
                    check_mark = true;
                }else {
                    check_mark = false;
                }
            }
            m =new HashMap<>();
            m.put(P.ATTR_TYPE_MAT_NAME,tipe_mat_name);
            m.put(P.ATTR_TYPE_MAT_MARK, check_mark);
            data.add(m);
        }
        Log.d(TAG, " SmetasMatTab2Type updateAdapter data.size()  = "+ data.size() );
        String[] from = new String[]{P.ATTR_TYPE_MAT_NAME, P.ATTR_TYPE_MAT_MARK};
        int [] to = new int[]{R.id.base_text_two_mat, R.id.checkBoxTwoMat};

        sara = new SimpleAdapter(context, data, R.layout.list_item_two_mat, from, to);
        listView.setAdapter(sara);
    }

}
