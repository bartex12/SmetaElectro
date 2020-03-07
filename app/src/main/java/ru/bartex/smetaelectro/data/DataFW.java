package ru.bartex.smetaelectro.data;

public class DataFW {

    private long FW_File_ID;
    private String FW_File_Name;
    private long FW_Work_ID;
    private String FW_Work_Name;
    private long FW_Type_ID;
    private String FW_Type_Name;
    private long FW_Category_ID;
    private String FW_Catecgory_Name;
    private float FW_Cost;
    private int FW_Count;
    private String FW_Unit;
    private float FW_Summa;

    public DataFW(){
        //пустой конструктор
    }

    public DataFW(long FW_File_ID, String FW_File_Name, long FW_Work_ID, String FW_Work_Name,
                  long FW_Type_ID, String FW_Type_Name, long FW_Category_ID,
                  String FW_Catecgory_Name, float FW_Cost, int FW_Count,
                  String FW_Unit, float FW_Summa) {
        this.FW_File_ID = FW_File_ID;
        this.FW_File_Name = FW_File_Name;
        this.FW_Work_ID = FW_Work_ID;
        this.FW_Work_Name = FW_Work_Name;
        this.FW_Type_ID = FW_Type_ID;
        this.FW_Type_Name = FW_Type_Name;
        this.FW_Category_ID = FW_Category_ID;
        this.FW_Catecgory_Name = FW_Catecgory_Name;
        this.FW_Cost = FW_Cost;
        this.FW_Count = FW_Count;
        this.FW_Unit = FW_Unit;
        this.FW_Summa = FW_Summa;
    }

    public long getFW_File_ID() {
        return FW_File_ID;
    }

    public String getFW_File_Name() {
        return FW_File_Name;
    }

    public long getFW_Work_ID() {
        return FW_Work_ID;
    }

    public String getFW_Work_Name() {
        return FW_Work_Name;
    }

    public long getFW_Type_ID() {
        return FW_Type_ID;
    }

    public String getFW_Type_Name() {
        return FW_Type_Name;
    }

    public long getFW_Category_ID() {
        return FW_Category_ID;
    }

    public String getFW_Catecgory_Name() {
        return FW_Catecgory_Name;
    }

    public float getFW_Cost() {
        return FW_Cost;
    }

    public int getFW_Count() {
        return FW_Count;
    }

    public String getFW_Unit() {
        return FW_Unit;
    }

    public float getFW_Summa() {
        return FW_Summa;
    }
}
