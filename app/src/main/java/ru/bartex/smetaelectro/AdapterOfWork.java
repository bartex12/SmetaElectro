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
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.Work;

public class AdapterOfWork {

    SmetaOpenHelper mSmetaOpenHelper;
    long type_id;
    Context ctx;
    ListView mListView;


    public AdapterOfWork(Context ctx,long type_id, ListView mListView){
        this.ctx = ctx;
        this.type_id = type_id;
        this.mListView = mListView;
        this.mSmetaOpenHelper  = new SmetaOpenHelper(ctx);
    }

    public void updateAdapter(){
        Cursor cursor = mSmetaOpenHelper.getWorkNames(type_id);
        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(cursor.getCount());
        Map<String,Object> m;

        while (cursor.moveToNext()){
            String name_work = cursor.getString(cursor.getColumnIndex(Work.COLUMN_WORK_NAME));
            int mark = cursor.getInt(cursor.getColumnIndex(Work.COLUMN_WORK_DONE));
            boolean  check_mark = false;
            if (mark == 0){
                check_mark = false;
            }else {
                check_mark = true;
            }
            m = new HashMap<>();
            m.put(P.ATTR_WORK_NAME,name_work);
            m.put(P.ATTR_WORK_MARK, check_mark);
            data.add(m);
        }
        String[] from = new String[]{P.ATTR_WORK_MARK, P.ATTR_WORK_NAME};
        int[] to = new int[]{R.id.checkBoxTwo, R.id.base_text_two};
        SimpleAdapter sara = new SimpleAdapter(ctx, data, R.layout.list_item_two, from, to);
        mListView.setAdapter(sara);
    }
}
