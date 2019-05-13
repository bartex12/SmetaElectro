package ru.bartex.smetaelectro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tabs_for_works_and_materials, container, false);
        //tvSumma = rootView.findViewById(R.id.tvSumma);
        lvSmetasMaterials = rootView.findViewById(R.id.listViewFragmentTabs);
        lvSmetasMaterials.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_smeta_item = view.findViewById(R.id.base_text);
                String smeta_item_name = tv_smeta_item.getText().toString();
                long mat_id = mSmetaOpenHelper.getIdFromMatName(smeta_item_name);
                long type_mat_id = mSmetaOpenHelper.getTypeIdMat(file_id, mat_id);
                long cat_mat_id = mSmetaOpenHelper.getCateIdMat(file_id, mat_id);

                Intent intent = new Intent(getActivity(), SmetaMatDetail.class);
                intent.putExtra(P.ID_FILE, file_id);
                intent.putExtra(P.ID_CATEGORY_MAT, cat_mat_id);
                intent.putExtra(P.ID_TYPE_MAT, type_mat_id);
                intent.putExtra(P.ID_MAT, mat_id);
                intent.putExtra(P.IS_MAT, true);  // такой материал есть
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateAdapter();
        //объявляем о регистрации контекстного меню
        registerForContextMenu(lvSmetasMaterials);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "//SmetasTab2Materialy onPause // " );
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "//SmetasTab2Materialy onStop // " );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "//SmetasTab2Materialy onDestroy // " );
    }

    public void updateAdapter() {
        Log.d(TAG, "//SmetasTab2Materialy updateAdapter // " );
        //Массив категорий материалов для сметы с file_id
        String[] cat_mat_name = mSmetaOpenHelper.getCategoryNamesFM(file_id);
        Log.d(TAG, "SmetasTab2Materialy - updateAdapter  cat_name.length = " + cat_mat_name.length);
        //массив типов материалов для сметы с file_id
        String[] type_mat_name = mSmetaOpenHelper.getTypeNamesFM(file_id);
        Log.d(TAG, "SmetasTab2Materialy - updateAdapter  type_name.length = " + type_mat_name.length);
        //Массив материалов в файле с file_id
        String[] mat_name = mSmetaOpenHelper.getNameOfMat(file_id);
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

        for (int i = 0; i < mat_name.length; i++) {
            Log.d(TAG, "SmetasTab2Materialy - updateAdapter  mat_name = " + mat_name[i]);

            m = new HashMap<>();
            m.put(P.MAT_NUMBER, (i + 1));
            m.put(P.MAT_NAME, mat_name[i]);
            m.put(P.MAT_COST, mat_cost[i]);
            m.put(P.MAT_AMOUNT, mat_amount[i]);
            m.put(P.MAT_UNITS, mat_units[i]);
            m.put(P.MAT_SUMMA, mat_summa[i]);
            Log.d(TAG, "SmetasTab2Materialy - updateAdapter  i+1 = "
                    + (i+1) + " mat_units[i] = " + mat_units[i] );
            data.add(m);
        }

        String[] from = new String[]{P.MAT_NUMBER, P.MAT_NAME, P.MAT_COST, P.MAT_AMOUNT,
                P.MAT_UNITS, P.MAT_SUMMA};
        int[] to = new int[]{R.id.tvNumberOfLine, R.id.base_text, R.id.tvCost, R.id.tvAmount,
                R.id.tvUnits, R.id.tvSumma};

        //***************************Header and Footer***************
        lvSmetasMaterials.removeHeaderView(header);
        //добавляем хедер
        header = getActivity().getLayoutInflater().inflate(R.layout.list_item_single, null);
        String fileName = mSmetaOpenHelper.getFileNameById(file_id);
        ((TextView)header.findViewById(R.id.base_text)).setText(
                String.format(Locale.ENGLISH,"Смета на материалы:   %s", fileName));
        lvSmetasMaterials.addHeaderView(header, null, false);
        Log.d(TAG, "***********getHeaderViewsCount*********** = " +
                lvSmetasMaterials.getHeaderViewsCount());

        lvSmetasMaterials.removeFooterView(footer);
        Log.d(TAG, "*********  removeFooterView2  ********* ");
        //добавляем футер
        footer = getActivity().getLayoutInflater().inflate(R.layout.list_item_single, null);
        totalSumma = P.updateTotalSumma(mat_summa);
        Log.d(TAG, "SmetasTab1Rabota - updateAdapter  totalSumma = " + totalSumma);
        ((TextView)footer.findViewById(R.id.base_text)).
                setText(String.format(Locale.ENGLISH,"За материалы: %.0f руб", totalSumma ));
        lvSmetasMaterials.addFooterView(footer, null, false);
        Log.d(TAG, "*********  addFooterView getFooterViewsCount1 = " +
                lvSmetasMaterials.getFooterViewsCount());
        //***************************Header and Footer***************

        sara = new SimpleAdapter(getActivity(), data, R.layout.list_item_complex, from, to);
        lvSmetasMaterials.setAdapter(sara);
    }

    //создаём контекстное меню для списка (сначала регистрация нужна  - здесь в onResume)
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, P.DELETE_ITEM_SMETA_MAT, 0, "Удалить пункт");
        menu.add(0, P.CANCEL_MAT, 0, "Отмена");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        final AdapterView.AdapterContextMenuInfo acmi =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        //если удалить из контекстного меню
        if (item.getItemId() == P.DELETE_ITEM_SMETA_MAT) {

            Log.d(TAG, "SmetasTab2Materialy P.DELETE_CHANGETEMP");
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.Delete_Item);
            builder.setPositiveButton(R.string.DeleteNo, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setNegativeButton(R.string.DeleteYes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG, "SmetasTab2Materialy P.DELETE_CHANGETEMP acmi.position + 1 =" +
                            (acmi.position + 1));

                    TextView tv = acmi.targetView.findViewById(R.id.base_text);
                    String mat_name = tv.getText().toString();
                    //находим id по имени работы
                    long mat_id = mSmetaOpenHelper.getIdFromMatName(mat_name);
                    Log.d(TAG, "SmetasTab2Materialy onContextItemSelected file_id = " +
                            file_id + " mat_id =" + mat_id + " mat_name =" + mat_name);

                    mSmetaOpenHelper.displayFM();

                    //удаляем пункт сметы из таблицы FM
                    mSmetaOpenHelper.deleteMatItemFromFM(file_id, mat_id);

                    //иначе почему то дублируются
                    //lvSmetasMaterials.removeHeaderView(header);
                   // lvSmetasMaterials.removeFooterView(footer);
                    //обновляем данные списка фрагмента активности
                    updateAdapter();
                }
            });
            builder.show();
            return true;
            //если изменить из контекстного меню
        } else if (item.getItemId() == P.CANCEL_MAT) {
            //getActivity().finish();
            return true;
        }
        return super.onContextItemSelected(item);
    }

}
