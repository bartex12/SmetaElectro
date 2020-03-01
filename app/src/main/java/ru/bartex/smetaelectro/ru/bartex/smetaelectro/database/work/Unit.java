package ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
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
        Log.d(TAG, "SmetaOpenHelper - onCreate- создание таблицы Unit");
        // Если файлов в базе нет, вносим записи единиц измерения
        createDefaultUnit(db,fContext );
    }

    private static void createDefaultUnit(SQLiteDatabase db, Context fContext){
        Log.i(TAG, "SmetaOpenHelper.createDefaultUnit...");
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
        Log.d(TAG, "createDefaultUnit unit_name.length = " + unit_name.length );
    }

}
