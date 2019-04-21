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
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
        //аргументы выводим не здесь, а в OnRtsume, так как при щелчке на строке списка
        // этот метод не срабатывает - срабатывают onCreateView и onResume
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "//  SmetasMatTab3Mat onCreateView // " );
        View rootView = inflater.inflate(R.layout.fragment_smetas_mat_tab3_mat, container, false);
        listView = rootView.findViewById(R.id.listViewSmetasMat);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "//  SmetasMatTab3Mat onResume // " );
        file_id = getArguments().getLong(P.ID_FILE);
        position = getArguments().getInt(P.TAB_POSITION);
        isSelectedType = getArguments().getBoolean(P.IS_SELECTED_TYPE);
        type_id = getArguments().getLong(P.ID_TYPE_MAT);
        Log.d(TAG, "SmetasMatTab3Mat onCreate isSelectedType = " + isSelectedType );

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
        String[] matNamesFW = mSmetaOpenHelper.getMatNamesFM(file_id);

        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        Log.d(TAG, " SmetasMatTab3Mat updateAdapter cursor.getCount() = "+ cursor.getCount() );
        while (cursor.moveToNext()){
            String mat_name = cursor.getString(cursor.getColumnIndex(Mat.MAT_NAME));
            Log.d(TAG, " SmetasMatTab3Mat updateAdapter mat_name  = "+
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
        Log.d(TAG, " AdapterOfTypeMat updateAdapter data.size()  = "+ data.size() );
        String[] from = new String[]{P.ATTR_MAT_NAME, P.ATTR_MAT_MARK};
        int [] to = new int[]{R.id.base_text_two_mat, R.id.checkBoxTwoMat};

        sara = new SimpleAdapter(context, data, R.layout.list_item_two_mat_small, from, to);
        listView.setAdapter(sara);
    }


}
