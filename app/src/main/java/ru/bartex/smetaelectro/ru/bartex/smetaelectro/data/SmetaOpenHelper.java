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
        //получаем количество файлов (сохранённых подходов) в базе
    //public int getFilesCount()
        //формируем строку с датой
    //public String getDateString()
        //формируем строку с временем
    //public String getTimeString()
        //Метод для добавления нового файла в таблицу файлов
    //public long addFile(DataFile file)
        //получаем ID по имени файла
    //public long getIdFromFileName(String name)
        //создаём категории из ресурсов
    //public void createDefaultCategory(SQLiteDatabase db)
        //получаем курсор с названиями категорий и отметкой о заходе в категорию
    //public Cursor getCategoryNames()
        //получаем курсор с Category._id  и делаем массив id
    //public int[] getArrayCategoryId()
        //создаём типы из ресурсов и получаем COLUMN_TYPE_CATEGORY_ID из  this.getArrayCategoryId()
    //public void createDefaultType(SQLiteDatabase db)
        //получаем курсор с названиями типов работ  и отметкой о заходе в тип
    //public Cursor getTypeNames()
        //Метод для изменения отметки категории
   //public void updateCategoryMark(int mark, long cat_Id)
        //получаем ID по имени категории
    //public long getIdFromCategoryName(String name)
        //обновляем отметку входа в тип работы
    //public void updateTypeMark(int mark, long type_Id)
        //получаем ID по имени типа работы
    //public long getIdFromTypeName(String name)

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

    //получаем количество файлов (сохранённых подходов) в базе
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

    // Добавляем имя и другие параметры сметы в таблицу FileWork (из активности SmetaNewName)
    public long addFile(String fileName, String adress, String description) {

        SQLiteDatabase db = getWritableDatabase();

        //получаем дату и время в нужном для базы данных формате
        String dateFormat = this.getDateString();
        String timeFormat = this.getTimeString();

        ContentValues cv = new ContentValues();
        cv.put(FileWork.FILE_NAME, fileName);
        cv.put(FileWork.ADRESS, adress);
        cv.put(FileWork.FILE_NAME_DATE, dateFormat);
        cv.put(FileWork.FILE_NAME_TIME, timeFormat);
        cv.put(FileWork.DESCRIPTION_OF_FILE, description);
        // вставляем строку
        long ID = db.insert(FileWork.TABLE_NAME, null, cv);
        // закрываем соединение с базой
        db.close();
        Log.d(TAG, "MyDatabaseHelper.createDefaultFile...  file1_id = " + ID);
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

        if ((cursor != null) && (cursor.getCount() != 0)) {
            cursor.moveToFirst();
            // Узнаем индекс  столбца FileWork._ID
            int idColumnIndex = cursor.getColumnIndex(FileWork._ID);
            // Используем индекс для получения строки или числа
            currentID = cursor.getLong(idColumnIndex);
        } else {
            currentID = -1;
        }
        Log.d(TAG, "getIdFromFileName currentID = " + currentID);
        if (cursor != null) {
            cursor.close();
        }
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

    //получаем курсор с названиями категорий и отметкой о заходе в категорию
    public Cursor getCategoryNames() {
        Log.i(TAG, "SmetaOpenHelper.getCategoryNames ... ");
        String categoryNames = " SELECT " + CategoryWork._ID + " , " +
                CategoryWork.CATEGORY_NAME + " FROM " + CategoryWork.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(categoryNames, null);
        return cursor;
    }

    //получаем курсор с названиями категорий и отметкой о заходе в категорию
    public Cursor getMatCategoryNames() {
        Log.i(TAG, "SmetaOpenHelper.getMatCategoryNames ... ");
        String matCategoryNames = " SELECT " + CategoryMat._ID + " , " +
                CategoryMat.CATEGORY_MAT_NAME + " FROM " + CategoryMat.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(matCategoryNames, null);
        Log.i(TAG, "SmetaOpenHelper.getMatCategoryNames cursor.getCount() = " + cursor.getCount());
        return cursor;
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
        ii = 13; //work_name_type_hren
        for (int i = 0; i <work_name_type_musor.length; i++ ){
            this.InsertWork(db, values, type_id[ii],  work_name_type_musor[i]);
        }
        ii = 14; //work_name_type_full_hren
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
        //*******ИСПРАВИТЬ ПОСЛЕ НАЗНАЧЕНИЯ ЦЕН************

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
        return type_id_mat;
    }

    //получаем курсор с названиями типов работ
    public Cursor getTypeNames(long cat_id) {
        Log.i(TAG, "SmetaOpenHelper.getTypeNames ... ");
        String typeNames = " SELECT " + TypeWork._ID + " , " + TypeWork.TYPE_CATEGORY_ID +
                " , " + TypeWork.TYPE_NAME + " FROM " + TypeWork.TABLE_NAME +
                " WHERE " + TypeWork.TYPE_CATEGORY_ID  + " = ?" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(typeNames, new String[]{String.valueOf(cat_id)});
        Log.i(TAG, "SmetaOpenHelper.getTypeNames cursor.getCount() =  " + cursor.getCount()+
                "  cat_id = " + cat_id);
        return cursor;
    }

    //получаем курсор с названиями типов работ
    public Cursor getTypeMatNames(long cat_id) {
        Log.i(TAG, "SmetaOpenHelper.getTypeMatNames ... ");
        String typeMatNames = " SELECT " + TypeMat._ID + " , " + TypeMat.TYPE_MAT_CATEGORY_ID +
                " , " + TypeMat.TYPE_MAT_NAME + " FROM " + TypeMat.TABLE_NAME +
                " WHERE " + TypeMat.TYPE_MAT_CATEGORY_ID  + " = ?" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(typeMatNames, new String[]{String.valueOf(cat_id)});
        Log.i(TAG, "SmetaOpenHelper.getTypeMatNames cursor.getCount() =  " + cursor.getCount());
        return cursor;
    }

    //получаем курсор с названиями типов работ по всем категориям
    public Cursor getTypeMatNamesAllCategories() {
        Log.i(TAG, "SmetaOpenHelper.getTypeMatNamesAllCategories ... ");
        String typeMatNames = " SELECT " + TypeMat._ID + " , " + TypeMat.TYPE_MAT_CATEGORY_ID +
                " , " + TypeMat.TYPE_MAT_NAME + " FROM " + TypeMat.TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(typeMatNames, null);
        Log.i(TAG, "SmetaOpenHelper.getTypeMatNamesAllCategories cursor.getCount() =  " + cursor.getCount());
        return cursor;
    }

    //получаем курсор с названиями  работ  и отметкой о заходе в тип для типа с type_id
    public Cursor getWorkNames(long type_id) {
        Log.i(TAG, "SmetaOpenHelper.getWorkNames ... ");
        String typeNames = " SELECT " + Work._ID + " , " + Work.WORK_TYPE_ID + " , " +
                Work.WORK_NAME + " FROM " + Work.TABLE_NAME +
                " WHERE " + Work.WORK_TYPE_ID  + " = ?" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(typeNames, new String[]{String.valueOf(type_id)});
        Log.i(TAG, "SmetaOpenHelper.getWorkNames cursor.getCount() =  " + cursor.getCount());
        return cursor;
    }

    //получаем курсор с названиями  материалов
    public Cursor getMatNamesAllTypes() {
        Log.i(TAG, "SmetaOpenHelper.getMatNamesAllTypes ... ");
        String matNames = " SELECT " + Mat._ID + " , " +
                Mat.MAT_NAME + " FROM " + Mat.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(matNames, null);
        Log.i(TAG, "SmetaOpenHelper.getMatNamesAllTypes cursor.getCount() =  " + cursor.getCount());
        return cursor;
    }

    //получаем ID по имени категории
    public long getIdFromCategoryName(String name) {
        Log.i(TAG, "SmetaOpenHelper.getIdFromCategoryName ... ");
        long currentID;
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                CategoryWork.TABLE_NAME,   // таблица
                new String[]{CategoryWork._ID},            // столбцы
                CategoryWork.CATEGORY_NAME + "=?",                  // столбцы для условия WHERE
                new String[]{name},                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки
        Log.i(TAG, " SmetaOpenHelper.getIdFromCategoryName - cursor.getCount()=" + cursor.getCount());
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            // Узнаем индекс каждого столбца
            int idColumnIndex = cursor.getColumnIndex(CategoryWork._ID);
            // Используем индекс для получения строки или числа
            currentID = cursor.getLong(idColumnIndex);
        } else {
            currentID = -1;
        }
        Log.d(TAG, "getIdFromCategoryName currentID = " + currentID);
        cursor.close();
        return currentID;
    }

    //получаем ID по имени типа работы
    public long getIdFromTypeName(String name) {
        long currentID;
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TypeWork.TABLE_NAME,   // таблица
                new String[]{TypeWork._ID},            // столбцы
                TypeWork.TYPE_NAME + "=?",                  // столбцы для условия WHERE
                new String[]{name},                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        if ((cursor != null) && (cursor.getCount() != 0)) {
            cursor.moveToFirst();
            // Узнаем индекс каждого столбца
            int idColumnIndex = cursor.getColumnIndex(TypeWork._ID);
            // Используем индекс для получения строки или числа
            currentID = cursor.getLong(idColumnIndex);
        } else {
            currentID = -1;
        }
        Log.d(TAG, "getIdFromTypeName currentID = " + currentID);
        if (cursor != null) {
            cursor.close();
        }
        return currentID;
    }

    //получаем ID по имени  работы
    public long getIdFromWorkName(String name) {
        long currentID;
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                Work.TABLE_NAME,   // таблица
                new String[]{Work._ID},            // столбцы
                Work.WORK_NAME + "=?",                  // столбцы для условия WHERE
                new String[]{name},                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        if ((cursor != null) && (cursor.getCount() != 0)) {
            cursor.moveToFirst();
            // Узнаем индекс каждого столбца
            int idColumnIndex = cursor.getColumnIndex(Work._ID);
            // Используем индекс для получения строки или числа
            currentID = cursor.getLong(idColumnIndex);
        } else {
            currentID = -1;
        }
        Log.d(TAG, "getIdFromWorkName currentID = " + currentID);
        if (cursor != null) {
            cursor.close();
        }
        return currentID;
    }

    //получаем  все ID таблицы Work
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
        if ((cursor != null) && (cursor.getCount() != 0)) {
            currentID = new int[cursor.getCount()];
            while (cursor.moveToNext()) {
                // Узнаем индекс каждого столбца
                int idColumnIndex = cursor.getColumnIndex(Work._ID);
                // Используем индекс для получения строки или числа
                currentID[cursor.getPosition()] = cursor.getInt(idColumnIndex);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        Log.d(TAG, "getIdFromWorks currentID.length = " +currentID.length);
        return currentID;
    }

    //получаем  все ID таблицы Mat
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

    //получаем имя работы по её id
    public String getWorkNameById(long work_id){
        Log.i(TAG, "SmetaOpenHelper.getWorkNameById ... ");
        String currentWorkName = "";
        String workName = " SELECT " + Work._ID + " , " +  Work.WORK_NAME +
                " FROM " + Work.TABLE_NAME +
                " WHERE " + Work._ID  + " = ?" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(workName, new String[]{String.valueOf(work_id)});
        if ((cursor != null) && (cursor.getCount() != 0)) {
            cursor.moveToFirst();
            // Узнаем индекс каждого столбца
            int idColumnIndex = cursor.getColumnIndex(Work.WORK_NAME);
            // Используем индекс для получения строки или числа
            currentWorkName = cursor.getString(idColumnIndex);
        }
        Log.d(TAG, "getWorkNameById currentWorkName = " + currentWorkName);
        if (cursor != null) {
            cursor.close();
        }
        return currentWorkName;
    }



    //получаем стоимость работы по её id
    public float getWorkCostById(long work_id){
        Log.i(TAG, "SmetaOpenHelper.getWorkCostById ... ");
       float costOfWork = 0;
        String cost = " SELECT " + CostWork.COST_COST +
                " FROM " + CostWork.TABLE_NAME +
                " WHERE " + CostWork.COST_WORK_ID  + " = ?" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(cost, new String[]{String.valueOf(work_id)});
        Log.d(TAG, "getWorkCostById cursor.getCount() = " + cursor.getCount());

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            // Узнаем индекс каждого столбца
            int idColumnIndex = cursor.getColumnIndex(CostWork.COST_COST);
            // Используем индекс для получения строки или числа
            costOfWork = cursor.getFloat(idColumnIndex);
        }
        Log.d(TAG, "getWorkCostById costOfWork = " + costOfWork);
        cursor.close();
        return costOfWork;
    }

    //вывод в лог всех строк базы
    public void displayTableCost() {
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = this.getReadableDatabase();

        // Зададим условие для выборки - список столбцов
        String[] projection = {
                CostWork._ID,
                CostWork.COST_WORK_ID,
                CostWork.COST_UNIT_ID,
                CostWork.COST_COST,
                CostWork.COST_NUMBER};

        // Делаем запрос
        Cursor cursor = db.query(
                CostWork.TABLE_NAME,   // таблица
                projection,            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки
        try {
            // Проходим через все ряды в таблице CostWork
            while (cursor.moveToNext()) {
                // Используем индекс для получения строки или числа
                int currentID = cursor.getInt(
                        cursor.getColumnIndex(CostWork._ID));
                int current_WORK_ID = cursor.getInt(
                        cursor.getColumnIndex(CostWork.COST_WORK_ID));
                String current_UNIT = cursor.getString(
                        cursor.getColumnIndex(CostWork.COST_UNIT_ID));
                float current_COST = cursor.getFloat(
                        cursor.getColumnIndex(CostWork.COST_COST));
                int current_NUMBER = cursor.getInt(
                        cursor.getColumnIndex(CostWork.COST_NUMBER));
                // Выводим построчно значения каждого столбца
                Log.d(TAG, "\n" + "ID = " + currentID + " - " +
                        " WORK_ID = " + current_WORK_ID + " - " +
                        " UNIT = " + current_UNIT + " - " +
                        " COST = " + current_COST + " - " +
                        " NUMBER = " + current_NUMBER);
            }

        } finally {
            // Всегда закрываем курсор после чтения
            cursor.close();
        }
    }

    //вывод в лог всех строк базы
    public void displayTableCostMat() {
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = this.getReadableDatabase();

        // Зададим условие для выборки - список столбцов
        String[] projection = {
                CostMat._ID,
                CostMat.COST_MAT_ID,
                CostMat.COST_MAT_UNIT_ID,
                CostMat.COST_MAT_COST,
                CostMat.COST_MAT_NUMBER};

        // Делаем запрос
        Cursor cursor = db.query(
                CostMat.TABLE_NAME,   // таблица
                projection,            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки
        try {
            // Проходим через все ряды в таблице CostWork
            while (cursor.moveToNext()) {
                // Используем индекс для получения строки или числа
                int currentID = cursor.getInt(
                        cursor.getColumnIndex(CostMat._ID));
                int current_WORK_ID = cursor.getInt(
                        cursor.getColumnIndex(CostMat.COST_MAT_ID));
                String current_UNIT = cursor.getString(
                        cursor.getColumnIndex(CostMat.COST_MAT_UNIT_ID));
                float current_COST = cursor.getFloat(
                        cursor.getColumnIndex(CostMat.COST_MAT_COST));
                int current_NUMBER = cursor.getInt(
                        cursor.getColumnIndex(CostMat.COST_MAT_NUMBER));
                // Выводим построчно значения каждого столбца
                Log.d(TAG, "\n" + "ID = " + currentID + " - " +
                        " MAT_ID = " + current_WORK_ID + " - " +
                        " UNIT = " + current_UNIT + " - " +
                        " MAT_COST = " + current_COST + " - " +
                        " MAT_NUMBER = " + current_NUMBER);
            }

        } finally {
            // Всегда закрываем курсор после чтения
            cursor.close();
        }
    }

    //получаем единицы измерения с помощью двух запросов, так как при одном запросе возникает
    //ошибка связанная с тем, что в таблицах есть одинаковые поля _ID
    public String getCostUnitById1(long work_id){
        Log.i(TAG, "SmetaOpenHelper.getCostUnitById ... ");
        String unitName = "";
        int unitId = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        String unit = " SELECT " +  CostWork.COST_UNIT_ID +
                " FROM " + CostWork.TABLE_NAME  +
                " WHERE " + CostWork.COST_WORK_ID  + " = " +  String.valueOf(work_id);

        Cursor cursor = db.rawQuery(unit, null);

        Log.d(TAG, "getCostUnitById cursor.getCount() = " + cursor.getCount());

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            // Узнаем индекс  столбца
            int idColumnIndex = cursor.getColumnIndex(CostWork.COST_UNIT_ID);
            // Используем индекс для получения строки или числа
            unitId = cursor.getInt(idColumnIndex);
            Log.d(TAG, "getWorkCostById unitId = " + unitId);

            String unitN = " SELECT " +  Unit.UNIT_NAME +
                    " FROM " + Unit.TABLE_NAME +
                    " WHERE " + Unit._ID  + " = " + String.valueOf(unitId);
            Cursor cursor1 = db.rawQuery(unitN, null);
            Log.d(TAG, "getCostUnitById cursor1.getCount() = " + cursor1.getCount());

            if (cursor1.getCount() != 0) {
                cursor1.moveToFirst();
                // Узнаем индекс  столбца
                int idColumnIndex1 = cursor1.getColumnIndex(Unit.UNIT_NAME);
                // Используем индекс для получения строки или числа
                unitName = cursor1.getString(idColumnIndex1);
                Log.d(TAG, "getWorkCostById unitName = " + unitName);
            }
            cursor1.close();
        }
        cursor.close();
        return unitName;
    }

    //получаем единицы измерения с помощью вложенного запроса
    public String getCostUnitById(long work_id){
        Log.i(TAG, "SmetaOpenHelper.getCostUnitById ... ");
        String unitName = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String unit = " SELECT " +  Unit.UNIT_NAME +
                " FROM " + Unit.TABLE_NAME  +
                " WHERE " + Unit._ID + " IN " +
                "(" + " SELECT " + CostWork.COST_UNIT_ID +
                " FROM " + CostWork.TABLE_NAME +
                " WHERE " + CostWork.COST_WORK_ID  + " = " +  String.valueOf(work_id) + ")";

        Cursor cursor = db.rawQuery(unit, null);

        Log.d(TAG, "getCostUnitById cursor.getCount() = " + cursor.getCount());

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            // Узнаем индекс  столбца
            int idColumnIndex = cursor.getColumnIndex(Unit.UNIT_NAME);
            // Используем индекс для получения строки или числа
            unitName = cursor.getString(idColumnIndex);
            Log.d(TAG, "getCostUnitById unitName = " + unitName);
        }
            cursor.close();
        return unitName;
    }

    public long  insertRowInFW(long file_id, long work_id, long type_id, long category_id,
                              float cost_work, float  count, String unit, float summa){

        Log.i(TAG, "SmetaOpenHelper.insertRowInFW ... ");

        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(FW.FW_FILE_ID,file_id);
        cv.put(FW.FW_WORK_ID,work_id);
        cv.put(FW.FW_TYPE_ID,type_id);
        cv.put(FW.FW_CATEGORY_ID,category_id);
        cv.put(FW.FW_COST,cost_work);
        cv.put(FW.FW_COUNT,count);
        cv.put(FW.FW_UNIT,unit);
        cv.put(FW.FW_SUMMA,summa);

        // вставляем строку
        long ID = db.insert(FW.TABLE_NAME, null, cv);
        // закрываем соединение с базой
        db.close();
        Log.d(TAG, "MyDatabaseHelper.insertRowInFW  FW._ID = " + ID);

        return ID;
    }

    /**
     * Вставляет строку в таблицу FW
     */
    public long  insertRowInFW_Name(long file_id, long work_id, long type_id, long category_id,
                               float cost_work, float  count, String unit, float summa){

        Log.i(TAG, "SmetaOpenHelper.insertRowInFW_Name ... ");

        SQLiteDatabase db = getWritableDatabase();

        //получаем имя файла по его id
        String fileName = this.getFileNameById(file_id);
        Log.i(TAG, "SmetaOpenHelper.insertRowInFW_Name fileName =  " + fileName);
        //получаем имя работы по id работы
        String workName = this.getWorkNameById(work_id);
        Log.i(TAG, "SmetaOpenHelper.insertRowInFW_Name workName =  " + workName);
        //получаем имя типа работы по id типа работы
        String typeName = this.getTypeNameById(type_id);
        Log.i(TAG, "SmetaOpenHelper.insertRowInFW_Name typeName =  " + typeName);
        //получаем имя категории работы по id категории работы
        String catName = this.getCategoryNameById(category_id);
        Log.i(TAG, "SmetaOpenHelper.insertRowInFW_Name catName =  " + catName);

                ContentValues cv = new ContentValues();
        cv.put(FW.FW_FILE_ID,file_id);
        cv.put(FW.FW_FILE_NAME,fileName);
        cv.put(FW.FW_WORK_ID,work_id);
        cv.put(FW.FW_WORK_NAME,workName);
        cv.put(FW.FW_TYPE_ID,type_id);
        cv.put(FW.FW_TYPE_NAME,typeName);
        cv.put(FW.FW_CATEGORY_ID,category_id);
        cv.put(FW.FW_CATEGORY_NAME,catName);
        cv.put(FW.FW_COST,cost_work);
        cv.put(FW.FW_COUNT,count);
        cv.put(FW.FW_UNIT,unit);
        cv.put(FW.FW_SUMMA,summa);

        // вставляем строку
        long ID = db.insert(FW.TABLE_NAME, null, cv);
        // закрываем соединение с базой
        db.close();
        Log.d(TAG, "MyDatabaseHelper.insertRowInFW_Name  FW._ID = " + ID);

        return ID;
    }

    /**
     * обновляем количество и сумму в таблице FW
     */
    public void updateRowInFW_Count_Summa(
            long file_id, long work_id, float cost_work, String unit_work, float count, float summa){

        Log.i(TAG, "SmetaOpenHelper.updateRowInFW_Count_Summa ... ");
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(FW.FW_COST, cost_work);
        updatedValues.put(FW.FW_UNIT, unit_work);
        updatedValues.put(FW.FW_COUNT, count);
        updatedValues.put(FW.FW_SUMMA, summa);
        db.update(FW.TABLE_NAME, updatedValues,
                FW.FW_FILE_ID + " =? " +" AND " + FW.FW_WORK_ID + " =? ",
                new String[]{String.valueOf(file_id), String.valueOf(work_id)});
        Log.i(TAG, "SmetaOpenHelper.updateRowInFW_Count_Summa - count =" +
                count + "  summa = " + summa);
    }

    /**
     * Возвращает имя файла работы по его ID
     */
    public String getFileNameById( long file_id)  {
        Log.i(TAG, "SmetaOpenHelper.getFileNameById ... ");
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor mCursor = db.query(true, FileWork.TABLE_NAME,
                null,
                FileWork._ID + "=" + file_id,
                null, null, null, null, null);
        Log.d(TAG, "getFileNameById mCursor.getCount() = " + mCursor.getCount());
        if (mCursor.getCount() != 0) {
            mCursor.moveToFirst();
        }
        String fileName = mCursor.getString(mCursor.getColumnIndex(FileWork.FILE_NAME));
        Log.d(TAG, "getFileNameById fileName = " + fileName);
        mCursor.close();
        return fileName;
    }

    /**
     * Возвращает имя типа работы по его ID
     */
    public String getTypeNameById( long type_id){
        Log.i(TAG, "SmetaOpenHelper.getTypeNameById ... ");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mCursor = db.query(true, TypeWork.TABLE_NAME,
                null,
                TypeWork._ID + "=" + type_id,
                null, null, null, null, null);
        if ((mCursor != null) && (mCursor.getCount() != 0)) {
            mCursor.moveToFirst();
        }
        String typeName = mCursor.getString(mCursor.getColumnIndex(TypeWork.TYPE_NAME));
        Log.d(TAG, "getTypeNameById typeName = " + typeName);
        mCursor.close();
        return typeName;
    }

    /**
     * Возвращает имя категории работы по его ID
     */
    public String getCategoryNameById( long cat_id) throws SQLException {
        Log.i(TAG, "SmetaOpenHelper.getCategoryNameById ... ");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mCursor = db.query(true, CategoryWork.TABLE_NAME,
                null,
                CategoryWork._ID + "=" + cat_id,
                null, null, null, null, null);
        if ((mCursor != null) && (mCursor.getCount() != 0)) {
            mCursor.moveToFirst();
        }
        String catName = mCursor.getString(mCursor.getColumnIndex(CategoryWork.CATEGORY_NAME));
        Log.d(TAG, "getCategoryNameById catName = " + catName);
        mCursor.close();
        return catName;
    }

    //вывод в лог всех строк базы
    public void displayFW() {
        Log.i(TAG, "SmetaOpenHelper.displayFW ... ");
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = this.getReadableDatabase();

        // Зададим условие для выборки - список столбцов
        String[] projection = {
                FW._ID,
                FW.FW_FILE_ID,
                FW.FW_FILE_NAME,
                FW.FW_WORK_ID,
                FW.FW_WORK_NAME,
                FW.FW_TYPE_ID,
                FW.FW_TYPE_NAME,
                FW.FW_CATEGORY_ID,
                FW.FW_CATEGORY_NAME,
                FW.FW_COST,
                FW.FW_COUNT,
                FW.FW_UNIT,
                FW.FW_SUMMA};

        // Делаем запрос
        Cursor cursor = db.query(
                FW.TABLE_NAME,   // таблица
                projection,            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки
        try {
            // Проходим через все ряды в таблице CostWork
            while (cursor.moveToNext()) {
                // Используем индекс для получения строки или числа
                int currentID = cursor.getInt(
                        cursor.getColumnIndex(FW._ID));
                int current_FILE_ID = cursor.getInt(
                        cursor.getColumnIndex(FW.FW_FILE_ID));
                String current_FILE_NAME = cursor.getString(
                        cursor.getColumnIndex(FW.FW_FILE_NAME));
                int current_WORK_ID = cursor.getInt(
                        cursor.getColumnIndex(FW.FW_WORK_ID));
                String current_WORK_NAME = cursor.getString(
                        cursor.getColumnIndex(FW.FW_WORK_NAME));
                int current_TYPE_ID = cursor.getInt(
                        cursor.getColumnIndex(FW.FW_TYPE_ID));
                String current_TYPE_NAME = cursor.getString(
                        cursor.getColumnIndex(FW.FW_TYPE_NAME));
                int current_CATEGORY_ID = cursor.getInt(
                        cursor.getColumnIndex(FW.FW_CATEGORY_ID));
                String current_CATEGORY_NAME = cursor.getString(
                        cursor.getColumnIndex(FW.FW_CATEGORY_NAME));
                float current_COST = cursor.getFloat(
                        cursor.getColumnIndex(FW.FW_COST));
                int current_COUNT = cursor.getInt(
                        cursor.getColumnIndex(FW.FW_COUNT));
                String current_UNIT = cursor.getString(
                        cursor.getColumnIndex(FW.FW_UNIT));
                float current_SUMMA = cursor.getFloat(
                        cursor.getColumnIndex(FW.FW_SUMMA));
                // Выводим построчно значения каждого столбца
                Log.d(TAG, "\n" + "ID = " + currentID + "/" +
                        " FILE_ID = " + current_FILE_ID + "/" +
                        " FILE_NAME = " + current_FILE_NAME + "/" +
                        " WORK_ID = " + current_WORK_ID + "/" +
                        " WORK_NAME = " + current_WORK_NAME + "/" +
                        " TYPE_ID = " + current_TYPE_ID + "/" +
                        " TYPE_NAME = " + current_TYPE_NAME + "/" +
                        " CAT_ID = " + current_CATEGORY_ID + "/" +
                        " CAT_NAME = " + current_CATEGORY_NAME + "/" +
                        " COST = " + current_COST + "/" +
                        " COUNT = " + current_COUNT + "/" +
                        " UNIT = " + current_UNIT + "/" +
                        " SUMMA = " + current_SUMMA);
            }
        } finally {
            // Всегда закрываем курсор после чтения
            cursor.close();
        }
    }

    //вывод в лог всех строк базы
    public void displayFM() {
        Log.i(TAG, "%%%%%%%%%   SmetaOpenHelper.displayFM  %%%%%%%%% ... ");
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = this.getReadableDatabase();
        // Зададим условие для выборки - список столбцов
        String[] projection = {
                FM._ID,
                FM.FM_FILE_ID,
                FM.FM_FILE_NAME,
                FM.FM_MAT_ID,
                FM.FM_MAT_NAME,
                FM.FM_MAT_TYPE_ID,
                FM.FM_MAT_TYPE_NAME,
                FM.FM_MAT_CATEGORY_ID,
                FM.FM_MAT_CATEGORY_NAME,
                FM.FM_MAT_COST,
                FM.FM_MAT_COUNT,
                FM.FM_MAT_UNIT,
                FM.FM_MAT_SUMMA};

        // Делаем запрос
        Cursor cursor = db.query(
                FM.TABLE_NAME,   // таблица
                projection,            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки
        try {
            // Проходим через все ряды в таблице CostWork
            while (cursor.moveToNext()) {
                // Используем индекс для получения строки или числа
                int currentID = cursor.getInt(
                        cursor.getColumnIndex(FM._ID));
                int current_FILE_ID = cursor.getInt(
                        cursor.getColumnIndex(FM.FM_FILE_ID));
                String current_FILE_NAME = cursor.getString(
                        cursor.getColumnIndex(FM.FM_FILE_NAME));
                int current_WORK_ID = cursor.getInt(
                        cursor.getColumnIndex(FM.FM_MAT_ID));
                String current_WORK_NAME = cursor.getString(
                        cursor.getColumnIndex(FM.FM_MAT_NAME));
                int current_TYPE_ID = cursor.getInt(
                        cursor.getColumnIndex(FM.FM_MAT_TYPE_ID));
                String current_TYPE_NAME = cursor.getString(
                        cursor.getColumnIndex(FM.FM_MAT_TYPE_NAME));
                int current_CATEGORY_ID = cursor.getInt(
                        cursor.getColumnIndex(FM.FM_MAT_CATEGORY_ID));
                String current_CATEGORY_NAME = cursor.getString(
                        cursor.getColumnIndex(FM.FM_MAT_CATEGORY_NAME));
                float current_COST = cursor.getFloat(
                        cursor.getColumnIndex(FM.FM_MAT_COST));
                int current_COUNT = cursor.getInt(
                        cursor.getColumnIndex(FM.FM_MAT_COUNT));
                String current_UNIT = cursor.getString(
                        cursor.getColumnIndex(FM.FM_MAT_UNIT));
                float current_SUMMA = cursor.getFloat(
                        cursor.getColumnIndex(FM.FM_MAT_SUMMA));
                // Выводим построчно значения каждого столбца
                Log.d(TAG, "\n" + "ID = " + currentID + "/" +
                        " FILE_ID = " + current_FILE_ID + "/" +
                        " FILE_NAME = " + current_FILE_NAME + "/" +
                        " MAT_ID = " + current_WORK_ID + "/" +
                        " MAT_NAME = " + current_WORK_NAME + "/" +
                        " MAT_TYPE_ID = " + current_TYPE_ID + "/" +
                        " MAT_TYPE_NAME = " + current_TYPE_NAME + "/" +
                        " MAT_CATEGORY_ID = " + current_CATEGORY_ID + "/" +
                        " MAT_CATEGORY_NAME = " + current_CATEGORY_NAME + "/" +
                        " MAT_COST = " + current_COST + "/" +
                        " MAT_COUNT = " + current_COUNT + "/" +
                        " MAT_UNIT = " + current_UNIT + "/" +
                        " MAT_SUMMA = " + current_SUMMA);
            }
        } finally {
            // Всегда закрываем курсор после чтения
            cursor.close();
        }
    }

    //обновляем цену работы
    public void updateWorkCost(long work_Id, float cost, long unit_Id){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(CostWork.COST_COST, cost);
        updatedValues.put(CostWork.COST_UNIT_ID, unit_Id);
        updatedValues.put(CostWork.COST_NUMBER, 1);

        int countLine = db.update(CostWork.TABLE_NAME, updatedValues,
                CostWork.COST_WORK_ID + "=" + work_Id, null);
        Log.i(TAG, "SmetaOpenHelper.updateWorkCost - cost =" + cost + "  countLine = " + countLine);
    }

    //получаем все имена файлов со сметами
    public String[] getFileNames(){
        Log.i(TAG, "TempDBHelper.getFileNames ... ");
        String namesQuery = "SELECT  * FROM " + FileWork.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(namesQuery, null);
        String[] file_name = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            file_name[position] = cursor.getString(cursor.getColumnIndex(FileWork.FILE_NAME));
            Log.i(TAG, "SmetaOpenHelper.getFileNames position = " + position);
        }
        cursor.close();
        return file_name;
    }

    //получаем имена работ  по смете с id файла file_id
    public ArrayList<String>  getNameOfTypesAndWorkStructured(long file_id){

  /*
        String select_work_name = " SELECT " + FW.FW_WORK_NAME +
                " FROM " +  FW.TABLE_NAME  +
                " WHERE " + FW.FW_FILE_ID  + " = " + String.valueOf(file_id);

        String select_work_name_structured =
                " select  distinct " + FW.FW_CATEGORY_NAME  + " as Title "  + " , " +
                        FW.FW_CATEGORY_ID + " as CatID " + " ," + " 0 " + " as TypeID " +
                        " from " +  FW.TABLE_NAME  + " where " +  FW.FW_FILE_ID + " = " +  String.valueOf(file_id) +
                        " union " +
        " select distinct " + '-' + " || " + FW.FW_TYPE_NAME  + " as Title " + " , " +
                         FW.FW_CATEGORY_ID + " , " +  FW.FW_TYPE_ID + " from " +
                        FW.TABLE_NAME +  " where " +  FW.FW_FILE_ID  + " = " +  String.valueOf(file_id) +
                        " union " +
        " select distinct " + '-' + '-' + " || " + FW.FW_WORK_NAME  + " as Title "  + " , " +
        FW.FW_CATEGORY_ID + " , " +  FW.FW_TYPE_ID +  " from " + FW.TABLE_NAME +
                        " where " +  FW.FW_FILE_ID  + " = " +  String.valueOf(file_id) +
                        " order by " + " CatID " + "," + " TypeID " + "," + " Title " + " desc ";

        String select_work_name_structured =
                " select  distinct " + FW.FW_CATEGORY_NAME  + " as Title " +
                        " from " +  FW.TABLE_NAME  + " where " +  FW.FW_FILE_ID + " = " +  String.valueOf(file_id) +
                        " union " +
                        " select distinct " + FW.FW_TYPE_NAME  + " as Title " +
                        " from " + FW.TABLE_NAME +  " where " +  FW.FW_FILE_ID  + " = " +  String.valueOf(file_id) +
                        " union " +
                        " select distinct " + FW.FW_WORK_NAME  + " as Title "  +
                        " from " + FW.TABLE_NAME + " where " +  FW.FW_FILE_ID  + " = " +  String.valueOf(file_id);
*/
        String select_type_name_structured =
                " select  distinct " + FW.FW_TYPE_NAME  +
                        " from " + FW.TABLE_NAME +
                        " where " +  FW.FW_FILE_ID  + " = " +  String.valueOf(file_id)+
                " order by " + FW.FW_TYPE_ID ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select_type_name_structured, null);
        Log.i(TAG, "TempDBHelper.getNameOfTypesAndWorkStructured cursor.getCount()  " + cursor.getCount());

        ArrayList<String> arrayList = new ArrayList<>();
        String[] type_name = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            type_name[position] = cursor.getString(cursor.getColumnIndex(FW.FW_TYPE_NAME));
            arrayList.add((position+1) + " " + type_name[position]);
            long type_id = this.getIdFromTypeName(type_name[position]);
            Log.i(TAG, "SmetaOpenHelper.getNameOfTypesAndWorkStructured type_name[position] = " +
                    type_name[position] + " type_id = " + type_id);

            String select_work_name_structured =
                    " select distinct " + FW.FW_WORK_NAME  +
                            " from " + FW.TABLE_NAME +
                            " where " +  FW.FW_FILE_ID  + " = " +  String.valueOf(file_id)+
                            " and " +  FW.FW_TYPE_ID + " = " +  String.valueOf(type_id);
            Cursor cursor1 = db.rawQuery(select_work_name_structured, null);
            Log.i(TAG, "TempDBHelper.getNameOfTypesAndWorkStructured cursor1.getCount()  " + cursor1.getCount());

            String[] work_name = new String[cursor1.getCount()];
            while (cursor1.moveToNext()){
                int position1 = cursor1.getPosition();
                work_name[position1] = cursor1.getString(cursor1.getColumnIndex(FW.FW_WORK_NAME));
                arrayList.add("   " + (position+1)+ "." + (position1+1) + " " + work_name[position1]);
                Log.i(TAG, "SmetaOpenHelper.getNameOfTypesAndWorkStructured work_name[position1] = " + work_name[position1]);
            }
            cursor1.close();
        }
            cursor.close();

        return arrayList;
    }

    //получаем имена работ  по смете с id файла file_id
    public String[]  getNameOfWork(long file_id){
        String select_work_name = " SELECT " + FW.FW_WORK_NAME +
                " FROM " +  FW.TABLE_NAME  +
                " WHERE " + FW.FW_FILE_ID  + " = " + String.valueOf(file_id);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select_work_name, null);
        Log.i(TAG, "TempDBHelper.getNameOfWork cursor.getCount()  " + cursor.getCount());
        String[] work_name = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            work_name[position] = cursor.getString(cursor.getColumnIndex(FW.FW_WORK_NAME));
            Log.i(TAG, "SmetaOpenHelper.getNameOfWork work_name[position] = " + work_name[position]);
        }
        cursor.close();
        return work_name;
    }

    //получаем имена материалов  по смете с id файла file_id
    public String[]  getNameOfMat(long file_id){
        String select_mat_name = " SELECT " + FM.FM_MAT_NAME +
                " FROM " +  FM.TABLE_NAME  +
                " WHERE " + FM.FM_FILE_ID  + " = " + String.valueOf(file_id);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select_mat_name, null);
        Log.i(TAG, "TempDBHelper.getNameOfMat cursor.getCount()  " + cursor.getCount());
        String[] mat_name = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            mat_name[position] = cursor.getString(cursor.getColumnIndex(FM.FM_MAT_NAME));
            Log.i(TAG, "SmetaOpenHelper.getNameOfMat work_name[position] = " + mat_name[position]);
        }
            cursor.close();
        return mat_name;
    }

    //получаем имена работ  по смете с id файла file_id и id типа type_id
    public String[]  getNameOfWorkSelectedType(long file_id, long type_id){

        SQLiteDatabase db = this.getWritableDatabase();

        String select_work_name = " SELECT " + FW.FW_WORK_NAME +
                " FROM " +  FW.TABLE_NAME  +
                " WHERE " + FW.FW_FILE_ID  + " = " + String.valueOf(file_id)+
                " AND " + FW.FW_TYPE_ID  + " = " + String.valueOf(type_id);
        Cursor cursor = db.rawQuery(select_work_name, null);
        Log.i(TAG, "TempDBHelper.getNameOfWorkSelectedType cursor.getCount()  " + cursor.getCount());
        String[] work_name = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            work_name[position] = cursor.getString(cursor.getColumnIndex(FW.FW_WORK_NAME));
            Log.i(TAG, "SmetaOpenHelper.getNameOfWorkSelectedType work_name[position] = " + work_name[position]);
        }
            cursor.close();

        return work_name;
    }

    //получаем имена работ  по смете с id файла file_id и id типа type_id
    public String[]  getNameOfMatkSelectedType(long file_id, long type_mat_id){

        SQLiteDatabase db = this.getWritableDatabase();

        String select_mat_name = " SELECT " + FM.FM_MAT_NAME +
                " FROM " +  FM.TABLE_NAME  +
                " WHERE " + FM.FM_FILE_ID  + " = " + String.valueOf(file_id)+
                " AND " + FM.FM_MAT_TYPE_ID  + " = " + String.valueOf(type_mat_id);
        Cursor cursor = db.rawQuery(select_mat_name, null);
        Log.i(TAG, "TempDBHelper.getNameOfMatkSelectedType cursor.getCount()  " + cursor.getCount());
        String[] mat_name = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            mat_name[position] = cursor.getString(cursor.getColumnIndex(FM.FM_MAT_NAME));
            Log.i(TAG, "SmetaOpenHelper.getNameOfMatkSelectedType mat_name[position] = " + mat_name[position]);
        }
        cursor.close();
        return mat_name;
    }


    //получаем расценки работ  по смете с id файла file_id
    public float[]  getCostOfWork(long file_id){
        String select_work_cost = " SELECT " + FW.FW_COST +
                " FROM " +  FW.TABLE_NAME  +
                " WHERE " + FW.FW_FILE_ID  + " = " + String.valueOf(file_id);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select_work_cost, null);
        Log.i(TAG, "TempDBHelper.getCostOfWork cursor.getCount()  " + cursor.getCount());
        float[] work_cost = new float[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            work_cost[position] = cursor.getFloat(cursor.getColumnIndex(FW.FW_COST));
            Log.i(TAG, "SmetaOpenHelper.getCostOfWork work_cost[position] = " + work_cost[position]);
        }
        cursor.close();
        return work_cost;
    }

    //получаем цены материалов  по смете с id файла file_id
    public float[]  getCostOfMat(long file_id){
        String select_mat_cost = " SELECT " + FM.FM_MAT_COST +
                " FROM " +  FM.TABLE_NAME  +
                " WHERE " + FM.FM_FILE_ID  + " = " + String.valueOf(file_id);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select_mat_cost, null);
        Log.i(TAG, "TempDBHelper.getCostOfMat cursor.getCount()  " + cursor.getCount());
        float[] mat_cost = new float[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            mat_cost[position] = cursor.getFloat(cursor.getColumnIndex(FM.FM_MAT_COST));
            Log.i(TAG, "SmetaOpenHelper.getCostOfMat mat_cost[position] = " + mat_cost[position]);
        }
            cursor.close();
        return mat_cost;
    }

    //получаем расценки работ  по смете с id файла file_id и id типа type_id
    public float[]  getCostOfWorkSelectedType(long file_id, long type_id){
        String select_work_cost = " SELECT " + FW.FW_COST +
                " FROM " +  FW.TABLE_NAME  +
                " WHERE " + FW.FW_FILE_ID  + " = " + String.valueOf(file_id)+
                " AND " + FW.FW_TYPE_ID  + " = " + String.valueOf(type_id);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select_work_cost, null);
        Log.i(TAG, "TempDBHelper.getCostOfWorkSelectedType cursor.getCount()  " + cursor.getCount());
        float[] work_cost = new float[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            work_cost[position] = cursor.getFloat(cursor.getColumnIndex(FW.FW_COST));
            Log.i(TAG, "SmetaOpenHelper.getCostOfWorkSelectedType work_cost[position] = " + work_cost[position]);
        }
            cursor.close();
        return work_cost;
    }

    //получаем расценки работ  по смете с id файла file_id и id типа type_id
    public float[]  getCostOfMatSelectedType(long file_id, long type_mat_id){
        String select_mat_cost = " SELECT " + FM.FM_MAT_COST +
                " FROM " +  FM.TABLE_NAME  +
                " WHERE " + FM.FM_FILE_ID  + " = " + String.valueOf(file_id)+
                " AND " + FM.FM_MAT_TYPE_ID  + " = " + String.valueOf(type_mat_id);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select_mat_cost, null);
        Log.i(TAG, "TempDBHelper.getCostOfMatSelectedType cursor.getCount()  " + cursor.getCount());
        float[] mat_cost = new float[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            mat_cost[position] = cursor.getFloat(cursor.getColumnIndex(FM.FM_MAT_COST));
            Log.i(TAG, "SmetaOpenHelper.getCostOfMatSelectedType mat_cost[position] = " + mat_cost[position]);
        }
        cursor.close();
        return mat_cost;
    }


    //получаем количество работ  по смете с id файла file_id
    public float[]  getAmountOfWork(long file_id){
        String select_work_amount = " SELECT " + FW.FW_COUNT +
                " FROM " +  FW.TABLE_NAME  +
                " WHERE " + FW.FW_FILE_ID  + " = " + String.valueOf(file_id);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select_work_amount, null);
        Log.i(TAG, "TempDBHelper.getAmountOfWork cursor.getCount()  " + cursor.getCount());
        float[] work_amount = new float[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            work_amount[position] = cursor.getFloat(cursor.getColumnIndex(FW.FW_COUNT));
            Log.i(TAG, "SmetaOpenHelper.getAmountOfWork work_amount[position] = " + work_amount[position]);
        }
            cursor.close();
        return work_amount;
    }

    //получаем количество материалов  по смете с id файла file_id
    public float[]  getAmountOfMat(long file_id){
        String select_mat_amount = " SELECT " + FM.FM_MAT_COUNT +
                " FROM " +  FM.TABLE_NAME  +
                " WHERE " + FM.FM_FILE_ID  + " = " + String.valueOf(file_id);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select_mat_amount, null);
        Log.i(TAG, "TempDBHelper.getAmountOfWork cursor.getCount()  " + cursor.getCount());
        float[] mat_amount = new float[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            mat_amount[position] = cursor.getFloat(cursor.getColumnIndex(FM.FM_MAT_COUNT));
            Log.i(TAG, "SmetaOpenHelper.getAmountOfWork work_amount[position] = " + mat_amount[position]);
        }
        cursor.close();
        return mat_amount;
    }

    //получаем количество работ  по смете с id файла file_id и id типа type_id
    public float[]  getAmountOfWorkSelectedType(long file_id, long type_id){
        String select_work_amount = " SELECT " + FW.FW_COUNT +
                " FROM " +  FW.TABLE_NAME  +
                " WHERE " + FW.FW_FILE_ID  + " = " + String.valueOf(file_id)+
                " AND " + FW.FW_TYPE_ID  + " = " + String.valueOf(type_id);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select_work_amount, null);
        Log.i(TAG, "TempDBHelper.getAmountOfWorkSelectedType cursor.getCount()  " + cursor.getCount());
        float[] work_amount = new float[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            work_amount[position] = cursor.getFloat(cursor.getColumnIndex(FW.FW_COUNT));
            Log.i(TAG, "SmetaOpenHelper.getAmountOfWorkSelectedType work_amount[position] = " + work_amount[position]);
        }
            cursor.close();
        return work_amount;
    }

    //получаем количество работ  по смете с id файла file_id и id типа type_id
    public float[]  getAmountOfMatSelectedType(long file_id, long type_mat_id){
        String select_mat_amount = " SELECT " + FM.FM_MAT_COUNT +
                " FROM " +  FM.TABLE_NAME  +
                " WHERE " + FM.FM_FILE_ID  + " = " + String.valueOf(file_id)+
                " AND " + FM.FM_MAT_TYPE_ID  + " = " + String.valueOf(type_mat_id);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select_mat_amount, null);
        Log.i(TAG, "TempDBHelper.getAmountOfMatSelectedType cursor.getCount()  " + cursor.getCount());
        float[] mat_amount = new float[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            mat_amount[position] = cursor.getFloat(cursor.getColumnIndex(FM.FM_MAT_COUNT));
            Log.i(TAG, "SmetaOpenHelper.getAmountOfMatSelectedType mat_amount[position] = " + mat_amount[position]);
        }
        cursor.close();
        return mat_amount;
    }

    //получаем единицы измерения для  работ  по смете с id файла file_id
    public String[]  getUnitsOfWork(long file_id){
        String select_work_units = " SELECT " + FW.FW_UNIT +
                " FROM " +  FW.TABLE_NAME  +
                " WHERE " + FW.FW_FILE_ID  + " = " + String.valueOf(file_id);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select_work_units, null);
        Log.i(TAG, "TempDBHelper.getUnitsOfWork cursor.getCount()  " + cursor.getCount());
        String[] work_units = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            work_units[position] = cursor.getString(cursor.getColumnIndex(FW.FW_UNIT ));
            Log.i(TAG, "SmetaOpenHelper.getUnitsOfWork work_units[position] = " + work_units[position]);
        }
        cursor.close();
        return work_units;
    }

    //получаем единицы измерения для  материалов  по смете с id файла file_id
    public String[]  getUnitsOfMat(long file_id){
        String select_maat_units = " SELECT " + FM.FM_MAT_UNIT +
                " FROM " +  FM.TABLE_NAME  +
                " WHERE " + FM.FM_FILE_ID  + " = " + String.valueOf(file_id);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select_maat_units, null);
        Log.i(TAG, "TempDBHelper.getUnitsOfMat cursor.getCount()  " + cursor.getCount());
        String[] mat_units = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            mat_units[position] = cursor.getString(cursor.getColumnIndex(FM.FM_MAT_UNIT ));
            Log.i(TAG, "SmetaOpenHelper.getUnitsOfMat mat_units[position] = " + mat_units[position]);
        }
            cursor.close();
        return mat_units;
    }

    //получаем единицы измерения для  работ  по смете с id файла file_id  и id типа type_id
    public String[]  getUnitsOfWorkSelectedType(long file_id, long type_id){
        String select_work_units = " SELECT " + FW.FW_UNIT +
                " FROM " +  FW.TABLE_NAME  +
                " WHERE " + FW.FW_FILE_ID  + " = " + String.valueOf(file_id)+
                " AND " + FW.FW_TYPE_ID  + " = " + String.valueOf(type_id);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select_work_units, null);
        Log.i(TAG, "TempDBHelper.getUnitsOfWorkSelectedType cursor.getCount()  " + cursor.getCount());
        String[] work_units = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            work_units[position] = cursor.getString(cursor.getColumnIndex(FW.FW_UNIT ));
            Log.i(TAG, "SmetaOpenHelper.getUnitsOfWorkSelectedType work_units[position] = " + work_units[position]);
        }
        cursor.close();
        return work_units;
    }

    //получаем единицы измерения для  работ  по смете с id файла file_id  и id типа type_id
    public String[]  getUnitsOfMatSelectedType(long file_id, long type_mat_id){
        String select_mat_units = " SELECT " + FM.FM_MAT_UNIT +
                " FROM " +  FM.TABLE_NAME  +
                " WHERE " + FM.FM_FILE_ID  + " = " + String.valueOf(file_id)+
                " AND " + FM.FM_MAT_TYPE_ID  + " = " + String.valueOf(type_mat_id);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select_mat_units, null);
        Log.i(TAG, "TempDBHelper.getUnitsOfMatSelectedType cursor.getCount()  " + cursor.getCount());
        String[] mat_units = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            mat_units[position] = cursor.getString(cursor.getColumnIndex(FM.FM_MAT_UNIT ));
            Log.i(TAG, "SmetaOpenHelper.getUnitsOfMatSelectedType mat_units[position] = " + mat_units[position]);
        }
        cursor.close();
        return mat_units;
    }

    //получаем количество работ  по смете с id файла file_id
    public float[]  getSummaOfWork(long file_id){
        String select_work_summa = " SELECT " + FW.FW_SUMMA +
                " FROM " +  FW.TABLE_NAME  +
                " WHERE " + FW.FW_FILE_ID  + " = " + String.valueOf(file_id);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select_work_summa, null);
        Log.i(TAG, "TempDBHelper.getSummaOfWork cursor.getCount()  " + cursor.getCount());
        float[] work_summa = new float[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            work_summa[position] = cursor.getFloat(cursor.getColumnIndex(FW.FW_SUMMA));
            Log.i(TAG, "SmetaOpenHelper.getSummaOfWork work_summa[position] = " + work_summa[position]);
        }
        cursor.close();
        return work_summa;
    }

    //получаем количество материалов  по смете с id файла file_id
    public float[]  getSummaOfMat(long file_id){
        String select_mat_summa = " SELECT " + FM.FM_MAT_SUMMA +
                " FROM " +  FM.TABLE_NAME  +
                " WHERE " + FM.FM_FILE_ID  + " = " + String.valueOf(file_id);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select_mat_summa, null);
        Log.i(TAG, "TempDBHelper.getSummaOfMat cursor.getCount()  " + cursor.getCount());
        float[] mat_summa = new float[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            mat_summa[position] = cursor.getFloat(cursor.getColumnIndex(FM.FM_MAT_SUMMA));
            Log.i(TAG, "SmetaOpenHelper.getSummaOfMat mat_summa[position] = " + mat_summa[position]);
        }
            cursor.close();
        return mat_summa;
    }

    //получаем стоимость работы  по смете с id файла file_id  и id типа type_id
    public float[]  getSummaOfWorkSelectedType(long file_id, long type_id){
        String select_work_summa = " SELECT " + FW.FW_SUMMA +
                " FROM " +  FW.TABLE_NAME  +
                " WHERE " + FW.FW_FILE_ID  + " = " + String.valueOf(file_id)+
                " AND " + FW.FW_TYPE_ID  + " = " + String.valueOf(type_id);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select_work_summa, null);
        Log.i(TAG, "TempDBHelper.getSummaOfWorkSelectedType cursor.getCount()  " + cursor.getCount());
        float[] work_summa = new float[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            work_summa[position] = cursor.getFloat(cursor.getColumnIndex(FW.FW_SUMMA));
            Log.i(TAG, "SmetaOpenHelper.getSummaOfWorkSelectedType work_summa[position] = " + work_summa[position]);
        }
        cursor.close();
        return work_summa;
    }

    //получаем стоимость работы  по смете с id файла file_id  и id типа type_id
    public float[]  getSummaOfMatSelectedType(long file_id, long type_mat_id){
        String select_mat_summa = " SELECT " + FM.FM_MAT_SUMMA +
                " FROM " +  FM.TABLE_NAME  +
                " WHERE " + FM.FM_FILE_ID  + " = " + String.valueOf(file_id)+
                " AND " + FM.FM_MAT_TYPE_ID + " = " + String.valueOf(type_mat_id);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select_mat_summa, null);
        Log.i(TAG, "TempDBHelper.getSummaOfMatSelectedType cursor.getCount()  " + cursor.getCount());
        float[] mat_summa = new float[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            mat_summa[position] = cursor.getFloat(cursor.getColumnIndex(FM.FM_MAT_SUMMA));
            Log.i(TAG, "SmetaOpenHelper.getSummaOfMatSelectedType mat_summa[position] = " + mat_summa[position]);
        }
        cursor.close();
        return mat_summa;
    }

    //получаем список категорий по id файла из таблицы FW
    public String[] getCategoryNamesFW(long file_id){
        Log.i(TAG, "SmetaOpenHelper.getCategoryNamesFW ... ");
        String selectCategoryNamesFW =  " select DISTINCT " + FW.FW_CATEGORY_NAME +
        " from " +  FW.TABLE_NAME + " where " +  FW.FW_FILE_ID + " = " + file_id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectCategoryNamesFW, null);
        Log.i(TAG, "SmetaOpenHelper.getCategoryNamesFW cursor.getCount()  " + cursor.getCount());
        String[] categoryNamesFW = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            categoryNamesFW[position] = cursor.getString(cursor.getColumnIndex(FW.FW_CATEGORY_NAME));
            Log.i(TAG, "SmetaOpenHelper.getCategoryNamesFW catNamesFW[position] = " + categoryNamesFW[position]);
        }
            cursor.close();
        return categoryNamesFW;
    }

    //получаем список категорий материалов по id файла из таблицы FM
    public String[] getCategoryNamesFM(long file_id){
        Log.i(TAG, "SmetaOpenHelper.getCategoryNamesFM ... ");
        String selectCategoryNamesFM =  " select DISTINCT " + FM.FM_MAT_CATEGORY_NAME +
                " from " +  FM.TABLE_NAME + " where " +  FM.FM_FILE_ID + " = " + file_id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectCategoryNamesFM, null);
        Log.i(TAG, "SmetaOpenHelper.getCategoryNamesFM cursor.getCount()  " + cursor.getCount());
        String[] categoryNamesFM = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            categoryNamesFM[position] = cursor.getString(cursor.getColumnIndex(FM.FM_MAT_CATEGORY_NAME));
            Log.i(TAG, "SmetaOpenHelper.getCategoryNamesFM catNamesFW[position] = " + categoryNamesFM[position]);
        }
        cursor.close();
        return categoryNamesFM;
    }

    //получаем список типов по id файла из таблицы FW
    public String[] getTypeNamesFW(long file_id){
        Log.i(TAG, "SmetaOpenHelper.getTypeNamesFW ... ");
        String selectTypeNamesFW =  " select DISTINCT " + FW.FW_TYPE_NAME +
                " from " +  FW.TABLE_NAME + " where " +  FW.FW_FILE_ID + " = " + file_id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectTypeNamesFW, null);
        Log.i(TAG, "SmetaOpenHelper.getTypeNamesFW cursor.getCount()  " + cursor.getCount());
        String[] typeNamesFW = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            typeNamesFW[position] = cursor.getString(cursor.getColumnIndex(FW.FW_TYPE_NAME));
            Log.i(TAG, "SmetaOpenHelper.getTypeNamesFW typeNamesFW[position] = " + typeNamesFW[position]);
        }
            cursor.close();
        return typeNamesFW;
    }

    //получаем список типов по id файла из таблицы FW
    public String[] getTypeNamesFWSort(long file_id){

        SQLiteDatabase db = this.getReadableDatabase();
        Log.i(TAG, "SmetaOpenHelper.getTypeNamesFWSort ... ");

        Cursor cursor = db.query(
                FW.TABLE_NAME,   // таблица
                new String[]{FW.FW_TYPE_NAME, FW.FW_TYPE_ID},            // столбцы
                FW.FW_FILE_ID  + "=?",                  // столбцы для условия WHERE
                new String[]{String.valueOf(file_id)},                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                FW.FW_TYPE_ID);                   // порядок сортировки
        Log.i(TAG, "SmetaOpenHelper.getTypeNamesFWSort cursor.getCount()  " + cursor.getCount());

        String[] typeNamesFW = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            typeNamesFW[position] = cursor.getString(cursor.getColumnIndex(FW.FW_TYPE_NAME));
            Log.i(TAG, "SmetaOpenHelper.getTypeNamesFWSort typeNamesFW[position] = " + typeNamesFW[position]);
        }
        cursor.close();
        return typeNamesFW;
    }

    //получаем список типов материалов по id файла из таблицы FM
    public String[] getTypeNamesFM(long file_id){
        Log.i(TAG, "SmetaOpenHelper.getTypeNamesFM ... ");
        String selectTypeNamesFM =  " select DISTINCT " + FM.FM_MAT_TYPE_NAME +
                " from " +  FM.TABLE_NAME + " where " +  FM.FM_FILE_ID + " = " + file_id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectTypeNamesFM, null);
        Log.i(TAG, "SmetaOpenHelper.getTypeNamesFM cursor.getCount()  " + cursor.getCount());
        String[] typeNamesFM = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            typeNamesFM[position] = cursor.getString(cursor.getColumnIndex(FM.FM_MAT_TYPE_NAME));
            Log.i(TAG, "SmetaOpenHelper.getTypeNamesFM typeNamesFW[position] = " + typeNamesFM[position]);
        }
            cursor.close();
        return typeNamesFM;
    }

    //получаем список типов материалов по id файла из таблицы FM
    public String[] getTypeNamesFMSort(long file_id){

        SQLiteDatabase db = this.getReadableDatabase();
        Log.i(TAG, "SmetaOpenHelper.getTypeNamesFMSort ... ");

        Cursor cursor = db.query(
                FM.TABLE_NAME,   // таблица
                new String[]{FM.FM_MAT_TYPE_NAME, FM.FM_MAT_TYPE_ID},            // столбцы
                FM.FM_FILE_ID  + "=?",                  // столбцы для условия WHERE
                new String[]{String.valueOf(file_id)},                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                FM.FM_MAT_TYPE_ID);                   // порядок сортировки
        Log.i(TAG, "SmetaOpenHelper.getTypeNamesFMSort cursor.getCount()  " + cursor.getCount());
        String[] typeNamesFM = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            typeNamesFM[position] = cursor.getString(cursor.getColumnIndex(FM.FM_MAT_TYPE_NAME));
            Log.i(TAG, "SmetaOpenHelper.getTypeNamesFMSort typeNamesFW[position] = " + typeNamesFM[position]);
        }
        cursor.close();
        return typeNamesFM;
    }

    //получаем список работ по id файла из таблицы FW
    public String[] getWorkNamesFW(long file_id){
        Log.i(TAG, "SmetaOpenHelper.getWorkNamesFW ... ");
        String selectWorkNamesFW =  " select DISTINCT " + FW.FW_WORK_NAME +
                " from " +  FW.TABLE_NAME + " where " +  FW.FW_FILE_ID + " = " + file_id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectWorkNamesFW, null);
        Log.i(TAG, "SmetaOpenHelper.getWorkNamesFW cursor.getCount()  " + cursor.getCount());
        String[] workNamesFW = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            workNamesFW[position] = cursor.getString(cursor.getColumnIndex(FW.FW_WORK_NAME));
            Log.i(TAG, "SmetaOpenHelper.getWorkNamesFW workNamesFW[position] = " + workNamesFW[position]);
        }
        cursor.close();
        return workNamesFW;
    }

    //получаем список материалов по id файла из таблицы FM
    public String[] getMatNamesFM(long file_id){
        Log.i(TAG, "SmetaOpenHelper.getMatNamesFM ... ");
        String selectMatNamesFM =  " select DISTINCT " + FM.FM_MAT_NAME +
                " from " +  FM.TABLE_NAME + " where " +  FM.FM_FILE_ID + " = " + file_id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectMatNamesFM, null);
        Log.i(TAG, "SmetaOpenHelper.getMatNamesFM cursor.getCount()  " + cursor.getCount());
        String[] matNamesFW = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            matNamesFW[position] = cursor.getString(cursor.getColumnIndex(FM.FM_MAT_NAME));
            Log.i(TAG, "SmetaOpenHelper.getMatNamesFM workNamesFW[position] = " + matNamesFW[position]);
        }
            cursor.close();
        return matNamesFW;
    }

    //удаляем название сметы из таблицы FileWork и все строки из FW по id сметы fileId
    public void deleteFile(long fileId) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FW.TABLE_NAME, FW.FW_FILE_ID + " =? " ,
                new String[]{String.valueOf(fileId)});
        db.delete(FileWork.TABLE_NAME, FileWork._ID + " =? " ,
                new String[]{String.valueOf(fileId)});
        db.close();
    }

    //получаем все данные сметы в классе DataFile - для удобства работы
    public  DataFile getFileData(long file_id){
        Log.i(TAG, "SmetaOpenHelper.getFileData ... ");
        DataFile dataFile = new DataFile();
        String workName = " SELECT  * FROM " +  FileWork.TABLE_NAME +
                " WHERE " + FileWork._ID  + " = ? " ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(workName, new String[]{String.valueOf(file_id)});
        if ((cursor != null) && (cursor.getCount() != 0)) {
            cursor.moveToFirst();
            // Узнаем индекс каждого столбца и Используем индекс для получения строки
            String currentFileName = cursor.getString(cursor.getColumnIndex(FileWork.FILE_NAME));
            String currentAdress = cursor.getString(cursor.getColumnIndex(FileWork.ADRESS));
            String currentDate = cursor.getString(cursor.getColumnIndex(FileWork.FILE_NAME_DATE));
            String currentTime = cursor.getString(cursor.getColumnIndex(FileWork.FILE_NAME_TIME));
            String currentDescription = cursor.getString(cursor.getColumnIndex(FileWork.DESCRIPTION_OF_FILE));
            Log.d(TAG, "getFileData currentFileName = " + currentFileName);
            //создаём экземпляр класса DataFile в конструкторе
            dataFile = new DataFile(currentFileName, currentAdress,
                    currentDate, currentTime, currentDescription);
        }
        if (cursor != null) {
            cursor.close();
        }
        return dataFile;
    }

    //обновляем данные файла сметы имя, адрес, описание, дата и время
    public void updateFileData(long file_id, String name, String adress, String description){

        SQLiteDatabase db = this.getWritableDatabase();

        //заполняем данные для обновления в базе
        ContentValues values = new ContentValues();
        values.put(FileWork.FILE_NAME, name);
        values.put(FileWork.ADRESS, adress);
        values.put(FileWork.DESCRIPTION_OF_FILE, description);

        db.update(FileWork.TABLE_NAME, values,
                FileWork._ID + "=" + file_id, null);
        Log.i(TAG, "SmetaOpenHelper.updateFileData - name =" + name + "  file_id = " + file_id);
    }

    public boolean isWorkInFW(long file_id, long work_id){
        Log.i(TAG, "SmetaOpenHelper.isWork ... ");

        String select = " SELECT " + FW.FW_WORK_NAME + " FROM " + FW.TABLE_NAME +
        " where " + FW.FW_FILE_ID + " =? " + " and " + FW.FW_WORK_ID + " =? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(file_id),String.valueOf(work_id)});
        Log.i(TAG, "SmetaOpenHelper.isWork cursor.getCount() = " + cursor.getCount());
        if (cursor.getCount() != 0) {
            return true;
        }
        cursor.close();
        return false;
    }

    //получаем значение количества работы для сметы с id = file_id и работы с id = work_id
    public float getCountWork(long file_id, long work_id){
        Log.i(TAG, "SmetaOpenHelper.getCountWork ... ");
        String select = " SELECT " + FW.FW_COUNT + " FROM " + FW.TABLE_NAME +
                " where " + FW.FW_FILE_ID + " =? " + " and " + FW.FW_WORK_ID + " =? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(file_id),String.valueOf(work_id)});
        Log.i(TAG, "SmetaOpenHelper.getCountWork cursor.getCount() = " + cursor.getCount());
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            // Узнаем индекс столбца и Используем индекс для получения количества работы
            float count = cursor.getFloat(cursor.getColumnIndex(FW.FW_COUNT));
            return count;
        }
        cursor.close();
        return -1;
    }

    //получаем id типа работы
    public long getTypeIdWork(long file_id, long work_id){
        Log.i(TAG, "SmetaOpenHelper.getTypeIdWork ... ");
        String select = " SELECT " + FW.FW_TYPE_ID + " FROM " + FW.TABLE_NAME +
                " where " + FW.FW_FILE_ID + " =? " + " and " + FW.FW_WORK_ID + " =? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(file_id),String.valueOf(work_id)});
        Log.i(TAG, "SmetaOpenHelper.getTypeIdWork cursor.getCount() = " + cursor.getCount());
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            // Узнаем индекс столбца и Используем индекс для получения количества работы
            long type_id = cursor.getLong(cursor.getColumnIndex(FW.FW_TYPE_ID));
            Log.i(TAG, "SmetaOpenHelper.getTypeIdWork type_id = " + type_id );
            return type_id;
        }
        cursor.close();
        return -1;
    }

    //получаем id категории работы
    public long getCateIdWork(long file_id, long work_id){
        Log.i(TAG, "SmetaOpenHelper.getCateIdWork ... ");
        String select = " SELECT " + FW.FW_CATEGORY_ID + " FROM " + FW.TABLE_NAME +
                " where " + FW.FW_FILE_ID + " =? " + " and " + FW.FW_WORK_ID + " =? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(file_id),String.valueOf(work_id)});
        Log.i(TAG, "SmetaOpenHelper.getCateIdWork cursor.getCount() = " + cursor.getCount());
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            // Узнаем индекс столбца и Используем индекс для получения количества работы
            long cat_id = cursor.getLong(cursor.getColumnIndex(FW.FW_CATEGORY_ID));
            Log.i(TAG, "SmetaOpenHelper.getCateIdWork cat_id = " + cat_id );
            return cat_id;
        }
        cursor.close();
        return -1;
    }

    //удаляем работу из сметы FW по file_id и work_id
    public void deleteWorkItemFromFW(long file_id, long work_id){
        Log.i(TAG, "SmetaOpenHelper.deleteWorkItemFromFW ... ");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FW.TABLE_NAME, FW.FW_FILE_ID + " =? " + " AND " + FW.FW_WORK_ID + " =? ",
                new String[]{String.valueOf(file_id), String.valueOf(work_id)});
    }

    //Добавляем категорию
    public long  insertCatName(String catName){
        Log.i(TAG, "SmetaOpenHelper.insertCatName ... ");

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CategoryWork.CATEGORY_NAME,catName);
        // вставляем строку
        long ID = db.insert(CategoryWork.TABLE_NAME, null, cv);
        // закрываем соединение с базой
        db.close();
        Log.d(TAG, "MyDatabaseHelper.insertCatName  CategoryWork._ID = " + ID);
        return ID;
    }

    //получаем курсор с Category._id  и делаем массив id
    public String[] getArrayCategoryNames() {
        SQLiteDatabase db = getReadableDatabase();
        Log.i(TAG, "SmetaOpenHelper.getArrayCategoryNames ... ");
        String categoryName  = " SELECT " + CategoryWork.CATEGORY_NAME  + " FROM " + CategoryWork.TABLE_NAME;
        Cursor cursor = db.rawQuery(categoryName, null);
        String[] category_name = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            category_name[position] = cursor.getString(cursor.getColumnIndex(CategoryWork.CATEGORY_NAME));
            Log.i(TAG, "SmetaOpenHelper.getArrayCategoryNames position = " + position);
        }
        return category_name;
    }

    //Добавляем тип работы
    public long  insertTypeName(String typeName, long type_category_Id){
        Log.i(TAG, "SmetaOpenHelper.insertTypeName ... ");

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TypeWork.TYPE_NAME,typeName);
        cv.put(TypeWork.TYPE_CATEGORY_ID,type_category_Id);
        // вставляем строку
        long ID = db.insert(TypeWork.TABLE_NAME, null, cv);
        // закрываем соединение с базой
        db.close();
        Log.d(TAG, "MyDatabaseHelper.insertTypeName  TypeWork._ID = " + ID);
        return ID;
    }

    //получаем данные по категории по её id
    public DataCategory getCategoryData(long cat_id){
        Log.i(TAG, "SmetaOpenHelper.getCategoryData ... ");
        DataCategory dataCategory = new DataCategory();

        String catData = " SELECT  * FROM " +  CategoryWork.TABLE_NAME +
                " WHERE " + CategoryWork._ID  + " = ? " ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(catData, new String[]{String.valueOf(cat_id)});
        if ((cursor != null) && (cursor.getCount() != 0)) {
            cursor.moveToFirst();
            // Узнаем индекс каждого столбца и Используем индекс для получения строки
            String currentCatName = cursor.getString(cursor.getColumnIndex(CategoryWork.CATEGORY_NAME));
            String currentCatDescription = cursor.getString(cursor.getColumnIndex(CategoryWork.CATEGORY_DESCRIPTION));
            Log.d(TAG, "getCategoryData currentCatName = " + currentCatName);
            //создаём экземпляр класса DataFile в конструкторе
            dataCategory = new DataCategory(currentCatName, currentCatDescription);
        }
        if (cursor != null) {
            cursor.close();
        }
        return dataCategory;
    }

    //Обновляем данные по категории работ
    public void updateCategoryData(long cat_id, String nameCat, String descriptionCat){
        Log.i(TAG, "SmetaOpenHelper.updateCategoryData ...");
        SQLiteDatabase db = this.getWritableDatabase();
        //заполняем данные для обновления в базе
        ContentValues values = new ContentValues();
        values.put(CategoryWork.CATEGORY_NAME, nameCat);
        values.put(CategoryWork.CATEGORY_DESCRIPTION, descriptionCat);

        db.update(CategoryWork.TABLE_NAME, values,
                CategoryWork._ID + "=" + cat_id, null);
        Log.i(TAG, "SmetaOpenHelper.updateCategoryData - name =" + nameCat + "  cat_id = " + cat_id);
    }

    //находим количество строк типов работы для cat_id
    public int getCountType(long cat_id){
        Log.i(TAG, "SmetaOpenHelper.getCountType ... ");

        String select = " SELECT " + TypeWork._ID + " FROM " + TypeWork.TABLE_NAME +
                " where " + TypeWork.TYPE_CATEGORY_ID + " =? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(cat_id)});
        Log.i(TAG, "SmetaOpenHelper.getCountWork cursor.getCount() = " + cursor.getCount());
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    //удаляем категорию работ (если типы работ отсутствуют - это проверяется в другом месте)
    public void deleteCategory(long cat_Id) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CategoryWork.TABLE_NAME, CategoryWork._ID + " =? " ,
                new String[]{String.valueOf(cat_Id)});
        db.close();
    }

    //получаем количество видов работ с типом type_id
    public int getCountLineWork(long type_id){
        Log.i(TAG, "SmetaOpenHelper.getCountLineWork ... ");

        String select = " SELECT " + Work._ID + " FROM " + Work.TABLE_NAME +
                " where " + Work.WORK_TYPE_ID + " =? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(type_id)});
        Log.i(TAG, "SmetaOpenHelper.getCountLineWork cursor.getCount() = " + cursor.getCount());
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    //получаем данные по категории по её id
    public DataType getTypeData(long type_id){
        Log.i(TAG, "SmetaOpenHelper.getTypeData ... ");
        DataType dataType = new DataType();

        String typeData = " SELECT  * FROM " +  TypeWork.TABLE_NAME +
                " WHERE " + TypeWork._ID  + " = ? " ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(typeData, new String[]{String.valueOf(type_id)});
        if ((cursor != null) && (cursor.getCount() != 0)) {
            cursor.moveToFirst();
            // Узнаем индекс каждого столбца и Используем индекс для получения строки
            long currentTypeCategoryId = cursor.getLong(cursor.getColumnIndex(TypeWork.TYPE_CATEGORY_ID));
            String currentTypeName = cursor.getString(cursor.getColumnIndex(TypeWork.TYPE_NAME));
            String currentTypeDescription = cursor.getString(cursor.getColumnIndex(TypeWork.TYPE_DESCRIPTION));
            Log.d(TAG, "getTypeData currentTypeName = " + currentTypeName);
            //создаём экземпляр класса DataFile в конструкторе
            dataType = new DataType(currentTypeCategoryId, currentTypeName, currentTypeDescription);
        }
        if (cursor != null) {
            cursor.close();
        }
        return dataType;
    }

    //Обновляем данные по типам работ
    public void updateTypeData(long type_id, String nameType, String descriptionType){
        Log.i(TAG, "SmetaOpenHelper.updateTypeData ...");
        SQLiteDatabase db = this.getWritableDatabase();
        //заполняем данные для обновления в базе
        ContentValues values = new ContentValues();
        values.put(TypeWork.TYPE_NAME, nameType);
        values.put(TypeWork.TYPE_DESCRIPTION, descriptionType);

        db.update(TypeWork.TABLE_NAME, values,
                TypeWork._ID + "=" + type_id, null);
        Log.i(TAG, "SmetaOpenHelper.updateTypeData - nameType =" + nameType + "  type_id = " + type_id);
    }

    //удаляем тип работы (если виды работ отсутствуют - это проверяется в другом месте)
    public void deleteType(long type_Id) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TypeWork.TABLE_NAME, TypeWork._ID + " =? ",
                new String[]{String.valueOf(type_Id)});
        db.close();
    }

    //получаем массив названий типов работы
    public String[] getArrayTypeNames(long cat_id) {
        SQLiteDatabase db = getReadableDatabase();
        Log.i(TAG, "SmetaOpenHelper.getArrayTypeNames ... ");
        String typeName  = " SELECT " + TypeWork.TYPE_NAME  + " FROM " + TypeWork.TABLE_NAME +
                " WHERE " + TypeWork.TYPE_CATEGORY_ID  + " = ? " ;;
        Cursor cursor = db.rawQuery(typeName, new String[]{String.valueOf(cat_id)});
        String[] type_name = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            type_name[position] = cursor.getString(cursor.getColumnIndex(TypeWork.TYPE_NAME));
            Log.i(TAG, "SmetaOpenHelper.getArrayTypeNames position = " + position);
        }
        return type_name;
    }

    //Добавляем вид работы
    public long  insertWorkName(String workName, long work_type_Id){
        Log.i(TAG, "SmetaOpenHelper.insertWorkName ... ");

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Work.WORK_NAME,workName);
        cv.put(Work.WORK_TYPE_ID, work_type_Id);
        // вставляем строку
        long ID = db.insert(Work.TABLE_NAME, null, cv);
        // закрываем соединение с базой
        db.close();
        Log.d(TAG, "MyDatabaseHelper.insertWorkName  Work._ID = " + ID);
        return ID;
    }

    //получаем количество видов работ с  work_id в таблице FW
    public int getCountLineWorkInFW(long work_id){
        Log.i(TAG, "SmetaOpenHelper.getCountLineWorkInFW ... ");

        String select = " SELECT " + FW._ID + " FROM " + FW.TABLE_NAME +
                " where " + FW.FW_WORK_ID + " =? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(work_id)});
        Log.i(TAG, "SmetaOpenHelper.getCountLineWorkInFW cursor.getCount() = " + cursor.getCount());
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    //Смотрим, есть ли расценка на работу с work_id в таблице CostWork
    public int getCountLineWorkInCost(long work_id){
        Log.i(TAG, "SmetaOpenHelper.getCountLineWorkInCost ... ");

        String select = " SELECT " + CostWork._ID + " FROM " + CostWork.TABLE_NAME +
                " where " + CostWork.COST_WORK_ID + " =? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(work_id)});
        Log.i(TAG, "SmetaOpenHelper.getCountLineWorkInCost cursor.getCount() = " + cursor.getCount());
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    //удаляем вид работы из  Work (если в таблице FW нет записей с work_Id - это проверяется в другом месте)
    public void deleteWork(long work_Id) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Work.TABLE_NAME, Work._ID + " =? ",
                new String[]{String.valueOf(work_Id)});
        db.close();
    }

    //Добавляем вид работы
    public long  insertCostZero(long work_Id){
        Log.i(TAG, "SmetaOpenHelper.insertCostZero ... ");

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CostWork.COST_WORK_ID,work_Id);
        cv.put(CostWork.COST_UNIT_ID, 1);
        cv.put(CostWork.COST_COST,0);
        cv.put(CostWork.COST_NUMBER,1);
        // вставляем строку
        long ID = db.insert(CostWork.TABLE_NAME, null, cv);
        // закрываем соединение с базой
        db.close();
        Log.d(TAG, "MyDatabaseHelper.insertCostZero  CostWork._ID = " + ID);
        return ID;
    }

    //удаляем расценку работы (если в таблице FW нет записей с work_Id - это проверяется в другом месте)
    public void deleteCostOfWork(long work_Id) {
        Log.i(TAG, "SmetaOpenHelper.deleteCostOfWork ... ");
        SQLiteDatabase db = this.getWritableDatabase();
        int countDelete = db.delete(CostWork.TABLE_NAME, CostWork.COST_WORK_ID + " =? ",
                new String[]{String.valueOf(work_Id)});
        Log.i(TAG, "SmetaOpenHelper.deleteCostOfWork countDelete =" + countDelete);
        db.close();
    }

    //получаем массив названий типов работы
    public String[] getArrayUnitsNames() {
        SQLiteDatabase db = getReadableDatabase();
        Log.i(TAG, "SmetaOpenHelper.getArrayUnitsNames ... ");
        String unitsName  = " SELECT " + Unit.UNIT_NAME  + " FROM " + Unit.TABLE_NAME;
        Cursor cursor = db.rawQuery(unitsName, null);
        String[] units_name = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            units_name[position] = cursor.getString(cursor.getColumnIndex(Unit.UNIT_NAME));
            Log.i(TAG, "SmetaOpenHelper.getArrayUnitsNames position = " + position);
        }
        return units_name;
    }

    //получаем ID по имени  работы
    public long getIdFromUnitName(String unitName) {
        long currentID;
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                Unit.TABLE_NAME,   // таблица
                new String[]{Unit._ID},            // столбцы
                Unit.UNIT_NAME + "=?",                  // столбцы для условия WHERE
                new String[]{unitName},                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            // Узнаем индекс каждого столбца
            int idColumnIndex = cursor.getColumnIndex(Unit._ID);
            // Используем индекс для получения строки или числа
            currentID = cursor.getLong(idColumnIndex);
        } else {
            currentID = -1;
        }
        Log.d(TAG, "getIdFromUnitName currentID = " + currentID);
            cursor.close();
        return currentID;
    }

    //получаем данные по категории по её id
    public DataWork getWorkData(long work_id){
        Log.i(TAG, "SmetaOpenHelper.getWorkData ... ");
        DataWork dataWork = new DataWork();

        String workData = " SELECT  * FROM " +  Work.TABLE_NAME +
                " WHERE " + Work._ID  + " = ? " ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(workData, new String[]{String.valueOf(work_id)});
        if ((cursor != null) && (cursor.getCount() != 0)) {
            cursor.moveToFirst();
            // Узнаем индекс каждого столбца и Используем индекс для получения строки
            long currentWorkTypeId = cursor.getLong(cursor.getColumnIndex(Work.WORK_TYPE_ID));
            String currentWorkName = cursor.getString(cursor.getColumnIndex(Work.WORK_NAME));
            String currentWorkDescription = cursor.getString(cursor.getColumnIndex(Work.WORK_DESCRIPTION));
            Log.d(TAG, "getWorkData currentWorkName = " + currentWorkName);
            //создаём экземпляр класса DataWork в конструкторе
            dataWork = new DataWork(currentWorkTypeId, currentWorkName, currentWorkDescription);
        }
        if (cursor != null) {
            cursor.close();
        }
        return dataWork;
    }


    //Обновляем данные по типам работ
    public void updateWorkData(long work_id, String nameWork, String descriptionWork){
        Log.i(TAG, "SmetaOpenHelper.updateWorkData ...");
        SQLiteDatabase db = this.getWritableDatabase();
        //заполняем данные для обновления в базе
        ContentValues values = new ContentValues();
        values.put(Work.WORK_NAME, nameWork);
        values.put(Work.WORK_DESCRIPTION, descriptionWork);

        db.update(Work.TABLE_NAME, values,
                Work._ID + "=" + work_id, null);
        Log.i(TAG, "SmetaOpenHelper.updateWorkData - nameWork =" + nameWork + "  work_id = " + work_id);
    }

    //получаем ID по имени  материала
    public long getIdFromMatName(String matName) {
        long currentID;
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                Mat.TABLE_NAME,   // таблица
                new String[]{Unit._ID},            // столбцы
                Mat.MAT_NAME + "=?",                  // столбцы для условия WHERE
                new String[]{matName},                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        if ((cursor != null) && (cursor.getCount() != 0)) {
            cursor.moveToFirst();
            // Узнаем индекс каждого столбца
            int idColumnIndex = cursor.getColumnIndex(Unit._ID);
            // Используем индекс для получения строки или числа
            currentID = cursor.getLong(idColumnIndex);
        } else {
            currentID = -1;
        }
        Log.d(TAG, "getIdFromMatName currentID = " + currentID);
        if (cursor != null) {
            cursor.close();
        }
        return currentID;
    }

    //получаем ID типа материалов по имени типа материалов
    public long getIdFromMatTypeName(String matTypeName) {
        Log.d(TAG, "getIdFromMatTypeName ...");
        long currentID;
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TypeMat.TABLE_NAME,   // таблица
                new String[]{TypeMat._ID},            // столбцы
                TypeMat.TYPE_MAT_NAME + "=?",   // столбцы для условия WHERE
                new String[]{matTypeName},                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        if ((cursor != null) && (cursor.getCount() != 0)) {
            cursor.moveToFirst();
            // Узнаем индекс каждого столбца
            int idColumnIndex = cursor.getColumnIndex(TypeMat._ID);
            // Используем индекс для получения строки или числа
            currentID = cursor.getLong(idColumnIndex);
        } else {
            currentID = -1;
        }
        Log.d(TAG, "getIdFromMatTypeName currentID = " + currentID);
            cursor.close();
        return currentID;
    }

    //получаем ID категории по имени типа материалов
    public long getCatIdFromTypeMat(long type_id) {
        Log.d(TAG, "getCatIdFromTypeMat ...");
        long currentID;
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TypeMat.TABLE_NAME,   // таблица
                new String[]{TypeMat.TYPE_MAT_CATEGORY_ID},            // столбцы
                TypeMat._ID + "=?",   // столбцы для условия WHERE
                new String[]{String.valueOf(type_id)},                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        if ((cursor != null) && (cursor.getCount() != 0)) {
            cursor.moveToFirst();
            // Узнаем индекс каждого столбца
            int idColumnIndex = cursor.getColumnIndex(TypeMat.TYPE_MAT_CATEGORY_ID);
            // Используем индекс для получения строки или числа
            currentID = cursor.getLong(idColumnIndex);
        } else {
            currentID = -1;
        }
        Log.d(TAG, "getCatIdFromTypeMat currentID = " + currentID);
        cursor.close();
        return currentID;
    }

    //получаем курсор с названиями  материалов
    public Cursor getMatNamesOneType(long type_id) {
        Log.i(TAG, "SmetaOpenHelper.getMatNamesOneType ... ");
        String matNames = " SELECT " + Mat._ID + " , " +
                Mat.MAT_NAME + " FROM " + Mat.TABLE_NAME +
                " WHERE " + Mat.MAT_TYPE_ID  + " = ? " ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(matNames, new String[]{String.valueOf(type_id)});
        Log.i(TAG, "SmetaOpenHelper.getMatNamesOneType cursor.getCount() =  " +
                cursor.getCount() + " type_id = " + type_id);
        return cursor;
    }

    //Смотрим, сколько строк в таблице цен на материалы
    public int getCountLineInCostMat(){
        Log.i(TAG, "SmetaOpenHelper.getCountLineInCostMat ... ");

        String select = " SELECT " + CostMat._ID + " FROM " + CostMat.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, null);
        Log.i(TAG, "SmetaOpenHelper.getCountLineInCostMat В таблице цен на материалы  = " + cursor.getCount());
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    //получаем ID по имени категории
    public long getCatIdFromCategoryMatName(String catMatName) {
        Log.i(TAG, "SmetaOpenHelper.getIdFromCategoryMatName ... ");
        long currentID;
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                CategoryMat.TABLE_NAME,   // таблица
                new String[]{CategoryMat._ID},            // столбцы
                CategoryMat.CATEGORY_MAT_NAME + "=?",                  // столбцы для условия WHERE
                new String[]{catMatName},                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки
        Log.i(TAG, " SmetaOpenHelper.getIdFromCategoryMatName - cursor.getCount()=" + cursor.getCount());
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            // Узнаем индекс каждого столбца
            int idColumnIndex = cursor.getColumnIndex(CategoryMat._ID);
            // Используем индекс для получения строки или числа
            currentID = cursor.getLong(idColumnIndex);
        } else {
            currentID = -1;
        }
        Log.d(TAG, "getIdFromCategoryMatName currentID = " + currentID);
            cursor.close();
        return currentID;
    }

    //получаем курсор с названиями  материалов
    public Cursor getTypeNamesOneCategory(long cat_id) {
        Log.i(TAG, "SmetaOpenHelper.getTypeNamesOneCategory ... ");
        String matNames = " SELECT " + TypeMat._ID + " , " +
                TypeMat.TYPE_MAT_NAME + " FROM " + TypeMat.TABLE_NAME +
                " WHERE " + TypeMat.TYPE_MAT_CATEGORY_ID  + " = ? " ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(matNames, new String[]{String.valueOf(cat_id)});
        Log.i(TAG, "SmetaOpenHelper.getTypeNamesOneCategory cursor.getCount() =  " + cursor.getCount());
        return cursor;
    }

    public boolean isMatInFM(long file_id, long mat_id){
        Log.i(TAG, "SmetaOpenHelper.isMatInFM ... ");

        String select = " SELECT " + FM.FM_MAT_NAME + " FROM " + FM.TABLE_NAME +
                " where " + FM.FM_FILE_ID + " =? " + " and " + FM.FM_MAT_ID + " =? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(file_id),String.valueOf(mat_id)});
        Log.i(TAG, "SmetaOpenHelper.isMatInFM cursor.getCount() = " + cursor.getCount());
        if (cursor.getCount() != 0) {
            return true;
        }
            cursor.close();
        return false;
    }

    //получаем значение количества материала для сметы с id = file_id и материала с id = mat_id
    public float getCountMat(long file_id, long mat_id){
        Log.i(TAG, "SmetaOpenHelper.getCountMat ... ");
        String select = " SELECT " + FM.FM_MAT_COUNT + " FROM " + FM.TABLE_NAME +
                " where " + FM.FM_FILE_ID + " =? " + " and " + FM.FM_MAT_ID + " =? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(file_id),String.valueOf(mat_id)});
        Log.i(TAG, "SmetaOpenHelper.getCountMat cursor.getCount() = " + cursor.getCount());
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            // Узнаем индекс столбца и Используем индекс для получения количества работы
            float count = cursor.getFloat(cursor.getColumnIndex(FM.FM_MAT_COUNT));
            return count;
        }
            cursor.close();
        return -1;
    }
    //получаем имя материала  по его id
    public String getMatNameById(long mat_id){
        Log.i(TAG, "SmetaOpenHelper.getMatNameById ... ");
        String currentWorkName = "";
        String workName = " SELECT " + Mat._ID + " , " +  Mat.MAT_NAME +
                " FROM " + Mat.TABLE_NAME +
                " WHERE " + Mat._ID  + " = ?" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(workName, new String[]{String.valueOf(mat_id)});
        if ((cursor != null) && (cursor.getCount() != 0)) {
            cursor.moveToFirst();
            // Узнаем индекс каждого столбца
            int idColumnIndex = cursor.getColumnIndex(Mat.MAT_NAME);
            // Используем индекс для получения строки или числа
            currentWorkName = cursor.getString(idColumnIndex);
        }
        Log.d(TAG, "getMatNameById currentName = " + currentWorkName);
            cursor.close();
        return currentWorkName;
    }
    //получаем стоимость материала по его id
    public float getMatkCostById(long mat_id){
        Log.i(TAG, "SmetaOpenHelper.geMatkCostById ... ");
        float costOfWork = 0;
        String cost = " SELECT " + CostMat.COST_MAT_COST +
                " FROM " + CostMat.TABLE_NAME +
                " WHERE " + CostMat.COST_MAT_ID  + " = ?" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(cost, new String[]{String.valueOf(mat_id)});
        Log.d(TAG, "geMatkCostById cursor.getCount() = " + cursor.getCount());

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            // Узнаем индекс каждого столбца
            int idColumnIndex = cursor.getColumnIndex(CostMat.COST_MAT_COST);
            // Используем индекс для получения строки или числа
            costOfWork = cursor.getFloat(idColumnIndex);
        }
        Log.d(TAG, "geMatkCostById costOfWork = " + costOfWork);
            cursor.close();
        return costOfWork;
    }

    //получаем единицы измерения с помощью вложенного запроса
    public String getCostMatUnitById(long mat_id){
        Log.i(TAG, "SmetaOpenHelper.getCostMatUnitById ... ");
        String unitMatName = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String unit = " SELECT " +  UnitMat.UNIT_MAT_NAME+
                " FROM " + UnitMat.TABLE_NAME  +
                " WHERE " + UnitMat._ID + " IN " +
                "(" + " SELECT " + CostMat.COST_MAT_UNIT_ID +
                " FROM " + CostMat.TABLE_NAME +
                " WHERE " + CostMat.COST_MAT_ID  + " = " +  String.valueOf(mat_id) + ")";

        Cursor cursor = db.rawQuery(unit, null);

        Log.d(TAG, "getCostMatUnitById cursor.getCount() = " + cursor.getCount());

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            // Узнаем индекс  столбца
            int idColumnIndex = cursor.getColumnIndex(UnitMat.UNIT_MAT_NAME);
            // Используем индекс для получения строки или числа
            unitMatName = cursor.getString(idColumnIndex);
            Log.d(TAG, "getCostMatUnitById unitMatName = " + unitMatName);
        }
            cursor.close();
        return unitMatName;
    }

    /**
     * обновляем количество и сумму в таблице FM
     */
    public void updateRowInFM_Count_Summa(
            long file_id, long mat_id, float cost_mat, String cost_unit, float countMat, float summaMat){

        Log.i(TAG, "SmetaOpenHelper.updateRowInFM_Count_Summa ... ");
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(FM.FM_MAT_COST, cost_mat);
        updatedValues.put(FM.FM_MAT_UNIT, cost_unit);
        updatedValues.put(FM.FM_MAT_COUNT, countMat);
        updatedValues.put(FM.FM_MAT_SUMMA, summaMat);

        db.update(FM.TABLE_NAME, updatedValues,
                FM.FM_FILE_ID + " =? " +" AND " + FM.FM_MAT_ID + " =? ",
                new String[]{String.valueOf(file_id), String.valueOf(mat_id)});
        Log.i(TAG, "SmetaOpenHelper.updateRowInFM_Count_Summa - cost_mat =" +
                cost_mat + "  summa = " + summaMat + " ");
    }
    /**
     * Вставляет строку в таблицу FW
     */
    public long  insertRowInFM_Name(long file_id, long mat_id, long type_mat_id, long category_mat_id,
                                    float cost_mat, float  count_mat, String unit_mat, float summa_mat){

        Log.i(TAG, "SmetaOpenHelper.insertRowInFM_Name ... ");

        SQLiteDatabase db = getWritableDatabase();

        //получаем имя файла по его id
        String fileName = this.getFileNameById(file_id);
        Log.i(TAG, "SmetaOpenHelper.insertRowInFM_Name fileName =  " + fileName);
        //получаем имя материала  по id материала
        String matName = this.getMatNameById(mat_id);
        Log.i(TAG, "SmetaOpenHelper.insertRowInFM_Name matName =  " + matName);
        //получаем имя типа материала по id типа материала
        String typeMatName = this.getTypeNameMatById(type_mat_id);
        Log.i(TAG, "SmetaOpenHelper.insertRowInFM_Name typeMatName =  " + typeMatName);
        //получаем имя категории материала по id категории материала
        String catMatName = this.getCategoryMatNameById(category_mat_id);
        Log.i(TAG, "SmetaOpenHelper.insertRowInFM_Name catName =  " + catMatName);

        ContentValues cv = new ContentValues();
        cv.put(FM.FM_FILE_ID,file_id);
        cv.put(FM.FM_FILE_NAME,fileName);
        cv.put(FM.FM_MAT_ID,mat_id);
        cv.put(FM.FM_MAT_NAME,matName);
        cv.put(FM.FM_MAT_TYPE_ID,type_mat_id);
        cv.put(FM.FM_MAT_TYPE_NAME,typeMatName);
        cv.put(FM.FM_MAT_CATEGORY_ID,category_mat_id);
        cv.put(FM.FM_MAT_CATEGORY_NAME,catMatName);
        cv.put(FM.FM_MAT_COST,cost_mat);
        cv.put(FM.FM_MAT_COUNT,count_mat);
        cv.put(FM.FM_MAT_UNIT,unit_mat);
        cv.put(FM.FM_MAT_SUMMA,summa_mat);

        // вставляем строку
        long ID = db.insert(FM.TABLE_NAME, null, cv);
        // закрываем соединение с базой
        db.close();
        Log.d(TAG, "MyDatabaseHelper.insertRowInFM_Name  FM._ID = " + ID);

        return ID;
    }

    /**
     * Возвращает имя типа татериала  по  ID типа
     */
    public String getTypeNameMatById( long type_id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mCursor = db.query(true, TypeMat.TABLE_NAME,
                null,
                TypeMat._ID + "=" + type_id,
                null, null, null, null, null);
        if (mCursor.getCount() != 0) {
            mCursor.moveToFirst();
        }
        String typeMatName = mCursor.getString(mCursor.getColumnIndex(TypeMat.TYPE_MAT_NAME));
        Log.d(TAG, "getTypeNameMatById typeMatName = " + typeMatName);
        mCursor.close();
        return typeMatName;
    }

    /**
     * Возвращает имя категории работы по его ID
     */
    public String getCategoryMatNameById( long cat_mat_id) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mCursor = db.query(true, CategoryMat.TABLE_NAME,
                null,
                CategoryMat._ID + "=" + cat_mat_id,
                null, null, null, null, null);
        if (mCursor.getCount() != 0) {
            mCursor.moveToFirst();
        }
        String catMatName = mCursor.getString(mCursor.getColumnIndex(CategoryMat.CATEGORY_MAT_NAME));
        Log.d(TAG, "getCategoryMatNameById catMatName = " + catMatName);
        mCursor.close();
        return catMatName;
    }

    //удаляем работу из сметы FW по file_id и mat_id
    public void deleteMatItemFromFM(long file_id, long mat_id){
        Log.i(TAG, "SmetaOpenHelper.deleteMatItemFromFM ... ");
        SQLiteDatabase db = this.getWritableDatabase();
        int del = db.delete(
                FM.TABLE_NAME, FM.FM_FILE_ID + " =? " + " AND " + FM.FM_MAT_ID + " =? ",
                new String[]{String.valueOf(file_id), String.valueOf(mat_id)});
        Log.i(TAG, "SmetaOpenHelper.deleteMatItemFromFM  - del =  " + del);
    }

    //получаем id типа работы
    public long getTypeIdMat(long file_id, long mat_id){
        Log.i(TAG, "SmetaOpenHelper.getTypeIdMat ... ");
        String select = " SELECT " + FM.FM_MAT_TYPE_ID + " FROM " + FM.TABLE_NAME +
                " where " + FM.FM_FILE_ID + " =? " + " and " + FM.FM_MAT_ID + " =? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(file_id),String.valueOf(mat_id)});
        Log.i(TAG, "SmetaOpenHelper.getTypeIdMat cursor.getCount() = " + cursor.getCount());
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            // Узнаем индекс столбца и Используем индекс для получения количества работы
            long type_id = cursor.getLong(cursor.getColumnIndex(FM.FM_MAT_TYPE_ID));
            Log.i(TAG, "SmetaOpenHelper.getTypeIdMat type_id = " + type_id );
            return type_id;
        }
            cursor.close();
        return -1;
    }

    //получаем id категории работы
    public long getCateIdMat(long file_id, long mat_id){
        Log.i(TAG, "SmetaOpenHelper.getCateIdMat ... ");
        String select = " SELECT " + FM.FM_MAT_CATEGORY_ID + " FROM " + FM.TABLE_NAME +
                " where " + FM.FM_FILE_ID + " =? " + " and " + FM.FM_MAT_ID + " =? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(file_id),String.valueOf(mat_id)});
        Log.i(TAG, "SmetaOpenHelper.getCateIdMat cursor.getCount() = " + cursor.getCount());
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            // Узнаем индекс столбца и Используем индекс для получения количества работы
            long cat_id = cursor.getLong(cursor.getColumnIndex(FM.FM_MAT_CATEGORY_ID));
            Log.i(TAG, "SmetaOpenHelper.getCateIdMat cat_id = " + cat_id );
            return cat_id;
        }
            cursor.close();
        return -1;
    }
    //получаем стоимость материала по её id
    public float getCostMatById(long mat_id){
        Log.i(TAG, "SmetaOpenHelper.getCostMatById ... ");
        float costOfMat = 0;
        String cost = " SELECT " + CostMat.COST_MAT_COST +
                " FROM " + CostMat.TABLE_NAME +
                " WHERE " + CostMat.COST_MAT_ID  + " = ?" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(cost, new String[]{String.valueOf(mat_id)});
        Log.d(TAG, "getCostMatById cursor.getCount() = " + cursor.getCount());

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            // Узнаем индекс каждого столбца
            int idColumnIndex = cursor.getColumnIndex(CostMat.COST_MAT_COST);
            // Используем индекс для получения строки или числа
            costOfMat = cursor.getFloat(idColumnIndex);
        }
        Log.d(TAG, "getCostMatById costOfMat = " + costOfMat);
        cursor.close();
        return costOfMat;
    }

    //Добавляем вид материала с левыми параметрами, чобы удалить при отказе пользователя
    public long  insertCostMatZero(long mat_Id){
        Log.i(TAG, "SmetaOpenHelper.insertCostMatZero ... ");

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CostMat.COST_MAT_ID, mat_Id);
        cv.put(CostMat.COST_MAT_UNIT_ID, 1);
        cv.put(CostMat.COST_MAT_COST,0);
        cv.put(CostMat.COST_MAT_NUMBER,1);
        // вставляем строку
        long ID = db.insert(CostMat.TABLE_NAME, null, cv);
        // закрываем соединение с базой
        db.close();
        Log.d(TAG, "MyDatabaseHelper.insertCostMatZero  CostMat._ID = " + ID);
        return ID;
    }

    //получаем массив названий типов материала
    public String[] getArrayUnitsMatNames() {
        SQLiteDatabase db = getReadableDatabase();
        Log.i(TAG, "SmetaOpenHelper.getArrayUnitsMatNames ... ");
        String unitsMatName  = " SELECT " + UnitMat.UNIT_MAT_NAME  + " FROM " + UnitMat.TABLE_NAME;
        Cursor cursor = db.rawQuery(unitsMatName, null);
        String[] units_name = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            units_name[position] = cursor.getString(cursor.getColumnIndex(UnitMat.UNIT_MAT_NAME));
            Log.i(TAG, "SmetaOpenHelper.getArrayUnitsMatNames position = " + position);
        }
        return units_name;
    }

    //получаем единицы измерения материала с помощью вложенного запроса
    public String getCostUnitMatById(long mat_id){
        Log.i(TAG, "SmetaOpenHelper.getCostUnitMatById ... ");
        String unitMatName = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String unit = " SELECT " +  UnitMat.UNIT_MAT_NAME +
                " FROM " + UnitMat.TABLE_NAME  +
                " WHERE " + UnitMat._ID + " IN " +
                "(" + " SELECT " + CostMat.COST_MAT_UNIT_ID +
                " FROM " + CostMat.TABLE_NAME +
                " WHERE " + CostMat.COST_MAT_ID  + " = " +  String.valueOf(mat_id) + ")";

        Cursor cursor = db.rawQuery(unit, null);

        Log.d(TAG, "getCostUnitMatById cursor.getCount() = " + cursor.getCount());

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            // Узнаем индекс  столбца
            int idColumnIndex = cursor.getColumnIndex(UnitMat.UNIT_MAT_NAME);
            // Используем индекс для получения строки или числа
            unitMatName = cursor.getString(idColumnIndex);
            Log.d(TAG, "getCostUnitMatById unitName = " + unitMatName);
        }
            cursor.close();
        return unitMatName;
    }

    //получаем ID по имени  материала
    public long getIdFromUnitMatName(String unitMatName) {
        long currentID;
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                UnitMat.TABLE_NAME,   // таблица
                new String[]{UnitMat._ID},            // столбцы
                UnitMat.UNIT_MAT_NAME + "=?",                  // столбцы для условия WHERE
                new String[]{unitMatName},                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            // Узнаем индекс каждого столбца
            int idColumnIndex = cursor.getColumnIndex(UnitMat._ID);
            // Используем индекс для получения строки или числа
            currentID = cursor.getLong(idColumnIndex);
        } else {
            currentID = -1;
        }
        Log.d(TAG, "getIdFromUnitName currentID = " + currentID);
            cursor.close();
        return currentID;
    }

    //обновляем цену материала
    public void updateMatkCost(long mat_Id, float costMat, long unit_mat_Id){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(CostMat.COST_MAT_COST, costMat);
        updatedValues.put(CostMat.COST_MAT_UNIT_ID, unit_mat_Id);
        updatedValues.put(CostMat.COST_MAT_NUMBER, 1);

        int countLine = db.update(CostMat.TABLE_NAME, updatedValues,
                CostMat.COST_MAT_ID + "=" + mat_Id, null);
        Log.i(TAG, "SmetaOpenHelper.updateMatkCost - costMat =" + costMat + "  countLine = " + countLine);
    }

    //удаляем цену материала (если в таблице FM нет записей с mat_Id - это проверяется в другом месте)
    public void deleteCostOfMat(long mat_Id) {
        Log.i(TAG, "SmetaOpenHelper.deleteCostOfMat ... ");
        SQLiteDatabase db = this.getWritableDatabase();
        int countDelete = db.delete(CostMat.TABLE_NAME, CostMat.COST_MAT_ID + " =? ",
                new String[]{String.valueOf(mat_Id)});
        Log.i(TAG, "SmetaOpenHelper.deleteCostOfMat countDelete =" + countDelete);
        db.close();
    }

    //находим количество строк типов материала для cat_mat_id
    public int getCountTypeMat(long cat_mat_id){
        Log.i(TAG, "SmetaOpenHelper.getCountTypeMat ... ");

        String select = " SELECT " + TypeMat._ID + " FROM " + TypeMat.TABLE_NAME +
                " where " + TypeMat.TYPE_MAT_CATEGORY_ID + " =? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(cat_mat_id)});
        Log.i(TAG, "SmetaOpenHelper.getCountTypeMat cursor.getCount() = " + cursor.getCount());
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    //удаляем категорию работ (если типы работ отсутствуют - это проверяется в другом месте)
    public void deleteCategoryMat(long cat_mat_Id) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CategoryMat.TABLE_NAME, CategoryMat._ID + " =? " ,
                new String[]{String.valueOf(cat_mat_Id)});
        db.close();
    }

    //получаем количество видов материала с  mat_id в таблице FM
    public int getCountLineMatInFM(long mat_id){
        Log.i(TAG, "SmetaOpenHelper.getCountLineMatInFM ... ");

        String select = " SELECT " + FM._ID + " FROM " + FM.TABLE_NAME +
                " where " + FM.FM_MAT_ID + " =? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(mat_id)});
        Log.i(TAG, "SmetaOpenHelper.getCountLineMatInFM cursor.getCount() = " + cursor.getCount());
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    //получаем количество видов материала с типом type_mat_id
    public int getCountLineMat(long type_mat_id){
        Log.i(TAG, "SmetaOpenHelper.getCountLineMat ... ");

        String select = " SELECT " + Mat._ID + " FROM " + Mat.TABLE_NAME +
                " where " + Mat.MAT_TYPE_ID + " =? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(type_mat_id)});
        Log.i(TAG, "SmetaOpenHelper.getCountLineMat cursor.getCount() = " + cursor.getCount());
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    //получаем данные по категории по её id
    public DataCategoryMat getCategoryMatData(long cat_mat_id){
        Log.i(TAG, "SmetaOpenHelper.getCategoryMatData ... ");
        DataCategoryMat dataCategory = new DataCategoryMat();

        String catData = " SELECT  * FROM " +  CategoryMat.TABLE_NAME +
                " WHERE " + CategoryMat._ID  + " = ? " ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(catData, new String[]{String.valueOf(cat_mat_id)});
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            // Узнаем индекс каждого столбца и Используем индекс для получения строки
            String currentCatName = cursor.getString(cursor.getColumnIndex(CategoryMat.CATEGORY_MAT_NAME));
            String currentCatDescription = cursor.getString(cursor.getColumnIndex(CategoryMat.CATEGORY_MAT_DESCRIPTION));
            Log.d(TAG, "getCategoryMatData currentCatName = " + currentCatName);
            //создаём экземпляр класса DataFile в конструкторе
            dataCategory = new DataCategoryMat(currentCatName, currentCatDescription);
        }
            cursor.close();
        return dataCategory;
    }

    //получаем данные по категории по её id
    public DataTypeMat getTypeMatData(long type_mat_id){
        Log.i(TAG, "SmetaOpenHelper.getTypeMatData ... ");
        DataTypeMat dataTypeMat = new DataTypeMat();

        String typeMatData = " SELECT  * FROM " +  TypeMat.TABLE_NAME +
                " WHERE " + TypeMat._ID  + " = ? " ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(typeMatData, new String[]{String.valueOf(type_mat_id)});
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            // Узнаем индекс каждого столбца и Используем индекс для получения строки
            long currentTypeMatCategoryId = cursor.getLong(cursor.getColumnIndex(TypeMat.TYPE_MAT_CATEGORY_ID));
            String currentTypeMatName = cursor.getString(cursor.getColumnIndex(TypeMat.TYPE_MAT_NAME));
            String currentTypeMatDescription = cursor.getString(cursor.getColumnIndex(TypeMat.TYPE_MAT_DESCRIPTION));
            Log.d(TAG, "getTypeMatData currentTypeName = " + currentTypeMatName);
            //создаём экземпляр класса DataFile в конструкторе
            dataTypeMat = new DataTypeMat(currentTypeMatCategoryId, currentTypeMatName, currentTypeMatDescription);
        }
            cursor.close();
        return dataTypeMat;
    }

    //получаем данные по категории по её id
    public DataMat getMatData(long mat_id){
        Log.i(TAG, "SmetaOpenHelper.getMatData ... ");
        DataMat dataMat = new DataMat();

        String matData = " SELECT  * FROM " +  Mat.TABLE_NAME +
                " WHERE " + Mat._ID  + " = ? " ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(matData, new String[]{String.valueOf(mat_id)});
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            // Узнаем индекс каждого столбца и Используем индекс для получения строки
            long currentMatTypeId = cursor.getLong(cursor.getColumnIndex(Mat.MAT_TYPE_ID));
            String currentMatName = cursor.getString(cursor.getColumnIndex(Mat.MAT_NAME));
            String currentMatDescription = cursor.getString(cursor.getColumnIndex(Mat.MAT_DESCRIPTION));
            Log.d(TAG, "getMatData currentMatName = " + currentMatName);
            //создаём экземпляр класса DataWork в конструкторе
            dataMat = new DataMat(currentMatTypeId, currentMatName, currentMatDescription);
        }
            cursor.close();
        return dataMat;
    }
    //Обновляем данные по категории работ
    public void updateCategoryMatData(long cat_id, String nameCat, String descriptionCat){
        Log.i(TAG, "SmetaOpenHelper.updateCategoryMatData ...");
        SQLiteDatabase db = this.getWritableDatabase();
        //заполняем данные для обновления в базе
        ContentValues values = new ContentValues();
        values.put(CategoryMat.CATEGORY_MAT_NAME, nameCat);
        values.put(CategoryMat.CATEGORY_MAT_DESCRIPTION, descriptionCat);

        db.update(CategoryMat.TABLE_NAME, values,
                CategoryMat._ID + "=" + cat_id, null);
        Log.i(TAG, "SmetaOpenHelper.updateCategoryMatData - name =" + nameCat + "  cat_id = " + cat_id);
    }

    //Обновляем данные по типам материалов
    public void updateTypeMatData(long type_id, String nameType, String descriptionType){
        Log.i(TAG, "SmetaOpenHelper.updateTypeMatData ...");
        SQLiteDatabase db = this.getWritableDatabase();
        //заполняем данные для обновления в базе
        ContentValues values = new ContentValues();
        values.put(TypeMat.TYPE_MAT_NAME, nameType);
        values.put(TypeMat.TYPE_MAT_DESCRIPTION, descriptionType);

        db.update(TypeMat.TABLE_NAME, values,
                TypeMat._ID + "=" + type_id, null);
        Log.i(TAG, "SmetaOpenHelper.updateTypeMatData - nameType =" + nameType + "  type_id = " + type_id);
    }

    //Обновляем данные по материалу
    public void updateMatData(long work_id, String nameWork, String descriptionWork){
        Log.i(TAG, "SmetaOpenHelper.updateMatData ...");
        SQLiteDatabase db = this.getWritableDatabase();
        //заполняем данные для обновления в базе
        ContentValues values = new ContentValues();
        values.put(Mat.MAT_NAME, nameWork);
        values.put(Mat.MAT_DESCRIPTION, descriptionWork);

        db.update(Mat.TABLE_NAME, values,
                Mat._ID + "=" + work_id, null);
        Log.i(TAG, "SmetaOpenHelper.updateMatData - nameWork =" + nameWork + "  work_id = " + work_id);
    }

    //Добавляем категорию материала
    public long  insertCatMatName(String catName){
        Log.i(TAG, "SmetaOpenHelper.insertCatMatName ... ");

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CategoryMat.CATEGORY_MAT_NAME,catName);
        // вставляем строку
        long ID = db.insert(CategoryMat.TABLE_NAME, null, cv);
        // закрываем соединение с базой
        db.close();
        Log.d(TAG, "MyDatabaseHelper.insertCatMatName  CategoryWork._ID = " + ID);
        return ID;
    }

    //Добавляем вид работы
    public long  insertMatName(String matName, long mat_type_Id){
        Log.i(TAG, "SmetaOpenHelper.insertMatkName ... ");

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Mat.MAT_NAME,matName);
        cv.put(Mat.MAT_TYPE_ID, mat_type_Id);
        // вставляем строку
        long ID = db.insert(Mat.TABLE_NAME, null, cv);
        // закрываем соединение с базой
        db.close();
        Log.d(TAG, "MyDatabaseHelper.insertMatkName  Mat._ID = " + ID);
        return ID;
    }

    //получаем ID типа материалов по имени типа материалов
    public long getTypeIdFromTypeMatName(String typeMatName) {
        Log.i(TAG, "SmetaOpenHelper.getTypeIdFromTypeMatName ... ");
        long currentID;
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TypeMat.TABLE_NAME,   // таблица
                new String[]{TypeMat._ID},            // столбцы
                TypeMat.TYPE_MAT_NAME + "=?",                  // столбцы для условия WHERE
                new String[]{typeMatName},                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки
        Log.i(TAG, " SmetaOpenHelper.getIdFromCategoryMatName - cursor.getCount()=" + cursor.getCount());
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            // Узнаем индекс каждого столбца
            int idColumnIndex = cursor.getColumnIndex(TypeMat._ID);
            // Используем индекс для получения строки или числа
            currentID = cursor.getLong(idColumnIndex);
        } else {
            currentID = -1;
        }
        Log.d(TAG, "getIdFromCategoryMatName currentID = " + currentID);
        cursor.close();
        return currentID;
    }

    //удаляем тип материалов(если тип материалов отсутствуют - это проверяется в другом месте)
    public void deleteTypeMat(long type_mat_Id) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TypeMat.TABLE_NAME, TypeMat._ID + " =? " ,
                new String[]{String.valueOf(type_mat_Id)});
        db.close();
    }

    //получаем ID  материалов по имени материалов
    public long getMatIdFromMatName(String matName) {
        Log.i(TAG, "SmetaOpenHelper.getMatIdFromMatName ... ");
        long currentID;
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                Mat.TABLE_NAME,   // таблица
                new String[]{Mat._ID},            // столбцы
                Mat.MAT_NAME + "=?",                  // столбцы для условия WHERE
                new String[]{matName},                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки
        Log.i(TAG, " SmetaOpenHelper.getMatIdFromMatName - cursor.getCount()=" + cursor.getCount());
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            // Узнаем индекс каждого столбца
            int idColumnIndex = cursor.getColumnIndex(Mat._ID);
            // Используем индекс для получения строки или числа
            currentID = cursor.getLong(idColumnIndex);
        } else {
            currentID = -1;
        }
        Log.d(TAG, "getMatIdFromMatName currentID = " + currentID);
        cursor.close();
        return currentID;
    }

    //удаляем тип материалов(если тип материалов отсутствуют - это проверяется в другом месте)
    public void deleteMat(long mat_Id) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Mat.TABLE_NAME, Mat._ID + " =? " ,
                new String[]{String.valueOf(mat_Id)});
        db.close();
    }

    //получаем курсор с названиями типов работ по всем категориям
    public Cursor getTypeWorkNamesAllCategories() {
        Log.i(TAG, "SmetaOpenHelper.getTypeMatNamesAllCategories ... ");
        String typeMatNames = " SELECT " + TypeWork._ID + " , " + TypeWork.TYPE_CATEGORY_ID +
                " , " + TypeWork.TYPE_NAME + " FROM " + TypeWork.TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(typeMatNames, null);
        Log.i(TAG, "SmetaOpenHelper.getTypeMatNamesAllCategories cursor.getCount() =  " + cursor.getCount());
        return cursor;
    }

    //получаем ID категории работы по имени типа работы
    public long getCatIdFromTypeWork(long type_id) {
        Log.d(TAG, "getCatIdFromTypeMat ...");
        long currentID;
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TypeWork.TABLE_NAME,   // таблица
                new String[]{TypeWork.TYPE_CATEGORY_ID},            // столбцы
                TypeWork._ID + "=?",   // столбцы для условия WHERE
                new String[]{String.valueOf(type_id)},                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            // Узнаем индекс каждого столбца
            int idColumnIndex = cursor.getColumnIndex(TypeWork.TYPE_CATEGORY_ID);
            // Используем индекс для получения строки или числа
            currentID = cursor.getLong(idColumnIndex);
        } else {
            currentID = -1;
        }
        Log.d(TAG, "getCatIdFromTypeMat currentID = " + currentID);
        cursor.close();
        return currentID;
    }

    //получаем курсор с названиями  материалов
    public Cursor getWorkNamesAllTypes() {
        Log.i(TAG, "SmetaOpenHelper.getWorkNamesAllTypes ... ");
        String matNames = " SELECT " + Work._ID + " , " +
                Work.WORK_NAME + " FROM " + Work.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(matNames, null);
        Log.i(TAG, "SmetaOpenHelper.getWorkNamesAllTypes cursor.getCount() =  " + cursor.getCount());
        return cursor;
    }

    //Добавляем тип материала
    public long  insertTypeMatName(String typeMatName, long type_mat_category_Id){
        Log.i(TAG, "SmetaOpenHelper.insertTypeMatName ... ");

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TypeMat.TYPE_MAT_NAME,typeMatName);
        cv.put(TypeMat.TYPE_MAT_CATEGORY_ID,type_mat_category_Id);
        // вставляем строку
        long ID = db.insert(TypeMat.TABLE_NAME, null, cv);
        // закрываем соединение с базой
        db.close();
        Log.d(TAG, "MyDatabaseHelper.insertTypeMatName  TypeMat._ID = " + ID);
        return ID;
    }

    //Добавляем тип материала
    public long  insertTypeWorkName(String typeMatName, long type_mat_category_Id){
        Log.i(TAG, "SmetaOpenHelper.insertTypeWorkName ... ");

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TypeWork.TYPE_NAME,typeMatName);
        cv.put(TypeWork.TYPE_CATEGORY_ID,type_mat_category_Id);
        // вставляем строку
        long ID = db.insert(TypeMat.TABLE_NAME, null, cv);
        // закрываем соединение с базой
        db.close();
        Log.d(TAG, "MyDatabaseHelper.insertTypeWorkName  TypeMat._ID = " + ID);
        return ID;
    }

   //Добавляем тип материала
    public long  insertCostMat( long matID, float cost, long unit_mat_id){
        Log.i(TAG, "SmetaOpenHelper.insertCostMat ... ");

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CostMat.COST_MAT_ID,matID);
        cv.put(CostMat.COST_MAT_COST,cost);
        cv.put(CostMat.COST_MAT_UNIT_ID,unit_mat_id);
        // вставляем строку
        long ID = db.insert(CostMat.TABLE_NAME, null, cv);
        // закрываем соединение с базой
        db.close();
        Log.d(TAG, "MyDatabaseHelper.insertCostMat  CostMat._ID = " + ID);
        return ID;
    }

    //Добавляем тип материала
    public long  insertCostWork( long workID, float cost, long unit_work_id){
        Log.i(TAG, "SmetaOpenHelper.insertCostWork ... ");

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CostWork.COST_WORK_ID,workID);
        cv.put(CostWork.COST_COST,cost);
        cv.put(CostWork.COST_UNIT_ID,unit_work_id);
        // вставляем строку
        long ID = db.insert(CostWork.TABLE_NAME, null, cv);
        // закрываем соединение с базой
        db.close();
        Log.d(TAG, "MyDatabaseHelper.insertCostWork  CostWork._ID = " + ID);
        return ID;
    }

}
