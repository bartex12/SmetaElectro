package ru.bartex.smetaelectro;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.CategoryWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.FW;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.FileWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.TypeWork;

public class SmetasTab1Rabota extends Fragment {

    public static final String TAG = "33333";

    ListView lvSmetasRabota;
    TextView tvSumma;
    SmetaOpenHelper mSmetaOpenHelper;
    ArrayList<Map<String, Object>> data;
    Map<String, Object> m;
    SimpleAdapter sara;
    float[] work_summa; //массив стоимости работ

    long file_id;

    public static SmetasTab1Rabota newInstance(long file_id) {
        SmetasTab1Rabota fragment = new SmetasTab1Rabota();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "SmetasTab1Rabota: onCreate  ");
        //получаем id файла из аргументов
        file_id = getArguments().getLong(P.ID_FILE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSmetaOpenHelper = new SmetaOpenHelper(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_smetas_tab1_rabota, container, false);
        tvSumma = rootView.findViewById(R.id.tvSumma);
        lvSmetasRabota = rootView.findViewById(R.id.listViewSmetasRabota);
        lvSmetasRabota.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_smeta_item = view.findViewById(R.id.base_text);
                String smeta_item_name = tv_smeta_item.getText().toString();
                long work_id = mSmetaOpenHelper.getIdFromWorkName(smeta_item_name);
                long type_id = mSmetaOpenHelper.getTypeIdWork(file_id, work_id);
                long cat_id = mSmetaOpenHelper.getCateIdWork(file_id, work_id);

                Intent intent = new Intent(getActivity(), SmetaDetail.class);
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

    @Override
    public void onResume() {
        super.onResume();
        //обновляем данные списка фрагмента активности
        updateAdapter();
        //обновляем общую сумму сметы
        float totalSumma = updateTotalSumma(work_summa);
        tvSumma.setText("" + totalSumma);

        //объявляем о регистрации контекстного меню
        registerForContextMenu(lvSmetasRabota);
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

            Log.d(TAG, "ChangeTempActivity P.DELETE_CHANGETEMP");
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
                    Log.d(TAG, "ChangeTempActivity P.DELETE_CHANGETEMP acmi.position + 1 =" +
                            (acmi.position + 1));

                    TextView tv = acmi.targetView.findViewById(R.id.base_text);
                    String work_name = tv.getText().toString();
                    //находим id по имени работы
                    long work_id = mSmetaOpenHelper.getIdFromWorkName(work_name);
                    Log.d(TAG, "ListOfSmetasNames onContextItemSelected work_name = " +
                            work_id + " file_id =" + file_id);

                    //удаляем пункт сметы из таблицы FW
                    mSmetaOpenHelper.deleteWorkItemFromFW(file_id, work_id);

                    //обновляем данные списка фрагмента активности
                    updateAdapter();
                    //обновляем общую сумму сметы
                    float totalSumma = updateTotalSumma(work_summa);
                    tvSumma.setText("" + totalSumma);
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

        //Массив работ в файле с file_id
        String[] work_name = mSmetaOpenHelper.getNameOfWork(file_id);
        //Массив расценок для работ в файле с file_id
        float[] work_cost = mSmetaOpenHelper.getCostOfWork(file_id);
        //Массив количества работ для работ в файле с file_id
        float[] work_amount = mSmetaOpenHelper.getAmountOfWork(file_id);
        //Массив единиц измерения для работ в файле с file_id
        String[] work_units = mSmetaOpenHelper.getUnitsOfWork(file_id);
        //Массив стоимости работы  для работ в файле с file_id
        work_summa = mSmetaOpenHelper.getSummaOfWork(file_id);

        //Список с данными для адаптера
        data = new ArrayList<Map<String, Object>>(work_name.length);

        for (int i = 0; i < work_name.length; i++) {
            Log.d(TAG, "SmetasTab1Rabota - updateAdapter  name_file = " + work_name[i]);
            m = new HashMap<>();
            m.put(P.WORK_NUMBER, i + 1);
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
        sara = new SimpleAdapter(getActivity(), data, R.layout.list_item_complex, from, to);
        lvSmetasRabota.setAdapter(sara);
    }

    public float updateTotalSumma(float[] work_summa) {
        float totalSumma = 0;
        for (int i = 0; i < work_summa.length; i++) {
            totalSumma += work_summa[i];
        }
        return totalSumma;
    }


}
