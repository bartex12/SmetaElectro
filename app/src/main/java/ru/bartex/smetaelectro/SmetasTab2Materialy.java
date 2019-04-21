package ru.bartex.smetaelectro;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;

public class SmetasTab2Materialy extends Fragment {

    public static final String TAG ="33333";


    long file_id;
    int position;
    ListView lvSmetasMaterials;
    SmetaOpenHelper mSmetaOpenHelper;
    float[] mat_summa;
    float totalSumma; // общая стоимость материалов по смете
    ArrayList<Map<String, Object>> data;
    HashMap<String, Object> m;
    SimpleAdapter sara;

    ViewPager viewPager;
    View header;
    View footer;

    public static SmetasTab2Materialy newInstance(long file_id, int position){
        SmetasTab2Materialy fragment = new SmetasTab2Materialy();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE,file_id);
        args.putInt(P.TAB_POSITION,position);
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

        viewPager = new ViewPager(context);
        viewPager.setCurrentItem(position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_smetas_tab2_materialy, container, false);
        lvSmetasMaterials = rootView.findViewById(R.id.listViewSmetasMaterial);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateAdapter();
    }

    public void updateAdapter() {

        Log.d(TAG, "//SmetasTab2Materialy updateAdapter // " );
        //Массив категорий материалов для сметы с file_id
        String[] mat_name = mSmetaOpenHelper.getCategoryNamesFM(file_id);
        Log.d(TAG, "SmetasTab2Materialy - updateAdapter  cat_name.length = " + mat_name.length);
        //массив типов материалов для сметы с file_id
        String[] type_mat_name = mSmetaOpenHelper.getTypeNamesFM(file_id);
        Log.d(TAG, "SmetasTab2Materialy - updateAdapter  type_name.length = " + type_mat_name.length);
        //Массив материалов в файле с file_id
        String[] work_mat_name = mSmetaOpenHelper.getNameOfMat(file_id);
        //Массив цен для материалов в файле с file_id
        float[] mat_cost = mSmetaOpenHelper.getCostOfMat(file_id);
        //Массив количества работ для работ в файле с file_id
        float[] mat_amount = mSmetaOpenHelper.getAmountOfMat(file_id);
        //Массив единиц измерения для материалов в файле с file_id
        String[] mat_units = mSmetaOpenHelper.getUnitsOfMat(file_id);
        //Массив стоимости материалов  для работ в файле с file_id
        mat_summa = mSmetaOpenHelper.getSummaOfMat(file_id);

        //Список с данными для адаптера
        data = new ArrayList<Map<String, Object>>(mat_name.length);

        //добавляем хедер
        header = getActivity().getLayoutInflater().inflate(R.layout.list_item_single, null);
        ((TextView)header.findViewById(R.id.base_text)).setText("Смета на материалы");
        if (lvSmetasMaterials.getHeaderViewsCount()>0){
            lvSmetasMaterials.removeHeaderView(header);
        }else {
            lvSmetasMaterials.addHeaderView(header, null, false);
        }
        Log.d(TAG, "***********getHeaderViewsCount*********** = " +
                lvSmetasMaterials.getHeaderViewsCount());

        for (int i = 0; i < mat_name.length; i++) {
            Log.d(TAG, "SmetasTab1Rabota - updateAdapter  mat_name = " + mat_name[i]);

            m = new HashMap<>();
            m.put(P.MAT_NUMBER, (i + 1));
            m.put(P.MAT_NAME, mat_name[i]);
            m.put(P.MAT_COST, mat_cost[i]);
            m.put(P.MAT_AMOUNT, mat_amount[i]);
            m.put(P.MAT_UNITS, mat_units[i]);
            m.put(P.MAT_SUMMA, mat_summa[i]);
            data.add(m);
        }
        //добавляем футер
        footer = getActivity().getLayoutInflater().inflate(R.layout.list_item_single, null);
        Log.d(TAG, "*********  getFooterViewsCount1  ********* = " + lvSmetasMaterials.getFooterViewsCount());
        if (lvSmetasMaterials.getFooterViewsCount()>0){
            Log.d(TAG, "*********  removeFooterView2  ********* >0 ");
            lvSmetasMaterials.removeFooterView(footer);
        }else {
            Log.d(TAG, "*********  addFooterView3  ********* <=0 ");
            lvSmetasMaterials.addFooterView(footer, null, false);
        }
        totalSumma = P.updateTotalSumma(mat_summa);
        Log.d(TAG, "SmetasTab1Rabota - updateAdapter  totalSumma = " + totalSumma);
        ((TextView)footer.findViewById(R.id.base_text)).setText("Итого по работе:  " +
                Float.toString(totalSumma) + " руб");
        Log.d(TAG, "*********  getFooterViewsCount4  ********* = " + lvSmetasMaterials.getFooterViewsCount());

        String[] from = new String[]{P.MAT_NUMBER, P.MAT_NAME, P.MAT_COST, P.MAT_AMOUNT,
                P.MAT_UNITS, P.MAT_SUMMA};
        int[] to = new int[]{R.id.tvNumberOfLine, R.id.base_text, R.id.tvCost, R.id.tvAmount,
                R.id.tvUnits, R.id.tvSumma};
        sara = new SimpleAdapter(getActivity(), data, R.layout.list_item_complex, from, to);
        lvSmetasMaterials.setAdapter(sara);
    }
}
