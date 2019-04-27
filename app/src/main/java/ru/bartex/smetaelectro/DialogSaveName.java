package ru.bartex.smetaelectro;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.inputmethod.InputMethodManager;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;

abstract public class DialogSaveName extends DialogFragment {

    //int positionCategory;
    //int  positionType;
    //long cat_id;
    //long type_id;
    static String TAG = "33333";
    SmetaOpenHelper smetaOpenHelper;

    public DialogSaveName(){
        //пустой конструктор
    }

    public interface WorkCategoryTypeNameListener{
        void workCategoryTypeNameTransmit(String workName,String typeName, String catName);
    }
    WorkCategoryTypeNameListener workCategoryTypeNameListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        smetaOpenHelper = new SmetaOpenHelper(context);
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
