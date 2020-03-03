package ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.data.DataCategoryMat;

public class CategoryMat {
    public static final String TAG = "33333";

    public CategoryMat(){
        //пустой конструктор
    }
    public final static String TABLE_NAME = "CategoryMat";

    public final static String _ID = BaseColumns._ID;
    public final static String CATEGORY_MAT_NAME = "CATEGORY_MAT_NAME";
    public final static String CATEGORY_MAT_DESCRIPTION = "CATEGORY_MAT_DESCRIPTION";

    //создание таблицы
    public static void createTable(SQLiteDatabase db, Context fContext){
        // Строка для создания таблицы категорий материалов CategoryMat
        String SQL_CREATE_TAB_CATEGORY_MAT = "CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CATEGORY_MAT_NAME + " TEXT NOT NULL, "
                + CATEGORY_MAT_DESCRIPTION + " TEXT NOT NULL DEFAULT 'Без описания');";
        // Запускаем создание таблицы категорий работ
        db.execSQL(SQL_CREATE_TAB_CATEGORY_MAT);
        Log.d(TAG, "SmetaOpenHelper - onCreate- создание таблицы CategoryMat");
        // Если файлов в базе нет, вносим записи названий категорий материалов (добавление из программы)
        createDefaultCategoryMat(db, fContext);
    }

    //обновление таблицы
    static void onUpgrade(SQLiteDatabase database) {
        //обновлять пока не собираюсь
    }

    //создаём категории из ресурсов
    private static void createDefaultCategoryMat(SQLiteDatabase db, Context fContext) {

        ContentValues values = new ContentValues();
        // Получим массив строк из ресурсов
        Resources res = fContext.getResources();
        String[] cat_name_mat = res.getStringArray(R.array.category_name_mat);
        String[] cat_descr_mat = res.getStringArray(R.array.category_descr_mat);
        // проходим через массив и вставляем записи в таблицу
        int length = cat_name_mat.length;
        for (int i = 0; i<length ; i++){
            values.put(CATEGORY_MAT_NAME, cat_name_mat[i]);
            values.put(CATEGORY_MAT_DESCRIPTION, cat_descr_mat[i]);
            // Добавляем записи в таблицу
            db.insert(TABLE_NAME, null, values);
        }
        Log.d(TAG, "createDefaultCategory cat_name.length = " + cat_name_mat.length);
    }

    //получаем данные по категории материалов по её id
    public static DataCategoryMat getDataCategoryMat(SQLiteDatabase db, long cat_mat_id) {
        Log.i(TAG, "TableControllerSmeta.getDataCategoryMat ... ");
        DataCategoryMat dataCategory = new DataCategoryMat();

        String catData = " SELECT  * FROM " + TABLE_NAME +
                " WHERE " + _ID + " = ? ";
        Cursor cursor = db.rawQuery(catData, new String[]{String.valueOf(cat_mat_id)});

        if (cursor.moveToFirst()) {
            // Узнаем индекс каждого столбца и Используем индекс для получения строки
            String currentCatName = cursor.getString(cursor.getColumnIndex(CATEGORY_MAT_NAME));
            String currentCatDescription = cursor.getString(cursor.getColumnIndex(CATEGORY_MAT_DESCRIPTION));
            Log.d(TAG, "TableControllerSmeta.getDataCategoryMat currentCatName = " + currentCatName);
            //создаём экземпляр класса DataFile в конструкторе
            dataCategory = new DataCategoryMat(currentCatName, currentCatDescription);
        }
        cursor.close();
        return dataCategory;
    }

    //получаем id категории материалов по имени
    public static long getIdFromName(SQLiteDatabase db, String name) {
        Log.i(TAG, "TableControllerSmeta.getIdFromName ... ");
        long currentID = -1;
        Cursor cursor = db.query(
                TABLE_NAME,                     // таблица
                new String[]{_ID},            // столбцы
                CATEGORY_MAT_NAME + "=?",    // столбцы для условия WHERE
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

    //получаем имя категории  по  id
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
            currentName = cursor.getString(cursor.getColumnIndex(CATEGORY_MAT_NAME));
        }
        cursor.close();
        return currentName;
    }

    //удаляем категорию материала из таблицы CategoryMat по id категории материала
    public static void deleteObject(SQLiteDatabase db, long id) {
        Log.i(TAG, "TableControllerSmeta.deleteObject  case CategoryMat ");
        db.delete(TABLE_NAME, _ID + " =? ", new String[]{String.valueOf(id)});
    }
}
