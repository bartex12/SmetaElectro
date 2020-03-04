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

    

    //получаем курсор с названиями  материалов
    public Cursor getNamesAllTypes(String table) {
        Log.i(TAG, "TableControllerSmeta.getNamesAllTypes ... ");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        String names = "";

        switch (table) {
            case Mat.TABLE_NAME:
                names = " SELECT " + Mat._ID + " , " +
                        Mat.MAT_NAME + " FROM " + Mat.TABLE_NAME;
                break;

            case Work.TABLE_NAME:
                names = " SELECT " + Work._ID + " , " +
                        Work.WORK_NAME + " FROM " + Work.TABLE_NAME;
                break;
        }
        cursor = db.rawQuery(names, null);
        Log.i(TAG, "TableControllerSmeta.getNamesAllTypes cursor.getCount() =  " + cursor.getCount());
        db.close();
        return cursor;
    }

    //получаем курсор с названиями типов работ/материалов
    public Cursor getNamesFromCatId(long id, String table) {
        Log.i(TAG, "TableControllerSmeta.getNamesFromCatId ... ");
        SQLiteDatabase db = this.getReadableDatabase();
        String select ="";
        Cursor cursor =null;
        switch (table) {
            case TypeWork.TABLE_NAME:
                select = " SELECT " + TypeWork._ID + " , " + TypeWork.TYPE_CATEGORY_ID +
                        " , " + TypeWork.TYPE_NAME + " FROM " + TypeWork.TABLE_NAME +
                        " WHERE " + TypeWork.TYPE_CATEGORY_ID  + " = ?" ;
                break;
            case TypeMat.TABLE_NAME:
                select =  " SELECT " + TypeMat._ID + " , " + TypeMat.TYPE_MAT_CATEGORY_ID +
                        " , " + TypeMat.TYPE_MAT_NAME + " FROM " + TypeMat.TABLE_NAME +
                        " WHERE " + TypeMat.TYPE_MAT_CATEGORY_ID  + " = ?" ;
                break;
            case Work.TABLE_NAME:
                select = " SELECT " + Work._ID + " , " + Work.WORK_TYPE_ID + " , " +
                        Work.WORK_NAME + " FROM " + Work.TABLE_NAME +
                        " WHERE " + Work.WORK_TYPE_ID  + " = ?" ;
                break;
            case Mat.TABLE_NAME:
                select = " SELECT " + Mat._ID + " , " +
                        Mat.MAT_NAME + " FROM " + Mat.TABLE_NAME +
                        " WHERE " + Mat.MAT_TYPE_ID  + " = ? " ;
                break;
        }
        cursor = db.rawQuery(select, new String[]{String.valueOf(id)});
        Log.i(TAG, "TableControllerSmeta.getNamesFromCatId cursor.getCount() =  " + cursor.getCount()+
                "  id = " + id);
        return cursor;
    }

    //Добавляем вид материала с левыми параметрами, чобы удалить при отказе пользователя
    public long  insertZero(long id, String table){
        Log.i(TAG, "TableControllerSmeta.insertZero ... ");
        SQLiteDatabase db = getWritableDatabase();
        long currentId = -1;

        switch (table) {
            case CostMat.TABLE_NAME:
                cv = new ContentValues();
                cv.put(CostMat.COST_MAT_ID, id);
                cv.put(CostMat.COST_MAT_UNIT_ID, 1);
                cv.put(CostMat.COST_MAT_COST,0);
                cv.put(CostMat.COST_MAT_NUMBER,1);
                // вставляем строку
                currentId = db.insert(CostMat.TABLE_NAME, null, cv);
                break;

            case CostWork.TABLE_NAME:
                cv = new ContentValues();
                cv.put(CostWork.COST_WORK_ID,id);
                cv.put(CostWork.COST_UNIT_ID, 1);
                cv.put(CostWork.COST_COST,0);
                cv.put(CostWork.COST_NUMBER,1);
                // вставляем строку
                currentId = db.insert(CostWork.TABLE_NAME, null, cv);
                break;
        }
        db.close();
        Log.d(TAG, "TableControllerSmeta.insertZero  currentId = " + currentId);
        return currentId;
    }

    //Добавляем цену материала
    public long  insertCost( long Id, float cost, long unit_id, String table ){
        Log.i(TAG, "TableControllerSmeta.insertCost ... ");
        SQLiteDatabase db = getWritableDatabase();
        long costId =-1;

        switch (table){
            case CostWork.TABLE_NAME:
                cv = new ContentValues();
                cv.put(CostWork.COST_WORK_ID, Id);
                cv.put(CostWork.COST_COST,cost);
                cv.put(CostWork.COST_UNIT_ID,unit_id);
                // вставляем строку
                costId = db.insert(CostWork.TABLE_NAME, null, cv);
                break;

            case CostMat.TABLE_NAME:
                cv = new ContentValues();
                cv.put(CostMat.COST_MAT_ID, Id);
                cv.put(CostMat.COST_MAT_COST,cost);
                cv.put(CostMat.COST_MAT_UNIT_ID,unit_id);
                // вставляем строку
                costId = db.insert(CostMat.TABLE_NAME, null, cv);
                break;

        }
        // закрываем соединение с базой
        db.close();
        Log.d(TAG, "TableControllerSmeta.insertCost costId = " + costId);
        return costId;
    }

    //Добавляем категорию материала
    public long  insertCategory(String catName, String table){
        Log.i(TAG, "TableControllerSmeta.insertCategory ... ");
        SQLiteDatabase db = getWritableDatabase();
        long _id =-1;

        switch (table) {
            case CategoryMat.TABLE_NAME:
                cv = new ContentValues();
                cv.put(CategoryMat.CATEGORY_MAT_NAME,catName);
                // вставляем строку
                _id = db.insert(CategoryMat.TABLE_NAME, null, cv);
                break;

            case CategoryWork.TABLE_NAME:
                cv = new ContentValues();
                cv.put(CategoryWork.CATEGORY_NAME,catName);
                // вставляем строку
                _id = db.insert(CategoryWork.TABLE_NAME, null, cv);
                break;
        }
        // закрываем соединение с базой
        db.close();
        Log.d(TAG, "TableControllerSmeta.insertCategory  _id = " + _id);
        return _id;
    }

    //Добавляем тип материала
    public long  insertTypeCatName(String name, long id, String table ){
        Log.i(TAG, "TableControllerSmeta.insertTypeCatName ... ");
        SQLiteDatabase db = getWritableDatabase();
        long _id =-1;

        switch (table) {
            case TypeWork.TABLE_NAME:
                cv = new ContentValues();
                cv.put(TypeWork.TYPE_NAME,name);
                cv.put(TypeWork.TYPE_CATEGORY_ID,id);
                // вставляем строку
                _id = db.insert(TypeWork.TABLE_NAME, null, cv);
                break;

            case TypeMat.TABLE_NAME:
                cv = new ContentValues();
                cv.put(TypeMat.TYPE_MAT_NAME,name);
                cv.put(TypeMat.TYPE_MAT_CATEGORY_ID,id);
                // вставляем строку
                _id = db.insert(TypeMat.TABLE_NAME, null, cv);
                break;

            case Mat.TABLE_NAME:
                cv = new ContentValues();
                cv.put(Mat.MAT_NAME,name);
                cv.put(Mat.MAT_TYPE_ID, id);
                // вставляем строку
                _id = db.insert(Mat.TABLE_NAME, null, cv);
                break;

            case Work.TABLE_NAME:
                cv = new ContentValues();
                cv.put(Work.WORK_NAME,name);
                cv.put(Work.WORK_TYPE_ID, id);
                // вставляем строку
                _id = db.insert(Work.TABLE_NAME, null, cv);
                break;
        }
        // закрываем соединение с базой
        db.close();
        Log.d(TAG, "TableControllerSmeta.insertTypeCatName  _id = " + _id);
        return _id;
    }

    /**
     * Вставляет строку в таблицу FW
     */
    public long  insertRowInFWFM(long file_id, long work_mat_id, long type_id, long category_id,
                                    float cost, float  count, String unit, float summa, String table){
        Log.i(TAG, "TableControllerSmeta.insertRowInFWFM ... ");
        long ID = -1;
        String fileName="";

        switch (table) {
            case FW.TABLE_NAME:
                //получаем имя файла по его id
                fileName = this.getNameFromId(file_id, FileWork.TABLE_NAME);
                Log.i(TAG, "TableControllerSmeta.insertRowInFWFM fileName =  " + fileName);
                //получаем имя работы по id работы
                String workName = this.getNameFromId(work_mat_id, Work.TABLE_NAME);
                Log.i(TAG, "TableControllerSmeta.insertRowInFWFM workName =  " + workName);
                //получаем имя типа работы по id типа работы
                String typeName = this.getNameFromId(type_id, TypeWork.TABLE_NAME);
                Log.i(TAG, "TableControllerSmeta.insertRowInFWFM typeName =  " + typeName);
                //получаем имя категории работы по id категории работы
                String catName = this.getNameFromId(category_id, CategoryWork.TABLE_NAME);
                Log.i(TAG, "TableControllerSmeta.insertRowInFWFM catName =  " + catName);

                cv = new ContentValues();
                cv.put(FW.FW_FILE_ID,file_id);
                cv.put(FW.FW_FILE_NAME,fileName);
                cv.put(FW.FW_WORK_ID,work_mat_id);
                cv.put(FW.FW_WORK_NAME,workName);
                cv.put(FW.FW_TYPE_ID,type_id);
                cv.put(FW.FW_TYPE_NAME,typeName);
                cv.put(FW.FW_CATEGORY_ID,category_id);
                cv.put(FW.FW_CATEGORY_NAME,catName);
                cv.put(FW.FW_COST,cost);
                cv.put(FW.FW_COUNT,count);
                cv.put(FW.FW_UNIT,unit);
                cv.put(FW.FW_SUMMA,summa);
                //приходится сдесь открывать, так как она закрыта из getNameFromId
                SQLiteDatabase db = getWritableDatabase();
                // вставляем строку
                ID = db.insert(FW.TABLE_NAME, null, cv);
                db.close();
                break;

            case FM.TABLE_NAME:
                //получаем имя файла по его id
                fileName = this.getNameFromId(file_id, FileWork.TABLE_NAME);
                Log.i(TAG, "TableControllerSmeta.insertRowInFWFM fileName =  " + fileName);
                //получаем имя материала  по id материала
                String matName = this.getNameFromId(work_mat_id, Mat.TABLE_NAME);
                Log.i(TAG, "TableControllerSmeta.insertRowInFWFM matName =  " + matName);
                //получаем имя типа материала по id типа материала
                String typeMatName = this.getNameFromId(type_id, TypeMat.TABLE_NAME);
                Log.i(TAG, "TableControllerSmeta.insertRowInFWFM typeMatName =  " + typeMatName);
                //получаем имя категории материала по id категории материала
                String catMatName = this.getNameFromId(category_id, CategoryMat.TABLE_NAME);
                Log.i(TAG, "TableControllerSmeta.insertRowInFWFM catName =  " + catMatName);

                cv = new ContentValues();
                cv.put(FM.FM_FILE_ID,file_id);
                cv.put(FM.FM_FILE_NAME,fileName);
                cv.put(FM.FM_MAT_ID,work_mat_id);
                cv.put(FM.FM_MAT_NAME,matName);
                cv.put(FM.FM_MAT_TYPE_ID,type_id);
                cv.put(FM.FM_MAT_TYPE_NAME,typeMatName);
                cv.put(FM.FM_MAT_CATEGORY_ID,category_id);
                cv.put(FM.FM_MAT_CATEGORY_NAME,catMatName);
                cv.put(FM.FM_MAT_COST,cost);
                cv.put(FM.FM_MAT_COUNT,count);
                cv.put(FM.FM_MAT_UNIT,unit);
                cv.put(FM.FM_MAT_SUMMA,summa);
                //приходится сдесь открывать, так как она закрыта из getNameFromId
                SQLiteDatabase db1 = getWritableDatabase();
                // вставляем строку
                ID = db1.insert(FM.TABLE_NAME, null, cv);
                db1.close();
                break;
        }
        Log.d(TAG, "TableControllerSmeta.insertRowInFWFM  FW._ID = " + ID);
        return ID;
    }

    //получаем количество видов материала с типом type_mat_id
    public int getCountLine(long id, String table){
        Log.i(TAG, "TableControllerSmeta.getCountLine ... ");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =null;
        int count = 0;
        String select = "";

        switch (table) {
            case Work.TABLE_NAME:
                select = " SELECT " + Work._ID + " FROM " + Work.TABLE_NAME +
                        " where " + Work.WORK_TYPE_ID + " =? ";
                break;

            case Mat.TABLE_NAME:
                select = " SELECT " + Mat._ID + " FROM " + Mat.TABLE_NAME +
                        " where " + Mat.MAT_TYPE_ID + " =? ";
                break;

            case FW.TABLE_NAME:
                select = " SELECT " + FW._ID + " FROM " + FW.TABLE_NAME +
                        " where " + FW.FW_WORK_ID + " =? ";
                break;

            case FM.TABLE_NAME:
                select = " SELECT " + FM._ID + " FROM " + FM.TABLE_NAME +
                        " where " + FM.FM_MAT_ID + " =? ";
                break;

            case TypeMat.TABLE_NAME:
                select = " SELECT " + TypeMat._ID + " FROM " + TypeMat.TABLE_NAME +
                        " where " + TypeMat.TYPE_MAT_CATEGORY_ID + " =? ";
                break;

            case TypeWork.TABLE_NAME:
                select =  " SELECT " + TypeWork._ID + " FROM " + TypeWork.TABLE_NAME +
                        " where " + TypeWork.TYPE_CATEGORY_ID + " =? ";
                break;

            case CostWork.TABLE_NAME:
                select =   " SELECT " + CostWork._ID + " FROM " + CostWork.TABLE_NAME +
                        " where " + CostWork.COST_WORK_ID + " =? ";
                break;

            case CostMat.TABLE_NAME:
                select =   " SELECT " + CostMat._ID + " FROM " + CostMat.TABLE_NAME +
                        " where " + CostMat.COST_MAT_ID + " =? ";
                break;
        }
        cursor = db.rawQuery(select, new String[]{String.valueOf(id)});
        if (cursor != null) {
            count = cursor.getCount();
            cursor.close();
        }
        db.close();
        Log.i(TAG, "TableControllerSmeta.getCountLine count = " + count);
        return count;
    }

    //получаем единицы измерения материала с помощью вложенного запроса
    public String getUnitMat(long mat_id){
        Log.i(TAG, "TableControllerSmeta.getUnitMat ... ");
        String unitMatName = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String unit = " SELECT " +  UnitMat.UNIT_MAT_NAME +
                " FROM " + UnitMat.TABLE_NAME  +
                " WHERE " + UnitMat._ID + " IN " +
                "(" + " SELECT " + CostMat.COST_MAT_UNIT_ID +
                " FROM " + CostMat.TABLE_NAME +
                " WHERE " + CostMat.COST_MAT_ID + " = " + mat_id + ")";

        Cursor cursor = db.rawQuery(unit, null);

        Log.d(TAG, "TableControllerSmeta getUnitMat cursor.getCount() = " + cursor.getCount());

        if (cursor.moveToFirst()) {
            // Узнаем индекс  столбца
            int idColumnIndex = cursor.getColumnIndex(UnitMat.UNIT_MAT_NAME);
            // Используем индекс для получения строки или числа
            unitMatName = cursor.getString(idColumnIndex);
            Log.d(TAG, "TableControllerSmeta getUnitMat unitName = " + unitMatName);
        }
        cursor.close();
        db.close();
        return unitMatName;
    }

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

