package ru.bartex.smetaelectro;

import android.content.Context;
import android.content.Intent;
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

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.FM;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.Mat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.TypeMat;

public class SmetasMatTab3Mat extends Fragment {

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


    public static SmetasMatTab3Mat NewInstance(
            long file_id, int position, boolean isSelectedType, long type_id){
        Log.d(TAG, "//  SmetasMatTab3Mat NewInstance // " );
        SmetasMatTab3Mat fragment = new SmetasMatTab3Mat();
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
        Log.d(TAG, "//  SmetasMatTab3Mat onAttach // " );
        this.context =context;
        mSmetaOpenHelper = new SmetaOpenHelper(context);
        //получаем  ViewPager viewPager
        viewPager = getActivity().findViewById(R.id.containerMat);
        //устанавливаем нужную вкладку в открытое состояние
        //viewPager.setCurrentItem(position);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "//  SmetasMatTab3Mat onCreate // " );
        file_id = getArguments().getLong(P.ID_FILE);
        position = getArguments().getInt(P.TAB_POSITION);
        isSelectedType = getArguments().getBoolean(P.IS_SELECTED_TYPE);
        type_id = getArguments().getLong(P.ID_TYPE_MAT);
        Log.d(TAG, "SmetasMatTab3Mat onCreate isSelectedType = " + isSelectedType +
                 " file_id = " + file_id +" position = " + position+ " type_id = " + type_id);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "//  SmetasMatTab3Mat onCreateView // " );
        View rootView = inflater.inflate(R.layout.fragment_tabs_for_works_and_materials, container, false);
        listView = rootView.findViewById(R.id.listViewFragmentTabs);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parent.setSelection(position);
                //находим имя работы в адаптере
                TextView tv = view.findViewById(R.id.base_text_two_mat);
                String mat_name = tv.getText().toString();

                //находим id материала по имени материала
                final long mat_id = mSmetaOpenHelper.getIdFromMatName(mat_name);
                Log.d(TAG, "SmetasMatTab3Mat - onItemClick  mat_id = " + mat_id +
                        "  mat_name = " + mat_name);
                // проверяем есть ли такой  материал в FM для файла с file_id
                final boolean isMat = mSmetaOpenHelper.isWorkInFM(file_id, mat_id);
                Log.d(TAG, "SmetasMatTab3Mat - onItemClick  isMat = " + isMat);

                //ищем id категории материалов, зная id типа
                long cat_id = mSmetaOpenHelper.getCatIdFromTypeMat(type_id);

                Intent intent = new Intent(getActivity(), SmetaMatDetail.class);
                intent.putExtra(P.ID_FILE, file_id);
                intent.putExtra(P.ID_CATEGORY_MAT, cat_id);
                intent.putExtra(P.ID_TYPE_MAT, type_id);
                intent.putExtra(P.ID_MAT, mat_id);
                intent.putExtra(P.IS_MAT, isMat);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "//  SmetasMatTab3Mat onResume // " );
        updateAdapter();
    }
    public void updateAdapter(){
        Log.d(TAG, "//  SmetasMatTab3Mat updateAdapter // " );
        Cursor cursor;
        if (isSelectedType){
            Log.d(TAG, "SmetasMatTab3Mat updateAdapter isSelectedType = true " );
            //Курсор с именами  всех материалов из таблицы Mat
            cursor = mSmetaOpenHelper.getMatNamesOneType(type_id);
        }else {
            Log.d(TAG, "SmetasMatTab3Mat updateAdapter isSelectedType = false " );
            //Курсор с именами  всех материалов из таблицы Mat
            cursor = mSmetaOpenHelper.getMatNamesAllTypes();
        }

        //Строковый массив с именами типов материалов из таблицы FW для файла с file_id
        String[] matNamesFM = mSmetaOpenHelper.getMatNamesFM(file_id);

        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        Log.d(TAG, " SmetasMatTab3Mat updateAdapter Всего материалов = "+ cursor.getCount() );
        while (cursor.moveToNext()){
            String mat_name = cursor.getString(cursor.getColumnIndex(Mat.MAT_NAME));

            boolean check_mark = false;
            for (int i=0; i<matNamesFM.length; i++){
                if (matNamesFM[i].equals(mat_name)){
                    check_mark = true;
                    //если есть совпадение, прекращаем перебор
                    break;
                }
            }
            Log.d(TAG, " SmetasMatTab3Mat updateAdapter mat_name  = " +
                    (cursor.getPosition()+1) + "  " + mat_name + "  check_mark = " + check_mark);
            m =new HashMap<>();
            m.put(P.ATTR_MAT_NAME, mat_name);
            m.put(P.ATTR_MAT_MARK, check_mark);
            data.add(m);
        }
        Log.d(TAG, " SmetasMatTab3Mat updateAdapter data.size()  = "+ data.size() );
        String[] from = new String[]{P.ATTR_MAT_NAME, P.ATTR_MAT_MARK};
        int [] to = new int[]{R.id.base_text_two_mat, R.id.checkBoxTwoMat};

        sara = new SimpleAdapter(getActivity(), data, R.layout.list_item_two_mat, from, to);
        listView.setAdapter(sara);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "--------  SmetasMatTab3Mat onDestroy -------" );
    }
}
