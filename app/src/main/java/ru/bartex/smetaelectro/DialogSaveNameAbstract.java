package ru.bartex.smetaelectro;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.inputmethod.InputMethodManager;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.TableControllerSmeta;

abstract public class DialogSaveNameAbstract extends DialogFragment {

    static String TAG = "33333";
    TableControllerSmeta tableControllerSmeta;

    public DialogSaveNameAbstract(){
        //пустой конструктор
    }

    public interface WorkCategoryTypeNameListener{
        void workCategoryTypeNameTransmit(String workName,String typeName, String catName);
    }
    WorkCategoryTypeNameListener workCategoryTypeNameListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        tableControllerSmeta = new TableControllerSmeta(context);
        workCategoryTypeNameListener = (WorkCategoryTypeNameListener)context;
    }

    //абстрактный класс
    abstract public Dialog onCreateDialog(Bundle savedInstanceState);


    //принудительно вызываем клавиатуру - повторный вызов ее скроет
    public void takeOnAndOffSoftInput(){
        InputMethodManager imm = (InputMethodManager) getActivity().
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    //окончание диалога с убиранием клавиатуры
    public void fiishDialog(){
        //getActivity().finish(); //закрывает и диалог и активность
        getDialog().dismiss();  //закрывает только диалог
        takeOnAndOffSoftInput();
    }

}
