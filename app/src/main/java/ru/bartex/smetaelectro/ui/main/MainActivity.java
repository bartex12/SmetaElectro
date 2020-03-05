package ru.bartex.smetaelectro.ui.main;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ru.bartex.smetaelectro.DialogNewOrCurrentFragment;
import ru.bartex.smetaelectro.DialogWorkOrMatCosts;
import ru.bartex.smetaelectro.ListOfSmetasNames;
import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.SmetaOpenHelper;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "33333";
    ListView mListView;
    String[] stringListMain;
    ArrayList<Map<String, Object>> data;
    SimpleAdapter sara;
    Map<String, Object> m;

    private List<String> listOfMain;
    private RecyclerViewMainAdapter mainAdapter;
    private RecyclerView recyclerViewMain; //RecyclerView для списка в MainActivity

    final String ATTR_PICTURE = "PICTURE";
    final String ATTR_BASE_TEXT = "BASE_TEXT";

    SmetaOpenHelper mDbHelper;
    //private SQLiteDatabase database;
    long file_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initDB();

        //создаём базу данных, если ее не было
        mDbHelper = new SmetaOpenHelper(this);

        recyclerViewMain = findViewById(R.id.recyclerViewMain);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_main);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

//        mListView = findViewById(R.id.listViewMain);
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0){
//
//                }else if (position == 1){
//
//                }else if (position == 2){
//                    dialogNewOrCurrentFragment();
//
//                }else if (position == 3){
//                    dialogNewOrCurrentFragment();
//                }
//            }
//        });

        //если в базе нет записей, пишем файл с именем "Новая смета" в таблицу FileWork и получаем его id
        //если записи есть, получаем id по имени "Новая смета" (скорее всего, 1)
        //если id = -1, значит ошибка
        file_id = mDbHelper.createDefaultFileIfNeed();
        Log.d(TAG, "Новая смета - file_id = " + file_id);
        //Log.d(TAG, "db  - db.getPath() = " + database.getPath());
        //database.close();
    }

//    private void initDB() {
//        //
//        database = new SmetaOpenHelper(this).getWritableDatabase();
//    }

    @Override
    protected void onResume() {
        super.onResume();
        //updateAdapter();
        initRecycledView();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_smetas_main:
                    Intent intent_smetas = new Intent(MainActivity.this, ListOfSmetasNames.class);
                    startActivity(intent_smetas);
                    return true;
                case R.id.navigation_costs_main:
                    DialogFragment dialogFragment = new DialogWorkOrMatCosts();
                    dialogFragment.show(getSupportFragmentManager(), "dialogWorkOrMatCosts");
                    return true;
            }
            return false;
        }
    };

    //обновляем адаптер начального экрана с картинкой и названием пункта меню
    public void updateAdapter(){
        // получаем массив из ресурсов
        stringListMain = getResources().getStringArray(R.array.MenuMain);
        //делаем список из массива - Arrays.asList -это неизменяемый список
        listOfMain = new ArrayList<>(Arrays.asList(stringListMain));

//        int arraySize = stringListMain.length;
//        data = new ArrayList<Map<String, Object>>(arraySize);
//
//        for (int i = 0; i<arraySize; i++){
//            m = new HashMap<>();
//            m.put(ATTR_PICTURE, R.drawable.p1);
//            m.put(ATTR_BASE_TEXT, stringListMain[i]);
//            data.add(m);
//        }
//
//        String[] from = {ATTR_PICTURE, ATTR_BASE_TEXT};
//        int[] to = {R.id.picture, R.id.base_text};
//        sara = new SimpleAdapter(this, data, R.layout.list_item_main,from, to);
        mListView.setAdapter(sara);
    }

    //инициализация RecycledView
    private void initRecycledView() {
        Log.d(TAG, "MainActivity initRecycledView");
        // получаем массив из ресурсов
        stringListMain = getResources().getStringArray(R.array.MenuMain);
        //делаем список из массива - Arrays.asList -это неизменяемый список
        listOfMain = new ArrayList<>(Arrays.asList(stringListMain));

        //используем встроенный LinearLayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        RecyclerViewMainAdapter.OnMainListClickListener onMainListClickListener =
                new RecyclerViewMainAdapter.OnMainListClickListener() {
                    @Override
                    public void onMainListClick(int position) {
                        if (position == 0){

                        }else if (position == 1){

                        }else if (position == 2){
                            dialogNewOrCurrentFragment();

                        }else if (position == 3){
                            dialogNewOrCurrentFragment();
                        }
                    }
                };

        // вызываем конструктор адаптера, передаём список
        mainAdapter = new RecyclerViewMainAdapter(listOfMain);
        //устанавливаем onMainListClickListener в качестве слушателя на щелчках списка
        mainAdapter.setOnMainListClickListener(onMainListClickListener);
        recyclerViewMain.setLayoutManager(layoutManager);
        recyclerViewMain.setAdapter(mainAdapter);
    }

    //диалог сохранения, оформленный как класс с указанием имени файла
    private void dialogNewOrCurrentFragment() {
        DialogFragment dialogFragment = new DialogNewOrCurrentFragment();
        dialogFragment.show(getSupportFragmentManager(),"NewOrCurrentSmeta");
    }
}
