package ru.bartex.smetaelectro;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class SmetasTypeTab extends Fragment {

    public static final String TAG = "33333";
    ListView listView;
    long file_id;
    int position;
    SimpleAdapter sara;
    ArrayList<Map<String, Object>> data;
    Map<String, Object> m;
    SmetaOpenHelper mSmetaOpenHelper;
    boolean isSelectedCat;
    long cat_id;

    public abstract  void updateAdapter();
    public abstract  long getTypeId(String typeName);
    public abstract  long getCatId(long type_id);

    public interface OnClickTypekListener{
        void typeAndClickTransmit(long cat_id, long type_id, boolean isSelectedType);
    }
    OnClickTypekListener onClickTypeListener;

    public SmetasTypeTab() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "//  SmetasTypeTab onAttach // " );
        mSmetaOpenHelper = new SmetaOpenHelper(context);
        onClickTypeListener = (OnClickTypekListener)context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "//  SmetasTypeTab onCreate // " );
        file_id = getArguments().getLong(P.ID_FILE);
        position = getArguments().getInt(P.TAB_POSITION);
        isSelectedCat =getArguments().getBoolean(P.IS_SELECTED_CAT);
        cat_id = getArguments().getLong(P.ID_CATEGORY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "//  SmetasTypeTab onCreateView // " );
        View rootView = inflater.inflate(R.layout.fragment_tabs_for_works_and_materials, container, false);
        listView = rootView.findViewById(R.id.listViewFragmentTabs);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "//  SmetasTypeTab onItemClick // " );
                TextView tv_smeta_item = view.findViewById(R.id.base_text);
                String smeta_item_name = tv_smeta_item.getText().toString();

                long type_id = getTypeId(smeta_item_name);
                long cat_id = getCatId(type_id);
                Log.d(TAG, "SmetasTypeTab onItemClick  cat_id = " + cat_id + "  type_id = " + type_id);

                onClickTypeListener.typeAndClickTransmit(cat_id, type_id, true);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "//  SmetasTypeTab onResume // " );

        updateAdapter();

        //объявляем о регистрации контекстного меню здесь, но как то это всё работает из SmetaMat?!
        registerForContextMenu(listView);
    }

}
