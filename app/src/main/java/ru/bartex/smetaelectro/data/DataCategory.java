package ru.bartex.smetaelectro.data;

import java.io.Serializable;

public class DataCategory implements Serializable {
    private String mCategoryName;
    private String mCategoryDescription;

    public DataCategory() {
        //пустой конструктор
    }

    //основной конструктор
    public DataCategory(String categoryName, String categoryDescription) {

        mCategoryName = categoryName;
        mCategoryDescription = categoryDescription;
    }

    public String getmCategoryName() {
        return mCategoryName;
    }

    public void setmCategoryName(String mCategoryName) {
        this.mCategoryName = mCategoryName;
    }

    public String getmCategoryDescription() {
        return mCategoryDescription;
    }

    public void setmCategoryDescription(String mCategoryDescription) {
        this.mCategoryDescription = mCategoryDescription;
    }
}
