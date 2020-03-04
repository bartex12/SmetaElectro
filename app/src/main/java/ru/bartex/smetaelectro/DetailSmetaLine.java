package ru.bartex.smetaelectro;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.TableControllerSmeta;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.FM;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.CostWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.FW;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.Work;

public class DetailSmetaLine extends AppCompatActivity {

    public static final String TAG = "33333";

    TextView mTextViewWorkName;
    TextView mTextViewCost;
    TextView mTextViewUnit;
    EditText mEditTextCount;
    TextView mTextViewSumma;
    Button mButtonSave;
    Button mButtonCancel;
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
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smeta_detail);

        initDB();

        tableControllerSmeta = new TableControllerSmeta(this);

        file_id = getIntent().getLongExtra(P.ID_FILE_DEFAULT, 1);
        cat_id = getIntent().getLongExtra(P.ID_CATEGORY, 1);
        type_id = getIntent().getLongExtra(P.ID_TYPE, 1);
        work_id = getIntent().getLongExtra(P.ID_WORK, 1);
        isWork = getIntent().getBooleanExtra(P.IS_WORK, false);
        //Если такая работа есть в FW, то считываем из таблицы количество для file_id и work_id
        if (isWork){
            count = FW.getCount(database, file_id, work_id);
        }else {
            count = 0;
        }

        Log.d(TAG, "DetailSmetaLine - onCreate  file_id = " + file_id +
                "  cat_id = " + cat_id + "  type_id = " + type_id + "  work_id = " + work_id +
                "isWork" + isWork);

        //выводим название работы
        mTextViewWorkName = findViewById(R.id.tv_cost_workName);
        String workName = Work.getNameFromId(database, work_id);
        mTextViewWorkName.setText(workName);

        //выводим таблицу CostWork
        //mSmetaOpenHelper.displayTableCost();

        //выводим цену работы
        mTextViewCost = findViewById(R.id.etCost);
        cost = CostWork.getCostById(database, work_id);
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
        unit = CostWork.getNameFromId(database, work_id);
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
                        tableControllerSmeta.updateRowInFWFM(
                                file_id, work_id, cost,unit, count, count*cost, FW.TABLE_NAME);
                        finish();
                        //если работы нет, то сначала посмотреть цену работы и если она равна 0.0,
                        // вызвать диалог установки цены !ПЕРЕДЕЛАТЬ НА ЦЕНА <0
                    }else {
                        if ((mTextViewCost.getText().toString()).equals("0.0")){
                            Log.d(TAG, "DetailSmetaLine.if ((mTextViewCost.getText().toString()).eq...");
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            DialogFragment dialogFragment = new CostDialogFragment();
                            dialogFragment.show(fragmentManager,"Save_Cost");
                        }else{
                            long FW_ID = tableControllerSmeta.insertRowInFWFM(file_id, work_id,
                                    type_id, cat_id, cost, count, unit, count*cost, FW.TABLE_NAME);
                            Log.d(TAG, "DetailSmetaLine-mButtonSave-onClick FW_ID = " + FW_ID);
                            //выводим таблицу FW в лог для проверки
                            FM.displayTable(database);
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

    private void initDB() {
        //
        database = new SmetaOpenHelper(this).getWritableDatabase();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "DetailSmetaLine.onActivityResult...  resultCode = " + (resultCode == RESULT_OK) +
                "  requestCode = " +(requestCode==P.REQUEST_COST));
        if (resultCode == RESULT_OK) {
                Log.d(TAG, "DetailSmetaLine.onActivityResult..RESULT_OK - requestCode == P.REQUEST_COST)");
            cost = CostWork.getCostById(database, work_id);
                mTextViewCost.setText(Float.toString(cost));
            unit = CostWork.getNameFromId(database, work_id);
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
