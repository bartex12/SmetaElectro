package ru.bartex.smetaelectro.ui.smetas3tabs.changedata.datawork;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.data.DataCategory;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.CategoryWork;

public class ChangeDataCategory extends AppCompatActivity {

    static String TAG = "33333";

    EditText etCatName;
    EditText etCatDescription;
    Button btnCancelChangeCat;
    Button btnSaveChangeCat;
    long cat_id;
    DataCategory dataCategory;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_change_data);

        initDB();

        //получаем id выбранного файла из интента
        cat_id = getIntent().getExtras().getLong(P.ID_CATEGORY);
        Log.d(TAG, "ChangeDataCategory onCreate cat_id = " + cat_id);
        dataCategory = CategoryWork.getDataCategory(database, cat_id);

        initViews();
    }

    private void initDB() {
        //
        database = new SmetaOpenHelper(this).getWritableDatabase();
    }

    private void initViews() {
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
                    CategoryWork.updateData(database, cat_id, nameCat, description);

                    Toast.makeText(ChangeDataCategory.this,"Обновлено ",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

        });
    }

    }

