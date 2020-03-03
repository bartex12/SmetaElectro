package ru.bartex.smetaelectro;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.bartex.smetaelectro.data.DataCategoryMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.TableControllerSmeta;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.CategoryMat;

public class ChangeDataCategoryMat extends AppCompatActivity {
    static String TAG = "33333";

    EditText etCatName;
    EditText etCatDescription;
    Button btnCancelChangeCat;
    Button btnSaveChangeCat;
    long cat_id;
    DataCategoryMat dataCategoryMat;

    TableControllerSmeta tableControllerSmeta;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_change_data);

        initDB();

        tableControllerSmeta = new TableControllerSmeta(this);

        //получаем id выбранного файла из интента
        cat_id = getIntent().getExtras().getLong(P.ID_CATEGORY_MAT);
        Log.d(TAG, "ChangeDataCategoryMat onCreate cat_id = " + cat_id);
        dataCategoryMat = CategoryMat.getDataCategoryMat(database, cat_id);

        initViews();
    }

    private void initDB() {
        //
        database = new SmetaOpenHelper(this).getWritableDatabase();
    }

    private void initViews() {
        etCatName = findViewById(R.id.etChangeCatName);
        etCatName.setText(dataCategoryMat.getmCategoryMatName());

        etCatDescription = findViewById(R.id.etChangeCatDescription);
        etCatDescription.setText(dataCategoryMat.getmCategoryMatDescription());
        Log.d(TAG, "ChangeDataCategoryMat onCreate etCatDescription = " + etCatDescription.getText().toString());

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
                Log.d(TAG, "ChangeDataCategoryMat nameCat = " + nameCat);

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
                    CategoryMat.updateData(database, cat_id, nameCat, description);

                    Toast.makeText(ChangeDataCategoryMat.this,"Обновлено ",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

        });
    }
}
