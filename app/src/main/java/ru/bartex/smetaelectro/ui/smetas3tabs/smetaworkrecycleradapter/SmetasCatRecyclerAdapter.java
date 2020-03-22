package ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.CategoryWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.TypeWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.Work;

public class SmetasCatRecyclerAdapter extends
        RecyclerView.Adapter<SmetasCatRecyclerAdapter.ViewHolder> {

    public static final String TAG = "33333";
    OnClickOnNamekListener nameListener;
    SQLiteDatabase database;
    long file_id;
    int positionTab;

    private String[] names;
    private boolean[] checked;
    private boolean isSelectedCat;
    private long cat_id;
    private boolean isSelectedType;
    private long type_id;

    public SmetasCatRecyclerAdapter(
            SQLiteDatabase database, long file_id, int positionTab,
            boolean isSelectedCat, long cat_id,
            boolean isSelectedType, long type_id) {

        Log.d(TAG, "***** SmetasCatRecyclerAdapter конструктор ****** ");

        this.database = database;
        this.file_id = file_id;
        this.positionTab = positionTab;
        this.isSelectedCat = isSelectedCat;
        this.cat_id = cat_id;
        this.isSelectedType = isSelectedType;
        this.type_id = type_id;

        //вычисляем названия и чекбокс выбора позиции
        getParams(database,file_id,positionTab,isSelectedCat,cat_id,isSelectedType,type_id);
    }

    public interface OnClickOnNamekListener{
        void nameTransmit(String name);
    }

    public void setOnClickOnNamekListener(OnClickOnNamekListener nameListener){
        this.nameListener = nameListener;
    }

    private void getParams(SQLiteDatabase database, long file_id, int positionTab,
                           boolean isSelectedCat, long cat_id,
                           boolean isSelectedType, long type_id) {
        switch (positionTab){
            case 0:
                Log.d(TAG, "***** SmetasCatRecyclerAdapter getParams()  case 0 ");
                //массив с именами категорий из таблицы категорий CategoryMat
                names = CategoryWork.getArrayCategoryWorkNames(database);
                //булевый массив - есть ли такая категория работы в списке работ с file_id
                checked = CategoryWork.getArrayCategoryWorkChecked(
                        database, file_id, names);
                break;

            case 1:
                Log.d(TAG, "***** SmetasCatRecyclerAdapter getParams()  case 1 " +
                        " isSelectedCat = " + isSelectedCat);
                if(isSelectedCat){
                    names = TypeWork.getArrayTypeWorkNamesFromCatId(database, cat_id);
                    Log.d(TAG, "***** SmetasCatRecyclerAdapter getParams()  case 1 " +
                           " isSelectedCat = " + isSelectedCat + "  names.length = " + names.length);
                }else {
                    //массив с именами категорий из таблицы категорий CategoryMat
                    names = TypeWork.getArrayTypeWorkNames(database);
                    Log.d(TAG, "***** SmetasCatRecyclerAdapter getParams()  case 1 " +
                            " isSelectedCat = " + isSelectedCat + "  names.length = " + names.length);
                }
                //булевый массив - есть ли такой тип работы в списке работ с file_id
                checked = TypeWork.getArrayTypeWorkChacked(
                        database, file_id, names);
                break;

            case 2:
                Log.d(TAG, "***** SmetasCatRecyclerAdapter getParams()  case 2 ");
                if(isSelectedCat){
                    if(isSelectedType){
                        names = Work.getArrayWorkNamesFromtypeId(database, type_id);
                    }else {
                        names =  Work.getArrayWorkNamesFromCatId(database, cat_id);
                    }
                }else {
                    if(isSelectedType){
                        names = Work.getArrayWorkNamesFromtypeId(database, type_id);
                    }else {
                        names = Work.getArrayWorkNames(database);
                    }
                }
                //булевый массив - есть ли такой тип работы в списке работ с file_id
                checked = Work.getArrayWorkChacked(
                        database, file_id, names);
                break;
        }
    }


    @NonNull
    @Override
    public SmetasCatRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_two_mat, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SmetasCatRecyclerAdapter.ViewHolder holder, final int position) {
        holder.checkBoxTwoMat.setChecked(checked[position]);
        holder.base_text.setText(names[position]);

        holder.llTwoMat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameListener.nameTransmit(names[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llTwoMat;
        CheckBox  checkBoxTwoMat;
        TextView base_text;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            llTwoMat = itemView.findViewById(R.id.llTwoMat);
            checkBoxTwoMat = itemView.findViewById(R.id.checkBoxTwoMat);
            base_text = itemView.findViewById(R.id.base_text);
        }
    }

    public void updateRecyclerAdapter(int positionTab,
                                      boolean isSelectedCat, long cat_id,
                                      boolean isSelectedType, long type_id){
        Log.d(TAG, "***** SmetasCatRecyclerAdapter updateRecyclerAdapter() ");
        //вычисляем названия и чекбокс выбора позиции
        getParams(database,file_id,positionTab,isSelectedCat,cat_id,isSelectedType,type_id);
        notifyDataSetChanged();
    }
    public void updateType(long cat_id){
        getParams(database, file_id, 1,true,cat_id,false,0);
        notifyDataSetChanged();
    }

    public void  updateName(long cat_id, long type_id){
        getParams(database, file_id, 2,true, cat_id,true, type_id);
        notifyDataSetChanged();
    }

}
