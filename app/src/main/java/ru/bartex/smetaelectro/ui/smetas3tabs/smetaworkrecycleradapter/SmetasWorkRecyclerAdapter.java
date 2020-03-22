package ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter;

import android.content.Context;
import android.content.Intent;
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
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.FM;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.Mat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.CategoryWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.FW;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.TypeWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.Work;
import ru.bartex.smetaelectro.ui.smetas3tabs.changedata.changedatawork.ChangeDataCategory;
import ru.bartex.smetaelectro.ui.smetas3tabs.changedata.changedatawork.ChangeDataType;
import ru.bartex.smetaelectro.ui.smetas3tabs.changedata.changedatawork.ChangeDataWork;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetawork.SmetasWork;
import ru.bartex.smetaelectro.ui.smetas3tabs.specific.SpecificCategory;
import ru.bartex.smetaelectro.ui.smetas3tabs.specific.SpecificType;
import ru.bartex.smetaelectro.ui.smetas3tabs.specific.SpesificWork;

public class SmetasWorkRecyclerAdapter extends
        RecyclerView.Adapter<SmetasWorkRecyclerAdapter.ViewHolder> {

    public static final String TAG = "33333";
    OnClickOnNamekListener nameListener;
    SQLiteDatabase database;
    long file_id;
    int positionTab;
    private int posItem;  //позиция в списке
    Context context;

    private String[] names;
    private boolean[] checked;
    private boolean isSelectedCat;
    private long cat_id;
    private boolean isSelectedType;
    private long type_id;

    public SmetasWorkRecyclerAdapter(
            SQLiteDatabase database, long file_id, int positionTab,
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

        //вычисляем названия и чекбокс выбора позиции
        getParams(database, file_id, positionTab, isSelectedCat, cat_id, isSelectedType, type_id);
    }

    public interface OnClickOnNamekListener {
        void nameTransmit(String name);
    }

    public void setOnClickOnNamekListener(OnClickOnNamekListener nameListener) {
        this.nameListener = nameListener;
    }

    private void getParams(SQLiteDatabase database, long file_id, int positionTab,
                           boolean isSelectedCat, long cat_id,
                           boolean isSelectedType, long type_id) {
        switch (positionTab) {
            case 0:
                Log.d(TAG, "***** SmetasWorkRecyclerAdapter getParams()  case 0 ");
                //массив с именами категорий из таблицы категорий CategoryMat
                names = CategoryWork.getArrayCategoryWorkNames(database);
                //булевый массив - есть ли такая категория работы в списке работ с file_id
                checked = CategoryWork.getArrayCategoryWorkChecked(
                        database, file_id, names);
                break;

            case 1:
                Log.d(TAG, "***** SmetasWorkRecyclerAdapter getParams()  case 1 " +
                        " isSelectedCat = " + isSelectedCat);
                if (isSelectedCat) {
                    names = TypeWork.getArrayTypeWorkNamesFromCatId(database, cat_id);
                    Log.d(TAG, "***** SmetasWorkRecyclerAdapter getParams()  case 1 " +
                            " isSelectedCat = " + isSelectedCat + "  names.length = " + names.length);
                } else {
                    //массив с именами категорий из таблицы категорий CategoryMat
                    names = TypeWork.getArrayTypeWorkNames(database);
                    Log.d(TAG, "***** SmetasWorkRecyclerAdapter getParams()  case 1 " +
                            " isSelectedCat = " + isSelectedCat + "  names.length = " + names.length);
                }
                //булевый массив - есть ли такой тип работы в списке работ с file_id
                checked = TypeWork.getArrayTypeWorkChacked(
                        database, file_id, names);
                break;

            case 2:
                Log.d(TAG, "***** SmetasWorkRecyclerAdapter getParams()  case 2 ");
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
                //булевый массив - есть ли такой тип работы в списке работ с file_id
                checked = Work.getArrayWorkChacked(
                        database, file_id, names);
                break;
        }
    }


    @NonNull
    @Override
    public SmetasWorkRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_two_mat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SmetasWorkRecyclerAdapter.ViewHolder holder, final int position) {
        holder.checkBoxTwoMat.setChecked(checked[position]);
        holder.base_text.setText(names[position]);

        holder.llTwoMat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameListener.nameTransmit(names[position]);
            }
        });

        holder.llTwoMat.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
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

    public void updateType(long cat_id) {
        getParams(database, file_id, 1, true, cat_id, false, 0);
        notifyDataSetChanged();
    }

    public void updateName(long cat_id, long type_id) {
        getParams(database, file_id, 2, true, cat_id, true, type_id);
        notifyDataSetChanged();
    }

    public void showDetails(int position) {
        switch (position) {
            case 0:
                long cat_id_specific = CategoryWork.getIdFromName(database, names[posItem]);
                Log.d(TAG, "SmetasWorkRecyclerAdapter showDetails case 0 " +
                        "cat_id_specific = " + cat_id_specific +  " names[posItem] =" + names[posItem]);
                //отправляем интент с id категории
                Intent intentSpecificCat = new Intent(context, SpecificCategory.class);
                intentSpecificCat.putExtra(P.ID_CATEGORY, cat_id_specific);
                context.startActivity(intentSpecificCat);
                break;

            case 1:
                //находим id по имени типа
                long type_id_specific = TypeWork.getIdFromName(database, names[posItem]);
                Log.d(TAG, "SmetasWorkRecyclerAdapter showDetails  case 1:" +
                        "type_id_specific = " + type_id_specific +  " names[posItem] =" + names[posItem]);
                //отправляем интент с id типа
                Intent intentSpecificType = new Intent(context, SpecificType.class);
                intentSpecificType.putExtra(P.ID_TYPE, type_id_specific);
                context.startActivity(intentSpecificType);
                break;

            case 2:
                //находим id по имени типа
                long work_id_specific = Work.getIdFromName(database, names[posItem]);
                Log.d(TAG, "SmetasWorkRecyclerAdapter showDetails case 2 " +
                        "work_id_specific = " + work_id_specific +  " names[posItem] =" + names[posItem]);
                //отправляем интент с id типа
                Intent intentSpecificWork = new Intent(context, SpesificWork.class);
                intentSpecificWork.putExtra(P.ID_WORK, work_id_specific);
                context.startActivity(intentSpecificWork);
                break;
        }
    }

    public void changeName(int position) {
        switch (position) {
            case 0:
                long cat_id_change = CategoryWork.getIdFromName(database, names[posItem]);
                Log.d(TAG, "SmetasWorkRecyclerAdapter changeName  case 0" +
                        "cat_id_change = " + cat_id_change + " names[posItem] =" + names[posItem]);
                //отправляем интент с id категории
                Intent intentCat = new Intent(context, ChangeDataCategory.class);
                intentCat.putExtra(P.ID_CATEGORY, cat_id_change);
                context.startActivity(intentCat);

            case 1:
                long type_id_Change = TypeWork.getIdFromName(database, names[posItem]);
                Log.d(TAG, "SmetasWorkRecyclerAdapter changeName  case 1" +
                        "type_id_Change = " + type_id_Change + "  names[posItem] =" +  names[posItem]);
                //отправляем интент с id типа
                Intent intentType = new Intent(context, ChangeDataType.class);
                intentType.putExtra(P.ID_TYPE, type_id_Change);
                context.startActivity(intentType);
                break;

            case 2:
                long work_id_Change = Work.getIdFromName(database, names[posItem]);
                Log.d(TAG, "SmetasWorkRecyclerAdapter changeName  case 2" +
                        "work_id_Change = " + work_id_Change + "  names[posItem] =" +  names[posItem]);
                //отправляем интент с id работы
                Intent intentWork = new Intent(context, ChangeDataWork.class);
                intentWork.putExtra(P.ID_WORK, work_id_Change);
                context.startActivity(intentWork);
                break;
        }
    }
}
