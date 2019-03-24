package ru.bartex.smetaelectro.ru.bartex.smetaelectro.data;

import android.provider.BaseColumns;

public class FW {

    public FW(){
        //пустой конструктор
    }
    public final static String TABLE_NAME = "File_And_Work";

    public final static String _ID = BaseColumns._ID;
    public final static String COLUMN_FW_FILE_ID = "FW_File_ID";
    public final static String COLUMN_FW_FILE_NAME = "FW_File_Name";
    public final static String COLUMN_FW_WORK_ID = "FW_Work_ID";
    public final static String COLUMN_FW_WORK_NAME = "FW_Work_Name";
    public final static String COLUMN_FW_TYPE_ID = "FW_Type_ID";
    public final static String COLUMN_FW_TYPE_NAME = "FW_Type_Name";
    public final static String COLUMN_FW_CATEGORY_ID = "FW_Category_ID";
    public final static String COLUMN_FW_CATEGORY_NAME = "FW_Category_Name";
    public final static String COLUMN_FW_COST = "FW_Cost";
    public final static String COLUMN_FW_COUNT = "FW_Count";
    public final static String COLUMN_FW_UNIT = "FW_Unit";
    public final static String COLUMN_FW_SUMMA = "FW_Summa";

}
