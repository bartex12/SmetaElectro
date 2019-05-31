package ru.bartex.smetaelectro;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.TableControllerSmeta;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.TypeWork;

public class ChangeDataType extends AppCompatActivity {

    static String TAG = "33333";

    EditText etTypeName;
    EditText etTypeDescription;
    Button btnCancelChangeType;
    Button btnSaveChangeType;
    long type_id;

    private TableControllerSmeta tableControllerSmeta;
    private SmetaOpenHelper smetaOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_change_data);

        smetaOpenHelper = new SmetaOpenHelper(this);
        tableControllerSmeta = new TableControllerSmeta(this);

        //получаем id выбранного типа работы из интента
        type_id = getIntent().getExtras().getLong(P.ID_TYPE);
        Log.d(TAG, "ChangeDataType onCreate type_id = " + type_id);
        DataType dataType = smetaOpenHelper.getTypeData(type_id);

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
                    tableControllerSmeta.updateData(type_id, nameType, description, TypeWork.TABLE_NAME);

                    Toast.makeText(ChangeDataType.this,"Обновлено ",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            ;
        });
    }
}
