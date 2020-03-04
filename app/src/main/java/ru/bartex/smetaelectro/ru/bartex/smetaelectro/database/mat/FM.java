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

    //получаем id типа  материала из FM
    public static long getTypeId_FM(SQLiteDatabase db, long file_id, long id) {
        Log.i(TAG, "TableControllerSmeta.getTypeIdFWFM ... ");

        long type_id = -1;

        String select = " SELECT " + FM_MAT_TYPE_ID + " FROM " + TABLE_NAME +
                " where " + FM_FILE_ID + " =? " + " and " + FM_MAT_ID + " =? ";
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(file_id), String.valueOf(id)});

        if (cursor.moveToFirst()) {
            // Узнаем индекс столбца и Используем индекс для получения id типа материала
            type_id = cursor.getLong(cursor.getColumnIndex(FM_MAT_TYPE_ID));
        }
        cursor.close();
        return type_id;
    }

    //удаляем работу/материал  из сметы FW/FM по file_id и work_id/mat_id
    public static void deleteItemFrom_FM(SQLiteDatabase db, long file_id, long id) {
        Log.i(TAG, "TableControllerSmeta.deleteItemFrom_FM ... ");

        db.delete(TABLE_NAME, FM_FILE_ID + " =? " + " AND " + FM_MAT_ID + " =? ",
                new String[]{String.valueOf(file_id), String.valueOf(id)});
    }

    //вывод в лог всех строк FM
    public static void displayTable(SQLiteDatabase db) {
        Log.i(TAG, "TableControllerSmeta.displayTable ...");
        // Создадим и откроем для чтения базу данных
        Cursor cursor = null;
        // Зададим условие для выборки - список столбцов
        String[] projectionFM = {
                _ID,
                FM_FILE_ID,
                FM_FILE_NAME,
                FM_MAT_ID,
                FM_MAT_NAME,
                FM_MAT_TYPE_ID,
                FM_MAT_TYPE_NAME,
                FM_MAT_CATEGORY_ID,
                FM_MAT_CATEGORY_NAME,
                FM_MAT_COST,
                FM_MAT_COUNT,
                FM_MAT_UNIT,
                FM_MAT_SUMMA};
        // Делаем запрос
        cursor = db.query(
                TABLE_NAME,   // таблица
                projectionFM,            // столбцы
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
                    cursor.getColumnIndex(FM_FILE_ID));
            String current_FILE_NAME = cursor.getString(
                    cursor.getColumnIndex(FM_FILE_NAME));
            int current_WORK_ID = cursor.getInt(
                    cursor.getColumnIndex(FM_MAT_ID));
            String current_WORK_NAME = cursor.getString(
                    cursor.getColumnIndex(FM_MAT_NAME));
            int current_TYPE_ID = cursor.getInt(
                    cursor.getColumnIndex(FM_MAT_TYPE_ID));
            String current_TYPE_NAME = cursor.getString(
                    cursor.getColumnIndex(FM_MAT_TYPE_NAME));
            int current_CATEGORY_ID = cursor.getInt(
                    cursor.getColumnIndex(FM_MAT_CATEGORY_ID));
            String current_CATEGORY_NAME = cursor.getString(
                    cursor.getColumnIndex(FM_MAT_CATEGORY_NAME));
            float current_COST = cursor.getFloat(
                    cursor.getColumnIndex(FM_MAT_COST));
            int current_COUNT = cursor.getInt(
                    cursor.getColumnIndex(FM_MAT_COUNT));
            String current_UNIT = cursor.getString(
                    cursor.getColumnIndex(FM_MAT_UNIT));
            float current_SUMMA = cursor.getFloat(
                    cursor.getColumnIndex(FM_MAT_SUMMA));
            // Выводим построчно значения каждого столбца
            Log.d(TAG, "\n" + "ID = " + currentID + "/" +
                    " FILE_ID = " + current_FILE_ID + "/" +
                    " FILE_NAME = " + current_FILE_NAME + "/" +
                    " MAT_ID = " + current_WORK_ID + "/" +
                    " MAT_NAME = " + current_WORK_NAME + "/" +
                    " MAT_TYPE_ID = " + current_TYPE_ID + "/" +
                    " MAT_TYPE_NAME = " + current_TYPE_NAME + "/" +
                    " MAT_CATEGORY_ID = " + current_CATEGORY_ID + "/" +
                    " MAT_CATEGORY_NAME = " + current_CATEGORY_NAME + "/" +
                    " MAT_COST = " + current_COST + "/" +
                    " MAT_COUNT = " + current_COUNT + "/" +
                    " MAT_UNIT = " + current_UNIT + "/" +
                    " MAT_SUMMA = " + current_SUMMA);
        }
        cursor.close();
    }

    //получаем имена   по смете с id файла file_id
    public static String[] getArrayNames(SQLiteDatabase db, long file_id) {
        Log.i(TAG, "TableControllerSmeta.getArrayNames ... ");

        String select = " SELECT " + FM_MAT_NAME +
                " FROM " + TABLE_NAME +
                " WHERE " + FM_FILE_ID + " = " + file_id;

        Cursor cursor = db.rawQuery(select, null);
        Log.i(TAG, "TableControllerSmeta.getArrayNames cursor.getCount()  " + cursor.getCount());

        String[] name = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            name[position] = cursor.getString(cursor.getColumnIndex(FM_MAT_NAME));
        }
        cursor.close();
        return name;
    }

    //получаем имена   по смете с id файла file_id и id типа type_id
    public static String[] getArrayNamesSelectedType(SQLiteDatabase db, long file_id, long type_id) {
        Log.i(TAG, "TableControllerSmeta.getArrayNamesSelectedType ... ");

        String select = " SELECT " + FM_MAT_NAME +
                " FROM " + TABLE_NAME +
                " WHERE " + FM_FILE_ID + " = " + file_id +
                " AND " + FM_MAT_TYPE_ID + " = " + type_id;
        Cursor cursor = db.rawQuery(select, null);
        Log.i(TAG, "TableControllerSmeta.getArrayNamesSelectedType cursor.getCount()  " + cursor.getCount());

        String[] name = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            name[position] = cursor.getString(cursor.getColumnIndex(FM_MAT_NAME));
        }
        cursor.close();
        return name;
    }

    //получаем расценки  по смете с id файла file_id
    public static float[] getArrayCost(SQLiteDatabase db, long file_id) {
        Log.i(TAG, "TableControllerSmeta.getArrayCost ... ");

        String select = " SELECT " + FM_MAT_COST +
                " FROM " + TABLE_NAME +
                " WHERE " + FM_FILE_ID + " = " + file_id;
        Cursor cursor = db.rawQuery(select, null);
        Log.i(TAG, "TableControllerSmeta.getArrayCost cursor.getCount()  " + cursor.getCount());

        float[] cost = new float[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            cost[position] = cursor.getFloat(cursor.getColumnIndex(FM_MAT_COST));
        }
        cursor.close();
        return cost;
    }

    //получаем расценки  по смете с id файла file_id и id типа type_id
    public static float[] getArrayCostSelectedType(SQLiteDatabase db, long file_id, long type_id) {
        Log.i(TAG, "TableControllerSmeta.getArrayCostSelectedType ... ");


        Log.i(TAG, "TableControllerSmeta.getArrayCostSelectedType case FW ");
        String select = " SELECT " + FM_MAT_COST +
                " FROM " + TABLE_NAME +
                " WHERE " + FM_FILE_ID + " = " + file_id +
                " AND " + FM_MAT_TYPE_ID + " = " + type_id;


        Cursor cursor = db.rawQuery(select, null);
        Log.i(TAG, "TableControllerSmeta.getArrayCostSelectedType cursor.getCount()  " + cursor.getCount());

        float[] cost = new float[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            cost[position] = cursor.getFloat(cursor.getColumnIndex(FM_MAT_COST));
        }
        cursor.close();
        return cost;
    }

    //получаем количество материалов по смете с id файла file_id
    public static float[] getArrayAmount(SQLiteDatabase db, long file_id) {
        Log.i(TAG, "TableControllerSmeta.getArrayAmount ... ");

        String select = " SELECT " + FM_MAT_COUNT +
                " FROM " + TABLE_NAME +
                " WHERE " + FM_FILE_ID + " = " + file_id;
        Cursor cursor = db.rawQuery(select, null);
        Log.i(TAG, "TableControllerSmeta.getArrayAmount cursor.getCount()  " + cursor.getCount());

        float[] amount = new float[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            amount[position] = cursor.getFloat(cursor.getColumnIndex(FM_MAT_COUNT));
        }
        cursor.close();
        return amount;
    }

    //получаем количество материалов  по смете с id файла file_id и id типа type_id
    public static float[] getArrayAmountSelectedType(SQLiteDatabase db, long file_id, long type_id) {
        Log.i(TAG, "TableControllerSmeta.getArrayAmount ... ");

        String select = " SELECT " + FM_MAT_COUNT +
                " FROM " + TABLE_NAME +
                " WHERE " + FM_FILE_ID + " = " + file_id +
                " AND " + FM_MAT_TYPE_ID + " = " + type_id;
        Cursor cursor = db.rawQuery(select, null);
        Log.i(TAG, "TableControllerSmeta.getArrayAmountSelectedType cursor.getCount()  " + cursor.getCount());

        float[] amount = new float[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            amount[position] = cursor.getFloat(cursor.getColumnIndex(FM_MAT_COUNT));
        }
        cursor.close();
        return amount;
    }

    //получаем единицы измерения для  материалов  по смете с id файла file_id
    public static String[] getArrayUnit(SQLiteDatabase db, long file_id) {
        Log.i(TAG, "TableControllerSmeta.getArrayUnit ... ");

        String select = " SELECT " + FM_MAT_UNIT +
                " FROM " + TABLE_NAME +
                " WHERE " + FM_FILE_ID + " = " + file_id;
        Cursor cursor = db.rawQuery(select, null);
        Log.i(TAG, "TableControllerSmeta.getArrayUnit cursor.getCount()  " + cursor.getCount());

        String[] units = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            units[position] = cursor.getString(cursor.getColumnIndex(FM_MAT_UNIT));
            Log.i(TAG, "TableControllerSmeta.getArrayUnit units[position] = " + units[position]);
        }
        cursor.close();
        return units;
    }

    //получаем единицы измерения для  материалов  по смете с id файла file_id и type_id
    public static String[] getArrayUnitSelectedType(SQLiteDatabase db, long file_id, long type_id) {
        Log.i(TAG, "TableControllerSmeta.getArrayUnitSelectedType ... ");

        String select = " SELECT " + FM_MAT_UNIT +
                " FROM " + TABLE_NAME +
                " WHERE " + FM_FILE_ID + " = " + file_id +
                " AND " + FM_MAT_TYPE_ID + " = " + type_id;
        Cursor cursor = db.rawQuery(select, null);
        Log.i(TAG, "TableControllerSmeta.getArrayUnitSelectedType cursor.getCount()  " + cursor.getCount());

        String[] units = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            units[position] = cursor.getString(cursor.getColumnIndex(FM_MAT_UNIT));
        }
        cursor.close();
        return units;
    }

    //получаем список материалов по id файла из таблиц FM
    public static String[] getNames_FM(SQLiteDatabase db, long file_id) {
        Log.i(TAG, "TableControllerSmeta.getMatNamesFM ... ");

        String select = " select DISTINCT " + FM_MAT_NAME +
                " from " + TABLE_NAME + " where " + FM_FILE_ID + " = " + file_id;
        Cursor cursor = db.rawQuery(select, null);
        Log.i(TAG, "TableControllerSmeta.getMatNamesFM cursor.getCount()  " + cursor.getCount());

        String[] names = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            names[position] = cursor.getString(cursor.getColumnIndex(FM_MAT_NAME));
        }
        cursor.close();
        return names;
    }

    //получаем список типов по id файла из таблицы FM
    public static String[] getTypeNamesSort(SQLiteDatabase db, long file_id) {
        Log.i(TAG, "TableControllerSmeta.getTypeNamesSort ... ");
        Cursor cursor = db.query(
                true,
                TABLE_NAME,   // таблица
                new String[]{FM_MAT_TYPE_NAME, FM_MAT_TYPE_ID},            // столбцы
                FM_FILE_ID + "=?",                  // столбцы для условия WHERE
                new String[]{String.valueOf(file_id)},                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                FM_MAT_TYPE_ID,              // порядок сортировки
                null);
        Log.i(TAG, "TableControllerSmeta.getTypeNamesSort cursor.getCount()  " + cursor.getCount());
        String[] names = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            names[position] = cursor.getString(cursor.getColumnIndex(FM_MAT_TYPE_NAME));
        }
        cursor.close();
        return names;
    }

    //получаем количество материалов  по смете с id файла file_id
    public static float[] getArraySumma(SQLiteDatabase db, long file_id) {
        Log.i(TAG, "TableControllerSmeta.getArraySumma ... ");

        String select = " SELECT " + FM_MAT_SUMMA +
                " FROM " + TABLE_NAME +
                " WHERE " + FM_FILE_ID + " = " + file_id;
        Cursor cursor = db.rawQuery(select, null);
        Log.i(TAG, "TableControllerSmeta.getArraySumma cursor.getCount()  " + cursor.getCount());

        float[] summa = new float[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            summa[position] = cursor.getFloat(cursor.getColumnIndex(FM_MAT_SUMMA));
        }
        cursor.close();
        return summa;
    }

    //получаем количество материалов  по смете с id файла file_id
    public static float[] getArraySummaSelectedType(SQLiteDatabase db, long file_id, long type_id) {
        Log.i(TAG, "TableControllerSmeta.getArraySumma ... ");

        String select = " SELECT " + FM_MAT_SUMMA +
                " FROM " + TABLE_NAME +
                " WHERE " + FM_FILE_ID + " = " + file_id +
                " AND " + FM_MAT_TYPE_ID + " = " + type_id;
        Cursor cursor = db.rawQuery(select, null);
        Log.i(TAG, "TableControllerSmeta.getArraySumma cursor.getCount()  " + cursor.getCount());
        float[] summa = new float[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            summa[position] = cursor.getFloat(cursor.getColumnIndex(FM_MAT_SUMMA));
        }
        cursor.close();
        return summa;
    }

    //получаем значение количества материалов для сметы с file_id   MAT_ID
    public static float getCount(SQLiteDatabase db, long file_id, long id) {
        Log.i(TAG, "TableControllerSmeta.getCount ... ");
        float count = -1;

        String select = " SELECT " + FM_MAT_COUNT + " FROM " + TABLE_NAME +
                " where " + FM_FILE_ID + " =? " + " and " + FM_MAT_ID + " =? ";
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(file_id), String.valueOf(id)});

        if (cursor.moveToFirst()) {
            // Узнаем индекс столбца и Используем индекс для получения количества материалов
            count = cursor.getFloat(cursor.getColumnIndex(FM_MAT_COUNT));
        }
        Log.d(TAG, "TableControllerSmeta getCostById count = " + count);
        cursor.close();
        return count;
    }

    //получаем список категорий по id файла из таблицы FM
    public static String[] getArrayCategory(SQLiteDatabase db, long file_id) {
        Log.i(TAG, "TableControllerSmeta.getArrayCategory ... ");

        String  select = " select DISTINCT " + FM_MAT_CATEGORY_NAME +
                " from " + TABLE_NAME + " where " + FM_FILE_ID + " = " + file_id;
        Cursor  cursor = db.rawQuery(select, null);
        Log.i(TAG, "TableControllerSmeta.getArrayCategory cursor.getCount()  " + cursor.getCount());

        String[] categoryNames = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            categoryNames[position] = cursor.getString(cursor.getColumnIndex(FM_MAT_CATEGORY_NAME));
        }
        cursor.close();
        return categoryNames;
    }

    //получаем список типов по id файла из таблицы FM
    public static String[] getTypeNames(SQLiteDatabase db, long file_id){
        Log.i(TAG, "TableControllerSmeta.getTypeNames ... ");

        String  select =  " select DISTINCT " + FM_MAT_TYPE_NAME +
                " from " +  TABLE_NAME + " where " +  FM_FILE_ID + " = " + file_id;
        Cursor  cursor = db.rawQuery(select, null);
        Log.i(TAG, "TableControllerSmeta.getTypeNames cursor.getCount()  " + cursor.getCount());

        String[] typeNames = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            typeNames[position] = cursor.getString(cursor.getColumnIndex(FM_MAT_TYPE_NAME));
        }
        cursor.close();
        return typeNames;
    }

    //получаем курсор с именами типов материалов
    public static Cursor getTypeNamesStructured(SQLiteDatabase db, long file_id){
        Log.i(TAG, "TableControllerSmeta.getTypeNamesStructured ... ");

        Cursor   cursor = db.query(
                true,
                TABLE_NAME,                                      // таблица
                new String[]{FM_MAT_TYPE_NAME, FM_MAT_TYPE_ID},            // столбцы
                FM_FILE_ID  + "=?",                  // столбцы для условия WHERE
                new String[]{String.valueOf(file_id)},                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                FM_MAT_TYPE_ID,                 // порядок сортировки
                null);
        Log.i(TAG, "TableControllerSmeta  getTypeNamesStructured.getCount() = " +  cursor.getCount());
        return cursor;
    }

    //получаем курсор с данными сметы с file_id для типа материалов с type_id
    public static Cursor getDataSortStructured(SQLiteDatabase db, long file_id, long type_id){
        Log.i(TAG, "TableControllerSmeta.getDataSortStructured ... ");

        Cursor cursor = db.query(
                TABLE_NAME,   // таблица
                new String[]{FM_MAT_NAME,FM_MAT_COST,FM_MAT_COUNT,FM_MAT_SUMMA},// столбцы
                FM_FILE_ID + "=?"+ " AND " + FM_MAT_TYPE_ID + "=?",// столбцы для условия WHERE
                new String[]{String.valueOf(file_id), String.valueOf(type_id)}, // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                FM_MAT_ID);                   // порядок сортировки

        Log.i(TAG, "TableControllerSmeta getDataSortStructured  cursor.getCount() = " + cursor.getCount());
        return cursor;
    }

}
