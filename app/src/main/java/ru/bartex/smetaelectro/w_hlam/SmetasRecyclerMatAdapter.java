package ru.bartex.smetaelectro.w_hlam;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.files.FileWork;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.FM;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.mat.Mat;

//класс - адаптер для фрагмента материалов SmetasTabMat, в макете которого RecyclerView
public class SmetasRecyclerMatAdapter extends RecyclerView.Adapter<SmetasRecyclerMatAdapter.ViewHolder> {
    public static final String TAG = "33333";
    private SQLiteDatabase database;
    private Context context;
    private long file_id;
    private int size;
    private int positionTab;  //номер вкладки
    private int posItem;  //позиция в списке
    private OnClickOnMatListener matListener;


    private String[] name;
    private float[] cost;
    private float[] amount;
    private String[] units;
    private float[] summa; //массив стоимости

    public interface OnClickOnMatListener{
        void onClickOnMatListener(String namtItem);
    }

    public void setOnClickOnMatListener(OnClickOnMatListener onClickOnMatListener){
        this.matListener = onClickOnMatListener;
    };


    public SmetasRecyclerMatAdapter(SQLiteDatabase database, long file_id) {
        this.database = database;
        this.file_id = file_id;
        this.positionTab = positionTab;
        Log.d(TAG, "***** SmetasRecyclerMatAdapter positionTab = " + positionTab);
        getParams(database, file_id);
    }

    private void getParams(SQLiteDatabase database, long file_id) {

        //Массив работ в файле с file_id
        name = FM.getArrayNames(database, file_id);
        //Массив цен для работ в файле с file_id
        cost = FM.getArrayCost(database, file_id);
        //Массив количества работ для работ в файле с file_id
        amount = FM.getArrayAmount(database, file_id);
        //Массив единиц измерения для работ в файле с file_id
        units = FM.getArrayUnit(database, file_id);
        //Массив стоимости работ  для работ в файле с file_id
        summa = FM.getArraySumma(database, file_id);

        size = name.length;
        Log.d(TAG, "***** SmetasRecyclerMatAdapter size = " + size);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        View rowView;
        switch (viewType) {
            case VIEW_TYPES.Normal:
                rowView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_complex, parent, false);
                break;
            case VIEW_TYPES.Header:
                rowView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.header, parent, false);
                break;
            case VIEW_TYPES.Footer:
                rowView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.footer, parent, false);
                break;
            default:
                rowView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_complex, parent, false);
                break;
        }
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        int viewType = getItemViewType(position);
        switch(viewType) {
            case VIEW_TYPES.Header:
                // handle row header
                String fileName = FileWork.getNameFromId(database, file_id);
                holder.base_text_header.setText(
                        String.format(Locale.getDefault(),"Смета: %s", fileName));
                break;

            case VIEW_TYPES.Footer:
                // handle row footer
                //summa = FW.getArraySumma(database, file_id);
                float totalSumma = P.updateTotalSumma(summa);
                holder.base_text_footer.setText(
                        String.format(Locale.getDefault(),"За материалы: %.0f руб", totalSumma ));
                break;

            case VIEW_TYPES.Normal:
                holder.tvNumberOfLine.setText(String.format(Locale.getDefault(),
                        "%s", Integer.toString (position)));
                holder.base_text.setText(String.format(Locale.getDefault(),
                        "%s", name[position-1]));
                holder.tvCost.setText(String.format(Locale.getDefault(),
                        "%s", Float.toString(cost[position-1])));
                holder.tvAmount.setText(String.format(Locale.getDefault(),
                        "%s", Float.toString(amount[position-1])));
                holder.tvUnits.setText(String.format(Locale.getDefault(),
                        "%s", units[position-1]));
                holder.tvSumma.setText( String.format(Locale.getDefault(),
                        "%s", Float.toString(summa[position-1])));

                //слушатель на строке списка
                holder.ll_complex.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        matListener.onClickOnMatListener(name[position-1]);
                    }
                });

                // устанавливаем слушатель долгих нажатий на списке для вызова контекстного меню
                //запоминаем позицию в списке - нужно при удалении, например
                holder.ll_complex.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        posItem = position-1;
                        return false;
                    }
                });
                break;
        }

//        holder.tvNumberOfLine.setText(
//                String.format(Locale.getDefault(),"%s", Integer.toString (position + 1)));
//        holder.base_text.setText(
//                String.format(Locale.getDefault(),"%s", name[position]));
//        holder.tvCost.setText(
//                String.format(Locale.getDefault(),"%s", Float.toString(cost[position])));
//        holder.tvAmount.setText(
//                String.format(Locale.getDefault(),"%s", Float.toString(amount[position])));
//        holder.tvUnits.setText(
//                String.format(Locale.getDefault(),"%s", units[position]));
//        holder.tvSumma.setText(
//                String.format(Locale.getDefault(),"%s", Float.toString(summa[position])));
//
//        holder.ll_complex.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                matListener.onClickOnMatListener(name[position]);
//            }
//        });
//
//        // устанавливаем слушатель долгих нажатий на списке для вызова контекстного меню
//        //запоминаем позицию в списке - нужно при удалении, например
//        holder.ll_complex.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                posItem = position;
//                return false;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return size + 2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ll_complex;
        TextView tvNumberOfLine;
        TextView base_text;
        TextView tvCost;
        TextView tvAmount;
        TextView tvUnits;
        TextView tvSumma;

        TextView base_text_header;
        TextView base_text_footer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_complex = itemView.findViewById(R.id.ll_complex);
            tvNumberOfLine = itemView.findViewById(R.id.tvNumberOfLine);
            base_text = itemView.findViewById(R.id.base_text);
            tvCost = itemView.findViewById(R.id.tvCost);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvUnits = itemView.findViewById(R.id.tvUnits);
            tvSumma = itemView.findViewById(R.id.tvSumma);

            base_text_header = itemView.findViewById(R.id.base_text_header);
            base_text_footer = itemView.findViewById(R.id.base_text_footer);
        }
    }

    //удаление элемента списка с подтверждением через диалоговое окно
    //работает для любого списка, с которым работает данный адаптер
    public void removeElement() {

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

                //находим id по имени материала
                long mat_id = Mat.getIdFromName(database, name[posItem]);
                Log.d(TAG, "## ## SmetasRecyclerMatAdapter onClick" +
                                "file_id = " + file_id + " mat_id = " + mat_id);
                //удаляем пункт сметы из таблицы FM
                FM.deleteItemFrom_FM(database, file_id, mat_id);

                getParams(database, file_id);
                //обновляем данные списка фрагмента работ
                notifyDataSetChanged();
                Toast.makeText(context, " Удалено ", Toast.LENGTH_SHORT).show();
            }
        }).show();
    }

    private class VIEW_TYPES {
         static final int Header = 1;
         static final int Normal = 2;
         static final int Footer = 3;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return VIEW_TYPES.Header;
        }else if (position == (size+1)) {
            return VIEW_TYPES.Footer;
        }else{
            return VIEW_TYPES.Normal;
        }
    }
}
