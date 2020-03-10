package ru.bartex.smetaelectro.data;

public class DataFM {

    private long FM_FILE_ID;
    private String FM_FILE_NAME;
    private long FM_MAT_ID;
    private String FM_MAT_NAME;
    private long FM_MAT_TYPE_ID;
    private String FM_MAT_TYPE_NAME;
    private long FM_MAT_CATEGORY_ID;
    private String FM_MAT_CATEGORY_NAME;
    private float FM_MAT_COST;
    private int FM_MAT_COUNT;
    private String FM_MAT_UNIT;
    private float FM_MAT_SUMMA;

    public DataFM(){
        //пустой конструктор
    }

    public DataFM(long FM_FILE_ID, String FM_FILE_NAME, long FM_MAT_ID, String FM_MAT_NAME,
                  long FM_MAT_TYPE_ID, String FM_MAT_TYPE_NAME, long FM_MAT_CATEGORY_ID,
                  String FM_MAT_CATEGORY_NAME, float FM_MAT_COST, int FM_MAT_COUNT,
                  String FM_MAT_UNIT, float FM_MAT_SUMMA) {

        this.FM_FILE_ID = FM_FILE_ID;
        this.FM_FILE_NAME = FM_FILE_NAME;
        this.FM_MAT_ID = FM_MAT_ID;
        this.FM_MAT_NAME = FM_MAT_NAME;
        this.FM_MAT_TYPE_ID = FM_MAT_TYPE_ID;
        this.FM_MAT_TYPE_NAME = FM_MAT_TYPE_NAME;
        this.FM_MAT_CATEGORY_ID = FM_MAT_CATEGORY_ID;
        this.FM_MAT_CATEGORY_NAME = FM_MAT_CATEGORY_NAME;
        this.FM_MAT_COST = FM_MAT_COST;
        this.FM_MAT_COUNT = FM_MAT_COUNT;
        this.FM_MAT_UNIT = FM_MAT_UNIT;
        this.FM_MAT_SUMMA = FM_MAT_SUMMA;
    }

    public long getFM_FILE_ID() {
        return FM_FILE_ID;
    }

    public String getFM_FILE_NAME() {
        return FM_FILE_NAME;
    }

    public long getFM_MAT_ID() {
        return FM_MAT_ID;
    }

    public String getFM_MAT_NAME() {
        return FM_MAT_NAME;
    }

    public long getFM_MAT_TYPE_ID() {
        return FM_MAT_TYPE_ID;
    }

    public String getFM_MAT_TYPE_NAME() {
        return FM_MAT_TYPE_NAME;
    }

    public long getFM_MAT_CATEGORY_ID() {
        return FM_MAT_CATEGORY_ID;
    }

    public String getFM_MAT_CATEGORY_NAME() {
        return FM_MAT_CATEGORY_NAME;
    }

    public float getFM_MAT_COST() {
        return FM_MAT_COST;
    }

    public int getFM_MAT_COUNT() {
        return FM_MAT_COUNT;
    }

    public String getFM_MAT_UNIT() {
        return FM_MAT_UNIT;
    }

    public float getFM_MAT_SUMMA() {
        return FM_MAT_SUMMA;
    }
}
