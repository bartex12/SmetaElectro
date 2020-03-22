package ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.data.DataMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.FW;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.TypeWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.Work;

public class Mat {
    public static final String TAG = "33333";

    public Mat(){
        //пустой конструктор
    }
    public final static String TABLE_NAME = "Mat";

    public final static String _ID = BaseColumns._ID;
    public final static String MAT_NAME = "MAT_NAME";
    public final static String MAT_DESCRIPTION= "MAT_DESCRIPTION";
    public final static String MAT_TYPE_ID = "MAT_TYPE_ID";

    //создание таблицы
    public static void createTable(SQLiteDatabase db, Context fContext){
        // Строка для создания таблицы конкретных материалов Mat
        String SQL_CREATE_TAB_MAT = "CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MAT_TYPE_ID + " INTEGER NOT NULL, "
                + MAT_NAME + " TEXT NOT NULL, "
                + MAT_DESCRIPTION + " TEXT NOT NULL DEFAULT 'Без описания');";
        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TAB_MAT);
        Log.d(TAG, "Mat - onCreate- создание таблицы Mat");
        // Если файлов в базе нет, вносим записи названия  материалов (добавление из программы)
        createDefaultMat(db, fContext);
    }

    //создаём работы из ресурсов и получаем COLUMN_TYPE_CATEGORY_ID из  this.getArrayCategoryId()
    private static void createDefaultMat(SQLiteDatabase db, Context fContext) {

        Log.i(TAG, "Mat.createDefaultMat...");
        // Добавляем записи в таблицу
        ContentValues values = new ContentValues();
        // Получим массив строк из ресурсов
        Resources res = fContext.getResources();
        String[] mat_name_type_kabeli = res.getStringArray(R.array.mat_name_type_kabeli);
        String[] mat_name_type_kabel_kanali = res.getStringArray(R.array.mat_name_type_kabel_kanali);
        String[] mat_name_type_korobki = res.getStringArray(R.array.mat_name_type_korobki);
        String[] mat_name_type_vremianki = res.getStringArray(R.array.mat_name_type_vremianki);
        String[] mat_name_type_rozetki = res.getStringArray(R.array.mat_name_type_rozetki);
        String[] mat_name_type_shchity = res.getStringArray(R.array.mat_name_type_shchity);
        String[] mat_name_type_led = res.getStringArray(R.array.mat_name_type_led);
        String[] mat_name_type_svetilniki = res.getStringArray(R.array.mat_name_type_svetilniki);
        String[] mat_name_type_bury = res.getStringArray(R.array.mat_name_type_bury);
        String[] mat_name_type_krepez = res.getStringArray(R.array.mat_name_type_krepez);
        String[] mat_name_type_sypuchie = res.getStringArray(R.array.mat_name_type_sypuchie);
        String[] mat_name_type_vspomogat = res.getStringArray(R.array.mat_name_type_vspomogat);
        String[] mat_name_type_instr = res.getStringArray(R.array.mat_name_type_instr);
        String[] mat_name_type_obschestroy = res.getStringArray(R.array.mat_name_type_obschestroy);

        //получаем массив type_id  таблицы типов Types
        int[] type_id = getArrayTypeIdMat(db);
        // проходим через массив и вставляем записи в таблицу
        int ii = 0; //mat_name_type_kabeli
        for (String s : mat_name_type_kabeli) {
            InsertMat(db, values, type_id[ii], s);
        }
        ii = 1; //mat_name_type_kabel_kanali
        for (String s : mat_name_type_kabel_kanali) {
            InsertMat(db, values, type_id[ii], s);
        }
        ii = 2; //mat_name_type_korobki
        for (String s : mat_name_type_korobki) {
            InsertMat(db, values, type_id[ii], s);
        }
        ii = 3; //mat_name_type_vremianki
        for (String s : mat_name_type_vremianki) {
            InsertMat(db, values, type_id[ii], s);
        }
        ii = 4; //mat_name_type_rozetki
        for (String s : mat_name_type_rozetki) {
            InsertMat(db, values, type_id[ii], s);
        }
        ii = 5; //mat_name_type_shchity
        for (String s : mat_name_type_shchity) {
            InsertMat(db, values, type_id[ii], s);
        }
        ii = 6; //mat_name_type_led
        for (String s : mat_name_type_led) {
            InsertMat(db, values, type_id[ii], s);
        }
        ii = 7; //mat_name_type_svetilniki
        for (String s : mat_name_type_svetilniki) {
            InsertMat(db, values, type_id[ii], s);
        }
        ii = 8; //mat_name_type_bury
        for (String s : mat_name_type_bury) {
            InsertMat(db, values, type_id[ii], s);
        }
        ii = 9; //mat_name_type_krepez
        for (String s : mat_name_type_krepez) {
            InsertMat(db, values, type_id[ii], s);
        }
        ii = 10; //mat_name_type_sypuchie
        for (String s : mat_name_type_sypuchie) {
            InsertMat(db, values, type_id[ii], s);
        }
        ii = 11; //mat_name_type_vspomogat
        for (String s : mat_name_type_vspomogat) {
            InsertMat(db, values, type_id[ii], s);
        }
        ii = 12; //mat_name_type_instr
        for (String s : mat_name_type_instr) {
            InsertMat(db, values, type_id[ii], s);
        }
        ii = 13; //mat_name_type_obschestroy
        for (String s : mat_name_type_obschestroy) {
            InsertMat(db, values, type_id[ii], s);
        }
        Log.d(TAG, "Mat.createDefaultMat type_id.length = " +
                type_id.length + " mat_name_type_kabeli.length = " + mat_name_type_kabeli.length);
    }

    //получаем курсор с Mat_Type_id  и делаем массив id
    private static int[] getArrayTypeIdMat(SQLiteDatabase db) {

        Log.i(TAG, "Mat.getArrayTypeIdMat ... ");
        String typeIdMat = " SELECT " + TypeMat._ID  + " FROM " + TypeMat.TABLE_NAME;
        Cursor cursor = db.rawQuery(typeIdMat, null);
        Log.i(TAG, "Mat.getArrayTypeIdMat cursor.getCount() = " + cursor.getCount());
        int[] type_id_mat = new int[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()){
            int position = cursor.getPosition();
            type_id_mat[position] = cursor.getInt(cursor.getColumnIndex(TypeMat._ID));
        }
        cursor.close();
        return type_id_mat;
    }

    private static void InsertMat(SQLiteDatabase db, ContentValues values,
                           int type_id, String work_name){
        values.put(MAT_TYPE_ID, type_id);
        values.put(MAT_NAME, work_name);
        db.insert(TABLE_NAME, null, values);
    }

    //получаем данные по материалу  по  id
    public static DataMat getDataMat(SQLiteDatabase db, long mat_id) {
        Log.i(TAG, "Mat.getMatData ... ");
        DataMat dataMat = new DataMat();

        String matData = " SELECT  * FROM " + Mat.TABLE_NAME +
                " WHERE " + Mat._ID + " = ? ";
        Cursor cursor = db.rawQuery(matData, new String[]{String.valueOf(mat_id)});

        if (cursor.moveToFirst()) {
            // Узнаем индекс каждого столбца и Используем индекс для получения строки
            long currentMatTypeId = cursor.getLong(cursor.getColumnIndex(Mat.MAT_TYPE_ID));
            String currentMatName = cursor.getString(cursor.getColumnIndex(Mat.MAT_NAME));
            String currentMatDescription = cursor.getString(cursor.getColumnIndex(Mat.MAT_DESCRIPTION));
            Log.d(TAG, "Mat.getMatData currentMatName = " + currentMatName);
            //создаём экземпляр класса DataWork в конструкторе
            dataMat = new DataMat(currentMatTypeId, currentMatName, currentMatDescription);
        }
        cursor.close();
        return dataMat;
    }

    //получаем id материала по имени
    public static long getIdFromName(SQLiteDatabase db, String name) {
        Log.i(TAG, "Mat.getIdFromName ... ");
        long currentID = -1;
        Cursor cursor = db.query(
                TABLE_NAME,                     // таблица
                new String[]{_ID},            // столбцы
                MAT_NAME + "=?",    // столбцы для условия WHERE
                new String[]{name},                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        if (cursor.moveToFirst()) {
            // получаем id по индексу
            currentID = cursor.getLong(cursor.getColumnIndex(_ID));
        }
        cursor.close();
        return currentID;
    }

    //получаем id типа материала по имени
    public static long getTypeIdFromName(SQLiteDatabase db, String name) {
        Log.i(TAG, "Mat.getTypeIdFromName ... ");
        long currentID = -1;
        Cursor cursor = db.query(
                TABLE_NAME,                     // таблица
                new String[]{MAT_TYPE_ID},            // столбцы
                MAT_NAME + "=?",    // столбцы для условия WHERE
                new String[]{name},                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        if (cursor.moveToFirst()) {
            // получаем id по индексу
            currentID = cursor.getLong(cursor.getColumnIndex(MAT_TYPE_ID));
        }
        cursor.close();
        return currentID;
    }

    //получаем имя материала по  id
    public static String getNameFromId(SQLiteDatabase db, long id) {
        Log.i(TAG, "Mat getNameFromId... ");

        String currentName = "";

        String name = " SELECT " + _ID + " , " + MAT_NAME +
                " FROM " + TABLE_NAME + " WHERE " + _ID + " = ?";
        Cursor cursor = db.rawQuery(name, new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            // Используем индекс для получения id
            currentName = cursor.getString(cursor.getColumnIndex(MAT_NAME));
        }
        cursor.close();
        return currentName;
    }

    //удаляем категорию материала из таблицы CategoryMat по id категории материала
    public static void deleteObject(SQLiteDatabase db, long id) {
        Log.i(TAG, "Mat.deleteObject  case Mat ");
        db.delete(TABLE_NAME, _ID + " =? ", new String[]{String.valueOf(id)});
    }

    //Обновляем данные по  материалам
    public static void updateData(SQLiteDatabase db, long id, String name, String description) {
        Log.i(TAG, "Mat.updateData ...");

        //заполняем данные для обновления в базе
        ContentValues cv = new ContentValues();
        cv.put(MAT_NAME, name);
        cv.put(MAT_DESCRIPTION, description);
        db.update(TABLE_NAME, cv, _ID + "=" + id, null);

        Log.i(TAG, "Mat.updateData - name =" + name + "  id = " + id);
    }

    //получаем курсор с названиями  материалов
    public static Cursor getNamesAllTypes(SQLiteDatabase db) {
        Log.i(TAG, "Mat.getNamesAllTypes ... ");
        String  names = " SELECT " + _ID + " , " +
                        MAT_NAME + " FROM " + TABLE_NAME;
        Cursor  cursor = db.rawQuery(names, null);
        Log.i(TAG, "Mat.getNamesAllTypes cursor.getCount() =  " + cursor.getCount());
        return cursor;
    }

    //получаем курсор с названиями типов материалов
    public static Cursor getNamesFromCatId(SQLiteDatabase db, long id) {
        Log.i(TAG, "Mat.getNamesFromCatId ... ");
        String  select = " SELECT " + _ID + " , " +
                MAT_NAME + " FROM " + TABLE_NAME +
                " WHERE " + MAT_TYPE_ID  + " = ? " ;
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(id)});
        Log.i(TAG, "Mat.getNamesFromCatId cursor.getCount() =  " + cursor.getCount()+
                "  id = " + id);
        return cursor;
    }

    //Добавляем  материал
    public static long  insertTypeCatName(SQLiteDatabase db, String name, long id){
        Log.i(TAG, "Mat.insertTypeCatName ... ");
        long _id =-1;

        ContentValues cv = new ContentValues();
        cv.put(MAT_NAME,name);
        cv.put(MAT_TYPE_ID, id);
        // вставляем строку
        _id = db.insert(TABLE_NAME, null, cv);
        Log.d(TAG, "Mat.insertTypeCatName  _id = " + _id);
        return _id;
    }

    //получаем количество видов материалов с типом MAT_TYPE_ID
    public static int getCountLine(SQLiteDatabase db, long id){
        Log.i(TAG, "Mat.getCountLine ... ");

        String   select = " SELECT " + _ID + " FROM " + TABLE_NAME +
                " where " + MAT_TYPE_ID + " =? ";
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(id)});

        int count = cursor.getCount();
        Log.i(TAG, "Mat.getCountLine count = " + count);
        cursor.close();
        return count;
    }

    //получаем массив строк  с названиями  работ по WORK_TYPE_ID
    public static String[] getArrayMatNamesFromtypeId(SQLiteDatabase db, long type_id) {
        Log.i(TAG, "Mat.getArrayMatNamesFromtypeId ... ");

        String  select = " SELECT " + _ID + " , " + MAT_TYPE_ID + " , " +
                MAT_NAME + " FROM " + TABLE_NAME +
                " WHERE " + MAT_TYPE_ID  + " = ?" ;
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(type_id)});

        String[] names = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            names[position] = cursor.getString(cursor.getColumnIndex(MAT_NAME));
        }
        cursor.close();
        return names;
    }

    //получаем массив строк  с названиями  работ по WORK_TYPE_ID
    public static String[] getArrayMatNamesFromCatId(SQLiteDatabase db, long cat_id) {
        Log.i(TAG, "Mat.getArrayMatNamesFromCatId ... ");

        String  select = " SELECT " + _ID + " , " + MAT_TYPE_ID + " , " +
                MAT_NAME + " FROM " + TABLE_NAME +
                " WHERE " + MAT_TYPE_ID  + " IN " +
                "(" + " SELECT " + TypeWork._ID +
                " FROM " + TypeWork.TABLE_NAME +
                " WHERE " + TypeWork.TYPE_CATEGORY_ID + " = ?" + ")";

        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(cat_id)});

        String[] names = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            names[position] = cursor.getString(cursor.getColumnIndex(MAT_NAME));
        }
        cursor.close();
        return names;
    }

    //получаем массив строк  с названиями категорий
    public static String[] getArrayMatNames(SQLiteDatabase db) {
        Log.i(TAG, "Mat.getArrayMatNames ... ");

        String select =  " SELECT " + _ID + " , " +
                MAT_NAME + " FROM " + TABLE_NAME;
        Cursor  cursor = db.rawQuery(select, null);

        String[] names = new String[cursor.getCount()];
        // Проходим через все строки в курсоре
        while (cursor.moveToNext()) {
            int position = cursor.getPosition();
            names[position] = cursor.getString(cursor.getColumnIndex(MAT_NAME));
        }
        cursor.close();
        return names;
    }

    //получаем boolean массив с отметкой - есть ли такая позиция категории в смете
    public static  boolean[] getArrayMatChacked(
            SQLiteDatabase db, long file_id, String[] names) {
        Log.i(TAG, "Mat.getArrayMatChacked ... ");
        String[] workNamesFW = FM.getNames_FM(db, file_id);
        boolean[] workChacked = new boolean[names.length];

        for (int i = 0; i<names.length; i++)
            for (String s : workNamesFW) {
                if (names[i].equals(s)) {
                    workChacked[i] = true;
                    //если есть совпадение, прекращаем перебор
                    break;
                }
            }
        return workChacked;
    }
}
