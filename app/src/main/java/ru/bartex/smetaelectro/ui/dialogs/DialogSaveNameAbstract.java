package ru.bartex.smetaelectro.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import ru.bartex.smetaelectro.database.SmetaOpenHelper;

import android.view.inputmethod.InputMethodManager;



abstract public class DialogSaveNameAbstract extends DialogFragment {

    static String TAG = "33333";
    public SQLiteDatabase database;
    WorkCategoryTypeNameListener workCategoryTypeNameListener;

    public DialogSaveNameAbstract(){
        //пустой конструктор
    }

    public interface WorkCategoryTypeNameListener{
        void workCategoryTypeNameTransmit(String workName,String typeName, String catName);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        workCategoryTypeNameListener = (WorkCategoryTypeNameListener)context;
        database = new SmetaOpenHelper(context).getWritableDatabase();
    }

    //абстрактный класс
    @NonNull
    abstract public Dialog onCreateDialog(Bundle savedInstanceState);


    //принудительно вызываем клавиатуру - повторный вызов ее скроет
    public void takeOnAndOffSoftInput(){
        InputMethodManager imm = (InputMethodManager) getActivity().
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    //окончание диалога с убиранием клавиатуры
    void fiishDialog(){
        //getActivity().finish(); //закрывает и диалог и активность
        getDialog().dismiss();  //закрывает только диалог
        takeOnAndOffSoftInput();
    }

}
