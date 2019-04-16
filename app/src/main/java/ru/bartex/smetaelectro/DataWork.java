package ru.bartex.smetaelectro;

import java.io.Serializable;

public class DataWork implements Serializable {

    private long mWorkTypeId;
    private String mWorkName;
    private String mWorkDescription;

    public DataWork( ){
        //пустой конструктор
    }
    //основной конструктор
    public DataWork(long worTypeId, String workName, String workDescription){
        mWorkTypeId = worTypeId;
        mWorkName = workName;
        mWorkDescription = workDescription;
    }

    public long getmWorkCategoryId() {
        return mWorkTypeId;
    }

    public void setmWorkCategoryId(long mWorkTypeId) {
        this.mWorkTypeId = mWorkTypeId;
    }

    public String getmWorkName() {
        return mWorkName;
    }

    public void setmWorkName(String mWorkName) {
        this.mWorkName = mWorkName;
    }

    public String getmWorkDescription() {
        return mWorkDescription;
    }

    public void setmWorkDescription(String mWorkDescription) {
        this.mWorkDescription = mWorkDescription;
    }
}
