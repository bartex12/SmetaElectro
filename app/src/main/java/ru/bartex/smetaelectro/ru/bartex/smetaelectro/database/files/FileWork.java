package ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.files;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;

import static ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P.getDateString;
import static ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P.getTimeString;

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
        String SQL_CREATE_TAB_FILE = "CREATE TABLE " + FileWork.TABLE_NAME + " ("
                + FileWork._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FileWork.WORK_OR_MATERIAL + " INTEGER NOT NULL DEFAULT 0, "
                + FileWork.FILE_NAME + " TEXT NOT NULL, "
                + FileWork.ADRESS + " TEXT, "
                + FileWork.FILE_NAME_DATE + " TEXT NOT NULL DEFAULT current_date, "
                + FileWork.FILE_NAME_TIME + " TEXT NOT NULL DEFAULT current_time, "
                + FileWork.DESCRIPTION_OF_FILE + " TEXT NOT NULL DEFAULT 'Без описания');";
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TAB_FILE);
        Log.d(TAG, "SmetaOpenHelper - onCreate- создание таблицы FileWork");

        // Если файлов в базе нет, вносим запись с именем файла по умолчанию P.FILENAME_DEFAULT
        createDefaultFile(db);
    }

    //обновление таблицы
    static void onUpgrade(SQLiteDatabase database) {
        //обновлять пока не собираюсь
    }

    // Когда файлов в базе нет, вносим запись с именем файла по умолчанию (Используем здесь)
    private static void createDefaultFile(SQLiteDatabase db) {
        Log.d(TAG, "MyDatabaseHelper.createDefaultFile..." );
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
        long ID = db.insert(FileWork.TABLE_NAME, null, cv);
        // закрываем соединение с базой
        //db.close();
        Log.d(TAG, "MyDatabaseHelper.createDefaultFile...  file1_id = " + ID);
    }

}
