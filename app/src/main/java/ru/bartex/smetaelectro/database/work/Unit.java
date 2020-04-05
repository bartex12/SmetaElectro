package ru.bartex.smetaelectro.database.work;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import ru.bartex.smetaelectro.R;


public class Unit {
    public static final String TAG = "33333";

    public Unit(){
        //пустой конструктор
    }
    public final static String TABLE_NAME = "Unit";

    public final static String _ID = BaseColumns._ID;
    public final static String UNIT_NAME = "UnitName";

    //создание таблицы
    public static void createTable(SQLiteDatabase db, Context fContext){
        // Строка для создания таблицы единиц измерения Unit для работ
        String SQL_CREATE_TAB_UNIT = "CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UNIT_NAME + " TEXT NOT NULL);";
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TAB_UNIT);
        Log.d(TAG, "Unit - onCreate- создание таблицы Unit");
        // Если файлов в базе нет, вносим записи единиц измерения
        createDefaultUnit(db,fContext );
    }

    private static void createDefaultUnit(SQLiteDatabase db, Context fContext){
        Log.i(TAG, "Unit.createDefaultUnit...");
        ContentValues values = new ContentValues();
        // Получим массив строк из ресурсов
        Resources res = fContext.getResources();
        String[] unit_name = res.getStringArray(R.array.unit_name);
        // проходим через массив и вставляем записи в таблицу
        for (String s : unit_name) {
            values.put(UNIT_NAME, s);
            // Добавляем записи в таблицу
            db.insert(TABLE_NAME, null, values);
        }
        Log.d(TAG, "Unit createDefaultUnit unit_name.length = " + unit_name.length );
    }

    //получаем id по имени
    public static long getIdFromName(SQLiteDatabase db, String name) {
        Log.i(TAG, "Unit.getIdFromName ... ");
        long currentID = -1;
        Cursor cursor = db.query(
                TABLE_NAME,                     // таблица
                new String[]{_ID},            // столбцы
                UNIT_NAME + "=?",    // столбцы для условия WHERE
                new String[]{name},                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        if (cursor.moveToFirst()) {
            // получаем id по индексу
            currentID = cursor.getLong(cursor.getColumnIndex(_ID));
        }
        cursor.close();
        return currentID;
    }

    //получаем массив единиц измерения
    public static String[] getArrayUnits(SQLiteDatabase db) {
        Log.i(TAG, "Unit.getArrayUnits ... ");

        String  select  = " SELECT " + UNIT_NAME  + " FROM " + TABLE_NAME;
        Cursor  cursor = db.rawQuery(select, null);

        String[]  units_name = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            units_name[position] = cursor.getString(cursor.getColumnIndex(UNIT_NAME));
        }
        cursor.close();
        return units_name;
    }
}