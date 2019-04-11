package ru.bartex.smetaelectro;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;

public class DialogSaveCatName extends DialogFragment {

    static String TAG = "33333";
    SmetaOpenHelper smetaOpenHelper;

    public DialogSaveCatName(){};

    public interface CategoryWorkNameListener {
        void categoryWorkNameTransmit(String catName);
    }

    private CategoryWorkNameListener categoryWorkNameListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        categoryWorkNameListener = (CategoryWorkNameListener)context;
        smetaOpenHelper = new SmetaOpenHelper(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog... ");
        //принудительно вызываем клавиатуру - повторный вызов ее скроет
        takeOnAndOffSoftInput();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_save_cat_name, null);
        final EditText etCatName = view.findViewById(R.id.etCatName);
        etCatName.requestFocus();
        etCatName.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(view);
        builder.setTitle("Сохранить категорию");
        builder.setIcon(R.drawable.ic_save_black_24dp);

        Button btnSaveName = view.findViewById(R.id.buttonSaveCatName);
        btnSaveName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //читаем имя файла в строке ввода
                String nameCat = etCatName.getText().toString();
                Log.d(TAG, " onCreateDialog nameCat = " + nameCat);

                //++++++++++++++++++   проверяем, есть ли такое имя   +++++++++++++//
                long catId = smetaOpenHelper.getIdFromCategoryName(nameCat);
                Log.d(TAG, "nameCat = " + nameCat + "  catId = " +catId);

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
                    categoryWorkNameListener.categoryWorkNameTransmit(nameCat);

                    //getActivity().finish(); //закрывает и диалог и активность
                    getDialog().dismiss();  //закрывает только диалог
                    //принудительно прячем  клавиатуру - повторный вызов ее покажет
                    takeOnAndOffSoftInput();
                }

            }
        });

        Button btnCancel = view.findViewById(R.id.buttonCancelCatName);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getActivity().finish(); //закрывает и диалог и активность
                getDialog().dismiss();  //закрывает только диалог
                //принудительно прячем  клавиатуру - повторный вызов ее покажет
                takeOnAndOffSoftInput();
            }
        });

        //если не делать запрет на закрытие окна при щелчке за пределами окна, то можно так
        return builder.create();
        //А если делать запрет, то так
       // Dialog  dialog = builder.create();
        //запрет на закрытие окна при щелчке за пределами окна
        //dialog.setCanceledOnTouchOutside(false);
        //return dialog;
    }

    //принудительно вызываем клавиатуру - повторный вызов ее скроет
    private void takeOnAndOffSoftInput(){
        InputMethodManager imm = (InputMethodManager) getActivity().
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
}
