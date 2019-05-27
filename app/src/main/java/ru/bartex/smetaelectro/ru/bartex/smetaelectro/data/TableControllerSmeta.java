package ru.bartex.smetaelectro.ru.bartex.smetaelectro.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.bartex.smetaelectro.DataFile;

public class TableControllerSmeta extends SmetaOpenHelper {

    public static final String TAG = "33333";

    public TableControllerSmeta(Context context) {
        super(context);
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
        long currentID = -1;
        Cursor cursor = null;
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = this.getReadableDatabase();

        switch (tableName){
            case FileWork.TABLE_NAME:
                Log.d(TAG, "getIdFromString case FileWork.TABLE_NAME...");
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
                    int idColumnIndex = cursor.getColumnIndex(FileWork._ID);
                    // Используем индекс для получения строки или числа
                    currentID = cursor.getLong(idColumnIndex);
                }
                Log.d(TAG, "getIdFromString case FileWork.TABLE_NAME currentID = " + currentID);
                break;

            case CategoryWork.TABLE_NAME:

                break;

        }
        if (cursor!=null){
            cursor.close();
        }
        db.close();

        return currentID;
    }

    //удаляем название сметы из таблицы FileWork и все строки из FW по id сметы fileId
    public void deleteFile(long fileId) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FW.TABLE_NAME, FW.FW_FILE_ID + " =? " ,
                new String[]{String.valueOf(fileId)});
        db.delete(FileWork.TABLE_NAME, FileWork._ID + " =? " ,
                new String[]{String.valueOf(fileId)});
        db.close();
    }

}
