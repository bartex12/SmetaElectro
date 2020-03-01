package ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import ru.bartex.smetaelectro.R;

public class CostWork {
    public static final String TAG = "33333";

    public CostWork(){
        //пустой конструктор
    }
    public final static String TABLE_NAME = "Cost";

    public final static String _ID = BaseColumns._ID;
    public final static String COST_WORK_ID = "Cost_Work_ID";
    public final static String COST_UNIT_ID = "Cost_Unit_ID";
    public final static String COST_COST = "Cost_Cost";
    public final static String COST_NUMBER = "Cost_Number";

    //создание таблицы
    public static void createTable(SQLiteDatabase db, Context fContext){
        // Строка для создания таблицы расценок CostWork
        String SQL_CREATE_TAB_COST = "CREATE TABLE " + CostWork.TABLE_NAME + " ("
                + CostWork._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CostWork.COST_WORK_ID + " INTEGER NOT NULL, "
                + CostWork.COST_UNIT_ID + " INTEGER NOT NULL, "
                + CostWork.COST_COST + " REAL NOT NULL DEFAULT 0, "
                + CostWork.COST_NUMBER + " INTEGER NOT NULL DEFAULT 1);";
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TAB_COST);
        Log.d(TAG, "SmetaOpenHelper - onCreate- создание таблицы CostWork");
        // Если файлов в базе нет, вносим записи расценок
        createDefaultCost(db, fContext);
    }

    private static void createDefaultCost(SQLiteDatabase db, Context fContext){
        Log.i(TAG, "SmetaOpenHelper.createDefaultCost...");
        ContentValues values = new ContentValues();
        // Получим массив строк из ресурсов
        Resources res = fContext.getResources();

        int [] work_id = getIdFromWorks(db);
        int length = work_id.length;

        String[] unitIdOfWork = res.getStringArray(R.array.unit_id_of_work);
        int[] unit_id =new int[unitIdOfWork.length];
        for (int i = 0; i<length; i++){
            unit_id[i] = Integer.valueOf(unitIdOfWork[i]);
        }
        String[] cost = res.getStringArray(R.array.cost_of_work);
        float[] cost_of_work = new float[cost.length];
        for (int i = 0; i < length; i++){
            cost_of_work[i] = Float.valueOf(cost[i]);
        }
        // проходим через массив и вставляем записи в таблицу
        for (int i = 0; i < length; i++){
            // Добавляем записи в таблицу
            InsertCost(db, values, work_id[i], unit_id[i], cost_of_work[i]);
        }
        Log.d(TAG, "createDefaultCost unit_id.length = " + unit_id.length );
    }

    //получаем  все ID таблицы Work ***
    private static int[] getIdFromWorks(SQLiteDatabase db) {
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
        if (cursor.getCount() != 0) {
            currentID = new int[cursor.getCount()];
            while (cursor.moveToNext()) {
                // Узнаем индекс каждого столбца
                int idColumnIndex = cursor.getColumnIndex(Work._ID);
                // Используем индекс для получения строки или числа
                currentID[cursor.getPosition()] = cursor.getInt(idColumnIndex);
            }
        }
        cursor.close();
        if (currentID != null) {
            Log.d(TAG, "getIdFromWorks currentID.length = " +currentID.length);
        }
        return currentID;
    }

    private static void InsertCost(SQLiteDatabase db, ContentValues values,
                            long work_id, long unit_id, float cost){
        values.put(CostWork.COST_WORK_ID, work_id);
        values.put(CostWork.COST_UNIT_ID, unit_id);
        values.put(CostWork.COST_COST, cost);
        db.insert(CostWork.TABLE_NAME, null, values);
    }
}
