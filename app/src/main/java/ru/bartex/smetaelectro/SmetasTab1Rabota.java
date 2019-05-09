package ru.bartex.smetaelectro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class SmetasTab1Rabota extends Fragment {

    public static final String TAG = "33333";

    ListView lvSmetasRabota;
    SmetaOpenHelper mSmetaOpenHelper;
    ArrayList<Map<String, Object>> data;
    Map<String, Object> m;
    SimpleAdapter sara;
    float[] work_summa; //массив стоимости работ
    float totalSumma; // общая стоимость работ по смете

    long file_id;
    int position;

    View header;
    View footer;

    ViewPager viewPager;

    public static SmetasTab1Rabota newInstance(long file_id, int position) {
        SmetasTab1Rabota fragment = new SmetasTab1Rabota();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "// SmetasTab1Rabota onCreate // " );
        //получаем id файла из аргументов
        file_id = getArguments().getLong(P.ID_FILE);
        position = getArguments().getInt(P.TAB_POSITION);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //получаем  ViewPager viewPager
        viewPager = getActivity().findViewById(R.id.container);
        Log.d(TAG, "// SmetasTab1Rabota onAttach  viewPager = " + viewPager);
        //устанавливаем нужную вкладку в открытое состояние нельзя - при повороте экрана viewPager = null
        //viewPager.setCurrentItem(position);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "//SmetasTab1Rabota  onActivityCreated // " );
        mSmetaOpenHelper = new SmetaOpenHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "// SmetasTab1Rabota onCreateView // " );
        View rootView = inflater.inflate(R.layout.fragment_tabs_for_works_and_materials, container, false);
        //tvSumma = rootView.findViewById(R.id.tvSumma);
        lvSmetasRabota = rootView.findViewById(R.id.listViewFragmentTabs);

        lvSmetasRabota.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_smeta_item = view.findViewById(R.id.base_text);
                String smeta_item_name = tv_smeta_item.getText().toString();
                long work_id = mSmetaOpenHelper.getIdFromWorkName(smeta_item_name);
                long type_id = mSmetaOpenHelper.getTypeIdWork(file_id, work_id);
                long cat_id = mSmetaOpenHelper.getCateIdWork(file_id, work_id);

                Intent intent = new Intent(getActivity(), DetailSmetaLine.class);
                intent.putExtra(P.ID_FILE_DEFAULT, file_id);
                intent.putExtra(P.ID_CATEGORY, cat_id);
                intent.putExtra(P.ID_TYPE, type_id);
                intent.putExtra(P.ID_WORK, work_id);
                intent.putExtra(P.IS_WORK, true);  // такая работа есть
                startActivity(intent);
            }
        });
        return rootView;
    }

    /*
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "//  SmetasTab1Rabota onSaveInstanceState // " );
        outState.putLong("file_id", file_id);
        outState.putInt("position", position);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(TAG, "//  SmetasTab1Rabota onViewStateRestored // " );
        if (savedInstanceState!=null){
            file_id = savedInstanceState.getLong("file_id");
            position = savedInstanceState.getInt("position");
        }
    }

*/
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "//  SmetasTab1Rabota onResume // " );
        //обновляем данные списка фрагмента активности
        updateAdapter();
        //объявляем о регистрации контекстного меню
        registerForContextMenu(lvSmetasRabota);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "//SmetasTab1Rabota onDestroy // " );
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "//SmetasTab1Rabota onPause // " );
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "//SmetasTab1Rabota onStop // " );
        //иначе почему то дублируются
        lvSmetasRabota.removeHeaderView(header);
        lvSmetasRabota.removeFooterView(footer);
    }

    //создаём контекстное меню для списка (сначала регистрация нужна  - здесь в onResume)
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, P.DELETE_ITEM_SMETA, 0, "Удалить пункт");
        menu.add(0, P.CANCEL, 0, "Отмена");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        final AdapterView.AdapterContextMenuInfo acmi =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        //если удалить из контекстного меню
        if (item.getItemId() == P.DELETE_ITEM_SMETA) {

            Log.d(TAG, "SmetasTab1Rabota P.DELETE_CHANGETEMP");
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
                    Log.d(TAG, "SmetasTab1Rabota P.DELETE_CHANGETEMP acmi.position + 1 =" +
                            (acmi.position + 1));

                    TextView tv = acmi.targetView.findViewById(R.id.base_text);
                    String work_name = tv.getText().toString();
                    //находим id по имени работы
                    long work_id = mSmetaOpenHelper.getIdFromWorkName(work_name);
                    Log.d(TAG, "SmetasTab1Rabota onContextItemSelected work_name = " +
                            work_id + " file_id =" + file_id);

                    //удаляем пункт сметы из таблицы FW
                    mSmetaOpenHelper.deleteWorkItemFromFW(file_id, work_id);

                    //иначе почему то дублируются
                    lvSmetasRabota.removeHeaderView(header);
                    lvSmetasRabota.removeFooterView(footer);
                    //обновляем данные списка фрагмента активности
                    updateAdapter();
                }
            });
            builder.show();
            return true;
            //если изменить из контекстного меню
        } else if (item.getItemId() == P.CANCEL) {
            //getActivity().finish();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    public void updateAdapter() {

        Log.d(TAG, "//SmetasTab1Rabota updateAdapter // " );
        //Массив категорий материалов для сметы с file_id
        String[] cat_name = mSmetaOpenHelper.getCategoryNamesFW(file_id);
        Log.d(TAG, "SmetasTab1Rabota - updateAdapter  cat_name.length = " + cat_name.length);
        //массив типов материалов для сметы с file_id
        String[] type_name = mSmetaOpenHelper.getTypeNamesFW(file_id);
        Log.d(TAG, "SmetasTab1Rabota - updateAdapter  type_name.length = " + type_name.length);
        //Массив материалов в файле с file_id
        String[] work_name = mSmetaOpenHelper.getNameOfWork(file_id);
        //Массив цен для материалов в файле с file_id
        float[] work_cost = mSmetaOpenHelper.getCostOfWork(file_id);
        //Массив количества материалов для материалов в файле с file_id
        float[] work_amount = mSmetaOpenHelper.getAmountOfWork(file_id);
        //Массив единиц измерения для материалов в файле с file_id
        String[] work_units = mSmetaOpenHelper.getUnitsOfWork(file_id);
        //Массив стоимости материалов  для материалов в файле с file_id
        work_summa = mSmetaOpenHelper.getSummaOfWork(file_id);

        //Список с данными для адаптера
        data = new ArrayList<Map<String, Object>>(work_name.length);

        for (int i = 0; i < work_name.length; i++) {
            Log.d(TAG, "SmetasTab1Rabota - updateAdapter  work_name = " + work_name[i]);

                m = new HashMap<>();
                m.put(P.WORK_NUMBER, (i + 1));
                m.put(P.WORK_NAME, work_name[i]);
                m.put(P.WORK_COST, work_cost[i]);
                m.put(P.WORK_AMOUNT, work_amount[i]);
                m.put(P.WORK_UNITS, work_units[i]);
                m.put(P.WORK_SUMMA, work_summa[i]);
                data.add(m);
       }
        String[] from = new String[]{P.WORK_NUMBER, P.WORK_NAME, P.WORK_COST, P.WORK_AMOUNT,
                P.WORK_UNITS, P.WORK_SUMMA};
        int[] to = new int[]{R.id.tvNumberOfLine, R.id.base_text, R.id.tvCost, R.id.tvAmount,
                R.id.tvUnits, R.id.tvSumma};

        lvSmetasRabota.removeHeaderView(header);
        //добавляем хедер
        header = getActivity().getLayoutInflater().inflate(R.layout.list_item_single, null);
        String fileName = mSmetaOpenHelper.getFileNameById(file_id);
        ((TextView)header.findViewById(R.id.base_text)).setText(
                String.format(Locale.ENGLISH,"Смета на работу:   %s", fileName));
        lvSmetasRabota.addHeaderView(header, null, false);
        Log.d(TAG, "***********getHeaderViewsCount*********** = " +
                lvSmetasRabota.getHeaderViewsCount());

        lvSmetasRabota.removeFooterView(footer);
        Log.d(TAG, "*********  removeFooterView2  ********* ");
        //добавляем футер
        footer = getActivity().getLayoutInflater().inflate(R.layout.list_item_single, null);
        totalSumma = P.updateTotalSumma(work_summa);
        Log.d(TAG, "SmetasTab1Rabota - updateAdapter  totalSumma = " + totalSumma);
        ((TextView)footer.findViewById(R.id.base_text)).
                setText(String.format(Locale.ENGLISH,"За работу: %.0f руб", totalSumma ));
        lvSmetasRabota.addFooterView(footer, null, false);
        Log.d(TAG, "*********  addFooterView getFooterViewsCount1 = " +
                lvSmetasRabota.getFooterViewsCount());

        sara = new SimpleAdapter(getActivity(), data, R.layout.list_item_complex, from, to);
        lvSmetasRabota.setAdapter(sara);
    }
}
