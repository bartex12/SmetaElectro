package ru.bartex.smetaelectro;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;

public class DialogSaveWorkName extends DialogFragment {

    int positionCategory;
    int  positionType;
    long cat_id;
    static String TAG = "33333";
    SmetaOpenHelper smetaOpenHelper;
    String selCategoryName = "";
    String selTypeName = "";


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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
        View view = inflater.inflate(R.layout.dialog_save_work_name, null);
        builder.setView(view);

        builder.setTitle("Сохранить работу");
        builder.setIcon(R.drawable.ic_save_black_24dp);

        EditText workName = view.findViewById(R.id.etWorkName);
        workName.requestFocus();
        workName.setInputType(InputType.TYPE_CLASS_TEXT);

        final String[] arrayTypeNames = smetaOpenHelper.getArrayTypeNames(cat_id);
        Spinner  spinnerType = view.findViewById(R.id.spinnerTypeName);
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, arrayTypeNames);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapterType);
        spinnerType.setSelection(positionType);

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selTypeName = arrayTypeNames[position];
                Log.d(TAG, "onItemSelected selTypeName = " + selTypeName);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final String[] arrayCatNames = smetaOpenHelper.getArrayCategoryNames();
        Spinner  spinnerCat  = view.findViewById(R.id.spinnerCatName);
        ArrayAdapter<String> adapterCat = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, arrayCatNames);
        adapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCat.setAdapter(adapterCat);
        spinnerCat.setSelection(positionCategory);

        spinnerCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selCategoryName = arrayCatNames[position];
                Log.d(TAG, "onItemSelected selCategoryName = " + selCategoryName);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return builder.create();
    }

    //принудительно вызываем клавиатуру - повторный вызов ее скроет
    private void takeOnAndOffSoftInput(){
        InputMethodManager imm = (InputMethodManager) getActivity().
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
}
