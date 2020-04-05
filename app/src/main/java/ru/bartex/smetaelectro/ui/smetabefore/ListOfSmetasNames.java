package ru.bartex.smetaelectro.ui.smetabefore;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.database.SmetaOpenHelper;
import ru.bartex.smetaelectro.database.files.FileWork;
import ru.bartex.smetaelectro.ui.smetas2tabs.SmetasTab;

import ru.bartex.smetaelectro.ui.dialogs.DialogWorkOrMatCosts;

public class ListOfSmetasNames extends AppCompatActivity {

    private static final String TAG = "33333";
    private RecyclerView recyclerView;
    private RecyclerViewListOfFilesAdapter filesNameAdapter;

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_smetas_names);

        initDB();
        initViews();
        //объявляем о регистрации контекстного меню
        registerForContextMenu(recyclerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        initRecycledViewAdapter();
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
        recyclerView = findViewById(R.id.recyclerViewMainSmetasNames);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_smetas_list);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

//        //находим View, которое выводит текст Список пуст
//        View empty = findViewById(android.R.id.empty);
//        TextView tvEmpty = (TextView)empty;
//        tvEmpty.setText(R.string.list_empty_names);
//        mListViewNames.setEmptyView(empty);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener=
           new BottomNavigationView.OnNavigationItemSelectedListener() {
               @Override
               public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                   switch (item.getItemId()){
                       case R.id.navigation_home_smetas_list:
                            finish();
                           return true;

                       case R.id.navigation_costs_smetas_list:
                           DialogFragment dialogFragment = new DialogWorkOrMatCosts();
                           dialogFragment.show(getSupportFragmentManager(), "dialogWorkOrMatCosts");
                           return true;
                   }
                   return false;
               }
           };

    //создание меню тулбара
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu); //вместо этого - return true
        getMenuInflater().inflate(R.menu.menu_list_of_smetas, menu);
        return true;
    }

    // обработка нажатий пунктов  меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d(TAG, "ListOfSmetasNames onOptionsItemSelected id = " + id);
        switch (id){
            case R.id.action_add:
                Intent intent = new Intent(ListOfSmetasNames.this, SmetaNewName.class);
                startActivity(intent);
                return true;

            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //********************************** Жесть 2 **************************************************
    //Действия для контекстного меню для списка RecyclerView, вызов из активности
    // 1 в onCreate активности пишем registerForContextMenu(recyclerView);
    // 2 делаем метод onCreateContextMenu(...) как обычно (см ниже)
    // 3 делаем метод onContextItemSelected(...) как обычно (см ниже)
    // 4  устанавливаем слушатель для долгих нажатий в onBindViewHolder адаптера - ловим позицию
    // и запоминаем её в переменной
    // holder.base_text_file_names.setOnLongClickListener(new View.OnLongClickListener() {...});
    //*********************************************************************************************

    //создаём контекстное меню для списка
    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu,
                                    @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu_list_of_smetas, menu);
    }

    //обработка для контекстного меню
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        handleMenuItemClick(item);
        return super.onContextItemSelected(item);
    }

    private void handleMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_detail: {
                filesNameAdapter.callSpecification();
                break;
            }
            case R.id.menu_change_name: {
                filesNameAdapter.changeName();
                break;
            }
            case R.id.menu_delete: {
                filesNameAdapter.removeElement();
                break;
            }
            case R.id.menu_cancel: {
                break;
            }
        }
    }

    //инициализация RecycledView
    private void initRecycledViewAdapter() {
        Log.d(TAG, "ListOfSmetasNames initRecycledView");
        //используем встроенный LinearLayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // вызываем конструктор адаптера, передаём список
        filesNameAdapter = new RecyclerViewListOfFilesAdapter(database);
        // получаем слушатель щелчков на списке смет
        RecyclerViewListOfFilesAdapter.OnFileListClickListener onFileListClickListener =
                getOnFileListClickListener();
        //устанавливаем onFileListClickListener в качестве слушателя на щелчке списка
        filesNameAdapter.setOnFileListClickListener(onFileListClickListener);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(filesNameAdapter);
    }

    // метод чтобы получить слушатель щелчков на списке смет
    private  RecyclerViewListOfFilesAdapter.OnFileListClickListener getOnFileListClickListener(){
        return  new RecyclerViewListOfFilesAdapter.OnFileListClickListener() {
            @Override
            public void onFileListClick(String fileName) {
                //находим id по имени файла
                long file_id = FileWork.getIdFromName(database, fileName);
                Log.d(TAG, "ListOfSmetasNames - onItemClick  file_id = " + file_id);

                Intent intent = new Intent(ListOfSmetasNames.this, SmetasTab.class);
                intent.putExtra(P.ID_FILE, file_id);
                startActivity(intent);
            }
        };
    }
}
