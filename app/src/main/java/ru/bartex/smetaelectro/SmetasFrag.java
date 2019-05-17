package ru.bartex.smetaelectro;


import android.content.Context;
import android.content.Intent;
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
import java.util.Locale;
import java.util.Map;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmetasFrag extends Fragment {


    public SmetasFrag() {
        // Required empty public constructor
    }

    public static final String TAG = "33333";

    ListView lvSmetas;
    SmetaOpenHelper mSmetaOpenHelper;
    ArrayList<Map<String, Object>> data;
    Map<String, Object> m;
    SimpleAdapter sara;
    float[] array_summa; //массив стоимости работ
    float totalSumma; // общая стоимость работ по смете

    long file_id;
    int positionItem;

    View header;
    View footer;

    ViewPager viewPager;

    public static SmetasFrag newInstance(long file_id, int position) {
        SmetasFrag fragment = new SmetasFrag();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "// SmetasFrag onCreate // " );
        //получаем id файла из аргументов
        file_id = getArguments().getLong(P.ID_FILE);
        positionItem = getArguments().getInt(P.TAB_POSITION);
        Log.d(TAG, "SmetasFrag onCreate  file_id = " + file_id + "  positionItem = " +  positionItem);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //получаем  ViewPager viewPager
        viewPager = getActivity().findViewById(R.id.container);
        mSmetaOpenHelper = new SmetaOpenHelper(context);
        Log.d(TAG, "// SmetasFrag onAttach  viewPager = " + viewPager);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "// SmetasFrag onCreateView // " );
        View rootView = inflater.inflate(R.layout.fragment_tabs_for_works_and_materials, container, false);
        lvSmetas = rootView.findViewById(R.id.listViewFragmentTabs);

        lvSmetas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_smeta_item = view.findViewById(R.id.base_text);
                String smeta_item_name = tv_smeta_item.getText().toString();
                switch (viewPager.getCurrentItem()){
                    //switch (positionItem){
                    case 0:
                        long work_id = mSmetaOpenHelper.getIdFromWorkName(smeta_item_name);
                        long type_id = mSmetaOpenHelper.getTypeIdWork(file_id, work_id);
                        long cat_id = mSmetaOpenHelper.getCateIdWork(file_id, work_id);

                        Intent intent_work = new Intent(getActivity(), DetailSmetaLine.class);
                        intent_work.putExtra(P.ID_FILE_DEFAULT, file_id);
                        intent_work.putExtra(P.ID_CATEGORY, cat_id);
                        intent_work.putExtra(P.ID_TYPE, type_id);
                        intent_work.putExtra(P.ID_WORK, work_id);
                        intent_work.putExtra(P.IS_WORK, true);  // такая работа есть
                        startActivity(intent_work);
                        break;

                    case 1:
                        long mat_id = mSmetaOpenHelper.getIdFromMatName(smeta_item_name);
                        long type_mat_id = mSmetaOpenHelper.getTypeIdMat(file_id, mat_id);
                        long cat_mat_id = mSmetaOpenHelper.getCateIdMat(file_id, mat_id);

                        Intent intent_mat = new Intent(getActivity(), DetailSmetaMatLine.class);
                        intent_mat.putExtra(P.ID_FILE, file_id);
                        intent_mat.putExtra(P.ID_CATEGORY_MAT, cat_mat_id);
                        intent_mat.putExtra(P.ID_TYPE_MAT, type_mat_id);
                        intent_mat.putExtra(P.ID_MAT, mat_id);
                        intent_mat.putExtra(P.IS_MAT, true);  // такой материал есть
                        startActivity(intent_mat);
                        break;
                }
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "//  SmetasFrag onResume // " );
        //обновляем данные списка фрагмента активности
        updateAdapter();
        //объявляем о регистрации контекстного меню
        //странно, но работа с контекстным меню происходит в активности Smetas, а не здесь
        registerForContextMenu(lvSmetas);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "//SmetasFrag onPause // " );
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "//SmetasFrag onStop // " );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "//SmetasFrag onDestroy // " );
    }


    public void updateAdapter() {
        Log.d(TAG, "SmetasFrag updateAdapter positionItem = " + positionItem);
        //switch (viewPager.getCurrentItem()){
            switch (positionItem){
            case 0:
                Log.d(TAG, "//SmetasFrag updateAdapter case 0// " );
                //Массив категорий материалов для сметы с file_id
                String[] cat_name = mSmetaOpenHelper.getCategoryNamesFW(file_id);
                Log.d(TAG, "SmetasFrag - updateAdapter  cat_name.length = " + cat_name.length);
                //массив типов материалов для сметы с file_id
                String[] type_name = mSmetaOpenHelper.getTypeNamesFW(file_id);
                Log.d(TAG, "SmetasFrag - updateAdapter  type_name.length = " + type_name.length);
                //Массив материалов в файле с file_id
                String[] work_name = mSmetaOpenHelper.getNameOfWork(file_id);
                //Массив цен для материалов в файле с file_id
                float[] work_cost = mSmetaOpenHelper.getCostOfWork(file_id);
                //Массив количества материалов для материалов в файле с file_id
                float[] work_amount = mSmetaOpenHelper.getAmountOfWork(file_id);
                //Массив единиц измерения для материалов в файле с file_id
                String[] work_units = mSmetaOpenHelper.getUnitsOfWork(file_id);
                //Массив стоимости материалов  для материалов в файле с file_id
                array_summa = mSmetaOpenHelper.getSummaOfWork(file_id);

                //Список с данными для адаптера
                data = new ArrayList<Map<String, Object>>(work_name.length);

                for (int i = 0; i < work_name.length; i++) {
                    Log.d(TAG, "SmetasFrag - updateAdapter  work_name = " + work_name[i]);

                    m = new HashMap<>();
                    m.put(P.WORK_NUMBER, (i + 1));
                    m.put(P.WORK_NAME, work_name[i]);
                    m.put(P.WORK_COST, work_cost[i]);
                    m.put(P.WORK_AMOUNT, work_amount[i]);
                    m.put(P.WORK_UNITS, work_units[i]);
                    m.put(P.WORK_SUMMA, array_summa[i]);
                    data.add(m);
                }
                String[] fromWork = new String[]{P.WORK_NUMBER, P.WORK_NAME, P.WORK_COST, P.WORK_AMOUNT,
                        P.WORK_UNITS, P.WORK_SUMMA};
                int[] toWork = new int[]{R.id.tvNumberOfLine, R.id.base_text, R.id.tvCost, R.id.tvAmount,
                        R.id.tvUnits, R.id.tvSumma};

                lvSmetas.removeHeaderView(header);
                //добавляем хедер
                header = getActivity().getLayoutInflater().inflate(R.layout.list_item_single, null);
                String fileName = mSmetaOpenHelper.getFileNameById(file_id);
                ((TextView)header.findViewById(R.id.base_text)).setText(
                        String.format(Locale.ENGLISH,"Смета на работу:   %s", fileName));
                lvSmetas.addHeaderView(header, null, false);
                Log.d(TAG, "***********getHeaderViewsCount*********** = " +
                        lvSmetas.getHeaderViewsCount());

                lvSmetas.removeFooterView(footer);
                Log.d(TAG, "*********  removeFooterView2  ********* ");
                //добавляем футер
                footer = getActivity().getLayoutInflater().inflate(R.layout.list_item_single, null);
                totalSumma = P.updateTotalSumma(array_summa);
                Log.d(TAG, "SmetasFrag - updateAdapter  totalSumma = " + totalSumma);
                ((TextView)footer.findViewById(R.id.base_text)).
                        setText(String.format(Locale.ENGLISH,"За работу: %.0f руб", totalSumma ));
                lvSmetas.addFooterView(footer, null, false);
                Log.d(TAG, "*********  addFooterView getFooterViewsCount1 = " +
                        lvSmetas.getFooterViewsCount());

                sara = new SimpleAdapter(getActivity(), data, R.layout.list_item_complex, fromWork, toWork);
                lvSmetas.setAdapter(sara);
                break;

            case 1:
                Log.d(TAG, "//SmetasFrag updateAdapter case 1// " );
                //Массив категорий материалов для сметы с file_id
                String[] cat_mat_name = mSmetaOpenHelper.getCategoryNamesFM(file_id);
                Log.d(TAG, "SmetasFrag - updateAdapter  cat_name.length = " + cat_mat_name.length);
                //массив типов материалов для сметы с file_id
                String[] type_mat_name = mSmetaOpenHelper.getTypeNamesFM(file_id);
                Log.d(TAG, "SmetasFrag - updateAdapter  type_name.length = " + type_mat_name.length);
                //Массив материалов в файле с file_id
                String[] mat_name = mSmetaOpenHelper.getNameOfMat(file_id);
                //Массив цен для материалов в файле с file_id
                float[] mat_cost = mSmetaOpenHelper.getCostOfMat(file_id);
                //Массив количества работ для работ в файле с file_id
                float[] mat_amount = mSmetaOpenHelper.getAmountOfMat(file_id);
                //Массив единиц измерения для материалов в файле с file_id
                String[] mat_units = mSmetaOpenHelper.getUnitsOfMat(file_id);
                //Массив стоимости материалов  для работ в файле с file_id
                array_summa = mSmetaOpenHelper.getSummaOfMat(file_id);

                //Список с данными для адаптера
                data = new ArrayList<Map<String, Object>>(mat_name.length);

                for (int i = 0; i < mat_name.length; i++) {
                    Log.d(TAG, "SmetasFrag - updateAdapter  mat_name = " + mat_name[i]);

                    m = new HashMap<>();
                    m.put(P.MAT_NUMBER, (i + 1));
                    m.put(P.MAT_NAME, mat_name[i]);
                    m.put(P.MAT_COST, mat_cost[i]);
                    m.put(P.MAT_AMOUNT, mat_amount[i]);
                    m.put(P.MAT_UNITS, mat_units[i]);
                    m.put(P.MAT_SUMMA, array_summa[i]);
                    Log.d(TAG, "SmetasFrag - updateAdapter  i+1 = "
                            + (i+1) + " mat_units[i] = " + mat_units[i] );
                    data.add(m);
                }

                String[] from = new String[]{P.MAT_NUMBER, P.MAT_NAME, P.MAT_COST, P.MAT_AMOUNT,
                        P.MAT_UNITS, P.MAT_SUMMA};
                int[] to = new int[]{R.id.tvNumberOfLine, R.id.base_text, R.id.tvCost, R.id.tvAmount,
                        R.id.tvUnits, R.id.tvSumma};

                //***************************Header and Footer***************
                lvSmetas.removeHeaderView(header);
                //добавляем хедер
                header = getActivity().getLayoutInflater().inflate(R.layout.list_item_single, null);
                String fileNameMat = mSmetaOpenHelper.getFileNameById(file_id);
                ((TextView)header.findViewById(R.id.base_text)).setText(
                        String.format(Locale.ENGLISH,"Смета на материалы:   %s", fileNameMat));
                lvSmetas.addHeaderView(header, null, false);
                Log.d(TAG, "***********getHeaderViewsCount*********** = " +
                        lvSmetas.getHeaderViewsCount());

                lvSmetas.removeFooterView(footer);
                Log.d(TAG, "*********  removeFooterView2  ********* ");
                //добавляем футер
                footer = getActivity().getLayoutInflater().inflate(R.layout.list_item_single, null);
                totalSumma = P.updateTotalSumma(array_summa);
                Log.d(TAG, "SmetasFrag - updateAdapter  totalSumma = " + totalSumma);
                ((TextView)footer.findViewById(R.id.base_text)).
                        setText(String.format(Locale.ENGLISH,"За материалы: %.0f руб", totalSumma ));
                lvSmetas.addFooterView(footer, null, false);
                Log.d(TAG, "*********  addFooterView getFooterViewsCount1 = " +
                        lvSmetas.getFooterViewsCount());
                //***************************Header and Footer***************

                sara = new SimpleAdapter(getActivity(), data, R.layout.list_item_complex, from, to);
                lvSmetas.setAdapter(sara);
                break;
        }
    }
}
