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

import ru.bartex.smetaelectro.data.DataTypeMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.TableControllerSmeta;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.TypeMat;

public class ChangeDataTypeMat extends AppCompatActivity {
    static String TAG = "33333";

    EditText etTypeName;
    EditText etTypeDescription;
    Button btnCancelChangeType;
    Button btnSaveChangeType;
    long type_id;
    DataTypeMat dataTypeMat;

    private TableControllerSmeta tableControllerSmeta;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_change_data);

        initDB();

        tableControllerSmeta = new TableControllerSmeta(this);

        //получаем id выбранного типа материала из интента
        type_id = getIntent().getExtras().getLong(P.ID_TYPE_MAT);
        Log.d(TAG, "ChangeDataType onCreate type_id = " + type_id);
        dataTypeMat = TypeMat.getDataTypeMat(database, type_id);

        initViews();
    }

    private void initDB() {
        //
        database = new SmetaOpenHelper(this).getWritableDatabase();
    }

    private void initViews() {
        etTypeName = findViewById(R.id.etChangeTypeName);
        etTypeName.setText(dataTypeMat.getmTypeMatName());

        etTypeDescription = findViewById(R.id.etChangeTypeDescription);
        etTypeDescription.setText(dataTypeMat.getmTypeMatDescription());
        Log.d(TAG, "ChangeDataType onCreate etTypeDescription = " + etTypeDescription.getText().toString());

        btnCancelChangeType = findViewById(R.id.btnCancelChangeType);
        btnCancelChangeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //принудительно прячем  клавиатуру - повторный вызов ее покажет
                //takeOnAndOffSoftInput();
                finish();
            }
        });
        btnSaveChangeType = findViewById(R.id.btnSaveChangeType);
        btnSaveChangeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //читаем имя типа работы в строке ввода
                String nameType = etTypeName.getText().toString();
                Log.d(TAG, "ChangeDataType nameType = " + nameType);

                //++++++++++++++++++   проверяем, пустое ли имя   +++++++++++++//
                //если имя - пустая строка
                if (nameType.trim().isEmpty()) {
                    //Чтобы Snackbar появлялся над клавиатурой, в манифесте в активности
                    // написано android:windowSoftInputMode="adjustResize"
                    Snackbar.make(v, "Введите непустое название типа материала",
                            Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    Log.d(TAG, "Введите непустое название ");
                    return;
                    //если имя  не пустое то
                } else {
                    Log.d(TAG, " OK, Название не пустое");
                    String description = etTypeDescription.getText().toString();

                    //обновляем данные типа работы
                    TypeMat.updateData(database, type_id, nameType, description);

                    Toast.makeText(ChangeDataTypeMat.this,"Обновлено ",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

        });
    }
}
