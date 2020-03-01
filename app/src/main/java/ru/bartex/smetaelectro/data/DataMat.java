package ru.bartex.smetaelectro.data;

import java.io.Serializable;

public class DataMat implements Serializable {

    private long mMatTypeId;
    private String mMatName;
    private String mMatDescription;

    public DataMat( ){
        //пустой конструктор
    }
    //основной конструктор
    public DataMat(long matTypeId, String matName, String matDescription){
        mMatTypeId = matTypeId;
        mMatName = matName;
        mMatDescription = matDescription;
    }

    public long getmMatTypeId() {
        return mMatTypeId;
    }

    public void setmMatTypeId(long mMatTypeId) {
        this.mMatTypeId = mMatTypeId;
    }

    public String getmMatName() {
        return mMatName;
    }

    public void setmMatName(String mMatName) {
        this.mMatName = mMatName;
    }

    public String getmMatDescription() {
        return mMatDescription;
    }

    public void setmMatDescription(String mMatDescription) {
        this.mMatDescription = mMatDescription;
    }
}
