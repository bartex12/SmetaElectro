package ru.bartex.smetaelectro.database.work;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import ru.bartex.smetaelectro.data.DataFW;
import ru.bartex.smetaelectro.database.files.FileWork;

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
        Log.d(TAG, "FW - onCreate- создание таблицы FW");
    }

    //обновление таблицы
    static void onUpgrade(SQLiteDatabase database) {
        //обновлять пока не собираюсь
    }

    //получаем id категории  работы из FW
    public static long getCatId_FW(SQLiteDatabase db, long file_id, long id) {
        Log.i(TAG, "FW.getCateIdFWFM ... ");

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
        Log.i(TAG, "FW.getTypeIdFWFM ... ");

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

    //удаляем работу из сметы FW по file_id и work_id
    public static void deleteItemFrom_FW(SQLiteDatabase db, long file_id, long id) {
        Log.i(TAG, "FW.deleteItemFrom_FW ... ");
        db.delete(TABLE_NAME, FW_FILE_ID + " =? " + " AND " + FW_WORK_ID + " =? ",
                new String[]{String.valueOf(file_id), String.valueOf(id)});
    }

    //получаем имена   по смете с id файла file_id
    public static String[] getArrayNames(SQLiteDatabase db, long file_id) {
        Log.i(TAG, "FW.getArrayNames ... ");

        String select = " SELECT " + FW_WORK_NAME +
                " FROM " + TABLE_NAME +
                " WHERE " + FW_FILE_ID + " = " + file_id;

        Cursor cursor = db.rawQuery(select, null);
        Log.i(TAG, "FW.getArrayNames cursor.getCount()  " + cursor.getCount());

        String[] name = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            name[position] = cursor.getString(cursor.getColumnIndex(FW_WORK_NAME));
        }
        cursor.close();
        return name;
    }

    //получаем имена   по смете с id файла file_id и id типа type_id
    public static String[] getArrayNamesSelectedType(SQLiteDatabase db, long file_id, long type_id) {
        Log.i(TAG, "FW.getArrayNamesSelectedType ... ");

        String select = " SELECT " + FW_WORK_NAME +
                " FROM " + TABLE_NAME +
                " WHERE " + FW_FILE_ID + " = " + file_id +
                " AND " + FW_TYPE_ID + " = " + type_id;

        Cursor cursor = db.rawQuery(select, null);
        Log.i(TAG, "FW.getArrayNamesSelectedType cursor.getCount()  " + cursor.getCount());

        String[] name = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            name[position] = cursor.getString(cursor.getColumnIndex(FW_WORK_NAME));
        }
        cursor.close();
        return name;
    }

    //вывод в лог всех строк FW
    public void displayTable(SQLiteDatabase db) {
        Log.i(TAG, "FW.displayTable ...");
        // Создадим и откроем для чтения базу данных
        Cursor cursor = null;
        // Зададим условие для выборки - список столбцов
        String[] projectionFW = {
                _ID,
                FW_FILE_ID,
                FW_FILE_NAME,
                FW_WORK_ID,
                FW_WORK_NAME,
                FW_TYPE_ID,
                FW_TYPE_NAME,
                FW_CATEGORY_ID,
                FW_CATEGORY_NAME,
                FW_COST,
                FW_COUNT,
                FW_UNIT,
                FW_SUMMA};
        // Делаем запрос
        cursor = db.query(
                TABLE_NAME,   // таблица
                projectionFW,            // столбцы
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
            int current_FILE_ID = cursor.getInt(
                    cursor.getColumnIndex(FW_FILE_ID));
            String current_FILE_NAME = cursor.getString(
                    cursor.getColumnIndex(FW_FILE_NAME));
            int current_WORK_ID = cursor.getInt(
                    cursor.getColumnIndex(FW_WORK_ID));
            String current_WORK_NAME = cursor.getString(
                    cursor.getColumnIndex(FW_WORK_NAME));
            int current_TYPE_ID = cursor.getInt(
                    cursor.getColumnIndex(FW_TYPE_ID));
            String current_TYPE_NAME = cursor.getString(
                    cursor.getColumnIndex(FW_TYPE_NAME));
            int current_CATEGORY_ID = cursor.getInt(
                    cursor.getColumnIndex(FW_CATEGORY_ID));
            String current_CATEGORY_NAME = cursor.getString(
                    cursor.getColumnIndex(FW_CATEGORY_NAME));
            float current_COST = cursor.getFloat(
                    cursor.getColumnIndex(FW_COST));
            int current_COUNT = cursor.getInt(
                    cursor.getColumnIndex(FW_COUNT));
            String current_UNIT = cursor.getString(
                    cursor.getColumnIndex(FW_UNIT));
            float current_SUMMA = cursor.getFloat(
                    cursor.getColumnIndex(FW_SUMMA));
            // Выводим построчно значения каждого столбца
            Log.d(TAG, "\n" + "ID = " + currentID + "/" +
                    " FILE_ID = " + current_FILE_ID + "/" +
                    " FILE_NAME = " + current_FILE_NAME + "/" +
                    " WORK_ID = " + current_WORK_ID + "/" +
                    " WORK_NAME = " + current_WORK_NAME + "/" +
                    " TYPE_ID = " + current_TYPE_ID + "/" +
                    " TYPE_NAME = " + current_TYPE_NAME + "/" +
                    " CAT_ID = " + current_CATEGORY_ID + "/" +
                    " CAT_NAME = " + current_CATEGORY_NAME + "/" +
                    " COST = " + current_COST + "/" +
                    " COUNT = " + current_COUNT + "/" +
                    " UNIT = " + current_UNIT + "/" +
                    " SUMMA = " + current_SUMMA);
        }
        cursor.close();
    }

    //получаем расценки  по смете с id файла file_id
    public static float[] getArrayCost(SQLiteDatabase db, long file_id) {
        Log.i(TAG, "FW.getArrayCost ... ");

        String select = " SELECT " + FW_COST +
                " FROM " + TABLE_NAME +
                " WHERE " + FW_FILE_ID + " = " + file_id;
        Cursor cursor = db.rawQuery(select, null);
        Log.i(TAG, "FW.getArrayCost cursor.getCount()  " + cursor.getCount());

        float[] cost = new float[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            cost[position] = cursor.getFloat(cursor.getColumnIndex(FW_COST));
        }
        cursor.close();
        return cost;
    }

    //получаем расценки  по смете с id файла file_id и id типа type_id
    public static float[] getArrayCostSelectedType(SQLiteDatabase db, long file_id, long type_id) {
        Log.i(TAG, "FW.getArrayCostSelectedType ... ");


        Log.i(TAG, "FW.getArrayCostSelectedType case FW ");
        String select = " SELECT " + FW_COST +
                " FROM " + TABLE_NAME +
                " WHERE " + FW_FILE_ID + " = " + file_id +
                " AND " + FW_TYPE_ID + " = " + type_id;

        Cursor cursor = db.rawQuery(select, null);
        Log.i(TAG, "FW.getArrayCostSelectedType cursor.getCount()  " + cursor.getCount());

        float[] cost = new float[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            cost[position] = cursor.getFloat(cursor.getColumnIndex(FW_COST));
        }
        cursor.close();
        return cost;
    }

    //получаем количество работ по смете с id файла file_id
    public static float[] getArrayAmount(SQLiteDatabase db, long file_id) {
        Log.i(TAG, "FW.getArrayAmount ... ");

        String select = " SELECT " + FW_COUNT +
                " FROM " + TABLE_NAME +
                " WHERE " + FW_FILE_ID + " = " + file_id;
        Cursor cursor = db.rawQuery(select, null);
        Log.i(TAG, "FW.getArrayAmount cursor.getCount()  " + cursor.getCount());

        float[] amount = new float[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            amount[position] = cursor.getFloat(cursor.getColumnIndex(FW_COUNT));
        }
        cursor.close();
        return amount;
    }

    //получаем количество работ  по смете с id файла file_id и id типа type_id
    public static float[] getArrayAmountSelectedType(SQLiteDatabase db, long file_id, long type_id) {
        Log.i(TAG, "FW.getArrayAmount ... ");

        String select = " SELECT " + FW_COUNT +
                " FROM " + TABLE_NAME +
                " WHERE " + FW_FILE_ID + " = " + file_id +
                " AND " + FW_TYPE_ID + " = " + type_id;
        Cursor cursor = db.rawQuery(select, null);
        Log.i(TAG, "FW.getArrayAmountSelectedType cursor.getCount()  " + cursor.getCount());

        float[] amount = new float[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            amount[position] = cursor.getFloat(cursor.getColumnIndex(FW_COUNT));
        }
        cursor.close();
        return amount;
    }

    //получаем единицы измерения для  работ  по смете с id файла file_id
    public static String[] getArrayUnit(SQLiteDatabase db, long file_id) {
        Log.i(TAG, "FW.getArrayUnit ... ");

        String select = " SELECT " + FW_UNIT +
                " FROM " + TABLE_NAME +
                " WHERE " + FW_FILE_ID + " = " + file_id;
        Cursor cursor = db.rawQuery(select, null);
        Log.i(TAG, "FW.getArrayUnit cursor.getCount()  " + cursor.getCount());

        String[] units = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            units[position] = cursor.getString(cursor.getColumnIndex(FW_UNIT));
        }
        cursor.close();
        return units;
    }

    //получаем единицы измерения для  работ  по смете с id файла file_id
    public static String[] getArrayUnitSelectedType(SQLiteDatabase db, long file_id, long type_id) {
        Log.i(TAG, "FW.getArrayUnitSelectedType ... ");

        String select = " SELECT " + FW_UNIT +
                " FROM " + TABLE_NAME +
                " WHERE " + FW_FILE_ID + " = " + file_id +
                " AND " + FW_TYPE_ID + " = " + type_id;
        Cursor cursor = db.rawQuery(select, null);
        Log.i(TAG, "FW.getArrayUnitSelectedType cursor.getCount()  " + cursor.getCount());

        String[] units = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            units[position] = cursor.getString(cursor.getColumnIndex(FW_UNIT));
        }
        cursor.close();
        return units;
    }

    //получаем список работ по id файла из таблиц FW
    public static String[] getNames_FW(SQLiteDatabase db, long file_id) {
        Log.i(TAG, "FW.getNames_FW ... ");

        String select = " select DISTINCT " + FW_WORK_NAME +
                " from " + TABLE_NAME + " where " + FW_FILE_ID + " = " + file_id;
        Cursor cursor = db.rawQuery(select, null);
        Log.i(TAG, "FW.getNames_FW cursor.getCount()  " + cursor.getCount());

        String[] names = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            names[position] = cursor.getString(cursor.getColumnIndex(FW_WORK_NAME));
        }
        cursor.close();
        return names;
    }

    //получаем список типов по id файла из таблицы FW
    public static String[] getTypeNamesSort(SQLiteDatabase db, long file_id) {
        Log.i(TAG, "FW.getTypeNamesSort ... ");
        Cursor cursor = db.query(
                true,
                TABLE_NAME,                                 // таблица
                new String[]{FW_TYPE_NAME, FW_TYPE_ID},            // столбцы
                FW_FILE_ID + "=?",                  // столбцы для условия WHERE
                new String[]{String.valueOf(file_id)},     // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                FW_TYPE_ID,                 // порядок сортировки
                null);
        Log.i(TAG, "FW.getTypeNamesSort cursor.getCount()  " + cursor.getCount());
        String[] names = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            names[position] = cursor.getString(cursor.getColumnIndex(FW_TYPE_NAME));
        }
        cursor.close();
        return names;
    }

    //получаем количество работ  по смете с id файла file_id
    public static float[] getArraySumma(SQLiteDatabase db, long file_id) {
        Log.i(TAG, "FW.getArraySumma ... ");

        String select = " SELECT " + FW_SUMMA +
                " FROM " + TABLE_NAME +
                " WHERE " + FW_FILE_ID + " = " + file_id;
        Cursor cursor = db.rawQuery(select, null);
        Log.i(TAG, "FW.getArraySumma cursor.getCount()  " + cursor.getCount());

        float[] summa = new float[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            summa[position] = cursor.getFloat(cursor.getColumnIndex(FW_SUMMA));
        }
        cursor.close();
        return summa;
    }

    //получаем количество работ  по смете с id файла file_id
    public static float[] getArraySummaSelectedType(SQLiteDatabase db, long file_id, long type_id) {
        Log.i(TAG, "FW.getArraySumma ... ");

        String select = " SELECT " + FW.FW_SUMMA +
                " FROM " + FW.TABLE_NAME +
                " WHERE " + FW.FW_FILE_ID + " = " + file_id +
                " AND " + FW.FW_TYPE_ID + " = " + type_id;
        Cursor cursor = db.rawQuery(select, null);
        Log.i(TAG, "FW.getArraySumma cursor.getCount()  " + cursor.getCount());
        float[] summa = new float[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            summa[position] = cursor.getFloat(cursor.getColumnIndex(FW.FW_SUMMA));
        }
        cursor.close();
        return summa;
    }

    //получаем значение количества работы для сметы с file_id   work_id
    public static float getCount(SQLiteDatabase db, long file_id, long id) {
        Log.i(TAG, "FW.getCount ... ");
        float count = -1;

        String select = " SELECT " + FW_COUNT + " FROM " + TABLE_NAME +
                " where " + FW_FILE_ID + " =? " + " and " + FW_WORK_ID + " =? ";
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(file_id), String.valueOf(id)});

        if (cursor.moveToFirst()) {
            // Узнаем индекс столбца и Используем индекс для получения количества работы
            count = cursor.getFloat(cursor.getColumnIndex(FW_COUNT));
        }
        Log.d(TAG, "FW getCostById count = " + count);
        cursor.close();
        return count;
    }

    //получаем список категорий по id файла из таблицы FW
    public static String[] getArrayCategory(SQLiteDatabase db, long file_id) {
        Log.i(TAG, "FW.getArrayCategory ... ");

        String  select = " select DISTINCT " + FW_CATEGORY_NAME +
                " from " + TABLE_NAME + " where " + FW_FILE_ID + " = " + file_id;
        Cursor  cursor = db.rawQuery(select, null);
        Log.i(TAG, "FW.getArrayCategory cursor.getCount()  " + cursor.getCount());

        String[] categoryNames = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            categoryNames[position] = cursor.getString(cursor.getColumnIndex(FW_CATEGORY_NAME));
            Log.i(TAG, "FW.getArrayCategory categoryNames  " + categoryNames[position]);
        }
        cursor.close();
        return categoryNames;
    }

    //получаем список типов по id файла из таблицы FW
    public static String[] getTypeNames(SQLiteDatabase db, long file_id){
        Log.i(TAG, "FW.getTypeNames ... ");

        String select =  " select DISTINCT " + FW_TYPE_NAME +
                        " from " +  TABLE_NAME + " where " +  FW_FILE_ID + " = " + file_id;
        Cursor  cursor = db.rawQuery(select, null);
        Log.i(TAG, "FW.getTypeNames cursor.getCount() = " + cursor.getCount());

        String[] typeNames = new String[cursor.getCount()];
                // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            typeNames[position] = cursor.getString(cursor.getColumnIndex(FW_TYPE_NAME));
        }
        cursor.close();
        return typeNames;
    }

    //получаем курсор с именами типов работ
    public static Cursor getTypeNamesStructured(SQLiteDatabase db, long file_id){
        Log.i(TAG, "FW.getTypeNamesStructured ... ");

        Cursor   cursor = db.query(
                        true,
                        TABLE_NAME,                                  // таблица
                        new String[]{FW_TYPE_NAME, FW_TYPE_ID},            // столбцы
                        FW_FILE_ID  + "=?",                  // столбцы для условия WHERE
                        new String[]{String.valueOf(file_id)},                  // значения для условия WHERE
                        null,                  // Don't group the rows
                        null,                  // Don't filter by row groups
                        FW_TYPE_ID,                 // порядок сортировки
                        null);
        Log.i(TAG, "FW  getTypeNamesStructured.getCount() = " +  cursor.getCount());
        return cursor;
    }

    //получаем курсор с данными сметы с file_id для типа работ с type_id
    public static Cursor getDataSortStructured(SQLiteDatabase db, long file_id, long type_id){
        Log.i(TAG, "FW.getDataSortStructured ... ");

        Cursor cursor = db.query(
                        TABLE_NAME,   // таблица
                        new String[]{FW_WORK_NAME, FW_COST, FW_COUNT, FW_SUMMA},  // столбцы
                        FW_FILE_ID + "=?" + " AND " + FW_TYPE_ID + "=?", // столбцы для условия WHERE
                        new String[]{String.valueOf(file_id), String.valueOf(type_id)}, // значения для условия WHERE
                        null,                  // Don't group the rows
                        null,                  // Don't filter by row groups
                        FW_WORK_ID);                   // порядок сортировки

        Log.i(TAG, "FW getDataSortStructured  cursor.getCount() = " + cursor.getCount());
        return cursor;
    }

    /**
     * Вставляет строку в таблицу FW
     */
    public static long  insertRowInFW(SQLiteDatabase db, long file_id, long work_mat_id,
                                        long type_id, long category_id,  float cost,
                                        float  count, String unit, float summa){
        Log.i(TAG, "FW.insertRowInFWFM ... ");
        long ID = -1;

        //получаем имя файла по его id
        String fileName = FileWork.getNameFromId(db, file_id);
        Log.i(TAG, "FW.insertRowInFW fileName =  " + fileName);
        //получаем имя работы по id работы
        String workName = Work.getNameFromId(db, work_mat_id);
        Log.i(TAG, "FW.insertRowInFW workName =  " + workName);
        //получаем имя типа работы по id типа работы
        String typeName = TypeWork.getNameFromId(db, type_id);
        Log.i(TAG, "FW.insertRowInFW typeName =  " + typeName);
        //получаем имя категории работы по id категории работы
        String catName = CategoryWork.getNameFromId(db, category_id);
        Log.i(TAG, "FW.insertRowInFW catName =  " + catName);

        ContentValues cv = new ContentValues();
        cv.put(FW_FILE_ID,file_id);
        cv.put(FW_FILE_NAME,fileName);
        cv.put(FW_WORK_ID,work_mat_id);
        cv.put(FW_WORK_NAME,workName);
        cv.put(FW_TYPE_ID,type_id);
        cv.put(FW_TYPE_NAME,typeName);
        cv.put(FW_CATEGORY_ID,category_id);
        cv.put(FW_CATEGORY_NAME,catName);
        cv.put(FW_COST,cost);
        cv.put(FW_COUNT,count);
        cv.put(FW_UNIT,unit);
        cv.put(FW_SUMMA,summa);

        // вставляем строку
        ID = db.insert(TABLE_NAME, null, cv);
        Log.d(TAG, "FW.insertRowInFW  FW._ID = " + ID);
        return ID;
    }

    //получаем количество строк  с типом FW_WORK_ID
    public static int getCountLine(SQLiteDatabase db, long id){
        Log.i(TAG, "FW.getCountLine ... ");

        String   select = " SELECT " + _ID + " FROM " + TABLE_NAME +
                " where " + FW_WORK_ID + " =? ";
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(id)});

        int count = cursor.getCount();
        Log.i(TAG, "FW.getCountLine count = " + count);
        cursor.close();
        return count;
    }

    //получаем количество строк  с типом FW_WORK_ID
    public static int getCountLineInFileWork(SQLiteDatabase db, long file_id){
        Log.i(TAG, "FW.getCountLine ... ");

        String   select = " SELECT " + _ID + " FROM " + TABLE_NAME +
                " where " + FW_FILE_ID + " =? ";
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(file_id)});

        int count = cursor.getCount();
        Log.i(TAG, "FW.getCountLine count = " + count);
        cursor.close();
        return count;
    }

    /**
     * обновляем количество и сумму в таблице FW
     */
    public static void updateRowInFW(SQLiteDatabase db, long file_id,
                                     long id, float cost, String unit,
                                     float count, float summa){
        Log.i(TAG, "FW.updateRowInFW ... ");

        ContentValues cv = new ContentValues();
        cv.put(FW_COST, cost);
        cv.put(FW_UNIT, unit);
        cv.put(FW_COUNT, count);
        cv.put(FW_SUMMA, summa);

        db.update(TABLE_NAME, cv,
                FW_FILE_ID + " =? " +" AND " + FW_WORK_ID + " =? ",
                new String[]{String.valueOf(file_id), String.valueOf(id)});

        Log.i(TAG, "FW.updateRowInFW - cost =" +
                cost + "  summa = " + summa);
    }

    public static boolean isWorkInFW(SQLiteDatabase db, long file_id, long mat_id){
        Log.i(TAG, "FW.isWorkMatInFWFM ... ");

        String select = " SELECT " + FW_WORK_NAME + " FROM " + TABLE_NAME +
                        " where " + FW_FILE_ID + " =? " + " and " + FW_WORK_ID + " =? ";
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(file_id),String.valueOf(mat_id)});
        Log.i(TAG, "FW.isWorkMatInFWFM cursor.getCount() = " + cursor.getCount());

        if (cursor.getCount() != 0) {
            return true;
        }

        cursor.close();
        return false;
    }

    //получаем данные по таблице FW  по  id файла
    public static DataFW[] getDataFW(SQLiteDatabase db, long file_id) {
        Log.i(TAG, "FW.getDataWork ... ");

        String select = " SELECT  * FROM " + TABLE_NAME +
                " WHERE " + FW_FILE_ID + " = ? ";
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(file_id)});

        DataFW[] dataFW = new DataFW[cursor.getCount()];

        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            // Узнаем индекс каждого столбца и Используем индекс для получения строки
            long FW_File_ID = cursor.getLong(cursor.getColumnIndex(FW_FILE_ID));
            String FW_File_Name = cursor.getString(cursor.getColumnIndex(FW_FILE_NAME));

            long FW_Work_ID = cursor.getLong(cursor.getColumnIndex(FW_WORK_ID));
            String FW_Work_Name = cursor.getString(cursor.getColumnIndex(FW_WORK_NAME));

            long FW_Type_ID = cursor.getLong(cursor.getColumnIndex(FW_TYPE_ID));
            String FW_Type_Name = cursor.getString(cursor.getColumnIndex(FW_TYPE_NAME));

            long FW_Category_ID = cursor.getLong(cursor.getColumnIndex(FW_CATEGORY_ID));
            String FW_Category_Name = cursor.getString(cursor.getColumnIndex(FW_CATEGORY_NAME));

            float FW_Cost = cursor.getLong(cursor.getColumnIndex(FW_COST));
            int FW_Count = cursor.getInt(cursor.getColumnIndex(FW_COUNT));
            String FW_Unit = cursor.getString(cursor.getColumnIndex(FW_UNIT));
            float FW_Summa = cursor.getLong(cursor.getColumnIndex(FW_SUMMA));

            //создаём экземпляр класса DataWork в конструкторе
            dataFW[position] = new DataFW(FW_File_ID,FW_File_Name,FW_Work_ID, FW_Work_Name,
                    FW_Type_ID, FW_Type_Name,  FW_Category_ID, FW_Category_Name, FW_Cost,
                    FW_Count, FW_Unit, FW_Summa );
        }
        cursor.close();
        return dataFW;
    }

}



