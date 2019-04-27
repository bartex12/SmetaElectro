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

public class WorkChangeData extends AppCompatActivity {

    public static final String TAG = "33333";

    EditText etWorkName;
    EditText etWorkDescription;
    Button btnCancelChangeWork;
    Button btnSaveChangeWork;
    long work_id;

    private SmetaOpenHelper smetaOpenHelper = new SmetaOpenHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_change_data);


        //получаем id выбранного типа работы из интента
        work_id = getIntent().getExtras().getLong(P.ID_WORK);
        Log.d(TAG, "WorkChangeData onCreate work_id = " + work_id);
        DataWork dataWork  = smetaOpenHelper.getWorkData(work_id);

        etWorkName = findViewById(R.id.etChangeWorkName);
        etWorkName.setText(dataWork.getmWorkName());

        etWorkDescription = findViewById(R.id.etChangeWorkDescription);
        etWorkDescription.setText(dataWork.getmWorkDescription());
        Log.d(TAG, "WorkChangeData onCreate etWorkDescription = " + etWorkDescription.getText().toString());

        btnCancelChangeWork = findViewById(R.id.btnCancelChangeWork);
        btnCancelChangeWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //принудительно прячем  клавиатуру - повторный вызов ее покажет
                //takeOnAndOffSoftInput();
                finish();
            }
        });
        btnSaveChangeWork = findViewById(R.id.btnSaveChangeWork);
        btnSaveChangeWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //читаем имя типа работы в строке ввода
                String nameWork = etWorkName.getText().toString();
                Log.d(TAG, "WorkChangeData nameWork = " + nameWork);

                //++++++++++++++++++   проверяем, пустое ли имя   +++++++++++++//
                //если имя - пустая строка
                if (nameWork.trim().isEmpty()) {
                    //Чтобы Snackbar появлялся над клавиатурой, в манифесте в активности
                    // написано android:windowSoftInputMode="adjustResize"
                    Snackbar.make(v, "Введите непустое название работы",
                            Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    Log.d(TAG, "Введите непустое название работы ");
                    return;
                    //если имя  не пустое то
                } else {
                    Log.d(TAG, " OK, Название не пустое");
                    String description = etWorkDescription.getText().toString();

                    //обновляем данные типа работы
                    smetaOpenHelper.updateWorkData(work_id, nameWork, description);

                    Toast.makeText(WorkChangeData.this,"Обновлено ",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            ;
        });
    }
}
