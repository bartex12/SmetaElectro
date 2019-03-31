package ru.bartex.smetaelectro.ru.bartex.smetaelectro.data;

import android.provider.BaseColumns;

public class FileWork {

    private FileWork(){
        //пустой конструктор
    }

    public final static String TABLE_NAME = "FileWork";

    public final static String _ID = BaseColumns._ID;
    public final static String FILE_NAME = "FileName";
    public final static String ADRESS = "FileAdress";
    public final static String FILE_NAME_DATE = "FileNameDate";
    public final static String FILE_NAME_TIME = "FileNameTime";
    public final static String DESCRIPTION_OF_FILE = "DescriptionOfFile";
}
