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
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.bartex.smetaelectro.data.DataFile;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.files.FileWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.TableControllerSmeta;

public class ListOfSmetasNames extends AppCompatActivity {

    public static final String TAG = "33333";

    ListView mListViewNames;
    TableControllerSmeta tableControllerSmeta;
    ArrayList<Map<String, Object>> data;
    Map<String,Object> m;
    SimpleAdapter sara;
    Button newSmeta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_smetas_names);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_smetas_list);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        tableControllerSmeta = new TableControllerSmeta(this);

        mListViewNames = findViewById(R.id.listViewSmetasRabota);
        //находим View, которое выводит текст Список пуст
        View empty = findViewById(android.R.id.empty);
        TextView tvEmpty = (TextView)empty;
        tvEmpty.setText(R.string.list_empty_names);
        mListViewNames.setEmptyView(empty);
        mListViewNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //находим имя файла в адаптере
                TextView tv = view.findViewById(R.id.base_text);
                String file_name = tv.getText().toString();
                //находим id по имени файла
                long file_id = tableControllerSmeta.getIdFromName(file_name, FileWork.TABLE_NAME);

                Log.d(TAG, "ListOfSmetasNames - onItemClick  file_id = " + file_id +
                        "  Name = " + tv.getText());

                Intent intent = new Intent(ListOfSmetasNames.this, Smetas.class);
                intent.putExtra(P.ID_FILE, file_id);
                startActivity(intent);
            }
        });

        //объявляем о регистрации контекстного меню
        registerForContextMenu(mListViewNames);

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
        updateAdapter();
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
        final long file_id = tableControllerSmeta.getIdFromName(file_name, FileWork.TABLE_NAME);
        Log.d(TAG, "ListOfSmetasNames onContextItemSelected file_name = " + file_name +
                " file_id_cpecific =" + file_id);

        switch (id){
            case P.SPECIFIC_ID:
                //отправляем интент с id файла
                Intent intentSpecific = new Intent(ListOfSmetasNames.this, SpecificSmeta.class);
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
                        tableControllerSmeta.deleteObject(file_id, FileWork.TABLE_NAME);
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

    public void updateAdapter() {

        //Курсор с данными файловБ из которых берём имена
        List<DataFile> recordsList = new TableControllerSmeta(this).readFilesData();
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
