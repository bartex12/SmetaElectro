package ru.bartex.smetaelectro.ui.smetas3tabs.specific;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.data.DataTypeMat;
import ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.database.SmetaOpenHelper;
import ru.bartex.smetaelectro.database.mat.TypeMat;


public class SpesificTypeMat extends AppCompatActivity {
    public static final String TAG = "33333";
    TextView tvTypeMatName;
    TextView tvTypeMatDescription;
    Button btnOkType;
    long type_mat_id;
    DataTypeMat dataTypeMat;

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_specific);

        initDB();

        //получаем id выбранного файла из интента
        type_mat_id = getIntent().getExtras().getLong(P.ID_TYPE_MAT);
        Log.d(TAG, "SpesificTypeMat onCreate type_mat_id = " + type_mat_id);
        dataTypeMat = TypeMat.getDataTypeMat(database, type_mat_id);

        initViews();
    }

    private void initDB() {
        //
        database = new SmetaOpenHelper(this).getWritableDatabase();
    }

    private void initViews() {

        tvTypeMatName = findViewById(R.id.tvName);
        tvTypeMatName.setText(dataTypeMat.getmTypeMatName());
        Log.d(TAG, "SpesificTypeMat onCreate tvTypeMatName = " + dataTypeMat.getmTypeMatName());

        tvTypeMatDescription = findViewById(R.id.tvDescription);
        tvTypeMatDescription.setText(dataTypeMat.getmTypeMatDescription());
        Log.d(TAG, "SpesificTypeMat onCreate tvTypeDescription = " + dataTypeMat.getmTypeMatDescription());

        btnOkType = findViewById(R.id.btnOk);
        btnOkType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

