package ru.bartex.smetaelectro.ui.smetas2tabs;

import android.content.Intent;
import android.os.Bundle;

import ru.bartex.smetaelectro.ui.smetas2tabs.adapters.SmetasTabRecyclerAdapter;
import ru.bartex.smetaelectro.ui.smetas3tabs.workmat.mat.detmat.DetailSmetaMatLine;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.FM;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.Mat;

//класс - фрагмент для вкладки Материалы
public class SmetasTabMat extends AbstrSmetasTab {

    public static SmetasTabMat newInstance(long file_id, int position) {
        SmetasTabMat fragment = new SmetasTabMat();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    //метод получения адаптера
    @Override
    public SmetasTabRecyclerAdapter getSmetasTabRecyclerAdapter(){
        return new SmetasTabRecyclerAdapter(database, file_id, 1);
    }

    // метод чтобы получить слушатель щелчков на списке сметы материалов
    @Override
    public SmetasTabRecyclerAdapter.OnClickOnLineListener getOnClickOnLineListener(){
        return  new SmetasTabRecyclerAdapter.OnClickOnLineListener() {
            @Override
            public void onClickOnLineListener(String namtItem) {
                long mat_id = Mat.getIdFromName(database, namtItem);
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
    }

}
