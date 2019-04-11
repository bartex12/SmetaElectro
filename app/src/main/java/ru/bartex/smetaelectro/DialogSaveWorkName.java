package ru.bartex.smetaelectro;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;

public class DialogSaveWorkName extends DialogFragment {

    int positionCategory;
    int  positionType;
    long cat_id;
    static String TAG = "33333";
    SmetaOpenHelper smetaOpenHelper;
    String selCategoryName = "";
    String selTypeName = "";


    public DialogSaveWorkName(){
        //пустой конструктор
    }

    public interface WorkCategoryTypeWorkNameListener{
        void workCategoryTypeWorkNameTransmit(String workName,String typeName, String catName);
    }
    WorkCategoryTypeWorkNameListener workCategoryTypeWorkNameListener;


    public static DialogSaveWorkName newInstance(long cat_id,int positionCategory, int  positionType){
        DialogSaveWorkName dialogSaveWorkName = new DialogSaveWorkName();
        Bundle bundle = new Bundle();
        bundle.putInt(P.POSITION_CATEGORY,positionCategory);
        bundle.putInt(P.POSITION_TYPE,positionType);
        bundle.putLong(P.ID_CATEGORY,cat_id);
        dialogSaveWorkName.setArguments(bundle);
        return dialogSaveWorkName;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        positionCategory = getArguments().getInt(P.POSITION_CATEGORY);
        positionType = getArguments().getInt(P.POSITION_TYPE);
        cat_id = getArguments().getLong(P.ID_CATEGORY);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        smetaOpenHelper = new SmetaOpenHelper(context);
        workCategoryTypeWorkNameListener = (WorkCategoryTypeWorkNameListener)context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Log.d(TAG, "onCreateDialog... ");
        //принудительно вызываем клавиатуру - повторный вызов ее скроет
        takeOnAndOffSoftInput();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_save_work_name, null);
        builder.setView(view);

        builder.setTitle("Сохранить работу");
        builder.setIcon(R.drawable.ic_save_black_24dp);

        final EditText workName = view.findViewById(R.id.etWorkName);
        workName.requestFocus();
        workName.setInputType(InputType.TYPE_CLASS_TEXT);

        final String[] arrayTypeNames = smetaOpenHelper.getArrayTypeNames(cat_id);
        Spinner  spinnerType = view.findViewById(R.id.spinnerTypeName);
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, arrayTypeNames);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapterType);
        spinnerType.setSelection(positionType);

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selTypeName = arrayTypeNames[position];
                Log.d(TAG, "onItemSelected selTypeName = " + selTypeName);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final String[] arrayCatNames = smetaOpenHelper.getArrayCategoryNames();
        Spinner  spinnerCat  = view.findViewById(R.id.spinnerCatName);
        ArrayAdapter<String> adapterCat = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, arrayCatNames);
        adapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCat.setAdapter(adapterCat);
        spinnerCat.setSelection(positionCategory);

        spinnerCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selCategoryName = arrayCatNames[position];
                Log.d(TAG, "onItemSelected selCategoryName = " + selCategoryName);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button btnSaveType = view.findViewById(R.id.buttonSaveWorkName);
        btnSaveType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //читаем имя работы в строке ввода
                String nameWork = workName.getText().toString();
                Log.d(TAG, " onCreateDialog nameWork = " + nameWork);

                //++++++++++++++++++   проверяем, есть ли такое имя   +++++++++++++//
                long workId = smetaOpenHelper.getIdFromWorkName(nameWork);
                Log.d(TAG, "nameWork = " + nameWork + "  workId = " + workId);

                //если имя - пустая строка
                if (nameWork.trim().isEmpty()){
                    Snackbar.make(v, "Введите непустое название работы", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    Log.d(TAG, "Введите непустое название работы ");
                    return;

                    //если такое имя уже есть в базе
                }else if (workId != -1) {
                    Snackbar.make(v, "Такое название уже существует. Введите другое.",
                            Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    Log.d(TAG, "Такое название существует. Введите другое название. workId = " + workId);
                    return;

                    //если имя не повторяется, оно не пустое то
                }else {
                    Log.d(TAG, "Такое название отсутствует workId = " + workId);
/*
                    //проверка, если ничего ещё не выбрано - не нужна, так как слушатель отрабатывает
                    //на утановленной по умолчанию позиции - у нас это позиция 0
                    String nameCategory  = "";
                    if (selCategoryName.equals("")){
                        nameCategory = categoryNames[0];
                    }else {
                        nameCategory = selCategoryName;
                    }
                    Log.d(TAG, "nameCategory = " + nameCategory);
*/

                    //Вызываем метод интерфейса, передаём название категории в SmetaCategoryElectro
                    workCategoryTypeWorkNameListener.workCategoryTypeWorkNameTransmit
                            (nameWork, selTypeName, selCategoryName);

                    //getActivity().finish(); //закрывает и диалог и активность
                    getDialog().dismiss();  //закрывает только диалог
                    //принудительно прячем  клавиатуру - повторный вызов ее покажет
                    takeOnAndOffSoftInput();
                }
            }
        });

        Button btnCancelType = view.findViewById(R.id.buttonCancelWorkName);
        btnCancelType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().finish(); //закрывает и диалог и активность
                getDialog().dismiss();  //закрывает только диалог
                takeOnAndOffSoftInput();
            }
        });
        return builder.create();
    }

    //принудительно вызываем клавиатуру - повторный вызов ее скроет
    private void takeOnAndOffSoftInput(){
        InputMethodManager imm = (InputMethodManager) getActivity().
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
}
