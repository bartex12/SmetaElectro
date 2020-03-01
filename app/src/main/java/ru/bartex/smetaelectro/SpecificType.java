package ru.bartex.smetaelectro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.TableControllerSmeta;

public class SpecificType extends AppCompatActivity {

    public static final String TAG = "33333";
    TextView tvTypeName;
    TextView tvTypeDescription;
    Button btnOkType;
    long type_id;
    DataType dataType;

    TableControllerSmeta tableControllerSmeta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_specific);

        tableControllerSmeta  = new TableControllerSmeta(this);

        //получаем id выбранного файла из интента
        type_id = getIntent().getExtras().getLong(P.ID_TYPE);
        Log.d(TAG, "SpecificType onCreate type_id = " + type_id);
        dataType = tableControllerSmeta.getDataType(type_id);

        tvTypeName = findViewById(R.id.tvName);
        tvTypeName.setText(dataType.getmTypeName());
        Log.d(TAG, "SpecificType onCreate tvTypeName = " + dataType.getmTypeName());

        tvTypeDescription = findViewById(R.id.tvDescription);
        tvTypeDescription.setText(dataType.getmTypeDescription());
        Log.d(TAG, "SpecificType onCreate tvTypeDescription = " + dataType.getmTypeDescription());

        btnOkType = findViewById(R.id.btnOk);
        btnOkType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

