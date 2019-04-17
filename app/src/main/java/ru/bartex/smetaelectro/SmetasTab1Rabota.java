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
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleExpandableListAdapter;
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
    float totalSumma; // общая стоимость работ по смете

    long file_id;

    View header;
    View footer;

    //список для групп
    ArrayList<Map<String,String>> groupData;
    //список для элементов
    ArrayList<Map<String,String>> childDataItem;
    //список для групп элементов
    ArrayList<ArrayList<Map<String,String>>> childData;

    Map<String,String> mm;


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
        totalSumma = P.updateTotalSumma(work_summa);
        tvSumma.setText("" + totalSumma);
        //объявляем о регистрации контекстного меню
        registerForContextMenu(lvSmetasRabota);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onPause() {
        super.onPause();
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

                    //иначе почему то дублируются
                    lvSmetasRabota.removeHeaderView(header);
                    lvSmetasRabota.removeFooterView(footer);

                    //обновляем данные списка фрагмента активности
                    updateAdapter();
                    //обновляем общую сумму сметы
                    totalSumma = P.updateTotalSumma(work_summa);
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

        //Массив категорий работ для сметы с file_id
        String[] cat_name = mSmetaOpenHelper.getCategoryNamesFW(file_id);
        Log.d(TAG, "SmetasTab1Rabota - updateAdapter  cat_name.length = " + cat_name.length);
        //массив типов работ для сметы с file_id
        String[] type_name = mSmetaOpenHelper.getTypeNamesFW(file_id);
        Log.d(TAG, "SmetasTab1Rabota - updateAdapter  type_name.length = " + type_name.length);
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

        for (int j=0; j<cat_name.length; j++){
            //добавляем хедер
            header = getActivity().getLayoutInflater().inflate(R.layout.list_item_single, null);
            ((TextView)header.findViewById(R.id.base_text)).setText((j+1) +" "+ cat_name[j]);
            lvSmetasRabota.addHeaderView(header, null, false);

        for (int i = 0; i < work_name.length; i++) {
            Log.d(TAG, "SmetasTab1Rabota - updateAdapter  work_name = " + work_name[i]);

                m = new HashMap<>();
                m.put(P.WORK_NUMBER, (j+1) + "." +(i + 1));
                m.put(P.WORK_NAME, work_name[i]);
                m.put(P.WORK_COST, work_cost[i]);
                m.put(P.WORK_AMOUNT, work_amount[i]);
                m.put(P.WORK_UNITS, work_units[i]);
                m.put(P.WORK_SUMMA, work_summa[i]);
                data.add(m);
       }
            //добавляем футер
            footer = getActivity().getLayoutInflater().inflate(R.layout.list_item_single, null);
            //обновляем общую сумму сметы
            totalSumma = P.updateTotalSumma(work_summa);
            Log.d(TAG, "SmetasTab1Rabota - updateAdapter  totalSumma = " + totalSumma);
            ((TextView)footer.findViewById(R.id.base_text)).setText("Итого по работе:  " +
                    Float.toString(totalSumma) + " руб");
            lvSmetasRabota.addFooterView(footer, null, false);
        }
        String[] from = new String[]{P.WORK_NUMBER, P.WORK_NAME, P.WORK_COST, P.WORK_AMOUNT,
                P.WORK_UNITS, P.WORK_SUMMA};
        int[] to = new int[]{R.id.tvNumberOfLine, R.id.base_text, R.id.tvCost, R.id.tvAmount,
                R.id.tvUnits, R.id.tvSumma};
        sara = new SimpleAdapter(getActivity(), data, R.layout.list_item_complex, from, to);
        lvSmetasRabota.setAdapter(sara);
    }

/*
    public void updateExpandableAdapter() {

        //Массив категорий работ для сметы с file_id
        //String[] cat_name = mSmetaOpenHelper.getCategoryNamesFW(file_id);

        //массив типов работ для сметы с file_id
        String[] type_name = mSmetaOpenHelper.getTypeNamesFW(file_id);
        Log.d(TAG, "SmetasTab1Rabota - updateAdapter  type_name.length = " + type_name.length);

        //создаём список групп
        groupData = new ArrayList<Map<String,String>>();

        for (String type:type_name) {
            mm = new HashMap<String, String>();
            mm.put("groupName", type);
            groupData.add(mm);
        }
        // список атрибутов групп для чтения
        String[] groupFrom = new String[]{"groupName"};
        // список ID view-элементов, в которые будет помещены атрибуты групп
        int[] groupTo = new int[]{android.R.id.text1};

        //сначала создаём список групп элементов, чтобы в него добавлять списки элементов
        childData = new ArrayList<ArrayList<Map<String,String>>>();

        //теперь создаём список элементов для всех групп
        for (int i=0; i< type_name.length;i++) {

            childDataItem = new ArrayList<Map<String, String>>();

            //массив типов работ для сметы с file_id
            long tipe_id = mSmetaOpenHelper.getIdFromTypeName(type_name[i]);
            //Массив работ в файле с file_id и типом tipe_id
            String[] workNames = mSmetaOpenHelper.getNameOfWorkSelectedType(file_id,tipe_id );
            //Массив расценок для работ в файле с file_id и типом tipe_id
            float[] workCost = mSmetaOpenHelper.getCostOfWorkSelectedType(file_id, tipe_id );
            //Массив количества работ для работ в файле с file_id и типом tipe_id
            float[] workАmount = mSmetaOpenHelper.getAmountOfWorkSelectedType(file_id, tipe_id );
            //Массив единиц измерения для работ в файле с file_id и типом tipe_id
            String[] workUnits = mSmetaOpenHelper.getUnitsOfWorkSelectedType(file_id, tipe_id );
            //Массив стоимости работы  для работ в файле с file_id и типом tipe_id
            float[] workSumma = mSmetaOpenHelper.getSummaOfWorkSelectedType(file_id, tipe_id );
            //Массив стоимости работы  для работ в файле с file_id
            work_summa = mSmetaOpenHelper.getSummaOfWork(file_id);

            for (int j = 0; j < workNames.length; j++) {
                mm = new HashMap<String, String>();
                mm.put("workName", workNames[j]);
                mm.put("workCost", Float.toString(workCost[j]));
                mm.put("workАmount", Float.toString(workАmount[j]));
                mm.put("workUnit", workUnits[j]);
                mm.put("workSumma", Float.toString(workSumma[j]));
                childDataItem.add(mm);
            }
            childData.add(childDataItem);
        }

        // список атрибутов элементов для чтения
        String[] childFrom = new String[]{"workName", "workCost", "workАmount", "workUnit", "workSumma"};
        // список ID view-элементов, в которые будет помещены атрибуты элементов
        int[] childTo = new int[]{R.id.base_text, R.id.tvCost, R.id.tvAmount, R.id.tvUnits, R.id.tvSumma};

        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                getActivity(),
                groupData,
                android.R.layout.simple_expandable_list_item_1,
                groupFrom,groupTo,
                childData,
                R.layout.list_item_complex, //можно свой макет с android:id="@android:id/text1"
                childFrom,childTo);

        lvSmetasRabota.setAdapter(adapter);
        //разворачиваем все группы
        for(int i=0; i < adapter.getGroupCount(); i++)
            lvSmetasRabota.expandGroup(i);
    }
*/

}
