package ru.bartex.smetaelectro.ru.bartex.smetaelectro.data;


import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.Calendar;
import java.util.GregorianCalendar;

import ru.bartex.smetaelectro.DataFile;
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
                + FileWork.COLUMN_FILE_NAME + " TEXT NOT NULL, "
                + FileWork.COLUMN_ADRESS + " TEXT, "
                + FileWork.COLUMN_FILE_NAME_DATE + " TEXT NOT NULL, "
                + FileWork.COLUMN_FILE_NAME_TIME + " TEXT NOT NULL, "
                + FileWork.COLUMN_DESCRIPTION_OF_FILE + " TEXT NOT NULL DEFAULT 'Без описания');";
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TAB_FILE);
        // Если файлов в базе нет, вносим запись с именем файла по умолчанию P.FILENAME_DEFAULT
        this.createDefaultFile(db);

        // Строка для создания основной таблицы базы, записи добавляются только программно
           String SQL_CREATE_TAB_FILE_AND_WORK  = "CREATE TABLE " + FW.TABLE_NAME + " ("
                + FW._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FW.COLUMN_FW_FILE_ID + " INTEGER NOT NULL, "
                + FW.COLUMN_FW_WORK_ID + " INTEGER NOT NULL, "
                + FW.COLUMN_FW_TYPE_ID + " INTEGER NOT NULL, "
                + FW.COLUMN_FW_CATEGORY_ID + " INTEGER NOT NULL, "
                + FW.COLUMN_FW_COST + " REAL NOT NULL, "
                + FW.COLUMN_FW_COUNT + " INTEGER NOT NULL, "
                + FW.COLUMN_FW_UNIT + " TEXT NOT NULL, "
                + FW.COLUMN_FW_SUMMA + " REAL NOT NULL);";
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TAB_FILE_AND_WORK);

        // Строка для создания таблицы категорий работ CategoryWork
        String SQL_CREATE_TAB_CATEGORY = "CREATE TABLE " + CategoryWork.TABLE_NAME + " ("
                + CategoryWork._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CategoryWork.COLUMN_CATEGORY_MARK + " INTEGER NOT NULL DEFAULT 0, "
                + CategoryWork.COLUMN_CATEGORY_NAME + " TEXT NOT NULL, "
                + CategoryWork.COLUMN_CATEGORY_DESCRIPTION + " TEXT NOT NULL DEFAULT 'Без описания');";
        // Запускаем создание таблицы категорий работ
        db.execSQL(SQL_CREATE_TAB_CATEGORY);
        // Если файлов в базе нет, вносим записи названий категорий работ (добавление из программы)
        this.createDefaultCategory(db);

        // Строка для создания таблицы разделов (типов) работ TypeWork
        String SQL_CREATE_TAB_TYPE = "CREATE TABLE " + TypeWork.TABLE_NAME + " ("
                + TypeWork._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TypeWork.COLUMN_TYPE_CATEGORY_ID + " INTEGER NOT NULL, "
                + TypeWork.COLUMN_TYPE_MARK + " INTEGER NOT NULL DEFAULT 0, "
                + TypeWork.COLUMN_TYPE_NAME + " TEXT NOT NULL, "
                + TypeWork.COLUMN_TYPE_DESCRIPTION + " TEXT NOT NULL DEFAULT 'Без описания');";
        // Запускаем создание таблицы разделов (типов) работ TypeWork
        db.execSQL(SQL_CREATE_TAB_TYPE);
        // Если файлов в базе нет, вносим записи названия типов работ (добавление из программы)
        this.createDefaultType(db);

        // Строка для создания таблицы конкретных работ Work
        String SQL_CREATE_TAB_WORK = "CREATE TABLE " + Work.TABLE_NAME + " ("
                + Work._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Work.COLUMN_WORK_TYPE_ID + " INTEGER NOT NULL, "
                + Work.COLUMN_WORK_NAME + " TEXT NOT NULL, "
                + Work.COLUMN_WORK_DONE + " INTEGER NOT NULL DEFAULT 0, "
                + Work.COLUMN_WORK_DESCRIPTION + " TEXT NOT NULL DEFAULT 'Без описания');";
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TAB_WORK);
        // Если файлов в базе нет, вносим записи названия  работ (добавление из программы)
        this.createDefaultWork(db);

        // Строка для создания таблицы единиц измерения Unit
        String SQL_CREATE_TAB_UNIT = "CREATE TABLE " + Unit.TABLE_NAME + " ("
                + Unit._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Unit.COLUMN_UNIT_NAME + " INTEGER NOT NULL);";
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TAB_UNIT);
        // Если файлов в базе нет, вносим записи единиц измерения
        this.createDefaultUnit(db);

        // Строка для создания таблицы расценок CostWork
        String SQL_CREATE_TAB_COST = "CREATE TABLE " + CostWork.TABLE_NAME + " ("
                + CostWork._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CostWork.COLUMN_COST_WORK_ID + " INTEGER NOT NULL, "
                + CostWork.COLUMN_COST_UNIT_ID + " INTEGER NOT NULL, "
                + CostWork.COLUMN_COST_COST + " REAL NOT NULL DEFAULT 0, "
                + CostWork.COLUMN_COST_NUMBER + " INTEGER NOT NULL DEFAULT 1);";
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TAB_COST);
        // Если файлов в базе нет, вносим записи расценок
        this.createDefaultCost(db);

        // Строка для создания таблицы смет TotalTab
        String SQL_CREATE_TAB_TOTAL = "CREATE TABLE " + TotalTab.TABLE_NAME + " ("
                + TotalTab._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TotalTab.COLUMN_TOTAL_FILE_ID + " TEXT NOT NULL, "
                + TotalTab.COLUMN_TOTAL_CATEGORY_ID + " TEXT NOT NULL, "
                + TotalTab.COLUMN_TOTAL_TYPE_ID + " TEXT NOT NULL, "
                + TotalTab.COLUMN_TOTAL_WORK_ID + " TEXT NOT NULL, "
                + TotalTab.COLUMN_TOTAL_COST + " REAL NOT NULL DEFAULT 0, "
                + TotalTab.COLUMN_TOTAL_COST_NUMBER + " INTEGER NOT NULL DEFAULT 0, "
                + TotalTab.COLUMN_TOTAL_COST_UNIT + " TEXT NOT NULL, "
                + TotalTab.COLUMN_TOTAL_SUMMA + " REAL NOT NULL DEFAULT 0);";

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
        cv.put(FileWork.COLUMN_FILE_NAME, P.FILENAME_DEFAULT);
        cv.put(FileWork.COLUMN_ADRESS, P.ADRESS_DEFAULT);
        cv.put(FileWork.COLUMN_FILE_NAME_DATE, dateFormat);
        cv.put(FileWork.COLUMN_FILE_NAME_TIME, timeFormat);
        cv.put(FileWork.COLUMN_DESCRIPTION_OF_FILE, P.DESCRIPTION_DEFAULT);
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
        cv.put(FileWork.COLUMN_FILE_NAME, file.getFileName());
        cv.put(FileWork.COLUMN_ADRESS, file.getAdress());
        cv.put(FileWork.COLUMN_FILE_NAME_DATE, file.getFileNameDate());
        cv.put(FileWork.COLUMN_FILE_NAME_TIME, file.getFileNameTime());
        cv.put(FileWork.COLUMN_DESCRIPTION_OF_FILE, file.getDescription());
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
                FileWork.COLUMN_FILE_NAME + "=?",                  // столбцы для условия WHERE
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
            values.put(CategoryWork.COLUMN_CATEGORY_MARK, 0);
            values.put(CategoryWork.COLUMN_CATEGORY_NAME, cat_name[i]);
            values.put(CategoryWork.COLUMN_CATEGORY_DESCRIPTION, cat_descr[i]);
            // Добавляем записи в таблицу
            db.insert(CategoryWork.TABLE_NAME, null, values);
        }
    }

    //получаем курсор с названиями категорий и отметкой о заходе в категорию
    public Cursor getCategoryNames() {
        Log.i(TAG, "SmetaOpenHelper.getCategoryNames ... ");
        String categoryNames = " SELECT " + CategoryWork._ID + " , " +
                CategoryWork.COLUMN_CATEGORY_NAME + " , " +
                CategoryWork.COLUMN_CATEGORY_MARK + " FROM " + CategoryWork.TABLE_NAME;
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
        values.put(TypeWork.COLUMN_TYPE_CATEGORY_ID, category_id);
        values.put(TypeWork.COLUMN_TYPE_MARK, 0);
        values.put(TypeWork.COLUMN_TYPE_NAME, type_name);
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
        values.put(Work.COLUMN_WORK_TYPE_ID, type_id);
        values.put(Work.COLUMN_WORK_DONE, 0);
        values.put(Work.COLUMN_WORK_NAME, work_name);
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
            values.put(Unit.COLUMN_UNIT_NAME, unit_name[i]);
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
        values.put(CostWork.COLUMN_COST_WORK_ID, work_id);
        values.put(CostWork.COLUMN_COST_UNIT_ID, unit_name);
        values.put(CostWork.COLUMN_COST_COST, cost);
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

    //получаем курсор с названиями типов работ  и отметкой о заходе в тип для категории с cat_id
    public Cursor getTypeNames(long cat_id) {
        Log.i(TAG, "SmetaOpenHelper.getTypeNames ... ");
        String typeNames = " SELECT " + TypeWork._ID + " , " + TypeWork.COLUMN_TYPE_CATEGORY_ID +
                " , " + TypeWork.COLUMN_TYPE_NAME + " , " +
                TypeWork.COLUMN_TYPE_MARK + " FROM " + TypeWork.TABLE_NAME +
                " WHERE " + TypeWork.COLUMN_TYPE_CATEGORY_ID  + " = ?" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(typeNames, new String[]{String.valueOf(cat_id)});
        Log.i(TAG, "SmetaOpenHelper.getTypeNames cursor.getCount() =  " + cursor.getCount());
        return cursor;
    }

    //получаем курсор с названиями  работ  и отметкой о заходе в тип для типа с type_id
    public Cursor getWorkNames(long type_id) {
        Log.i(TAG, "SmetaOpenHelper.getWorkNames ... ");
        String typeNames = " SELECT " + Work._ID + " , " + Work.COLUMN_WORK_TYPE_ID + " , " +
                Work.COLUMN_WORK_NAME + " , " +  Work.COLUMN_WORK_DONE + " FROM " + Work.TABLE_NAME +
                " WHERE " + Work.COLUMN_WORK_TYPE_ID  + " = ?" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(typeNames, new String[]{String.valueOf(type_id)});
        Log.i(TAG, "SmetaOpenHelper.getWorkNames cursor.getCount() =  " + cursor.getCount());
        return cursor;
    }

    //Метод для изменения отметки категории
    public void updateCategoryMark(int mark, long cat_Id) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(CategoryWork.COLUMN_CATEGORY_MARK, mark);
        db.update(CategoryWork.TABLE_NAME, updatedValues,
                CategoryWork._ID + "=" + cat_Id, null);
        Log.i(TAG, "SmetaOpenHelper.updateCategoryMark ... cat_Id =" + cat_Id);
    }

    //получаем ID по имени категории
    public long getIdFromCategoryName(String name) {
        long currentID;
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                CategoryWork.TABLE_NAME,   // таблица
                new String[]{CategoryWork._ID},            // столбцы
                CategoryWork.COLUMN_CATEGORY_NAME + "=?",                  // столбцы для условия WHERE
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

    //обновляем отметку входа в тип работы
    public void updateTypeMark(int mark, long type_Id){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(TypeWork.COLUMN_TYPE_MARK, mark);
        db.update(TypeWork.TABLE_NAME, updatedValues,
                TypeWork._ID + "=" + type_Id, null);
        Log.i(TAG, "SmetaOpenHelper.updateTypeMark ... type_Id =" + type_Id);
    }

    //получаем ID по имени типа работы
    public long getIdFromTypeName(String name) {
        long currentID;
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TypeWork.TABLE_NAME,   // таблица
                new String[]{TypeWork._ID},            // столбцы
                TypeWork.COLUMN_TYPE_NAME + "=?",                  // столбцы для условия WHERE
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
                Work.COLUMN_WORK_NAME + "=?",                  // столбцы для условия WHERE
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


    //обновляем отметку входа в работу
    public void updateWorkMark(int mark, long work_Id){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues updatedValues = new ContentValues();
        updatedValues.put(Work.COLUMN_WORK_DONE, mark);
        db.update(Work.TABLE_NAME, updatedValues,
                Work._ID + "=" + work_Id, null);
        Log.i(TAG, "SmetaOpenHelper.updateWorkMark ... work_Id =" + work_Id);
    }

    public String getWorkNameById(long work_id){
        Log.i(TAG, "SmetaOpenHelper.getWorkNameById ... ");
        String currentWorkName = "";
        String workName = " SELECT " + Work._ID + " , " +  Work.COLUMN_WORK_NAME +
                " FROM " + Work.TABLE_NAME +
                " WHERE " + Work._ID  + " = ?" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(workName, new String[]{String.valueOf(work_id)});
        if ((cursor != null) && (cursor.getCount() != 0)) {
            cursor.moveToFirst();
            // Узнаем индекс каждого столбца
            int idColumnIndex = cursor.getColumnIndex(Work.COLUMN_WORK_NAME);
            // Используем индекс для получения строки или числа
            currentWorkName = cursor.getString(idColumnIndex);
        }
        Log.d(TAG, "getIdFromWorkName currentWorkName = " + currentWorkName);
        if (cursor != null) {
            cursor.close();
        }
        return currentWorkName;
    }

    public float getWorkCostById(long work_id){
        Log.i(TAG, "SmetaOpenHelper.getWorkCostById ... ");
       float costOfWork = -1;
        String cost = " SELECT " + CostWork._ID + " , " +  CostWork.COLUMN_COST_COST +
                " FROM " + CostWork.TABLE_NAME +
                " WHERE " + CostWork.COLUMN_COST_WORK_ID  + " = ?" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(cost, new String[]{String.valueOf(work_id)});
        Log.d(TAG, "getWorkCostById cursor.getCount() = " + cursor.getCount());

        if ((cursor != null) && (cursor.getCount() != 0)) {
            cursor.moveToFirst();
            // Узнаем индекс каждого столбца
            int idColumnIndex = cursor.getColumnIndex(CostWork.COLUMN_COST_COST);
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
                CostWork.COLUMN_COST_WORK_ID,
                CostWork.COLUMN_COST_UNIT_ID,
                CostWork.COLUMN_COST_COST,
                CostWork.COLUMN_COST_NUMBER};

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
                        cursor.getColumnIndex(CostWork.COLUMN_COST_WORK_ID));
                String current_UNIT = cursor.getString(
                        cursor.getColumnIndex(CostWork.COLUMN_COST_UNIT_ID));
                float current_COST = cursor.getFloat(
                        cursor.getColumnIndex(CostWork.COLUMN_COST_COST));
                int current_NUMBER = cursor.getInt(
                        cursor.getColumnIndex(CostWork.COLUMN_COST_NUMBER));
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
        String unit = " SELECT " +  CostWork.COLUMN_COST_UNIT_ID +
                " FROM " + CostWork.TABLE_NAME  +
                " WHERE " + CostWork.COLUMN_COST_WORK_ID  + " = " +  String.valueOf(work_id);

        Cursor cursor = db.rawQuery(unit, null);

        Log.d(TAG, "getCostUnitById cursor.getCount() = " + cursor.getCount());

        if ((cursor != null) && (cursor.getCount() != 0)) {
            cursor.moveToFirst();
            // Узнаем индекс  столбца
            int idColumnIndex = cursor.getColumnIndex(CostWork.COLUMN_COST_UNIT_ID);
            // Используем индекс для получения строки или числа
            unitId = cursor.getInt(idColumnIndex);
            Log.d(TAG, "getWorkCostById unitId = " + unitId);

            String unitN = " SELECT " +  Unit.COLUMN_UNIT_NAME +
                    " FROM " + Unit.TABLE_NAME +
                    " WHERE " + Unit._ID  + " = " + String.valueOf(unitId);
            Cursor cursor1 = db.rawQuery(unitN, null);
            Log.d(TAG, "getCostUnitById cursor1.getCount() = " + cursor1.getCount());

            if ((cursor1 != null) && (cursor1.getCount() != 0)) {
                cursor1.moveToFirst();
                // Узнаем индекс  столбца
                int idColumnIndex1 = cursor1.getColumnIndex(Unit.COLUMN_UNIT_NAME);
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
        String unit = " SELECT " +  Unit.COLUMN_UNIT_NAME +
                " FROM " + Unit.TABLE_NAME  +
                " WHERE " + Unit._ID + " IN " +
                "(" + " SELECT " + CostWork.COLUMN_COST_UNIT_ID +
                " FROM " + CostWork.TABLE_NAME +
                " WHERE " + CostWork.COLUMN_COST_WORK_ID  + " = " +  String.valueOf(work_id) + ")";

        Cursor cursor = db.rawQuery(unit, null);

        Log.d(TAG, "getCostUnitById cursor.getCount() = " + cursor.getCount());

        if ((cursor != null) && (cursor.getCount() != 0)) {
            cursor.moveToFirst();
            // Узнаем индекс  столбца
            int idColumnIndex = cursor.getColumnIndex(Unit.COLUMN_UNIT_NAME);
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
        cv.put(FW.COLUMN_FW_FILE_ID,file_id);
        cv.put(FW.COLUMN_FW_WORK_ID,work_id);
        cv.put(FW.COLUMN_FW_TYPE_ID,type_id);
        cv.put(FW.COLUMN_FW_CATEGORY_ID,category_id);
        cv.put(FW.COLUMN_FW_COST,cost_work);
        cv.put(FW.COLUMN_FW_COUNT,count);
        cv.put(FW.COLUMN_FW_UNIT,unit);
        cv.put(FW.COLUMN_FW_SUMMA,summa);

        // вставляем строку
        long ID = db.insert(FW.TABLE_NAME, null, cv);
        // закрываем соединение с базой
        db.close();
        Log.d(TAG, "MyDatabaseHelper.insertRowInFW  FW._ID = " + ID);

        return ID;
    }

    //вывод в лог всех строк базы
    public void displayFW() {
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = this.getReadableDatabase();

        // Зададим условие для выборки - список столбцов
        String[] projection = {
                FW._ID,
                FW.COLUMN_FW_FILE_ID,
                FW.COLUMN_FW_WORK_ID,
                FW.COLUMN_FW_TYPE_ID,
                FW.COLUMN_FW_CATEGORY_ID,
                FW.COLUMN_FW_COST,
                FW.COLUMN_FW_COUNT,
                FW.COLUMN_FW_UNIT,
                FW.COLUMN_FW_SUMMA};

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
                        cursor.getColumnIndex(FW.COLUMN_FW_FILE_ID));
                int current_WORK_ID = cursor.getInt(
                        cursor.getColumnIndex(FW.COLUMN_FW_WORK_ID));
                int current_TYPE_ID = cursor.getInt(
                        cursor.getColumnIndex(FW.COLUMN_FW_TYPE_ID));
                int current_CATEGORY_ID = cursor.getInt(
                        cursor.getColumnIndex(FW.COLUMN_FW_CATEGORY_ID));
                float current_COST = cursor.getFloat(
                        cursor.getColumnIndex(FW.COLUMN_FW_COST));
                int current_COUNT = cursor.getInt(
                        cursor.getColumnIndex(FW.COLUMN_FW_COUNT));
                String current_UNIT = cursor.getString(
                        cursor.getColumnIndex(FW.COLUMN_FW_UNIT));
                float current_SUMMA = cursor.getFloat(
                        cursor.getColumnIndex(FW.COLUMN_FW_SUMMA));
                // Выводим построчно значения каждого столбца
                Log.d(TAG, "\n" + "ID = " + currentID + "/" +
                        " FILE_ID = " + current_FILE_ID + "/" +
                        " WORK_ID = " + current_WORK_ID + "/" +
                        " TYPE_ID = " + current_TYPE_ID + "/" +
                        " CAT_ID = " + current_CATEGORY_ID + "/" +
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
        updatedValues.put(CostWork.COLUMN_COST_COST, cost);
        db.update(CostWork.TABLE_NAME, updatedValues,
                CostWork.COLUMN_COST_WORK_ID + "=" + work_Id, null);
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
            file_name[position] = cursor.getString(cursor.getColumnIndex(FileWork.COLUMN_FILE_NAME));
            Log.i(TAG, "SmetaOpenHelper.getFileNames position = " + position);
        }
        if (cursor != null) {
            cursor.close();
        }
        return file_name;
    }

    //получаем все названия работ по смете с id файла file_id
    public String[]  getSmetaWork(long file_id){

        String select_work_name = " SELECT " + Work.COLUMN_WORK_NAME +
        " FROM " +  Work.TABLE_NAME  +
        " WHERE " + Work._ID  + " IN " +
                "(" + " SELECT " + FW.COLUMN_FW_WORK_ID + " FROM " + FW.TABLE_NAME +
                " WHERE " +  FW.COLUMN_FW_FILE_ID + " IN " +
                "(" +" SELECT " + FileWork._ID + " FROM " + FileWork.TABLE_NAME +
                " WHERE " + FileWork._ID + " = " + String.valueOf(file_id) + " )) ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select_work_name, null);
        Log.i(TAG, "TempDBHelper.getSmetaWork cursor.getCount()  " + cursor.getCount());
        String[] work_name = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            work_name[position] = cursor.getString(cursor.getColumnIndex(Work.COLUMN_WORK_NAME));
            Log.i(TAG, "SmetaOpenHelper.getSmetaWork work_name[position] = " + work_name[position]);
        }
        if (cursor != null) {
            cursor.close();
        }
        return work_name;
    }

}
