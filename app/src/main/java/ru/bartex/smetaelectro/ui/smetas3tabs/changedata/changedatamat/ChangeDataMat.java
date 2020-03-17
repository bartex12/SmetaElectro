package ru.bartex.smetaelectro.ui.smetas3tabs.changedata.changedatamat;

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
import ru.bartex.smetaelectro.data.DataMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.Mat;

public class ChangeDataMat extends AppCompatActivity {
    public static final String TAG = "33333";

    EditText etWorkName;
    EditText etWorkDescription;
    Button btnCancelChangeWork;
    Button btnSaveChangeWork;
    long work_id;
    DataMat dataMat;

    private SQLiteDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_change_data);

        initDB();

        //получаем id выбранного материала из интента
        work_id = getIntent().getExtras().getLong(P.ID_MAT);
        Log.d(TAG, "ChangeDataMat onCreate work_id = " + work_id);
        dataMat = Mat.getDataMat(database, work_id);

        initViews();
    }

    private void initDB() {
        //
        database = new SmetaOpenHelper(this).getWritableDatabase();
    }

    private void initViews() {
        etWorkName = findViewById(R.id.etChangeWorkName);
        etWorkName.setText(dataMat.getmMatName());

        etWorkDescription = findViewById(R.id.etChangeWorkDescription);
        etWorkDescription.setText(dataMat.getmMatDescription());
        Log.d(TAG, "ChangeDataMat onCreate etWorkDescription = " + etWorkDescription.getText().toString());

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
                Log.d(TAG, "ChangeDataMat nameWork = " + nameWork);

                //++++++++++++++++++   проверяем, пустое ли имя   +++++++++++++//
                //если имя - пустая строка
                if (nameWork.trim().isEmpty()) {
                    //Чтобы Snackbar появлялся над клавиатурой, в манифесте в активности
                    // написано android:windowSoftInputMode="adjustResize"
                    Snackbar.make(v, "Введите непустое название материала",
                            Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    Log.d(TAG, "Введите непустое название ");
                    return;
                    //если имя  не пустое то
                } else {
                    Log.d(TAG, " OK, Название не пустое");
                    String description = etWorkDescription.getText().toString();

                    //обновляем данные типа работы
                    Mat.updateData(database, work_id, nameWork, description);

                    Toast.makeText(ChangeDataMat.this,"Обновлено ",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
