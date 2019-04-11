package ru.bartex.smetaelectro.ru.bartex.smetaelectro.data;


import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.Calendar;
import java.util.GregorianCalendar;

import ru.bartex.smetaelectro.DataCategory;
import ru.bartex.smetaelectro.DataFile;
import ru.bartex.smetaelectro.DataType;
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
                + FileWork.FILE_NAME + " TEXT NOT NULL, "
                + FileWork.ADRESS + " TEXT, "
                + FileWork.FILE_NAME_DATE + " TEXT NOT NULL DEFAULT current_date, "
                + FileWork.FILE_NAME_TIME + " TEXT NOT NULL DEFAULT current_time, "
                + FileWork.DESCRIPTION_OF_FILE + " TEXT NOT NULL DEFAULT 'Без описания');";
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TAB_FILE);
        // Если файлов в базе нет, вносим запись с именем файла по умолчанию P.FILENAME_DEFAULT
        this.createDefaultFile(db);

        // Строка для создания основной таблицы базы, записи добавляются только программно
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

        // Строка для создания таблицы категорий работ CategoryWork
        String SQL_CREATE_TAB_CATEGORY = "CREATE TABLE " + CategoryWork.TABLE_NAME + " ("
                + CategoryWork._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CategoryWork.CATEGORY_NAME + " TEXT NOT NULL, "
                + CategoryWork.CATEGORY_DESCRIPTION + " TEXT NOT NULL DEFAULT 'Без описания');";
        // Запускаем создание таблицы категорий работ
        db.execSQL(SQL_CREATE_TAB_CATEGORY);
        // Если файлов в базе нет, вносим записи названий категорий работ (добавление из программы)
        this.createDefaultCategory(db);

        // Строка для создания таблицы разделов (типов) работ TypeWork
        String SQL_CREATE_TAB_TYPE = "CREATE TABLE " + TypeWork.TABLE_NAME + " ("
                + TypeWork._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TypeWork.TYPE_CATEGORY_ID + " INTEGER NOT NULL, "
                + TypeWork.TYPE_NAME + " TEXT NOT NULL, "
                + TypeWork.TYPE_DESCRIPTION + " TEXT NOT NULL DEFAULT 'Без описания');";
        // Запускаем создание таблицы разделов (типов) работ TypeWork
        db.execSQL(SQL_CREATE_TAB_TYPE);
        // Если файлов в базе нет, вносим записи названия типов работ (добавление из программы)
        this.createDefaultType(db);

        // Строка для создания таблицы конкретных работ Work
        String SQL_CREATE_TAB_WORK = "CREATE TABLE " + Work.TABLE_NAME + " ("
                + Work._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Work.WORK_TYPE_ID + " INTEGER NOT NULL, "
                + Work.WORK_NAME + " TEXT NOT NULL, "
                + Work.WORK_DESCRIPTION + " TEXT NOT NULL DEFAULT 'Без описания');";
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TAB_WORK);
        // Если файлов в базе нет, вносим записи названия  работ (добавление из программы)
        this.createDefaultWork(db);

        // Строка для создания таблицы единиц измерения Unit
        String SQL_CREATE_TAB_UNIT = "CREATE TABLE " + Unit.TABLE_NAME + " ("
                + Unit._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Unit.UNIT_NAME + " TEXT NOT NULL);";
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TAB_UNIT);
        // Если файлов в базе нет, вносим записи единиц измерения
        this.createDefaultUnit(db);

        // Строка для создания таблицы расценок CostWork
        String SQL_CREATE_TAB_COST = "CREATE TABLE " + CostWork.TABLE_NAME + " ("
                + CostWork._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CostWork.COST_WORK_ID + " INTEGER NOT NULL, "
                + CostWork.COST_UNIT_ID + " INTEGER NOT NULL, "
                + CostWork.COST_COST + " REAL NOT NULL DEFAULT 0, "
                + CostWork.COST_NUMBER + " INTEGER NOT NULL DEFAULT 1);";
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TAB_COST);
        // Если файлов в базе нет, вносим записи расценок
        this.createDefaultCost(db);

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

        Log.d(TAG, "Создана база данных  " + DATABASE_NAME);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Когда файлов в базе нет, вносим запись с именем файла по умолчанию (Используем здесь)
    public void createDefaultFile(SQLiteDatabase db) {
            //получаем дату и время в нужном для базы данных формате
            String dateFormat = this.getDateString();
            String timeFormat = this.getTimeString();
            //создаём экземпляр класса DataFile в конструкторе
            //DataFile file = new DataFile(P.FILENAME_DEFAULT, P.ADRESS_DEFAULT,
            //        dateFormat, timeFormat, P.DESCRIPTION_DEFAULT);

        ContentValues cv = new ContentValues();
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
        Log.d(TAG, "getIdFromName currentID = " + currentID);
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
    }

    public void InsertType( SQLiteDatabase db,ContentValues values,
                            int category_id,  String type_name){
        values.put(TypeWork.TYPE_CATEGORY_ID, category_id);
        values.put(TypeWork.TYPE_NAME, type_name);
        db.insert(TypeWork.TABLE_NAME, null, values);
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

        String[] work_name_type_hren = res.getStringArray(R.array.work_name_type_hren);
        String[] work_name_type_full_hren = res.getStringArray(R.array.work_name_type_full_hren);

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
        for (int i = 0; i <work_name_type_hren.length; i++ ){
            this.InsertWork(db, values, type_id[ii],  work_name_type_hren[i]);
        }
        ii = 14; //work_name_type_full_hren
        for (int i = 0; i <work_name_type_full_hren.length; i++ ){
            this.InsertWork(db, values, type_id[ii],  work_name_type_full_hren[i]);
        }
    }

    public void InsertWork( SQLiteDatabase db,ContentValues values,
                            int type_id,  String work_name){
        values.put(Work.WORK_TYPE_ID, type_id);
        values.put(Work.WORK_NAME, work_name);
        db.insert(Work.TABLE_NAME, null, values);
    }

    public void createDefaultUnit(SQLiteDatabase db){
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
    }

    public void createDefaultCost(SQLiteDatabase db){

        ContentValues values = new ContentValues();

        // Получим массив строк из ресурсов
        Resources res = fContext.getResources();

        int [] work_id = this.getIdFromWorks(db);
        int length = work_id.length;

        String[] unit_name = res.getStringArray(R.array.unit_of_work);
        int[] unit_id =new int[unit_name.length];
        for (int i = 0; i<length; i++){
            unit_id[i] = Integer.valueOf(unit_name[i]);
        }

        String[] cost = res.getStringArray(R.array.cost_of_work);
        float[] cost_of_work = new float[cost.length];
        for (int i = 0; i < length; i++){
            cost_of_work[i] = Float.valueOf(cost[i]);
        }

        // проходим через массив и вставляем записи в таблицу
        for (int i = 0; i < length; i++){
            // Добавляем записи в таблицу
            this.InsertCost(db, values, work_id[i], unit_name[i], cost_of_work[i]);
        }
    }

    public void InsertCost( SQLiteDatabase db,ContentValues values,
                            long work_id,  String unit_name, float cost){
        values.put(CostWork.COST_WORK_ID, work_id);
        values.put(CostWork.COST_UNIT_ID, unit_name);
        values.put(CostWork.COST_COST, cost);
        db.insert(CostWork.TABLE_NAME, null, values);
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

    //получаем курсор с названиями типов работ
    public Cursor getTypeNames(long cat_id) {
        Log.i(TAG, "SmetaOpenHelper.getTypeNames ... ");
        String typeNames = " SELECT " + TypeWork._ID + " , " + TypeWork.TYPE_CATEGORY_ID +
                " , " + TypeWork.TYPE_NAME + " FROM " + TypeWork.TABLE_NAME +
                " WHERE " + TypeWork.TYPE_CATEGORY_ID  + " = ?" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(typeNames, new String[]{String.valueOf(cat_id)});
        Log.i(TAG, "SmetaOpenHelper.getTypeNames cursor.getCount() =  " + cursor.getCount());
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

    //получаем ID по имени категории
    public long getIdFromCategoryName(String name) {
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

        if ((cursor != null) && (cursor.getCount() != 0)) {
            cursor.moveToFirst();
            // Узнаем индекс каждого столбца
            int idColumnIndex = cursor.getColumnIndex(CategoryWork._ID);
            // Используем индекс для получения строки или числа
            currentID = cursor.getLong(idColumnIndex);
        } else {
            currentID = -1;
        }
        Log.d(TAG, "getIdFromCategoryName currentID = " + currentID);
        if (cursor != null) {
            cursor.close();
        }
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
        Log.d(TAG, "getIdFromWorks currentID.length = " + currentID.length);
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
        Log.d(TAG, "getIdFromWorkName currentWorkName = " + currentWorkName);
        if (cursor != null) {
            cursor.close();
        }
        return currentWorkName;
    }



    //получаем стоимость работы по её id
    public float getWorkCostById(long work_id){
        Log.i(TAG, "SmetaOpenHelper.getWorkCostById ... ");
       float costOfWork = -1;
        String cost = " SELECT " + CostWork._ID + " , " +  CostWork.COST_COST +
                " FROM " + CostWork.TABLE_NAME +
                " WHERE " + CostWork.COST_WORK_ID  + " = ?" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(cost, new String[]{String.valueOf(work_id)});
        Log.d(TAG, "getWorkCostById cursor.getCount() = " + cursor.getCount());

        if ((cursor != null) && (cursor.getCount() != 0)) {
            cursor.moveToFirst();
            // Узнаем индекс каждого столбца
            int idColumnIndex = cursor.getColumnIndex(CostWork.COST_COST);
            // Используем индекс для получения строки или числа
            costOfWork = cursor.getFloat(idColumnIndex);
        }
        Log.d(TAG, "getWorkCostById costOfWork = " + costOfWork);
        if (cursor != null) {
            cursor.close();
        }
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

        if ((cursor != null) && (cursor.getCount() != 0)) {
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

            if ((cursor1 != null) && (cursor1.getCount() != 0)) {
                cursor1.moveToFirst();
                // Узнаем индекс  столбца
                int idColumnIndex1 = cursor1.getColumnIndex(Unit.UNIT_NAME);
                // Используем индекс для получения строки или числа
                unitName = cursor1.getString(idColumnIndex1);
                Log.d(TAG, "getWorkCostById unitName = " + unitName);
            }
            if (cursor1 != null) {
                cursor1.close();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
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

        if ((cursor != null) && (cursor.getCount() != 0)) {
            cursor.moveToFirst();
            // Узнаем индекс  столбца
            int idColumnIndex = cursor.getColumnIndex(Unit.UNIT_NAME);
            // Используем индекс для получения строки или числа
            unitName = cursor.getString(idColumnIndex);
            Log.d(TAG, "getWorkCostById unitName = " + unitName);
        }
        if (cursor != null) {
            cursor.close();
        }
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
        String fileName = this.getFileNameById(file_id,db);
        Log.i(TAG, "SmetaOpenHelper.insertRowInFW_Name fileName =  " + fileName);
        //получаем имя работы по id работы
        String workName = this.getWorkNameById(work_id, db);
        Log.i(TAG, "SmetaOpenHelper.insertRowInFW_Name workName =  " + workName);
        //получаем имя типа работы по id типа работы
        String typeName = this.getTypeNameById(type_id, db);
        Log.i(TAG, "SmetaOpenHelper.insertRowInFW_Name typeName =  " + typeName);
        //получаем имя категории работы по id категории работы
        String catName = this.getCategoryNameById(category_id,db);
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
            long file_id, long work_id, float cost_work, float count, float summa){

        Log.i(TAG, "SmetaOpenHelper.updateRowInFW_Count_Summa ... ");
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(FW.FW_COST, cost_work);
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
    public String getFileNameById( long file_id, SQLiteDatabase db) throws SQLException {
        Cursor mCursor = db.query(true, FileWork.TABLE_NAME,
                null,
                FileWork._ID + "=" + file_id,
                null, null, null, null, null);
        if ((mCursor != null) && (mCursor.getCount() != 0)) {
            mCursor.moveToFirst();
        }
        String fileName = mCursor.getString(mCursor.getColumnIndex(FileWork.FILE_NAME));
        Log.d(TAG, "getFileNameById fileName = " + fileName);
        mCursor.close();
        return fileName;
    }
    /**
     * Возвращает имя работы по его ID
     */
    public String getWorkNameById( long work_id, SQLiteDatabase db) throws SQLException {
        Cursor mCursor = db.query(true, Work.TABLE_NAME,
                null,
                Work._ID + "=" + work_id,
                null, null, null, null, null);
        if ((mCursor != null) && (mCursor.getCount() != 0)) {
            mCursor.moveToFirst();
        }
        String workName = mCursor.getString(mCursor.getColumnIndex(Work.WORK_NAME));
        Log.d(TAG, "getWorkNameById workName = " + workName);
        mCursor.close();
        return workName;
    }
    /**
     * Возвращает имя типа работы по его ID
     */
    public String getTypeNameById( long type_id, SQLiteDatabase db) throws SQLException {
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
    public String getCategoryNameById( long cat_id, SQLiteDatabase db) throws SQLException {
        Cursor mCursor = db.query(true, CategoryWork.TABLE_NAME,
                null,
                TypeWork._ID + "=" + cat_id,
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

    //обновляем цену работы
    public void updateWorkCost(long work_Id, float cost){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(CostWork.COST_COST, cost);
        db.update(CostWork.TABLE_NAME, updatedValues,
                CostWork.COST_WORK_ID + "=" + work_Id, null);
        Log.i(TAG, "SmetaOpenHelper.updateWorkCost - cost =" + cost + "  work_Id = " + work_Id);
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
        if (cursor != null) {
            cursor.close();
        }
        return file_name;
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
        if (cursor != null) {
            cursor.close();
        }
        return work_name;
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
        if (cursor != null) {
            cursor.close();
        }
        return work_cost;
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
        if (cursor != null) {
            cursor.close();
        }
        return work_amount;
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
        if (cursor != null) {
            cursor.close();
        }
        return work_units;
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
        if (cursor != null) {
            cursor.close();
        }
        return work_summa;
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
        if (cursor != null) {
            cursor.close();
        }
        return categoryNamesFW;
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
        if (cursor != null) {
            cursor.close();
        }
        return typeNamesFW;
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
        if (cursor != null) {
            cursor.close();
        }
        return workNamesFW;
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

    public boolean isWork(long file_id, long work_id){
        Log.i(TAG, "SmetaOpenHelper.isWork ... ");

        String select = " SELECT " + FW.FW_WORK_NAME + " FROM " + FW.TABLE_NAME +
        " where " + FW.FW_FILE_ID + " =? " + " and " + FW.FW_WORK_ID + " =? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(file_id),String.valueOf(work_id)});
        Log.i(TAG, "SmetaOpenHelper.isWork cursor.getCount() = " + cursor.getCount());
        if ((cursor != null) && (cursor.getCount() != 0)) {
            return true;
        }
        if (cursor != null) {
            cursor.close();
        }
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
        if (cursor != null) {
            cursor.close();
        }
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
        if (cursor != null) {
            cursor.close();
        }
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
        if (cursor != null) {
            cursor.close();
        }
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

    //получаем курсор с Category._id  и делаем массив id
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

}
