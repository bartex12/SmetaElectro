package ru.bartex.smetaelectro.ui.smetas2tabs;

import android.content.Intent;
import android.os.Bundle;
import ru.bartex.smetaelectro.ui.smetas2tabs.detailes.DetailSmetaLine;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.FW;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.Work;


//класс - фрагмент для вкладки Работа
public class SmetasTabWork extends AbstrSmetasTab {

    public static SmetasTabWork newInstance(long file_id, int position) {
        SmetasTabWork fragment = new SmetasTabWork();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    //метод получения адаптера
    @Override
    public SmetasTabRecyclerAdapter getSmetasTabRecyclerAdapter(){
        return new SmetasTabRecyclerAdapter(database, file_id, 0);
    }

    // метод чтобы получить слушатель щелчков на списке сметы работ
    @Override
    public SmetasTabRecyclerAdapter.OnClickOnLineListener getOnClickOnLineListener(){
        return  new SmetasTabRecyclerAdapter.OnClickOnLineListener() {
            @Override
            public void onClickOnLineListener(String nameItem) {
                long work_id = Work.getIdFromName(database, nameItem);
                long type_id = FW.getTypeId_FW(database, file_id, work_id);
                long cat_id = FW.getCatId_FW(database, file_id, work_id);

                Intent intent_work = new Intent(getActivity(), DetailSmetaLine.class);
                intent_work.putExtra(P.ID_FILE_DEFAULT, file_id);
                intent_work.putExtra(P.ID_CATEGORY, cat_id);
                intent_work.putExtra(P.ID_TYPE, type_id);
                intent_work.putExtra(P.ID_WORK, work_id);
                intent_work.putExtra(P.IS_WORK, true);  // такая работа есть
                startActivity(intent_work);
            }
        };
    }

}
