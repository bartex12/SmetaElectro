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

public class DialogSaveWorkName extends DialogSaveName {

    static String TAG = "33333";

    public DialogSaveWorkName(){
        //пустой конструктор
    }

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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Log.d(TAG, "onCreateDialog... ");
        //принудительно вызываем клавиатуру - повторный вызов ее скроет
        takeOnAndOffSoftInput();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_save_name, null);
        builder.setView(view);

        builder.setTitle("Сохранить");
        builder.setIcon(R.drawable.ic_save_black_24dp);

        final EditText etWorkName = view.findViewById(R.id.etSaveNameWork);
        etWorkName.requestFocus();
        etWorkName.setInputType(InputType.TYPE_CLASS_TEXT);

        TextView tvTypeName = view.findViewById(R.id.tvTypeName);
        String[] arrayTypeNames = smetaOpenHelper.getArrayTypeNames(cat_id);
        final String typeName = arrayTypeNames[positionType];
        tvTypeName.setText(typeName);

        EditText etSaveNameType = view.findViewById(R.id.etSaveNameType);
        etSaveNameType.setVisibility(View.GONE);

        TextView tvCatName = view.findViewById(R.id.tvCatName);
        String[] arrayCatNames = smetaOpenHelper.getArrayCategoryNames();
        final String catName = arrayCatNames[positionCategory];
        tvCatName.setText(catName);

        EditText etSaveNameCat = view.findViewById(R.id.etSaveNameCat);
        etSaveNameCat.setVisibility(View.GONE);

        Button buttonSaveName = view.findViewById(R.id.buttonSaveName);
        buttonSaveName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String workName = etWorkName.getText().toString();

                //++++++++++++++++++   проверяем, есть ли такое имя   +++++++++++++//
                long workId = smetaOpenHelper.getIdFromWorkName(workName);
                Log.d(TAG, "workName = " + workName + "  workId = " + workId);

                //если имя - пустая строка
                if (workName.trim().isEmpty()){
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

                    //Вызываем метод интерфейса, передаём название категории в SmetaWorkElectro
                    workCategoryTypeNameListener.workCategoryTypeNameTransmit
                            (workName, typeName, catName);

                    fiishDialog();
                }
            }
        });

        Button buttonCancelName = view.findViewById(R.id.buttonCancelName);
        buttonCancelName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fiishDialog();
            }
        });

        return builder.create();
    }
}
