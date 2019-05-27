package ru.bartex.smetaelectro;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.TableControllerSmeta;

public class ChangeDataSmetaName extends AppCompatActivity {

    static String TAG = "33333";

    LinearLayout linearLayout;
    EditText etSmetaName;
    EditText etSmetaDescription;
    EditText etObjectAdress;
    CheckBox checkBoxDateTime;
    Button btnCancel;
    Button btnSave;
    long file_id;

    private SmetaOpenHelper smetaOpenHelper = new SmetaOpenHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smeta_new_name);

        //получаем id выбранного файла из интента
        file_id = getIntent().getExtras().getLong(P.ID_FILE);
        Log.d(TAG, "ChangeDataSmetaName onCreate file_id = " + file_id);
        DataFile dataFile = new TableControllerSmeta(this).getFileData(file_id);

        //InputMethodManager imm = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        linearLayout = findViewById(R.id.llNewFileName);

        etSmetaName = findViewById(R.id.etNameOfSmetaFile);
        etSmetaName.setText(dataFile.getFileName());

        etSmetaDescription = findViewById(R.id.etSmetaShortDescription);
        etSmetaDescription.setText(dataFile.getDescription());

        etObjectAdress = findViewById(R.id.etAdressOfObject);
        etObjectAdress.setText(dataFile.getAdress());

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
                    Log.d(TAG, "ChangeDataSmetaName date.isChecked() Имя файла = " + nameFile);
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
                    //если имя  не пустое то
                } else {
                    Log.d(TAG, " OK, Такое имя отсутствует");
                    String adress = etObjectAdress.getText().toString();
                    String description = etSmetaDescription.getText().toString();

                    //обновляем данные файла
                    smetaOpenHelper.updateFileData(file_id, nameFile, adress, description);

                    Toast.makeText(ChangeDataSmetaName.this,"Обновлено ",
                           Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            ;
        });
    }
}
