package ru.bartex.smetaelectro;

import android.app.Dialog;
import android.support.v4.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.CostWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.FM;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.TableControllerSmeta;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.Work;

public class DetailSmetaLine extends AppCompatActivity {

    public static final String TAG = "33333";

    TextView mTextViewWorkName;
    TextView mTextViewCost;
    TextView mTextViewUnit;
    EditText mEditTextCount;
    TextView mTextViewSumma;
    Button mButtonSave;
    Button mButtonCancel;
    SmetaOpenHelper mSmetaOpenHelper;
    TableControllerSmeta tableControllerSmeta;
    long file_id;
    static long cat_id;
    static long type_id;
    static long work_id;
    boolean isWork;

    float count; //количество для работы
    float cost; //цена работы
    String unit; //единицы измерения

    static final int request_code_add_cost = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smeta_detail);

        mSmetaOpenHelper = new SmetaOpenHelper(this);
        tableControllerSmeta = new TableControllerSmeta(this);

        file_id = getIntent().getLongExtra(P.ID_FILE_DEFAULT, 1);
        cat_id = getIntent().getLongExtra(P.ID_CATEGORY, 1);
        type_id = getIntent().getLongExtra(P.ID_TYPE, 1);
        work_id = getIntent().getLongExtra(P.ID_WORK, 1);
        isWork = getIntent().getBooleanExtra(P.IS_WORK, false);
        //Если такая работа есть в FW, то считываем из таблицы количество для file_id и work_id
        if (isWork){
            count = mSmetaOpenHelper.getCountWork(file_id, work_id);
        }else {
            count = 0;
        }

        Log.d(TAG, "DetailSmetaLine - onCreate  file_id = " + file_id +
                "  cat_id = " + cat_id + "  type_id = " + type_id + "  work_id = " + work_id +
                "isWork" + isWork);

        //выводим название работы
        mTextViewWorkName = findViewById(R.id.tv_cost_workName);
        String workName = tableControllerSmeta.getNameFromId(work_id, Work.TABLE_NAME);
        mTextViewWorkName.setText(workName);

        //выводим таблицу CostWork
        //mSmetaOpenHelper.displayTableCost();

        //выводим цену работы
        mTextViewCost = findViewById(R.id.etCost);
        cost = mSmetaOpenHelper.getWorkCostById(work_id);
        mTextViewCost.setText(Float.toString(cost));
        if ((mTextViewCost.getText().toString()).equals("0.0")){
            Log.d(TAG, "DetailSmetaLine.(mTextViewCost.getText().toString()).equals");

            //если для work_id в таблице расценок ничего нет (цена =0), то вызываем диалог
            FragmentManager fragmentManager = getSupportFragmentManager();
            DialogFragment dialogFragment = new CostDialogFragment();
            dialogFragment.show(fragmentManager,"Save_Cost");
        }

        //выводим единицы измерения
        mTextViewUnit = findViewById(R.id.textView_unit);
        unit = tableControllerSmeta.getNameFromId(work_id, CostWork.TABLE_NAME);
        mTextViewUnit.setText(unit);

        //находим поле Сумма
        mTextViewSumma = findViewById(R.id.textView_summa);
        mTextViewSumma.setText(String.format(Locale.ENGLISH,"%.2f", (count*cost)));


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
               if ((str.equals(""))||(str.equals("."))){
                    str = "0";
                }
                count = Float.parseFloat(str);
               //пишем в поле Сумма
                mTextViewSumma.setText(String.format(Locale.ENGLISH,"%.2f", (count*cost)));
            }
        });

        mButtonSave = findViewById(R.id.button_cost_save);
        View.OnClickListener onButtonSave = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //проверка на 0, чтобы не было нулевых строк в смете
                if ((mEditTextCount.getText().toString()).equals(".")||
                        (mEditTextCount.getText().toString()).equals("")||
                        Float.parseFloat(mEditTextCount.getText().toString())==0){

                    //Snackbar заслонён клавиатурой, поэтому в манифесте пишем
                    //android:windowSoftInputMode="stateVisible|adjustResize"
                    Snackbar.make(getCurrentFocus(), "Введите количество, не равное нулю",
                            Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }else {
                    if (isWork){
                        //Если такая работа уже есть в смете, то не вставлять, а обновлять строку
                        //но сначала нужно посмотреть, не изменилась ли расценка и единица измерения,
                        // поэтому cost и unit входит
                        mSmetaOpenHelper.updateRowInFW_Count_Summa(file_id, work_id, cost,unit, count, count*cost);
                        finish();
                    }else {
                        if ((mTextViewCost.getText().toString()).equals("0.0")){
                            Log.d(TAG, "DetailSmetaLine.if ((mTextViewCost.getText().toString()).eq...");
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            DialogFragment dialogFragment = new CostDialogFragment();
                            dialogFragment.show(fragmentManager,"Save_Cost");
                        }else{
                            long FW_ID = mSmetaOpenHelper.insertRowInFW_Name(file_id, work_id,
                                    type_id, cat_id, cost, count, unit, count*cost);
                            Log.d(TAG, "DetailSmetaLine-mButtonSave-onClick FW_ID = " + FW_ID);
                            //выводим таблицу FW в лог для проверки
                            tableControllerSmeta.displayTable(FM.TABLE_NAME);
                            finish();
                        }
                    }
                }
            }
        };
        mButtonSave.setOnClickListener(onButtonSave);


        mButtonCancel = findViewById(R.id.button_cost_cancel);
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "DetailSmetaLine.onActivityResult...  resultCode = "+ (resultCode == RESULT_OK?true:false) +
                "  requestCode = " +(requestCode==P.REQUEST_COST));
        if (resultCode == RESULT_OK) {
                Log.d(TAG, "DetailSmetaLine.onActivityResult..RESULT_OK - requestCode == P.REQUEST_COST)");
                cost = mSmetaOpenHelper.getWorkCostById(work_id);
                mTextViewCost.setText(Float.toString(cost));
                unit =tableControllerSmeta.getNameFromId(work_id, CostWork.TABLE_NAME);
                mTextViewUnit.setText(unit);
                mTextViewSumma.setText(String.format(Locale.ENGLISH,"%.2f", (count*cost)));
        }
    }

    public static class CostDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Log.d(TAG, "DetailSmetaLine.CostDialogFragment...");
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.CostZero);
            builder.setPositiveButton(R.string.CostOk, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getActivity(), DetailCost.class);
                    intent.putExtra(P.ID_CATEGORY, cat_id);
                    intent.putExtra(P.ID_TYPE, type_id);
                    intent.putExtra(P.ID_WORK, work_id);
                    intent.putExtra(DetailCost.REQUEST_CODE, request_code_add_cost);
                    startActivityForResult(intent, P.REQUEST_COST);
                }
            });
            return builder.create();
        }
    }
}
