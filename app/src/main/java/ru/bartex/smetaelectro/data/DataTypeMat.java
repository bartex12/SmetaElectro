package ru.bartex.smetaelectro.data;

import java.io.Serializable;

public class DataTypeMat implements Serializable {

    private long mTypeMatCategoryId;
    private String mTypeMatName;
    private String mTypeMatDescription;

    public DataTypeMat( ){
        //пустой конструктор
    }

    //основной конструктор
    public DataTypeMat(long mTypeMatCategoryId, String mTypeMatName, String mTypeMatDescription){
        this.mTypeMatCategoryId = mTypeMatCategoryId;
        this.mTypeMatName = mTypeMatName;
        this.mTypeMatDescription = mTypeMatDescription;
    }

    public String getmTypeMatName() {
        return mTypeMatName;
    }

    public void setmTypeMatName(String mTypeMatName) {
        this.mTypeMatName = mTypeMatName;
    }

    public String getmTypeMatDescription() {
        return mTypeMatDescription;
    }

    public void setmTypeMatDescription(String mTypeMatDescription) {
        this.mTypeMatDescription = mTypeMatDescription;
    }

    public long getmTypeMatCategoryId() {
        return mTypeMatCategoryId;
    }

    public void setmTypeMatCategoryId(long mTypeMatCategoryId) {
        this.mTypeMatCategoryId = mTypeMatCategoryId;
    }
}
