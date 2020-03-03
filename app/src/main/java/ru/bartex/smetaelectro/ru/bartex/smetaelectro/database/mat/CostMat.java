package ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import ru.bartex.smetaelectro.R;

public class CostMat {
    public static final String TAG = "33333";

    public CostMat(){
        //пустой конструктор
    }
    public final static String TABLE_NAME = "CostMat";

    public final static String _ID = BaseColumns._ID;
    public final static String COST_MAT_ID = "COST_MAT_ID";
    public final static String COST_MAT_UNIT_ID = "COST_MAT_UNIT_ID";
    public final static String COST_MAT_COST = "COST_MAT_COST";
    public final static String COST_MAT_NUMBER = "COST_MAT_NUMBER";

    //создание таблицы
    public static void createTable(SQLiteDatabase db, Context fContext){
        // Строка для создания таблицы расценок CostMat
        String SQL_CREATE_TAB_COST_MAT = "CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COST_MAT_ID + " INTEGER NOT NULL, "
                + COST_MAT_UNIT_ID + " INTEGER NOT NULL, "
                + COST_MAT_COST + " REAL NOT NULL DEFAULT 0, "
                + COST_MAT_NUMBER + " INTEGER NOT NULL DEFAULT 1);";
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TAB_COST_MAT);
        Log.d(TAG, "SmetaOpenHelper - onCreate- создание таблицы CostMat");
        // Если файлов в базе нет, вносим записи расценок
        createDefaultCostMat(db, fContext);
    }

    private static void createDefaultCostMat(SQLiteDatabase db, Context fContext){
        Log.i(TAG, "SmetaOpenHelper.createDefaultCostMat...");
        ContentValues values = new ContentValues();

        // Получим ресурс
        Resources res = fContext.getResources();

        int [] mat_id = getIdFromMats(db);
        int length = mat_id.length;
        // Получим массив строк из ресурсов
        String[] unitIdOfMat = res.getStringArray(R.array.unit_id_of_mat);
        int[] unit_id =new int[unitIdOfMat.length];
        for (int i = 0; i<length; i++){
            unit_id[i] = Integer.valueOf(unitIdOfMat[i]);
        }

        String[] costMat = res.getStringArray(R.array.cost_of_mat);
        float[] cost_of_mat = new float[costMat.length];
        for (int i = 0; i < length; i++){
            cost_of_mat[i] = Float.valueOf(costMat[i]);
        }

        // проходим через массив и вставляем записи в таблицу
        for (int i = 0; i < length; i++){
            // Добавляем записи в таблицу
            InsertCostMat(db, values, mat_id[i], unit_id[i], cost_of_mat[i]);
        }
        Log.d(TAG, "createDefaultCost unit_id.length = " +
                unit_id.length + "  cost_of_mat.length" + cost_of_mat.length);
    }

    //получаем  все ID таблицы Mat ***
    private static int[] getIdFromMats(SQLiteDatabase db) {

        Log.d(TAG, "getIdFromMats...");
        Cursor cursor = db.query(
                Mat.TABLE_NAME,   // таблица
                new String[]{Mat._ID},            // столбцы
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
                int idColumnIndex = cursor.getColumnIndex(Mat._ID);
                // Используем индекс для получения строки или числа
                currentID[cursor.getPosition()] = cursor.getInt(idColumnIndex);
            }
        }
        cursor.close();
        if (currentID != null) {
            Log.d(TAG, "getIdFromMats currentID.length = " + currentID.length);
        }
        return currentID;
    }

    private static void InsertCostMat(SQLiteDatabase db, ContentValues values,
                               long work_id, long unit_id, float cost){
        values.put(COST_MAT_ID, work_id);
        values.put(COST_MAT_UNIT_ID, unit_id);
        values.put(COST_MAT_COST, cost);
        db.insert(TABLE_NAME, null, values);
    }

    //удаляем цену на материал из таблицы CostMat по id цены на материал
    public static void deleteObject(SQLiteDatabase db, long id) {
        Log.i(TAG, "TableControllerSmeta.deleteObject ... ");
        db.delete(TABLE_NAME, COST_MAT_ID + " =? ", new String[]{String.valueOf(id)});
    }

    //обновляем цену материалов
    public static void updateCost(SQLiteDatabase db, long id, float cost, long unit_Id) {
        Log.i(TAG, "TableControllerSmeta.updateCost ...");
        //заполняем данные для обновления в базе
        ContentValues cv = new ContentValues();
        cv.put(COST_MAT_COST, cost);
        cv.put(COST_MAT_UNIT_ID, unit_Id);
        cv.put(COST_MAT_NUMBER, 1);
        db.update(TABLE_NAME, cv, COST_MAT_ID + "=" + id, null);
        Log.i(TAG, "SmetaOpenHelper.updateWorkCost - cost =" + cost);
    }

    //вывод в лог всех строк CostMat
    public void displayTable(SQLiteDatabase db) {
        Log.i(TAG, "TableControllerSmeta.displayTable ...");
        // Создадим и откроем для чтения базу данных
        Cursor cursor = null;
        // Зададим условие для выборки - список столбцов
        String[] projectionCostMat = {
                _ID,
                COST_MAT_ID,
                COST_MAT_UNIT_ID,
                COST_MAT_COST,
                COST_MAT_NUMBER};
        // Делаем запрос
        cursor = db.query(
                TABLE_NAME,   // таблица
                projectionCostMat,            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки
        // Проходим через все ряды в таблице CostWork
        while (cursor.moveToNext()) {
            // Используем индекс для получения строки или числа
            int currentID = cursor.getInt(
                    cursor.getColumnIndex(_ID));
            int current_WORK_ID = cursor.getInt(
                    cursor.getColumnIndex(COST_MAT_ID));
            String current_UNIT = cursor.getString(
                    cursor.getColumnIndex(COST_MAT_UNIT_ID));
            float current_COST = cursor.getFloat(
                    cursor.getColumnIndex(COST_MAT_COST));
            int current_NUMBER = cursor.getInt(
                    cursor.getColumnIndex(COST_MAT_NUMBER));
            // Выводим построчно значения каждого столбца
            Log.d(TAG, "\n" + "ID = " + currentID + " - " +
                    " MAT_ID = " + current_WORK_ID + " - " +
                    " UNIT = " + current_UNIT + " - " +
                    " MAT_COST = " + current_COST + " - " +
                    " MAT_NUMBER = " + current_NUMBER);
        }
        cursor.close();
    }
}
