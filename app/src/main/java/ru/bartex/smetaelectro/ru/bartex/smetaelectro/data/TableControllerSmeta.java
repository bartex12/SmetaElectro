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

    //вывод в лог всех строк CostWork
    public void displayTable(String table) {
        Log.i(TAG, "TableControllerSmeta.displayTable ...");
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =null;

        switch (table){
            case CostWork.TABLE_NAME:
                Log.i(TAG, "TableControllerSmeta.displayTable case CostWork");
                // Зададим условие для выборки - список столбцов
                String[] projectionCostWork = {
                        CostWork._ID,
                        CostWork.COST_WORK_ID,
                        CostWork.COST_UNIT_ID,
                        CostWork.COST_COST,
                        CostWork.COST_NUMBER};
                // Делаем запрос
                cursor = db.query(
                        CostWork.TABLE_NAME,   // таблица
                        projectionCostWork,            // столбцы
                        null,                  // столбцы для условия WHERE
                        null,                  // значения для условия WHERE
                        null,                  // Don't group the rows
                        null,                  // Don't filter by row groups
                        null);                   // порядок сортировки
                    // Проходим через все ряды в таблице CostWork
                    while (cursor.moveToNext()) {
                        // Используем индекс для получения строки или числа
                        int currentID = cursor.getInt(
                                cursor.getColumnIndex(CostWork._ID));
                        int current_WORK_ID = cursor.getInt(
                                cursor.getColumnIndex(CostWork.COST_WORK_ID));
                        String current_UNIT = cursor.getString(
                                cursor.getColumnIndex(CostWork.COST_UNIT_ID));
                        float current_COST = cursor.getFloat(
                                cursor.getColumnIndex(CostWork.COST_COST));
                        int current_NUMBER = cursor.getInt(
                                cursor.getColumnIndex(CostWork.COST_NUMBER));
                        // Выводим построчно значения каждого столбца
                        Log.d(TAG, "\n" + "ID = " + currentID + " - " +
                                " WORK_ID = " + current_WORK_ID + " - " +
                                " UNIT = " + current_UNIT + " - " +
                                " COST = " + current_COST + " - " +
                                " NUMBER = " + current_NUMBER);
                    }
                break;

            case CostMat.TABLE_NAME:
                Log.i(TAG, "TableControllerSmeta.displayTable case CostMat");
            // Зададим условие для выборки - список столбцов
                String[] projectionCostMat = {
                        CostMat._ID,
                        CostMat.COST_MAT_ID,
                        CostMat.COST_MAT_UNIT_ID,
                        CostMat.COST_MAT_COST,
                        CostMat.COST_MAT_NUMBER};
                // Делаем запрос
                cursor = db.query(
                        CostMat.TABLE_NAME,   // таблица
                        projectionCostMat,            // столбцы
                        null,                  // столбцы для условия WHERE
                        null,                  // значения для условия WHERE
                        null,                  // Don't group the rows
                        null,                  // Don't filter by row groups
                        null);                   // порядок сортировки
                    // Проходим через все ряды в таблице CostWork
                    while (cursor.moveToNext()) {
                        // Используем индекс для получения строки или числа
                        int currentID = cursor.getInt(
                                cursor.getColumnIndex(CostMat._ID));
                        int current_WORK_ID = cursor.getInt(
                                cursor.getColumnIndex(CostMat.COST_MAT_ID));
                        String current_UNIT = cursor.getString(
                                cursor.getColumnIndex(CostMat.COST_MAT_UNIT_ID));
                        float current_COST = cursor.getFloat(
                                cursor.getColumnIndex(CostMat.COST_MAT_COST));
                        int current_NUMBER = cursor.getInt(
                                cursor.getColumnIndex(CostMat.COST_MAT_NUMBER));
                        // Выводим построчно значения каждого столбца
                        Log.d(TAG, "\n" + "ID = " + currentID + " - " +
                                " MAT_ID = " + current_WORK_ID + " - " +
                                " UNIT = " + current_UNIT + " - " +
                                " MAT_COST = " + current_COST + " - " +
                                " MAT_NUMBER = " + current_NUMBER);
                    }
                break;

            case FW.TABLE_NAME:
                Log.i(TAG, "TableControllerSmeta.displayTable case FW");
            // Зададим условие для выборки - список столбцов
                String[] projectionFW = {
                        FW._ID,
                        FW.FW_FILE_ID,
                        FW.FW_FILE_NAME,
                        FW.FW_WORK_ID,
                        FW.FW_WORK_NAME,
                        FW.FW_TYPE_ID,
                        FW.FW_TYPE_NAME,
                        FW.FW_CATEGORY_ID,
                        FW.FW_CATEGORY_NAME,
                        FW.FW_COST,
                        FW.FW_COUNT,
                        FW.FW_UNIT,
                        FW.FW_SUMMA};
                // Делаем запрос
                cursor = db.query(
                        FW.TABLE_NAME,   // таблица
                        projectionFW,            // столбцы
                        null,                  // столбцы для условия WHERE
                        null,                  // значения для условия WHERE
                        null,                  // Don't group the rows
                        null,                  // Don't filter by row groups
                        null);                   // порядок сортировки
                    // Проходим через все ряды в таблице CostWork
                    while (cursor.moveToNext()) {
                        // Используем индекс для получения строки или числа
                        int currentID = cursor.getInt(
                                cursor.getColumnIndex(FW._ID));
                        int current_FILE_ID = cursor.getInt(
                                cursor.getColumnIndex(FW.FW_FILE_ID));
                        String current_FILE_NAME = cursor.getString(
                                cursor.getColumnIndex(FW.FW_FILE_NAME));
                        int current_WORK_ID = cursor.getInt(
                                cursor.getColumnIndex(FW.FW_WORK_ID));
                        String current_WORK_NAME = cursor.getString(
                                cursor.getColumnIndex(FW.FW_WORK_NAME));
                        int current_TYPE_ID = cursor.getInt(
                                cursor.getColumnIndex(FW.FW_TYPE_ID));
                        String current_TYPE_NAME = cursor.getString(
                                cursor.getColumnIndex(FW.FW_TYPE_NAME));
                        int current_CATEGORY_ID = cursor.getInt(
                                cursor.getColumnIndex(FW.FW_CATEGORY_ID));
                        String current_CATEGORY_NAME = cursor.getString(
                                cursor.getColumnIndex(FW.FW_CATEGORY_NAME));
                        float current_COST = cursor.getFloat(
                                cursor.getColumnIndex(FW.FW_COST));
                        int current_COUNT = cursor.getInt(
                                cursor.getColumnIndex(FW.FW_COUNT));
                        String current_UNIT = cursor.getString(
                                cursor.getColumnIndex(FW.FW_UNIT));
                        float current_SUMMA = cursor.getFloat(
                                cursor.getColumnIndex(FW.FW_SUMMA));
                        // Выводим построчно значения каждого столбца
                        Log.d(TAG, "\n" + "ID = " + currentID + "/" +
                                " FILE_ID = " + current_FILE_ID + "/" +
                                " FILE_NAME = " + current_FILE_NAME + "/" +
                                " WORK_ID = " + current_WORK_ID + "/" +
                                " WORK_NAME = " + current_WORK_NAME + "/" +
                                " TYPE_ID = " + current_TYPE_ID + "/" +
                                " TYPE_NAME = " + current_TYPE_NAME + "/" +
                                " CAT_ID = " + current_CATEGORY_ID + "/" +
                                " CAT_NAME = " + current_CATEGORY_NAME + "/" +
                                " COST = " + current_COST + "/" +
                                " COUNT = " + current_COUNT + "/" +
                                " UNIT = " + current_UNIT + "/" +
                                " SUMMA = " + current_SUMMA);
                }
                break;

            case FM.TABLE_NAME:
                Log.i(TAG, "TableControllerSmeta.displayTable case FM");
                // Зададим условие для выборки - список столбцов
                String[] projectionFM = {
                        FM._ID,
                        FM.FM_FILE_ID,
                        FM.FM_FILE_NAME,
                        FM.FM_MAT_ID,
                        FM.FM_MAT_NAME,
                        FM.FM_MAT_TYPE_ID,
                        FM.FM_MAT_TYPE_NAME,
                        FM.FM_MAT_CATEGORY_ID,
                        FM.FM_MAT_CATEGORY_NAME,
                        FM.FM_MAT_COST,
                        FM.FM_MAT_COUNT,
                        FM.FM_MAT_UNIT,
                        FM.FM_MAT_SUMMA};
                // Делаем запрос
                cursor = db.query(
                        FM.TABLE_NAME,   // таблица
                        projectionFM,            // столбцы
                        null,                  // столбцы для условия WHERE
                        null,                  // значения для условия WHERE
                        null,                  // Don't group the rows
                        null,                  // Don't filter by row groups
                        null);                   // порядок сортировки
                    // Проходим через все ряды в таблице CostWork
                    while (cursor.moveToNext()) {
                        // Используем индекс для получения строки или числа
                        int currentID = cursor.getInt(
                                cursor.getColumnIndex(FM._ID));
                        int current_FILE_ID = cursor.getInt(
                                cursor.getColumnIndex(FM.FM_FILE_ID));
                        String current_FILE_NAME = cursor.getString(
                                cursor.getColumnIndex(FM.FM_FILE_NAME));
                        int current_WORK_ID = cursor.getInt(
                                cursor.getColumnIndex(FM.FM_MAT_ID));
                        String current_WORK_NAME = cursor.getString(
                                cursor.getColumnIndex(FM.FM_MAT_NAME));
                        int current_TYPE_ID = cursor.getInt(
                                cursor.getColumnIndex(FM.FM_MAT_TYPE_ID));
                        String current_TYPE_NAME = cursor.getString(
                                cursor.getColumnIndex(FM.FM_MAT_TYPE_NAME));
                        int current_CATEGORY_ID = cursor.getInt(
                                cursor.getColumnIndex(FM.FM_MAT_CATEGORY_ID));
                        String current_CATEGORY_NAME = cursor.getString(
                                cursor.getColumnIndex(FM.FM_MAT_CATEGORY_NAME));
                        float current_COST = cursor.getFloat(
                                cursor.getColumnIndex(FM.FM_MAT_COST));
                        int current_COUNT = cursor.getInt(
                                cursor.getColumnIndex(FM.FM_MAT_COUNT));
                        String current_UNIT = cursor.getString(
                                cursor.getColumnIndex(FM.FM_MAT_UNIT));
                        float current_SUMMA = cursor.getFloat(
                                cursor.getColumnIndex(FM.FM_MAT_SUMMA));
                        // Выводим построчно значения каждого столбца
                        Log.d(TAG, "\n" + "ID = " + currentID + "/" +
                                " FILE_ID = " + current_FILE_ID + "/" +
                                " FILE_NAME = " + current_FILE_NAME + "/" +
                                " MAT_ID = " + current_WORK_ID + "/" +
                                " MAT_NAME = " + current_WORK_NAME + "/" +
                                " MAT_TYPE_ID = " + current_TYPE_ID + "/" +
                                " MAT_TYPE_NAME = " + current_TYPE_NAME + "/" +
                                " MAT_CATEGORY_ID = " + current_CATEGORY_ID + "/" +
                                " MAT_CATEGORY_NAME = " + current_CATEGORY_NAME + "/" +
                                " MAT_COST = " + current_COST + "/" +
                                " MAT_COUNT = " + current_COUNT + "/" +
                                " MAT_UNIT = " + current_UNIT + "/" +
                                " MAT_SUMMA = " + current_SUMMA);
                }
                break;
        }
        if (cursor!=null){
            cursor.close();
        }
        db.close();
    }

    //получаем курсор с названиями  материалов
    public Cursor getNamesAllTypes(String table) {
        Log.i(TAG, "TableControllerSmeta.getNamesAllTypes ... ");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =null;
        String names = "";

        switch (table){
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
        Log.i(TAG, "SmetaOpenHelper.getMatNamesAllTypes cursor.getCount() =  " + cursor.getCount());
        db.close();
        return cursor;
    }

    //получаем имена   по смете с id файла file_id
    public String[]  getArrayNames(long file_id, String table){
        Log.i(TAG, "TableControllerSmeta.getArrayNames ... ");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor =null;
        String select = "";
        String[] name = new String[]{""};
        switch (table){
            case FW.TABLE_NAME:
                Log.i(TAG, "TableControllerSmeta.getArrayNames  case FW ");
                select = " SELECT " + FW.FW_WORK_NAME +
                        " FROM " +  FW.TABLE_NAME  +
                        " WHERE " + FW.FW_FILE_ID  + " = " + String.valueOf(file_id);

                cursor = db.rawQuery(select, null);
                Log.i(TAG, "TableControllerSmeta.getArrayNames cursor.getCount()  " + cursor.getCount());
                name = new String[cursor.getCount()];
                // Проходим через все строки в курсоре
                while (cursor.moveToNext()){
                    int position = cursor.getPosition();
                    name[position] = cursor.getString(cursor.getColumnIndex(FW.FW_WORK_NAME));
                    Log.i(TAG, "TableControllerSmeta.getArrayNames name[position] = " + name[position]);
                }
                break;

            case FM.TABLE_NAME:
                select = " SELECT " + FM.FM_MAT_NAME +
                        " FROM " +  FM.TABLE_NAME  +
                        " WHERE " + FM.FM_FILE_ID  + " = " + String.valueOf(file_id);
                cursor = db.rawQuery(select, null);
                Log.i(TAG, "TableControllerSmeta.getArrayNames cursor.getCount()  " + cursor.getCount());
                name = new String[cursor.getCount()];
                // Проходим через все строки в курсоре
                while (cursor.moveToNext()){
                    int position = cursor.getPosition();
                    name[position] = cursor.getString(cursor.getColumnIndex(FM.FM_MAT_NAME));
                    Log.i(TAG, "TableControllerSmeta.getArrayNames name[position] = " + name[position]);
                }
                break;
        }
        if (cursor!=null){
            cursor.close();
        }
        db.close();
        return name;
    }

    //получаем имена   по смете с id файла file_id и id типа type_id
    public String[]  getArrayNamesSelectedType(long file_id, long type_id, String table){
        Log.i(TAG, "TableControllerSmeta.getArrayNamesSelectedType ... ");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor =null;
        String select = "";
        String[] name = new String[]{};
        switch (table){
            case FW.TABLE_NAME:
                Log.i(TAG, "TableControllerSmeta.getArrayNamesSelectedType  case FW ");
                select = " SELECT " + FW.FW_WORK_NAME +
                        " FROM " +  FW.TABLE_NAME  +
                        " WHERE " + FW.FW_FILE_ID  + " = " + String.valueOf(file_id)+
                        " AND " + FW.FW_TYPE_ID  + " = " + String.valueOf(type_id);

                cursor = db.rawQuery(select, null);
                Log.i(TAG, "TableControllerSmeta.getArrayNamesSelectedType cursor.getCount()  " + cursor.getCount());
                name = new String[cursor.getCount()];
                // Проходим через все строки в курсоре
                while (cursor.moveToNext()){
                    int position = cursor.getPosition();
                    name[position] = cursor.getString(cursor.getColumnIndex(FW.FW_WORK_NAME));
                    Log.i(TAG, "TableControllerSmeta.getArrayNamesSelectedType name[position] = " + name[position]);
                }
                break;

            case FM.TABLE_NAME:
                select = " SELECT " + FM.FM_MAT_NAME +
                        " FROM " +  FM.TABLE_NAME  +
                        " WHERE " + FM.FM_FILE_ID  + " = " + String.valueOf(file_id)+
                        " AND " + FM.FM_MAT_TYPE_ID  + " = " + String.valueOf(type_id);
                cursor = db.rawQuery(select, null);
                Log.i(TAG, "TableControllerSmeta.getArrayNames cursor.getCount()  " + cursor.getCount());
                name = new String[cursor.getCount()];
                // Проходим через все строки в курсоре
                while (cursor.moveToNext()){
                    int position = cursor.getPosition();
                    name[position] = cursor.getString(cursor.getColumnIndex(FM.FM_MAT_NAME));
                    Log.i(TAG, "TableControllerSmeta.getArrayNames name[position] = " + name[position]);
                }
                break;
        }
        if (cursor!=null){
            cursor.close();
        }
        db.close();
        return name;
    }

    //получаем расценки  по смете с id файла file_id
    public float[]  getArrayCost(long file_id, String table){
        Log.i(TAG, "TableControllerSmeta.getArrayCost ... ");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor =null;
        String select = "";
        float[] cost = new float[]{};

        switch (table){
            case FW.TABLE_NAME:
                select = " SELECT " + FW.FW_COST +
                        " FROM " +  FW.TABLE_NAME  +
                        " WHERE " + FW.FW_FILE_ID  + " = " + String.valueOf(file_id);

                cursor = db.rawQuery(select, null);
                Log.i(TAG, "TableControllerSmeta.getArrayCost cursor.getCount()  " + cursor.getCount());
                cost = new float[cursor.getCount()];
                // Проходим через все строки в курсоре
                while (cursor.moveToNext()){
                    int position = cursor.getPosition();
                    cost[position] = cursor.getFloat(cursor.getColumnIndex(FW.FW_COST));
                    Log.i(TAG, "TableControllerSmeta.getArrayCost cost[position] = " + cost[position]);
                }
                break;

            case FM.TABLE_NAME:
                select =" SELECT " + FM.FM_MAT_COST +
                        " FROM " +  FM.TABLE_NAME  +
                        " WHERE " + FM.FM_FILE_ID  + " = " + String.valueOf(file_id);
                cursor = db.rawQuery(select, null);
                Log.i(TAG, "TableControllerSmeta.getArrayCost cursor.getCount()  " + cursor.getCount());
                cost = new float[cursor.getCount()];
                // Проходим через все строки в курсоре
                while (cursor.moveToNext()){
                    int position = cursor.getPosition();
                    cost[position] = cursor.getFloat(cursor.getColumnIndex(FM.FM_MAT_COST));
                    Log.i(TAG, "TableControllerSmeta.getArrayCost cost[position] = " + cost[position]);
                }
                break;
        }
        if (cursor!=null){
            cursor.close();
        }
        db.close();
        return cost;
    }

    //получаем расценки  по смете с id файла file_id и id типа type_id
    public float[]  getArrayCostSelectedType(long file_id, long type_id, String table){
        Log.i(TAG, "TableControllerSmeta.getArrayCostSelectedType ... ");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor =null;
        String select = "";
        float[] cost = new float[]{};
        switch (table){
            case FW.TABLE_NAME:
                Log.i(TAG, "TableControllerSmeta.getArrayCostSelectedType case FW ");
                select = " SELECT " + FW.FW_COST +
                        " FROM " +  FW.TABLE_NAME  +
                        " WHERE " + FW.FW_FILE_ID  + " = " + String.valueOf(file_id)+
                        " AND " + FW.FW_TYPE_ID  + " = " + String.valueOf(type_id);

                cursor = db.rawQuery(select, null);
                Log.i(TAG, "TableControllerSmeta.getArrayCostSelectedType cursor.getCount()  " + cursor.getCount());
                cost = new float[cursor.getCount()];
                // Проходим через все строки в курсоре
                while (cursor.moveToNext()){
                    int position = cursor.getPosition();
                    cost[position] = cursor.getFloat(cursor.getColumnIndex(FW.FW_COST));
                    Log.i(TAG, "TableControllerSmeta.getArrayCostSelectedType cost[position] = " + cost[position]);
                }
                break;

            case FM.TABLE_NAME:
                Log.i(TAG, "TableControllerSmeta.getArrayCostSelectedType case FM ");
                select =   " SELECT " + FM.FM_MAT_COST +
                        " FROM " +  FM.TABLE_NAME  +
                        " WHERE " + FM.FM_FILE_ID  + " = " + String.valueOf(file_id)+
                        " AND " + FM.FM_MAT_TYPE_ID  + " = " + String.valueOf(type_id);
                cursor = db.rawQuery(select, null);
                Log.i(TAG, "TableControllerSmeta.getArrayCostSelectedType cursor.getCount()  " + cursor.getCount());
                cost = new float[cursor.getCount()];
                // Проходим через все строки в курсоре
                while (cursor.moveToNext()){
                    int position = cursor.getPosition();
                    cost[position] = cursor.getFloat(cursor.getColumnIndex(FM.FM_MAT_COST));
                    Log.i(TAG, "TableControllerSmeta.getArrayCostSelectedType cost[position] = " + cost[position]);
                }

                break;
        }
        if (cursor!=null){
            cursor.close();
        }
        db.close();
        return cost;
    }

    //получаем количество работ/материалов  по смете с id файла file_id
    public float[]  getArrayAmount(long file_id, String table){
        Log.i(TAG, "TableControllerSmeta.getArrayAmount ... ");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor =null;
        String select = "";
        float[] amount = new float[]{};
        switch (table){
            case FW.TABLE_NAME:
                select = " SELECT " + FW.FW_COUNT +
                        " FROM " +  FW.TABLE_NAME  +
                        " WHERE " + FW.FW_FILE_ID  + " = " + String.valueOf(file_id);
                cursor = db.rawQuery(select, null);
                Log.i(TAG, "TableControllerSmeta.getArrayAmount cursor.getCount()  " + cursor.getCount());
                amount = new float[cursor.getCount()];
                // Проходим через все строки в курсоре
                while (cursor.moveToNext()){
                    int position = cursor.getPosition();
                    amount[position] = cursor.getFloat(cursor.getColumnIndex(FW.FW_COUNT));
                    Log.i(TAG, "TableControllerSmeta.getArrayAmount amount[position] = " + amount[position]);
                }
                break;

            case FM.TABLE_NAME:
                select = " SELECT " + FM.FM_MAT_COUNT +
                        " FROM " +  FM.TABLE_NAME  +
                        " WHERE " + FM.FM_FILE_ID  + " = " + String.valueOf(file_id);
                cursor = db.rawQuery(select, null);
                Log.i(TAG, "TableControllerSmeta.getArrayAmount cursor.getCount()  " + cursor.getCount());
                amount = new float[cursor.getCount()];
                // Проходим через все строки в курсоре
                while (cursor.moveToNext()){
                    int position = cursor.getPosition();
                    amount[position] = cursor.getFloat(cursor.getColumnIndex(FM.FM_MAT_COUNT));
                    Log.i(TAG, "TableControllerSmeta.getArrayAmount amount[position] = " + amount[position]);
                }
                break;
        }
        if (cursor!=null){
            cursor.close();
        }
        db.close();
        return amount;
    }

    //получаем количество работ  по смете с id файла file_id и id типа type_id
    public float[]  getArrayAmountSelectedType(long file_id, long type_id, String table){

        Log.i(TAG, "TableControllerSmeta.getArrayAmount ... ");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor =null;
        String select = "";
        float[] amount = new float[]{};
        switch (table){
            case FW.TABLE_NAME:
                select = " SELECT " + FW.FW_COUNT +
                        " FROM " +  FW.TABLE_NAME  +
                        " WHERE " + FW.FW_FILE_ID  + " = " + String.valueOf(file_id)+
                        " AND " + FW.FW_TYPE_ID  + " = " + String.valueOf(type_id);
                cursor = db.rawQuery(select, null);
                Log.i(TAG, "TableControllerSmeta.getArrayAmountSelectedType cursor.getCount()  " + cursor.getCount());
                amount = new float[cursor.getCount()];
                // Проходим через все строки в курсоре
                while (cursor.moveToNext()){
                    int position = cursor.getPosition();
                    amount[position] = cursor.getFloat(cursor.getColumnIndex(FW.FW_COUNT));
                    Log.i(TAG, "TableControllerSmeta.getArrayAmountSelectedType amount[position] = " + amount[position]);
                }
                break;

            case FM.TABLE_NAME:
                select = " SELECT " + FM.FM_MAT_COUNT +
                        " FROM " +  FM.TABLE_NAME  +
                        " WHERE " + FM.FM_FILE_ID  + " = " + String.valueOf(file_id)+
                        " AND " + FM.FM_MAT_TYPE_ID  + " = " + String.valueOf(type_id);
                cursor = db.rawQuery(select, null);
                Log.i(TAG, "TableControllerSmeta.getArrayAmountSelectedType cursor.getCount()  " + cursor.getCount());
                amount = new float[cursor.getCount()];
                // Проходим через все строки в курсоре
                while (cursor.moveToNext()){
                    int position = cursor.getPosition();
                    amount[position] = cursor.getFloat(cursor.getColumnIndex(FM.FM_MAT_COUNT));
                    Log.i(TAG, "TableControllerSmeta.getArrayAmountSelectedType amount[position] = " + amount[position]);
                }
                break;
        }
        if (cursor!=null){
            cursor.close();
        }
        db.close();
        return amount;
    }

    //получаем единицы измерения для  работ  по смете с id файла file_id
    public String[]  getArrayUnit(long file_id, String table){
        Log.i(TAG, "TableControllerSmeta.getArrayUnit ... ");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor =null;
        String select = "";
        String[] units = new String[]{};
        switch (table){
            case FW.TABLE_NAME:
                select = " SELECT " + FW.FW_UNIT +
                        " FROM " +  FW.TABLE_NAME  +
                        " WHERE " + FW.FW_FILE_ID  + " = " + String.valueOf(file_id);
                cursor = db.rawQuery(select, null);
                Log.i(TAG, "TableControllerSmeta.getArrayUnit cursor.getCount()  " + cursor.getCount());
                units = new String[cursor.getCount()];
                // Проходим через все строки в курсоре
                while (cursor.moveToNext()){
                    int position = cursor.getPosition();
                    units[position] = cursor.getString(cursor.getColumnIndex(FW.FW_UNIT ));
                    Log.i(TAG, "TableControllerSmeta.getArrayUnit units[position] = " + units[position]);
                }
                break;

            case FM.TABLE_NAME:
                select = " SELECT " + FM.FM_MAT_UNIT +
                        " FROM " +  FM.TABLE_NAME  +
                        " WHERE " + FM.FM_FILE_ID  + " = " + String.valueOf(file_id);
                cursor = db.rawQuery(select, null);
                Log.i(TAG, "TableControllerSmeta.getArrayUnit cursor.getCount()  " + cursor.getCount());
                units = new String[cursor.getCount()];
                // Проходим через все строки в курсоре
                while (cursor.moveToNext()){
                    int position = cursor.getPosition();
                    units[position] = cursor.getString(cursor.getColumnIndex(FM.FM_MAT_UNIT ));
                    Log.i(TAG, "TableControllerSmeta.getArrayUnit units[position] = " + units[position]);
                }
                break;
        }
        if (cursor!=null){
            cursor.close();
        }
        db.close();
        return units;
    }

    //получаем единицы измерения для  работ  по смете с id файла file_id
    public String[]  getArrayUnitSelectedType(long file_id, long type_id, String table){
        Log.i(TAG, "TableControllerSmeta.getArrayUnitSelectedType ... ");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor =null;
        String select = "";
        String[] units = new String[]{};
        switch (table){
            case FW.TABLE_NAME:
                select =" SELECT " + FW.FW_UNIT +
                        " FROM " +  FW.TABLE_NAME  +
                        " WHERE " + FW.FW_FILE_ID  + " = " + String.valueOf(file_id)+
                        " AND " + FW.FW_TYPE_ID  + " = " + String.valueOf(type_id);
                cursor = db.rawQuery(select, null);
                Log.i(TAG, "TableControllerSmeta.getArrayUnitSelectedType cursor.getCount()  " + cursor.getCount());
                units = new String[cursor.getCount()];
                // Проходим через все строки в курсоре
                while (cursor.moveToNext()){
                    int position = cursor.getPosition();
                    units[position] = cursor.getString(cursor.getColumnIndex(FW.FW_UNIT ));
                    Log.i(TAG, "TableControllerSmeta.getArrayUnitSelectedType units[position] = " + units[position]);
                }
                break;

            case FM.TABLE_NAME:
                select =  " SELECT " + FM.FM_MAT_UNIT +
                        " FROM " +  FM.TABLE_NAME  +
                        " WHERE " + FM.FM_FILE_ID  + " = " + String.valueOf(file_id)+
                        " AND " + FM.FM_MAT_TYPE_ID  + " = " + String.valueOf(type_id);
                cursor = db.rawQuery(select, null);
                Log.i(TAG, "TableControllerSmeta.getArrayUnitSelectedType cursor.getCount()  " + cursor.getCount());
                units = new String[cursor.getCount()];
                // Проходим через все строки в курсоре
                while (cursor.moveToNext()){
                    int position = cursor.getPosition();
                    units[position] = cursor.getString(cursor.getColumnIndex(FM.FM_MAT_UNIT ));
                    Log.i(TAG, "TableControllerSmeta.getArrayUnitSelectedType units[position] = " + units[position]);
                }
                break;
        }
        if (cursor!=null){
            cursor.close();
        }
        db.close();
        return units;
    }

    //получаем количество работ  по смете с id файла file_id
    public float[]  getArraySumma(long file_id, String table){

        Log.i(TAG, "TableControllerSmeta.getArraySumma ... ");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor =null;
        String select = "";
        float[] summa = new float[]{};
        switch (table){
            case FW.TABLE_NAME:
                select =" SELECT " + FW.FW_SUMMA +
                        " FROM " +  FW.TABLE_NAME  +
                        " WHERE " + FW.FW_FILE_ID  + " = " + String.valueOf(file_id);
                cursor = db.rawQuery(select, null);
                Log.i(TAG, "TableControllerSmeta.getArraySumma cursor.getCount()  " + cursor.getCount());
                summa = new float[cursor.getCount()];
                // Проходим через все строки в курсоре
                while (cursor.moveToNext()){
                    int position = cursor.getPosition();
                    summa[position] = cursor.getFloat(cursor.getColumnIndex(FW.FW_SUMMA));
                    Log.i(TAG, "TableControllerSmeta.getArraySumma summa[position] = " + summa[position]);
                }
                break;

            case FM.TABLE_NAME:
                select = " SELECT " + FM.FM_MAT_SUMMA +
                        " FROM " +  FM.TABLE_NAME  +
                        " WHERE " + FM.FM_FILE_ID  + " = " + String.valueOf(file_id);
                cursor = db.rawQuery(select, null);
                Log.i(TAG, "TableControllerSmeta.getArraySumma cursor.getCount()  " + cursor.getCount());
                summa = new float[cursor.getCount()];
                // Проходим через все строки в курсоре
                while (cursor.moveToNext()){
                    int position = cursor.getPosition();
                    summa[position] = cursor.getFloat(cursor.getColumnIndex(FM.FM_MAT_SUMMA));
                    Log.i(TAG, "TableControllerSmeta.getArraySumma summa[position] = " + summa[position]);
                }
                break;
        }
        if (cursor!=null){
            cursor.close();
        }
        db.close();
        return summa;
    }

    //получаем количество работ  по смете с id файла file_id
    public float[]  getArraySummaSelectedType(long file_id, long type_id, String table){

        Log.i(TAG, "TableControllerSmeta.getArraySumma ... ");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor =null;
        String select = "";
        float[] summa = new float[]{};
        switch (table){
            case FW.TABLE_NAME:
                select =" SELECT " + FW.FW_SUMMA +
                        " FROM " +  FW.TABLE_NAME  +
                        " WHERE " + FW.FW_FILE_ID  + " = " + String.valueOf(file_id)+
                        " AND " + FW.FW_TYPE_ID  + " = " + String.valueOf(type_id);
                        cursor = db.rawQuery(select, null);
                Log.i(TAG, "TableControllerSmeta.getArraySumma cursor.getCount()  " + cursor.getCount());
                summa = new float[cursor.getCount()];
                // Проходим через все строки в курсоре
                while (cursor.moveToNext()){
                    int position = cursor.getPosition();
                    summa[position] =cursor.getFloat(cursor.getColumnIndex(FW.FW_SUMMA));
                            Log.i(TAG, "TableControllerSmeta.getArraySumma summa[position] = " + summa[position]);
                }
                break;

            case FM.TABLE_NAME:
                select =" SELECT " + FM.FM_MAT_SUMMA +
                        " FROM " +  FM.TABLE_NAME  +
                        " WHERE " + FM.FM_FILE_ID  + " = " + String.valueOf(file_id)+
                        " AND " + FM.FM_MAT_TYPE_ID + " = " + String.valueOf(type_id);
                        cursor = db.rawQuery(select, null);
                Log.i(TAG, "TableControllerSmeta.getArraySumma cursor.getCount()  " + cursor.getCount());
                summa = new float[cursor.getCount()];
                // Проходим через все строки в курсоре
                while (cursor.moveToNext()){
                    int position = cursor.getPosition();
                    summa[position] =cursor.getFloat(cursor.getColumnIndex(FM.FM_MAT_SUMMA));
                            Log.i(TAG, "TableControllerSmeta.getArraySumma summa[position] = " + summa[position]);
                }
                break;
        }
        if (cursor!=null){
            cursor.close();
        }
        db.close();
        return summa;
    }


}
