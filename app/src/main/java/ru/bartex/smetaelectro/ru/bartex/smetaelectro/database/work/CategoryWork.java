package ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work;

import android.provider.BaseColumns;

public class CategoryWork {

    public CategoryWork(){
        //пустой конструктор
    }
    public final static String TABLE_NAME = "Category";

    public final static String _ID = BaseColumns._ID;
    public final static String CATEGORY_NAME = "CategoryName";
    public final static String CATEGORY_DESCRIPTION = "DescriptionOfCategory";
}
