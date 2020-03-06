package ru.bartex.smetaelectro.ui.smetabefore;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.data.DataFile;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.files.FileWork;

public class SmetaSpecification extends AppCompatActivity {

    static String TAG = "33333";

    private TextView tvSmetaName;
    private TextView tvSmetaDescription;
    private TextView tvObjectAdress;
    private TextView tvDateTime;
    private Button btnOk;
    private long file_id;
    private  DataFile dataFile;

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smeta_specific);

        initDB();

        //получаем id выбранного файла из интента
        file_id = getIntent().getExtras().getLong(P.ID_FILE);
        Log.d(TAG, "SmetaSpecification onCreate file_id = " + file_id);
        dataFile = FileWork.getFileData(database, file_id);

        initViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }

    private void initDB() {
        // вызываем здесь, закрываем в onDestroy()
        database = new SmetaOpenHelper(this).getWritableDatabase();
    }

    private void initViews() {
        tvSmetaName = findViewById(R.id.tvName);
        tvSmetaName.setText(dataFile.getFileName());
        Log.d(TAG, "SmetaSpecification onCreate tvSmetaName = " + dataFile.getFileName());

        tvSmetaDescription = findViewById(R.id.tvDescription);
        tvSmetaDescription.setText(dataFile.getDescription());
        Log.d(TAG, "SmetaSpecification onCreate tvSmetaDescription = " + dataFile.getDescription());

        tvObjectAdress = findViewById(R.id.tvAdress);
        tvObjectAdress.setText(dataFile.getAdress());
        Log.d(TAG, "SmetaSpecification onCreate tvObjectAdress = " + dataFile.getAdress());

        tvDateTime = findViewById(R.id.tvDateTime);
        tvDateTime.setText(dataFile.getFileNameDate() + "_" + dataFile.getFileNameTime());
        Log.d(TAG, "SmetaSpecification onCreate tvDateTime = " +
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
