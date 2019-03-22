package ru.bartex.smetaelectro;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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
    SmetaOpenHelper mDbHelper = new SmetaOpenHelper(this);
    long file_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mListView = findViewById(R.id.listViewMain);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){

                }else if (position == 1){

                }else if (position == 2){

                    Intent intent = new Intent(MainActivity.this, SmetaCategoryElectro.class);
                    intent.putExtra(P.ID_FILE_DEFAULT, file_id);
                    startActivity(intent);

                }else if (position == 3){

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
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_smetas:
                    Intent intent_smetas = new Intent(MainActivity.this, ListOfSmetasNames.class);
                    startActivity(intent_smetas);
                    return true;
                case R.id.navigation_costs:
                    Intent intent_costs = new Intent(MainActivity.this, CostCategory.class);
                    startActivity(intent_costs);
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


}
