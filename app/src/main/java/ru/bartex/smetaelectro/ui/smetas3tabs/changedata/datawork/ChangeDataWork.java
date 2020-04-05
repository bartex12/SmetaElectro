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
import ru.bartex.smetaelectro.data.DataWork;
import ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.database.SmetaOpenHelper;
import ru.bartex.smetaelectro.database.work.Work;


public class ChangeDataWork extends AppCompatActivity {

    public static final String TAG = "33333";

    EditText etWorkName;
    EditText etWorkDescription;
    Button btnCancelChangeWork;
    Button btnSaveChangeWork;
    long work_id;
    DataWork dataWork;

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_change_data);

        initDB();

        //получаем id выбранного типа работы из интента
        work_id = getIntent().getExtras().getLong(P.ID_WORK);
        Log.d(TAG, "ChangeDataWork onCreate work_id = " + work_id);
        dataWork = Work.getDataWork(database, work_id);

        initViews();
    }

    private void initDB() {
        //
        database = new SmetaOpenHelper(this).getWritableDatabase();
    }

    private void initViews() {
        etWorkName = findViewById(R.id.etChangeWorkName);
        etWorkName.setText(dataWork.getmWorkName());

        etWorkDescription = findViewById(R.id.etChangeWorkDescription);
        etWorkDescription.setText(dataWork.getmWorkDescription());
        Log.d(TAG, "ChangeDataWork onCreate etWorkDescription = " + etWorkDescription.getText().toString());

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
                Log.d(TAG, "ChangeDataWork nameWork = " + nameWork);

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
                    Work.updateData(database, work_id, nameWork, description);

                    Toast.makeText(ChangeDataWork.this,"Обновлено ",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
