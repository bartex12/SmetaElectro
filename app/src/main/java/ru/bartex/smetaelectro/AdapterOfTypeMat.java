package ru.bartex.smetaelectro;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.TypeMat;

public class AdapterOfTypeMat {

    public static final String TAG = "33333";

    Context context;
    long file_id;
    //long cat_mat_id;
    SmetaOpenHelper smetaOpenHelper;
    SimpleAdapter sara;
    ArrayList<Map<String, Object>> data;
    Map<String, Object> m;
    ListView listView;

    public AdapterOfTypeMat(Context context, long file_id, ListView listView ){
        Log.d(TAG, "//  AdapterOfTypeMat Конструктор// " );
        this.context =context;
        this.file_id = file_id;
        //this.cat_mat_id = cat_mat_id;
        this.listView = listView;
        smetaOpenHelper = new SmetaOpenHelper(context);
    }
public void updateAdapter(){
    Log.d(TAG, "//  AdapterOfTypeMat updateAdapter // " );

    //Курсор с именами категорий материалов из таблицы категорий CategoryMat
    //Cursor cursor1 = smetaOpenHelper.getTypeMatNames(cat_mat_id);
    Cursor cursor = smetaOpenHelper.getTypeMatNamesAllTypes();
    //Строковый массив с именами типов материалов из таблицы FW для файла с file_id
    String[] typetMatNamesFW = smetaOpenHelper.getTypeNamesFM(file_id);

    data = new ArrayList<Map<String, Object>>(cursor.getCount());
    Log.d(TAG, " AdapterOfTypeMat updateAdapter cursor.getCount() = "+ cursor.getCount() );
    while (cursor.moveToNext()){
        String tipe_mat_name = cursor.getString(cursor.getColumnIndex(TypeMat.TYPE_MAT_NAME));
        boolean check_mark = false;
        for (int i=0; i<typetMatNamesFW.length; i++){
            if (typetMatNamesFW[i].equals(tipe_mat_name)){
                check_mark = true;
            }else {
                check_mark = false;
            }
        }
        m =new HashMap<>();
        m.put(P.ATTR_TYPE_MAT_NAME,tipe_mat_name);
        m.put(P.ATTR_TYPE_MAT_MARK, check_mark);
        data.add(m);
    }
    Log.d(TAG, " AdapterOfTypeMat updateAdapter data.size()  = "+ data.size() );
    String[] from = new String[]{P.ATTR_TYPE_MAT_NAME, P.ATTR_TYPE_MAT_MARK};
    int [] to = new int[]{R.id.base_text_two, R.id.checkBoxTwo};

    sara = new SimpleAdapter(context, data, R.layout.list_item_two, from, to);
    listView.setAdapter(sara);
  }
}
