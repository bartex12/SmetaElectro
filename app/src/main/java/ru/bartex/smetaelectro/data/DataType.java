package ru.bartex.smetaelectro.data;

import java.io.Serializable;

public class DataType implements Serializable {

    private long mTypeCategoryId;
    private String mTypeName;
    private String mTypeDescription;

    public DataType( ){
        //пустой конструктор
    }

    public long getmTypeCategoryId() {
        return mTypeCategoryId;
    }

    public void setmTypeCategoryId(long mTypeCategoryId) {
        this.mTypeCategoryId = mTypeCategoryId;
    }

    //основной конструктор
    public DataType(long typeCategoryId, String typeName, String typeDescription){
        mTypeCategoryId = typeCategoryId;
        mTypeName = typeName;
        mTypeDescription = typeDescription;
    }

    public String getmTypeName() {
        return mTypeName;
    }

    public void setmTypeName(String mTypeName) {
        this.mTypeName = mTypeName;
    }

    public String getmTypeDescription() {
        return mTypeDescription;
    }

    public void setmTypeDescription(String mTypeDescription) {
        this.mTypeDescription = mTypeDescription;
    }
}
