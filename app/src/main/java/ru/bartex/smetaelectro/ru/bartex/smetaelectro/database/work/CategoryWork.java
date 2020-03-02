package ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.data.DataCategory;

public class CategoryWork {
    public static final String TAG = "33333";

    public CategoryWork(){
        //пустой конструктор
    }
    public final static String TABLE_NAME = "Category";

    public final static String _ID = BaseColumns._ID;
    public final static String CATEGORY_NAME = "CategoryName";
    public final static String CATEGORY_DESCRIPTION = "DescriptionOfCategory";

    //создание таблицы
    public static void createTable(SQLiteDatabase db, Context fContext){
        // Строка для создания таблицы категорий работ CategoryWork
        String SQL_CREATE_TAB_CATEGORY = "CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CATEGORY_NAME + " TEXT NOT NULL, "
                + CATEGORY_DESCRIPTION + " TEXT NOT NULL DEFAULT 'Без описания');";
        // Запускаем создание таблицы категорий работ
        db.execSQL(SQL_CREATE_TAB_CATEGORY);
        Log.d(TAG, "SmetaOpenHelper - onCreate- создание таблицы CategoryWork");
        // Если файлов в базе нет, вносим записи названий категорий работ (добавление из программы)
        createDefaultCategory(db, fContext);
    }

    //обновление таблицы
    static void onUpgrade(SQLiteDatabase database) {
        //обновлять пока не собираюсь
    }

    //создаём категории из ресурсов
    private static void createDefaultCategory(SQLiteDatabase db, Context fContext) {

        ContentValues values = new ContentValues();
        // Получим массив строк из ресурсов
        Resources res = fContext.getResources();
        String[] cat_name = res.getStringArray(R.array.category_name);
        String[] cat_descr = res.getStringArray(R.array.category_descr);
        // проходим через массив и вставляем записи в таблицу
        int length = cat_name.length;
        for (int i = 0; i<length ; i++){
            values.put(CATEGORY_NAME, cat_name[i]);
            values.put(CATEGORY_DESCRIPTION, cat_descr[i]);
            // Добавляем записи в таблицу
            db.insert(TABLE_NAME, null, values);
        }
        Log.d(TAG, "createDefaultCategory cat_name.length = " + cat_name.length);
    }

    //получаем данные по категории работы по её id
    public static DataCategory getDataCategory(SQLiteDatabase db, long cat_id){
        Log.i(TAG, "CategoryWork.getDataCategory ... ");
        DataCategory dataCategory = new DataCategory();

        String catData = " SELECT  * FROM " +  TABLE_NAME +
                " WHERE " + _ID  + " = ? " ;
        Cursor cursor = db.rawQuery(catData, new String[]{String.valueOf(cat_id)});

        if (cursor.moveToFirst()) {
            // Узнаем индекс каждого столбца и Используем индекс для получения строки
            String currentCatName = cursor.getString(cursor.getColumnIndex(CATEGORY_NAME));
            String currentCatDescription = cursor.getString(cursor.getColumnIndex(CATEGORY_DESCRIPTION));
            Log.d(TAG, "CategoryWork.getDataCategory currentCatName = " + currentCatName);
            //создаём экземпляр класса DataFile в конструкторе
            dataCategory = new DataCategory(currentCatName, currentCatDescription);
        }
        cursor.close();
        return dataCategory;
    }

}
