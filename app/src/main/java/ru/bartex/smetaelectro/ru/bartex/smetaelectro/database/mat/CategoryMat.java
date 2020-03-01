package ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.CategoryWork;

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
}
