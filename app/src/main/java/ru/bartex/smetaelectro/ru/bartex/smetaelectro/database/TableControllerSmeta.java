package ru.bartex.smetaelectro.ru.bartex.smetaelectro.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.files.FileWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.CategoryMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.CostMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.FM;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.Mat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.TypeMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.UnitMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.CategoryWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.CostWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.FW;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.TypeWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.Unit;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.Work;

public class TableControllerSmeta extends SmetaOpenHelper {

    public static final String TAG = "33333";
    ContentValues cv;

    public TableControllerSmeta(Context context) {
        super(context);

        cv = new ContentValues();
    }

    //получаем имя работы по её id
    public String getNameFromId(long id, String table) {
        Log.i(TAG, "TableControllerSmeta getNameFromId... ");
        SQLiteDatabase db = this.getReadableDatabase();
        String name = "";
        String currentName = "";
        Cursor cursor = null;
        switch (table) {

            case FileWork.TABLE_NAME:
                cursor = db.query(true,
                        FileWork.TABLE_NAME,
                        null,
                        FileWork._ID + "=" + id,
                        null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    currentName = cursor.getString(cursor.getColumnIndex(FileWork.FILE_NAME));
                }
                break;

            case CategoryWork.TABLE_NAME:
                cursor = db.query(true, CategoryWork.TABLE_NAME,
                        null,
                        CategoryWork._ID + "=" + id,
                        null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    currentName = cursor.getString(cursor.getColumnIndex(CategoryWork.CATEGORY_NAME));
                }
                break;

            case TypeWork.TABLE_NAME:
                cursor = db.query(true, TypeWork.TABLE_NAME,
                        null,
                        TypeWork._ID + "=" + id,
                        null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    currentName = cursor.getString(cursor.getColumnIndex(TypeWork.TYPE_NAME));
                }
                break;

            case Work.TABLE_NAME:
                name = " SELECT " + Work._ID + " , " + Work.WORK_NAME +
                        " FROM " + Work.TABLE_NAME +
                        " WHERE " + Work._ID + " = ?";
                cursor = db.rawQuery(name, new String[]{String.valueOf(id)});
                if (cursor.moveToFirst()) {
                    // Узнаем индекс каждого столбца
                    int idColumnIndex = cursor.getColumnIndex(Work.WORK_NAME);
                    // Используем индекс для получения строки или числа
                    currentName = cursor.getString(idColumnIndex);
                }
                break;

            case CostWork.TABLE_NAME:
                name = " SELECT " + Unit.UNIT_NAME +
                        " FROM " + Unit.TABLE_NAME +
                        " WHERE " + Unit._ID + " IN " +
                        "(" + " SELECT " + CostWork.COST_UNIT_ID +
                        " FROM " + CostWork.TABLE_NAME +
                        " WHERE " + CostWork.COST_WORK_ID + " = " + id + ")";
                cursor = db.rawQuery(name, null);
                if (cursor.moveToFirst()) {
                    // Узнаем индекс  столбца
                    int idColumnIndex = cursor.getColumnIndex(Unit.UNIT_NAME);
                    // Используем индекс для получения строки или числа
                    currentName = cursor.getString(idColumnIndex);
                }
                break;

            case Mat.TABLE_NAME:
                name = " SELECT " + Mat._ID + " , " +  Mat.MAT_NAME +
                        " FROM " + Mat.TABLE_NAME +
                        " WHERE " + Mat._ID  + " = ?" ;
                cursor = db.rawQuery(name, new String[]{String.valueOf(id)});
                if (cursor.moveToFirst()) {
                    // Узнаем индекс каждого столбца
                    int idColumnIndex =  cursor.getColumnIndex(Mat.MAT_NAME);
                    // Используем индекс для получения строки или числа
                    currentName = cursor.getString(idColumnIndex);
                }
                break;

            case TypeMat.TABLE_NAME:
                cursor = db.query(true, TypeMat.TABLE_NAME,
                        null,
                        TypeMat._ID + "=" + id,
                        null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    // Используем индекс для получения строки или числа
                    currentName = cursor.getString(cursor.getColumnIndex(TypeMat.TYPE_MAT_NAME));
                }
                break;

            case CategoryMat.TABLE_NAME:
                cursor = db.query(true, CategoryMat.TABLE_NAME,
                        null,
                        CategoryMat._ID + "=" + id,
                        null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    // Используем индекс для получения строки или числа
                    currentName = cursor.getString(cursor.getColumnIndex(CategoryMat.CATEGORY_MAT_NAME));
                }
                break;

        }
        Log.d(TAG, "getNameFromId currentName = " + currentName);
        if (cursor != null) {
            cursor.close();
        }
        return currentName;
    }

    //********************************
    //здесь остановился 03-03
    //***********************************


    /**
     * обновляем количество и сумму в таблице FM
     */
    public void updateRowInFWFM(
            long file_id, long id, float cost, String unit,
            float count, float summa, String table){
        Log.i(TAG, "TableControllerSmeta.updateRowInFWFM ... ");
        SQLiteDatabase db = this.getWritableDatabase();

        switch (table) {
            case FW.TABLE_NAME:
                cv = new ContentValues();
                cv.put(FW.FW_COST, cost);
                cv.put(FW.FW_UNIT, unit);
                cv.put(FW.FW_COUNT, count);
                cv.put(FW.FW_SUMMA, summa);
                db.update(FW.TABLE_NAME, cv,
                        FW.FW_FILE_ID + " =? " +" AND " + FW.FW_WORK_ID + " =? ",
                        new String[]{String.valueOf(file_id), String.valueOf(id)});
                break;
            case FM.TABLE_NAME:
                cv = new ContentValues();
                cv.put(FM.FM_MAT_COST, cost);
                cv.put(FM.FM_MAT_UNIT, unit);
                cv.put(FM.FM_MAT_COUNT, count);
                cv.put(FM.FM_MAT_SUMMA, summa);
                db.update(FM.TABLE_NAME, cv,
                        FM.FM_FILE_ID + " =? " +" AND " + FM.FM_MAT_ID + " =? ",
                        new String[]{String.valueOf(file_id), String.valueOf(id)});
                break;
        }
        Log.i(TAG, "TableControllerSmeta.updateRowInFWFM - cost =" +
                cost + "  summa = " + summa);
        db.close();
    }

    public boolean isWorkMatInFWFM(long file_id, long mat_id, String table){
        Log.i(TAG, "TableControllerSmeta.isWorkMatInFWFM ... ");
        SQLiteDatabase db = this.getReadableDatabase();
        String select = "";
        Cursor cursor =null;
        switch (table) {
            case FW.TABLE_NAME:
                select = " SELECT " + FW.FW_WORK_NAME + " FROM " + FW.TABLE_NAME +
                        " where " + FW.FW_FILE_ID + " =? " + " and " + FW.FW_WORK_ID + " =? ";
                break;

            case FM.TABLE_NAME:
                select = " SELECT " + FM.FM_MAT_NAME + " FROM " + FM.TABLE_NAME +
                        " where " + FM.FM_FILE_ID + " =? " + " and " + FM.FM_MAT_ID + " =? ";
                break;
        }
        cursor = db.rawQuery(select, new String[]{String.valueOf(file_id),String.valueOf(mat_id)});
        Log.i(TAG, "TableControllerSmeta.isWorkMatInFWFM cursor.getCount() = " + cursor.getCount());
        if (cursor.getCount() != 0) {
            return true;
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return false;
    }

    //обновляем данные файла сметы имя, адрес, описание, дата и время
    public void updateDataFile(long file_id, String name, String adress, String description){

        SQLiteDatabase db = this.getWritableDatabase();

        //заполняем данные для обновления в базе
        ContentValues values = new ContentValues();
        values.put(FileWork.FILE_NAME, name);
        values.put(FileWork.ADRESS, adress);
        values.put(FileWork.DESCRIPTION_OF_FILE, description);

        db.update(FileWork.TABLE_NAME, values,
                FileWork._ID + "=" + file_id, null);
        Log.i(TAG, "TableControllerSmeta.updateDataFile - name =" + name + "  file_id = " + file_id);
    }

}

