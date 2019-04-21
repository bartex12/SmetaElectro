package ru.bartex.smetaelectro.ru.bartex.smetaelectro.data;

import android.provider.BaseColumns;

public class FM {
    public FM(){
        //пустой конструктор
    }
    public final static String TABLE_NAME = "File_And_Materials";

    public final static String _ID = BaseColumns._ID;
    public final static String FM_FILE_ID = "FM_FILE_ID";
    public final static String FM_FILE_NAME = "FM_FILE_NAME";
    public final static String FM_MAT_ID = "FM_MAT_ID";
    public final static String FM_MAT_NAME = "FM_MAT_NAME";
    public final static String FM_MAT_TYPE_ID = "FM_MAT_TYPE_ID";
    public final static String FM_MAT_TYPE_NAME = "FM_MAT_TYPE_NAME";
    public final static String FM_MAT_CATEGORY_ID = "FM_MAT_CATEGORY_ID";
    public final static String FM_MAT_CATEGORY_NAME = "FM_MAT_CATEGORY_NAME";
    public final static String FM_MAT_COST = "FM_MAT_COST";
    public final static String FM_MAT_COUNT = "FM_MAT_COUNT";
    public final static String FM_MAT_UNIT = "FM_MAT_UNIT";
    public final static String FM_MAT_SUMMA = "FM_MAT_SUMMA";
}
