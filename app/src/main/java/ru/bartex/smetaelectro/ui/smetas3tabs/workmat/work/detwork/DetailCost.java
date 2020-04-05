package ru.bartex.smetaelectro.ui.smetas3tabs.workmat.work.detwork;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.database.SmetaOpenHelper;
import ru.bartex.smetaelectro.database.work.CostWork;
import ru.bartex.smetaelectro.database.work.Unit;
import ru.bartex.smetaelectro.database.work.Work;


public class DetailCost extends AppCompatActivity {

    public static final String TAG = "33333";

    TextView mTextViewWorkName;
    EditText mTextViewCost;
    Button mButtonSave;
    Button mButtonCancel;

    long cat_id;
    long type_id;
    long work_id;
    int requestCode;

    float cost; //цена работы
    String unit; //единицы измерения

    public static final String REQUEST_CODE = "request_codeCostDetail";
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_detail);

        initDB();

        cat_id = getIntent().getLongExtra(P.ID_CATEGORY, 1);
        type_id = getIntent().getLongExtra(P.ID_TYPE, 1);
        work_id = getIntent().getLongExtra(P.ID_WORK, 1);

        Log.d(TAG, "## // ## DetailCost - onCreate  cat_id = " +
                 cat_id + "  type_id = " + type_id + "  work_id = " + work_id);

        //выводим название работы
        mTextViewWorkName = findViewById(R.id.tv_cost_workName);
        String workName = Work.getNameFromId(database, work_id);
        mTextViewWorkName.setText(workName);

        //выводим таблицу CostWork
        //mSmetaOpenHelper.displayTableCost();

        //выводим стоимость работы
        mTextViewCost = findViewById(R.id.etCost);
        cost = CostWork.getCostById(database, work_id);
        Log.d(TAG, "## // ## DetailCost - onCreate  cost = " + cost);
        if (cost == -1.0f){
            //вставляем строку с левыми параметрами, чтобы ее потом изменить в updateWorkCost
            // при нажатии кнопки Сохранить
            CostWork.insertZero(database, work_id);
            Log.d(TAG, "## // ## DetailCost - onCreate insertZero");
        }
        mTextViewCost.setText(Float.toString(cost));
        mTextViewCost.requestFocus();
        mTextViewCost.selectAll();

        //получаем массив единиц измерения из таблицы Unit
        String[] unins = Unit.getArrayUnits(database);

        //создаём адаптер для спиннера со встроенными android.R.layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,unins);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner spinner = findViewById(R.id.spinnerUnits);
        spinner.setAdapter(adapter);
        //spinner.setPrompt("Спиннер");
        if (cost == -1.0f){
            spinner.setSelection(0);
        }else {
            String unitName = CostWork.getNameFromId(database, work_id);
            long unitId = Unit.getIdFromName(database, unitName);
            Log.d(TAG, "DetailCost- Spinner -unitName = " + unitName + "  unitId = " + unitId);
            //!!! - может быть опасно, так как id и позиция не одно и то же (позиция с нуля а id с 1)
            spinner.setSelection((int)unitId - 1);
        }

        //устанавливаем слушатель щелсчков на спиннере
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //устанавливаем выделение
                spinner.setSelection(i);
                //меняем цвет и размер шрифта выделения (не выпадающего списка)
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLUE);
                ((TextView) adapterView.getChildAt(0)).setTextSize(20);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mButtonSave = findViewById(R.id.button_cost_save);
        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String costOfWork =  mTextViewCost.getText().toString();
                if ((costOfWork.equals(""))||(costOfWork.equals("."))){
                    costOfWork = "0";
                }
                Log.d(TAG, "DetailCost-mButtonSave.setOnClickListener costOfWork = " + costOfWork);
                //проверка на 0, чтобы не было нулевых строк в смете
                if (Float.parseFloat(costOfWork)==0) {
                    //Snackbar заслонён клавиатурой, поэтому в манифесте пишем
                    //android:windowSoftInputMode="stateVisible|adjustResize"
                    Snackbar.make(getCurrentFocus(), "Введите число, не равное нулю",
                            Snackbar.LENGTH_SHORT).setAction("Action", null).show();

                }else {
                    cost =  Float.parseFloat(costOfWork);
                    String unit = spinner.getSelectedItem().toString();
                    long unit_id = Unit.getIdFromName(database, unit);
                    Log.d(TAG, "DetailCost-mButtonSave.setOnClickListener work_id = " +
                            work_id + " cost = " + cost + " unit = " + unit + " unit_id = " + unit_id);

                    //обновляем стоимость работы с единицами измерения
                    CostWork.updateCost(database, work_id, cost, unit_id);

                    Bundle extras = getIntent().getExtras();
                    if(extras != null) {
                        requestCode = extras.getInt(REQUEST_CODE);
                        //если пришло из изменить запись
                        if (requestCode == 111) {
                            Log.d(TAG, "DetailCost-mButtonSave requestCode =  " + requestCode);
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                        }
                    }
                    finish();
                }
            }
        });

        mButtonCancel = findViewById(R.id.button_cost_cancel);
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initDB() {
        //
        database = new SmetaOpenHelper(this).getWritableDatabase();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "DetailCost-onDestroy..." );
        String costOfWork =  mTextViewCost.getText().toString();
        if ((costOfWork.equals(""))||(costOfWork.equals("."))){
            costOfWork = "0";
        }
        //если Cost = 0, то стираем строку в случае отмены кнопкой Cancel или Назад
        //это надо будет изменить - не писать строку, чтобы ее потом изменять,
        // а вставлять тогда, когда это будет нужно вместе с единицами измерения ,
        // для чего переделать макет DetailCost
        //---макет переделан а вставка не сделана пока---
        if (Float.parseFloat(costOfWork)==0){
            CostWork.deleteObject(database, work_id);
        }

    }
}
