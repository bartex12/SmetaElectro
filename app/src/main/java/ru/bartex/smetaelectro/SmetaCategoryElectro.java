package ru.bartex.smetaelectro;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.CategoryWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;

public class SmetaCategoryElectro extends AppCompatActivity
        implements DialogSaveCatName.CategoryWorkNameListener {

    public static final String TAG = "33333";
    ListView mListView;
    SmetaOpenHelper mSmetaOpenHelper;
    ArrayList<Map<String, Object>> data;
    Map<String,Object> m;
    SimpleAdapter sara;
    long file_id;

    @Override
    public void categoryWorkNameTransmit(String catName) {
        long newCatNameId = mSmetaOpenHelper.insertCatName(catName);
        Log.d(TAG, "categoryWorkNameTransmit - newCatNameId = " + newCatNameId);
        updateAdapter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smeta_category);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mSmetaOpenHelper = new SmetaOpenHelper(this);
        file_id = getIntent().getLongExtra(P.ID_FILE_DEFAULT, 1);

        mListView = findViewById(R.id.listViewCategory);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //находим имя категории в адаптере
                    TextView tv = view.findViewById(R.id.base_text_two);
                    String cat_name = tv.getText().toString();
                    //находим id по имени категории
                    long cat_id = mSmetaOpenHelper.getIdFromCategoryName(cat_name);
                    Log.d(TAG, "SmetaCategory - onItemClick  cat_id = " + cat_id +
                         "  Name = " + tv.getText());

                    Intent intent = new Intent(SmetaCategoryElectro.this, SmetaTypeElectro.class);
                    intent.putExtra(P.ID_FILE_DEFAULT, file_id); //передаём дальше id файла
                    intent.putExtra(P.ID_CATEGORY, cat_id); //передаём дальше id категории
                    intent.putExtra(P.POSITION_CATEGORY, position); //передаём дальше position категории в списке
                    startActivity(intent);
            }
        });

        //объявляем о регистрации контекстного меню
        registerForContextMenu(mListView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_cat_of_work, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                openSaveCatDialogFragment();
                return true;

            case R.id.action_settings:

                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    //создаём контекстное меню для списка
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, P.SPECIFIC_ID, 0, R.string.action_detail);
        menu.add(0, P.CHANGE_NAME_ID, 0, R.string.action_change_name);
        menu.add(0, P.DELETE_ID, 0, R.string.action_delete);
        menu.add(0, P.CANCEL, 0, R.string.action_cancel);

        // получаем инфу о пункте списка
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;

        //получаем имя категории из строки списка категории
        TextView tvCat = acmi.targetView.findViewById(R.id.base_text_two);
        String catName = tvCat.getText().toString();
        //находим id категории по имени категории
        long cat_id = mSmetaOpenHelper.getIdFromCategoryName(catName);
        //находим количество строк типов работы для cat_id
        int countType = mSmetaOpenHelper.getCountType(cat_id);
        Log.d(TAG, "onContextItemSelected - countType = " + countType);

        if(countType > 0) {
            menu.findItem(P.DELETE_ID).setEnabled(false); //так лучше
            //menu.findItem(P.DELETE_ID).setVisible(false);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        // получаем инфу о пункте списка
        final AdapterView.AdapterContextMenuInfo acmi =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int id = item.getItemId();
        Log.d(TAG, "SmetaCategoryElectro onContextItemSelected id = " + id +
                " acmi.position = " + acmi.position + " acmi.id = " +acmi.id);

        switch (id){
            case P.SPECIFIC_ID:
                //получаем имя категории из строки списка категорий
                TextView tvSpecificCat = acmi.targetView.findViewById(R.id.base_text_two);
                String cat_name_specific = tvSpecificCat.getText().toString();
                //находим id по имени категории
                long cat_id_specific = mSmetaOpenHelper.getIdFromCategoryName(cat_name_specific);
                Log.d(TAG, "SmetaCategoryElectro onContextItemSelected case P.SPECIFIC_ID " +
                        "cat_name_specific = " + cat_name_specific +  " cat_id_specific =" + cat_id_specific);
                //отправляем интент с id категории
                Intent intentSpecificCat = new Intent(SmetaCategoryElectro.this, CategorySpecific.class);
                intentSpecificCat.putExtra(P.ID_CATEGORY, cat_id_specific);
                startActivity(intentSpecificCat);
                return true;

            case P.CHANGE_NAME_ID:
                //получаем имя категории из строки списка категорий
                TextView tvChangCat = acmi.targetView.findViewById(R.id.base_text_two);
                String cat_name_chang = tvChangCat.getText().toString();
                //находим id категории по имени категории
                long cat_id_Change = mSmetaOpenHelper.getIdFromCategoryName(cat_name_chang);
                Log.d(TAG, "SmetaCategoryElectro onContextItemSelected  case P.CHANGE_NAME_ID " +
                        "cat_name_chang = " + cat_name_chang + " cat_id_Change =" + cat_id_Change);
                //отправляем интент с id категории
                Intent intent = new Intent(SmetaCategoryElectro.this, CategoryChangeData.class);
                intent.putExtra(P.ID_CATEGORY, cat_id_Change);
                startActivity(intent);
                return true;

            case P.DELETE_ID:
                Log.d(TAG, "SmetaCategoryElectro onContextItemSelected case P.DELETE_ID");

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.DeleteCategory);
                builder.setPositiveButton(R.string.DeleteNo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton(R.string.DeleteYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextView tv = acmi.targetView.findViewById(R.id.base_text_two);
                        String cat_name = tv.getText().toString();
                        //находим id по имени файла
                        long cat_id = mSmetaOpenHelper.getIdFromCategoryName(cat_name);
                        Log.d(TAG, "SmetaCategoryElectro onContextItemSelected case P.DELETE_ID" +
                                " cat_name = " + cat_name + " cat_id =" + cat_id);

                        //Удаляем файл из таблицы CategoryWork когда в категории нет типов
                        mSmetaOpenHelper.deleteCategory(cat_id);

                        updateAdapter();
                    }
                });
                builder.show();
                return true;

            case P.CANCEL:
                return true;
        }
        return super.onContextItemSelected(item);
    }

    public void updateAdapter() {
        //Курсор с именами категорий из таблицы категорий CategoryWork
        Cursor cursor = mSmetaOpenHelper.getCategoryNames();
        //Строковый массив с именами категорий из таблицы FW для файла с file_id
        String[] catNamesFW = mSmetaOpenHelper.getCategoryNamesFW(file_id);

        //Список с данными для адаптера
        data = new ArrayList<Map<String, Object>>(cursor.getCount());
        while (cursor.moveToNext()) {
            //смотрим значение текущей строки курсора
            String name_cat = cursor.getString(cursor.getColumnIndex(CategoryWork.CATEGORY_NAME));
            boolean chek_mark = false;
            for (int i = 0; i<catNamesFW.length; i++){
                if (name_cat.equalsIgnoreCase(catNamesFW[i])){
                    chek_mark = true;
                }
            }
            m = new HashMap<>();
            m.put(P.ATTR_CATEGORY_MARK,chek_mark);
            m.put(P.ATTR_CATEGORY_NAME,name_cat);
            data.add(m);
        }
        String[] from = new String[]{P.ATTR_CATEGORY_MARK,P.ATTR_CATEGORY_NAME};
        int[] to = new int[]{R.id.checkBoxTwo, R.id.base_text_two};
        sara =  new SimpleAdapter(this, data, R.layout.list_item_two, from, to);
        mListView.setAdapter(sara);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home_smetas_work:
                    // Для данного варианта в манифесте указан режим singlTask для активности MainActivity
                   Intent intentHome = new Intent(SmetaCategoryElectro.this, MainActivity.class);
                    // установка флагов- создаём новую задачу и убиваем старую вместе
                    // со всеми предыдущими переходами - работает дёргано по сравнению с манифестом
                    //intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                           // Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentHome);
                    return true;

                case R.id.navigation_smetas_smetas_work:
                    // Для данного варианта в манифесте указан режим singlTask для активности Smetas
                    Intent intent = new Intent(SmetaCategoryElectro.this, Smetas.class);
                    intent.putExtra(P.ID_FILE, file_id);
                    startActivity(intent);
                    return true;

                case R.id.navigation_costs_smetas_work:
                    Intent intent_costs = new Intent(SmetaCategoryElectro.this, CostCategory.class);
                    startActivity(intent_costs);
                    return true;
            }
            return false;
        }
    };
    //диалог сохранения, оформленный как класс с указанием имени файла
    private void openSaveCatDialogFragment() {
        DialogFragment dialogFragment = new DialogSaveCatName();
        dialogFragment.show(getSupportFragmentManager(),"SaveCatName");
    }

}
