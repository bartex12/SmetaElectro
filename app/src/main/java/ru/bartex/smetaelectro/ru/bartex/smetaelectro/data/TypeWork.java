package ru.bartex.smetaelectro.ru.bartex.smetaelectro.data;

import android.provider.BaseColumns;

public class TypeWork {

    public TypeWork(){
        //пустой конструктор
    }

    public final static String TABLE_NAME = "Type";

    public final static String _ID = BaseColumns._ID;
    public final static String COLUMN_TYPE_CATEGORY_ID = "Type_Category_Id";
    public final static String COLUMN_TYPE_MARK = "Type_Mark";
    public final static String COLUMN_TYPE_NAME = "Type_Name";
    public final static String COLUMN_TYPE_DESCRIPTION = "Type_Description";

}
