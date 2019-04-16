package ru.bartex.smetaelectro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;

public class TypeSpecific extends AppCompatActivity {

    public static final String TAG = "33333";
    TextView tvTypeName;
    TextView tvTypeDescription;
    Button btnOkType;
    long type_id;
    DataType dataType;

    private SmetaOpenHelper smetaOpenHelper = new SmetaOpenHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_specific);


        //получаем id выбранного файла из интента
        type_id = getIntent().getExtras().getLong(P.ID_TYPE);
        Log.d(TAG, "TypeSpecific onCreate type_id = " + type_id);
        dataType = smetaOpenHelper.getTypeData(type_id);

        tvTypeName = findViewById(R.id.tvNameType);
        tvTypeName.setText(dataType.getmTypeName());
        Log.d(TAG, "TypeSpecific onCreate tvTypeName = " + dataType.getmTypeName());

        tvTypeDescription = findViewById(R.id.tvDescriptionType);
        tvTypeDescription.setText(dataType.getmTypeDescription());
        Log.d(TAG, "TypeSpecific onCreate tvTypeDescription = " + dataType.getmTypeDescription());

        btnOkType = findViewById(R.id.btnOkType);
        btnOkType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

