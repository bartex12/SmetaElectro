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
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.CategoryMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.TypeMat;

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
        Log.d(TAG, "CategoryWork - onCreate- создание таблицы CategoryWork");
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
        Log.d(TAG, "CategoryWork createDefaultCategory cat_name.length = " + cat_name.length);
    }

    //получаем данные по категории работы по её id
    public static DataCategory getDataCategory(SQLiteDatabase db, long cat_id) {
        Log.i(TAG, "CategoryWork.getDataCategory ... ");
        DataCategory dataCategory = new DataCategory();

        String catData = " SELECT  * FROM " + TABLE_NAME +
                " WHERE " + _ID + " = ? ";
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

    //получаем id категории по имени
    public static long getIdFromName(SQLiteDatabase db, String name) {
        Log.i(TAG, "CategoryWork.getIdFromName ... ");
        long currentID = -1;
        Cursor cursor = db.query(
                TABLE_NAME,                     // таблица
                new String[]{_ID},            // столбцы
                CATEGORY_NAME + "=?",  // столбцы для условия WHERE
                new String[]{name},             // значения для условия WHERE
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
        Log.i(TAG, "CategoryWork getNameFromId... ");

        String currentName = "";

        Cursor cursor = db.query(
                true,
                TABLE_NAME,
                null,
                _ID + "=" + id,
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            currentName = cursor.getString(cursor.getColumnIndex(CATEGORY_NAME));
        }

        cursor.close();
        return currentName;
    }

    //удаляем категорию работы из таблицы CategoryWork по id категории
    public static void deleteObject(SQLiteDatabase db, long id) {
        Log.i(TAG, "CategoryWork.deleteObject case CategoryWork ");
        db.delete(TABLE_NAME, _ID + " =? ", new String[]{String.valueOf(id)});
    }

    //Обновляем данные по категории работ
    public static void updateData(SQLiteDatabase db, long id, String name, String description) {
        Log.i(TAG, "CategoryWork.updateData ...");

        //заполняем данные для обновления в базе
        ContentValues cv = new ContentValues();
        cv.put(CATEGORY_NAME, name);
        cv.put(CATEGORY_DESCRIPTION, description);
        db.update(TABLE_NAME, cv, _ID + "=" + id, null);

        Log.i(TAG, "CategoryWork.updateData - name =" + name + "  id = " + id);
    }

    //получаем курсор с названиями категорий
    public static Cursor getCursorNames(SQLiteDatabase db) {
        Log.i(TAG, "CategoryWork.getCursorNames ... ");

        String select = " SELECT " + _ID + " , " + CATEGORY_NAME + " FROM " + TABLE_NAME;
        Cursor  cursor = db.rawQuery(select, null);

        Log.i(TAG, "CategoryWork.getCursorNames cursor.getCount() =  " + cursor.getCount());
        return cursor;
    }

    //Добавляем категорию работ
    public static long  insertCategory(SQLiteDatabase db, String catName){
        Log.i(TAG, "CategoryWork.insertCategory ... ");
        long _id =-1;

        ContentValues cv = new ContentValues();
        cv.put(CATEGORY_NAME,catName);
        // вставляем строку
        _id = db.insert(TABLE_NAME, null, cv);

        Log.d(TAG, "CategoryWork.insertCategory  _id = " + _id);
        return _id;
    }

    //получаем массив строк  с названиями категорий
    public static String[] getArrayCategoryWorkNames(SQLiteDatabase db) {
        Log.i(TAG, " ==++== CategoryWork.getArrayCategoryWorkNames ... ");

        String select = " SELECT " + _ID + " , " + CATEGORY_NAME + " FROM " + TABLE_NAME;
        Cursor  cursor = db.rawQuery(select, null);

        String[] categoryNames = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            categoryNames[position] = cursor.getString(cursor.getColumnIndex(CATEGORY_NAME));
        }
        Log.i(TAG, " ==++== CategoryWork.getArrayCategoryWorkNames... Count = " +
                cursor.getCount());

        cursor.close();
        return categoryNames;
    }

    //получаем boolean массив с отметкой - есть ли такая позиция категории в смете
    public static  boolean[] getArrayCategoryWorkChecked(
            SQLiteDatabase db, long file_id, String[] categoryNames) {
        Log.i(TAG, " ==++== CategoryWork.getArrayCategoryWorkChecked ... " +
                " categoryNames.length = " + categoryNames.length);
        String[] catMatNamesFW = FW.getArrayCategory(db, file_id);
        Log.i(TAG, " ==++== CategoryWork.getArrayCategoryWorkChecked ... " +
                " catMatNamesFW.length = " + catMatNamesFW.length);
        boolean[] categoryChacked = new boolean[categoryNames.length];

        for (int i = 0; i<categoryNames.length; i++) {
            for (int s = 0; s < catMatNamesFW.length; s++) {
                Log.i(TAG, "i = " + i + " categoryNames = " + categoryNames[i] +
                        " s = " + s + " catMatNamesFW = " + catMatNamesFW[s]);
                if (categoryNames[i].equals(catMatNamesFW[s])) {
                    categoryChacked[i] = true;
                    Log.i(TAG, "categoryChacked = " + categoryChacked[i]);
                    //если есть совпадение, прекращаем перебор
                    break;
                }
            }
        }
        Log.i(TAG, " ==++== CategoryWork categoryChacked.length  = "+ categoryChacked.length);
        return categoryChacked;
    }
}
