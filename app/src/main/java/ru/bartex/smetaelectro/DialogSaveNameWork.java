package ru.bartex.smetaelectro;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.CategoryMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.Mat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.TypeMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.CategoryWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.TypeWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.Work;

public class DialogSaveNameWork extends DialogSaveNameAbstract {

    static String TAG = "33333";
    long cat_id;
    long type_id;
    boolean isWorkDialog;
    public DialogSaveNameWork(){
        //пустой конструктор
    }

    public static DialogSaveNameWork newInstance(long cat_id, long type_id, boolean isWorkDialog){
        Log.d(TAG, "DialogSaveNameWork newInstance... ");
        DialogSaveNameWork dialogSaveWorkName = new DialogSaveNameWork();
        Bundle bundle = new Bundle();
        bundle.putLong(P.ID_CATEGORY,cat_id);
        bundle.putLong(P.ID_TYPE,type_id);
        bundle.putBoolean(P.IS_WORK_DIALOG,isWorkDialog);
        dialogSaveWorkName.setArguments(bundle);
        return dialogSaveWorkName;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "DialogSaveNameWork onCreate... ");
        //positionCategory = getArguments().getInt(P.POSITION_CATEGORY);
        //positionType = getArguments().getInt(P.POSITION_TYPE);
        cat_id = getArguments().getLong(P.ID_CATEGORY);
        type_id = getArguments().getLong(P.ID_TYPE);
        isWorkDialog = getArguments().getBoolean(P.IS_WORK_DIALOG);
        Log.d(TAG, "DialogSaveNameWork onCreate.  cat_id = " + cat_id +
                "  type_id = " + type_id + "  isWorkDialog = " + isWorkDialog);
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

        TextView tvCatName = view.findViewById(R.id.tvCatName);
        TextView tvTypeName = view.findViewById(R.id.tvTypeName);
        final String typeName;
        final String catName;
        if (isWorkDialog){
            Log.d(TAG, "DialogSaveNameWork onCreateDialog. isWorkDialog = true  cat_id = " +
            cat_id + "  type_id = " + type_id);
            typeName = TypeWork.getNameFromId(database, type_id);
            catName = CategoryWork.getNameFromId(database, cat_id);
        }else{
            Log.d(TAG, "DialogSaveNameWork onCreateDialog. isWorkDialog = false  cat_id = " +
                    cat_id + "  type_id = " + type_id);
            typeName = TypeMat.getNameFromId(database, type_id);
            catName = CategoryMat.getNameFromId(database, cat_id);
        }
        tvCatName.setText(catName);
        tvTypeName.setText(typeName);

        EditText etSaveNameType = view.findViewById(R.id.etSaveNameType);
        etSaveNameType.setVisibility(View.GONE);

        EditText etSaveNameCat = view.findViewById(R.id.etSaveNameCat);
        etSaveNameCat.setVisibility(View.GONE);

        Button buttonSaveName = view.findViewById(R.id.buttonSaveName);
        buttonSaveName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String workName = etWorkName.getText().toString();

                //++++++++++++++++++   проверяем, есть ли такое имя   +++++++++++++//
                long workId;
                if (isWorkDialog){
                    workId = Work.getIdFromName(database, workName);
                    Log.d(TAG, "workName = " + workName + "  workId = " + workId);
                }else{
                    workId = Mat.getIdFromName(database, workName);
                    Log.d(TAG, "workName = " + workName + "  workId = " + workId);
                }
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
