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

public class DialogSaveTypeName extends DialogFragment {

    static String TAG = "33333";
    SmetaOpenHelper smetaOpenHelper;
    String selCategoryName = "";
    int position;

    public DialogSaveTypeName(){
        //пустой конструктор
    }

    public interface CategoryTypeWorkNameListener{
        void categoryTypeWorkNameTransmit(String typeName, String catName);
    }
    CategoryTypeWorkNameListener categoryTypeWorkNameListener;


    public static DialogSaveTypeName newInstance(int positionCategory) {
        DialogSaveTypeName fragment = new DialogSaveTypeName();
        Bundle args = new Bundle();
        args.putInt(P.POSITION_CATEGORY, positionCategory);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(P.POSITION_CATEGORY);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        smetaOpenHelper = new SmetaOpenHelper(context);
        categoryTypeWorkNameListener = (CategoryTypeWorkNameListener)context;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Log.d(TAG, "onCreateDialog... ");
        //принудительно вызываем клавиатуру - повторный вызов ее скроет
        takeOnAndOffSoftInput();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_save_type_name, null);
        builder.setView(view);

        builder.setTitle("Сохранить тип работы");
        builder.setIcon(R.drawable.ic_save_black_24dp);

        final EditText typeName = view.findViewById(R.id.etTypeName);
        typeName.requestFocus();
        typeName.setInputType(InputType.TYPE_CLASS_TEXT);

        final String[] categoryNames = smetaOpenHelper.getArrayCategoryNames();

        Spinner spinner = view.findViewById(R.id.spinnerCatName);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, categoryNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(position);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selCategoryName = categoryNames[position];
                Log.d(TAG, "onItemSelected selCategoryName = " + selCategoryName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btnSaveType = view.findViewById(R.id.buttonSaveTypeName);
        btnSaveType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //читаем имя файла в строке ввода
                String nameType = typeName.getText().toString();
                Log.d(TAG, " onCreateDialog nameType = " + nameType);

                //++++++++++++++++++   проверяем, есть ли такое имя   +++++++++++++//
                long typeId = smetaOpenHelper.getIdFromTypeName(nameType);
                Log.d(TAG, "nameType = " + nameType + "  typeId = " + typeId);

                //если имя - пустая строка
                if (nameType.trim().isEmpty()){
                    Snackbar.make(v, "Введите непустое название типа работ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    Log.d(TAG, "Введите непустое название типа работ ");
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

                    //проверка, если ничего ещё не выбрано - не нужна, так как слушатель отрабатывает
                    //на утановленной по умолчанию позиции - у нас это позиция 0
                    String nameCategory  = "";
                    if (selCategoryName.equals("")){
                        nameCategory = categoryNames[0];
                    }else {
                        nameCategory = selCategoryName;
                    }
                    Log.d(TAG, "nameCategory = " + nameCategory);

                    //Вызываем метод интерфейса, передаём название категории в SmetaCategoryElectro
                    categoryTypeWorkNameListener.categoryTypeWorkNameTransmit(nameType, nameCategory);

                    //getActivity().finish(); //закрывает и диалог и активность
                    getDialog().dismiss();  //закрывает только диалог
                    //принудительно прячем  клавиатуру - повторный вызов ее покажет
                    takeOnAndOffSoftInput();
                }
            }
        });

        Button btnCancelType = view.findViewById(R.id.buttonCancelTypeName);
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
