package ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work;

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
        String SQL_CREATE_TAB_FILE_AND_WORK  = "CREATE TABLE " + FW.TABLE_NAME + " ("
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

}
