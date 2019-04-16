package ru.bartex.smetaelectro;

import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.TypeWork;

public class SmetaTypeElectro extends AppCompatActivity
        implements DialogSaveName.WorkCategoryTypeNameListener{

    public static final String TAG = "33333";
    ListView mListView;
    SmetaOpenHelper mSmetaOpenHelper;
    ArrayList<Map<String, Object>> data;
    Map<String,Object> m;
    SimpleAdapter sara;
    long file_id;
    long cat_id;
    AdapterOfType mAdapterOfType;
    int positionCategory;

    @Override
    public void workCategoryTypeNameTransmit(String workName, String typeName, String catName) {
        Log.d(TAG, "SmetaTypeElectro - workCategoryTypeNameTransmit  workName = " + workName +
                "  typeName = " + typeName +"  catName = " + catName);
        //определяем id категории по её имени
        long type_category_Id = mSmetaOpenHelper.getIdFromCategoryName(catName);
        //вставляем имя типа работы в таблицу TypeWork
        long newTypeNameId = mSmetaOpenHelper.insertTypeName(typeName, type_category_Id);
        Log.d(TAG, "SmetaTypeElectro-workCategoryTypeNameTransmit newTypeNameId = " + newTypeNameId);
        //обновляем адаптер
        mAdapterOfType.updateAdapter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smeta_type_electro);

        mSmetaOpenHelper = new SmetaOpenHelper(this);
        file_id = getIntent().getLongExtra(P.ID_FILE_DEFAULT, 1);
        cat_id = getIntent().getLongExtra(P.ID_CATEGORY, 1);
        positionCategory = getIntent().getExtras().getInt(P.POSITION_CATEGORY);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mListView = findViewById(R.id.listViewType);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //находим имя типа работы в адаптере
                TextView tv = view.findViewById(R.id.base_text_two );
                String type_name = tv.getText().toString();
                //находим id по имени типа
                long type_id = mSmetaOpenHelper.getIdFromTypeName(type_name);

                Log.d(TAG, "SmetaTypeElectro - onItemClick  type_id = " + type_id +
                        "  Name = " + type_name);

                Intent intent = new Intent(SmetaTypeElectro.this, SmetaWorkElectro.class);
                intent.putExtra(P.ID_FILE_DEFAULT, file_id);
                intent.putExtra(P.ID_CATEGORY, cat_id);
                intent.putExtra(P.POSITION_CATEGORY, positionCategory);
                intent.putExtra(P.ID_TYPE, type_id);
                intent.putExtra(P.POSITION_TYPE, position);
                startActivity(intent);
            }
        });

        //объявляем о регистрации контекстного меню
        registerForContextMenu(mListView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //конструктор класса адаптера списка работ
        mAdapterOfType = new AdapterOfType(this, file_id, cat_id, mListView);
        //обновляем данные
        mAdapterOfType.updateAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_type_of_work, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                openSaveTypeDialogFragment(positionCategory);
                return true;

            case R.id.action_settings:

                return true;
        }


        return super.onOptionsItemSelected(item);
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

        //получаем имя типа из строки списка типов
        TextView tvType = acmi.targetView.findViewById(R.id.base_text_two);
        String typeName = tvType.getText().toString();
        //находим id типа по имени типа
        long type_id = mSmetaOpenHelper.getIdFromTypeName(typeName);
        //находим количество строк видов работы для type_id
        int countLineWork = mSmetaOpenHelper.getCountLineWork(type_id);
        Log.d(TAG, "onContextItemSelected - countLineWork = " + countLineWork);

        if(countLineWork > 0) {
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
        Log.d(TAG, "SmetaTypeElectro onContextItemSelected id = " + id +
                " acmi.position = " + acmi.position + " acmi.id = " +acmi.id);

        switch (id){
            case P.SPECIFIC_ID:
                Log.d(TAG, "SmetaTypeElectro onContextItemSelected case P.SPECIFIC_ID");
                //получаем имя типа из строки списка типов работ
                TextView tvSpecificType = acmi.targetView.findViewById(R.id.base_text_two);
                String type_name_specific = tvSpecificType.getText().toString();
                //находим id по имени типа
                long type_id_specific = mSmetaOpenHelper.getIdFromTypeName(type_name_specific);
                Log.d(TAG, "SmetaTypeElectro onContextItemSelected case P.SPECIFIC_ID " +
                        "type_name_specific = " + type_name_specific +  " type_id_specific =" + type_id_specific);
                //отправляем интент с id типа
                Intent intentSpecificType = new Intent(SmetaTypeElectro.this, TypeSpecific.class);
                intentSpecificType.putExtra(P.ID_TYPE, type_id_specific);
                startActivity(intentSpecificType);
                return true;

            case P.CHANGE_NAME_ID:
                Log.d(TAG, "SmetaTypeElectro onContextItemSelected case P.CHANGE_NAME_ID");
                //получаем имя типа из строки списка типов работ
                TextView tvChangType = acmi.targetView.findViewById(R.id.base_text_two);
                String type_name_chang = tvChangType.getText().toString();
                //находим id по имени типа
                long type_id_Change = mSmetaOpenHelper.getIdFromTypeName(type_name_chang);
                Log.d(TAG, "SmetaTypeElectro onContextItemSelected  case P.CHANGE_NAME_ID " +
                        "type_name_chang = " + type_name_chang + " type_id_Change =" + type_id_Change);
                //отправляем интент с id типа
                Intent intent = new Intent(SmetaTypeElectro.this, TypeChangeData.class);
                intent.putExtra(P.ID_TYPE, type_id_Change);
                startActivity(intent);

                return true;

            case P.DELETE_ID:
                Log.d(TAG, "SmetaTypeElectro onContextItemSelected case P.DELETE_ID");

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.DeleteType);
                builder.setPositiveButton(R.string.DeleteNo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton(R.string.DeleteYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextView tv = acmi.targetView.findViewById(R.id.base_text_two);
                        String type_name = tv.getText().toString();
                        //находим id по имени файла
                        long type_id = mSmetaOpenHelper.getIdFromTypeName(type_name);
                        Log.d(TAG, "SmetaTypeElectro onContextItemSelected case P.DELETE_ID" +
                                " type_name = " + type_name + " type_id =" + type_id);

                        //Удаляем файл из таблицы TypeWork когда в типе нет видов работ
                        mSmetaOpenHelper.deleteType(type_id);

                        //обновляем данные
                        mAdapterOfType.updateAdapter();
                    }
                });
                builder.show();

                return true;

            case P.CANCEL:
                return true;
        }
        return super.onContextItemSelected(item);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home_smetas_work:
                    // Для данного варианта в манифесте указан режим singlTask для активности MainActivity
                    Intent intentHome = new Intent(SmetaTypeElectro.this, MainActivity.class);
                    // установка флагов- создаём новую задачу и убиваем старую вместе
                    // со всеми предыдущими переходами - работает дёргано по сравнению с манифестом
                    //intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                     //       Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentHome);
                    return true;

                case R.id.navigation_smetas_smetas_work:
                    // Для данного варианта в манифесте указан режим singlTask для активности Smetas
                    Intent intent = new Intent(SmetaTypeElectro.this, Smetas.class);
                    intent.putExtra(P.ID_FILE, file_id);
                    startActivity(intent);
                    //finish();
                    return true;

                case R.id.navigation_costs_smetas_work:
                    Intent intent_costs = new Intent(SmetaTypeElectro.this, CostCategory.class);
                    startActivity(intent_costs);
                    return true;
            }
            return false;
        }
    };

    private void openSaveTypeDialogFragment(int positionCategory){
        DialogFragment saveType = DialogSaveTypeName.newInstance(positionCategory);
        saveType.show(getSupportFragmentManager(), "saveType");
    }


}
