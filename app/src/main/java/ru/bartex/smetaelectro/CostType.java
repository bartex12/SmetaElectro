package ru.bartex.smetaelectro;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class CostType extends AppCompatActivity {

    public static final String TAG = "33333";
    ListView mListView;
    SmetaOpenHelper mSmetaOpenHelper;
    ArrayList<Map<String, Object>> data;
    Map<String,Object> m;
    SimpleAdapter sara;
    long cat_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_type);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mSmetaOpenHelper = new SmetaOpenHelper(this);
        cat_id = getIntent().getLongExtra(P.ID_CATEGORY, 1);

        mListView = findViewById(R.id.listViewTypeCost);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //находим имя типа работы в адаптере
                TextView tv = view.findViewById(R.id.base_text);
                String type_name = tv.getText().toString();
                //находим id по имени типа
                long type_id = mSmetaOpenHelper.getIdFromTypeName(type_name);
                Log.d(TAG, "CostType - onItemClick  type_id = " + type_id +
                        "  Name = " + type_name);

                Intent intent = new Intent(CostType.this, CostWork.class);
                intent.putExtra(P.ID_CATEGORY, cat_id);
                intent.putExtra(P.ID_TYPE, type_id);
                startActivity(intent);
            }
        });
    }

    @Override
protected void onResume() {
    super.onResume();
    updateAdapter();
}

    public void updateAdapter() {
        //Курсор с именами типов работ
        Cursor cursor = mSmetaOpenHelper.getTypeNames(cat_id);
        //Список с данными для адаптера
        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        while (cursor.moveToNext()) {
            //смотрим значение текущей строки курсора
            String name_type = cursor.getString(cursor.getColumnIndex(TypeWork.TYPE_NAME));
            Log.d(TAG, "CostType - updateAdapter  name_type = " + name_type);
            m = new HashMap<>();
            m.put(P.ATTR_TYPE_NAME,name_type);
            data.add(m);
        }
        String[] from = new String[]{P.ATTR_TYPE_NAME};
        int[] to = new int[]{R.id.base_text};
        sara =  new SimpleAdapter(this, data, R.layout.list_item_single, from, to);
        mListView.setAdapter(sara);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home_cost:
                    // Для данного варианта в манифесте указан режим singlTask для активности MainActivity
                    Intent intentHome = new Intent(CostType.this, MainActivity.class);
                    // установка флагов- создаём новую задачу и убиваем старую вместе
                    // со всеми предыдущими переходами- работает дёргано по сравнению с манифестом
                    intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentHome);
                    return true;
                case R.id.navigation_smetas_cost:
                    // Для данного варианта в манифесте указан режим singlTask для активности ListOfSmetasNames
                    Intent intent_smetas = new Intent(CostType.this, ListOfSmetasNames.class);
                    startActivity(intent_smetas);
                    return true;
            }
            return false;
        }
    };

}
