package ru.bartex.smetaelectro;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.CategoryMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.Mat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;

public abstract class SmetasTabCatAbstrFrag extends Fragment {

    public static final String TAG = "33333";
    ListView listView;
    long file_id;
    int position;
    SimpleAdapter sara;
    ArrayList<Map<String, Object>> data;
    Map<String, Object> m;

    SmetaOpenHelper mSmetaOpenHelper;

    public abstract  void updateAdapter();
    public abstract  long getCatId(String catName);

    public interface OnClickCatListener{
        void catAndClickTransmit(long cat_id, boolean isSelectedCat);
    }
    OnClickCatListener onClickCatListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "//  SmetasTabCatAbstrFrag onAttach // " );
        mSmetaOpenHelper = new SmetaOpenHelper(context);
        onClickCatListener = (OnClickCatListener)context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "//  SmetasTabCatAbstrFrag onCreate // " );
        file_id = getArguments().getLong(P.ID_FILE);
        position = getArguments().getInt(P.TAB_POSITION);
        Log.d(TAG, "SmetasTabCatAbstrFrag onCreate file_id = " + file_id );
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "//  SmetasTabCatAbstrFrag onCreateView // " );
        // Inflate the layout for this fragment
        View view = inflater.inflate(
                R.layout.fragment_tabs_for_works_and_materials, container, false);
        listView = view.findViewById(R.id.listViewFragmentTabs);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "//  SmetasTabCatAbstrFrag onItemClick // " );
                TextView tv_smeta_item = view.findViewById(R.id.base_text);
                String smeta_item_name = tv_smeta_item.getText().toString();

                long cat_id = getCatId(smeta_item_name);
                Log.d(TAG, "SmetasTabCatAbstrFrag onItemClick  cat_id = " + cat_id);

                onClickCatListener.catAndClickTransmit(cat_id, true);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "//  SmetasTabCatAbstrFrag onResume // " );
        updateAdapter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "--------  SmetasTabCatAbstrFrag onDestroy -------" );
    }
}
