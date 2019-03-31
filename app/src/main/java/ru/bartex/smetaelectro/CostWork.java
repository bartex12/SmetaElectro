package ru.bartex.smetaelectro;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.Work;

public class CostWork extends AppCompatActivity {

    public static final String TAG = "33333";
    ListView mListView;
    SmetaOpenHelper mSmetaOpenHelper;
    ArrayList<Map<String, Object>> data;
    Map<String,Object> m;
    SimpleAdapter sara;
    long cat_id;
    long type_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_work);

        mSmetaOpenHelper = new SmetaOpenHelper(this);
        cat_id = getIntent().getLongExtra(P.ID_CATEGORY, 1);
        type_id = getIntent().getLongExtra(P.ID_TYPE, 1);

        mListView = findViewById(R.id.listViewWorkCost);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //находим имя типа работы в адаптере
                TextView tv = view.findViewById(R.id.base_text);
                String type_name = tv.getText().toString();

                //находим id по имени работы
                long work_id = mSmetaOpenHelper.getIdFromWorkName(type_name);

                Intent intent = new Intent(CostWork.this, CostDetail.class);
                intent.putExtra(P.ID_CATEGORY, cat_id);
                intent.putExtra(P.ID_TYPE, type_id);
                intent.putExtra(P.ID_WORK, work_id);
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
        //Курсор с именами работ
        Cursor cursor = mSmetaOpenHelper.getWorkNames(type_id);
        //Список с данными для адаптера
        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        while (cursor.moveToNext()) {
            //смотрим значение текущей строки курсора
            String name_work = cursor.getString(cursor.getColumnIndex(Work.WORK_NAME));
            Log.d(TAG, "CostWork - updateAdapter  name_work = " + name_work);
            m = new HashMap<>();
            m.put(P.ATTR_WORK_NAME,name_work);
            data.add(m);
        }
        String[] from = new String[]{P.ATTR_WORK_NAME};
        int[] to = new int[]{R.id.base_text};
        sara =  new SimpleAdapter(this, data, R.layout.list_item_single, from, to);
        mListView.setAdapter(sara);
    }
}
