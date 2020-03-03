package ru.bartex.smetaelectro;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.TableControllerSmeta;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.files.FileWork;

public class SmetaNewName extends AppCompatActivity {

    static String TAG = "33333";

    LinearLayout linearLayout;
    EditText etSmetaName;
    EditText etSmetaDescription;
    EditText etObjectAdress;
    CheckBox checkBoxDateTime;
    Button btnCancel;
    Button btnSave;

    private TableControllerSmeta tableControllerSmeta;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smeta_new_name);

        initDB();

        tableControllerSmeta  = new TableControllerSmeta(this);

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
                long fileId = FileWork.getIdFromName(database, nameFile);
                Log.d(TAG, "nameFile = " + nameFile + "  fileId = " + fileId);

                //если имя - пустая строка
                if (nameFile.trim().isEmpty()) {
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

                    long file_id = FileWork.addFile(database, nameFile, adress, description);
                    Toast.makeText(SmetaNewName.this,"Сохранено ",
                                 Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Сохранено имя файла  = " + nameFile + "  id = " + file_id);

                    Intent intent = new Intent(SmetaNewName.this, ListOfSmetasNames.class);
                    //если бы вызывали Smetas, тогда надо было бы
                    //intent.putExtra(P.ID_FILE, file_id);
                    startActivity(intent);
                    finish();  //чтобы не возвращаться в эту активность по кнопке Назад
                }
            }

        });
    }

    private void initDB() {
        //
        database = new SmetaOpenHelper(this).getWritableDatabase();
    }

    //принудительно вызываем клавиатуру - повторный вызов ее скроет
    private void takeOnAndOffSoftInput(){
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
}
