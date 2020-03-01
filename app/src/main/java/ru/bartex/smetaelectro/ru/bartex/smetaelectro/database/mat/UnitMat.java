package ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import ru.bartex.smetaelectro.R;

public class UnitMat {
    public static final String TAG = "33333";

    public UnitMat(){
        //пустой конструктор
    }
    public final static String TABLE_NAME = "UnitMat";

    public final static String _ID = BaseColumns._ID;
    public final static String UNIT_MAT_NAME = "UNIT_MAT_NAME";

    //создание таблицы
    public static void createTable(SQLiteDatabase db, Context fContext){
        // Строка для создания таблицы единиц измерения UnitMat для материалов
        String SQL_CREATE_TAB_UNIT_MAT = "CREATE TABLE " + UnitMat.TABLE_NAME + " ("
                + UnitMat._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UnitMat.UNIT_MAT_NAME + " TEXT NOT NULL);";
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TAB_UNIT_MAT);
        Log.d(TAG, "SmetaOpenHelper - onCreate- создание таблицы UnitMat");
        // Если файлов в базе нет, вносим записи единиц измерения
        createDefaultUnitMat(db, fContext);
    }

    private static void createDefaultUnitMat(SQLiteDatabase db, Context fContext){
        Log.i(TAG, "SmetaOpenHelper.createDefaultUnitMat...");
        ContentValues values = new ContentValues();
        // Получим массив строк из ресурсов
        Resources res = fContext.getResources();
        String[] unit_name = res.getStringArray(R.array.unit_name);
        // проходим через массив и вставляем записи в таблицу
        for (String s : unit_name) {
            values.put(UnitMat.UNIT_MAT_NAME, s);
            // Добавляем записи в таблицу
            db.insert(UnitMat.TABLE_NAME, null, values);
        }
        Log.d(TAG, "createDefaultUnitMat unit_name.length = " + unit_name.length );
    }
}
