package ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class FW {
    public static final String TAG = "33333";

    public FW(){
        //пустой конструктор
    }
    public final static String TABLE_NAME = "File_And_Work";

    public final static String _ID = BaseColumns._ID;
    public final static String FW_FILE_ID = "FW_File_ID";
    public final static String FW_FILE_NAME = "FW_File_Name";
    public final static String FW_WORK_ID = "FW_Work_ID";
    public final static String FW_WORK_NAME = "FW_Work_Name";
    public final static String FW_TYPE_ID = "FW_Type_ID";
    public final static String FW_TYPE_NAME = "FW_Type_Name";
    public final static String FW_CATEGORY_ID = "FW_Category_ID";
    public final static String FW_CATEGORY_NAME = "FW_Category_Name";
    public final static String FW_COST = "FW_Cost";
    public final static String FW_COUNT = "FW_Count";
    public final static String FW_UNIT = "FW_Unit";
    public final static String FW_SUMMA = "FW_Summa";

    //создание таблицы
    public static void createTable(SQLiteDatabase db){
    // Строка для создания основной таблицы базы по работе, записи добавляются только программно
        String SQL_CREATE_TAB_FILE_AND_WORK  = "CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FW_FILE_ID + " INTEGER NOT NULL, "
                + FW_FILE_NAME + " TEXT NOT NULL, "
                + FW_WORK_ID + " INTEGER NOT NULL, "
                + FW_WORK_NAME + " TEXT NOT NULL, "
                + FW_TYPE_ID + " INTEGER NOT NULL, "
                + FW_TYPE_NAME + " TEXT NOT NULL, "
                + FW_CATEGORY_ID + " INTEGER NOT NULL, "
                + FW_CATEGORY_NAME + " TEXT NOT NULL, "
                + FW_COST + " REAL NOT NULL, "
                + FW_COUNT + " INTEGER NOT NULL, "
                + FW_UNIT + " TEXT NOT NULL, "
                + FW_SUMMA + " REAL NOT NULL);";
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TAB_FILE_AND_WORK);
        Log.d(TAG, "SmetaOpenHelper - onCreate- создание таблицы FW");
    }

    //обновление таблицы
    static void onUpgrade(SQLiteDatabase database) {
        //обновлять пока не собираюсь
    }

    //получаем id категории  работы из FW
    public static long getCatId_FW(SQLiteDatabase db, long file_id, long id) {
        Log.i(TAG, "TableControllerSmeta.getCateIdFWFM ... ");

        long cat_id = -1;

        String select = " SELECT " + FW_CATEGORY_ID + " FROM " + TABLE_NAME +
                " where " + FW_FILE_ID + " =? " + " and " + FW_WORK_ID + " =? ";
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(file_id), String.valueOf(id)});

        if (cursor.moveToFirst()) {
            // Узнаем индекс столбца и Используем индекс для получения количества работы
            cat_id = cursor.getLong(cursor.getColumnIndex(FW_CATEGORY_ID));
        }
        cursor.close();
        return cat_id;
    }

    //получаем id типа  работы из FW
    public static long getTypeId_FW(SQLiteDatabase db, long file_id, long id) {
        Log.i(TAG, "TableControllerSmeta.getTypeIdFWFM ... ");

        long type_id = -1;

        String select = " SELECT " + FW_TYPE_ID + " FROM " + TABLE_NAME +
                " where " + FW_FILE_ID + " =? " + " and " + FW_WORK_ID + " =? ";
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(file_id), String.valueOf(id)});

        if (cursor.moveToFirst()) {
            // Узнаем индекс столбца и Используем индекс для получения id типа работы
            type_id = cursor.getLong(cursor.getColumnIndex(FW_TYPE_ID));
        }
        cursor.close();
        return type_id;
    }

}
