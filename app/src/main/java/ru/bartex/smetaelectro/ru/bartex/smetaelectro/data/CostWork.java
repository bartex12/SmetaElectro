package ru.bartex.smetaelectro.ru.bartex.smetaelectro.data;

import android.provider.BaseColumns;

public class CostWork {

    public CostWork(){
        //пустой конструктор
    }
    public final static String TABLE_NAME = "Cost";

    public final static String _ID = BaseColumns._ID;
    public final static String COLUMN_COST_WORK_ID = "Cost_Work_ID";
    public final static String COLUMN_COST_UNIT = "Cost_Unit";
    public final static String COLUMN_COST_COST = "Cost_Cost";
    public final static String COLUMN_COST_NUMBER = "Cost_Number";
}
