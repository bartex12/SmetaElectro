package ru.bartex.smetaelectro;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
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

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;

public class SmetaMatDetail extends AppCompatActivity {

    public static final String TAG = "33333";

    TextView mTextViewMatName;
    TextView mTextViewCost;
    TextView mTextViewUnit;
    EditText mEditTextCount;
    TextView mTextViewSumma;
    Button mButtonSave;
    Button mButtonCancel;
    SmetaOpenHelper mSmetaOpenHelper;
    long file_id;
    static long cat_mat_id;
    static long type_mat_id;
    static long mat_id;
    boolean isMat;

    float countMat; //количество для работы
    float costMat; //цена работы
    String unit; //единицы измерения

    static final int request_code_add_cost_mat = 222;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smeta_mat_detail);

        mSmetaOpenHelper = new SmetaOpenHelper(this);
        file_id = getIntent().getLongExtra(P.ID_FILE, 1);
        cat_mat_id = getIntent().getLongExtra(P.ID_CATEGORY_MAT, 1);
        type_mat_id = getIntent().getLongExtra(P.ID_TYPE_MAT, 1);
        mat_id = getIntent().getLongExtra(P.ID_MAT, 1);
        isMat = getIntent().getBooleanExtra(P.IS_MAT, false);

        //Если такой материал есть в FM, то считываем из таблицы количество для file_id и mat_id
        if (isMat){
            countMat = mSmetaOpenHelper.getCountMat(file_id, mat_id);
        }else {
            countMat = 0;
        }

        Log.d(TAG, "SmetaMatDetail - onCreate  file_id = " + file_id +
                "  cat_mat_id = " + cat_mat_id + "  type_id = " + type_mat_id +
                "  mat_id = " + mat_id + "  isMat = " + isMat);

        //выводим название материала
        mTextViewMatName = findViewById(R.id.tv_cost_workName);
        String matName = mSmetaOpenHelper.getMatNameById(mat_id);
        mTextViewMatName.setText(matName);

        //выводим таблицу CostWork
        //mSmetaOpenHelper.displayTableCost();

        //выводим цену работы
        mTextViewCost = findViewById(R.id.edittext_cost_cost);
        costMat = mSmetaOpenHelper.geMatkCostById(mat_id);
        mTextViewCost.setText(Float.toString(costMat));
        if ((mTextViewCost.getText().toString()).equals("0.0")){
            Log.d(TAG, "SmetaMatDetail.(mTextViewCost.getText().toString()).equals \"0.0\"");

            //если для mat_id в таблице расценок ничего нет (цена =0), то вызываем диалог
            FragmentManager fragmentManager = getSupportFragmentManager();
            DialogFragment dialogFragment = new SmetaDetail.CostDialogFragment();
            dialogFragment.show(fragmentManager,"Save_Cost_mat");
        }

        //выводим единицы измерения
        mTextViewUnit = findViewById(R.id.textView_unit);
        unit = mSmetaOpenHelper.getCostMatUnitById(mat_id);
        mTextViewUnit.setText(unit);

        //находим поле Сумма
        mTextViewSumma = findViewById(R.id.textView_summa);
        mTextViewSumma.setText(String.valueOf(countMat*costMat));

        //смотрим, что записано в поле Количество
        mEditTextCount = findViewById(R.id.editText_count);
        mEditTextCount.setText(String.valueOf(countMat));
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
                countMat = Float.parseFloat(str);
                //пишем в поле Сумма
                mTextViewSumma.setText(Float.toString(countMat*costMat));
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
                    if (isMat){
                        //Если такая работа уже есть в смете, то не вставлять, а обновлять строку
                        //но сначала нужно посмотреть, не изменилась ли расценка, поэтому cost входит
                        mSmetaOpenHelper.updateRowInFM_Count_Summa(
                                file_id, mat_id, costMat, countMat, countMat*costMat);
                        finish();
                    }else {
                        if ((mTextViewCost.getText().toString()).equals("0.0")){
                            Log.d(TAG, "SmetaMatDetail.if ((mTextViewCost.getText().toString()).eq..\"0.0\".");
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            DialogFragment dialogFragment = new SmetaMatDetail.CostMatDialogFragment();
                            dialogFragment.show(fragmentManager,"Save_Cost_mat");
                        }else{
                            long FM_ID = mSmetaOpenHelper.insertRowInFM_Name(file_id, mat_id,
                                    type_mat_id, cat_mat_id, costMat, countMat, unit, countMat*costMat);
                            Log.d(TAG, "SmetaMatDetail-mButtonSave-onClick FM_ID = " + FM_ID);
                            //выводим таблицу FM в лог для проверки
                            mSmetaOpenHelper.displayFM();
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

    public static class CostMatDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Log.d(TAG, "SmetaDetail.CostDialogFragment...");
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.CostZero);
            builder.setPositiveButton(R.string.CostOk, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getActivity(), CostDetail.class);
                    intent.putExtra(P.ID_CATEGORY_MAT, cat_mat_id);
                    intent.putExtra(P.ID_TYPE_MAT, type_mat_id);
                    intent.putExtra(P.ID_MAT, mat_id);
                    //intent.putExtra(CostMatDetail.REQUEST_CODE, request_code_add_cost_mat);
                    startActivityForResult(intent, P.REQUEST_COST);
                }
            });
            return builder.create();
        }

    }
}
