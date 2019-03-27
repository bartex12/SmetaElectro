package ru.bartex.smetaelectro;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

public class ListOfSmetasNames extends AppCompatActivity {

    public static final String TAG = "33333";

    ListView mListViewNames;
    SmetaOpenHelper mSmetaOpenHelper;
    ArrayList<Map<String, Object>> data;
    Map<String,Object> m;
    SimpleAdapter sara;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_smetas_names);


        mSmetaOpenHelper = new SmetaOpenHelper(this);

        mListViewNames = findViewById(R.id.listViewSmetasRabota);
        mListViewNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //находим имя файла в адаптере
                TextView tv = view.findViewById(R.id.base_text);
                String file_name = tv.getText().toString();
                //находим id по имени категории
                long file_id = mSmetaOpenHelper.getIdFromFileName(file_name);

                Log.d(TAG, "ListOfSmetasNames - onItemClick  file_id = " + file_id +
                        "  Name = " + tv.getText());

                Intent intent = new Intent(ListOfSmetasNames.this, Smetas.class);
                intent.putExtra(P.ID_FILE, file_id);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_list_of_smetas);
        //fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ListOfSmetasNames.this, SmetaNewName.class);
                startActivity(intent);
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
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
        String[] file_name = mSmetaOpenHelper.getFileNames();
        //Список с данными для адаптера
        data = new ArrayList<Map<String, Object>>(file_name.length);

        for (int i = 0; i< file_name.length; i++){
            Log.d(TAG, "SmetasTab1Rabota - updateAdapter  name_file = " + file_name[i]);
            m = new HashMap<>();
            m.put(P.FILENAME_DEFAULT,file_name[i]);
            data.add(m);
        }
        String[] from = new String[]{P.FILENAME_DEFAULT};
        int[] to = new int[]{ R.id.base_text};
        sara =  new SimpleAdapter(this, data, R.layout.list_item_single, from, to);
        mListViewNames.setAdapter(sara);
    }
}
