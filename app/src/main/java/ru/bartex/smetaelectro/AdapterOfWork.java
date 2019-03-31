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
    long file_id;
    long type_id;
    Context ctx;
    ListView mListView;


    public AdapterOfWork(Context ctx,long file_id,long type_id, ListView mListView){
        this.ctx = ctx;
        this.file_id = file_id;
        this.type_id = type_id;
        this.mListView = mListView;
        this.mSmetaOpenHelper  = new SmetaOpenHelper(ctx);
    }

    public void updateAdapter(){
        //Курсор с именами работ с типом type_id
        Cursor cursor = mSmetaOpenHelper.getWorkNames(type_id);
        //Строковый массив с именами работы из таблицы FW для файла с file_id
        String[] workNamesFW = mSmetaOpenHelper.getWorkNamesFW(file_id);

        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(cursor.getCount());
        Map<String,Object> m;

        while (cursor.moveToNext()){
            String name_work = cursor.getString(cursor.getColumnIndex(Work.WORK_NAME));
            boolean check_mark = false;
            for (int i = 0; i < workNamesFW.length; i++){
                if (name_work.equalsIgnoreCase(workNamesFW[i])){
                    check_mark = true;
                }
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
