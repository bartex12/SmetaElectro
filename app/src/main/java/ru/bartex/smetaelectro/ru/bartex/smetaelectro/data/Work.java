package ru.bartex.smetaelectro.ru.bartex.smetaelectro.data;

import android.provider.BaseColumns;

public class Work {

    public Work(){
        //пустой конструктор
    }
    public final static String TABLE_NAME = "Work";

    public final static String _ID = BaseColumns._ID;
    public final static String COLUMN_WORK_NAME = "Work_Name";
    public final static String COLUMN_WORK_DONE = "Work_Done";
    public final static String COLUMN_WORK_DESCRIPTION= "Work_Description";
    public final static String COLUMN_WORK_TYPE_ID = "Work_Type_ID";
}