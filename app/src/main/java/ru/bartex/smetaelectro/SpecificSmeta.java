package ru.bartex.smetaelectro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.TableControllerSmeta;

public class SpecificSmeta extends AppCompatActivity {

    static String TAG = "33333";

    TextView tvSmetaName;
    TextView tvSmetaDescription;
    TextView tvObjectAdress;
    TextView tvDateTime;
    Button btnOk;
    long file_id;
    DataFile dataFile;

    TableControllerSmeta tableControllerSmeta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smeta_specific);

        tableControllerSmeta  = new TableControllerSmeta(this);

        //получаем id выбранного файла из интента
        file_id = getIntent().getExtras().getLong(P.ID_FILE);
        Log.d(TAG, "SpecificSmeta onCreate file_id = " + file_id);
        dataFile = new TableControllerSmeta(this).getFileData(file_id);

        tvSmetaName = findViewById(R.id.tvName);
        tvSmetaName.setText(dataFile.getFileName());
        Log.d(TAG, "SpecificSmeta onCreate tvSmetaName = " + dataFile.getFileName());

        tvSmetaDescription = findViewById(R.id.tvDescription);
        tvSmetaDescription.setText(dataFile.getDescription());
        Log.d(TAG, "SpecificSmeta onCreate tvSmetaDescription = " + dataFile.getDescription());

        tvObjectAdress = findViewById(R.id.tvAdress);
        tvObjectAdress.setText(dataFile.getAdress());
        Log.d(TAG, "SpecificSmeta onCreate tvObjectAdress = " + dataFile.getAdress());

        tvDateTime = findViewById(R.id.tvDateTime);
        tvDateTime.setText(dataFile.getFileNameDate() + "_" + dataFile.getFileNameTime());
        Log.d(TAG, "SpecificSmeta onCreate tvDateTime = " +
                dataFile.getFileNameDate() + "_" + dataFile.getFileNameTime());

        btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
