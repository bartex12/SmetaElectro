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

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.TableControllerSmeta;

public abstract class Tab1SmetasCatAbstrFrag extends Fragment {

    public static final String TAG = "33333";
    ListView listView;
    long file_id;
    int position;
    SimpleAdapter sara;
    ArrayList<Map<String, Object>> data;
    Map<String, Object> m;

    TableControllerSmeta tableControllerSmeta;

    public abstract  void updateAdapter();
    public abstract  long getCatId(String catName);

    public interface OnClickCatListener{
        void catAndClickTransmit(long cat_id, boolean isSelectedCat);
    }
    OnClickCatListener onClickCatListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "//  Tab1SmetasCatAbstrFrag onAttach // " );
        tableControllerSmeta  = new TableControllerSmeta(context);
        onClickCatListener = (OnClickCatListener)context;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "//  Tab1SmetasCatAbstrFrag onCreate // " );
        file_id = getArguments().getLong(P.ID_FILE);
        position = getArguments().getInt(P.TAB_POSITION);
        Log.d(TAG, "Tab1SmetasCatAbstrFrag onCreate file_id = " + file_id );
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "//  Tab1SmetasCatAbstrFrag onCreateView // " );
        // Inflate the layout for this fragment
        View view = inflater.inflate(
                R.layout.fragment_tabs_for_works_and_materials, container, false);
        listView = view.findViewById(R.id.listViewFragmentTabs);

        //находим View, которое выводит текст Список пуст
        View empty = view.findViewById(android.R.id.empty);
        TextView tvEmpty = (TextView)empty;
        tvEmpty.setText(R.string.list_empty_tab);
        listView.setEmptyView(empty);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "//  Tab1SmetasCatAbstrFrag onItemClick // " );
                TextView tv_smeta_item = view.findViewById(R.id.base_text);
                String smeta_item_name = tv_smeta_item.getText().toString();

                long cat_id = getCatId(smeta_item_name);
                Log.d(TAG, "Tab1SmetasCatAbstrFrag onItemClick  cat_id = " + cat_id);

                onClickCatListener.catAndClickTransmit(cat_id, true);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "//  Tab1SmetasCatAbstrFrag onResume // " );
        updateAdapter();

        //объявляем о регистрации контекстного меню здесь, но как то это всё работает из SmetaMat?!
        registerForContextMenu(listView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "--------  Tab1SmetasCatAbstrFrag onDestroy -------" );
    }
}
