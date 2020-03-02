package ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import ru.bartex.smetaelectro.R;

public class Work {
    public static final String TAG = "33333";

    public Work(){
        //пустой конструктор
    }
    public final static String TABLE_NAME = "Work";

    public final static String _ID = BaseColumns._ID;
    public final static String WORK_NAME = "Work_Name";
    public final static String WORK_DESCRIPTION= "Work_Description";
    public final static String WORK_TYPE_ID = "Work_Type_ID";

    //создание таблицы
    public static void createTable(SQLiteDatabase db, Context fContext){
        // Строка для создания таблицы конкретных работ Work
        String SQL_CREATE_TAB_WORK = "CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WORK_TYPE_ID + " INTEGER NOT NULL, "
                + WORK_NAME + " TEXT NOT NULL, "
                + WORK_DESCRIPTION + " TEXT NOT NULL DEFAULT 'Без описания');";
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TAB_WORK);
        Log.d(TAG, "SmetaOpenHelper - onCreate- создание таблицы Work");
        // Если файлов в базе нет, вносим записи названия  работ (добавление из программы)
        createDefaultWork(db, fContext);
    }

    //создаём работы из ресурсов и получаем COLUMN_TYPE_CATEGORY_ID из  this.getArrayCategoryId()
    private static void createDefaultWork(SQLiteDatabase db, Context fContext) {
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
        int[] type_id = getArrayTypeId(db);
        // проходим через массив и вставляем записи в таблицу
        int ii = 0; //work_name_type_demontag
        for (String s : work_name_type_demontag) {
            InsertWork(db, values, type_id[ii], s);
        }
        ii = 1; //work_name_type_podgotovka
        for (String s : work_name_type_podgotovka) {
            InsertWork(db, values, type_id[ii], s);
        }
        ii = 2; //work_name_type_shtroby_otverstia
        for (String s : work_name_type_shtroby_otverstia) {
           InsertWork(db, values, type_id[ii], s);
        }
        ii = 3; //work_name_type_kabel_kabelkanaly
        for (String s : work_name_type_kabel_kabelkanaly) {
            InsertWork(db, values, type_id[ii], s);
        }
        ii = 4; //work_name_type_korobki
        for (String s : work_name_type_korobki) {
            InsertWork(db, values, type_id[ii], s);
        }
        ii = 5; //work_name_type_sverlenie_otverstii
        for (String s : work_name_type_sverlenie_otverstii) {
            InsertWork(db, values, type_id[ii], s);
        }
        ii = 6; //work_name_type_shchit
        for (String s : work_name_type_shchit) {
            InsertWork(db, values, type_id[ii], s);
        }
        ii = 7; //work_name_type_set_rozetki
        for (String s : work_name_type_set_rozetki) {
           InsertWork(db, values, type_id[ii], s);
        }
        ii = 8; //work_name_type_set_svetilniki
        for (String s : work_name_type_set_svetilniki) {
            InsertWork(db, values, type_id[ii], s);
        }
        ii = 9; //work_name_type_LED
        for (String s : work_name_type_LED) {
            InsertWork(db, values, type_id[ii], s);
        }
        ii = 10; //work_name_type_oborudovanie
        for (String s : work_name_type_oborudovanie) {
            InsertWork(db, values, type_id[ii], s);
        }
        ii = 11; //work_name_type_soputstv
        for (String s : work_name_type_soputstv) {
            InsertWork(db, values, type_id[ii], s);
        }
        ii = 12; //work_name_type_primechania
        for (String s : work_name_type_primechania) {
            InsertWork(db, values, type_id[ii], s);
        }
        ii = 13; //work_name_type_musor
        for (String s : work_name_type_musor) {
           InsertWork(db, values, type_id[ii], s);
        }
        ii = 14; //work_name_type_tovary
        for (String s : work_name_type_tovary) {
            InsertWork(db, values, type_id[ii], s);
        }
        Log.d(TAG, "createDefaultWork type_id.length = " +
                type_id.length + " work_name_type_demontag.length = " +work_name_type_demontag.length);
    }

    //получаем курсор с Work_Type_id  и делаем массив id
    private static int[] getArrayTypeId(SQLiteDatabase db) {
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

    private static void InsertWork(SQLiteDatabase db, ContentValues values,
                            int type_id, String work_name){
        values.put(WORK_TYPE_ID, type_id);
        values.put(WORK_NAME, work_name);
        db.insert(TABLE_NAME, null, values);
    }
}