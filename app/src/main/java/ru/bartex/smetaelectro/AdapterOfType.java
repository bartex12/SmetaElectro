package ru.bartex.smetaelectro;

import android.content.Context;
import android.database.Cursor;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.TypeWork;

public class AdapterOfType {
    SmetaOpenHelper mSmetaOpenHelper;
    long cat_id;
    Context ctx;
    ListView mListView;


    public AdapterOfType(Context ctx,long cat_id, ListView mListView){
        this.ctx = ctx;
        this.cat_id = cat_id;
        this.mListView = mListView;
        this.mSmetaOpenHelper  = new SmetaOpenHelper(ctx);
    }

    public void updateAdapter(){
        Cursor cursor = mSmetaOpenHelper.getTypeNames(cat_id);
        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(cursor.getCount());
        Map<String,Object> m;

        while (cursor.moveToNext()){
            String name_type = cursor.getString(cursor.getColumnIndex(TypeWork.COLUMN_TYPE_NAME));
            int mark = cursor.getInt(cursor.getColumnIndex(TypeWork.COLUMN_TYPE_MARK));
            boolean  check_mark = false;
            if (mark == 0){
                check_mark = false;
            }else {
                check_mark = true;
            }
            m = new HashMap<>();
            m.put(P.ATTR_TYPE_NAME,name_type);
            m.put(P.ATTR_TYPE_MARK, check_mark);
            data.add(m);
        }
        String[] from = new String[]{P.ATTR_TYPE_MARK, P.ATTR_TYPE_NAME};
        int[] to = new int[]{R.id.checkBoxTwo, R.id.base_text_two};
        SimpleAdapter sara = new SimpleAdapter(ctx, data, R.layout.list_item_two, from, to);
        mListView.setAdapter(sara);
    }
}
