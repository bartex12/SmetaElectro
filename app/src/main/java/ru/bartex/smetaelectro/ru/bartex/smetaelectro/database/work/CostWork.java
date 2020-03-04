package ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.CostMat;

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
        String SQL_CREATE_TAB_COST = "CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COST_WORK_ID + " INTEGER NOT NULL, "
                + COST_UNIT_ID + " INTEGER NOT NULL, "
                + COST_COST + " REAL NOT NULL DEFAULT 0, "
                + COST_NUMBER + " INTEGER NOT NULL DEFAULT 1);";
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
        values.put(COST_WORK_ID, work_id);
        values.put(COST_UNIT_ID, unit_id);
        values.put(COST_COST, cost);
        db.insert(TABLE_NAME, null, values);
    }

    //получаем имя  по её id
    public static String getNameFromId(SQLiteDatabase db, long id) {
        Log.i(TAG, "TableControllerSmeta getNameFromId... ");

        String currentName = "";

        String name = " SELECT " + Unit.UNIT_NAME +
                " FROM " + Unit.TABLE_NAME +
                " WHERE " + Unit._ID + " IN " +
                "(" + " SELECT " + CostWork.COST_UNIT_ID +
                " FROM " + CostWork.TABLE_NAME +
                " WHERE " + CostWork.COST_WORK_ID + " = " + id + ")";
        Cursor cursor = db.rawQuery(name, null);

        if (cursor.moveToFirst()) {
            // Используем индекс для получения строки или числа
            currentName = cursor.getString(cursor.getColumnIndex(Unit.UNIT_NAME));
        }
        cursor.close();
        return currentName;
    }

    //удаляем цену на работу из таблицы CostWork по id цены на работу
    public static void deleteObject(SQLiteDatabase db, long id) {
        Log.i(TAG, "TableControllerSmeta.deleteObject case CostWork ");
        db.delete(TABLE_NAME, COST_WORK_ID + " =? ", new String[]{String.valueOf(id)});
    }

    //обновляем цену работы
    public static void updateCost(SQLiteDatabase db, long id, float cost, long unit_Id) {
        Log.i(TAG, "TableControllerSmeta.updateCost ...");
        //заполняем данные для обновления в базе
        ContentValues cv = new ContentValues();
        cv.put(COST_COST, cost);
        cv.put(COST_UNIT_ID, unit_Id);
        cv.put(COST_NUMBER, 1);
        db.update(TABLE_NAME, cv, COST_WORK_ID + "=" + id, null);
        Log.i(TAG, "SmetaOpenHelper.updateWorkCost - cost =" + cost);
    }

    //вывод в лог всех строк CostWork
    public static void displayTable(SQLiteDatabase db) {
        Log.i(TAG, "TableControllerSmeta.displayTable ...");
        // Создадим и откроем для чтения базу данных
        Cursor cursor = null;
        // Зададим условие для выборки - список столбцов
        String[] projectionCostWork = {_ID, COST_WORK_ID, COST_UNIT_ID, COST_COST, COST_NUMBER};
        // Делаем запрос
        cursor = db.query(
                TABLE_NAME,   // таблица
                projectionCostWork,            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        // Проходим через все ряды в таблице CostWork
        while (cursor.moveToNext()) {
            // Используем индекс для получения строки или числа
            int currentID = cursor.getInt(cursor.getColumnIndex(_ID));
            int current_WORK_ID = cursor.getInt(cursor.getColumnIndex(COST_WORK_ID));
            String current_UNIT = cursor.getString(cursor.getColumnIndex(COST_UNIT_ID));
            float current_COST = cursor.getFloat(cursor.getColumnIndex(COST_COST));
            int current_NUMBER = cursor.getInt(cursor.getColumnIndex(COST_NUMBER));

            // Выводим построчно значения каждого столбца
            Log.d(TAG, "\n" + "ID = " + currentID + " - " +
                    " WORK_ID = " + current_WORK_ID + " - " +
                    " UNIT = " + current_UNIT + " - " +
                    " COST = " + current_COST + " - " +
                    " NUMBER = " + current_NUMBER);
        }
        cursor.close();
    }

    //получаем стоимость работы по  id
    public static float getCostById(SQLiteDatabase db, long id) {
        Log.i(TAG, "TableControllerSmeta.getCostById ... ");
        float cost = -1;

        String select = " SELECT " + COST_COST +
                " FROM " + TABLE_NAME +
                " WHERE " + COST_WORK_ID + " = ?";

        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(id)});
        Log.d(TAG, "getCostById cursor.getCount() = " + cursor.getCount());

        if (cursor.moveToFirst()) {
            // Используем индекс для получения строки или числа
            cost = cursor.getFloat(cursor.getColumnIndex(COST_COST));
        }
        cursor.close();
        return cost;
    }

    //Добавляем вид работы с левыми параметрами, чобы удалить при отказе пользователя
    public static long  insertZero(SQLiteDatabase db, long id){
        Log.i(TAG, "TableControllerSmeta.insertZero ... ");

        ContentValues cv = new ContentValues();
        cv.put(COST_WORK_ID,id);
        cv.put(COST_UNIT_ID, 1);
        cv.put(COST_COST,0);
        cv.put(COST_NUMBER,1);
        // вставляем строку
        long currentId = db.insert(TABLE_NAME, null, cv);

        Log.d(TAG, "TableControllerSmeta.insertZero  currentId = " + currentId);
        return currentId;
    }

    //Добавляем цену работы
    public static long  insertCost(SQLiteDatabase db, long Id, float cost, long unit_id){
        Log.i(TAG, "TableControllerSmeta.insertCost ... ");
        long costId =-1;

        ContentValues cv = new ContentValues();
        cv.put(COST_WORK_ID, Id);
        cv.put(COST_COST,cost);
        cv.put(COST_UNIT_ID,unit_id);
        // вставляем строку
        costId = db.insert(TABLE_NAME, null, cv);

        Log.d(TAG, "TableControllerSmeta.insertCost costId = " + costId);
        return costId;
    }


}
