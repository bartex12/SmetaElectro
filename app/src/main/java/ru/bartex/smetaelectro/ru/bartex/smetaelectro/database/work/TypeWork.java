package ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.data.DataType;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.Mat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.TypeMat;

public class TypeWork {
    public static final String TAG = "33333";

    public TypeWork(){
        //пустой конструктор
    }

    public final static String TABLE_NAME = "Type";

    public final static String _ID = BaseColumns._ID;
    public final static String TYPE_CATEGORY_ID = "Type_Category_Id";
    public final static String TYPE_NAME = "Type_Name";
    public final static String TYPE_DESCRIPTION = "Type_Description";

    //создание таблицы
    public static void createTable(SQLiteDatabase db, Context fContext){
        // Строка для создания таблицы типов работ TypeWork
        String SQL_CREATE_TAB_TYPE = "CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TYPE_CATEGORY_ID + " INTEGER NOT NULL, "
                + TYPE_NAME + " TEXT NOT NULL, "
                + TYPE_DESCRIPTION + " TEXT NOT NULL DEFAULT 'Без описания');";
        // Запускаем создание таблицы разделов (типов) работ TypeWork
        db.execSQL(SQL_CREATE_TAB_TYPE);
        Log.d(TAG, "SmetaOpenHelper - onCreate- создание таблицы TypeWork");
        // Если файлов в базе нет, вносим записи названия типов работ (добавление из программы)
        createDefaultType(db, fContext);
    }

    //создаём типы из ресурсов и получаем COLUMN_TYPE_CATEGORY_ID из  this.getArrayCategoryId()
    private static void createDefaultType(SQLiteDatabase db, Context fContext) {
        Log.i(TAG, "SmetaOpenHelper.createDefaultType...");
        // Добавляем записи в таблицу
        ContentValues values = new ContentValues();

        // Получим массив строк из ресурсов
        Resources res = fContext.getResources();
        String[] type_name_category_electro = res.getStringArray(R.array.type_name_electro);
        String[] type_name_category_drugoe = res.getStringArray(R.array.type_name_drugoe);

        //получаем массив Category._id  таблицы категорий Category
        int[] category_id = getArrayCategoryId(db);
        // проходим через массив и вставляем записи в таблицу
        int ii = 0; //type_name_category_electro
        for (String s : type_name_category_electro) {
            InsertType(db, values, category_id[ii], s);
        }
        ii = 1; //type_name_category_drugoe
        for (String s : type_name_category_drugoe) {
            InsertType(db, values, category_id[ii], s);
        }
        Log.d(TAG, "createDefaultType type_name_category_electro.length = " + type_name_category_electro.length);
    }

    //получаем курсор с Category._id  и делаем массив id
    private static int[] getArrayCategoryId(SQLiteDatabase db) {
        Log.i(TAG, "SmetaOpenHelper.getArrayCategoryId ... ");
        String categoryId = " SELECT " + CategoryWork._ID  + " FROM " + CategoryWork.TABLE_NAME;
        Cursor cursor = db.rawQuery(categoryId, null);
        int[] category_id = new int[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            category_id[position] = cursor.getInt(cursor.getColumnIndex(CategoryWork._ID));
            Log.i(TAG, "SmetaOpenHelper.getArrayCategoryId position = " + position);
        }
        cursor.close();
        return category_id;
    }

    private static void InsertType(SQLiteDatabase db, ContentValues values,
                            int category_id, String type_name){
        values.put(TYPE_CATEGORY_ID, category_id);
        values.put(TYPE_NAME, type_name);
        db.insert(TABLE_NAME, null, values);
    }

    //получаем данные по  типу работы по её id
    public static DataType getDataType(SQLiteDatabase db, long type_id) {
        Log.i(TAG, "TableControllerSmeta.getDataType ... ");
        DataType dataType = new DataType();

        String typeData = " SELECT  * FROM " + TypeWork.TABLE_NAME +
                " WHERE " + TypeWork._ID + " = ? ";
        Cursor cursor = db.rawQuery(typeData, new String[]{String.valueOf(type_id)});

        if (cursor.moveToFirst()) {
            // Узнаем индекс каждого столбца и Используем индекс для получения строки
            long currentTypeCategoryId = cursor.getLong(cursor.getColumnIndex(TypeWork.TYPE_CATEGORY_ID));
            String currentTypeName = cursor.getString(cursor.getColumnIndex(TypeWork.TYPE_NAME));
            String currentTypeDescription = cursor.getString(cursor.getColumnIndex(TypeWork.TYPE_DESCRIPTION));
            Log.d(TAG, "TableControllerSmeta.getDataType currentTypeName = " + currentTypeName);
            //создаём экземпляр класса DataFile в конструкторе
            dataType = new DataType(currentTypeCategoryId, currentTypeName, currentTypeDescription);
        }
        cursor.close();
        return dataType;
    }

    //получаем id типа работы по имени
    public static long getIdFromName(SQLiteDatabase db, String name) {
        Log.i(TAG, "TableControllerSmeta.getIdFromName ... ");
        long currentID = -1;
        Cursor cursor = db.query(
                TABLE_NAME,                     // таблица
                new String[]{_ID},            // столбцы
                TYPE_NAME + "=?",    // столбцы для условия WHERE
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

    //получаем ID категории работы по имени типа работы
    public static long getCatIdFromTypeId(SQLiteDatabase db, long type_id) {
        Log.d(TAG, "TableControllerSmeta getCatIdFromTypeId ...");

        long currentID = -1;

        Cursor cursor = db.query(
                TABLE_NAME,   // таблица
                new String[]{TYPE_CATEGORY_ID},            // столбцы
                _ID + "=?",   // столбцы для условия WHERE
                new String[]{String.valueOf(type_id)},                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        if (cursor.moveToFirst()) {
            // Используем индекс для получения строки или числа
            currentID = cursor.getLong(cursor.getColumnIndex(TYPE_CATEGORY_ID));
        }
        cursor.close();
        return currentID;
    }

    //получаем имя типа работы  по  id
    public static String getNameFromId(SQLiteDatabase db, long id) {
        Log.i(TAG, "TableControllerSmeta getNameFromId... ");

        String currentName = "";

        Cursor cursor = db.query(
                true,
                TABLE_NAME,
                null,
                _ID + "=" + id,
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            currentName = cursor.getString(cursor.getColumnIndex(TYPE_NAME));
        }

        cursor.close();
        return currentName;
    }

    //удаляем тип работы из таблицы CategoryWork по id типа
    public static void deleteObject(SQLiteDatabase db, long id) {
        Log.i(TAG, "TableControllerSmeta.deleteObject case TypeWork ");
        db.delete(TABLE_NAME, _ID + " =? ", new String[]{String.valueOf(id)});
    }

    //Обновляем данные по типам работ
    public static void updateData(SQLiteDatabase db, long id, String name, String description) {
        Log.i(TAG, "TableControllerSmeta.updateData ...");

        //заполняем данные для обновления в базе
        ContentValues cv = new ContentValues();
        cv.put(TYPE_NAME, name);
        cv.put(TYPE_DESCRIPTION, description);
        db.update(TABLE_NAME, cv, _ID + "=" + id, null);

        Log.i(TAG, "TableControllerSmeta.updateData - name =" + name + "  id = " + id);
    }

    //получаем курсор с названиями категорий
    public static Cursor getCursorNames(SQLiteDatabase db) {
        Log.i(TAG, "TableControllerSmeta.getCursorNames ... ");

        String select =  " SELECT " + _ID + " , " + TYPE_CATEGORY_ID +
                " , " + TYPE_NAME + " FROM " + TABLE_NAME ;
        Cursor  cursor = db.rawQuery(select, null);

        Log.i(TAG, "TableControllerSmeta.getCursorNames cursor.getCount() =  " + cursor.getCount());
        return cursor;
    }

    //получаем курсор с названиями типов работ
    public static Cursor getNamesFromCatId(SQLiteDatabase db, long id) {
        Log.i(TAG, "TableControllerSmeta.getNamesFromCatId ... ");
        String  select = " SELECT " + _ID + " , " + TYPE_CATEGORY_ID +
                        " , " + TYPE_NAME + " FROM " + TABLE_NAME +
                        " WHERE " + TYPE_CATEGORY_ID  + " = ?" ;
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(id)});
        Log.i(TAG, "TableControllerSmeta.getNamesFromCatId cursor.getCount() =  " + cursor.getCount()+
                "  id = " + id);
        return cursor;
    }

    //Добавляем тип работ
    public static long  insertTypeCatName(SQLiteDatabase db, String name, long id){
        Log.i(TAG, "TableControllerSmeta.insertTypeCatName ... ");
        long _id =-1;

        ContentValues cv = new ContentValues();
        cv.put(TYPE_NAME,name);
        cv.put(TYPE_CATEGORY_ID,id);
        // вставляем строку
        _id = db.insert(TABLE_NAME, null, cv);
        Log.d(TAG, "TableControllerSmeta.insertTypeCatName  _id = " + _id);
        return _id;
    }
}
