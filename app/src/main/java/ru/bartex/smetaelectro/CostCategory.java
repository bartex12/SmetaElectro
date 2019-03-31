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

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.CategoryWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;

public class CostCategory extends AppCompatActivity {

    public static final String TAG = "33333";
    ListView mListView;
    SmetaOpenHelper mSmetaOpenHelper;
    ArrayList<Map<String, Object>> data;
    Map<String,Object> m;
    SimpleAdapter sara;
    //long file_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costs_category);

        mSmetaOpenHelper = new SmetaOpenHelper(this);
       // file_id = getIntent().getLongExtra(P.ID_FILE_DEFAULT, 1);

        mListView = findViewById(R.id.listViewCategoryCost);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //находим имя категории в адаптере
                TextView tv = view.findViewById(R.id.base_text);
                String cat_name = tv.getText().toString();
                //находим id по имени категории
                long cat_id = mSmetaOpenHelper.getIdFromCategoryName(cat_name);
                Log.d(TAG, "CostCategory - onItemClick  cat_id = " + cat_id +
                        "  Name = " + tv.getText());

                Intent intent = new Intent(CostCategory.this, CostType.class);
                intent.putExtra(P.ID_CATEGORY, cat_id); //передаём дальше id категории
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
        //Курсор с именами категорий
        Cursor cursor = mSmetaOpenHelper.getCategoryNames();
        //Список с данными для адаптера
        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        while (cursor.moveToNext()) {
            //смотрим значение текущей строки курсора
            String name_cat = cursor.getString(cursor.getColumnIndex(CategoryWork.CATEGORY_NAME));

            Log.d(TAG, "SmetaCategory - updateAdapter  name_cat = " + name_cat);
            m = new HashMap<>();
            m.put(P.ATTR_CATEGORY_NAME,name_cat);
            data.add(m);
        }
        String[] from = new String[]{P.ATTR_CATEGORY_NAME};
        int[] to = new int[]{R.id.base_text};
        sara =  new SimpleAdapter(this, data, R.layout.list_item_single, from, to);
        mListView.setAdapter(sara);
    }

}
