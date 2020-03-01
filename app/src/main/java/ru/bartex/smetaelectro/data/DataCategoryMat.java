package ru.bartex.smetaelectro.data;

import java.io.Serializable;

public class DataCategoryMat implements Serializable {

    private String mCategoryMatName;
    private String mCategoryMatDescription;

    public DataCategoryMat( ){
        //пустой конструктор
    }

    //основной конструктор
    public DataCategoryMat(String categoryMatName, String categoryMatDescription){

        mCategoryMatName = categoryMatName;
        mCategoryMatDescription = categoryMatDescription;
    }

    public String getmCategoryMatName() {
        return mCategoryMatName;
    }

    public void setmCategoryMatName(String mCategoryMatName) {
        this.mCategoryMatName = mCategoryMatName;
    }

    public String getmCategoryMatDescription() {
        return mCategoryMatDescription;
    }

    public void setmCategoryMatDescription(String mCategoryMatDescription) {
        this.mCategoryMatDescription = mCategoryMatDescription;
    }
}
