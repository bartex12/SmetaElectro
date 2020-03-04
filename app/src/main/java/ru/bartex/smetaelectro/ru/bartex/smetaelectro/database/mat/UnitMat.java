package ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
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
        String SQL_CREATE_TAB_UNIT_MAT = "CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UNIT_MAT_NAME + " TEXT NOT NULL);";
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
            values.put(UNIT_MAT_NAME, s);
            // Добавляем записи в таблицу
            db.insert(TABLE_NAME, null, values);
        }
        Log.d(TAG, "createDefaultUnitMat unit_name.length = " + unit_name.length );
    }

    //получаем id по имени
    public static long getIdFromName(SQLiteDatabase db, String name) {
        Log.i(TAG, "TableControllerSmeta.getIdFromName ... ");
        long currentID = -1;
        Cursor cursor = db.query(
                TABLE_NAME,                     // таблица
                new String[]{_ID},            // столбцы
                UNIT_MAT_NAME + "=?",    // столбцы для условия WHERE
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
        Log.i(TAG, "TableControllerSmeta.getArrayUnits ... ");

        String  select  = " SELECT " + UNIT_MAT_NAME  + " FROM " + TABLE_NAME;
        Cursor  cursor = db.rawQuery(select, null);

        String[]  units_name = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            units_name[position] = cursor.getString(cursor.getColumnIndex(UNIT_MAT_NAME));
        }
        cursor.close();
        return units_name;
    }

    //получаем единицы измерения материала с помощью вложенного запроса
    public static String getUnitMat(SQLiteDatabase db, long mat_id){
        Log.i(TAG, "TableControllerSmeta.getUnitMat ... ");
        String unitMatName = "";
        String unit = " SELECT " +  UNIT_MAT_NAME +
                " FROM " + TABLE_NAME  +
                " WHERE " + _ID + " IN " +
                "(" + " SELECT " + CostMat.COST_MAT_UNIT_ID +
                " FROM " + CostMat.TABLE_NAME +
                " WHERE " + CostMat.COST_MAT_ID + " = " + mat_id + ")";

        Cursor cursor = db.rawQuery(unit, null);

        Log.d(TAG, "TableControllerSmeta getUnitMat cursor.getCount() = " + cursor.getCount());

        if (cursor.moveToFirst()) {
            // Используем индекс для получения строки или числа
            unitMatName = cursor.getString(cursor.getColumnIndex(UNIT_MAT_NAME));
            Log.d(TAG, "TableControllerSmeta getUnitMat unitName = " + unitMatName);
        }
        cursor.close();
        return unitMatName;
    }

}
