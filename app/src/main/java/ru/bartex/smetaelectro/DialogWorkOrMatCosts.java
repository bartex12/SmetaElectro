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

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;

public class DialogWorkOrMatCosts extends DialogFragment {

    static String TAG = "33333";

    public DialogWorkOrMatCosts(){}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater =getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_work_or_mat_cost, null);
        builder.setView(view);
        builder.setTitle("Выберите вариант");
        //builder.setIcon(R.drawable.ic_save_black_24dp);

        Button newButton = view.findViewById(R.id.btnWork);
        //действия при нажатии кнопки "Новая смета" в диалоге
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().finish(); //закрывает и диалог и активность
                //getDialog().dismiss();  //закрывает только диалог
                //Intent intent_costs = new Intent(SmetasMat.this, CostCategory.class);
                // startActivity(intent_costs);
                Intent intent = new Intent(getActivity(), CostCategory.class);
                startActivity(intent);
                getDialog().dismiss();  //закрывает только диалог
            }
        });

        Button currentButton = view.findViewById(R.id.btnCost);
        //действия при нажатии кнопки "Нет" в диалоге сохранения данных в базу
        currentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().finish(); //закрывает и диалог и активность
                //getDialog().dismiss();  //закрывает только диалог
                Intent intent = new Intent(getActivity(), SmetasMatCost.class);
                startActivity(intent);
                getDialog().dismiss();  //закрывает только диалог
            }
        });
        //если не делать запрет на закрытие окна при щелчке за пределами окна, то можно так
        //return bilder.create();
        //А если делать запрет, то так
        //Dialog  dialog = builder.create();
        //запрет на закрытие окна при щелчке за пределами окна
        //dialog.setCanceledOnTouchOutside(false);
        return builder.create();
    }
}
