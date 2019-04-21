package ru.bartex.smetaelectro;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.CategoryMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.CategoryWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;

public class SmetasMatTab1Category extends Fragment {

    public static final String TAG = "33333";

    ListView lvSmetasMatCategory;
    SmetaOpenHelper mSmetaOpenHelper;
    ArrayList<Map<String, Object>> data;
    long file_id;
    int position;
    HashMap<String, Object> m;
    SimpleAdapter sara;

    public static SmetasMatTab1Category NewInstance(long file_id, int position){
        Log.d(TAG, "//  SmetasMatTab1Category NewInstance // " );
        SmetasMatTab1Category fragment = new SmetasMatTab1Category();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putLong(P.TAB_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "//  SmetasMatTab1Category onAttach // " );
        mSmetaOpenHelper = new SmetaOpenHelper(context);
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
        Log.d(TAG, "//  SmetasMatTab1Category onCreateView // " );
        View rootView = inflater.inflate(R.layout.fragment_smetas_mat_tab1_category, container, false);
        lvSmetasMatCategory = rootView.findViewById(R.id.listViewSmetasMatCategory);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "//  SmetasMatTab1Category onResume // " );
        updateAdapter();
    }


    public void updateAdapter(){
        //Курсор с именами категорий из таблицы категорий CategoryMat
        Cursor cursor = mSmetaOpenHelper.getMatCategoryNames();
        //Строковый массив с именами категорий из таблицы FM для файла с file_id
        String[] catMatNamesFM = mSmetaOpenHelper.getCategoryNamesFM(file_id);

        //Список с данными для адаптера
        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        while (cursor.moveToNext()) {
            //смотрим значение текущей строки курсора
            String name_cat_mat = cursor.getString(cursor.getColumnIndex(CategoryMat.CATEGORY_MAT_NAME));
            boolean chek_mark = false;
            for (int i = 0; i<catMatNamesFM.length; i++){
                if (name_cat_mat.equalsIgnoreCase(catMatNamesFM[i])){
                    chek_mark = true;
                }
            }
            m = new HashMap<>();
            m.put(P.ATTR_CATEGORY_MARK,chek_mark);
            m.put(P.ATTR_CATEGORY_NAME,name_cat_mat);
            data.add(m);
        }
        String[] from = new String[]{P.ATTR_CATEGORY_MARK,P.ATTR_CATEGORY_NAME};
        int[] to = new int[]{R.id.checkBoxTwoMat, R.id.base_text_two_mat};

        sara =  new SimpleAdapter(getActivity(), data, R.layout.list_item_two_mat, from, to);
        lvSmetasMatCategory.setAdapter(sara);
    }
}
