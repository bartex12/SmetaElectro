package ru.bartex.smetaelectro;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.FileWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.TableControllerSmeta;

public class SmetaNewNameChange extends SmetaNewName {

    long file_id;
    TableControllerSmeta tableControllerSmeta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //получаем id выбранного файла из интента
        file_id = getIntent().getExtras().getLong(P.ID_FILE);
        Log.d(TAG, "SmetaNewNameChange onCreate file_id = " + file_id);

        tableControllerSmeta = new TableControllerSmeta(this);
        DataFile dataFile = tableControllerSmeta.getFileData(file_id);

        etSmetaName.setText(dataFile.getFileName());
        etSmetaDescription.setText(dataFile.getDescription());
        etObjectAdress.setText(dataFile.getAdress());
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //читаем имя файла в строке ввода
                String nameFile = etSmetaName.getText().toString();

                if (checkBoxDateTime.isChecked()) {
                    nameFile = nameFile + "_" + P.setDateTimeString();
                    Log.d(TAG, "SmetaNewNameChange date.isChecked() Имя файла = " + nameFile);
                }

                //++++++++++++++++++   проверяем, есть ли такое имя   +++++++++++++//
                long fileId = tableControllerSmeta.getIdFromName(nameFile, FileWork.TABLE_NAME);
                Log.d(TAG, "nameFile = " + nameFile + "  fileId = " + fileId);

                //если имя - пустая строка
                if (nameFile.trim().isEmpty()) {
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
                    tableControllerSmeta.updateFileData(file_id, nameFile, adress, description);

                    Toast.makeText(SmetaNewNameChange.this,"Обновлено ",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
