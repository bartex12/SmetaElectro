package ru.bartex.smetaelectro;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class DialogNewOrCurrentFragment extends DialogFragment {

    static String TAG = "33333";
    public DialogNewOrCurrentFragment(){}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater =getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_new_or_current_smeta, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        builder.setTitle("Выберите вариант");
        //builder.setIcon(R.drawable.ic_save_black_24dp);

        Button newButton = view.findViewById(R.id.btnNew);
        //действия при нажатии кнопки "Новая смета" в диалоге
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().finish(); //закрывает и диалог и активность
                //getDialog().dismiss();  //закрывает только диалог
                Intent intent = new Intent(getActivity(), SmetaNewName.class);
                startActivity(intent);
                getDialog().dismiss();  //закрывает только диалог
            }
        });

        Button currentButton = view.findViewById(R.id.btnCurrent);
        //действия при нажатии кнопки "Нет" в диалоге сохранения данных в базу
        currentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().finish(); //закрывает и диалог и активность
                //getDialog().dismiss();  //закрывает только диалог
                Intent intent = new Intent(getActivity(), ListOfSmetasNames.class);
                //intent.putExtra(P.ID_FILE_DEFAULT, file_id);
                startActivity(intent);
                getDialog().dismiss();  //закрывает только диалог
            }
        });
        //если не делать запрет на закрытие окна при щелчке за пределами окна, то можно так
        //return bilder.create();
        //А если делать запрет, то так
        Dialog  dialog = builder.create();
        //запрет на закрытие окна при щелчке за пределами окна - false
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }
}
