package ru.bartex.smetaelectro.database.files;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.bartex.smetaelectro.data.DataFile;
import ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.database.work.FW;

import static ru.bartex.smetaelectro.database.P.getDateString;
import static ru.bartex.smetaelectro.database.P.getTimeString;


public class FileWork {

    public static final String TAG = "33333";

    private FileWork(){
        //пустой конструктор
    }

    public final static String TABLE_NAME = "FileWork";

    public final static String _ID = BaseColumns._ID;
    public final static String FILE_NAME = "FileName";
    public final static String WORK_OR_MATERIAL = "WorkOrMaterial";
    public final static String ADRESS = "FileAdress";
    public final static String FILE_NAME_DATE = "FileNameDate";
    public final static String FILE_NAME_TIME = "FileNameTime";
    public final static String DESCRIPTION_OF_FILE = "DescriptionOfFile";

    //создание таблицы
    public static void createTable(SQLiteDatabase db){
    // Строка для создания таблицы наименований смет FileWork
        String SQL_CREATE_TAB_FILE = "CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WORK_OR_MATERIAL + " INTEGER NOT NULL DEFAULT 0, "
                + FILE_NAME + " TEXT NOT NULL, "
                + ADRESS + " TEXT, "
                + FILE_NAME_DATE + " TEXT NOT NULL DEFAULT current_date, "
                + FILE_NAME_TIME + " TEXT NOT NULL DEFAULT current_time, "
                + DESCRIPTION_OF_FILE + " TEXT NOT NULL DEFAULT 'Без описания');";
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TAB_FILE);
        Log.d(TAG, "FileWork - onCreate- создание таблицы FileWork");

        // Если файлов в базе нет, вносим запись с именем файла по умолчанию P.FILENAME_DEFAULT
        createDefaultFile(db);
    }

    //обновление таблицы
    static void onUpgrade(SQLiteDatabase database) {
        //обновлять пока не собираюсь
    }

    // Когда файлов в базе нет, вносим запись с именем файла по умолчанию (Используем здесь)
    private static void createDefaultFile(SQLiteDatabase db) {
        Log.d(TAG, "FileWork.createDefaultFile..." );
        //получаем дату и время в нужном для базы данных формате
        String dateFormat = getDateString();
        String timeFormat = getTimeString();

        ContentValues cv = new ContentValues();
        cv.put(WORK_OR_MATERIAL, 0);
        cv.put(FILE_NAME, P.FILENAME_DEFAULT);
        cv.put(ADRESS, P.ADRESS_DEFAULT);
        cv.put(FILE_NAME_DATE, dateFormat);
        cv.put(FILE_NAME_TIME, timeFormat);
        cv.put(DESCRIPTION_OF_FILE, P.DESCRIPTION_DEFAULT);
        // вставляем строку
        long ID = db.insert(TABLE_NAME, null, cv);

        Log.d(TAG, "FileWork.createDefaultFile...  file1_id = " + ID);
    }

    //получаем список объектов DataFile
    public static List<DataFile> readFilesData(SQLiteDatabase db) {
        Log.i(TAG, "FileWork.readFilesData ... ");
        List<DataFile> recordsList = new ArrayList<DataFile>();

        String dataQuery = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(dataQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DataFile dataFile = getFileDataFromCurcor(cursor);
                //добавляем в список
                recordsList.add(dataFile);

            } while (cursor.moveToNext());
        }
        Log.d(TAG, "FileWork readFilesData cursor.getCount() = " + cursor.getCount());
        cursor.close();
        return recordsList;
    }

    //получаем объект DataFile для файла с file_id
    public static DataFile getFileData(SQLiteDatabase db, long file_id) {
        Log.i(TAG, "FileWork.getFileData ... ");
        DataFile dataFile = new DataFile();

        String fileName = " SELECT  * FROM " + FileWork.TABLE_NAME +
                " WHERE " + FileWork._ID + " = ? ";
        Cursor cursor = db.rawQuery(fileName, new String[]{String.valueOf(file_id)});

        if (cursor.moveToFirst()) {
            dataFile = getFileDataFromCurcor(cursor);
        }
        Log.d(TAG, "FileWork.getFileData cursor.getCount() = " + cursor.getCount());
        cursor.close();
        return dataFile;
    }

    //получаем объект DataFile из курсора
    private static DataFile getFileDataFromCurcor(Cursor cursor) {
        // Узнаем индекс каждого столбца и Используем индекс для получения строки
        long id = cursor.getLong(cursor.getColumnIndex(FileWork._ID));
        String currentFileName = cursor.getString(cursor.getColumnIndex(FileWork.FILE_NAME));
        String currentAdress = cursor.getString(cursor.getColumnIndex(FileWork.ADRESS));
        String currentDate = cursor.getString(cursor.getColumnIndex(FileWork.FILE_NAME_DATE));
        String currentTime = cursor.getString(cursor.getColumnIndex(FileWork.FILE_NAME_TIME));
        String currentDescription = cursor.getString(cursor.getColumnIndex(FileWork.DESCRIPTION_OF_FILE));
        //Log.d(TAG, "getFileData currentFileName = " + currentFileName);
        //создаём экземпляр класса DataFile в конструкторе и возвращаем его
        return new DataFile(id, currentFileName, currentAdress,
                currentDate, currentTime, currentDescription);
    }

    //получаем id по имени
    public static long getIdFromName(SQLiteDatabase db, String name) {
        Log.i(TAG, "FileWork.getIdFromName ... ");
        long currentID = -1;
        Cursor cursor = db.query(
                TABLE_NAME,                     // таблица
                new String[]{_ID},            // столбцы
                FILE_NAME + "=?",    // столбцы для условия WHERE
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

    //получаем имя файла  по  id
    public static String getNameFromId(SQLiteDatabase db, long id) {
        Log.i(TAG, "FileWork getNameFromId... ");

        String currentName = "";

        Cursor cursor = db.query(
                true,
                TABLE_NAME,
                null,
                _ID + "=" + id,
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            currentName = cursor.getString(cursor.getColumnIndex(FILE_NAME));
        }
        cursor.close();
        return currentName;
    }

    //удаляем название сметы из таблицы FileWork и все строки из FW по id сметы fileId
    public static void deleteObject(SQLiteDatabase db, long id) {
        Log.i(TAG, "FileWork.deleteObject case FileWork ");
        db.delete(FW.TABLE_NAME, FW.FW_FILE_ID + " =? ",
                new String[]{String.valueOf(id)});
        db.delete(TABLE_NAME, _ID + " =? ",
                new String[]{String.valueOf(id)});
    }

    // Добавляем имя и другие параметры сметы в таблицу FileWork (из активности SmetaNewName)
    public static long addFile(SQLiteDatabase db, String fileName, String adress, String description) {
        Log.i(TAG, "FileWork.addFile ... ");

        //получаем дату и время в нужном для базы данных формате
        String dateFormat = getDateString();
        String timeFormat = getTimeString();

        //заполняем данные для обновления в базе
        ContentValues cv = new ContentValues();
        cv.put(FILE_NAME, fileName);
        cv.put(ADRESS, adress);
        cv.put(FILE_NAME_DATE, dateFormat);
        cv.put(FILE_NAME_TIME, timeFormat);
        cv.put(DESCRIPTION_OF_FILE, description);
        // вставляем строку
        long ID = db.insert(TABLE_NAME, null, cv);

        Log.d(TAG, "FileWork.createDefaultFile...  file1_id = " + ID);
        return ID;
    }

    //обновляем данные файла сметы имя, адрес, описание, дата и время - на всякий случай
    public void updateFileData(SQLiteDatabase db, long file_id, String name, String adress, String description) {
        Log.i(TAG, "FileWork.updateFileData ... ");

        //заполняем данные для обновления в базе
        ContentValues cv = new ContentValues();
        cv.put(FILE_NAME, name);
        cv.put(ADRESS, adress);
        cv.put(DESCRIPTION_OF_FILE, description);

        db.update(TABLE_NAME, cv, _ID + "=" + file_id, null);
    }

    //обновляем данные файла сметы имя, адрес, описание, дата и время
    public static void updateDataFile(SQLiteDatabase db, long file_id, String name, String adress, String description){

        //заполняем данные для обновления в базе
        ContentValues values = new ContentValues();
        values.put(FILE_NAME, name);
        values.put(ADRESS, adress);
        values.put(DESCRIPTION_OF_FILE, description);

        db.update(TABLE_NAME, values,
                _ID + "=" + file_id, null);
        Log.i(TAG, "FileWork.updateDataFile - name =" + name + "  file_id = " + file_id);
    }

}
