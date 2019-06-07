package ru.bartex.smetaelectro.ru.bartex.smetaelectro.data;


import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

import ru.bartex.smetaelectro.DataCategory;
import ru.bartex.smetaelectro.DataCategoryMat;
import ru.bartex.smetaelectro.DataFile;
import ru.bartex.smetaelectro.DataMat;
import ru.bartex.smetaelectro.DataType;
import ru.bartex.smetaelectro.DataTypeMat;
import ru.bartex.smetaelectro.DataWork;
import ru.bartex.smetaelectro.R;

public class SmetaOpenHelper extends SQLiteOpenHelper {

        // Когда файлов в базе нет, вносим запись с именем файла по умолчанию (Используем здесь)
    //public void createDefaultFile(SQLiteDatabase db)
        // Если файлов в базе нет, вносим запись с именем файла по умолчанию (используем в MainActiviti)
    //public long createDefaultFileIfNeed()


    public static final String TAG = "33333";
    private final Context fContext;

    //Имя файла базы данных
    private static final String DATABASE_NAME = "SmetaElectro.db";
    // Версия базы данных. При изменении схемы увеличить на единицу
    private static final int DATABASE_VERSION = 1;

    public SmetaOpenHelper(Context context){
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
        fContext = context;
        Log.d(TAG, "SmetaOpenHelper - конструктор хелпера  DATABASE_NAME = " + DATABASE_NAME);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d(TAG, "SmetaOpenHelper - onCreate");

        // Строка для создания таблицы наименований смет FileData
        String SQL_CREATE_TAB_FILE = "CREATE TABLE " + FileWork.TABLE_NAME + " ("
                + FileWork._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FileWork.WORK_OR_MATERIAL + " INTEGER NOT NULL DEFAULT 0, "
                + FileWork.FILE_NAME + " TEXT NOT NULL, "
                + FileWork.ADRESS + " TEXT, "
                + FileWork.FILE_NAME_DATE + " TEXT NOT NULL DEFAULT current_date, "
                + FileWork.FILE_NAME_TIME + " TEXT NOT NULL DEFAULT current_time, "
                + FileWork.DESCRIPTION_OF_FILE + " TEXT NOT NULL DEFAULT 'Без описания');";
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TAB_FILE);
        Log.d(TAG, "SmetaOpenHelper - onCreate- создание таблицы FileData");
        // Если файлов в базе нет, вносим запись с именем файла по умолчанию P.FILENAME_DEFAULT
        this.createDefaultFile(db);

        // Строка для создания основной таблицы базы по работе, записи добавляются только программно
           String SQL_CREATE_TAB_FILE_AND_WORK  = "CREATE TABLE " + FW.TABLE_NAME + " ("
                + FW._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FW.FW_FILE_ID + " INTEGER NOT NULL, "
                   + FW.FW_FILE_NAME + " TEXT NOT NULL, "
                + FW.FW_WORK_ID + " INTEGER NOT NULL, "
                   + FW.FW_WORK_NAME + " TEXT NOT NULL, "
                + FW.FW_TYPE_ID + " INTEGER NOT NULL, "
                   + FW.FW_TYPE_NAME + " TEXT NOT NULL, "
                + FW.FW_CATEGORY_ID + " INTEGER NOT NULL, "
                   + FW.FW_CATEGORY_NAME + " TEXT NOT NULL, "
                + FW.FW_COST + " REAL NOT NULL, "
                + FW.FW_COUNT + " INTEGER NOT NULL, "
                + FW.FW_UNIT + " TEXT NOT NULL, "
                + FW.FW_SUMMA + " REAL NOT NULL);";
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TAB_FILE_AND_WORK);
        Log.d(TAG, "SmetaOpenHelper - onCreate- создание таблицы FW");


        // Строка для создания основной таблицы базы по материалам, записи добавляются только программно
        String SQL_CREATE_TAB_FILE_AND_MATERIALS  = "CREATE TABLE " + FM.TABLE_NAME + " ("
                + FM._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FM.FM_FILE_ID+ " INTEGER NOT NULL, "
                + FM.FM_FILE_NAME + " TEXT NOT NULL, "
                + FM.FM_MAT_ID + " INTEGER NOT NULL, "
                + FM.FM_MAT_NAME + " TEXT NOT NULL, "
                + FM.FM_MAT_TYPE_ID + " INTEGER NOT NULL, "
                + FM.FM_MAT_TYPE_NAME + " TEXT NOT NULL, "
                + FM.FM_MAT_CATEGORY_ID + " INTEGER NOT NULL, "
                + FM.FM_MAT_CATEGORY_NAME + " TEXT NOT NULL, "
                + FM.FM_MAT_COST + " REAL NOT NULL, "
                + FM.FM_MAT_COUNT + " INTEGER NOT NULL, "
                + FM.FM_MAT_UNIT + " TEXT NOT NULL, "
                + FM.FM_MAT_SUMMA + " REAL NOT NULL);";
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TAB_FILE_AND_MATERIALS);
        Log.d(TAG, "SmetaOpenHelper - onCreate- создание таблицы FM");

        // Строка для создания таблицы категорий работ CategoryWork
        String SQL_CREATE_TAB_CATEGORY = "CREATE TABLE " + CategoryWork.TABLE_NAME + " ("
                + CategoryWork._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CategoryWork.CATEGORY_NAME + " TEXT NOT NULL, "
                + CategoryWork.CATEGORY_DESCRIPTION + " TEXT NOT NULL DEFAULT 'Без описания');";
        // Запускаем создание таблицы категорий работ
        db.execSQL(SQL_CREATE_TAB_CATEGORY);
        Log.d(TAG, "SmetaOpenHelper - onCreate- создание таблицы CategoryWork");
        // Если файлов в базе нет, вносим записи названий категорий работ (добавление из программы)
        this.createDefaultCategory(db);

        // Строка для создания таблицы категорий материалов CategoryMat
        String SQL_CREATE_TAB_CATEGORY_MAT = "CREATE TABLE " + CategoryMat.TABLE_NAME + " ("
                + CategoryMat._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CategoryMat.CATEGORY_MAT_NAME + " TEXT NOT NULL, "
                + CategoryMat.CATEGORY_MAT_DESCRIPTION + " TEXT NOT NULL DEFAULT 'Без описания');";
        // Запускаем создание таблицы категорий работ
        db.execSQL(SQL_CREATE_TAB_CATEGORY_MAT);
        Log.d(TAG, "SmetaOpenHelper - onCreate- создание таблицы CategoryMat");
        // Если файлов в базе нет, вносим записи названий категорий материалов (добавление из программы)
        this.createDefaultCategoryMat(db);

        // Строка для создания таблицы типов работ TypeWork
        String SQL_CREATE_TAB_TYPE = "CREATE TABLE " + TypeWork.TABLE_NAME + " ("
                + TypeWork._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TypeWork.TYPE_CATEGORY_ID + " INTEGER NOT NULL, "
                + TypeWork.TYPE_NAME + " TEXT NOT NULL, "
                + TypeWork.TYPE_DESCRIPTION + " TEXT NOT NULL DEFAULT 'Без описания');";
        // Запускаем создание таблицы разделов (типов) работ TypeWork
        db.execSQL(SQL_CREATE_TAB_TYPE);
        Log.d(TAG, "SmetaOpenHelper - onCreate- создание таблицы TypeWork");
        // Если файлов в базе нет, вносим записи названия типов работ (добавление из программы)
        this.createDefaultType(db);

        // Строка для создания таблицы типов материалов TypeMat
        String SQL_CREATE_TAB_TYPE_MAT = "CREATE TABLE " + TypeMat.TABLE_NAME + " ("
                + TypeMat._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TypeMat.TYPE_MAT_CATEGORY_ID + " INTEGER NOT NULL, "
                + TypeMat.TYPE_MAT_NAME + " TEXT NOT NULL, "
                + TypeMat.TYPE_MAT_DESCRIPTION + " TEXT NOT NULL DEFAULT 'Без описания');";
        // Запускаем создание таблицы разделов (типов) работ TypeWork
        db.execSQL(SQL_CREATE_TAB_TYPE_MAT);
        Log.d(TAG, "SmetaOpenHelper - onCreate- создание таблицы TypeMat");
        // Если файлов в базе нет, вносим записи названия типов работ (добавление из программы)
        this.createDefaultTypeMat(db);


        // Строка для создания таблицы конкретных работ Work
        String SQL_CREATE_TAB_WORK = "CREATE TABLE " + Work.TABLE_NAME + " ("
                + Work._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Work.WORK_TYPE_ID + " INTEGER NOT NULL, "
                + Work.WORK_NAME + " TEXT NOT NULL, "
                + Work.WORK_DESCRIPTION + " TEXT NOT NULL DEFAULT 'Без описания');";
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TAB_WORK);
        Log.d(TAG, "SmetaOpenHelper - onCreate- создание таблицы Work");
        // Если файлов в базе нет, вносим записи названия  работ (добавление из программы)
        this.createDefaultWork(db);

        // Строка для создания таблицы конкретных материалов Mat
        String SQL_CREATE_TAB_MAT = "CREATE TABLE " + Mat.TABLE_NAME + " ("
                + Mat._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Mat.MAT_TYPE_ID + " INTEGER NOT NULL, "
                + Mat.MAT_NAME + " TEXT NOT NULL, "
                + Mat.MAT_DESCRIPTION + " TEXT NOT NULL DEFAULT 'Без описания');";
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TAB_MAT);
        Log.d(TAG, "SmetaOpenHelper - onCreate- создание таблицы Mat");
        // Если файлов в базе нет, вносим записи названия  материалов (добавление из программы)
        this.createDefaultMat(db);

        // Строка для создания таблицы единиц измерения Unit для работ
        String SQL_CREATE_TAB_UNIT = "CREATE TABLE " + Unit.TABLE_NAME + " ("
                + Unit._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Unit.UNIT_NAME + " TEXT NOT NULL);";
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TAB_UNIT);
        Log.d(TAG, "SmetaOpenHelper - onCreate- создание таблицы Unit");
        // Если файлов в базе нет, вносим записи единиц измерения
        this.createDefaultUnit(db);

        // Строка для создания таблицы единиц измерения UnitMat для материалов
        String SQL_CREATE_TAB_UNIT_MAT = "CREATE TABLE " + UnitMat.TABLE_NAME + " ("
                + UnitMat._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UnitMat.UNIT_MAT_NAME + " TEXT NOT NULL);";
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TAB_UNIT_MAT);
        Log.d(TAG, "SmetaOpenHelper - onCreate- создание таблицы UnitMat");
        // Если файлов в базе нет, вносим записи единиц измерения
        this.createDefaultUnitMat(db);

        // Строка для создания таблицы расценок CostWork
        String SQL_CREATE_TAB_COST = "CREATE TABLE " + CostWork.TABLE_NAME + " ("
                + CostWork._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CostWork.COST_WORK_ID + " INTEGER NOT NULL, "
                + CostWork.COST_UNIT_ID + " INTEGER NOT NULL, "
                + CostWork.COST_COST + " REAL NOT NULL DEFAULT 0, "
                + CostWork.COST_NUMBER + " INTEGER NOT NULL DEFAULT 1);";
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TAB_COST);
        Log.d(TAG, "SmetaOpenHelper - onCreate- создание таблицы CostWork");
        // Если файлов в базе нет, вносим записи расценок
        this.createDefaultCost(db);


        // Строка для создания таблицы расценок CostMat
        String SQL_CREATE_TAB_COST_MAT = "CREATE TABLE " + CostMat.TABLE_NAME + " ("
                + CostMat._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CostMat.COST_MAT_ID + " INTEGER NOT NULL, "
                + CostMat.COST_MAT_UNIT_ID + " INTEGER NOT NULL, "
                + CostMat.COST_MAT_COST + " REAL NOT NULL DEFAULT 0, "
                + CostMat.COST_MAT_NUMBER + " INTEGER NOT NULL DEFAULT 1);";
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TAB_COST_MAT);
        Log.d(TAG, "SmetaOpenHelper - onCreate- создание таблицы CostMat");
        // Если файлов в базе нет, вносим записи расценок
        this.createDefaultCostMat(db);

        // Строка для создания таблицы смет TotalTab
        String SQL_CREATE_TAB_TOTAL = "CREATE TABLE " + TotalTab.TABLE_NAME + " ("
                + TotalTab._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TotalTab.TOTAL_FILE_ID + " TEXT NOT NULL, "
                + TotalTab.TOTAL_CATEGORY_ID + " TEXT NOT NULL, "
                + TotalTab.TOTAL_TYPE_ID + " TEXT NOT NULL, "
                + TotalTab.TOTAL_WORK_ID + " TEXT NOT NULL, "
                + TotalTab.TOTAL_COST + " REAL NOT NULL DEFAULT 0, "
                + TotalTab.TOTAL_COST_NUMBER + " INTEGER NOT NULL DEFAULT 0, "
                + TotalTab.TOTAL_COST_UNIT + " TEXT NOT NULL, "
                + TotalTab.TOTAL_SUMMA + " REAL NOT NULL DEFAULT 0);";
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TAB_TOTAL);
        Log.d(TAG, "SmetaOpenHelper - onCreate- создание таблицы TotalTab");

        Log.d(TAG, "++++++++++++  Создана база данных  " + DATABASE_NAME + "  ++++++++++++++++");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Когда файлов в базе нет, вносим запись с именем файла по умолчанию (Используем здесь)
    public void createDefaultFile(SQLiteDatabase db) {
        Log.d(TAG, "MyDatabaseHelper.createDefaultFile..." );
            //получаем дату и время в нужном для базы данных формате
            String dateFormat = this.getDateString();
            String timeFormat = this.getTimeString();

        ContentValues cv = new ContentValues();
        cv.put(FileWork.WORK_OR_MATERIAL, 0);
        cv.put(FileWork.FILE_NAME, P.FILENAME_DEFAULT);
        cv.put(FileWork.ADRESS, P.ADRESS_DEFAULT);
        cv.put(FileWork.FILE_NAME_DATE, dateFormat);
        cv.put(FileWork.FILE_NAME_TIME, timeFormat);
        cv.put(FileWork.DESCRIPTION_OF_FILE, P.DESCRIPTION_DEFAULT);
        // вставляем строку
        long ID = db.insert(FileWork.TABLE_NAME, null, cv);
        // закрываем соединение с базой
        //db.close();
        Log.d(TAG, "MyDatabaseHelper.createDefaultFile...  file1_id = " + ID);
    }

    // Если файлов в базе нет, вносим запись с именем файла по умолчанию (используем в MainActiviti)
    public long createDefaultFileIfNeed() {
        int count = this.getFilesCount();
        long file1_id = -1;
        if (count == 0) {
            //получаем дату и время в нужном для базы данных формате
            String dateFormat = this.getDateString();
            String timeFormat = this.getTimeString();

            //создаём экземпляр класса DataFile в конструкторе
            DataFile file1 = new DataFile(P.FILENAME_DEFAULT, P.ADRESS_DEFAULT,
                    dateFormat, timeFormat, P.DESCRIPTION_DEFAULT);
            //добавляем запись в таблицу FileWork, используя данные DataFile и получаем id записи
            file1_id = this.addFile(file1);
        }else {
            file1_id = this.getIdFromFileName(P.FILENAME_DEFAULT);
        }
        Log.d(TAG, "MyDatabaseHelper.createDefaultFileIfNeed ... count = " +
                this.getFilesCount() + " file1_id = " + file1_id);
        return file1_id;
    }

    //получаем количество файлов  в базе
    public int getFilesCount() {
        Log.i(TAG, "TempDBHelper.getFilesCount ... ");
        String countQuery = "SELECT  * FROM " + FileWork.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    //формируем строку с датой
    public String getDateString() {
        Calendar calendar = new GregorianCalendar();
        return String.format("%s-%s-%s",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH));
    }

    //формируем строку с временем
    public String getTimeString() {
        Calendar calendar = new GregorianCalendar();
        return String.format("%s:%s:%s",
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND));
    }

    //Метод для добавления нового файла в таблицу файлов
    public long addFile(DataFile file) {
        Log.d(TAG, "TempDBHelper.addFile ... " + file.getFileName());

        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(FileWork.FILE_NAME, file.getFileName());
        cv.put(FileWork.ADRESS, file.getAdress());
        cv.put(FileWork.FILE_NAME_DATE, file.getFileNameDate());
        cv.put(FileWork.FILE_NAME_TIME, file.getFileNameTime());
        cv.put(FileWork.DESCRIPTION_OF_FILE, file.getDescription());
        // вставляем строку
        long ID = db.insert(FileWork.TABLE_NAME, null, cv);
        // закрываем соединение с базой
        db.close();
        return ID;
    }


    //получаем ID по имени файла
    public long getIdFromFileName(String name) {
        long currentID;
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                FileWork.TABLE_NAME,   // таблица
                new String[]{FileWork._ID},            // столбцы
                FileWork.FILE_NAME + "=?",                  // столбцы для условия WHERE
                new String[]{name},                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            // Узнаем индекс  столбца FileWork._ID
            int idColumnIndex = cursor.getColumnIndex(FileWork._ID);
            // Используем индекс для получения строки или числа
            currentID = cursor.getLong(idColumnIndex);
        } else {
            currentID = -1;
        }
        Log.d(TAG, "getIdFromFileName currentID = " + currentID);
        cursor.close();
        db.close();
        return currentID;
    }

    //создаём категории из ресурсов
    public void createDefaultCategory(SQLiteDatabase db) {

        ContentValues values = new ContentValues();
        // Получим массив строк из ресурсов
        Resources res = fContext.getResources();
        String[] cat_name = res.getStringArray(R.array.category_name);
        String[] cat_descr = res.getStringArray(R.array.category_descr);
        // проходим через массив и вставляем записи в таблицу
        int length = cat_name.length;
        for (int i = 0; i<length ; i++){
            values.put(CategoryWork.CATEGORY_NAME, cat_name[i]);
            values.put(CategoryWork.CATEGORY_DESCRIPTION, cat_descr[i]);
            // Добавляем записи в таблицу
            db.insert(CategoryWork.TABLE_NAME, null, values);
        }
        Log.d(TAG, "createDefaultCategory cat_name.length = " + cat_name.length);
    }

    //создаём категории из ресурсов
    public void createDefaultCategoryMat(SQLiteDatabase db) {

        ContentValues values = new ContentValues();
        // Получим массив строк из ресурсов
        Resources res = fContext.getResources();
        String[] cat_name_mat = res.getStringArray(R.array.category_name_mat);
        String[] cat_descr_mat = res.getStringArray(R.array.category_descr_mat);
        // проходим через массив и вставляем записи в таблицу
        int length = cat_name_mat.length;
        for (int i = 0; i<length ; i++){
            values.put(CategoryMat.CATEGORY_MAT_NAME, cat_name_mat[i]);
            values.put(CategoryMat.CATEGORY_MAT_DESCRIPTION, cat_descr_mat[i]);
            // Добавляем записи в таблицу
            db.insert(CategoryMat.TABLE_NAME, null, values);
        }
        Log.d(TAG, "createDefaultCategory cat_name.length = " + cat_name_mat.length);
    }

    //получаем курсор с Category._id  и делаем массив id
    public int[] getArrayCategoryId(SQLiteDatabase db) {
        Log.i(TAG, "SmetaOpenHelper.getArrayCategoryId ... ");
        String categoryId = " SELECT " + CategoryWork._ID  + " FROM " + CategoryWork.TABLE_NAME;
        Cursor cursor = db.rawQuery(categoryId, null);
        int[] category_id = new int[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            category_id[position] = cursor.getInt(cursor.getColumnIndex(CategoryWork._ID));
            Log.i(TAG, "SmetaOpenHelper.getArrayCategoryId position = " + position);
        }
        cursor.close();
        return category_id;
    }

    //получаем курсор с Category._id  и делаем массив id
    public int[] getArrayCategoryIdMat( SQLiteDatabase db) {

        Log.i(TAG, "SmetaOpenHelper.getArrayCategoryIdMat ... ");
        String categoryIdMat = " SELECT " + CategoryMat._ID  + " FROM " + CategoryMat.TABLE_NAME;
        Cursor cursor = db.rawQuery(categoryIdMat, null);
        int[] category_id_mat = new int[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            category_id_mat[position] = cursor.getInt(cursor.getColumnIndex(CategoryMat._ID));
            Log.i(TAG, "SmetaOpenHelper.getArrayCategoryId position = " + position);
        }
        cursor.close();
        return category_id_mat;
    }

    //создаём типы из ресурсов и получаем COLUMN_TYPE_CATEGORY_ID из  this.getArrayCategoryId()
    public void createDefaultType(SQLiteDatabase db) {
        Log.i(TAG, "SmetaOpenHelper.createDefaultType...");
        // Добавляем записи в таблицу
        ContentValues values = new ContentValues();

        // Получим массив строк из ресурсов
        Resources res = fContext.getResources();
        String[] type_name_category_electro = res.getStringArray(R.array.type_name_electro);
        String[] type_name_category_drugoe = res.getStringArray(R.array.type_name_drugoe);

        //получаем массив Category._id  таблицы категорий Category
        int[] category_id = this.getArrayCategoryId(db);
        // проходим через массив и вставляем записи в таблицу
        int ii = 0; //type_name_category_electro
        for (int i = 0; i <type_name_category_electro.length; i++ ){
            this.InsertType(db, values, category_id[ii], type_name_category_electro[i]);
        }
        ii = 1; //type_name_category_drugoe
        for (int i = 0; i <type_name_category_drugoe.length; i++ ){
            this.InsertType(db, values, category_id[ii], type_name_category_drugoe[i]);
        }
        Log.d(TAG, "createDefaultType type_name_category_electro.length = " + type_name_category_electro.length);
    }

    //создаём типы из ресурсов и получаем COLUMN_TYPE_CATEGORY_ID из  this.getArrayCategoryId()
    public void createDefaultTypeMat(SQLiteDatabase db) {

        Log.i(TAG, "SmetaOpenHelper.createDefaultTypeMat...");
        // Добавляем записи в таблицу
        ContentValues values = new ContentValues();

        // Получим массив строк из ресурсов
        Resources res = fContext.getResources();
        String[] type_mat_name_electro = res.getStringArray(R.array.type_name_electro_mat);
        String[] type_mat_name_drugoe = res.getStringArray(R.array.type_name_drugoe_mat);

        //получаем массив Category._id  таблицы категорий Category
        int[] category_id_mat = this.getArrayCategoryIdMat(db);
        // проходим через массив и вставляем записи в таблицу
        int ii = 0; //type_mat_name_electro
        for (int i = 0; i <type_mat_name_electro.length; i++ ){
            this.InsertTypeMat(db, values, category_id_mat[ii], type_mat_name_electro[i]);
        }
        ii = 1; //type_mat_name_drugoe
        for (int i = 0; i <type_mat_name_drugoe.length; i++ ){
            this.InsertTypeMat(db, values, category_id_mat[ii], type_mat_name_drugoe[i]);
        }
        Log.d(TAG, "createDefaultTypeMat type_mat_name_electro.length = " +
                type_mat_name_electro.length + " type_mat_name_drugoe.length = " +type_mat_name_drugoe.length);
    }

    public void InsertType( SQLiteDatabase db,ContentValues values,
                            int category_id,  String type_name){
        values.put(TypeWork.TYPE_CATEGORY_ID, category_id);
        values.put(TypeWork.TYPE_NAME, type_name);
        db.insert(TypeWork.TABLE_NAME, null, values);
    }

    public void InsertTypeMat( SQLiteDatabase db,ContentValues values,
                            int category_id,  String type_name){
        values.put(TypeMat.TYPE_MAT_CATEGORY_ID, category_id);
        values.put(TypeMat.TYPE_MAT_NAME, type_name);
        db.insert(TypeMat.TABLE_NAME, null, values);
    }

    //создаём работы из ресурсов и получаем COLUMN_TYPE_CATEGORY_ID из  this.getArrayCategoryId()
    public void createDefaultWork(SQLiteDatabase db) {
        Log.i(TAG, "SmetaOpenHelper.createDefaultWork...");
        // Добавляем записи в таблицу
        ContentValues values = new ContentValues();
        // Получим массив строк из ресурсов
        Resources res = fContext.getResources();
        String[] work_name_type_demontag = res.getStringArray(R.array.work_name_type_demontag);
        String[] work_name_type_podgotovka = res.getStringArray(R.array.work_name_type_podgotovka);
        String[] work_name_type_shtroby_otverstia = res.getStringArray(R.array.work_name_type_shtroby_otverstia);
        String[] work_name_type_kabel_kabelkanaly = res.getStringArray(R.array.work_name_type_kabel_kabelkanaly);
        String[] work_name_type_korobki = res.getStringArray(R.array.work_name_type_korobki);
        String[] work_name_type_sverlenie_otverstii = res.getStringArray(R.array.work_name_type_sverlenie_otverstii);
        String[] work_name_type_shchit = res.getStringArray(R.array.work_name_type_shchit);
        String[] work_name_type_set_rozetki = res.getStringArray(R.array.work_name_type_set_rozetki);
        String[] work_name_type_set_svetilniki = res.getStringArray(R.array.work_name_type_set_svetilniki);
        String[] work_name_type_LED = res.getStringArray(R.array.work_name_type_LED);
        String[] work_name_type_oborudovanie = res.getStringArray(R.array.work_name_type_oborudovanie);
        String[] work_name_type_soputstv = res.getStringArray(R.array.work_name_type_soputstv);
        String[] work_name_type_primechania = res.getStringArray(R.array.work_name_type_primechania);

        String[] work_name_type_musor = res.getStringArray(R.array.work_name_type_musor);
        String[] work_name_type_tovary = res.getStringArray(R.array.work_name_type_tovary);

        //получаем массив type_id  таблицы типов Types
        int[] type_id = this.getArrayTypeId(db);
        // проходим через массив и вставляем записи в таблицу
        int ii = 0; //work_name_type_demontag
            for (int i = 0; i <work_name_type_demontag.length; i++ ){
                this.InsertWork(db, values, type_id[ii], work_name_type_demontag[i]);
            }
        ii = 1; //work_name_type_podgotovka
        for (int i = 0; i <work_name_type_podgotovka.length; i++ ){
            this.InsertWork(db, values, type_id[ii], work_name_type_podgotovka[i]);
        }
           ii = 2; //work_name_type_shtroby_otverstia
        for (int i = 0; i <work_name_type_shtroby_otverstia.length; i++ ){
            this.InsertWork(db, values, type_id[ii],  work_name_type_shtroby_otverstia[i]);
        }
        ii = 3; //work_name_type_kabel_kabelkanaly
        for (int i = 0; i <work_name_type_kabel_kabelkanaly.length; i++ ){
            this.InsertWork(db, values, type_id[ii],  work_name_type_kabel_kabelkanaly[i]);
        }
        ii = 4; //work_name_type_korobki
        for (int i = 0; i <work_name_type_korobki.length; i++ ){
            this.InsertWork(db, values, type_id[ii],  work_name_type_korobki[i]);
        }
        ii = 5; //work_name_type_sverlenie_otverstii
        for (int i = 0; i <work_name_type_sverlenie_otverstii.length; i++ ){
            this.InsertWork(db, values, type_id[ii],  work_name_type_sverlenie_otverstii[i]);
        }
        ii = 6; //work_name_type_shchit
        for (int i = 0; i <work_name_type_shchit.length; i++ ){
            this.InsertWork(db, values, type_id[ii],  work_name_type_shchit[i]);
        }
        ii = 7; //work_name_type_set_rozetki
        for (int i = 0; i <work_name_type_set_rozetki.length; i++ ){
            this.InsertWork(db, values, type_id[ii],  work_name_type_set_rozetki[i]);
        }
        ii = 8; //work_name_type_set_svetilniki
        for (int i = 0; i <work_name_type_set_svetilniki.length; i++ ){
            this.InsertWork(db, values, type_id[ii],  work_name_type_set_svetilniki[i]);
        }
        ii = 9; //work_name_type_LED
        for (int i = 0; i <work_name_type_LED.length; i++ ){
            this.InsertWork(db, values, type_id[ii], work_name_type_LED[i]);
        }
        ii = 10; //work_name_type_oborudovanie
        for (int i = 0; i <work_name_type_oborudovanie.length; i++ ){
            this.InsertWork(db, values, type_id[ii],  work_name_type_oborudovanie[i]);
        }
        ii = 11; //work_name_type_soputstv
        for (int i = 0; i <work_name_type_soputstv.length; i++ ){
            this.InsertWork(db, values, type_id[ii],  work_name_type_soputstv[i]);
        }
        ii = 12; //work_name_type_primechania
        for (int i = 0; i <work_name_type_primechania.length; i++ ){
            this.InsertWork(db, values, type_id[ii],  work_name_type_primechania[i]);
        }
        ii = 13; //work_name_type_musor
        for (int i = 0; i <work_name_type_musor.length; i++ ){
            this.InsertWork(db, values, type_id[ii],  work_name_type_musor[i]);
        }
        ii = 14; //work_name_type_tovary
        for (int i = 0; i <work_name_type_tovary.length; i++ ){
            this.InsertWork(db, values, type_id[ii],  work_name_type_tovary[i]);
        }
        Log.d(TAG, "createDefaultWork type_id.length = " +
                type_id.length + " work_name_type_demontag.length = " +work_name_type_demontag.length);
    }

    //создаём работы из ресурсов и получаем COLUMN_TYPE_CATEGORY_ID из  this.getArrayCategoryId()
    public void createDefaultMat(SQLiteDatabase db) {

        Log.i(TAG, "SmetaOpenHelper.createDefaultMat...");
        // Добавляем записи в таблицу
        ContentValues values = new ContentValues();
        // Получим массив строк из ресурсов
        Resources res = fContext.getResources();
        String[] mat_name_type_kabeli = res.getStringArray(R.array.mat_name_type_kabeli);
        String[] mat_name_type_kabel_kanali = res.getStringArray(R.array.mat_name_type_kabel_kanali);
        String[] mat_name_type_korobki = res.getStringArray(R.array.mat_name_type_korobki);
        String[] mat_name_type_vremianki = res.getStringArray(R.array.mat_name_type_vremianki);
        String[] mat_name_type_rozetki = res.getStringArray(R.array.mat_name_type_rozetki);
        String[] mat_name_type_shchity = res.getStringArray(R.array.mat_name_type_shchity);
        String[] mat_name_type_led = res.getStringArray(R.array.mat_name_type_led);
        String[] mat_name_type_svetilniki = res.getStringArray(R.array.mat_name_type_svetilniki);
        String[] mat_name_type_bury = res.getStringArray(R.array.mat_name_type_bury);
        String[] mat_name_type_krepez = res.getStringArray(R.array.mat_name_type_krepez);
        String[] mat_name_type_sypuchie = res.getStringArray(R.array.mat_name_type_sypuchie);
        String[] mat_name_type_vspomogat = res.getStringArray(R.array.mat_name_type_vspomogat);

        String[] mat_name_type_instr = res.getStringArray(R.array.mat_name_type_instr);
        String[] mat_name_type_obschestroy = res.getStringArray(R.array.mat_name_type_obschestroy);

        //получаем массив type_id  таблицы типов Types
        int[] type_id = this.getArrayTypeIdMat(db);
        // проходим через массив и вставляем записи в таблицу
        int ii = 0; //mat_name_type_kabeli
        for (int i = 0; i <mat_name_type_kabeli.length; i++ ){
            this.InsertMat(db, values, type_id[ii], mat_name_type_kabeli[i]);
        }
        ii = 1; //mat_name_type_kabel_kanali
        for (int i = 0; i <mat_name_type_kabel_kanali.length; i++ ){
            this.InsertMat(db, values, type_id[ii], mat_name_type_kabel_kanali[i]);
        }
        ii = 2; //mat_name_type_korobki
        for (int i = 0; i <mat_name_type_korobki.length; i++ ){
            this.InsertMat(db, values, type_id[ii],  mat_name_type_korobki[i]);
        }
        ii = 3; //mat_name_type_vremianki
        for (int i = 0; i <mat_name_type_vremianki.length; i++ ){
            this.InsertMat(db, values, type_id[ii],  mat_name_type_vremianki[i]);
        }
        ii = 4; //mat_name_type_rozetki
        for (int i = 0; i <mat_name_type_rozetki.length; i++ ){
            this.InsertMat(db, values, type_id[ii],  mat_name_type_rozetki[i]);
        }
        ii = 5; //mat_name_type_shchity
        for (int i = 0; i <mat_name_type_shchity.length; i++ ){
            this.InsertMat(db, values, type_id[ii],  mat_name_type_shchity[i]);
        }
        ii = 6; //mat_name_type_led
        for (int i = 0; i <mat_name_type_led.length; i++ ){
            this.InsertMat(db, values, type_id[ii],  mat_name_type_led[i]);
        }
        ii = 7; //mat_name_type_svetilniki
        for (int i = 0; i <mat_name_type_svetilniki.length; i++ ){
            this.InsertMat(db, values, type_id[ii],  mat_name_type_svetilniki[i]);
        }
        ii = 8; //mat_name_type_bury
        for (int i = 0; i <mat_name_type_bury.length; i++ ){
            this.InsertMat(db, values, type_id[ii],  mat_name_type_bury[i]);
        }
        ii = 9; //mat_name_type_krepez
        for (int i = 0; i <mat_name_type_krepez.length; i++ ){
            this.InsertMat(db, values, type_id[ii], mat_name_type_krepez[i]);
        }
        ii = 10; //mat_name_type_sypuchie
        for (int i = 0; i <mat_name_type_sypuchie.length; i++ ){
            this.InsertMat(db, values, type_id[ii],  mat_name_type_sypuchie[i]);
        }
        ii = 11; //mat_name_type_vspomogat
        for (int i = 0; i <mat_name_type_vspomogat.length; i++ ){
            this.InsertMat(db, values, type_id[ii],  mat_name_type_vspomogat[i]);
        }
        ii = 12; //mat_name_type_instr
        for (int i = 0; i <mat_name_type_instr.length; i++ ){
            this.InsertMat(db, values, type_id[ii],  mat_name_type_instr[i]);
        }
        ii = 13; //mat_name_type_obschestroy
        for (int i = 0; i <mat_name_type_obschestroy.length; i++ ){
            this.InsertMat(db, values, type_id[ii],  mat_name_type_obschestroy[i]);
        }
        Log.d(TAG, "createDefaultMat type_id.length = " +
                type_id.length + " mat_name_type_kabeli.length = " + mat_name_type_kabeli.length);
    }

    public void InsertWork( SQLiteDatabase db,ContentValues values,
                            int type_id,  String work_name){
        values.put(Work.WORK_TYPE_ID, type_id);
        values.put(Work.WORK_NAME, work_name);
        db.insert(Work.TABLE_NAME, null, values);
    }

    public void InsertMat( SQLiteDatabase db,ContentValues values,
                            int type_id,  String work_name){
        values.put(Mat.MAT_TYPE_ID, type_id);
        values.put(Mat.MAT_NAME, work_name);
        db.insert(Mat.TABLE_NAME, null, values);
    }

    public void createDefaultUnit(SQLiteDatabase db){
        Log.i(TAG, "SmetaOpenHelper.createDefaultUnit...");
        ContentValues values = new ContentValues();
        // Получим массив строк из ресурсов
        Resources res = fContext.getResources();
        String[] unit_name = res.getStringArray(R.array.unit_name);
        // проходим через массив и вставляем записи в таблицу
        int length = unit_name.length;
        for (int i = 0; i<length ; i++){
            values.put(Unit.UNIT_NAME, unit_name[i]);
            // Добавляем записи в таблицу
            db.insert(Unit.TABLE_NAME, null, values);
        }
        Log.d(TAG, "createDefaultUnit unit_name.length = " + unit_name.length );
    }

    public void createDefaultUnitMat(SQLiteDatabase db){
        Log.i(TAG, "SmetaOpenHelper.createDefaultUnitMat...");
        ContentValues values = new ContentValues();
        // Получим массив строк из ресурсов
        Resources res = fContext.getResources();
        String[] unit_name = res.getStringArray(R.array.unit_name);
        // проходим через массив и вставляем записи в таблицу
        int length = unit_name.length;
        for (int i = 0; i<length ; i++){
            values.put(UnitMat.UNIT_MAT_NAME, unit_name[i]);
            // Добавляем записи в таблицу
            db.insert(UnitMat.TABLE_NAME, null, values);
        }
        Log.d(TAG, "createDefaultUnitMat unit_name.length = " + unit_name.length );
    }

    public void createDefaultCost(SQLiteDatabase db){
        Log.i(TAG, "SmetaOpenHelper.createDefaultCost...");
        ContentValues values = new ContentValues();
        // Получим массив строк из ресурсов
        Resources res = fContext.getResources();

        int [] work_id = this.getIdFromWorks(db);
        int length = work_id.length;

        String[] unitIdOfWork = res.getStringArray(R.array.unit_id_of_work);
        int[] unit_id =new int[unitIdOfWork.length];
        for (int i = 0; i<length; i++){
            unit_id[i] = Integer.valueOf(unitIdOfWork[i]);
        }
        String[] cost = res.getStringArray(R.array.cost_of_work);
        float[] cost_of_work = new float[cost.length];
        for (int i = 0; i < length; i++){
            cost_of_work[i] = Float.valueOf(cost[i]);
        }
        // проходим через массив и вставляем записи в таблицу
        for (int i = 0; i < length; i++){
            // Добавляем записи в таблицу
            this.InsertCost(db, values, work_id[i], unit_id[i], cost_of_work[i]);
        }
        Log.d(TAG, "createDefaultCost unit_id.length = " + unit_id.length );
    }

    public void createDefaultCostMat(SQLiteDatabase db){
        Log.i(TAG, "SmetaOpenHelper.createDefaultCostMat...");
        ContentValues values = new ContentValues();

        // Получим ресурс
        Resources res = fContext.getResources();

        int [] mat_id = this.getIdFromMats(db);
        int length = mat_id.length;
        // Получим массив строк из ресурсов
        String[] unitIdOfMat = res.getStringArray(R.array.unit_id_of_mat);
        int[] unit_id =new int[unitIdOfMat.length];
        for (int i = 0; i<length; i++){
            unit_id[i] = Integer.valueOf(unitIdOfMat[i]);
        }

        String[] costMat = res.getStringArray(R.array.cost_of_mat);
        float[] cost_of_mat = new float[costMat.length];
        for (int i = 0; i < length; i++){
            cost_of_mat[i] = Float.valueOf(costMat[i]);
        }

        // проходим через массив и вставляем записи в таблицу
        for (int i = 0; i < length; i++){
            // Добавляем записи в таблицу
            this.InsertCostMat(db, values, mat_id[i], unit_id[i], cost_of_mat[i]);
        }
        Log.d(TAG, "createDefaultCost unit_id.length = " +
                unit_id.length + "  cost_of_mat.length" + cost_of_mat.length);
    }

    public void InsertCost( SQLiteDatabase db,ContentValues values,
                            long work_id,  long unit_id, float cost){
        values.put(CostWork.COST_WORK_ID, work_id);
        values.put(CostWork.COST_UNIT_ID, unit_id);
        values.put(CostWork.COST_COST, cost);
        db.insert(CostWork.TABLE_NAME, null, values);
    }

    public void InsertCostMat( SQLiteDatabase db,ContentValues values,
                            long work_id,  long unit_id, float cost){
        values.put(CostMat.COST_MAT_ID, work_id);
        values.put(CostMat.COST_MAT_UNIT_ID, unit_id);
        values.put(CostMat.COST_MAT_COST, cost);
        db.insert(CostMat.TABLE_NAME, null, values);
    }

    //получаем курсор с Work_Type_id  и делаем массив id
    public int[] getArrayTypeId(SQLiteDatabase db) {
        Log.i(TAG, "SmetaOpenHelper.getArrayTypeId ... ");
        String typeId = " SELECT " + TypeWork._ID  + " FROM " + TypeWork.TABLE_NAME;
        Cursor cursor = db.rawQuery(typeId, null);
        Log.i(TAG, "SmetaOpenHelper.getArrayTypeId cursor.getCount() = " + cursor.getCount());
        int[] type_id = new int[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            type_id[position] = cursor.getInt(cursor.getColumnIndex(TypeWork._ID));
            //Log.i(TAG, "SmetaOpenHelper.getArrayTypeId position = " + position);
        }
        cursor.close();
        return type_id;
    }

    //получаем курсор с Work_Type_id  и делаем массив id
    public int[] getArrayTypeIdMat(SQLiteDatabase db) {

        Log.i(TAG, "SmetaOpenHelper.getArrayTypeIdMat ... ");
        String typeIdMat = " SELECT " + TypeMat._ID  + " FROM " + TypeMat.TABLE_NAME;
        Cursor cursor = db.rawQuery(typeIdMat, null);
        Log.i(TAG, "SmetaOpenHelper.getArrayTypeIdMat cursor.getCount() = " + cursor.getCount());
        int[] type_id_mat = new int[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            type_id_mat[position] = cursor.getInt(cursor.getColumnIndex(TypeMat._ID));
            //Log.i(TAG, "SmetaOpenHelper.getArrayTypeId position = " + position);
        }
        cursor.close();
        return type_id_mat;
    }

    //получаем  все ID таблицы Work ***
    public int[] getIdFromWorks(SQLiteDatabase db) {
        Log.d(TAG, "getIdFromWorks...");
        Cursor cursor = db.query(
                Work.TABLE_NAME,   // таблица
                new String[]{Work._ID},            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки
        int[] currentID = null;
        if (cursor.getCount() != 0) {
            currentID = new int[cursor.getCount()];
            while (cursor.moveToNext()) {
                // Узнаем индекс каждого столбца
                int idColumnIndex = cursor.getColumnIndex(Work._ID);
                // Используем индекс для получения строки или числа
                currentID[cursor.getPosition()] = cursor.getInt(idColumnIndex);
            }
        }
            cursor.close();
        Log.d(TAG, "getIdFromWorks currentID.length = " +currentID.length);
        return currentID;
    }

    //получаем  все ID таблицы Mat ***
    public int[] getIdFromMats(SQLiteDatabase db) {

        Log.d(TAG, "getIdFromMats...");
        Cursor cursor = db.query(
                Mat.TABLE_NAME,   // таблица
                new String[]{Mat._ID},            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки
        int[] currentID = null;
        if (cursor.getCount() != 0) {
            currentID = new int[cursor.getCount()];
            while (cursor.moveToNext()) {
                // Узнаем индекс каждого столбца
                int idColumnIndex = cursor.getColumnIndex(Mat._ID);
                // Используем индекс для получения строки или числа
                currentID[cursor.getPosition()] = cursor.getInt(idColumnIndex);
            }
        }
        cursor.close();
        Log.d(TAG, "getIdFromMats currentID.length = " + currentID.length);
        return currentID;
    }

}
