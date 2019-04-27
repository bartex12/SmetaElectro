package ru.bartex.smetaelectro;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;


public class DialogSaveCatName extends DialogSaveName {

    static String TAG = "33333";
    boolean isWorkDialog;
    public DialogSaveCatName(){};

    public static DialogSaveCatName newInstance(boolean isWorkDialog) {
        Log.d(TAG, "DialogSaveCatName newInstance. isWorkDialog =   " + isWorkDialog);
        DialogSaveCatName fragment = new DialogSaveCatName();
        Bundle args = new Bundle();
        args.putBoolean(P.IS_WORK_DIALOG, isWorkDialog);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isWorkDialog = getArguments().getBoolean(P.IS_WORK_DIALOG);
        Log.d(TAG, "DialogSaveCatName onCreate. isWorkDialog =   " + isWorkDialog);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog... ");
        //принудительно вызываем клавиатуру - повторный вызов ее скроет
        takeOnAndOffSoftInput();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_save_name, null);
        builder.setView(view);
        builder.setTitle("Сохранить");
        builder.setIcon(R.drawable.ic_save_black_24dp);


        TextView tvKind  = view.findViewById(R.id.tvKind);
        tvKind.setVisibility(View.GONE);

        EditText etSaveNameWork = view.findViewById(R.id.etSaveNameWork);
        etSaveNameWork.setVisibility(View.GONE);

        TextView tvType  = view.findViewById(R.id.tvType);
        tvType.setVisibility(View.GONE);

        TextView tvTypeName  = view.findViewById(R.id.tvTypeName);
        tvTypeName.setVisibility(View.GONE);

        EditText typeName = view.findViewById(R.id.etSaveNameType);
        typeName.setVisibility(View.GONE);

        TextView tvCatName  = view.findViewById(R.id.tvCatName);
        tvCatName.setVisibility(View.GONE);

        final EditText etSaveNameCat = view.findViewById(R.id.etSaveNameCat);
        etSaveNameCat.requestFocus();
        etSaveNameCat.setInputType(InputType.TYPE_CLASS_TEXT);

        Button btnSaveName = view.findViewById(R.id.buttonSaveName);
        btnSaveName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //читаем имя файла в строке ввода
                String nameCat = etSaveNameCat.getText().toString();
                Log.d(TAG, " onCreateDialog nameCat = " + nameCat);

                //++++++++++++++++++   проверяем, есть ли такое имя   +++++++++++++//
                long catId;
                if (isWorkDialog){
                    catId = smetaOpenHelper.getIdFromCategoryName(nameCat);
                    Log.d(TAG, "nameCat = " + nameCat + "  catId = " +catId);
                }else {
                    catId = smetaOpenHelper.getCatIdFromCategoryMatName(nameCat);
                    Log.d(TAG, "nameCat = " + nameCat + "  catId = " +catId);
                }

                //если имя - пустая строка
                if (nameCat.trim().isEmpty()){
                    Snackbar.make(view, "Введите непустое название категории", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    Log.d(TAG, "Введите непустое название категории ");
                    return;

                    //если такое имя уже есть в базе
                }else if (catId != -1) {
                    Snackbar.make(view, "Такое название уже существует. Введите другое.",
                            Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    Log.d(TAG, "Такое имя название существует. Введите другое название. catId = " +catId);
                    return;

                    //если имя не повторяется, оно не пустое то
                }else {
                    Log.d(TAG, "Такое название отсутствует catId = " + catId);

                    //Вызываем метод интерфейса, передаём название категории в SmetaCategoryElectro
                    workCategoryTypeNameListener.workCategoryTypeNameTransmit(null,null, nameCat);

                    fiishDialog();
                    //getDialog().dismiss();
                }

            }
        });

        Button btnCancel = view.findViewById(R.id.buttonCancelName);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fiishDialog();
            }
        });

        //если не делать запрет на закрытие окна при щелчке за пределами окна, то можно так
        return builder.create();
    }


}
