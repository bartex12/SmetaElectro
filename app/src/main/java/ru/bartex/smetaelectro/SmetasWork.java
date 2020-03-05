package ru.bartex.smetaelectro;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.CategoryWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.CostWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.FW;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.TypeWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.Work;
import ru.bartex.smetaelectro.ui.dialogs.DialogSaveNameAbstract;
import ru.bartex.smetaelectro.ui.dialogs.DialogSaveNameCat;
import ru.bartex.smetaelectro.ui.dialogs.DialogSaveNameType;
import ru.bartex.smetaelectro.ui.dialogs.DialogSaveNameWork;
import ru.bartex.smetaelectro.ui.main.MainActivity;

public class SmetasWork extends AppCompatActivity implements
        Tab2SmetasTypeAbstrFrag.OnClickTypekListener, Tab1SmetasCatAbstrFrag.OnClickCatListener,
        DialogSaveNameAbstract.WorkCategoryTypeNameListener {

    public static final String TAG = "33333";
    long file_id;
    boolean isSelectedType = false;
    boolean isSelectedCat =  false;
    long type_id;
    long cat_id;

    private SmetasWork.SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private SQLiteDatabase database;

    @Override
    public void catAndClickTransmit(long cat_id, boolean isSelected) {
        Log.d(TAG, "//  SmetasWork  catAndClickTransmit  // " );
        this.isSelectedCat = isSelected;
        this.cat_id = cat_id;
        Log.d(TAG, "SmetasWork  catAndClickTransmit cat_id =" +
                cat_id + "  isSelectedCat = " + isSelectedCat);
        //гениально простой способ заставить обновляться соседнюю вкладку
        //http://qaru.site/questions/683149/my-fragments-in-viewpager-tab-dont-refresh
        updateAdapter(1);
    }

    @Override
    public void typeAndClickTransmit(long cat_mat_id, long type_mat_id, boolean isSelectedTypeMat) {
        Log.d(TAG, "//  SmetasWork  typeAndCatTransmit  // " );
        this.isSelectedType = isSelectedTypeMat;
        this.type_id = type_mat_id;
        this.cat_id = cat_mat_id;
        Log.d(TAG, "SmetasWork  typeAndCatTransmit cat_id ="  +
                cat_id + "  type_id" + type_id + "  isSelectedType = " + isSelectedType);

        // обновляем соседнюю вкладку типов материалов и показываем её
        updateAdapter(2);
    }

    @Override
    public void workCategoryTypeNameTransmit(String workName, String typeName, String catName) {
        int position = mViewPager.getCurrentItem();
        Log.d(TAG, " ++++++++SmetasWork  workCategoryTypeNameTransmit ++++++");
        switch (position){
            case 0:
                Log.d(TAG, "++++++++ SmetasWork  workCategoryTypeNameTransmit ++++++ case 0");

                long newCatMatNameId = CategoryWork.insertCategory(database, catName);
                Log.d(TAG, "workCategoryTypeNameTransmit - workName = " + workName +
                        " typeName=" + typeName + " catName=" + catName +  " newCatMatNameId=" + newCatMatNameId);

                // обновляем соседнюю вкладку типов материалов и показываем её
                updateAdapter(0);
                break;

            case 1:
                Log.d(TAG, "++++++++ SmetasWork  workCategoryTypeNameTransmit ++++++ case 1");
                //определяем id категории по её имени
                long type_category_Id = CategoryWork.getIdFromName(database, catName);
                long newTypeNameId = TypeWork.insertTypeCatName(database, typeName, type_category_Id);
                Log.d(TAG, "workCategoryTypeNameTransmit - workName = " + workName +
                        " typeName=" + typeName + " catName=" + catName +  " newTypeNameId=" + newTypeNameId);
                // обновляем соседнюю вкладку типов материалов и показываем её
                updateAdapter(1);
                break;

            case 2:
                Log.d(TAG, "++++++++ SmetasWork  workCategoryTypeNameTransmit ++++++ case 2");
                //определяем id типа по его имени
                long work_type_Id = TypeWork.getIdFromName(database, typeName);
                long newWorkNameId = Work.insertTypeCatName(database,  workName, work_type_Id);
                Log.d(TAG, "workCategoryTypeNameTransmit - workName = " + workName +
                        " typeName=" + typeName + " catName=" + catName +  " newWorkNameId=" + newWorkNameId);

                // обновляем соседнюю вкладку типов материалов и показываем её
                updateAdapter(2);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smetas_work);

        initDB();

        file_id = getIntent().getExtras().getLong(P.ID_FILE);
        Log.d(TAG, " ))))))))SmetasWork  onCreate((((((((  file_id = " +  file_id);

        BottomNavigationView navigation = findViewById(R.id.navigation_smetas_mat);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Toolbar toolbar = findViewById(R.id.toolbarWork);
        setSupportActionBar(toolbar);
        //показываем заголовокмв заголовке экрана
        toolbar.setTitle(R.string.title_activity_SmetasWork);
        toolbar.setTitleTextColor(Color.GREEN);

        //Создаём адаптер для фрагментов
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Привязываем ViewPager к адаптеру
        mViewPager = findViewById(R.id.containerWork);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //средняя вкладка открыта
        mViewPager.setCurrentItem(1);
        // mViewPager.setOffscreenPageLimit(0);

        TabLayout tabLayout = findViewById(R.id.tabsWork);
        tabLayout.setTabTextColors(Color.WHITE, Color.GREEN);
        //добавляем слушатель для tabLayout из трёх вкладок, который добавлен в макет
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //добавляем слушатель нажатий на заголовки вкладок
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        //добавляем слушатель для mViewPager, отслеживающий смену вкладки в ViewPager,
        // это нужно, чтобы организовать правильную работу меню тулбара в зависимости от действий с вкладками
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                invalidateOptionsMenu();
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        FloatingActionButton fab = findViewById(R.id.fabWork);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Log.d(TAG, " ))))))))SmetasWork  onCreate((((((((  **************************");
    }

    private void initDB() {
        //
        database = new SmetaOpenHelper(this).getWritableDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, " ))))))))SmetasWork  onResume((((((((");
        //добираемся до списка фрагмента ___________пока нет в onPrepareOptionsMenu тоже вызывает ошибку_____________
        //http://qaru.site/questions/2399151/get-child-views-of-the-current-selected-items-in-viewpager
        View view = mViewPager.getChildAt(mViewPager.getCurrentItem());
        Log.d(TAG, " SmetasWork  onResume mViewPager.getCurrentItem() = " +
                mViewPager.getCurrentItem() + "  view = " + view );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, " ))))))))SmetasWork  onCreateOptionsMenu(((((((( ///");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_smeta_mat, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d(TAG, " ))))))))SmetasWork  onPrepareOptionsMenu(((((((( ///");

        int position = mViewPager.getCurrentItem();
        Log.d(TAG, " ))))))))SmetasWork  onPrepareOptionsMenu(((((((( position = " + position +
                "  isSelectedCat = " + isSelectedCat + "  isSelectedType = " + isSelectedType);
        switch (position){
            case 0:
                Log.d(TAG, " ))))))))SmetasWork  onPrepareOptionsMenu case 0");
                menu.findItem(R.id.action_add).setVisible(true);
                break;
            case 1:
                Log.d(TAG, " ))))))))SmetasWork  onPrepareOptionsMenu case 1");
                menu.findItem(R.id.action_add).setVisible(isSelectedCat);
                break;
            case 2:
                Log.d(TAG, " ))))))))SmetasWork  onPrepareOptionsMenu case 2");
                menu.findItem(R.id.action_add).setVisible(isSelectedType);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, " ))))))))SmetasWork  onOptionsItemSelected(((((((( ///");
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;

        }else if (id == R.id.action_add){
            int position = mViewPager.getCurrentItem();
            Log.d(TAG, " ))))))))SmetasWork  onOptionsItemSelected(((((((( position = " + position );
            switch (position){
                case 0:
                    Log.d(TAG, " ))))))))SmetasWork  onOptionsItemSelected case 0");
                    DialogFragment saveCat = DialogSaveNameCat.newInstance(true);
                    Log.d(TAG, " ))))))))SmetasWork  onOptionsItemSelected case 0 - 13");
                    saveCat.show(getSupportFragmentManager(),"SaveCatName");
                    Log.d(TAG, " ))))))))SmetasWork  onOptionsItemSelected case 0 - 14");
                    break;
                case 1:
                    Log.d(TAG, " ))))))))SmetasWork  onOptionsItemSelected case 1");
                    if (isSelectedCat){
                        DialogFragment saveType = DialogSaveNameType.newInstance(cat_id, true);
                        saveType.show(getSupportFragmentManager(), "saveType");
                    }
                    break;
                case 2:
                    Log.d(TAG, " ))))))))SmetasWork  onOptionsItemSelected case 2");

                    DialogFragment saveMat = DialogSaveNameWork.newInstance(cat_id, type_id, true);
                    saveMat.show(getSupportFragmentManager(), "SaveWorkName");
                    break;
            }
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

        switch (mViewPager.getCurrentItem()){
            case 0:
                Log.d(TAG, "SmetasWork c  case 0:");
                //получаем имя категории из строки списка категории
                TextView tvName = acmi.targetView.findViewById(R.id.base_text);
                String name = tvName.getText().toString();
                //находим id категории по имени категории
                long cat_id = CategoryWork.getIdFromName(database, name);
                //находим количество строк типов работы для cat_id
                int countLineType = TypeWork.getCountLine(database, cat_id);
                Log.d(TAG, "SmetasWork onCreateContextMenu - countLineType = " + countLineType);
                if(countLineType > 0) {
                    menu.findItem(P.DELETE_ID).setEnabled(false);
                }
                break;
            case 1:
                Log.d(TAG, "SmetasWork onCreateContextMenu  case 1:");
                //получаем имя типа из строки списка типов
                TextView tvType = acmi.targetView.findViewById(R.id.base_text);
                String typeName = tvType.getText().toString();
                //находим id типа по имени типа
                long type_id = TypeWork.getIdFromName(database, typeName);
                //находим количество строк видов работы для type_id
                int countLineWork = Work.getCountLine(database, type_id);
                Log.d(TAG, "SmetasWork onCreateContextMenu - countLineWork = " + countLineWork);
                if(countLineWork > 0) {
                    menu.findItem(P.DELETE_ID).setEnabled(false); //так лучше
                    //menu.findItem(P.DELETE_ID).setVisible(false);
                }
                break;

            case 2:
                Log.d(TAG, "SmetasWork onCreateContextMenu  case 2:");
                //получаем имя работы  из строки списка видов работ
                TextView tvWork = acmi.targetView.findViewById(R.id.base_text);
                String workName = tvWork.getText().toString();
                //находим id вида  по имени вида работ
                long work_id = Work.getIdFromName(database, workName);
                //находим количество строк видов работы в таблице FW для work_id
                int countLineWorkFW = FW.getCountLine(database, work_id);
                //находим количество строк расценок работы в таблице CostWork для work_id
                int countCostLineWork = CostWork.getCountLine(database, work_id);
                Log.d(TAG, "SmetasWork onCreateContextMenu - countLineWorkFW = " + countLineWorkFW +
                        " countCostLineWork =" + countCostLineWork);

                //mSmetaOpenHelper.displayTableCost();

                if(countLineWorkFW > 0) {
                    menu.findItem(P.DELETE_ID).setEnabled(false); //так лучше
                    //menu.findItem(P.DELETE_ID).setVisible(false);
                }
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        // получаем инфу о пункте списка
        final AdapterView.AdapterContextMenuInfo acmi =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int id = item.getItemId();
        Log.d(TAG, "SmetasWork onContextItemSelected id = " + id +
                " acmi.position = " + acmi.position + " acmi.id = " +acmi.id);

        switch (id){
            case P.SPECIFIC_ID:
                Log.d(TAG, "SmetasWork onContextItemSelected case P.SPECIFIC_ID");

                switch (mViewPager.getCurrentItem()){
                    case 0:
                        Log.d(TAG, "SmetasWork onContextItemSelected case P.SPECIFIC_ID case 0:");
                        //получаем имя категории из строки списка категорий
                        TextView tvSpecificCat = acmi.targetView.findViewById(R.id.base_text);
                        String cat_name_specific = tvSpecificCat.getText().toString();
                        //находим id по имени категории
                        long cat_id_specific = CategoryWork.getIdFromName(database, cat_name_specific);
                        Log.d(TAG, "SmetasWork onContextItemSelected case P.SPECIFIC_ID " +
                                "cat_name_specific = " + cat_name_specific +  " cat_id_specific =" + cat_id_specific);
                        //отправляем интент с id категории
                        Intent intentSpecificCat = new Intent(SmetasWork.this, SpecificCategory.class);
                        intentSpecificCat.putExtra(P.ID_CATEGORY, cat_id_specific);
                        startActivity(intentSpecificCat);
                        break;

                    case 1:
                        Log.d(TAG, "SmetasWork onContextItemSelected case P.SPECIFIC_ID case 1:");
                        //получаем имя типа из строки списка типов работ
                        TextView tvSpecificType = acmi.targetView.findViewById(R.id.base_text);
                        String type_name_specific = tvSpecificType.getText().toString();
                        //находим id по имени типа
                        long type_id_specific = TypeWork.getIdFromName(database, type_name_specific);
                        Log.d(TAG, "SmetaTypeElectro onContextItemSelected case P.SPECIFIC_ID case 1:" +
                                "type_name_specific = " + type_name_specific +  " type_id_specific =" + type_id_specific);
                        //отправляем интент с id типа
                        Intent intentSpecificType = new Intent(SmetasWork.this, SpecificType.class);
                        intentSpecificType.putExtra(P.ID_TYPE, type_id_specific);
                        startActivity(intentSpecificType);
                        break;

                    case 2:
                        Log.d(TAG, "SmetasWork onContextItemSelected case P.SPECIFIC_ID case 2:");
                        //получаем имя типа из строки списка типов работ
                        TextView tvSpecificWork = acmi.targetView.findViewById(R.id.base_text);
                        String work_name_specific = tvSpecificWork.getText().toString();
                        //находим id по имени типа
                        long work_id_specific = Work.getIdFromName(database, work_name_specific);
                        //Log.d(TAG, "SmetaWorkElectro onContextItemSelected case P.SPECIFIC_ID " +
                        //        "work_name_specific = " + work_name_specific +  " work_id_specific =" + work_id_specific);
                        //отправляем интент с id типа
                        Intent intentSpecificWork = new Intent(SmetasWork.this, SpesificWork.class);
                        intentSpecificWork.putExtra(P.ID_WORK, work_id_specific);
                        startActivity(intentSpecificWork);
                        break;
                }
                return true;

            case P.CHANGE_NAME_ID:
                Log.d(TAG, "SmetasWork onContextItemSelected case P.CHANGE_NAME_ID");
                switch (mViewPager.getCurrentItem()){

                    case 0:
                        Log.d(TAG, "SmetasWork onContextItemSelected  P.CHANGE_NAME_ID case 0");
                        TextView tvChangCat = acmi.targetView.findViewById(R.id.base_text);
                        String cat_name_chang = tvChangCat.getText().toString();
                        //находим id категории по имени категории
                        long cat_id_Change = CategoryWork.getIdFromName(database, cat_name_chang);
                        Log.d(TAG, "SmetasWork onContextItemSelected  P.CHANGE_NAME_ID   case 0" +
                                "cat_name_chang = " + cat_name_chang + " cat_id_Change =" + cat_id_Change);
                        //отправляем интент с id категории
                        Intent intent = new Intent(SmetasWork.this, ChangeDataCategory.class);
                        intent.putExtra(P.ID_CATEGORY, cat_id_Change);
                        startActivity(intent);
                        break;

                    case 1:
                        Log.d(TAG, "SmetasWork onContextItemSelected  P.CHANGE_NAME_ID case 1");
                        //получаем имя типа из строки списка типов работ
                        TextView tvChangType = acmi.targetView.findViewById(R.id.base_text);
                        String type_name_chang = tvChangType.getText().toString();
                        //находим id по имени типа
                        long type_id_Change = TypeWork.getIdFromName(database, type_name_chang);
                        Log.d(TAG, "SmetasWork onContextItemSelected  P.CHANGE_NAME_ID case 1" +
                                "type_name_chang = " + type_name_chang + " type_id_Change =" + type_id_Change);
                        //отправляем интент с id типа
                        Intent intentType = new Intent(SmetasWork.this, ChangeDataType.class);
                        intentType.putExtra(P.ID_TYPE, type_id_Change);
                        startActivity(intentType);
                        break;
                    case 2:
                        Log.d(TAG, "SmetasWork onContextItemSelected P.CHANGE_NAME_ID case 2");
                        //получаем имя типа из строки списка типов работ
                        TextView tvChangWork = acmi.targetView.findViewById(R.id.base_text);
                        String work_name_chang = tvChangWork.getText().toString();
                        //находим id по имени работы
                        long work_id_Change = Work.getIdFromName(database, work_name_chang);
                        Log.d(TAG, "SmetasWork onContextItemSelected P.CHANGE_NAME_ID case 2" +
                                "work_name_chang = " + work_name_chang + " work_id_Change =" + work_id_Change);
                        //отправляем интент с id работы
                        Intent intentWork = new Intent(SmetasWork.this, ChangeDataWork.class);
                        intentWork.putExtra(P.ID_WORK, work_id_Change);
                        startActivity(intentWork);
                        break;
                }
                return true;

            case P.DELETE_ID:
                Log.d(TAG, "SmetasWork onContextItemSelected case P.DELETE_ID");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.Delete);
                builder.setPositiveButton(R.string.DeleteNo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setNegativeButton(R.string.DeleteYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (mViewPager.getCurrentItem()){
                            case 0:
                                Log.d(TAG, "SmetasWork onContextItemSelected  P.DELETE_ID case 0");
                                TextView tvCat = acmi.targetView.findViewById(R.id.base_text);
                                String cat_name = tvCat.getText().toString();
                                //находим id по имени файла
                                long cat_id = CategoryWork.getIdFromName(database, cat_name);
                                Log.d(TAG, "SmetasWork onContextItemSelected case P.DELETE_ID" +
                                        " cat_name = " + cat_name + " cat_id =" + cat_id);
                                //Удаляем файл из таблицы CategoryWork когда в категории нет типов
                                CategoryWork.deleteObject(database, cat_id);
                                // обновляем соседнюю вкладку категорий работы и показываем её
                                updateAdapter(0);
                                break;

                            case 1:
                                Log.d(TAG, "SmetasWork onContextItemSelected  P.DELETE_ID case 1");
                                TextView tvType = acmi.targetView.findViewById(R.id.base_text);
                                String type_name = tvType.getText().toString();
                                //находим id по имени файла
                                long type_id = TypeWork.getIdFromName(database, type_name);
                                Log.d(TAG, "SmetasWork onContextItemSelected case P.DELETE_ID" +
                                        " type_name = " + type_name + " type_id =" + type_id);
                                //Удаляем файл из таблицы TypeWork когда в типе нет видов работ
                                TypeWork.deleteObject(database, type_id);

                                //после удаления в типе работ не даём появиться + в тулбаре
                                isSelectedCat = false;
                                // обновляем соседнюю вкладку типов работы и показываем её
                                updateAdapter(0);
                                break;

                            case 2:
                                Log.d(TAG, "SmetasWork onContextItemSelected  P.DELETE_ID case 2");
                                TextView tvWork = acmi.targetView.findViewById(R.id.base_text);
                                String work_Name = tvWork.getText().toString();
                                //находим id по имени файла
                                long work_id = Work.getIdFromName(database, work_Name);
                                //Log.d(TAG, "SmetaWorkElectro onContextItemSelected case P.DELETE_ID" +
                                 //       " work_Name = " + work_Name + " work_id =" + work_id);
                                //Удаляем запись из таблицы Work когда в таблице FW нет такой  работы
                                // проверка в onCreateContextMenu
                                Work.deleteObject(database, work_id);
                                //Удаляем запись из таблицы CostWork когда в таблице FW нет такой  работы
                                // проверка в onCreateContextMenu
                                CostWork.deleteObject(database, work_id);
                                //после удаления в работах не даём появиться + в тулбаре
                                isSelectedType = false;
                                // обновляем соседнюю вкладку работы и показываем её
                                updateAdapter(1);
                                break;
                        }
                    }
                });
                builder.show();
                return true;

            case P.CANCEL:
                return true;
        }
        return super.onContextItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Log.d(TAG, " ))))))))SmetasWork Fragment getItem ((((((((");
            switch (position){
                case 0:
                    Log.d(TAG, "SmetasWork  Fragment getItem case 0: " );
                    Tab1WorkCat tab1Category = Tab1WorkCat.NewInstance(
                            file_id,position);
                    Log.d(TAG, "SmetasWork  Fragment getItem case 0: file_id = " +
                            file_id + "  position = " +  position);
                    return tab1Category;
                case 1:
                    Log.d(TAG, "SmetasWork  Fragment getItem case 1/1: " );
                    Tab2WorkType tab2Type = Tab2WorkType.NewInstance(
                            file_id, position, isSelectedCat, cat_id);
                    Log.d(TAG, "SmetasWork  Fragment getItem case 1/2: isSelectedCat = " +
                            isSelectedCat + "  cat_id = " +  cat_id + "  file_id = " +  file_id +
                            "  position = " +  position);
                    return tab2Type;
                case 2:
                    Log.d(TAG, "SmetasWork  Fragment getItem case 2/1: " );
                    //передаём во фрагмент данные (и способ их обработки) в зависимости от isSelectedType
                    Tab3Work tab3Mat = Tab3Work.NewInstance(
                            file_id, position, isSelectedType, type_id);
                    Log.d(TAG, "SmetasWork  Fragment getItem case 2/2: isSelectedType = " +
                            isSelectedType + "  type_id = " +  type_id + "  file_id = " +  file_id +
                            "  position = " +  position);
                    return tab3Mat;
                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home_smetas_mat:
                    // Для данного варианта в манифесте указан режим singlTask для активности MainActivity
                    Intent intentHome = new Intent(SmetasWork.this, MainActivity.class);
                    // установка флагов- создаём новую задачу и убиваем старую вместе
                    // со всеми предыдущими переходами - работает дёргано по сравнению с манифестом
                    //intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    //      Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentHome);
                    return true;
                case R.id.navigation_smetas_smetas_mat:
                    // Для данного варианта в манифесте указан режим singlTask для активности ListOfSmetasNames
                    Intent intent = new Intent(SmetasWork.this, Smetas.class);
                    intent.putExtra(P.ID_FILE, file_id);
                    startActivity(intent);
                    return true;
                case R.id.navigation_costs_smetas_mat:
                    Intent intentCostMat = new Intent(SmetasWork.this, SmetasWorkCost.class);
                    intentCostMat.putExtra(P.ID_FILE, file_id);
                    startActivity(intentCostMat);
                    return true;
            }
            return false;
        }
    };

    private void updateAdapter(int currentItem){
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(currentItem);
        mSectionsPagerAdapter.notifyDataSetChanged();
    }

}

