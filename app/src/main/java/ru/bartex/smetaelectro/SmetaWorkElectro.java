package ru.bartex.smetaelectro;

import android.app.AlertDialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;

public class SmetaWorkElectro extends AppCompatActivity
        implements DialogSaveWorkName.WorkCategoryTypeWorkNameListener{

    public static final String TAG = "33333";
    ListView mListView;
    SmetaOpenHelper mSmetaOpenHelper;
    long file_id;
    long cat_id;
    long type_id;
    AdapterOfWork mAdapterOfWork;
    int positionCategory;
    int positionType;

    @Override
    public void workCategoryTypeWorkNameTransmit(String workName, String typeName, String catName) {
        Log.d(TAG, "SmetaWorkElectro - workCategoryTypeWorkNameTransmit  workName = " + workName +
                "  typeName = " + typeName + "  catName = " + catName);
        //определяем id типа по его имени
        long work_type_Id = mSmetaOpenHelper.getIdFromTypeName(typeName);
        //вставляем имя  работы в таблицу Work
        long newWorkNameId = mSmetaOpenHelper.insertWorkName(workName, work_type_Id);
        Log.d(TAG, "SmetaWorkElectro-workCategoryTypeWorkNameTransmit newWorkNameId = " + newWorkNameId);
        //обновляем адаптер
        mAdapterOfWork.updateAdapter();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smeta_work_electro);

        mSmetaOpenHelper = new SmetaOpenHelper(this);
        file_id = getIntent().getLongExtra(P.ID_FILE_DEFAULT, 1);
        cat_id = getIntent().getLongExtra(P.ID_CATEGORY, 1);
        type_id = getIntent().getLongExtra(P.ID_TYPE, 1);
        positionCategory = getIntent().getIntExtra(P.POSITION_CATEGORY, 1);
        positionType = getIntent().getIntExtra(P.POSITION_TYPE, 1);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mListView = findViewById(R.id.listViewWorkElectro);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //находим имя работы в адаптере
                TextView tv = view.findViewById(R.id.base_text_two);
                String work_name = tv.getText().toString();

                //находим id по имени работы
                final long work_id = mSmetaOpenHelper.getIdFromWorkName(work_name);
                Log.d(TAG, "SmetaWorkElectro - onItemClick  work_id = " + work_id +
                        "  Name = " + work_name);
                final boolean isWork = mSmetaOpenHelper.isWork(file_id, work_id);
                Log.d(TAG, "SmetaWorkElectro - onItemClick  isWork = " + isWork);

                Intent intent = new Intent(SmetaWorkElectro.this, SmetaDetail.class);
                intent.putExtra(P.ID_FILE_DEFAULT, file_id);
                intent.putExtra(P.ID_CATEGORY, cat_id);
                intent.putExtra(P.ID_TYPE, type_id);
                intent.putExtra(P.ID_WORK, work_id);
                intent.putExtra(P.IS_WORK, isWork);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        //конструктор класса адаптера списка работ
        mAdapterOfWork = new AdapterOfWork(this, file_id, type_id, mListView);
        //обновляем данные
        mAdapterOfWork.updateAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_work_of_work, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                openSaveWorkDialogFragment(cat_id, positionCategory, positionType);
                return true;

            case R.id.action_settings:

                return true;
        }


        return super.onOptionsItemSelected(item);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home_smetas_work:
                    // Для данного варианта в манифесте указан режим singlTask для активности MainActivity
                    Intent intentHome = new Intent(SmetaWorkElectro.this, MainActivity.class);
                    // установка флагов- создаём новую задачу и убиваем старую вместе
                    // со всеми предыдущими переходами Для первого экрана это подходит
                    //Но  - работает дёргано по сравнению с манифестом
                    //intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                      //      Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentHome);
                    return true;

                case R.id.navigation_smetas_smetas_work:
                    // Для данного варианта в манифесте указан режим singlTask для активности Smetas
                    Intent intent = new Intent(SmetaWorkElectro.this, Smetas.class);
                    intent.putExtra(P.ID_FILE, file_id);
                    startActivity(intent);
                    return true;

                case R.id.navigation_costs_smetas_work:
                    Intent intent_costs = new Intent(SmetaWorkElectro.this, CostCategory.class);
                    startActivity(intent_costs);
                    return true;
            }
            return false;
        }
    };

    private void openSaveWorkDialogFragment(long cat_id, int positionCategory, int  positionType){
        DialogFragment dialogFragment = DialogSaveWorkName.newInstance(cat_id, positionCategory, positionType);
        dialogFragment.show(getSupportFragmentManager(), "SaveWorkName");
    }


}
