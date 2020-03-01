package ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat;

import android.provider.BaseColumns;

public class CostMat {

    public CostMat(){
        //пустой конструктор
    }
    public final static String TABLE_NAME = "CostMat";

    public final static String _ID = BaseColumns._ID;
    public final static String COST_MAT_ID = "COST_MAT_ID";
    public final static String COST_MAT_UNIT_ID = "COST_MAT_UNIT_ID";
    public final static String COST_MAT_COST = "COST_MAT_COST";
    public final static String COST_MAT_NUMBER = "COST_MAT_NUMBER";
}
