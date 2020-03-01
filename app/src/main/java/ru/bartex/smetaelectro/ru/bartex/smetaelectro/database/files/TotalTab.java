package ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.files;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class TotalTab {
    public static final String TAG = "33333";

    public TotalTab(){
        //пустой конструктор
    }

    public final static String TABLE_NAME = "TotalTab";

    public final static String _ID = BaseColumns._ID;
    public final static String TOTAL_FILE_ID = "Total_File_Id";
    public final static String TOTAL_CATEGORY_ID = "Total_Category_Id";
    public final static String TOTAL_TYPE_ID = "Total_TYpe_Id";
    public final static String TOTAL_WORK_ID= "Total_Work_Id";
    public final static String TOTAL_COST= "Total_Cost";
    public final static String TOTAL_COST_NUMBER= "Total_Cost_Number";
    public final static String TOTAL_COST_UNIT= "Total_Cost_Unit";
    public final static String TOTAL_SUMMA= "TotalSumma";

    //создание таблицы
    public static void createTable(SQLiteDatabase db){
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
    }
}
