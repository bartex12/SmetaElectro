package ru.bartex.smetaelectro;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
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

public class SmetaCategoryElectro extends AppCompatActivity {

    public static final String TAG = "33333";
    ListView mListView;
    SmetaOpenHelper mSmetaOpenHelper;
    ArrayList<Map<String, Object>> data;
    Map<String,Object> m;
    SimpleAdapter sara;
    long file_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smeta_category);

        mSmetaOpenHelper = new SmetaOpenHelper(this);
        file_id = getIntent().getLongExtra(P.ID_FILE_DEFAULT, 1);

        mListView = findViewById(R.id.listViewCategory);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //находим имя категории в адаптере
                    TextView tv = view.findViewById(R.id.base_text_category);
                    String cat_name = tv.getText().toString();
                    //находим id по имени категории
                    long cat_id = mSmetaOpenHelper.getIdFromCategoryName(cat_name);
                    //отмечаем в базе, что был заход в категорию для файла с file_id
                    mSmetaOpenHelper.updateCategoryMark(1, cat_id);
                    Log.d(TAG, "SmetaCategory - onItemClick  cat_id = " + cat_id +
                         "  Name = " + tv.getText());

                    //обновляем отметку
                    updateAdapter();

                    Intent intent = new Intent(SmetaCategoryElectro.this, SmetaTypeElectro.class);
                    intent.putExtra(P.ID_FILE_DEFAULT, file_id); //передаём дальше id файла
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
        //такой (SimpleAdapter) адаптер, чтобы чекбокс работал -  надо попробовать по-другому -
        // с картинкой или расширить SimpleCursorAdapter
        //Список с данными для адаптера
        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        while (cursor.moveToNext()) {
            //смотрим значение текущей строки курсора
            String name_cat = cursor.getString(cursor.getColumnIndex(CategoryWork.COLUMN_CATEGORY_NAME));
            int mark = cursor.getInt(cursor.getColumnIndex(CategoryWork.COLUMN_CATEGORY_MARK));
            boolean chek_mark = false;
            if (mark == 0){
                chek_mark= false;
            }else {
                chek_mark = true;
            }
            Log.d(TAG, "SmetaCategory - onResume  name_cat = " + name_cat +
                    "  mark = " + mark );
            m = new HashMap<>();
            m.put(P.ATTR_CATEGORY_MARK,chek_mark);
            m.put(P.ATTR_CATEGORY_NAME,name_cat);
            data.add(m);
        }
        String[] from = new String[]{P.ATTR_CATEGORY_MARK,P.ATTR_CATEGORY_NAME};
        int[] to = new int[]{R.id.checkBoxCategory, R.id.base_text_category};
        sara =  new SimpleAdapter(this, data, R.layout.list_itrm_category, from, to);
        mListView.setAdapter(sara);
    }
}
