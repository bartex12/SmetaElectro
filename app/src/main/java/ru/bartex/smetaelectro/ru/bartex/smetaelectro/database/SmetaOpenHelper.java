package ru.bartex.smetaelectro.ru.bartex.smetaelectro.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ru.bartex.smetaelectro.DataFile;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.files.TotalTab;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.CategoryMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.CostMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.FM;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.Mat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.TypeMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.UnitMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.CategoryWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.CostWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.FW;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.files.FileWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.TypeWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.Unit;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.Work;

public class SmetaOpenHelper extends SQLiteOpenHelper {

        // Когда файлов в базе нет, вносим запись с именем файла по умолчанию (Используем здесь)
    //public void createDefaultFile(SQLiteDatabase db)
        // Если файлов в базе нет, вносим запись с именем файла по умолчанию (используем в MainActiviti)
    //public long createDefaultFileIfNeed()


    public static final String TAG = "33333";
    private final Context fContext;

    //Имя файла базы данных
    private static final String DATABASE_NAME = "SmetaElectro.db";
    // Версия базы данных. При изменении схемы увеличить на единицу
    private static final int DATABASE_VERSION = 1;

    public SmetaOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        fContext = context;
        Log.d(TAG, "SmetaOpenHelper - конструктор хелпера  DATABASE_NAME = " + DATABASE_NAME);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "SmetaOpenHelper - onCreate");

        FileWork.createTable(db);
        FW.createTable(db);
        FM.createTable(db);
        CategoryMat.createTable(db, fContext);
        CategoryWork.createTable(db, fContext);
        TypeWork.createTable(db, fContext);
        TypeMat.createTable(db, fContext);
        Work.createTable(db, fContext);
        Mat.createTable(db, fContext);
        Unit.createTable(db, fContext);
        UnitMat.createTable(db, fContext);
        CostWork.createTable(db, fContext);
        CostMat.createTable(db, fContext);
        TotalTab.createTable(db);

        Log.d(TAG, "++++++++++++  Создана база данных  " + DATABASE_NAME + "  ++++++++++++++++");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //********************************************************

    // Если файлов в базе нет, вносим запись с именем файла по умолчанию (используем в MainActiviti)
    public long createDefaultFileIfNeed() {
        int count = this.getFilesCount();
        long file1_id ;
        if (count == 0) {
            //получаем дату и время в нужном для базы данных формате
            String dateFormat = P.getDateString();
            String timeFormat = P.getTimeString();

            //создаём экземпляр класса DataFile в конструкторе
            DataFile file1 = new DataFile(P.FILENAME_DEFAULT, P.ADRESS_DEFAULT,
                    dateFormat, timeFormat, P.DESCRIPTION_DEFAULT);
            //добавляем запись в таблицу FileWork, используя данные DataFile и получаем id записи
            file1_id = this.addFile(file1);
        }else {
            file1_id = this.getIdFromFileName(P.FILENAME_DEFAULT);
        }
        Log.d(TAG, "MyDatabaseHelper.createDefaultFileIfNeed ... count = " +
                this.getFilesCount() + " file1_id = " + file1_id);
        return file1_id;
    }

    //получаем количество файлов  в базе
    private int getFilesCount() {
        Log.i(TAG, "TempDBHelper.getFilesCount ... ");
        String countQuery = "SELECT  * FROM " + FileWork.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    //Метод для добавления нового файла в таблицу файлов
    private long addFile(DataFile file) {
        Log.d(TAG, "TempDBHelper.addFile ... " + file.getFileName());

        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(FileWork.FILE_NAME, file.getFileName());
        cv.put(FileWork.ADRESS, file.getAdress());
        cv.put(FileWork.FILE_NAME_DATE, file.getFileNameDate());
        cv.put(FileWork.FILE_NAME_TIME, file.getFileNameTime());
        cv.put(FileWork.DESCRIPTION_OF_FILE, file.getDescription());
        // вставляем строку
        long ID = db.insert(FileWork.TABLE_NAME, null, cv);
        // закрываем соединение с базой
        db.close();
        return ID;
    }


    //получаем ID по имени файла
    private long getIdFromFileName(String name) {
        long currentID;
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                FileWork.TABLE_NAME,   // таблица
                new String[]{FileWork._ID},            // столбцы
                FileWork.FILE_NAME + "=?",                  // столбцы для условия WHERE
                new String[]{name},                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            // Узнаем индекс  столбца FileWork._ID
            int idColumnIndex = cursor.getColumnIndex(FileWork._ID);
            // Используем индекс для получения строки или числа
            currentID = cursor.getLong(idColumnIndex);
        } else {
            currentID = -1;
        }
        Log.d(TAG, "getIdFromFileName currentID = " + currentID);
        cursor.close();
        db.close();
        return currentID;
    }

}
