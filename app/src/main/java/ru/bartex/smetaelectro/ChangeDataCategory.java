package ru.bartex.smetaelectro;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.bartex.smetaelectro.data.DataCategory;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.CategoryWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.TableControllerSmeta;

public class ChangeDataCategory extends AppCompatActivity {

    static String TAG = "33333";

    EditText etCatName;
    EditText etCatDescription;
    Button btnCancelChangeCat;
    Button btnSaveChangeCat;
    long cat_id;

    private TableControllerSmeta tableControllerSmeta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_change_data);

        tableControllerSmeta = new TableControllerSmeta(this);

        //получаем id выбранного файла из интента
        cat_id = getIntent().getExtras().getLong(P.ID_CATEGORY);
        Log.d(TAG, "ChangeDataCategory onCreate cat_id = " + cat_id);
        DataCategory dataCategory = tableControllerSmeta.getDataCategory(cat_id);

        etCatName = findViewById(R.id.etChangeCatName);
        etCatName.setText(dataCategory.getmCategoryName());

        etCatDescription = findViewById(R.id.etChangeCatDescription);
        etCatDescription.setText(dataCategory.getmCategoryDescription());
        Log.d(TAG, "ChangeDataCategory onCreate etCatDescription = " + etCatDescription.getText().toString());

        btnCancelChangeCat = findViewById(R.id.btnCancelChangeCat);
        btnCancelChangeCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //принудительно прячем  клавиатуру - повторный вызов ее покажет
                //takeOnAndOffSoftInput();
                finish();
            }
        });
        btnSaveChangeCat = findViewById(R.id.btnSaveChangeCat);
        btnSaveChangeCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //читаем имя файла в строке ввода
                String nameCat = etCatName.getText().toString();
                Log.d(TAG, "ChangeDataCategory nameCat = " + nameCat);

                //++++++++++++++++++   проверяем, пустое ли имя   +++++++++++++//
               // long catId = smetaOpenHelper.getIdFromCategoryName(nameCat);
                //Log.d(TAG, "nameCat = " + nameCat + "  catId = " + catId);

                //если имя - пустая строка
                if (nameCat.trim().isEmpty()) {
                    //Чтобы Snackbar появлялся над клавиатурой, в манифесте в активности
                    // написано android:windowSoftInputMode="adjustResize"
                    Snackbar.make(v, "Введите непустое название категории",
                            Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    Log.d(TAG, "Введите непустое название");
                    return;
                    //если имя  не пустое то
                } else {
                    Log.d(TAG, " OK, Название не пустое");
                    String description = etCatDescription.getText().toString();

                    //обновляем данные файла
                    tableControllerSmeta.updateData(cat_id, nameCat, description, CategoryWork.TABLE_NAME);

                    Toast.makeText(ChangeDataCategory.this,"Обновлено ",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            ;
        });
    }

    }

