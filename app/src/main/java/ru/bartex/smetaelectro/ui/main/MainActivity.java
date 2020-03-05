package ru.bartex.smetaelectro.ui.main;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.bartex.smetaelectro.DialogNewOrCurrentFragment;
import ru.bartex.smetaelectro.DialogWorkOrMatCosts;
import ru.bartex.smetaelectro.ListOfSmetasNames;
import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.SmetaOpenHelper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "33333";
    private RecyclerView recyclerViewMain; //RecyclerView для списка в MainActivity

    long file_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //создаём базу данных, если ее не было
        SmetaOpenHelper mDbHelper = new SmetaOpenHelper(this);

        recyclerViewMain = findViewById(R.id.recyclerViewMain);

        BottomNavigationView navigation = findViewById(R.id.navigation_main);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //если в базе нет записей, пишем файл с именем "Новая смета" в таблицу FileWork и получаем его id
        //если записи есть, получаем id по имени "Новая смета" (скорее всего, 1)
        //если id = -1, значит ошибка
        file_id = mDbHelper.createDefaultFileIfNeed();
        Log.d(TAG, "Новая смета - file_id = " + file_id);

    }


    @Override
    protected void onResume() {
        super.onResume();

        initRecycledView();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_smetas_main:
                    Intent intent_smetas = new Intent(MainActivity.this, ListOfSmetasNames.class);
                    startActivity(intent_smetas);
                    return true;
                case R.id.navigation_costs_main:
                    DialogFragment dialogFragment = new DialogWorkOrMatCosts();
                    dialogFragment.show(getSupportFragmentManager(), "dialogWorkOrMatCosts");
                    return true;
            }
            return false;
        }
    };

    //инициализация RecycledView
    private void initRecycledView() {
        Log.d(TAG, "MainActivity initRecycledView");
        // получаем массив из ресурсов
        String[] stringListMain = getResources().getStringArray(R.array.MenuMain);
        //делаем список из массива - Arrays.asList -это неизменяемый список
        List<String> listOfMain = new ArrayList<>(Arrays.asList(stringListMain));
        //используем встроенный LinearLayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        RecyclerViewMainAdapter.OnMainListClickListener onMainListClickListener =
                new RecyclerViewMainAdapter.OnMainListClickListener() {
                    @Override
                    public void onMainListClick(int position) {
                      if (position == 2){
                            dialogNewOrCurrentFragment();
                        }else if (position == 3){
                            dialogNewOrCurrentFragment();
                        }else{
                            //todo сделать для 0 и 1
                          dialogNewOrCurrentFragment();
                        }
                    }
                };

        // вызываем конструктор адаптера, передаём список
        RecyclerViewMainAdapter mainAdapter = new RecyclerViewMainAdapter(listOfMain);
        //устанавливаем onMainListClickListener в качестве слушателя на щелчках списка
        mainAdapter.setOnMainListClickListener(onMainListClickListener);
        recyclerViewMain.setLayoutManager(layoutManager);
        recyclerViewMain.setAdapter(mainAdapter);
    }

    //диалог сохранения, оформленный как класс с указанием имени файла
    private void dialogNewOrCurrentFragment() {
        DialogFragment dialogFragment = new DialogNewOrCurrentFragment();
        dialogFragment.show(getSupportFragmentManager(),"NewOrCurrentSmeta");
    }
}
