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

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.CategoryMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.CategoryWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmetasWorkTab1Category extends Fragment {
    public static final String TAG = "33333";

    ListView lvSmetasWorkCategory;
    SmetaOpenHelper mSmetaOpenHelper;
    ArrayList<Map<String, Object>> data;
    long file_id;
    int position;
    HashMap<String, Object> m;
    SimpleAdapter sara;

    public interface OnClickTCategoryWorkListener{
        void catAndClickTransmit(long cat_mat_id, boolean isSelectedCat);
    }
    SmetasWorkTab1Category.OnClickTCategoryWorkListener onClickCategoryWorktListener;

    public static SmetasWorkTab1Category NewInstance(long file_id, int position){
        Log.d(TAG, "//  SmetasWorkTab1Category NewInstance // " );
        SmetasWorkTab1Category fragment = new SmetasWorkTab1Category();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "//  SmetasWorkTab1Category onAttach // " );
        mSmetaOpenHelper = new SmetaOpenHelper(context);
        onClickCategoryWorktListener = (SmetasWorkTab1Category.OnClickTCategoryWorkListener)context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "//  SmetasWorkTab1Category onCreate // " );
        file_id = getArguments().getLong(P.ID_FILE);
        position = getArguments().getInt(P.TAB_POSITION);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "//  SmetasWorkTab1Category onCreateView // " );
        View rootView = inflater.inflate(R.layout.fragment_tabs_for_works_and_materials, container, false);
        lvSmetasWorkCategory = rootView.findViewById(R.id.listViewFragmentTabs);
        lvSmetasWorkCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "//  SmetasWorkTab1Category onItemClick // " );
                TextView tv_smeta_item = view.findViewById(R.id.base_text_two_mat);
                String smeta_item_name = tv_smeta_item.getText().toString();

                long cat_id = mSmetaOpenHelper.getIdFromCategoryName(smeta_item_name);
                Log.d(TAG, "SmetasWorkTab1Category onItemClick  cat_id = " + cat_id);
                //Вызываем метод интерфейса для передачи Id категории материалов
                onClickCategoryWorktListener.catAndClickTransmit(cat_id, true);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "//  SmetasWorkTab1Category onResume // " );
        updateAdapter();

        //объявляем о регистрации контекстного меню здесь, но как то это всё работает из SmetaWork?!
        registerForContextMenu(lvSmetasWorkCategory);
    }

    public void updateAdapter(){
        Log.d(TAG, "//  SmetasWorkTab1Category updateAdapter // " );
        //Курсор с именами категорий из таблицы категорий CategoryMat
        Cursor cursor = mSmetaOpenHelper.getCategoryNames();
        //Строковый массив с именами категорий из таблицы FM для файла с file_id
        String[] catMatNamesFW = mSmetaOpenHelper.getCategoryNamesFW(file_id);

        //Список с данными для адаптера
        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        Log.d(TAG, " SmetasWorkTab1Category updateAdapter Всего категорий материалов = "+ cursor.getCount() );
        while (cursor.moveToNext()) {
            //смотрим значение текущей строки курсора
            String name_cat = cursor.getString(cursor.getColumnIndex(CategoryWork.CATEGORY_NAME));

            boolean check_mark = false;
            for (int i = 0; i<catMatNamesFW.length; i++){
                if (name_cat.equalsIgnoreCase(catMatNamesFW[i])){
                    check_mark = true;
                    //если есть совпадение, прекращаем перебор
                    break;
                }
            }
            Log.d(TAG, " SmetasWorkTab1Category updateAdapter tipe_mat_name  = " +
                    (cursor.getPosition()+1) + " name_cat " + name_cat + "  check_mark = " + check_mark);
            m = new HashMap<>();
            m.put(P.ATTR_CATEGORY_MARK,check_mark);
            m.put(P.ATTR_CATEGORY_NAME,name_cat);
            data.add(m);
        }
        Log.d(TAG, " SmetasWorkTab1Category updateAdapter data.size()  = "+ data.size() );
        String[] from = new String[]{P.ATTR_CATEGORY_MARK,P.ATTR_CATEGORY_NAME};
        int[] to = new int[]{R.id.checkBoxTwoMat, R.id.base_text_two_mat};

        sara =  new SimpleAdapter(getActivity(), data, R.layout.list_item_two_mat, from, to);
        lvSmetasWorkCategory.setAdapter(sara);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "---------  SmetasWorkTab1Category onDestroy -----------" );
    }
}
