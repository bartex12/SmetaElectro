package ru.bartex.smetaelectro;

import java.io.Serializable;

public class DataFile implements Serializable {

    private String mFileName;
    private String mAdress;
    private String mFileNameDate;
    private String mFileNameTime;
    private String mDescription;

    public DataFile( ){
        //пустой конструктор
    }

    //основной конструктор
    public DataFile(String FileName, String Adress, String FileNameDate,
                    String FileNameTime, String Description){

        mFileName = FileName;
        mAdress = Adress;
        mFileNameDate = FileNameDate;
        mFileNameTime = FileNameTime;
        mDescription = Description;
    }

    public String getFileName() {
        return mFileName;
    }

    public void setFileName(String fileName) {
        mFileName = fileName;
    }

    public String getAdress() {
        return mAdress;
    }

    public void setAdress(String adress) {
        mAdress = adress;
    }

    public String getFileNameDate() {
        return mFileNameDate;
    }

    public void setFileNameDate(String fileNameDate) {
        mFileNameDate = fileNameDate;
    }

    public String getFileNameTime() {
        return mFileNameTime;
    }

    public void setFileNameTime(String fileNameTime) {
        mFileNameTime = fileNameTime;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }
}
