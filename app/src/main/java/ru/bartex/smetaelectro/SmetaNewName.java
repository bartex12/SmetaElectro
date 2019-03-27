package ru.bartex.smetaelectro;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;

public class SmetaNewName extends AppCompatActivity {

    static String TAG = "33333";

    LinearLayout linearLayout;
    EditText etSmetaName;
    EditText etSmetaDescription;
    EditText etObjectAdress;
    CheckBox checkBoxDateTime;
    Button btnCancel;
    Button btnSave;
    private SmetaOpenHelper smetaOpenHelper = new SmetaOpenHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smeta_new_name);

        //InputMethodManager imm = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        linearLayout = findViewById(R.id.llNewFileName);
        etSmetaName = findViewById(R.id.etNameOfSmetaFile);
        etSmetaDescription = findViewById(R.id.etSmetaShortDescription);
        etObjectAdress = findViewById(R.id.etAdressOfObject);
        checkBoxDateTime = findViewById(R.id.checkBoxDateTime);
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //принудительно прячем  клавиатуру - повторный вызов ее покажет
                //takeOnAndOffSoftInput();
                finish();
            }
        });
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //читаем имя файла в строке ввода
                String nameFile = etSmetaName.getText().toString();

                if (checkBoxDateTime.isChecked()) {
                    nameFile = nameFile + "_" + P.setDateTimeString();
                    Log.d(TAG, "SmetaNewName date.isChecked() Имя файла = " + nameFile);
                }

                //++++++++++++++++++   проверяем, есть ли такое имя   +++++++++++++//
                long fileId = smetaOpenHelper.getIdFromFileName(nameFile);
                Log.d(TAG, "nameFile = " + nameFile + "  fileId = " + fileId);

                //если имя - пустая строка
                if (nameFile.trim().isEmpty()) {
                    //Toast.makeText(SmetaNewName.this,"Введите непустое название сметы ",
                      //      Toast.LENGTH_LONG).show();

                    //Чтобы Snackbar появлялся над клавиатурой, в манифесте в активности
                    // написано android:windowSoftInputMode="adjustResize"
                   Snackbar.make(linearLayout, "Введите непустое название сметы",
                           Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    Log.d(TAG, "Введите непустое название сметы ");
                    return;

                    //если такое имя уже есть в базе
                } else if (fileId != -1) {
                    Snackbar.make(linearLayout, "Такое название уже существует." +
                                    " Введите другое название.",
                            Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    Log.d(TAG, "Такое название уже существует. Введите другое название. fileId = " + fileId);
                    return;

                    //если имя не повторяется, оно не пустое то
                } else {
                    Log.d(TAG, "Такое имя отсутствует fileId = " + fileId);
                    String adress = etObjectAdress.getText().toString();
                    String description = etSmetaDescription.getText().toString();

                    long file_id = smetaOpenHelper.addFile(nameFile,adress,description);
                    Toast.makeText(SmetaNewName.this,"Сохранено ",
                                 Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Сохранено имя файла  = " + nameFile + "  id = " + file_id);
                    finish();
                }
            }

            ;
        });
    }

    //принудительно вызываем клавиатуру - повторный вызов ее скроет
    private void takeOnAndOffSoftInput(){
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
}
