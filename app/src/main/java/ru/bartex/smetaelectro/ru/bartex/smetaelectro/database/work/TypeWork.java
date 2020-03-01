package ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work;

import android.provider.BaseColumns;

public class TypeWork {

    public TypeWork(){
        //пустой конструктор
    }

    public final static String TABLE_NAME = "Type";

    public final static String _ID = BaseColumns._ID;
    public final static String TYPE_CATEGORY_ID = "Type_Category_Id";
    public final static String TYPE_NAME = "Type_Name";
    public final static String TYPE_DESCRIPTION = "Type_Description";

}
