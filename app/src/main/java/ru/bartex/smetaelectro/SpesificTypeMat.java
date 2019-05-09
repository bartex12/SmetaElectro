package ru.bartex.smetaelectro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;

public class SpesificTypeMat extends AppCompatActivity {
    public static final String TAG = "33333";
    TextView tvTypeMatName;
    TextView tvTypeMatDescription;
    Button btnOkType;
    long type_mat_id;
    DataTypeMat dataTypeMat;
    private SmetaOpenHelper smetaOpenHelper = new SmetaOpenHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_specific);

        //получаем id выбранного файла из интента
        type_mat_id = getIntent().getExtras().getLong(P.ID_TYPE_MAT);
        Log.d(TAG, "SpesificTypeMat onCreate type_mat_id = " + type_mat_id);
        dataTypeMat = smetaOpenHelper.getTypeMatData(type_mat_id);

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

