package ru.bartex.smetaelectro;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.TableControllerSmeta;

public class CostMatDetail extends AppCompatActivity {
    public static final String TAG = "33333";

    TextView mTextViewMatName;
    EditText mTextViewCostMat;
    Button mButtonSave;
    Button mButtonCancel;
    SmetaOpenHelper mSmetaOpenHelper;
    TableControllerSmeta tableControllerSmeta;
    long cat_id;
    long type_id;
    long mat_id;
    int requestCode;
    float cost; //цена работы

    public static final String REQUEST_CODE = "request_codeCostMatDetail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_detail);

        mSmetaOpenHelper = new SmetaOpenHelper(this);
        cat_id = getIntent().getLongExtra(P.ID_CATEGORY_MAT, 1);
        type_id = getIntent().getLongExtra(P.ID_TYPE_MAT, 1);
        mat_id = getIntent().getLongExtra(P.ID_MAT, 1);

        Log.d(TAG, "CostMatDetail - onCreate  cat_id = " +
                cat_id + "  type_id = " + type_id + "  mat_id = " + mat_id);

        //выводим название материала
        mTextViewMatName = findViewById(R.id.tv_cost_workName);
        String matName = mSmetaOpenHelper.getMatNameById(mat_id);
        mTextViewMatName.setText(matName);

        //выводим таблицу CostWork
        //mSmetaOpenHelper.displayTableCost();

        //выводим стоимость материала
        mTextViewCostMat = findViewById(R.id.etCost);
        cost = mSmetaOpenHelper.getCostMatById(mat_id);
        if (cost == 0){
            //вставляем строку с левыми параметрами, чтобы ее потом изменить в updateMatCost
            // при нажатии кнопки Сохранить
            mSmetaOpenHelper.insertCostMatZero(mat_id);
        }
        mTextViewCostMat.setText(Float.toString(cost));
        mTextViewCostMat.requestFocus();
        mTextViewCostMat.selectAll();

        //получаем массив единиц измерения из таблицы Unit
        String[] uninsMat = mSmetaOpenHelper.getArrayUnitsMatNames();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,uninsMat);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner spinner = (Spinner)findViewById(R.id.spinnerUnits);
        spinner.setAdapter(adapter);
        //spinner.setPrompt("Спиннер");
        if (cost == 0){
            spinner.setSelection(0);
        }else {
            String unitMatName = mSmetaOpenHelper.getCostUnitMatById(mat_id);
            long unitId = mSmetaOpenHelper.getIdFromUnitMatName(unitMatName);
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
                    long unit_mat_id = mSmetaOpenHelper.getIdFromUnitMatName(unit);
                    Log.d(TAG, "CostMatDetail-mButtonSave.setOnClickListener mat_id = " +
                            mat_id + " cost = " + cost + " unit = " + unit + " unit_mat_id = " + unit_mat_id);

                    Bundle extras = getIntent().getExtras();

                        requestCode = extras.getInt(REQUEST_CODE);
                        //если пришло из DetailSmetaMatLine
                        if (requestCode == 222) {
                            //добавляем стоимость cost работы с mat_id  с единицами измерения unit_mat_id
                            //long costId = mSmetaOpenHelper.insertCostMat(mat_id,cost,unit_mat_id);
                            mSmetaOpenHelper.updateMatkCost(mat_id, cost, unit_mat_id);
                            Intent intent = new Intent();
                            intent.putExtra(P.ID_MAT,mat_id);
                            setResult(RESULT_OK, intent);
                            Log.d(TAG, "CostMatDetail-mButtonSave requestCode =  " + requestCode +
                                    "  RESULT_OK = " + RESULT_OK );
                        }else {
                            mSmetaOpenHelper.updateMatkCost(mat_id, cost, unit_mat_id);
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
            mSmetaOpenHelper.deleteCostOfMat(mat_id);
        }
    }
}

