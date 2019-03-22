package ru.bartex.smetaelectro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;

public class SmetaDetail extends AppCompatActivity {

    public static final String TAG = "33333";

    TextView mTextViewWorkName;
    TextView mTextViewCost;
    TextView mTextViewUnit;
    EditText mEditTextCount;
    TextView mTextViewSumma;
    Button mButtonSave;
    SmetaOpenHelper mSmetaOpenHelper;
    long file_id;
    long cat_id;
    long type_id;
    long work_id;

    float count; //количество для работы
    float cost; //цена работы
    String unit; //единицы измерения

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smeta_detail);

        mSmetaOpenHelper = new SmetaOpenHelper(this);
        file_id = getIntent().getLongExtra(P.ID_FILE_DEFAULT, 1);
        cat_id = getIntent().getLongExtra(P.ID_CATEGORY, 1);
        type_id = getIntent().getLongExtra(P.ID_TYPE, 1);
        work_id = getIntent().getLongExtra(P.ID_WORK, 1);

        Log.d(TAG, "SmetaDetail - onCreate  file_id = " + file_id +
                "  cat_id = " + cat_id + "  type_id = " + type_id + "  work_id = " + work_id);

        //выводим название работы
        mTextViewWorkName = findViewById(R.id.tv_cost_workName);
        String workName = mSmetaOpenHelper.getWorkNameById(work_id);
        mTextViewWorkName.setText(workName);

        //выводим таблицу CostWork
        //mSmetaOpenHelper.displayTableCost();

        //выводим цену работы
        mTextViewCost = findViewById(R.id.edittext_cost_cost);
        cost = mSmetaOpenHelper.getWorkCostById(work_id);
        mTextViewCost.setText(Float.toString(cost));

        //выводим единицы измерения
        mTextViewUnit = findViewById(R.id.textView_unit);
        unit = mSmetaOpenHelper.getCostUnitById(work_id);
        mTextViewUnit.setText(unit);

        //находим поле Сумма
        mTextViewSumma = findViewById(R.id.textView_summa);

        //смотрим, что записано в поле Количество
        mEditTextCount = findViewById(R.id.editText_count);
        mEditTextCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               String str =  mEditTextCount.getText().toString();
               if (str.equals("")){
                    str = "0";
                }
                count = Float.parseFloat(str);
               //пишем в поле Сумма
                mTextViewSumma.setText(Float.toString(count*cost));
            }
        });

        mButtonSave = findViewById(R.id.button_cost_save);
        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long FW_ID = mSmetaOpenHelper.insertRowInFW(file_id, work_id,
                        type_id, cat_id, cost, count, unit, count*cost);

                Log.d(TAG, "SmetaDetail-mButtonSave-onClick FW_ID = " + FW_ID);
                //Snackbar.make(getCurrentFocus(), "Системный файл. Удаление запрещено.", Snackbar.LENGTH_LONG)
                 //       .setAction("Action", null).show();
                //выводим таблицу FW в лог для проверки
                mSmetaOpenHelper.displayFW();
                finish();
            }
        });
    }
}
