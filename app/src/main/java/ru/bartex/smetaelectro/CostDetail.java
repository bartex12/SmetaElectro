package ru.bartex.smetaelectro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;

public class CostDetail extends AppCompatActivity {

    public static final String TAG = "33333";

    TextView mTextViewWorkName;
    TextView mTextViewCost;
    TextView mTextViewUnit;
    Button mButtonSave;
    SmetaOpenHelper mSmetaOpenHelper;
    long cat_id;
    long type_id;
    long work_id;

    float count; //количество для работы
    float cost; //цена работы
    String unit; //единицы измерения


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_detail);

        mSmetaOpenHelper = new SmetaOpenHelper(this);
        cat_id = getIntent().getLongExtra(P.ID_CATEGORY, 1);
        type_id = getIntent().getLongExtra(P.ID_TYPE, 1);
        work_id = getIntent().getLongExtra(P.ID_WORK, 1);

        Log.d(TAG, "SmetaDetail - onCreate  file_id = " +
                "  cat_id = " + cat_id + "  type_id = " + type_id + "  work_id = " + work_id);

        //выводим название работы
        mTextViewWorkName = findViewById(R.id.tv_cost_workName);
        String workName = mSmetaOpenHelper.getWorkNameById(work_id);
        mTextViewWorkName.setText(workName);

        //выводим таблицу CostWork
        //mSmetaOpenHelper.displayTableCost();

        //выводим стоимость работы
        mTextViewCost = findViewById(R.id.edittext_cost_cost);
        cost = mSmetaOpenHelper.getWorkCostById(work_id);
        mTextViewCost.setText(Float.toString(cost));

        //выводим единицы измерения
        mTextViewUnit = findViewById(R.id.tv_cost_unit);
        unit = mSmetaOpenHelper.getCostUnitById(work_id);
        mTextViewUnit.setText(unit);

        mButtonSave = findViewById(R.id.button_cost_save);
        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str =  mTextViewCost.getText().toString();
                if (str.equals("")){
                    str = "0";
                }
                cost =  Float.parseFloat(str);
                //обновляем стоимость работы
                mSmetaOpenHelper.updateWorkCost(work_id, cost);
                Log.d(TAG, "CostDetail-onCreate work_id = " + work_id);
                finish();
            }
        });
    }
}
