package ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import ru.bartex.smetaelectro.R;

public class Mat {
    public static final String TAG = "33333";

    public Mat(){
        //пустой конструктор
    }
    public final static String TABLE_NAME = "Mat";

    public final static String _ID = BaseColumns._ID;
    public final static String MAT_NAME = "MAT_NAME";
    public final static String MAT_DESCRIPTION= "MAT_DESCRIPTION";
    public final static String MAT_TYPE_ID = "MAT_TYPE_ID";

    //создание таблицы
    public static void createTable(SQLiteDatabase db, Context fContext){
        // Строка для создания таблицы конкретных материалов Mat
        String SQL_CREATE_TAB_MAT = "CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MAT_TYPE_ID + " INTEGER NOT NULL, "
                + MAT_NAME + " TEXT NOT NULL, "
                + MAT_DESCRIPTION + " TEXT NOT NULL DEFAULT 'Без описания');";
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TAB_MAT);
        Log.d(TAG, "SmetaOpenHelper - onCreate- создание таблицы Mat");
        // Если файлов в базе нет, вносим записи названия  материалов (добавление из программы)
        createDefaultMat(db, fContext);
    }

    //создаём работы из ресурсов и получаем COLUMN_TYPE_CATEGORY_ID из  this.getArrayCategoryId()
    private static void createDefaultMat(SQLiteDatabase db, Context fContext) {

        Log.i(TAG, "SmetaOpenHelper.createDefaultMat...");
        // Добавляем записи в таблицу
        ContentValues values = new ContentValues();
        // Получим массив строк из ресурсов
        Resources res = fContext.getResources();
        String[] mat_name_type_kabeli = res.getStringArray(R.array.mat_name_type_kabeli);
        String[] mat_name_type_kabel_kanali = res.getStringArray(R.array.mat_name_type_kabel_kanali);
        String[] mat_name_type_korobki = res.getStringArray(R.array.mat_name_type_korobki);
        String[] mat_name_type_vremianki = res.getStringArray(R.array.mat_name_type_vremianki);
        String[] mat_name_type_rozetki = res.getStringArray(R.array.mat_name_type_rozetki);
        String[] mat_name_type_shchity = res.getStringArray(R.array.mat_name_type_shchity);
        String[] mat_name_type_led = res.getStringArray(R.array.mat_name_type_led);
        String[] mat_name_type_svetilniki = res.getStringArray(R.array.mat_name_type_svetilniki);
        String[] mat_name_type_bury = res.getStringArray(R.array.mat_name_type_bury);
        String[] mat_name_type_krepez = res.getStringArray(R.array.mat_name_type_krepez);
        String[] mat_name_type_sypuchie = res.getStringArray(R.array.mat_name_type_sypuchie);
        String[] mat_name_type_vspomogat = res.getStringArray(R.array.mat_name_type_vspomogat);
        String[] mat_name_type_instr = res.getStringArray(R.array.mat_name_type_instr);
        String[] mat_name_type_obschestroy = res.getStringArray(R.array.mat_name_type_obschestroy);

        //получаем массив type_id  таблицы типов Types
        int[] type_id = getArrayTypeIdMat(db);
        // проходим через массив и вставляем записи в таблицу
        int ii = 0; //mat_name_type_kabeli
        for (String s : mat_name_type_kabeli) {
            InsertMat(db, values, type_id[ii], s);
        }
        ii = 1; //mat_name_type_kabel_kanali
        for (String s : mat_name_type_kabel_kanali) {
            InsertMat(db, values, type_id[ii], s);
        }
        ii = 2; //mat_name_type_korobki
        for (String s : mat_name_type_korobki) {
            InsertMat(db, values, type_id[ii], s);
        }
        ii = 3; //mat_name_type_vremianki
        for (String s : mat_name_type_vremianki) {
            InsertMat(db, values, type_id[ii], s);
        }
        ii = 4; //mat_name_type_rozetki
        for (String s : mat_name_type_rozetki) {
            InsertMat(db, values, type_id[ii], s);
        }
        ii = 5; //mat_name_type_shchity
        for (String s : mat_name_type_shchity) {
            InsertMat(db, values, type_id[ii], s);
        }
        ii = 6; //mat_name_type_led
        for (String s : mat_name_type_led) {
            InsertMat(db, values, type_id[ii], s);
        }
        ii = 7; //mat_name_type_svetilniki
        for (String s : mat_name_type_svetilniki) {
            InsertMat(db, values, type_id[ii], s);
        }
        ii = 8; //mat_name_type_bury
        for (String s : mat_name_type_bury) {
            InsertMat(db, values, type_id[ii], s);
        }
        ii = 9; //mat_name_type_krepez
        for (String s : mat_name_type_krepez) {
            InsertMat(db, values, type_id[ii], s);
        }
        ii = 10; //mat_name_type_sypuchie
        for (String s : mat_name_type_sypuchie) {
            InsertMat(db, values, type_id[ii], s);
        }
        ii = 11; //mat_name_type_vspomogat
        for (String s : mat_name_type_vspomogat) {
            InsertMat(db, values, type_id[ii], s);
        }
        ii = 12; //mat_name_type_instr
        for (String s : mat_name_type_instr) {
            InsertMat(db, values, type_id[ii], s);
        }
        ii = 13; //mat_name_type_obschestroy
        for (String s : mat_name_type_obschestroy) {
            InsertMat(db, values, type_id[ii], s);
        }
        Log.d(TAG, "createDefaultMat type_id.length = " +
                type_id.length + " mat_name_type_kabeli.length = " + mat_name_type_kabeli.length);
    }

    //получаем курсор с Mat_Type_id  и делаем массив id
    private static int[] getArrayTypeIdMat(SQLiteDatabase db) {

        Log.i(TAG, "SmetaOpenHelper.getArrayTypeIdMat ... ");
        String typeIdMat = " SELECT " + TypeMat._ID  + " FROM " + TypeMat.TABLE_NAME;
        Cursor cursor = db.rawQuery(typeIdMat, null);
        Log.i(TAG, "SmetaOpenHelper.getArrayTypeIdMat cursor.getCount() = " + cursor.getCount());
        int[] type_id_mat = new int[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            type_id_mat[position] = cursor.getInt(cursor.getColumnIndex(TypeMat._ID));
        }
        cursor.close();
        return type_id_mat;
    }

    private static void InsertMat(SQLiteDatabase db, ContentValues values,
                           int type_id, String work_name){
        values.put(MAT_TYPE_ID, type_id);
        values.put(MAT_NAME, work_name);
        db.insert(TABLE_NAME, null, values);
    }
}