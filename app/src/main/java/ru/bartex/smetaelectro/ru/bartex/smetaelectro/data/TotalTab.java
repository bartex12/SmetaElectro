package ru.bartex.smetaelectro.ru.bartex.smetaelectro.data;

import android.provider.BaseColumns;

public class TotalTab {

    public TotalTab(){
        //пустой конструктор
    }

    public final static String TABLE_NAME = "TotalTab";

    public final static String _ID = BaseColumns._ID;
    public final static String COLUMN_TOTAL_FILE_ID = "Total_File_Id";
    public final static String COLUMN_TOTAL_CATEGORY_ID = "Total_Category_Id";
    public final static String COLUMN_TOTAL_TYPE_ID = "Total_TYpe_Id";
    public final static String COLUMN_TOTAL_WORK_ID= "Total_Work_Id";
    public final static String COLUMN_TOTAL_COST= "Total_Cost";
    public final static String COLUMN_TOTAL_COST_NUMBER= "Total_Cost_Number";
    public final static String COLUMN_TOTAL_COST_UNIT= "Total_Cost_Unit";
    public final static String COLUMN_TOTAL_SUMMA= "TotalSumma";
}
