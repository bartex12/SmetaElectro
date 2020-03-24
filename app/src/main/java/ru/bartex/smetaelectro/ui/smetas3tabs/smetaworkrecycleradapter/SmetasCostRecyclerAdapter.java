package ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.CategoryWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.CostWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.TypeWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.Work;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkcost.WorkNameCost;

public class SmetasCostRecyclerAdapter extends
        RecyclerView.Adapter<SmetasCostRecyclerAdapter.ViewHolder> {

    public static final String TAG = "33333";
    private OnClickOnNamekListener nameListener;
    private SQLiteDatabase database;
    private long file_id;
    private int positionTab;
    private int posItem;  //позиция в списке
    private Context context;

    private String[] names;
    //private boolean[] checked;
    private boolean isSelectedCat;
    private long cat_id;
    private boolean isSelectedType;
    private long type_id;
    private Kind kind;

    public SmetasCostRecyclerAdapter(SQLiteDatabase database, Kind kind,
                                     long file_id, int positionTab,
                                     boolean isSelectedCat, long cat_id,
                                     boolean isSelectedType, long type_id) {
        Log.d(TAG, "***** SmetasWorkRecyclerAdapter конструктор ****** ");

        this.database = database;
        this.file_id = file_id;
        this.positionTab = positionTab;
        this.isSelectedCat = isSelectedCat;
        this.cat_id = cat_id;
        this.isSelectedType = isSelectedType;
        this.type_id = type_id;
        this.kind = kind;

        //вычисляем названия и чекбокс выбора позиции
        getParams(database, kind,
                positionTab, isSelectedCat, cat_id, isSelectedType, type_id);
    }

    public interface OnClickOnNamekListener {
        void nameTransmit(String name);
    }

    public void setOnClickOnNamekListener(OnClickOnNamekListener nameListener) {
        this.nameListener = nameListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_single_mat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.base_text_cost.setText(names[position]);

        holder.base_text_cost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameListener.nameTransmit(names[position]);
            }
        });

        holder.base_text_cost.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                posItem = position;
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        
        TextView base_text_cost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            base_text_cost = itemView.findViewById(R.id.base_text_cost);
        }
    }

    private void getParams(SQLiteDatabase database,
                           Kind kind, int positionTab,
                           boolean isSelectedCat, long cat_id,
                           boolean isSelectedType, long type_id) {
        switch (positionTab) {
            case 0:
                switch (kind){
                    case COST_WORK:
                        getParamsCategoryWorkCost(database);
                        break;
                    case COST_MAT:

                        break;
                }
                break;

            case 1:
                switch (kind){
                    case COST_WORK:
                        getParamsTypeWorkCost(database, isSelectedCat, cat_id);
                        break;
                    case COST_MAT:

                        break;
                }
                break;

            case 2:
                switch (kind){
                    case COST_WORK:
                        getParamsNameWorkCost(database,
                                isSelectedCat, cat_id, isSelectedType, type_id);
                        break;
                    case COST_MAT:

                        break;
                }
                break;
        }
    }

        private void getParamsNameWorkCost(SQLiteDatabase database,
                                           boolean isSelectedCat, long cat_id,
                                           boolean isSelectedType, long type_id) {
            Log.d(TAG, "***** SmetasCostRecyclerAdapter getParams()  case 2 ");
            if (isSelectedCat) {
                if (isSelectedType) {
                    names = Work.getArrayWorkNamesFromtypeId(database, type_id);
                } else {
                    names = Work.getArrayWorkNamesFromCatId(database, cat_id);
                }
            } else {
                if (isSelectedType) {
                    names = Work.getArrayWorkNamesFromtypeId(database, type_id);
                } else {
                    names = Work.getArrayWorkNames(database);
                }
            }
    }

    private void getParamsTypeWorkCost(SQLiteDatabase database,
                                       boolean isSelectedCat, long cat_id) {
        Log.d(TAG, "***** SmetasCostRecyclerAdapter getParams()  case 1 " +
                " isSelectedCat = " + isSelectedCat);
        if (isSelectedCat) {
            //массив с именами категорий из таблицы категорий TypeWork с cat_id
            names = TypeWork.getArrayTypeWorkNamesFromCatId(database, cat_id);
            Log.d(TAG, "***** SmetasCostRecyclerAdapter getParams()  case 1 " +
                    " isSelectedCat = " + isSelectedCat + "  names.length = " + names.length);
        } else {
            //массив с именами категорий из таблицы категорий TypeWork
            names = TypeWork.getArrayTypeWorkNames(database);
            Log.d(TAG, "***** SmetasCostRecyclerAdapter getParams()  case 1 " +
                    " isSelectedCat = " + isSelectedCat + "  names.length = " + names.length);
        }
    }

    private void getParamsCategoryWorkCost(SQLiteDatabase database) {
        Log.d(TAG, "***** SmetasCostRecyclerAdapter getParams()  case 0 ");
        //массив с именами категорий из таблицы категорий CategoryWork
        names = CategoryWork.getArrayCategoryWorkNames(database);
    }

    public void updateWorkCostType(long cat_id) {
        getParams(database, Kind.COST_WORK, 1, true, cat_id, false, 0);
        notifyDataSetChanged();
    }

    public void updateWorkCostName(long cat_id, long type_id) {
        getParams(database, Kind.COST_WORK, 2, true, cat_id, true, type_id);
        notifyDataSetChanged();
    }

}
