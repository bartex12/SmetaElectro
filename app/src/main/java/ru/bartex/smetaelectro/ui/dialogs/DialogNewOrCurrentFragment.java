package ru.bartex.smetaelectro.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import ru.bartex.smetaelectro.ListOfSmetasNames;
import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.SmetaNewName;

import android.view.View;
import android.widget.Button;

public class DialogNewOrCurrentFragment extends DialogFragment {

    public DialogNewOrCurrentFragment(){}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final View view = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_new_or_current_smeta, null);

        setBuilderParams(builder, view);
        initNewButton(view);
        initCurrentButton(view);

        //если не делать запрет на закрытие окна при щелчке за пределами окна, то можно так
        //return bilder.create();
        //А если делать запрет, то так
        Dialog  dialog = builder.create();
        //запрет на закрытие окна при щелчке за пределами окна - false
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    private void setBuilderParams(AlertDialog.Builder builder, View view) {
        builder.setView(view);
        builder.setTitle(R.string.chooce_var_smeta);
        builder.setIcon(R.drawable.ic_device_hub_black_24dp);
    }

    private void initNewButton(View view) {
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
    }

    private void initCurrentButton(View view) {
        Button currentButton = view.findViewById(R.id.btnCurrent);
        //действия при нажатии кнопки "Нет" в диалоге сохранения данных в базу
        currentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListOfSmetasNames.class);
                //intent.putExtra(P.ID_FILE_DEFAULT, file_id);
                startActivity(intent);
                getDialog().dismiss();  //закрывает только диалог
            }
        });
    }
}
