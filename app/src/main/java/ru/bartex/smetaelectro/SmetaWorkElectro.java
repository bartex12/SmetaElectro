package ru.bartex.smetaelectro;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;

public class SmetaWorkElectro extends AppCompatActivity
        implements DialogSaveName.WorkCategoryTypeNameListener{

    public static final String TAG = "33333";
    ListView mListView;
    SmetaOpenHelper mSmetaOpenHelper;
    long file_id;
    long cat_id;
    long type_id;
    AdapterOfWork mAdapterOfWork;
    int positionCategory;
    int positionType;

    @Override
    public void workCategoryTypeNameTransmit(String workName, String typeName, String catName) {
        Log.d(TAG, "SmetaWorkElectro - workCategoryTypeWorkNameTransmit  workName = " + workName +
                "  typeName = " + typeName + "  catName = " + catName);
        //определяем id типа по его имени
        long work_type_Id = mSmetaOpenHelper.getIdFromTypeName(typeName);
        //вставляем имя  работы в таблицу Work
        long newWorkNameId = mSmetaOpenHelper.insertWorkName(workName, work_type_Id);
        Log.d(TAG, "SmetaWorkElectro-workCategoryTypeWorkNameTransmit newWorkNameId = " + newWorkNameId);
        //обновляем адаптер
        mAdapterOfWork.updateAdapter();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smeta_work_electro);

        mSmetaOpenHelper = new SmetaOpenHelper(this);
        file_id = getIntent().getLongExtra(P.ID_FILE_DEFAULT, 1);
        cat_id = getIntent().getLongExtra(P.ID_CATEGORY, 1);
        type_id = getIntent().getLongExtra(P.ID_TYPE, 1);
        positionCategory = getIntent().getIntExtra(P.POSITION_CATEGORY, 1);
        positionType = getIntent().getIntExtra(P.POSITION_TYPE, 1);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mListView = findViewById(R.id.listViewWorkElectro);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //находим имя работы в адаптере
                TextView tv = view.findViewById(R.id.base_text_two);
                String work_name = tv.getText().toString();

                //находим id по имени работы
                final long work_id = mSmetaOpenHelper.getIdFromWorkName(work_name);
                Log.d(TAG, "SmetaWorkElectro - onItemClick  work_id = " + work_id +
                        "  Name = " + work_name);
                // проверяем есть ли такая работа в FW для файла с file_id
                final boolean isWork = mSmetaOpenHelper.isWorkInFW(file_id, work_id);
                Log.d(TAG, "SmetaWorkElectro - onItemClick  isWork = " + isWork);

                Intent intent = new Intent(SmetaWorkElectro.this, SmetaDetail.class);
                intent.putExtra(P.ID_FILE_DEFAULT, file_id);
                intent.putExtra(P.ID_CATEGORY, cat_id);
                intent.putExtra(P.ID_TYPE, type_id);
                intent.putExtra(P.ID_WORK, work_id);
                intent.putExtra(P.IS_WORK, isWork);
                startActivity(intent);
            }
        });

        //объявляем о регистрации контекстного меню
        registerForContextMenu(mListView);
    }

    //создаём контекстное меню для списка
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, P.SPECIFIC_ID, 0, R.string.action_detail);
        menu.add(0, P.CHANGE_NAME_ID, 0, R.string.action_change_name);
        menu.add(0, P.DELETE_ID, 0, R.string.action_delete);
        menu.add(0, P.CANCEL, 0, R.string.action_cancel);

        // получаем инфу о пункте списка
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;

        //получаем имя работы  из строки списка видов работ
        TextView tvWork = acmi.targetView.findViewById(R.id.base_text_two);
        String workName = tvWork.getText().toString();
        //находим id вида  по имени вида работ
        long work_id = mSmetaOpenHelper.getIdFromWorkName(workName);

        //находим количество строк видов работы в таблице FW для work_id
        int countLineWorkFW = mSmetaOpenHelper.getCountLineWorkInFW(work_id);
        //находим количество строк расценок работы в таблице CostWork для work_id
        int countCostLineWork = mSmetaOpenHelper.getCountLineWorkInCost(work_id);
        Log.d(TAG, "onContextItemSelected - countLineWorkFW = " + countLineWorkFW +
                " countCostLineWork =" + countCostLineWork);

        mSmetaOpenHelper.displayTableCost();

        if(countLineWorkFW > 0) {
            menu.findItem(P.DELETE_ID).setEnabled(false); //так лучше
            //menu.findItem(P.DELETE_ID).setVisible(false);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        // получаем инфу о пункте списка
        final AdapterView.AdapterContextMenuInfo acmi =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int id = item.getItemId();
        Log.d(TAG, "SmetaWorkElectro onContextItemSelected id = " + id +
                " acmi.position = " + acmi.position + " acmi.id = " +acmi.id);

        switch (id){
            case P.SPECIFIC_ID:

                Log.d(TAG, "SmetaWorkElectro onContextItemSelected case P.SPECIFIC_ID");
                //получаем имя типа из строки списка типов работ
                TextView tvSpecificWork = acmi.targetView.findViewById(R.id.base_text_two);
                String work_name_specific = tvSpecificWork.getText().toString();
                //находим id по имени типа
                long work_id_specific = mSmetaOpenHelper.getIdFromWorkName(work_name_specific);
                Log.d(TAG, "SmetaWorkElectro onContextItemSelected case P.SPECIFIC_ID " +
                        "work_name_specific = " + work_name_specific +  " work_id_specific =" + work_id_specific);
                //отправляем интент с id типа
                Intent intentSpecificWork = new Intent(SmetaWorkElectro.this, WorkSpesific.class);
                intentSpecificWork.putExtra(P.ID_WORK, work_id_specific);
                startActivity(intentSpecificWork);

                return true;

            case P.CHANGE_NAME_ID:

                Log.d(TAG, "SmetaWorkElectro onContextItemSelected case P.CHANGE_NAME_ID");
                //получаем имя типа из строки списка типов работ
                TextView tvChangWork = acmi.targetView.findViewById(R.id.base_text_two);
                String work_name_chang = tvChangWork.getText().toString();
                //находим id по имени работы
                long work_id_Change = mSmetaOpenHelper.getIdFromWorkName(work_name_chang);
                Log.d(TAG, "SmetaWorkElectro onContextItemSelected  case P.CHANGE_NAME_ID " +
                        "work_name_chang = " + work_name_chang + " work_id_Change =" + work_id_Change);
                //отправляем интент с id работы
                Intent intent = new Intent(SmetaWorkElectro.this, WorkChangeData.class);
                intent.putExtra(P.ID_WORK, work_id_Change);
                startActivity(intent);
                return true;

            case P.DELETE_ID:
                Log.d(TAG, "SmetaWorkElectro onContextItemSelected case P.DELETE_ID");

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.DeleteKind);
                builder.setPositiveButton(R.string.DeleteNo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton(R.string.DeleteYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextView tv = acmi.targetView.findViewById(R.id.base_text_two);
                        String work_Name = tv.getText().toString();
                        //находим id по имени файла
                        long work_id = mSmetaOpenHelper.getIdFromWorkName(work_Name);
                        Log.d(TAG, "SmetaWorkElectro onContextItemSelected case P.DELETE_ID" +
                                " work_Name = " + work_Name + " work_id =" + work_id);

                        //Удаляем запись из таблицы Work когда в таблице FW нет такой  работы
                        // проверка в onCreateContextMenu
                        mSmetaOpenHelper.deleteWork(work_id);
                        //Удаляем запись из таблицы CostWork когда в таблице FW нет такой  работы
                        // проверка в onCreateContextMenu
                        mSmetaOpenHelper.deleteCostOfWork(work_id);
                        //обновляем данные
                        mAdapterOfWork.updateAdapter();
                    }
                });
                builder.show();

                return true;

            case P.CANCEL:
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //конструктор класса адаптера списка работ
        mAdapterOfWork = new AdapterOfWork(this, file_id, type_id, mListView);
        //обновляем данные
        mAdapterOfWork.updateAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_work_of_work, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                openSaveWorkDialogFragment(cat_id, positionCategory, positionType);
                return true;

            case R.id.action_settings:

                return true;
        }


        return super.onOptionsItemSelected(item);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home_smetas_work:
                    // Для данного варианта в манифесте указан режим singlTask для активности MainActivity
                    Intent intentHome = new Intent(SmetaWorkElectro.this, MainActivity.class);
                    // установка флагов- создаём новую задачу и убиваем старую вместе
                    // со всеми предыдущими переходами Для первого экрана это подходит
                    //Но  - работает дёргано по сравнению с манифестом
                    //intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                      //      Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentHome);
                    return true;

                case R.id.navigation_smetas_smetas_work:
                    // Для данного варианта в манифесте указан режим singlTask для активности Smetas
                    Intent intent = new Intent(SmetaWorkElectro.this, Smetas.class);
                    intent.putExtra(P.ID_FILE, file_id);
                    startActivity(intent);
                    return true;

                case R.id.navigation_costs_smetas_work:
                    Intent intent_costs = new Intent(SmetaWorkElectro.this, CostCategory.class);
                    startActivity(intent_costs);
                    return true;
            }
            return false;
        }
    };

    private void openSaveWorkDialogFragment(long cat_id, int positionCategory, int  positionType){
        DialogFragment dialogFragment = DialogSaveWorkName.newInstance(cat_id, positionCategory, positionType);
        dialogFragment.show(getSupportFragmentManager(), "SaveWorkName");
    }


}
