package ru.bartex.smetaelectro.ui.smetas3tabs.specific;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.data.DataCategoryMat;
import ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.database.SmetaOpenHelper;
import ru.bartex.smetaelectro.database.mat.CategoryMat;


public class SpecificCategoryMat extends AppCompatActivity {

    public static final String TAG = "33333";
    TextView tvCatName;
    TextView tvSmetaDescription;
    Button btnOk;
    long cat_mat_id;
    DataCategoryMat dataCategory;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_specific);

        initDB();

        //получаем id выбранного файла из интента
        cat_mat_id = getIntent().getExtras().getLong(P.ID_CATEGORY_MAT);
        Log.d(TAG, "SpecificCategoryMat onCreate cat_mat_id = " + cat_mat_id);
        dataCategory = CategoryMat.getDataCategoryMat(database, cat_mat_id);

        initView();
    }

    private void initDB() {
        //
        database = new SmetaOpenHelper(this).getWritableDatabase();
    }

    private void initView() {
        tvCatName = findViewById(R.id.tvName);
        tvCatName.setText(dataCategory.getmCategoryMatName());
        Log.d(TAG, "SpecificCategoryMat onCreate tvCatName = " + dataCategory.getmCategoryMatName());

        tvSmetaDescription = findViewById(R.id.tvDescription);
        tvSmetaDescription.setText(dataCategory.getmCategoryMatDescription());
        Log.d(TAG, "SpecificCategoryMat onCreate tvSmetaDescription = " + dataCategory.getmCategoryMatDescription());

        btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
