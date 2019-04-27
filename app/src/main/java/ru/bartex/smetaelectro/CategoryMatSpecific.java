package ru.bartex.smetaelectro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;

public class CategoryMatSpecific extends AppCompatActivity {

    public static final String TAG = "33333";
    TextView tvCatName;
    TextView tvSmetaDescription;
    Button btnOk;
    long cat_mat_id;
    DataCategoryMat dataCategory;

    private SmetaOpenHelper smetaOpenHelper = new SmetaOpenHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_specific);

        //получаем id выбранного файла из интента
        cat_mat_id = getIntent().getExtras().getLong(P.ID_CATEGORY_MAT);
        Log.d(TAG, "CategoryMatSpecific onCreate cat_mat_id = " + cat_mat_id);
        dataCategory = smetaOpenHelper.getCategoryMatData(cat_mat_id);

        tvCatName = findViewById(R.id.tvName);
        tvCatName.setText(dataCategory.getmCategoryMatName());
        Log.d(TAG, "CategoryMatSpecific onCreate tvCatName = " + dataCategory.getmCategoryMatName());

        tvSmetaDescription = findViewById(R.id.tvDescription);
        tvSmetaDescription.setText(dataCategory.getmCategoryMatDescription());
        Log.d(TAG, "CategoryMatSpecific onCreate tvSmetaDescription = " + dataCategory.getmCategoryMatDescription());

        btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
