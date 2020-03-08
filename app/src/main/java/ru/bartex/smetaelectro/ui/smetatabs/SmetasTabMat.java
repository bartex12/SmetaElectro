package ru.bartex.smetaelectro.ui.smetatabs;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.bartex.smetaelectro.DetailSmetaMatLine;
import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.SmetaOpenHelper;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.files.FileWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.FM;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.Mat;

public class SmetasTabMat extends Fragment {

    public static final String TAG = "33333";
    private long file_id;
    private int positionItem;
    private SQLiteDatabase database;
    float[] summa; //массив стоимости
    float totalSumma; // общая стоимость

    RecyclerView recyclerView;
    SmetasRecyclerMatAdapter adapter;

    public static SmetasTabMat newInstance(long file_id, int position) {
        SmetasTabMat fragment = new SmetasTabMat();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    public SmetasTabMat() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        database = new SmetaOpenHelper(context).getWritableDatabase();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "// SmetasTabMat onCreate // " );
        //получаем id файла из аргументов
        file_id = getArguments().getLong(P.ID_FILE);
        positionItem = getArguments().getInt(P.TAB_POSITION);
        Log.d(TAG, "SmetasTabMat onCreate  file_id = " + file_id + "  positionItem = " +  positionItem);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mat_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initRecycler(view);
        //объявляем о регистрации контекстного меню
        registerForContextMenu(recyclerView);
    }

    private void initViews(View view) {
        TextView header = view.findViewById(R.id.header_mat_tab);
        String fileName = FileWork.getNameFromId(database, file_id);
        header.setText(String.format(Locale.getDefault(),"Смета: %s", fileName));

        TextView footer= view.findViewById(R.id.footer_mat_tab);
        summa = FM.getArraySumma(database, file_id);
        totalSumma = P.updateTotalSumma(summa);
        footer.setText(String.format(Locale.getDefault(),"За материалы: %.0f руб", totalSumma ));
    }


    private void initRecycler(View view) {
        recyclerView = view.findViewById(R.id.recycler_mat_tab);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        SmetasRecyclerMatAdapter.OnClickOnMatListener matListener =
                new SmetasRecyclerMatAdapter.OnClickOnMatListener() {
                    @Override
                    public void onClickOnMatListener(long file_id) {
                        String smeta_item_name = Mat.getNameFromId(database, file_id);
                        long mat_id = Mat.getIdFromName(database, smeta_item_name);
                        long type_mat_id = FM.getTypeId_FM(database, file_id, mat_id);
                        long cat_mat_id = FM.getCatId_FM(database, file_id, mat_id);

                        Intent intent_mat = new Intent(getActivity(), DetailSmetaMatLine.class);
                        intent_mat.putExtra(P.ID_FILE, file_id);
                        intent_mat.putExtra(P.ID_CATEGORY_MAT, cat_mat_id);
                        intent_mat.putExtra(P.ID_TYPE_MAT, type_mat_id);
                        intent_mat.putExtra(P.ID_MAT, mat_id);
                        intent_mat.putExtra(P.IS_MAT, true);  // такой материал есть
                        startActivity(intent_mat);
                    }
                };
        adapter = new SmetasRecyclerMatAdapter(database, file_id);
        recyclerView.setAdapter(adapter);
    }

    public SmetasRecyclerMatAdapter getAdapter(){
        return adapter;
    }
}
