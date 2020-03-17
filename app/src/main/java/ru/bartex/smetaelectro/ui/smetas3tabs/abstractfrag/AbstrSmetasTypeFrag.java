package ru.bartex.smetaelectro.ui.smetas3tabs.abstractfrag;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.SmetaOpenHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class AbstrSmetasTypeFrag extends Fragment {

    public static final String TAG = "33333";
    public ListView listView;
    public long file_id;
    public int position;
    public SimpleAdapter sara;
    public ArrayList<Map<String, Object>> data;
    public Map<String, Object> m;
    public SQLiteDatabase database;
    public boolean isSelectedCat;
    public long cat_id;

    public abstract  void updateAdapter();
    public abstract  long getTypeId(String typeName);
    public abstract  long getCatId(long type_id);

    public interface OnClickTypekListener{
        void typeAndClickTransmit(long cat_id, long type_id, boolean isSelectedType);
    }
    OnClickTypekListener onClickTypeListener;

    public AbstrSmetasTypeFrag() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "//  AbstrSmetasTypeFrag onAttach // " );
        onClickTypeListener = (OnClickTypekListener)context;
        database = new SmetaOpenHelper(context).getWritableDatabase();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "//  AbstrSmetasTypeFrag onCreate // " );
        file_id = getArguments().getLong(P.ID_FILE);
        position = getArguments().getInt(P.TAB_POSITION);
        isSelectedCat =getArguments().getBoolean(P.IS_SELECTED_CAT);
        cat_id = getArguments().getLong(P.ID_CATEGORY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "//  AbstrSmetasTypeFrag onCreateView // " );
        View rootView = inflater.inflate(R.layout.fragment_tabs_for_works_and_materials, container, false);
        listView = rootView.findViewById(R.id.listViewFragmentTabs);

        //находим View, которое выводит текст Список пуст
        View empty = rootView.findViewById(android.R.id.empty);
        TextView tvEmpty = (TextView)empty;
        tvEmpty.setText(R.string.list_empty_tab);
        listView.setEmptyView(empty);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "//  AbstrSmetasTypeFrag onItemClick // " );
                TextView tv_smeta_item = view.findViewById(R.id.base_text);
                String smeta_item_name = tv_smeta_item.getText().toString();

                long type_id = getTypeId(smeta_item_name);
                long cat_id = getCatId(type_id);
                Log.d(TAG, "AbstrSmetasTypeFrag onItemClick  cat_id = " + cat_id + "  type_id = " + type_id);

                onClickTypeListener.typeAndClickTransmit(cat_id, type_id, true);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "//  AbstrSmetasTypeFrag onResume // " );

        updateAdapter();

        //объявляем о регистрации контекстного меню здесь, но как то это всё работает из SmetaMat?!
        registerForContextMenu(listView);
    }

}
