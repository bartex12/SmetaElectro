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

public class DialogSaveTypeName extends DialogSaveName {

    static String TAG = "33333";
    long cat_id;
    boolean isWorkDialog;

    public DialogSaveTypeName(){
        //пустой конструктор
    }

    public static DialogSaveTypeName newInstance(long cat_id, boolean isWorkDialog) {
        Log.d(TAG, "DialogSaveTypeName newInstance. cat_id =   " + cat_id);
        DialogSaveTypeName fragment = new DialogSaveTypeName();
        Bundle args = new Bundle();
        args.putLong(P.ID_CATEGORY, cat_id);
        args.putBoolean(P.IS_WORK_DIALOG, isWorkDialog);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cat_id = getArguments().getLong(P.ID_CATEGORY);
        isWorkDialog = getArguments().getBoolean(P.IS_WORK_DIALOG);
        Log.d(TAG, "DialogSaveTypeName onCreate. cat_id =   " + cat_id + "  isWorkDialog = " + isWorkDialog);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Log.d(TAG, "DialogSaveTypeName onCreateDialog... ");
        //принудительно вызываем клавиатуру - повторный вызов ее скроет
        takeOnAndOffSoftInput();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_save_name, null);
        builder.setView(view);

        builder.setTitle("Сохранить");
        builder.setIcon(R.drawable.ic_save_black_24dp);

        TextView tvKind  = view.findViewById(R.id.tvKind);
        tvKind.setVisibility(View.GONE);

        EditText etSaveNameWork = view.findViewById(R.id.etSaveNameWork);
        etSaveNameWork.setVisibility(View.GONE);

        TextView tvTypeName  = view.findViewById(R.id.tvTypeName);
        tvTypeName.setVisibility(View.GONE);

        final EditText typeName = view.findViewById(R.id.etSaveNameType);
        typeName.requestFocus();
        typeName.setInputType(InputType.TYPE_CLASS_TEXT);

        TextView tvCatName = view.findViewById(R.id.tvCatName);
        final String catName;
        if (isWorkDialog){
            catName = smetaOpenHelper.getCategoryNameById(cat_id);
            Log.d(TAG, "DialogSaveTypeName onCreateDialog. cat_id =   " + cat_id + " catName = " + catName);
        }else {
            catName = smetaOpenHelper.getCategoryMatNameById(cat_id);
            Log.d(TAG, "DialogSaveTypeName onCreateDialog. cat_id =   " + cat_id + " catName = " + catName);
        }
        tvCatName.setText(catName);

        EditText etSaveNameCat = view.findViewById(R.id.etSaveNameCat);
        etSaveNameCat.setVisibility(View.GONE);

        Button btnSaveType = view.findViewById(R.id.buttonSaveName);
        btnSaveType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //читаем имя файла в строке ввода
                String nameType = typeName.getText().toString();
                Log.d(TAG, " onCreateDialog nameType = " + nameType);

                //++++++++++++++++++   проверяем, есть ли такое имя   +++++++++++++//
                long typeId;
                if (isWorkDialog){
                    typeId = smetaOpenHelper.getIdFromTypeName(nameType);
                    Log.d(TAG, "nameType = " + nameType + "  typeId = " + typeId);
                }else {
                    typeId = smetaOpenHelper.getIdFromMatTypeName(nameType);
                    Log.d(TAG, "nameType = " + nameType + "  typeId = " + typeId);
                }

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

                    //Вызываем метод интерфейса, передаём название категории в SmetaCategoryElectro
                    workCategoryTypeNameListener.workCategoryTypeNameTransmit(null,nameType, catName);

                    fiishDialog();
                }
            }
        });

        Button btnCancelType = view.findViewById(R.id.buttonCancelName);
        btnCancelType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fiishDialog();
            }
        });
        return builder.create();
    }


}
