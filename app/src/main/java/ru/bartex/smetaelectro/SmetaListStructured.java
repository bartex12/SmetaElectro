package ru.bartex.smetaelectro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;

public class SmetaListStructured extends AppCompatActivity {

    public static final String TAG = "33333";
    ListView mListViewNames;
    SmetaOpenHelper mSmetaOpenHelper;
    ArrayList<Map<String, Object>> data;
    Map<String,Object> m;
    SimpleAdapter sara;
    Button newSmeta;
    long file_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smeta_list_structured);

        mSmetaOpenHelper = new SmetaOpenHelper(this);
        file_id = getIntent().getExtras().getLong(P.ID_FILE);

        mListViewNames = findViewById(R.id.listViewSmetasRabota);
        mListViewNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        updateAdapter();
    }

    public void updateAdapter() {
        //Курсор с именами файлов
        ArrayList<String> work_type_name = mSmetaOpenHelper.getNameOfTypesAndWorkStructured(file_id);
        //Список с данными для адаптера
        data = new ArrayList<Map<String, Object>>(work_type_name.size());

        for (int i = 0; i< work_type_name.size(); i++){
            Log.d(TAG, "SmetaListStructured - updateAdapter  name_file = " + work_type_name.get(i));
            m = new HashMap<>();
            m.put(P.TYPE_WORK_NAME,work_type_name.get(i));
            data.add(m);
        }
        String[] from = new String[]{P.TYPE_WORK_NAME};
        int[] to = new int[]{ R.id.base_text};
        sara =  new SimpleAdapter(this, data, R.layout.list_item_single_small, from, to);
        mListViewNames.setAdapter(sara);
    }

}
