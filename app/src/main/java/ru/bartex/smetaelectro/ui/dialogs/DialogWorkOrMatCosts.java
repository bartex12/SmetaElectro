package ru.bartex.smetaelectro.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.SmetasMatCost;
import ru.bartex.smetaelectro.SmetasWorkCost;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class DialogWorkOrMatCosts extends DialogFragment {

    static String TAG = "33333";

    public DialogWorkOrMatCosts(){}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater =getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_work_or_mat_cost, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                Intent intent = new Intent(getActivity(), SmetasWorkCost.class);
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
