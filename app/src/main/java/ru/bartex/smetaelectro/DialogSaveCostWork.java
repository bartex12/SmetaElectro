package ru.bartex.smetaelectro;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.TableControllerSmeta;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.CategoryWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.TypeWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.Unit;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.Work;

public class DialogSaveCostWork extends DialogFragment {


    public static final String TAG = "33333";

    TableControllerSmeta tableControllerSmeta;
    float cost = 0; //цена работы
    boolean isType;
    boolean isCat;
    long type_id;
    long cat_id;

    EditText etName;
    TextView textView4;
    EditText etCost;
    TextView textView;
    Spinner spinner;
    Button buttonCostSave;
    Button buttonCostCancel;
    TextView textView18;

    private SQLiteDatabase database;

    public DialogSaveCostWork() {
        // Required empty public constructor
    }

    public static DialogSaveCostWork NewInstance(
            boolean isCat, boolean isType, long catId, long typeId){
        DialogSaveCostWork fragment = new DialogSaveCostWork();
        Bundle args = new Bundle();
        args.putBoolean(P.IS_CAT_MAT, isCat);
        args.putBoolean(P.IS_TYPE_MAT, isType);
        args.putLong(P.ID_CATEGORY_MAT, catId);
        args.putLong(P.ID_TYPE_MAT, typeId);
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnCatTypeMatCostNameListener{
        void catTypeMatCostNameTransmit(
                String catName,String typeName,String matName, String costOfMat, String unit);
    }

    OnCatTypeMatCostNameListener catTypeMatCostNameListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        tableControllerSmeta = new TableControllerSmeta(context);
        catTypeMatCostNameListener = (OnCatTypeMatCostNameListener)context;
        database = new SmetaOpenHelper(context).getWritableDatabase();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isType = getArguments().getBoolean(P.IS_TYPE_MAT);
        isCat = getArguments().getBoolean(P.IS_CAT_MAT);
        cat_id = getArguments().getLong(P.ID_CATEGORY_MAT);
        type_id = getArguments().getLong(P.ID_TYPE_MAT);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);

        Log.d(TAG, "onCreateDialog... ");
        //принудительно вызываем клавиатуру - повторный вызов ее скроет
        takeOnAndOffSoftInput();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_cost, null);
        builder.setView(view);
        builder.setTitle("Сохранить");
        builder.setIcon(R.drawable.ic_save_black_24dp);

        etName = view.findViewById(R.id.tv_cost_workName);

        textView4 = view.findViewById(R.id.textView4);
        etCost  = view.findViewById(R.id.etCost);
        textView = view.findViewById(R.id.textView);
        textView18 = view.findViewById(R.id.textView18);
        spinner = view.findViewById(R.id.spinnerUnits);

        buttonCostSave = view.findViewById(R.id.button_cost_save);
        buttonCostCancel = view.findViewById(R.id.button_cost_cancel);

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        buttonCostCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishDialog();
            }
        });

        //******************вариант, когда Добавить сделано в Категории работы**************
        if (isCat){
            textView4.setVisibility(View.GONE);
            etCost.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            textView18.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);

            buttonCostSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //читаем имя материала в строке ввода
                    String nameCat = etName.getText().toString();
                    Log.d(TAG, " onCreateDialog nameCat = " + nameCat);

                    //++++++++++++++++++   проверяем, есть ли такое имя   +++++++++++++//
                    long catId = CategoryWork.getIdFromName(database, nameCat);
                    Log.d(TAG, "nameCat = " + nameCat + "  catId = " + catId);

                    //если имя - пустая строка
                    if (nameCat.trim().isEmpty()){
                        Snackbar.make(v, "Введите непустое название", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                        Log.d(TAG, "Введите непустое название ");
                        return;

                        //если такое имя уже есть в базе
                    }else if (catId != -1) {
                        Snackbar.make(v, "Такое название уже существует. Введите другое.",
                                Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        Log.d(TAG, "Такое название существует. Введите другое название. catId = " + catId);
                        return;

                        //если имя не повторяется, оно не пустое то
                    }else {
                        Log.d(TAG, "Такое название отсутствует catId = " + catId);
                        //Вызываем метод интерфейса, передаём название типа в SmetaMatCost
                        catTypeMatCostNameListener.catTypeMatCostNameTransmit(
                                nameCat, null, null, null, null);

                        finishDialog();
                    }

                }
            });
            //******************вариант, когда Добавить сделано в Типе работы**************
        } else if (isType){
            textView4.setVisibility(View.GONE);
            etCost.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            textView18.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);

            buttonCostSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //читаем имя типа материала в строке ввода
                    String nameType = etName.getText().toString();
                    Log.d(TAG, " onCreateDialog nameType = " + nameType);

                    //++++++++++++++++++   проверяем, есть ли такое имя типа   +++++++++++++//
                    long typeId = TypeWork.getIdFromName(database, nameType);
                    Log.d(TAG, "nameType = " + nameType + "  typeId = " + typeId);

                    //если имя - пустая строка
                    if (nameType.trim().isEmpty()){
                        Snackbar.make(v, "Введите непустое название", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                        Log.d(TAG, "Введите непустое название ");
                        return;

                        //если такое имя уже есть в базе
                    }else if (typeId != -1) {
                        Snackbar.make(v, "Такое название уже существует. Введите другое.",
                                Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        Log.d(TAG, "Такое название существует. Введите другое название. typeId = " +typeId);
                        return;

                        //если имя не повторяется, оно не пустое то
                    }else {
                        Log.d(TAG, "Такое название отсутствует typeId = " + typeId);

                        //получаем имя категории по её Id
                        String catMatName = CategoryWork.getNameFromId(database, cat_id);

                        //Вызываем метод интерфейса, передаём название типа в SmetaMatCost
                        catTypeMatCostNameListener.catTypeMatCostNameTransmit(
                                catMatName, nameType, null, null, null);

                        finishDialog();
                    }
                }
            });

            //**************вариант, когда Добавить сделано в работе***********
        }else {
            etCost.setText("0");
            etCost.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String costOfMat = s.toString();
                    if ((costOfMat.equals("")) || (costOfMat.equals("."))) {
                        costOfMat = "0";
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            //получаем массив единиц измерения из таблицы Unit
            String[] unins = tableControllerSmeta.getArrayUnits(Unit.TABLE_NAME);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item,unins);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(adapter);
            spinner.setSelection(0);
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

            buttonCostSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //читаем имя материала в строке ввода
                    String nameMat = etName.getText().toString();
                    Log.d(TAG, " onCreateDialog nameMat = " + nameMat);

                    //++++++++++++++++++   проверяем, есть ли такое имя   +++++++++++++//
                    long matId = Work.getIdFromName(database, nameMat);
                    Log.d(TAG, "nameMat = " + nameMat + "  matId = " + matId);

                    //если имя - пустая строка
                    if (nameMat.trim().isEmpty()) {
                        Snackbar.make(v, "Введите непустое название", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                        Log.d(TAG, "Введите непустое название ");
                        return;

                        //если такое имя уже есть в базе
                    } else if (matId != -1) {
                        Snackbar.make(v, "Такое название уже существует. Введите другое.",
                                Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        Log.d(TAG, "Такое название существует. Введите другое название. matId = " + matId);
                        return;

                        //если имя не повторяется, оно не пустое то
                    } else {
                        Log.d(TAG, "Такое название отсутствует matId = " + matId + "  type_id  +=" + type_id);

                        String costOfMat = etCost.getText().toString();
                        if ((costOfMat.equals("")) || (costOfMat.equals("."))) {
                            costOfMat = "0";
                        }
                        Log.d(TAG, "DialogSaveCostMat-mButtonSave. costOfMat = " + costOfMat);
                        //проверка на 0, чтобы не было нулевых строк в смете
                        if (Float.parseFloat(costOfMat) == 0) {
                            //Snackbar заслонён клавиатурой, поэтому в манифесте пишем
                            //android:windowSoftInputMode="stateVisible|adjustResize"
                            Snackbar.make(v, "Введите число, не равное нулю",
                                    Snackbar.LENGTH_SHORT).setAction("Action", null).show();

                        } else {
                            costOfMat = etCost.getText().toString();
                            if ((costOfMat.equals("")) || (costOfMat.equals("."))) {
                                costOfMat = "0";
                            }

                            String unit = spinner.getSelectedItem().toString();

                            //Вызываем метод интерфейса, передаём название типа в SmetaMatCost
                            catTypeMatCostNameListener.catTypeMatCostNameTransmit(
                                    null, null,nameMat, costOfMat, unit );

                            finishDialog();
                        }
                    }
                }
            });
        }
        return builder.create();
    }

    //принудительно вызываем клавиатуру - повторный вызов ее скроет
    public void takeOnAndOffSoftInput(){
        InputMethodManager imm = (InputMethodManager) getActivity().
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    //окончание диалога с убиранием клавиатуры
    public void finishDialog(){
        //getActivity().finish(); //закрывает и диалог и активность
        getDialog().dismiss();  //закрывает только диалог
        takeOnAndOffSoftInput();
    }

}
