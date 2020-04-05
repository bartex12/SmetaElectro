package ru.bartex.smetaelectro.w_hlam;

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
import ru.bartex.smetaelectro.database.mat.CategoryMat;
import ru.bartex.smetaelectro.database.mat.Mat;
import ru.bartex.smetaelectro.database.mat.TypeMat;


public class SmetasMatRecyclerAdapter extends
        RecyclerView.Adapter<SmetasMatRecyclerAdapter.ViewHolder> {


    public static final String TAG = "33333";
    private OnClickOnNamekListener nameListener;
    private SQLiteDatabase database;
    private long file_id;
    private int positionTab;

    private String[] names;
    private boolean[] checked;
    private boolean isSelectedCat;
    private long cat_id;
    private boolean isSelectedType;
    private long type_id;

    public SmetasMatRecyclerAdapter(
            SQLiteDatabase database, long file_id, int positionTab,
            boolean isSelectedCat, long cat_id,
            boolean isSelectedType, long type_id) {

        Log.d(TAG, "***** SmetasMatRecyclerAdapter конструктор ****** ");

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
                Log.d(TAG, "***** SmetasMatRecyclerAdapter getParams()  case 0 ");
                //массив с именами категорий из таблицы категорий CategoryMat
                names = CategoryMat.getArrayCategoryMatNames(database);
                //булевый массив - есть ли такая категория работы в списке работ с file_id
                checked = CategoryMat.getArrayCategoryMatChecked(
                        database, file_id, names);
                break;

            case 1:
                Log.d(TAG, "***** SmetasMatRecyclerAdapter getParams()  case 1 " +
                        " isSelectedCat = " + isSelectedCat);
                if(isSelectedCat){
                    names = TypeMat.getArrayTypeMatNamesFromCatId(database, cat_id);
                    Log.d(TAG, "***** SmetasMatRecyclerAdapter getParams()  case 1 " +
                            " isSelectedCat = " + isSelectedCat + "  names.length = " + names.length);
                }else {
                    //массив с именами категорий из таблицы категорий CategoryMat
                    names = TypeMat.getArrayTypeMatNames(database);
                    Log.d(TAG, "***** SmetasMatRecyclerAdapter getParams()  case 1 " +
                            " isSelectedCat = " + isSelectedCat + "  names.length = " + names.length);
                }
                //булевый массив - есть ли такой тип работы в списке работ с file_id
                checked = TypeMat.getArrayTypeMatChacked(
                        database, file_id, names);
                break;

            case 2:
                Log.d(TAG, "***** SmetasMatRecyclerAdapter getParams()  case 2 ");
                if(isSelectedCat){
                    if(isSelectedType){
                        names = Mat.getArrayMatNamesFromtypeId(database, type_id);
                    }else {
                        names =  Mat.getArrayMatNamesFromCatId(database, cat_id);
                    }
                }else {
                    if(isSelectedType){
                        names = Mat.getArrayMatNamesFromtypeId(database, type_id);
                    }else {
                        names = Mat.getArrayMatNames(database);
                    }
                }
                //булевый массив - есть ли такой тип работы в списке работ с file_id
                checked = Mat.getArrayMatChacked(
                        database, file_id, names);
                break;
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_two_mat, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
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
        CheckBox checkBoxTwoMat;
        TextView base_text;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            llTwoMat = itemView.findViewById(R.id.llTwoMat);
            checkBoxTwoMat = itemView.findViewById(R.id.checkBoxTwoMat);
            base_text = itemView.findViewById(R.id.base_text);
        }
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
