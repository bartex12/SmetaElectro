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
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.CostMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.FM;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.Mat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.UnitMat;

public class DetailSmetaMatLine extends AppCompatActivity {

    public static final String TAG = "33333";

    TextView mTextViewMatName;
    TextView mTextViewCost;
    TextView mTextViewUnit;
    EditText mEditTextCount;
    TextView mTextViewSumma;
    Button mButtonSave;
    Button mButtonCancel;
    TableControllerSmeta tableControllerSmeta;
    long file_id;
    static long cat_mat_id;
    static long type_mat_id;
    static long mat_id;
    boolean isMat;

    float countMat; //количество для работы
    float costMat; //цена работы
    String unit; //единицы измерения

    static final int request_code_add_cost_mat = 222;
    private SQLiteDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smeta_mat_detail);

        initDB();

        tableControllerSmeta = new TableControllerSmeta(this);

        file_id = getIntent().getLongExtra(P.ID_FILE, 1);
        cat_mat_id = getIntent().getLongExtra(P.ID_CATEGORY_MAT, 1);
        type_mat_id = getIntent().getLongExtra(P.ID_TYPE_MAT, 1);
        mat_id = getIntent().getLongExtra(P.ID_MAT, 1);
        isMat = getIntent().getBooleanExtra(P.IS_MAT, false);

        //Если такой материал есть в FM, то считываем из таблицы количество для file_id и mat_id
        if (isMat){
            countMat = FM.getCount(database, file_id, mat_id);
        }else {
            countMat = 0;
        }
        Log.d(TAG, "DetailSmetaMatLine - onCreate  file_id = " + file_id +
                "  cat_mat_id = " + cat_mat_id + "  type_id = " + type_mat_id +
                "  mat_id = " + mat_id + "  isMat = " + isMat);

        //выводим название материала
        mTextViewMatName = findViewById(R.id.tv_cost_workName);
        String matName = Mat.getNameFromId(database, mat_id);
        mTextViewMatName.setText(matName);

        //выводим таблицу CostWork
        //mSmetaOpenHelper.displayTableCost();

        //выводим цену работы
        mTextViewCost = findViewById(R.id.etCost);
        costMat = CostMat.getCostById(database, mat_id);
        mTextViewCost.setText(Float.toString(costMat));
        if ((mTextViewCost.getText().toString()).equals("0.0")){
            Log.d(TAG, "DetailSmetaMatLine.(mTextViewCost.getText().toString()).equals \"0.0\"");
            //если для mat_id в таблице расценок ничего нет (цена =0), то вызываем диалог
            FragmentManager fragmentManager = getSupportFragmentManager();
            DialogFragment dialogFragment = new CostMatDialogFragment();
            dialogFragment.show(fragmentManager,"Save_Cost_mat");
        }

        //выводим единицы измерения
        mTextViewUnit = findViewById(R.id.textView_unit);
        unit = UnitMat.getUnitMat(database, mat_id);
        mTextViewUnit.setText(unit);

        //находим поле Сумма
        mTextViewSumma = findViewById(R.id.textView_summa);
        mTextViewSumma.setText(String.format(Locale.ENGLISH,"%.2f", (countMat*costMat)));


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
                mTextViewSumma.setText(String.format(Locale.ENGLISH,"%.2f", (countMat*costMat)));
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
                        //Если такой материал уже есть в смете, то не вставлять, а обновлять строку
                        //но сначала нужно посмотреть, не изменилась ли расценка и единица измерения,
                        // поэтому cost и unit входит
                        tableControllerSmeta.updateRowInFWFM(
                                file_id, mat_id, costMat, unit, countMat,
                                countMat*costMat, FM.TABLE_NAME);
                        finish();
                    }else {
                        if ((mTextViewCost.getText().toString()).equals("0.0")){
                            Log.d(TAG, "DetailSmetaMatLine.if ((mTextViewCost.getText().toString()).eq..\"0.0\".");
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            DialogFragment dialogFragment = new CostMatDialogFragment();
                            dialogFragment.show(fragmentManager,"Save_Cost_mat");
                        }else{
                            long FM_ID = FM.insertRowInFM(database, file_id, mat_id,
                                    type_mat_id, cat_mat_id, costMat, countMat,
                                    unit, countMat*costMat);
                            Log.d(TAG, "DetailSmetaMatLine-mButtonSave-onClick FM_ID = " + FM_ID +
                                    " unit = " + unit);
                            //выводим таблицу FM в лог для проверки
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

        Log.d(TAG, "DetailSmetaMatLine.onActivityResult...  resultCode = "+ resultCode +
                "  requestCode = " + requestCode);
        if (resultCode == RESULT_OK) {
            Log.d(TAG, "DetailSmetaLine.onActivityResult..RESULT_OK - requestCode == P.REQUEST_COST)");
            //long matId = data.getExtras().getLong(P.ID_MAT);
            costMat = CostMat.getCostById(database, mat_id);
            mTextViewCost.setText(Float.toString(costMat));

            mTextViewSumma.setText(String.format(Locale.ENGLISH,"%.2f", (countMat*costMat)));

            unit = UnitMat.getUnitMat(database, mat_id);
            mTextViewUnit.setText(unit);
        }
    }

    public static class CostMatDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Log.d(TAG, "DetailSmetaMatLine.CostMatDialogFragment...");
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.CostZero);
            builder.setPositiveButton(R.string.CostOk, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getActivity(), CostMatDetail.class);
                    intent.putExtra(P.ID_CATEGORY_MAT, cat_mat_id);
                    intent.putExtra(P.ID_TYPE_MAT, type_mat_id);
                    intent.putExtra(P.ID_MAT, mat_id);
                    intent.putExtra(CostMatDetail.REQUEST_CODE, request_code_add_cost_mat);
                    startActivityForResult(intent, P.REQUEST_COST_MAT);
                }
            });
            return builder.create();
        }
    }
}
