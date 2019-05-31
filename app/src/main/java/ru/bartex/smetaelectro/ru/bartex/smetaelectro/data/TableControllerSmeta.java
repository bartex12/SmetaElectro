package ru.bartex.smetaelectro.ru.bartex.smetaelectro.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.bartex.smetaelectro.DataFile;

public class TableControllerSmeta extends SmetaOpenHelper {

    public static final String TAG = "33333";
    ContentValues cv;

    public TableControllerSmeta(Context context) {
        super(context);

        cv = new ContentValues();
    }

    //получаем список объектов DataFile
    public List<DataFile> readFilesData() {
        Log.i(TAG, "TableControllerSmeta.readFilesData ... ");
        List<DataFile> recordsList = new ArrayList<DataFile>();

        String dataQuery = "SELECT  * FROM " + FileWork.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(dataQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DataFile dataFile =  this.getFileDataFromCurcor(cursor);
                //добавляем в список
                recordsList.add(dataFile);

            } while (cursor.moveToNext());
        }
        Log.d(TAG, "TableControllerSmeta readFilesData cursor.getCount() = " + cursor.getCount());
        cursor.close();
        db.close();
        return recordsList;
    }

    //получаем объект DataFile для файла с file_id
    public  DataFile getFileData(long file_id){
        Log.i(TAG, "TableControllerSmeta.getFileData ... ");
        DataFile dataFile = new DataFile();
        String fileName = " SELECT  * FROM " +  FileWork.TABLE_NAME +
                " WHERE " + FileWork._ID  + " = ? " ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(fileName, new String[]{String.valueOf(file_id)});
        if (cursor.moveToFirst()) {
            dataFile = this.getFileDataFromCurcor(cursor);
        }
        Log.d(TAG, "TableControllerSmeta.getFileData cursor.getCount() = " + cursor.getCount());
        cursor.close();
        db.close();
        return dataFile;
    }

    //получаем объект DataFile из курсора
    public  DataFile getFileDataFromCurcor(Cursor cursor){
        // Узнаем индекс каждого столбца и Используем индекс для получения строки
        long id = cursor.getLong(cursor.getColumnIndex(FileWork._ID));
        String currentFileName = cursor.getString(cursor.getColumnIndex(FileWork.FILE_NAME));
        String currentAdress = cursor.getString(cursor.getColumnIndex(FileWork.ADRESS));
        String currentDate = cursor.getString(cursor.getColumnIndex(FileWork.FILE_NAME_DATE));
        String currentTime = cursor.getString(cursor.getColumnIndex(FileWork.FILE_NAME_TIME));
        String currentDescription = cursor.getString(cursor.getColumnIndex(FileWork.DESCRIPTION_OF_FILE));
        //Log.d(TAG, "getFileData currentFileName = " + currentFileName);
        //создаём экземпляр класса DataFile в конструкторе
        DataFile dataFile = new DataFile(id, currentFileName, currentAdress,
                currentDate, currentTime, currentDescription);
        return dataFile;
    }

    //получаем id по имени в зависимости от имени таблицы
    public long getIdFromName(String name, String tableName){
        Log.i(TAG, "TableControllerSmeta.getIdFromName ... ");
        long currentID = -1;
        int idColumnIndex = -1;
        Cursor cursor = null;
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = this.getReadableDatabase();

        switch (tableName){
            case FileWork.TABLE_NAME:
                Log.d(TAG, "getIdFromName case FileWork.TABLE_NAME...");
                cursor = db.query(
                        FileWork.TABLE_NAME,   // таблица
                        new String[]{FileWork._ID},            // столбцы
                        FileWork.FILE_NAME + "=?",                  // столбцы для условия WHERE
                        new String[]{name},                  // значения для условия WHERE
                        null,                  // Don't group the rows
                        null,                  // Don't filter by row groups
                        null);                   // порядок сортировки
                if (cursor.moveToFirst()) {
                    // Узнаем индекс  столбца FileWork._ID
                    idColumnIndex = cursor.getColumnIndex(FileWork._ID);
                    // Используем индекс для получения id
                    currentID = cursor.getLong(idColumnIndex);
                }
                break;

            case CategoryWork.TABLE_NAME:
                Log.d(TAG, "getIdFromName case CategoryWork.TABLE_NAME...");
                cursor = db.query(
                        CategoryWork.TABLE_NAME,   // таблица
                        new String[]{CategoryWork._ID},            // столбцы
                        CategoryWork.CATEGORY_NAME + "=?",                  // столбцы для условия WHERE
                        new String[]{name},                  // значения для условия WHERE
                        null,                  // Don't group the rows
                        null,                  // Don't filter by row groups
                        null);                   // порядок сортировки
                if (cursor.moveToFirst()) {
                    // Узнаем индекс  столбца FileWork._ID
                    idColumnIndex = cursor.getColumnIndex(CategoryWork._ID);
                    currentID = cursor.getLong(idColumnIndex);
                }
                break;

            case CategoryMat.TABLE_NAME:
                Log.d(TAG, "getIdFromName case CategoryMat.TABLE_NAME...");
                cursor = db.query(
                        CategoryMat.TABLE_NAME,   // таблица
                        new String[]{CategoryMat._ID},            // столбцы
                        CategoryMat.CATEGORY_MAT_NAME + "=?",                  // столбцы для условия WHERE
                        new String[]{name},                  // значения для условия WHERE
                        null,                  // Don't group the rows
                        null,                  // Don't filter by row groups
                        null);                   // порядок сортировки
                if (cursor.moveToFirst()) {
                    // Узнаем индекс  столбца FileWork._ID
                    idColumnIndex = cursor.getColumnIndex(CategoryMat._ID);
                    currentID = cursor.getLong(idColumnIndex);
                }
                break;

            case TypeWork.TABLE_NAME:
                Log.d(TAG, "getIdFromName case TypeWork.TABLE_NAME...");
                cursor = db.query(
                        TypeWork.TABLE_NAME,   // таблица
                        new String[]{TypeWork._ID},            // столбцы
                        TypeWork.TYPE_NAME + "=?",                  // столбцы для условия WHERE
                        new String[]{name},                  // значения для условия WHERE
                        null,                  // Don't group the rows
                        null,                  // Don't filter by row groups
                        null);                   // порядок сортировки
                if (cursor.moveToFirst()) {
                    // Узнаем индекс  столбца FileWork._ID
                    idColumnIndex = cursor.getColumnIndex(TypeWork._ID);
                    currentID = cursor.getLong(idColumnIndex);
                }
                break;

            case TypeMat.TABLE_NAME:
                Log.d(TAG, "getIdFromName case TypeMat.TABLE_NAME...");
                cursor = db.query(
                        TypeMat.TABLE_NAME,   // таблица
                        new String[]{TypeMat._ID},            // столбцы
                        TypeMat.TYPE_MAT_NAME + "=?",   // столбцы для условия WHERE
                        new String[]{name},                  // значения для условия WHERE
                        null,                  // Don't group the rows
                        null,                  // Don't filter by row groups
                        null);                   // порядок сортировки
                if (cursor.moveToFirst()) {
                    // Узнаем индекс  столбца FileWork._ID
                    idColumnIndex = cursor.getColumnIndex(TypeMat._ID);
                    currentID = cursor.getLong(idColumnIndex);
                }
                break;

            case Work.TABLE_NAME:
                Log.d(TAG, "getIdFromName case Work.TABLE_NAME...");
                cursor = db.query(
                        Work.TABLE_NAME,   // таблица
                        new String[]{Work._ID},            // столбцы
                        Work.WORK_NAME + "=?",                  // столбцы для условия WHERE
                        new String[]{name},                  // значения для условия WHERE
                        null,                  // Don't group the rows
                        null,                  // Don't filter by row groups
                        null);                   // порядок сортировки
                if (cursor.moveToFirst()) {
                    // Узнаем индекс  столбца FileWork._ID
                    idColumnIndex = cursor.getColumnIndex(Work._ID);
                    currentID = cursor.getLong(idColumnIndex);
                }
                break;

            case Mat.TABLE_NAME:
                Log.d(TAG, "getIdFromName case Mat.TABLE_NAME...");
                cursor = db.query(
                        Mat.TABLE_NAME,   // таблица
                        new String[]{Unit._ID},            // столбцы
                        Mat.MAT_NAME + "=?",                  // столбцы для условия WHERE
                        new String[]{name},                  // значения для условия WHERE
                        null,                  // Don't group the rows
                        null,                  // Don't filter by row groups
                        null);                   // порядок сортировки
                if (cursor.moveToFirst()) {
                    // Узнаем индекс  столбца FileWork._ID
                    idColumnIndex = cursor.getColumnIndex(Mat._ID);
                    currentID = cursor.getLong(idColumnIndex);
                }
                break;

            case Unit.TABLE_NAME:
                Log.d(TAG, "getIdFromName case Unit.TABLE_NAME...");
                cursor = db.query(
                        Unit.TABLE_NAME,   // таблица
                        new String[]{Unit._ID},            // столбцы
                        Unit.UNIT_NAME + "=?",                  // столбцы для условия WHERE
                        new String[]{name},                  // значения для условия WHERE
                        null,                  // Don't group the rows
                        null,                  // Don't filter by row groups
                        null);                   // порядок сортировки
                if (cursor.moveToFirst()) {
                    // Узнаем индекс  столбца FileWork._ID
                    idColumnIndex = cursor.getColumnIndex(Unit._ID);
                    currentID = cursor.getLong(idColumnIndex);
                }
                break;

            case UnitMat.TABLE_NAME:
                Log.d(TAG, "getIdFromName case UnitMat.TABLE_NAME...");
                cursor = db.query(
                        UnitMat.TABLE_NAME,   // таблица
                        new String[]{UnitMat._ID},            // столбцы
                        UnitMat.UNIT_MAT_NAME + "=?",                  // столбцы для условия WHERE
                        new String[]{name},                  // значения для условия WHERE
                        null,                  // Don't group the rows
                        null,                  // Don't filter by row groups
                        null);                   // порядок сортировки
                if (cursor.moveToFirst()) {
                    // Узнаем индекс  столбца FileWork._ID
                    idColumnIndex = cursor.getColumnIndex(UnitMat._ID);
                    currentID = cursor.getLong(idColumnIndex);
                }
                break;
        }
        // Используем индекс для получения id
       // currentID = cursor.getLong(idColumnIndex);
        Log.d(TAG, "getIdFromName currentID = " + currentID);

        if (cursor!=null){
            cursor.close();
        }
        db.close();
        return currentID;
    }

    //получаем имя работы по её id
    public String getNameFromId(long id, String table){
        Log.i(TAG, "TableControllerSmeta getNameFromId... ");
        SQLiteDatabase db = this.getReadableDatabase();
        String name = "";
        String currentName = "";
        Cursor cursor = null;
        switch (table){

            case FileWork.TABLE_NAME:
                cursor = db.query(true, FileWork.TABLE_NAME,
                        null,
                        FileWork._ID + "=" + id,
                        null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    currentName = cursor.getString(cursor.getColumnIndex(FileWork.FILE_NAME));
                }
                break;

            case Work.TABLE_NAME:
                name = " SELECT " + Work._ID + " , " +  Work.WORK_NAME +
                        " FROM " + Work.TABLE_NAME +
                        " WHERE " + Work._ID  + " = ?" ;
                cursor = db.rawQuery(name, new String[]{String.valueOf(id)});
                if (cursor.moveToFirst()) {
                    // Узнаем индекс каждого столбца
                    int idColumnIndex = cursor.getColumnIndex(Work.WORK_NAME);
                    // Используем индекс для получения строки или числа
                    currentName = cursor.getString(idColumnIndex);
                }
                break;

            case CostWork.TABLE_NAME:
                name = " SELECT " +  Unit.UNIT_NAME +
                        " FROM " + Unit.TABLE_NAME  +
                        " WHERE " + Unit._ID + " IN " +
                        "(" + " SELECT " + CostWork.COST_UNIT_ID +
                        " FROM " + CostWork.TABLE_NAME +
                        " WHERE " + CostWork.COST_WORK_ID  + " = " +  String.valueOf(id) + ")";
                cursor = db.rawQuery(name, null);
                if (cursor.moveToFirst()) {
                    // Узнаем индекс  столбца
                    int idColumnIndex = cursor.getColumnIndex(Unit.UNIT_NAME);
                    // Используем индекс для получения строки или числа
                    currentName = cursor.getString(idColumnIndex);
                }
                break;
        }

        Log.d(TAG, "getNameFromId currentName = " + currentName);
        if (cursor!=null){
            cursor.close();
        }
        db.close();
        return currentName;
    }

    //удаляем название сметы из таблицы FileWork и все строки из FW по id сметы fileId
    public void deleteObject(long id, String tableName ) {
        Log.i(TAG, "TableControllerSmeta.deleteObject ... ");
        SQLiteDatabase db = this.getWritableDatabase();

        switch (tableName){
            case FileWork.TABLE_NAME:
                Log.i(TAG, "TableControllerSmeta.deleteObject case FileWork ");
                db.delete(FW.TABLE_NAME, FW.FW_FILE_ID + " =? " ,
                        new String[]{String.valueOf(id)});
                db.delete(FileWork.TABLE_NAME, FileWork._ID + " =? " ,
                        new String[]{String.valueOf(id)});
                break;

            case CategoryWork.TABLE_NAME:
                Log.i(TAG, "TableControllerSmeta.deleteObject case CategoryWork ");
                db.delete(CategoryWork.TABLE_NAME, CategoryWork._ID + " =? " ,
                        new String[]{String.valueOf(id)});
                break;

            case TypeWork.TABLE_NAME:
                Log.i(TAG, "TableControllerSmeta.deleteObject case TypeWork ");
                db.delete(TypeWork.TABLE_NAME, TypeWork._ID + " =? ",
                        new String[]{String.valueOf(id)});
                break;

            case Work.TABLE_NAME:
                Log.i(TAG, "TableControllerSmeta.deleteObject  case Work ");
                db.delete(Work.TABLE_NAME, Work._ID + " =? ",
                        new String[]{String.valueOf(id)});
                break;

            case CostWork.TABLE_NAME:
                Log.i(TAG, "TableControllerSmeta.deleteObject case CostWork ");
                db.delete(CostWork.TABLE_NAME, CostWork.COST_WORK_ID + " =? ",
                        new String[]{String.valueOf(id)});
                break;

            case CostMat.TABLE_NAME:
                Log.i(TAG, "TableControllerSmeta.deleteObject case CostMat ");
                db.delete(CostMat.TABLE_NAME, CostMat.COST_MAT_ID + " =? ",
                        new String[]{String.valueOf(id)});
                break;

            case CategoryMat.TABLE_NAME:
                Log.i(TAG, "TableControllerSmeta.deleteObject  case CategoryMat ");
                db.delete(CategoryMat.TABLE_NAME, CategoryMat._ID + " =? " ,
                        new String[]{String.valueOf(id)});
                break;

            case TypeMat.TABLE_NAME:
                Log.i(TAG, "TableControllerSmeta.deleteObject case TypeMat ");
                db.delete(TypeMat.TABLE_NAME, TypeMat._ID + " =? " ,
                        new String[]{String.valueOf(id)});
                break;

            case Mat.TABLE_NAME:
                Log.i(TAG, "TableControllerSmeta.deleteObject  case Mat ");
                db.delete(Mat.TABLE_NAME, Mat._ID + " =? " ,
                        new String[]{String.valueOf(id)});
                break;
        }
        db.close();
    }

    // Добавляем имя и другие параметры сметы в таблицу FileWork (из активности SmetaNewName)
    public long addFile(String fileName, String adress, String description) {
        Log.i(TAG, "TableControllerSmeta.addFile ... ");
        SQLiteDatabase db = getWritableDatabase();

        //получаем дату и время в нужном для базы данных формате
        String dateFormat = this.getDateString();
        String timeFormat = this.getTimeString();

        cv = new ContentValues();
        cv.put(FileWork.FILE_NAME, fileName);
        cv.put(FileWork.ADRESS, adress);
        cv.put(FileWork.FILE_NAME_DATE, dateFormat);
        cv.put(FileWork.FILE_NAME_TIME, timeFormat);
        cv.put(FileWork.DESCRIPTION_OF_FILE, description);
        // вставляем строку
        long ID = db.insert(FileWork.TABLE_NAME, null, cv);
        // закрываем соединение с базой
        db.close();
        Log.d(TAG, "MyDatabaseHelper.createDefaultFile...  file1_id = " + ID);
        return ID;
    }

    //обновляем данные файла сметы имя, адрес, описание, дата и время
    public void updateFileData(long file_id, String name, String adress, String description){
        Log.i(TAG, "TableControllerSmeta.updateFileData ... ");
        SQLiteDatabase db = this.getWritableDatabase();

        //заполняем данные для обновления в базе
        cv = new ContentValues();
        cv.put(FileWork.FILE_NAME, name);
        cv.put(FileWork.ADRESS, adress);
        cv.put(FileWork.DESCRIPTION_OF_FILE, description);

        db.update(FileWork.TABLE_NAME, cv,FileWork._ID + "=" + file_id, null);
        Log.i(TAG, "TableControllerSmeta.updateFileData - name =" + name + "  file_id = " + file_id);
        db.close();
    }

    //Обновляем данные по категории работ
    public void updateData(long id, String name, String description, String tableName){
        Log.i(TAG, "TableControllerSmeta.updateData ...");
        SQLiteDatabase db = this.getWritableDatabase();

        switch (tableName){
            case CategoryWork.TABLE_NAME:
                Log.i(TAG, "TableControllerSmeta.updateData case CategoryWork");
                //заполняем данные для обновления в базе
                cv = new ContentValues();
                cv.put(CategoryWork.CATEGORY_NAME, name);
                cv.put(CategoryWork.CATEGORY_DESCRIPTION, description);
                db.update(CategoryWork.TABLE_NAME, cv,
                        CategoryWork._ID + "=" + id, null);
                break;

            case TypeWork.TABLE_NAME:
                Log.i(TAG, "TableControllerSmeta.updateData case TypeWork");
                //заполняем данные для обновления в базе
                cv = new ContentValues();
                cv.put(TypeWork.TYPE_NAME, name);
                cv.put(TypeWork.TYPE_DESCRIPTION, description);
                db.update(TypeWork.TABLE_NAME, cv,
                        TypeWork._ID + "=" + id, null);
                break;

            case Work.TABLE_NAME:
                Log.i(TAG, "TableControllerSmeta.updateData case Work");
                //заполняем данные для обновления в базе
               cv = new ContentValues();
                cv.put(Work.WORK_NAME, name);
                cv.put(Work.WORK_DESCRIPTION, description);
                db.update(Work.TABLE_NAME, cv,
                        Work._ID + "=" + id, null);
                break;

            case CategoryMat.TABLE_NAME:
                Log.i(TAG, "TableControllerSmeta.updateData case CategoryMat");
                cv = new ContentValues();
                cv.put(CategoryMat.CATEGORY_MAT_NAME, name);
                cv.put(CategoryMat.CATEGORY_MAT_DESCRIPTION, description);
                db.update(CategoryMat.TABLE_NAME, cv,
                        CategoryMat._ID + "=" + id, null);
                break;

            case TypeMat.TABLE_NAME:
                Log.i(TAG, "TableControllerSmeta.updateData case TypeMat");
                cv = new ContentValues();
                cv.put(TypeMat.TYPE_MAT_NAME, name);
                cv.put(TypeMat.TYPE_MAT_DESCRIPTION, description);
                db.update(TypeMat.TABLE_NAME, cv,
                        TypeMat._ID + "=" + id, null);
                break;

            case Mat.TABLE_NAME:
                Log.i(TAG, "TableControllerSmeta.updateData case Mat");
                //заполняем данные для обновления в базе
                cv = new ContentValues();
                cv.put(Mat.MAT_NAME, name);
                cv.put(Mat.MAT_DESCRIPTION, description);
                db.update(Mat.TABLE_NAME, cv,
                        Mat._ID + "=" + id, null);
                break;
        }
        Log.i(TAG, "TableControllerSmeta.updateData - name =" + name + "  id = " + id);
        db.close();
    }

    //обновляем цену работы
    public void updateCost(long id, float cost, long unit_Id, String table){

        Log.i(TAG, "TableControllerSmeta.updateCost ...");
        SQLiteDatabase db = this.getWritableDatabase();
        int countLine= 0;
        switch (table){
            case CostWork.TABLE_NAME:
                cv = new ContentValues();
                cv.put(CostWork.COST_COST, cost);
                cv.put(CostWork.COST_UNIT_ID, unit_Id);
                cv.put(CostWork.COST_NUMBER, 1);
                countLine = db.update(CostWork.TABLE_NAME, cv,
                        CostWork.COST_WORK_ID + "=" + id, null);
                break;

            case CostMat.TABLE_NAME:
                cv = new ContentValues();
                cv.put(CostMat.COST_MAT_COST, cost);
                cv.put(CostMat.COST_MAT_UNIT_ID, unit_Id);
                cv.put(CostMat.COST_MAT_NUMBER, 1);
                countLine = db.update(CostMat.TABLE_NAME, cv,
                        CostMat.COST_MAT_ID + "=" + id, null);
                break;
        }
        Log.i(TAG, "SmetaOpenHelper.updateWorkCost - cost =" + cost + "  countLine = " + countLine);
        db.close();
    }

}