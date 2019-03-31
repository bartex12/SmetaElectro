package ru.bartex.smetaelectro.ru.bartex.smetaelectro.data;

import android.provider.BaseColumns;

public class CostWork {

    public CostWork(){
        //пустой конструктор
    }
    public final static String TABLE_NAME = "Cost";

    public final static String _ID = BaseColumns._ID;
    public final static String COST_WORK_ID = "Cost_Work_ID";
    public final static String COST_UNIT_ID = "Cost_Unit_ID";
    public final static String COST_COST = "Cost_Cost";
    public final static String COST_NUMBER = "Cost_Number";
}
