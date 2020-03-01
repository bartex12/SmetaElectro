package ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat;

import android.provider.BaseColumns;

public class Mat {
    public Mat(){
        //пустой конструктор
    }
    public final static String TABLE_NAME = "Mat";

    public final static String _ID = BaseColumns._ID;
    public final static String MAT_NAME = "MAT_NAME";
    public final static String MAT_DESCRIPTION= "MAT_DESCRIPTION";
    public final static String MAT_TYPE_ID = "MAT_TYPE_ID";
}
