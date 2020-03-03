package ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.data.DataTypeMat;

public class TypeMat {
    public static final String TAG = "33333";

    public TypeMat(){
        //пустой конструктор
    }

    public final static String TABLE_NAME = "TypeMat";

    public final static String _ID = BaseColumns._ID;
    public final static String TYPE_MAT_CATEGORY_ID = "TYPE_MAT_CATEGORY_ID";
    public final static String TYPE_MAT_NAME = "TYPE_MAT_NAME";
    public final static String TYPE_MAT_DESCRIPTION = "TYPE_MAT_DESCRIPTION";

    //создание таблицы
    public static void createTable(SQLiteDatabase db, Context fContext){
        // Строка для создания таблицы типов материалов TypeMat
        String SQL_CREATE_TAB_TYPE_MAT = "CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TYPE_MAT_CATEGORY_ID + " INTEGER NOT NULL, "
                + TYPE_MAT_NAME + " TEXT NOT NULL, "
                + TYPE_MAT_DESCRIPTION + " TEXT NOT NULL DEFAULT 'Без описания');";
        // Запускаем создание таблицы разделов (типов) работ TypeWork
        db.execSQL(SQL_CREATE_TAB_TYPE_MAT);
        Log.d(TAG, "SmetaOpenHelper - onCreate- создание таблицы TypeMat");
        // Если файлов в базе нет, вносим записи названия типов работ (добавление из программы)
        createDefaultTypeMat(db, fContext);
    }

    //создаём типы из ресурсов и получаем COLUMN_TYPE_CATEGORY_ID из  this.getArrayCategoryId()
    private static void createDefaultTypeMat(SQLiteDatabase db, Context fContext) {

        Log.i(TAG, "SmetaOpenHelper.createDefaultTypeMat...");
        // Добавляем записи в таблицу
        ContentValues values = new ContentValues();

        // Получим массив строк из ресурсов
        Resources res = fContext.getResources();
        String[] type_mat_name_electro = res.getStringArray(R.array.type_name_electro_mat);
        String[] type_mat_name_drugoe = res.getStringArray(R.array.type_name_drugoe_mat);

        //получаем массив Category._id  таблицы категорий Category
        int[] category_id_mat = getArrayCategoryIdMat(db);
        // проходим через массив и вставляем записи в таблицу
        int ii = 0; //type_mat_name_electro
        for (String s : type_mat_name_electro) {
            InsertTypeMat(db, values, category_id_mat[ii], s);
        }
        ii = 1; //type_mat_name_drugoe
        for (String s : type_mat_name_drugoe) {
            InsertTypeMat(db, values, category_id_mat[ii], s);
        }
        Log.d(TAG, "createDefaultTypeMat type_mat_name_electro.length = " +
                type_mat_name_electro.length + " type_mat_name_drugoe.length = " +type_mat_name_drugoe.length);
    }

    //получаем курсор с Category._id  и делаем массив id
    private static  int[] getArrayCategoryIdMat(SQLiteDatabase db) {

        Log.i(TAG, "SmetaOpenHelper.getArrayCategoryIdMat ... ");
        String categoryIdMat = " SELECT " + CategoryMat._ID  + " FROM " + CategoryMat.TABLE_NAME;
        Cursor cursor = db.rawQuery(categoryIdMat, null);
        int[] category_id_mat = new int[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            category_id_mat[position] = cursor.getInt(cursor.getColumnIndex(CategoryMat._ID));
            Log.i(TAG, "SmetaOpenHelper.getArrayCategoryId position = " + position);
        }
        cursor.close();
        return category_id_mat;
    }

    private static void InsertTypeMat(SQLiteDatabase db, ContentValues values,
                               int category_id, String type_name){
        values.put(TYPE_MAT_CATEGORY_ID, category_id);
        values.put(TYPE_MAT_NAME, type_name);
        db.insert(TABLE_NAME, null, values);
    }


    //получаем данные по типу материалов  по  id
    public static DataTypeMat getDataTypeMat(SQLiteDatabase db, long type_mat_id) {
        Log.i(TAG, "TableControllerSmeta.getDataTypeMat ... ");
        DataTypeMat dataTypeMat = new DataTypeMat();

        String typeMatData = " SELECT  * FROM " + TABLE_NAME +
                " WHERE " + _ID + " = ? ";
        Cursor cursor = db.rawQuery(typeMatData, new String[]{String.valueOf(type_mat_id)});

        if (cursor.moveToFirst()) {
            // Узнаем индекс каждого столбца и Используем индекс для получения строки
            long currentTypeMatCategoryId = cursor.getLong(cursor.getColumnIndex(TYPE_MAT_CATEGORY_ID));
            String currentTypeMatName = cursor.getString(cursor.getColumnIndex(TYPE_MAT_NAME));
            String currentTypeMatDescription = cursor.getString(cursor.getColumnIndex(TYPE_MAT_DESCRIPTION));
            Log.d(TAG, "TableControllerSmeta.getDataTypeMat currentTypeName = " + currentTypeMatName);
            //создаём экземпляр класса DataFile в конструкторе
            dataTypeMat = new DataTypeMat(currentTypeMatCategoryId, currentTypeMatName, currentTypeMatDescription);
        }
        cursor.close();
        return dataTypeMat;
    }

    //получаем id типа материалов по имени
    public static long getIdFromName(SQLiteDatabase db, String name) {
        Log.i(TAG, "TableControllerSmeta.getIdFromName ... ");
        long currentID = -1;
        Cursor cursor = db.query(
                TABLE_NAME,                     // таблица
                new String[]{_ID},            // столбцы
                TYPE_MAT_NAME + "=?",    // столбцы для условия WHERE
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

    //получаем ID категории материалов по имени типа материалов
    public static long getCatIdFromTypeId(SQLiteDatabase db, long type_id) {
        Log.d(TAG, "TableControllerSmeta getCatIdFromTypeId ...");

        long currentID = -1;

        Cursor cursor = db.query(
                TABLE_NAME,   // таблица
                new String[]{TYPE_MAT_CATEGORY_ID},            // столбцы
                _ID + "=?",   // столбцы для условия WHERE
                new String[]{String.valueOf(type_id)},                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        if (cursor.moveToFirst()) {
            // Используем индекс для получения строки или числа
            currentID = cursor.getLong(cursor.getColumnIndex(TYPE_MAT_CATEGORY_ID));
        }
        cursor.close();
        return currentID;
    }

    //получаем имя типа материала  по  id
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
            // Используем индекс для получения строки или числа
            currentName = cursor.getString(cursor.getColumnIndex(TYPE_MAT_NAME));
        }
        cursor.close();
        return currentName;
    }

    //удаляем тип материала из таблицы CategoryMat по id типа материала
    public static void deleteObject(SQLiteDatabase db, long id) {
        Log.i(TAG, "TableControllerSmeta.deleteObject case TypeMat ");
        db.delete(TABLE_NAME, _ID + " =? ", new String[]{String.valueOf(id)});
    }
}
