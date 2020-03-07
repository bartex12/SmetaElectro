package ru.bartex.smetaelectro.ui.smetatabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.work.FW;

public class SmetasRecyclerAdapter extends
        RecyclerView.Adapter<SmetasRecyclerAdapter.ViewHolder> {

    private SQLiteDatabase database;
    private Context context;
    private long file_id;
    private int size;

    public SmetasRecyclerAdapter(SQLiteDatabase database, long file_id ) {
        this.database = database;
        this.file_id = file_id;
        size = FW.getCountLine(database, file_id);
    }

    @NonNull
    @Override
    public SmetasRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.list_item_complex, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SmetasRecyclerAdapter.ViewHolder holder, int position) {
        holder.tvNumberOfLine.setText(position + 1);
        holder.base_text.setText(FW.getDataFW(database, file_id).getFW_File_Name());
        holder.tvCost.setText(Float.toString(FW.getDataFW(database, file_id).getFW_Cost()));
        holder.tvAmount.setText(Integer.toString(FW.getDataFW(database, file_id).getFW_Count()));
        holder.tvUnits.setText(FW.getDataFW(database, file_id).getFW_Unit());
        holder.tvSumma.setText(Float.toString(FW.getDataFW(database, file_id).getFW_Summa()));
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumberOfLine;
        TextView base_text;
        TextView tvCost;
        TextView tvAmount;
        TextView tvUnits;
        TextView tvSumma;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumberOfLine = itemView.findViewById(R.id.tvNumberOfLine);
            base_text = itemView.findViewById(R.id.base_text);
            tvCost = itemView.findViewById(R.id.tvCost);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvUnits = itemView.findViewById(R.id.tvUnits);
            tvSumma = itemView.findViewById(R.id.tvSumma);

        }
    }
}
