package ru.bartex.smetaelectro.ru.bartex.smetaelectro.data;

import android.provider.BaseColumns;

public class CategoryWork {

    public CategoryWork(){
        //пустой конструктор
    }
    public final static String TABLE_NAME = "Category";

    public final static String _ID = BaseColumns._ID;
    public final static String COLUMN_CATEGORY_MARK = "Category_Mark";
    public final static String COLUMN_CATEGORY_NAME = "CategoryName";
    public final static String COLUMN_CATEGORY_DESCRIPTION = "DescriptionOfCategory";
}
