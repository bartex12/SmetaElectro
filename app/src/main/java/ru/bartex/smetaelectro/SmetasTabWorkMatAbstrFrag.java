package ru.bartex.smetaelectro;


import android.content.Context;
import android.content.Intent;
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
public abstract class SmetasTabWorkMatAbstrFrag extends Fragment {

    public static final String TAG = "33333";
    ListView listView;
    long file_id;
    int position;
    SimpleAdapter sara;
    ArrayList<Map<String, Object>> data;
    Map<String, Object> m;

    SmetaOpenHelper mSmetaOpenHelper;
    boolean isSelectedType;
    long type_id;

    public SmetasTabWorkMatAbstrFrag() {
        // Required empty public constructor
    }

    public abstract  void updateAdapter();
    public abstract  void sendIntent(String name);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "//  SmetasTabWorkMatAbstrFrag onAttach // " );
        mSmetaOpenHelper = new SmetaOpenHelper(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "//  SmetasTabWorkMatAbstrFrag onCreate // " );
        file_id = getArguments().getLong(P.ID_FILE);
        position = getArguments().getInt(P.TAB_POSITION);
        isSelectedType = getArguments().getBoolean(P.IS_SELECTED_TYPE);
        type_id = getArguments().getLong(P.ID_TYPE);
        Log.d(TAG, "SmetasTabWorkMatAbstrFrag onCreate isSelectedType = " + isSelectedType +
                " file_id = " + file_id +" position = " + position+ " type_id = " + type_id);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "//  SmetasTabWorkMatAbstrFrag onCreateView // " );
        View rootView = inflater.inflate(R.layout.fragment_tabs_for_works_and_materials, container, false);
        listView = rootView.findViewById(R.id.listViewFragmentTabs);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parent.setSelection(position);
                //находим имя работы в адаптере
                TextView tv = view.findViewById(R.id.base_text);
                String work_name = tv.getText().toString();

                sendIntent(work_name);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "//  SmetasTabWorkMatAbstrFrag onResume // " );
        updateAdapter();
        //объявляем о регистрации контекстного меню здесь, но как то это всё работает из SmetaMat?!
        registerForContextMenu(listView);
    }

}
