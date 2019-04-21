package ru.bartex.smetaelectro.ru.bartex.smetaelectro.data;

import android.provider.BaseColumns;

public class CategoryMat {

    public CategoryMat(){
        //пустой конструктор
    }
    public final static String TABLE_NAME = "CategoryMat";

    public final static String _ID = BaseColumns._ID;
    public final static String CATEGORY_MAT_NAME = "CATEGORY_MAT_NAME";
    public final static String CATEGORY_MAT_DESCRIPTION = "CATEGORY_MAT_DESCRIPTION";
}
