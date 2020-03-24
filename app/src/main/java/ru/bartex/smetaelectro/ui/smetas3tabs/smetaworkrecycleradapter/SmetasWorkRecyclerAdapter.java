package ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.CategoryMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.Mat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.TypeMat;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.CategoryWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.CostWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.FW;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.TypeWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.Work;
import ru.bartex.smetaelectro.ui.smetas3tabs.changedata.changedatamat.ChangeDataCategoryMat;
import ru.bartex.smetaelectro.ui.smetas3tabs.changedata.changedatamat.ChangeDataMat;
import ru.bartex.smetaelectro.ui.smetas3tabs.changedata.changedatamat.ChangeDataTypeMat;
import ru.bartex.smetaelectro.ui.smetas3tabs.changedata.changedatawork.ChangeDataCategory;
import ru.bartex.smetaelectro.ui.smetas3tabs.changedata.changedatawork.ChangeDataType;
import ru.bartex.smetaelectro.ui.smetas3tabs.changedata.changedatawork.ChangeDataWork;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkcost.WorkCatCost;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkcost.WorkNameCost;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkcost.WorkTypeCost;
import ru.bartex.smetaelectro.ui.smetas3tabs.specific.SpecificCategory;
import ru.bartex.smetaelectro.ui.smetas3tabs.specific.SpecificCategoryMat;
import ru.bartex.smetaelectro.ui.smetas3tabs.specific.SpecificType;
import ru.bartex.smetaelectro.ui.smetas3tabs.specific.SpesificMat;
import ru.bartex.smetaelectro.ui.smetas3tabs.specific.SpesificTypeMat;
import ru.bartex.smetaelectro.ui.smetas3tabs.specific.SpesificWork;

public class SmetasWorkRecyclerAdapter extends
        RecyclerView.Adapter<SmetasWorkRecyclerAdapter.ViewHolder> {

    public static final String TAG = "33333";
    private OnClickOnNamekListener nameListener;
    private SQLiteDatabase database;
    private long file_id;
    private int positionTab;
    private int posItem;  //позиция в списке
    private Context context;

    private String[] names;
    private boolean[] checked;
    private boolean isSelectedCat;
    private long cat_id;
    private boolean isSelectedType;
    private long type_id;
    private Kind kind;

    public SmetasWorkRecyclerAdapter(
            SQLiteDatabase database, Kind kind, long file_id, int positionTab,
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
                file_id, positionTab, isSelectedCat, cat_id, isSelectedType, type_id);
    }

    public interface OnClickOnNamekListener {
        void nameTransmit(String name);
    }

    public void setOnClickOnNamekListener(OnClickOnNamekListener nameListener) {
        this.nameListener = nameListener;
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

    private void getParams(SQLiteDatabase database, Kind kind, long file_id, int positionTab,
                           boolean isSelectedCat, long cat_id,
                           boolean isSelectedType, long type_id) {
        switch (positionTab) {
            case 0:
                switch (kind){
                    case WORK:
                        getParamsCategoryWork(database, file_id);
                        break;
                    case MAT:
                        getParamsCategoryMat(database, file_id);
                        break;
                    case COST_WORK:
//                        getParamsCategoryWorkCost(database, file_id);
                        break;
                    case COST_MAT:

                        break;
                }
                break;

            case 1:
                switch (kind){
                    case WORK:
                        getParamsTypeWork(database, file_id, isSelectedCat, cat_id);
                        break;
                    case MAT:
                        getParamsTypeMat(database, file_id, isSelectedCat, cat_id);
                        break;
                    case COST_WORK:
//                        getParamsTypeWorkCost(database, file_id, isSelectedCat, cat_id);
                        break;
                    case COST_MAT:

                        break;
                }
                break;

            case 2:
                switch (kind){
                    case WORK:
                        getParamsNameWork(database,
                                file_id, isSelectedCat, cat_id, isSelectedType, type_id);
                        break;
                    case MAT:
                        getParamsNameMat(database,
                                file_id, isSelectedCat, cat_id, isSelectedType, type_id);
                        break;
                    case COST_WORK:
//                        getParamsNameWorkCost(database,
//                                file_id, isSelectedCat, cat_id, isSelectedType, type_id);
                        break;
                    case COST_MAT:

                        break;
                }
                break;
        }
    }

    private void getParamsNameMat(SQLiteDatabase database, long file_id, boolean isSelectedCat, long cat_id, boolean isSelectedType, long type_id) {
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
    }

    private void getParamsTypeMat(SQLiteDatabase database, long file_id, boolean isSelectedCat, long cat_id) {
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
    }

    private void getParamsCategoryMat(SQLiteDatabase database, long file_id) {
        Log.d(TAG, "***** SmetasMatRecyclerAdapter getParams()  case 0 ");
        //массив с именами категорий из таблицы категорий CategoryMat
        names = CategoryMat.getArrayCategoryMatNames(database);
        //булевый массив - есть ли такая категория работы в списке работ с file_id
        checked = CategoryMat.getArrayCategoryMatChecked(
                database, file_id, names);
    }

    private void getParamsNameWork(SQLiteDatabase database, long file_id, boolean isSelectedCat, long cat_id, boolean isSelectedType, long type_id) {
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
    }

    private void getParamsTypeWork(SQLiteDatabase database, long file_id, boolean isSelectedCat, long cat_id) {
        Log.d(TAG, "***** SmetasWorkRecyclerAdapter getParams()  case 1 " +
                " isSelectedCat = " + isSelectedCat);
        if (isSelectedCat) {
            //массив с именами категорий из таблицы категорий TypeWork с cat_id
            names = TypeWork.getArrayTypeWorkNamesFromCatId(database, cat_id);
            Log.d(TAG, "***** SmetasWorkRecyclerAdapter getParams()  case 1 " +
                    " isSelectedCat = " + isSelectedCat + "  names.length = " + names.length);
        } else {
            //массив с именами категорий из таблицы категорий TypeWork
            names = TypeWork.getArrayTypeWorkNames(database);
            Log.d(TAG, "***** SmetasWorkRecyclerAdapter getParams()  case 1 " +
                    " isSelectedCat = " + isSelectedCat + "  names.length = " + names.length);
        }
        //булевый массив - есть ли такой тип работы в списке работ с file_id
        checked = TypeWork.getArrayTypeWorkChacked(
                database, file_id, names);
    }

    private void getParamsCategoryWork(SQLiteDatabase database, long file_id) {
        Log.d(TAG, "***** SmetasWorkRecyclerAdapter getParams()  case 0 ");
        //массив с именами категорий из таблицы категорий CategoryWork
        names = CategoryWork.getArrayCategoryWorkNames(database);
        //булевый массив - есть ли такая категория работы в списке работ с file_id
        checked = CategoryWork.getArrayCategoryWorkChecked(
                database, file_id, names);
    }

    public void updateWorkType(long cat_id) {
        getParams(database, Kind.WORK,  file_id, 1, true, cat_id, false, 0);
        notifyDataSetChanged();
    }

    public void updateWorkName(long cat_id, long type_id) {
        getParams(database, Kind.WORK, file_id, 2, true, cat_id, true, type_id);
        notifyDataSetChanged();
    }

    public void updateMatType(long cat_id){
        getParams(database, Kind.MAT,  file_id, 1,true,cat_id,false,0);
        notifyDataSetChanged();
    }

    public void  updateMatName(long cat_id, long type_id){
        getParams(database, Kind.MAT, file_id, 2,true, cat_id,true, type_id);
        notifyDataSetChanged();
    }

    public void showDetails(int position, Kind kind) {
        switch (position) {
            case 0:
                switch (kind){
                    case WORK:
                        long cat_id_specific = CategoryWork.getIdFromName(database, names[posItem]);
                        Log.d(TAG, "SmetasWorkRecyclerAdapter showDetails case 0 " +
                                "cat_id_specific = " + cat_id_specific +
                                " names[posItem] =" + names[posItem]);
                        //отправляем интент с id категории
                        Intent intentSpecificWork = new Intent(context, SpecificCategory.class);
                        intentSpecificWork.putExtra(P.ID_CATEGORY, cat_id_specific);
                        context.startActivity(intentSpecificWork);
                        break;
                    case MAT:
                        //находим id категории по имени категории
                        final long cat_mat_id = CategoryMat.getIdFromName(database, names[posItem]);
                        Log.d(TAG, "SmetasWorkRecyclerAdapter showDetails case 0 " +
                                " cat_mat_id = " + cat_mat_id +
                                " names[posItem] =" + names[posItem]);
                        //отправляем интент с id категории
                        Intent intentSpecificCat = new Intent(context, SpecificCategoryMat.class);
                        intentSpecificCat.putExtra(P.ID_CATEGORY_MAT, cat_mat_id);
                        context.startActivity(intentSpecificCat);
                        break;
                }
                break;

            case 1:
                switch (kind){
                    case WORK:
                        //находим id по имени типа
                        long type_id_specific = TypeWork.getIdFromName(database, names[posItem]);
                        Log.d(TAG, "SmetasWorkRecyclerAdapter showDetails  case 1:" +
                                "type_id_specific = " + type_id_specific +
                                " names[posItem] =" + names[posItem]);
                        //отправляем интент с id типа
                        Intent intentSpecificType = new Intent(context, SpecificType.class);
                        intentSpecificType.putExtra(P.ID_TYPE, type_id_specific);
                        context.startActivity(intentSpecificType);
                        break;
                    case MAT:
                        //находим id по имени типа
                        long type_mat_id_specific = TypeMat.getIdFromName(database, names[posItem]);
                        Log.d(TAG, "SmetasWorkRecyclerAdapter showDetails case 1 " +
                                "type_mat_id_specific = " + type_mat_id_specific +
                                " names[posItem] =" + names[posItem]);
                        //отправляем интент с id типа
                        Intent intentSpecificTypeMat = new Intent(context, SpesificTypeMat.class);
                        intentSpecificTypeMat.putExtra(P.ID_TYPE_MAT, type_mat_id_specific);
                        context.startActivity(intentSpecificTypeMat);
                        break;
                }
                break;

            case 2:
                switch (kind){
                    case WORK:
                        //находим id по имени типа
                        long work_id_specific = Work.getIdFromName(database, names[posItem]);
                        Log.d(TAG, "SmetasWorkRecyclerAdapter showDetails case 2 " +
                                "work_id_specific = " + work_id_specific +
                                " names[posItem] =" + names[posItem]);
                        //отправляем интент с id типа
                        Intent intentSpecificWork = new Intent(context, SpesificWork.class);
                        intentSpecificWork.putExtra(P.ID_WORK, work_id_specific);
                        context.startActivity(intentSpecificWork);
                        break;
                    case MAT:
                        //находим id по имени типа
                        long mat_id_specific = Mat.getIdFromName(database, names[posItem]);
                        Log.d(TAG, "SmetasWorkRecyclerAdapter showDetails case 2 " +
                                "mat_id_specific = " + mat_id_specific +
                                " names[posItem] =" + names[posItem]);
                        //отправляем интент с id материала
                        Intent intentSpecificMat = new Intent(context, SpesificMat.class);
                        intentSpecificMat.putExtra(P.ID_MAT, mat_id_specific);
                        context.startActivity(intentSpecificMat);
                        break;
                }
                break;
        }
    }

    public void changeName(int position, Kind kind) {
        switch (position) {
            case 0:
                switch (kind){
                    case WORK:
                        long cat_id_change = CategoryWork.getIdFromName(database, names[posItem]);
                        Log.d(TAG, "SmetasWorkRecyclerAdapter changeName  case 0" +
                                "cat_id_change = " + cat_id_change +
                                " names[posItem] =" + names[posItem]);
                        //отправляем интент с id категории
                        Intent intentCatWork = new Intent(context, ChangeDataCategory.class);
                        intentCatWork.putExtra(P.ID_CATEGORY, cat_id_change);
                        context.startActivity(intentCatWork);
                        break;
                    case MAT:
                        //находим id категории по имени категории
                        final long cat_mat_id = CategoryMat.getIdFromName(database, names[posItem]);
                        Log.d(TAG, "SmetasWorkRecyclerAdapter changeName  case 0  " +
                                " cat_mat_id = " + cat_mat_id +
                                "  names[posItem] =" +  names[posItem]);
                        Intent intentCatMat = new Intent(context, ChangeDataCategoryMat.class);
                        intentCatMat.putExtra(P.ID_CATEGORY_MAT, cat_mat_id);
                        context.startActivity(intentCatMat);
                        break;
                }
                break;

            case 1:
                switch (kind){
                    case WORK:
                        long type_id_Change = TypeWork.getIdFromName(database, names[posItem]);
                        Log.d(TAG, "SmetasWorkRecyclerAdapter changeName  case 1" +
                                "type_id_Change = " + type_id_Change +
                                "  names[posItem] =" +  names[posItem]);
                        //отправляем интент с id типа
                        Intent intentTypeWork = new Intent(context, ChangeDataType.class);
                        intentTypeWork.putExtra(P.ID_TYPE, type_id_Change);
                        context.startActivity(intentTypeWork);
                        break;
                    case MAT:
                        //находим id по имени типа
                        long type_mat_id = TypeMat.getIdFromName(database, names[posItem]);
                        Log.d(TAG, "SmetasWorkRecyclerAdapter changeName  case 1 " +
                                "type_mat_id = " + type_mat_id +
                                " names[posItem] =" + names[posItem]);

                        Intent intentTypeCat = new Intent(context, ChangeDataTypeMat.class);
                        intentTypeCat.putExtra(P.ID_TYPE_MAT, type_mat_id);
                        context.startActivity(intentTypeCat);
                        break;
                }
                break;

            case 2:
                switch (kind){
                    case WORK:
                        long work_id_Change = Work.getIdFromName(database, names[posItem]);
                        Log.d(TAG, "SmetasWorkRecyclerAdapter changeName  case 2" +
                                "work_id_Change = " + work_id_Change +
                                "  names[posItem] =" +  names[posItem]);
                        //отправляем интент с id работы
                        Intent intentWork = new Intent(context, ChangeDataWork.class);
                        intentWork.putExtra(P.ID_WORK, work_id_Change);
                        context.startActivity(intentWork);
                        break;
                    case MAT:
                        //находим id по имени материала
                        long mat_id_Change = Mat.getIdFromName(database, names[posItem]);
                        Log.d(TAG, "SmetasWorkRecyclerAdapter changeName  case 2 " +
                                "mat_id_Change = " + mat_id_Change +
                                "  names[posItem] =" +  names[posItem]);
                        //отправляем интент с id материала
                        Intent intent = new Intent(context, ChangeDataMat.class);
                        intent.putExtra(P.ID_MAT, mat_id_Change);
                        context.startActivity(intent);
                        break;
                }
                break;
        }
    }

    public void deleteItem(final int position, final Kind kind) {
        new AlertDialog.Builder(context)
                .setTitle("Удалить?")
                .setPositiveButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //никаких действий
                    }
                }).setNegativeButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (position) {
                    case 0:
                        switch (kind){
                            case WORK:
                                //находим id по имени файла
                                long work_id = CategoryWork.getIdFromName(database, names[posItem]);
                                Log.d(TAG, "SmetasWork onContextItemSelected case P.DELETE_ID" +
                                        " work_id = " + work_id +
                                        " names[posItem] =" + names[posItem]);
                                //находим количество строк типов работы для cat_id
                                int countLineType = TypeWork.getCountLine(database, work_id);
                                Log.d(TAG, "SmetasWork onCreateContextMenu - countLineType = " + countLineType);
                                if(countLineType > 0) {
                                //menu.findItem(P.DELETE_ID).setEnabled(false);
                                    Toast.makeText(context, " Удаление невозможно",
                                            Toast.LENGTH_SHORT).show();
                                }else {
                                //Удаляем файл из таблицы CategoryWork когда в категории нет типов
                                 CategoryWork.deleteObject(database, work_id);
                                 //вызываем обновлённые параметры чтобы обновление прошло успешно
                                getParamsCategoryWork(database, file_id);
                                    Toast.makeText(context, " Удалено ", Toast.LENGTH_SHORT).show();
                                      }
                                break;

                            case MAT:

                                break;
                        }
                        break;

                    case 1:
                        switch (kind){
                            case WORK:
                                //находим id по имени файла
                                long type_id = TypeWork.getIdFromName(database, names[posItem]);
                                Log.d(TAG, "SmetasWork onContextItemSelected case P.DELETE_ID" +
                                        " type_id = " + type_id +
                                        " names[posItem] =" + names[posItem]);
                                //находим количество строк видов работы для type_id
                                int countLineWork = Work.getCountLine(database, type_id);
                                 Log.d(TAG, "SmetasWork onCreateContextMenu - countLineWork = " +
                                         countLineWork);
                                if(countLineWork > 0) {
                                //menu.findItem(P.DELETE_ID).setEnabled(false); //так лучше
                                //menu.findItem(P.DELETE_ID).setVisible(false);
                                    Toast.makeText(context, " Удаление невозможно",
                                            Toast.LENGTH_SHORT).show();
                                }else {
                                  //Удаляем файл из таблицы TypeWork когда в типе нет видов работ
                                 TypeWork.deleteObject(database, type_id);
                                    //вызываем обновлённые параметры чтобы обновление прошло успешно
                                    getParamsTypeWork(database, file_id, isSelectedCat, cat_id);
                                    Toast.makeText(context, " Удалено ", Toast.LENGTH_SHORT).show();
                                      }
                                break;
                            case MAT:

                                break;
                        }
                        break;

                    case 2:
                        switch (kind){
                            case WORK:
                                //находим id по имени файла
                                long work_id = Work.getIdFromName(database, names[posItem]);
                                Log.d(TAG, "SmetaWorkElectro onContextItemSelected case P.DELETE_ID" +
                                        " work_id = " + work_id +
                                        " names[posItem] =" + names[posItem]);
                                //находим количество строк видов работы в таблице FW для work_id
                                 int countLineWorkFW = FW.getCountLine(database, work_id);
                                 //находим количество строк расценок работы в таблице CostWork для work_id
                                 int countCostLineWork = CostWork.getCountLine(database, work_id);
                                 Log.d(TAG, "SmetasWork onCreateContextMenu - countLineWorkFW = " + countLineWorkFW +
                                 " countCostLineWork =" + countCostLineWork);
                                if(countLineWorkFW > 0) {
                                 //menu.findItem(P.DELETE_ID).setEnabled(false); //так лучше
                                //menu.findItem(P.DELETE_ID).setVisible(false);
                                    Toast.makeText(context, " Удаление невозможно",
                                            Toast.LENGTH_SHORT).show();
                                }else {
                                    //Удаляем запись из таблицы Work когда в таблице FW нет такой  работы
                                    Work.deleteObject(database, work_id);
                                    //Удаляем запись из таблицы CostWork когда в таблице FW нет такой  работы
                                    CostWork.deleteObject(database, work_id);
                                    //вызываем обновлённые параметры чтобы обновление прошло успешно
                                    getParamsNameWork(database, file_id, isSelectedCat, cat_id, isSelectedType, type_id);
                                    Toast.makeText(context, " Удалено ", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case MAT:

                                break;
                        }
                        break;
                }
                //обновляем данные списка фрагмента работ
                notifyDataSetChanged();
            }
        }).show();
    }
}
