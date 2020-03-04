package ru.bartex.smetaelectro;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.bartex.smetaelectro.data.DataMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.Mat;

public class SpesificMat extends AppCompatActivity {
    public static final String TAG = "33333";
    TextView tvMatName;
    TextView tvMatDescription;
    Button btnOkMat;
    long mat_id;
    DataMat dataMat;

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_specific);

        initDB();

        //получаем id выбранного файла из интента
        mat_id = getIntent().getExtras().getLong(P.ID_MAT);
        Log.d(TAG, "SpesificMat onCreate mat_id = " + mat_id);
        dataMat = Mat.getDataMat(database, mat_id);

        initViews();
    }

    private void initDB() {
        //
        database = new SmetaOpenHelper(this).getWritableDatabase();
    }

    private void initViews() {
        tvMatName = findViewById(R.id.tvName);
        tvMatName.setText(dataMat.getmMatName());
        Log.d(TAG, "SpesificMat onCreate tvMatName = " + dataMat.getmMatName());

        tvMatDescription = findViewById(R.id.tvDescription);
        tvMatDescription.setText(dataMat.getmMatDescription());
        Log.d(TAG, "SpesificMat onCreate tvMatDescription = " + dataMat.getmMatDescription());

        btnOkMat = findViewById(R.id.btnOk);
        btnOkMat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
