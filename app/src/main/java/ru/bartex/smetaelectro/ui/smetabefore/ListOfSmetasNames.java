package ru.bartex.smetaelectro.ui.smetabefore;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.Smetas;
import ru.bartex.smetaelectro.data.DataFile;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.files.FileWork;
import ru.bartex.smetaelectro.ui.dialogs.DialogWorkOrMatCosts;
import ru.bartex.smetaelectro.ui.main.RecyclerViewMainAdapter;

public class ListOfSmetasNames extends AppCompatActivity {

    private static final String TAG = "33333";

    private ListView mListViewNames;
    private ArrayList<Map<String, Object>> data;
    private Map<String,Object> m;
    private SimpleAdapter sara;
//    private Re

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_smetas_names);

        initDB();
        initBottomNavigationView();
        initListView();
        //объявляем о регистрации контекстного меню
        registerForContextMenu(mListViewNames);
    }

    private void initDB() {
        //
        database = new SmetaOpenHelper(this).getWritableDatabase();
    }

    private void initBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_smetas_list);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void initListView() {
        mListViewNames = findViewById(R.id.listViewSmetasRabota);
        //находим View, которое выводит текст Список пуст
        View empty = findViewById(android.R.id.empty);
        TextView tvEmpty = (TextView)empty;
        tvEmpty.setText(R.string.list_empty_names);
        mListViewNames.setEmptyView(empty);
        listSetOnCLickListener();
    }

    private void listSetOnCLickListener() {
        mListViewNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //находим имя файла в адаптере
                TextView tv = view.findViewById(R.id.base_text);
                String file_name = tv.getText().toString();
                //находим id по имени файла
                long file_id = FileWork.getIdFromName(database, file_name);

                Log.d(TAG, "ListOfSmetasNames - onItemClick  file_id = " + file_id +
                        "  Name = " + tv.getText());

                Intent intent = new Intent(ListOfSmetasNames.this, Smetas.class);
                intent.putExtra(P.ID_FILE, file_id);
                startActivity(intent);
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener=
           new BottomNavigationView.OnNavigationItemSelectedListener() {
               @Override
               public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                   switch (item.getItemId()){
                       case R.id.navigation_home_smetas_list:
                            finish();
                           return true;

                       case R.id.navigation_costs_smetas_list:
                           DialogFragment dialogFragment = new DialogWorkOrMatCosts();
                           dialogFragment.show(getSupportFragmentManager(), "dialogWorkOrMatCosts");
                           return true;
                   }
                   return false;
               }
           };

    @Override
    public void onResume() {
        super.onResume();
        //initRecycledView();
        updateAdapter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_list_of_smetas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Log.d(TAG, "ListOfSmetasNames onOptionsItemSelected id = " + id);
        switch (id){
            case R.id.action_add:
                Intent intent = new Intent(ListOfSmetasNames.this, SmetaNewName.class);
                startActivity(intent);
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
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        // получаем инфу о пункте списка
        final AdapterView.AdapterContextMenuInfo acmi =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int id = item.getItemId();
        Log.d(TAG, "ListOfSmetasNames onContextItemSelected id = " + id +
                " acmi.position = " + acmi.position + " acmi.id = " +acmi.id);

        //получаем имя файла из строки списка файлов
        TextView tv = acmi.targetView.findViewById(R.id.base_text);
        String file_name = tv.getText().toString();
        //находим id по имени файла
        final long file_id = FileWork.getIdFromName(database, file_name);
        Log.d(TAG, "ListOfSmetasNames onContextItemSelected file_name = " + file_name +
                " file_id_cpecific =" + file_id);

        switch (id){
            case P.SPECIFIC_ID:
                //отправляем интент с id файла
                Intent intentSpecific = new Intent(ListOfSmetasNames.this, SmetaSpecification.class);
                intentSpecific.putExtra(P.ID_FILE, file_id);
                startActivity(intentSpecific);
                return true;

            case P.CHANGE_NAME_ID:

                //отправляем интент с id файла
                Intent intent = new Intent(ListOfSmetasNames.this, SmetaNewNameChange.class);
                intent.putExtra(P.ID_FILE, file_id);
                startActivity(intent);
                return true;

            case P.DELETE_ID:
                Log.d(TAG, "ChangeTempActivity P.DELETE_CHANGETEMP");
                new AlertDialog.Builder(this)
                .setTitle(R.string.DeleteYesNo)
                .setPositiveButton(R.string.DeleteNo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNegativeButton(R.string.DeleteYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Удаляем файл из таблицы FileWork и данные из таблицы FW по file_id
                        FileWork.deleteObject(database, file_id);
                        Toast.makeText(ListOfSmetasNames.this,"Удалено ",
                                Toast.LENGTH_SHORT).show();

                        updateAdapter();
                    }
                }).show();
                return true;

            case P.CANCEL:
                return true;
        }
        return super.onContextItemSelected(item);
    }

//    //инициализация RecycledView
//    private void initRecycledView() {
//        Log.d(TAG, "ListOfSmetasNames initRecycledView");
//        //Курсор с данными файлов из которых берём имена
//        List<DataFile> recordsList = FileWork.readFilesData(database);
//
//        //используем встроенный LinearLayoutManager
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//
////        RecyclerViewMainAdapter.OnMainListClickListener onMainListClickListener =
////                new RecyclerViewMainAdapter.OnMainListClickListener() {
////                    @Override
////                    public void onMainListClick(int position) {
////                        if (position == 2){
////                            dialogNewOrCurrentFragment();
////                        }else if (position == 3){
////                            dialogNewOrCurrentFragment();
////                        }else{
////                            //todo сделать для 0 и 1
////                            dialogNewOrCurrentFragment();
////                        }
////                    }
////                };
//
//        // вызываем конструктор адаптера, передаём список
//        RecyclerViewMainAdapter mainAdapter = new RecyclerViewMainAdapter(listOfMain);
//        //устанавливаем onMainListClickListener в качестве слушателя на щелчках списка
//        mainAdapter.setOnMainListClickListener(onMainListClickListener);
//        recyclerViewMain.setLayoutManager(layoutManager);
//        recyclerViewMain.setAdapter(mainAdapter);
//    }

    public void updateAdapter() {

        //Курсор с данными файлов из которых берём имена
        List<DataFile> recordsList = FileWork.readFilesData(database);
        //Список с данными для адаптера
        data = new ArrayList<Map<String, Object>>(recordsList.size());

        if (recordsList.size()>0){
            for (DataFile file: recordsList){
                m = new HashMap<>();
                m.put(P.FILENAME_DEFAULT,file.getFileName());
                data.add(m);
            }
        }else {
            m = new HashMap<>();
            m.put(P.FILENAME_DEFAULT,getResources().getString(R.string.list_empty_tab));
            data.add(m);
        }

        String[] from = new String[]{P.FILENAME_DEFAULT};
        int[] to = new int[]{ R.id.base_text};
        sara =  new SimpleAdapter(this, data, R.layout.list_item_single, from, to);
        mListViewNames.setAdapter(sara);
    }
}
