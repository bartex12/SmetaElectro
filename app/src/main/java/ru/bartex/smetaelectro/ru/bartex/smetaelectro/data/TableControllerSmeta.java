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
                }
                break;

            case CategoryMat.TABLE_NAME:
                Log.d(TAG, "getIdFromName case CategoryMat.TABLE_NAME...");

                if (cursor.moveToFirst()) {
                    // Узнаем индекс  столбца FileWork._ID
                    idColumnIndex = cursor.getColumnIndex(CategoryMat._ID);
                }
                break;

            case TypeWork.TABLE_NAME:
                Log.d(TAG, "getIdFromName case TypeWork.TABLE_NAME...");

                break;

            case TypeMat.TABLE_NAME:
                Log.d(TAG, "getIdFromName case TypeMat.TABLE_NAME...");

                break;

            case Work.TABLE_NAME:
                Log.d(TAG, "getIdFromName case Work.TABLE_NAME...");

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
                }
                break;

            case CostWork.TABLE_NAME:
                Log.d(TAG, "getIdFromName case CostWork.TABLE_NAME...");

                break;

            case CostMat.TABLE_NAME:
                Log.d(TAG, "getIdFromName case CostMat.TABLE_NAME...");

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
                }
                break;

            case UnitMat.TABLE_NAME:
                Log.d(TAG, "getIdFromName case UnitMat.TABLE_NAME...");

                break;
        }
        // Используем индекс для получения id
        currentID = cursor.getLong(idColumnIndex);
        Log.d(TAG, "getIdFromName currentID = " + currentID);

        if (cursor!=null){
            cursor.close();
        }
        db.close();

        return currentID;
    }

    //удаляем название сметы из таблицы FileWork и все строки из FW по id сметы fileId
    public void deleteFile(long fileId) {
        Log.i(TAG, "TableControllerSmeta.deleteFile ... ");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FW.TABLE_NAME, FW.FW_FILE_ID + " =? " ,
                new String[]{String.valueOf(fileId)});
        db.delete(FileWork.TABLE_NAME, FileWork._ID + " =? " ,
                new String[]{String.valueOf(fileId)});
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


}
