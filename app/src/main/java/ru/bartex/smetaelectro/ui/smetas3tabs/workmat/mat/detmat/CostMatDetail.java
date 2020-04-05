package ru.bartex.smetaelectro.ui.smetas3tabs.workmat.mat.detmat;

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
import ru.bartex.smetaelectro.database.mat.CostMat;
import ru.bartex.smetaelectro.database.mat.Mat;
import ru.bartex.smetaelectro.database.mat.UnitMat;


public class CostMatDetail extends AppCompatActivity {
    public static final String TAG = "33333";

    TextView mTextViewMatName;
    EditText mTextViewCostMat;
    Button mButtonSave;
    Button mButtonCancel;

    long cat_id;
    long type_id;
    long mat_id;
    int requestCode;
    float cost; //цена работы

    public static final String REQUEST_CODE = "request_codeCostMatDetail";
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_detail);

        initDB();

        cat_id = getIntent().getLongExtra(P.ID_CATEGORY_MAT, 1);
        type_id = getIntent().getLongExtra(P.ID_TYPE_MAT, 1);
        mat_id = getIntent().getLongExtra(P.ID_MAT, 1);

        Log.d(TAG, "CostMatDetail - onCreate  cat_id = " +
                cat_id + "  type_id = " + type_id + "  mat_id = " + mat_id);

        //выводим название материала
        mTextViewMatName = findViewById(R.id.tv_cost_workName);
        String matName = Mat.getNameFromId(database, mat_id);
        mTextViewMatName.setText(matName);

        //выводим таблицу CostWork
        //mSmetaOpenHelper.displayTableCost();

        //выводим стоимость материала
        mTextViewCostMat = findViewById(R.id.etCost);
        cost = CostMat.getCostById(database, mat_id);
        if (cost == -1.0f){
            //вставляем строку с левыми параметрами, чтобы ее потом изменить в updateMatCost
            // при нажатии кнопки Сохранить
            CostMat.insertZero(database, mat_id);
        }
        mTextViewCostMat.setText(Float.toString(cost));
        mTextViewCostMat.requestFocus();
        mTextViewCostMat.selectAll();

        //получаем массив единиц измерения из таблицы Unit
        String[] uninsMat = UnitMat.getArrayUnits(database);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,uninsMat);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner spinner = findViewById(R.id.spinnerUnits);
        spinner.setAdapter(adapter);
        //spinner.setPrompt("Спиннер");
        if (cost == -1.0f){
            spinner.setSelection(0);
        }else {
            String unitMatName = UnitMat.getUnitMat(database, mat_id);
            long unitId = UnitMat.getIdFromName(database, unitMatName);
            Log.d(TAG, "CostMatDetail- Spinner -unitMatName = " + unitMatName + "  unitId = " + unitId);
            //!!! - может быть опасно, так как id и позиция не одно и то же (позиция с нуля а id с 1)
            spinner.setSelection((int)unitId - 1);
        }

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

                String costOfMat =  mTextViewCostMat.getText().toString();
                if ((costOfMat.equals(""))||(costOfMat.equals("."))){
                    costOfMat = "0";
                }
                Log.d(TAG, "CostMatDetail-mButtonSave.setOnClickListener costOfMat = " + costOfMat);
                //проверка на 0, чтобы не было нулевых строк в смете
                if (Float.parseFloat(costOfMat)==0) {
                    //Snackbar заслонён клавиатурой, поэтому в манифесте пишем
                    //android:windowSoftInputMode="stateVisible|adjustResize"
                    Snackbar.make(getCurrentFocus(), "Введите число, не равное нулю",
                            Snackbar.LENGTH_SHORT).setAction("Action", null).show();

                }else {
                    cost =  Float.parseFloat(costOfMat);
                    String unit = spinner.getSelectedItem().toString();
                    long unit_mat_id = UnitMat.getIdFromName(database, unit);
                    Log.d(TAG, "CostMatDetail-mButtonSave.setOnClickListener mat_id = " +
                            mat_id + " cost = " + cost + " unit = " + unit + " unit_mat_id = " + unit_mat_id);

                    Bundle extras = getIntent().getExtras();

                        requestCode = extras.getInt(REQUEST_CODE);
                        //если пришло из DetailSmetaMatLine
                        if (requestCode == 222) {
                            //добавляем стоимость cost работы с mat_id  с единицами измерения unit_mat_id
                            //long costId = mSmetaOpenHelper.insertCostMat(mat_id,cost,unit_mat_id);
                            CostMat.updateCost(database, mat_id, cost, unit_mat_id);
                            Intent intent = new Intent();
                            intent.putExtra(P.ID_MAT,mat_id);
                            setResult(RESULT_OK, intent);
                            Log.d(TAG, "CostMatDetail-mButtonSave requestCode =  " + requestCode +
                                    "  RESULT_OK = " + RESULT_OK );
                        }else {
                            CostMat.updateCost(database, mat_id, cost, unit_mat_id);
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
        Log.d(TAG, "CostMatDetail-onDestroy..." );
        String costOfMat =  mTextViewCostMat.getText().toString();
        if ((costOfMat.equals(""))||(costOfMat.equals("."))){
            costOfMat = "0";
        }
        //если Cost = 0, то стираем строку в случае отмены кнопкой Cancel или Назад
        //это надо будет изменить - не писать строку, чтобы ее потом изменять,
        // а вставлять тогда, когда это будет нужно вместе с единицами измерения ,
        // для чего переделать макет DetailCost
        //---макет переделан а вставка не сделана пока---
        if (Float.parseFloat(costOfMat)==0){
            CostMat.deleteObject(database, mat_id);
        }
    }
}
