package ru.bartex.smetaelectro.database.mat;

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
        Log.d(TAG, "CategoryMat - onCreate- создание таблицы CategoryMat");
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
        Log.d(TAG, "CategoryMat createDefaultCategory cat_name.length = " + cat_name_mat.length);
    }

    //получаем данные по категории материалов по её id
    public static DataCategoryMat getDataCategoryMat(SQLiteDatabase db, long cat_mat_id) {
        Log.i(TAG, "CategoryMat.getDataCategoryMat ... ");
        DataCategoryMat dataCategory = new DataCategoryMat();

        String catData = " SELECT  * FROM " + TABLE_NAME +
                " WHERE " + _ID + " = ? ";
        Cursor cursor = db.rawQuery(catData, new String[]{String.valueOf(cat_mat_id)});

        if (cursor.moveToFirst()) {
            // Узнаем индекс каждого столбца и Используем индекс для получения строки
            String currentCatName = cursor.getString(cursor.getColumnIndex(CATEGORY_MAT_NAME));
            String currentCatDescription = cursor.getString(cursor.getColumnIndex(CATEGORY_MAT_DESCRIPTION));
            Log.d(TAG, "CategoryMat.getDataCategoryMat currentCatName = " + currentCatName);
            //создаём экземпляр класса DataFile в конструкторе
            dataCategory = new DataCategoryMat(currentCatName, currentCatDescription);
        }
        cursor.close();
        return dataCategory;
    }

    //получаем id категории материалов по имени
    public static long getIdFromName(SQLiteDatabase db, String name) {
        Log.i(TAG, "CategoryMat.getIdFromName ... ");
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
        Log.i(TAG, "CategoryMat getNameFromId... ");

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
        Log.i(TAG, "CategoryMat.deleteObject  case CategoryMat ");
        db.delete(TABLE_NAME, _ID + " =? ", new String[]{String.valueOf(id)});
    }

    //Обновляем данные по категории материалов
    public static void updateData(SQLiteDatabase db, long id, String name, String description) {
        Log.i(TAG, "CategoryMat.updateData ...");

        //заполняем данные для обновления в базе
        ContentValues cv = new ContentValues();
        cv.put(CATEGORY_MAT_NAME, name);
        cv.put(CATEGORY_MAT_DESCRIPTION, description);
        db.update(TABLE_NAME, cv, _ID + "=" + id, null);

        Log.i(TAG, "CategoryMat.updateData - name =" + name + "  id = " + id);
    }

    //получаем курсор с названиями категорий
    public static Cursor getCursorNames(SQLiteDatabase db) {
        Log.i(TAG, "CategoryMat.getCursorNames ... ");

        String select =  " SELECT " + _ID + " , " +
                CATEGORY_MAT_NAME + " FROM " + TABLE_NAME;
        Cursor  cursor = db.rawQuery(select, null);

        Log.i(TAG, "CategoryMat.getCursorNames cursor.getCount() =  " + cursor.getCount());
        return cursor;
    }

    //Добавляем категорию материала
    public static long  insertCategory(SQLiteDatabase db, String catName){
        Log.i(TAG, "CategoryMat.insertCategory ... ");
        long _id =-1;

        ContentValues cv = new ContentValues();
        cv = new ContentValues();
        cv.put(CATEGORY_MAT_NAME,catName);
        // вставляем строку
        _id = db.insert(TABLE_NAME, null, cv);

        Log.d(TAG, "CategoryMat.insertCategory  _id = " + _id);
        return _id;
    }

    //получаем массив строк  с названиями категорий
    public static String[] getArrayCategoryMatNames(SQLiteDatabase db) {
        Log.i(TAG, " ==++== CategoryMat.getArrayCategoryMatNames ... ");

        String select = " SELECT " + _ID + " , " + CATEGORY_MAT_NAME + " FROM " + TABLE_NAME;
        Cursor  cursor = db.rawQuery(select, null);

        String[] categoryNames = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            categoryNames[position] = cursor.getString(cursor.getColumnIndex(CATEGORY_MAT_NAME));
        }
        Log.i(TAG, " ==++== CategoryMat... Count = " + cursor.getCount());
        cursor.close();
        return categoryNames;
    }

    //получаем boolean массив с отметкой - есть ли такая позиция категории в смете
    public static  boolean[] getArrayCategoryMatChecked(
            SQLiteDatabase db, long file_id, String[] categoryNames) {
        Log.i(TAG, " ==++== CategoryMat.getArrayCategoryMatChecked ...  categoryNames.length = " +
                categoryNames.length);

        String[] catMatNamesFW = FM.getArrayCategory(db, file_id);
        boolean[] categoryChacked = new boolean[categoryNames.length];

        for (int i = 0; i<categoryNames.length; i++)
            for (String s : catMatNamesFW) {
                Log.i(TAG, "categoryNames[i] = "+ categoryNames[i] + " catMatNamesFW = " + s);
                if (categoryNames[i].equals(s)) {
                    categoryChacked[i] = true;
                    //если есть совпадение, прекращаем перебор
                    break;
                }
            }
        Log.i(TAG, " ==++== CategoryMat categoryChacked.length  = "+ categoryChacked.length);
        return categoryChacked;
    }

}
