package ru.bartex.smetaelectro.ru.bartex.smetaelectro.data;

import android.provider.BaseColumns;

public class TypeMat {
    public TypeMat(){
        //пустой конструктор
    }

    public final static String TABLE_NAME = "TypeMat";

    public final static String _ID = BaseColumns._ID;
    public final static String TYPE_MAT_CATEGORY_ID = "TYPE_MAT_CATEGORY_ID";
    public final static String TYPE_MAT_NAME = "TYPE_MAT_NAME";
    public final static String TYPE_MAT_DESCRIPTION = "TYPE_MAT_DESCRIPTION";
}
