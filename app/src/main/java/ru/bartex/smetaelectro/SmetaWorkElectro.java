package ru.bartex.smetaelectro;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;

public class SmetaWorkElectro extends AppCompatActivity {

    public static final String TAG = "33333";
    ListView mListView;
    SmetaOpenHelper mSmetaOpenHelper;
    long file_id;
    long cat_id;
    long type_id;
    AdapterOfWork mAdapterOfWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smeta_work_electro);

        mSmetaOpenHelper = new SmetaOpenHelper(this);
        file_id = getIntent().getLongExtra(P.ID_FILE_DEFAULT, 1);
        cat_id = getIntent().getLongExtra(P.ID_CATEGORY, 1);
        type_id = getIntent().getLongExtra(P.ID_TYPE, 1);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mListView = findViewById(R.id.listViewWorkElectro);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //находим имя типа работы в адаптере
                TextView tv = view.findViewById(R.id.base_text_two);
                String type_name = tv.getText().toString();

                //находим id по имени работы
                long work_id = mSmetaOpenHelper.getIdFromWorkName(type_name);

                //отмечаем в базе, что был заход в работу для файла с file_id, cat_id и type_id
                mSmetaOpenHelper.updateWorkMark(1, work_id);
                Log.d(TAG, "SmetaWorkDemontag - onItemClick  work_id = " + work_id +
                        "  Name = " + type_name);
                //обновляем отметку
                mAdapterOfWork.updateAdapter();

                Intent intent = new Intent(SmetaWorkElectro.this, SmetaDetail.class);
                intent.putExtra(P.ID_FILE_DEFAULT, file_id);
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
        //конструктор класса адаптера списка работ
        mAdapterOfWork = new AdapterOfWork(getApplicationContext(), type_id, mListView);
        //обновляем данные
        mAdapterOfWork.updateAdapter();
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
                    //mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_costs:
                    //mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };
}
