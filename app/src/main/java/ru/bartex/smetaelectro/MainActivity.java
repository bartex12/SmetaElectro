package ru.bartex.smetaelectro;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.TableControllerSmeta;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "33333";
    ListView mListView;
    String[] stringListMain;
    ArrayList<Map<String, Object>> data;
    SimpleAdapter sara;
    Map<String, Object> m;

    final String ATTR_PICTURE = "PICTURE";
    final String ATTR_BASE_TEXT = "BASE_TEXT";

    //создаём базу данных, если ее не было
    SmetaOpenHelper mDbHelper;
    TableControllerSmeta tableControllerSmeta;
    long file_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHelper = new SmetaOpenHelper(this);
        tableControllerSmeta = new TableControllerSmeta(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_main);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mListView = findViewById(R.id.listViewMain);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){

                }else if (position == 1){

                }else if (position == 2){
                    dialogNewOrCurrentFragment();

                }else if (position == 3){
                    dialogNewOrCurrentFragment();
                }
            }
        });

        //если в базе нет записей, пишем файл с именем "Новая смета" в таблицу FileWork и получаем его id
        //если записи есть, получаем id по имени "Новая смета" (скорее всего, 1)
        //если id = -1, значит ошибка
        file_id = mDbHelper.createDefaultFileIfNeed();
        Log.d(TAG, "Новая смета - file_id = " + file_id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAdapter();

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
        stringListMain = getResources().getStringArray(R.array.MenuMain);
        int arraySize = stringListMain.length;
        data = new ArrayList<Map<String, Object>>(arraySize);

        for (int i = 0; i<arraySize; i++){
            m = new HashMap<>();
            m.put(ATTR_PICTURE, R.drawable.p1);
            m.put(ATTR_BASE_TEXT, stringListMain[i]);
            data.add(m);
        }

        String[] from = {ATTR_PICTURE, ATTR_BASE_TEXT};
        int[] to = {R.id.picture, R.id.base_text};
        sara = new SimpleAdapter(this, data, R.layout.list_item_main,from, to);
        mListView.setAdapter(sara);
    }

    //диалог сохранения, оформленный как класс с указанием имени файла
    private void dialogNewOrCurrentFragment() {
        DialogFragment dialogFragment = new DialogNewOrCurrentFragment();
        dialogFragment.show(getSupportFragmentManager(),"NewOrCurrentSmeta");
    }
}
