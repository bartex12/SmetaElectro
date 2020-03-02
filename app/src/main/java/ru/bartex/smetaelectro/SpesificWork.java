package ru.bartex.smetaelectro;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.bartex.smetaelectro.data.DataWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.TableControllerSmeta;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.Work;

public class SpesificWork extends AppCompatActivity {

    public static final String TAG = "33333";
    TextView tvWorkName;
    TextView tvWorkDescription;
    Button btnOkWork;
    long work_id;
    DataWork dataWork;

    TableControllerSmeta tableControllerSmeta;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_specific);

        initDB();

        tableControllerSmeta  = new TableControllerSmeta(this);

        //получаем id выбранного файла из интента
        work_id = getIntent().getExtras().getLong(P.ID_WORK);
        Log.d(TAG, "SpesificWork onCreate work_id = " + work_id);
        dataWork = Work.getDataWork(database, work_id);

        initViews();

    }

    private void initDB() {
        //
        database = new SmetaOpenHelper(this).getWritableDatabase();
    }

    private void initViews() {
        tvWorkName = findViewById(R.id.tvName);
        tvWorkName.setText(dataWork.getmWorkName());
        Log.d(TAG, "SpesificWork onCreate tvWorkName = " + dataWork.getmWorkName());

        tvWorkDescription = findViewById(R.id.tvDescription);
        tvWorkDescription.setText(dataWork.getmWorkDescription());
        Log.d(TAG, "SpesificWork onCreate tvWorkDescription = " + dataWork.getmWorkDescription());

        btnOkWork = findViewById(R.id.btnOk);
        btnOkWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
