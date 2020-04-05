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
import ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.database.SmetaOpenHelper;
import ru.bartex.smetaelectro.database.files.FileWork;


public class SmetaSpecification extends AppCompatActivity {

    static String TAG = "33333";

    private  DataFile dataFile;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smeta_specific);

        initDB();

        //получаем id выбранного файла из интента
        long file_id = getIntent().getExtras().getLong(P.ID_FILE);
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
        TextView tvSmetaName = findViewById(R.id.tvName);
        tvSmetaName.setText(dataFile.getFileName());
        Log.d(TAG, "SmetaSpecification onCreate tvSmetaName = " + dataFile.getFileName());

        TextView tvSmetaDescription = findViewById(R.id.tvDescription);
        tvSmetaDescription.setText(dataFile.getDescription());
        Log.d(TAG, "SmetaSpecification onCreate tvSmetaDescription = " + dataFile.getDescription());

        TextView tvObjectAdress = findViewById(R.id.tvAdress);
        tvObjectAdress.setText(dataFile.getAdress());
        Log.d(TAG, "SmetaSpecification onCreate tvObjectAdress = " + dataFile.getAdress());

        TextView tvDateTime = findViewById(R.id.tvDateTime);
        tvDateTime.setText(String.format("%s_%s", dataFile.getFileNameDate(), dataFile.getFileNameTime()));
        Log.d(TAG, "SmetaSpecification onCreate tvDateTime = " +
                dataFile.getFileNameDate() + "_" + dataFile.getFileNameTime());

        Button btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
