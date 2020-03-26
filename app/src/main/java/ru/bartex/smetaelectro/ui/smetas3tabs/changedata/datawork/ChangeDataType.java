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
import ru.bartex.smetaelectro.data.DataType;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.TypeWork;

public class ChangeDataType extends AppCompatActivity {

    static String TAG = "33333";

    EditText etTypeName;
    EditText etTypeDescription;
    Button btnCancelChangeType;
    Button btnSaveChangeType;
    long type_id;

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_change_data);

        initDB();

        //получаем id выбранного типа работы из интента
        type_id = getIntent().getExtras().getLong(P.ID_TYPE);
        Log.d(TAG, "ChangeDataType onCreate type_id = " + type_id);
        DataType dataType = TypeWork.getDataType(database, type_id);

        initViews(dataType);
    }

    private void initDB() {
        //
        database = new SmetaOpenHelper(this).getWritableDatabase();
    }

    private void initViews(DataType dataType) {
        etTypeName = findViewById(R.id.etChangeTypeName);
        etTypeName.setText(dataType.getmTypeName());

        etTypeDescription = findViewById(R.id.etChangeTypeDescription);
        etTypeDescription.setText(dataType.getmTypeDescription());
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
                    Snackbar.make(v, "Введите непустое название типа работы",
                            Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    Log.d(TAG, "Введите непустое название типа работы ");
                    return;
                    //если имя  не пустое то
                } else {
                    Log.d(TAG, " OK, Название не пустое");
                    String description = etTypeDescription.getText().toString();

                    //обновляем данные типа работы
                    TypeWork.updateData(database, type_id, nameType, description);

                    Toast.makeText(ChangeDataType.this,"Обновлено ",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

        });
    }

}
