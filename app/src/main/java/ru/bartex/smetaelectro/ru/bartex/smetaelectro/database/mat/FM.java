package ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class FM {
    public static final String TAG = "33333";

    public FM(){
        //пустой конструктор
    }
    public final static String TABLE_NAME = "File_And_Materials";

    public final static String _ID = BaseColumns._ID;
    public final static String FM_FILE_ID = "FM_FILE_ID";
    public final static String FM_FILE_NAME = "FM_FILE_NAME";
    public final static String FM_MAT_ID = "FM_MAT_ID";
    public final static String FM_MAT_NAME = "FM_MAT_NAME";
    public final static String FM_MAT_TYPE_ID = "FM_MAT_TYPE_ID";
    public final static String FM_MAT_TYPE_NAME = "FM_MAT_TYPE_NAME";
    public final static String FM_MAT_CATEGORY_ID = "FM_MAT_CATEGORY_ID";
    public final static String FM_MAT_CATEGORY_NAME = "FM_MAT_CATEGORY_NAME";
    public final static String FM_MAT_COST = "FM_MAT_COST";
    public final static String FM_MAT_COUNT = "FM_MAT_COUNT";
    public final static String FM_MAT_UNIT = "FM_MAT_UNIT";
    public final static String FM_MAT_SUMMA = "FM_MAT_SUMMA";

    //создание таблицы
    public static void createTable(SQLiteDatabase db){
        // Строка для создания основной таблицы базы по материалам, записи добавляются только программно
        String SQL_CREATE_TAB_FILE_AND_MATERIALS  = "CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FM_FILE_ID+ " INTEGER NOT NULL, "
                + FM_FILE_NAME + " TEXT NOT NULL, "
                + FM_MAT_ID + " INTEGER NOT NULL, "
                + FM_MAT_NAME + " TEXT NOT NULL, "
                + FM_MAT_TYPE_ID + " INTEGER NOT NULL, "
                + FM_MAT_TYPE_NAME + " TEXT NOT NULL, "
                + FM_MAT_CATEGORY_ID + " INTEGER NOT NULL, "
                + FM_MAT_CATEGORY_NAME + " TEXT NOT NULL, "
                + FM_MAT_COST + " REAL NOT NULL, "
                + FM_MAT_COUNT + " INTEGER NOT NULL, "
                + FM_MAT_UNIT + " TEXT NOT NULL, "
                + FM_MAT_SUMMA + " REAL NOT NULL);";
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TAB_FILE_AND_MATERIALS);
        Log.d(TAG, "SmetaOpenHelper - onCreate- создание таблицы FM");
    }
    //обновление таблицы
    static void onUpgrade(SQLiteDatabase database) {
        //обновлять пока не собираюсь
    }

    //получаем id категории  материала из FM
    public static long getCatId_FM(SQLiteDatabase db, long file_id, long id) {
        Log.i(TAG, "TableControllerSmeta.getCateIdFWFM ... ");

        long cat_id = -1;

        String select = " SELECT " + FM_MAT_CATEGORY_ID + " FROM " + TABLE_NAME +
                " where " + FM_FILE_ID + " =? " + " and " + FM_MAT_ID + " =? ";
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(file_id), String.valueOf(id)});

        if (cursor.moveToFirst()) {
            // Узнаем индекс столбца и Используем индекс для получения количества работы
            cat_id = cursor.getLong(cursor.getColumnIndex(FM_MAT_CATEGORY_ID));
        }
        cursor.close();
        return cat_id;
    }


}
