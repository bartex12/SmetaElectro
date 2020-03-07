package ru.bartex.smetaelectro.ui.smetabefore;

import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import ru.bartex.smetaelectro.data.DataFile;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.files.FileWork;

public class SmetaNewNameChange extends SmetaNewName {

    long file_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //получаем id выбранного файла из интента
        file_id = getIntent().getExtras().getLong(P.ID_FILE);
        Log.d(TAG, "SmetaNewNameChange onCreate file_id = " + file_id);

        DataFile dataFile = FileWork.getFileData(database, file_id);

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
                    //если имя  не пустое то
                } else {
                    Log.d(TAG, " OK, Такое имя отсутствует");
                    String adress = etObjectAdress.getText().toString();
                    String description = etSmetaDescription.getText().toString();

                    //обновляем данные файла
                    FileWork.updateDataFile(database, file_id, nameFile, adress, description);

                    Toast.makeText(SmetaNewNameChange.this,"Обновлено ",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

}