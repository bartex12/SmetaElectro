package ru.bartex.smetaelectro.ui.smetas3tabs.specific;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.data.DataCategory;
import ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.database.SmetaOpenHelper;
import ru.bartex.smetaelectro.database.work.CategoryWork;


public class SpecificCategory extends AppCompatActivity {

    public static final String TAG = "33333";
    TextView tvCatName;
    TextView tvSmetaDescription;
    Button btnOk;
    long cat_id;
    DataCategory dataCategory;

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_specific);

        initDB();

        //получаем id выбранного файла из интента
        cat_id = getIntent().getExtras().getLong(P.ID_CATEGORY);
        Log.d(TAG, "SpecificCategory onCreate cat_id = " + cat_id);
        dataCategory = CategoryWork.getDataCategory(database, cat_id);

        initViews();

    }

    private void initDB() {
        //
        database = new SmetaOpenHelper(this).getWritableDatabase();
    }

    private void initViews() {
        tvCatName = findViewById(R.id.tvName);
        tvCatName.setText(dataCategory.getmCategoryName());
        Log.d(TAG, "SpecificCategory onCreate tvCatName = " + dataCategory.getmCategoryName());

        tvSmetaDescription = findViewById(R.id.tvDescription);
        tvSmetaDescription.setText(dataCategory.getmCategoryDescription());
        Log.d(TAG, "SpecificCategory onCreate tvSmetaDescription = " + dataCategory.getmCategoryDescription());

        btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
