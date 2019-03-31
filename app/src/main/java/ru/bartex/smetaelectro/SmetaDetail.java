package ru.bartex.smetaelectro;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    Button mButtonCancel;
    SmetaOpenHelper mSmetaOpenHelper;
    long file_id;
    long cat_id;
    long type_id;
    long work_id;
    boolean isWork;

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
        isWork = getIntent().getBooleanExtra(P.IS_WORK, false);
        if (isWork){
            count = mSmetaOpenHelper.getCountWork(file_id, work_id);
        }else {
            count = 0;
        }

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
        mEditTextCount.setText(String.valueOf(count));
        mEditTextCount.requestFocus();
        mEditTextCount.selectAll();

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
                //проверка на 0, чтобы не было нулевых строк в смете
                if ((mEditTextCount.getText().toString()).equals("0")||
                        (mEditTextCount.getText().toString()).equals("")){

                    //Snackbar заслонён клавиатурой, поэтому в манифесте пишем
                    //android:windowSoftInputMode="stateVisible|adjustResize"
                    Snackbar.make(getCurrentFocus(), "Введите количество, не равное нулю",
                            Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    //Toast.makeText(SmetaDetail.this,"Введите количество ",
                           // Toast.LENGTH_LONG).show();

                }else {
                    if (isWork){
                        //Если такая работа уже есть в смете, то не вставлять, а обновлять строку
                        mSmetaOpenHelper.updateRowInFW_Count_Summa(file_id, work_id, count, count*cost);

                    }else {
                        long FW_ID = mSmetaOpenHelper.insertRowInFW_Name(file_id, work_id,
                                type_id, cat_id, cost, count, unit, count*cost);
                        Log.d(TAG, "SmetaDetail-mButtonSave-onClick FW_ID = " + FW_ID);
                        //выводим таблицу FW в лог для проверки
                        mSmetaOpenHelper.displayFW();
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
}
