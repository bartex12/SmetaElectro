package ru.bartex.smetaelectro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.TableControllerSmeta;

public class SpecificCategory extends AppCompatActivity {

    public static final String TAG = "33333";
    TextView tvCatName;
    TextView tvSmetaDescription;
    Button btnOk;
    long cat_id;
    DataCategory dataCategory;

    private SmetaOpenHelper smetaOpenHelper;
    TableControllerSmeta tableControllerSmeta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_specific);

        smetaOpenHelper = new SmetaOpenHelper(this);
        tableControllerSmeta  = new TableControllerSmeta(this);

        //получаем id выбранного файла из интента
        cat_id = getIntent().getExtras().getLong(P.ID_CATEGORY);
        Log.d(TAG, "SpecificCategory onCreate cat_id = " + cat_id);
        dataCategory = tableControllerSmeta.getDataCategory(cat_id);

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
