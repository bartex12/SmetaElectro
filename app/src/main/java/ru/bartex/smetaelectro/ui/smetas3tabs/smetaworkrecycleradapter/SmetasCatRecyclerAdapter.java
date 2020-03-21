package ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter;

import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.bartex.smetaelectro.R;

public class SmetasCatRecyclerAdapter extends
        RecyclerView.Adapter<SmetasCatRecyclerAdapter.ViewHolder> {

    OnClickOnLineListener listener;
    SQLiteDatabase database;
    long file_id;
    int positionTab;

    public SmetasCatRecyclerAdapter(SQLiteDatabase database, long file_id, int positionTab) {
        this.database = database;
        this.file_id = file_id;
        this.positionTab = positionTab;

        getParams(database, file_id, positionTab);
    }

    private void getParams(SQLiteDatabase database, long file_id, int positionTab) {
        switch (positionTab){
            case 0:

                break;
            case 1:

                break;
            case 2:

                break;
        }
    }

    public interface OnClickOnLineListener{
        void onClickOnLineListener(String nameItem);
    }

    public void setClickOnLineListener(OnClickOnLineListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public SmetasCatRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_two_mat, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SmetasCatRecyclerAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox  select;
        TextView cat;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            select = itemView.findViewById(R.id.checkBoxTwoMat);
            cat = itemView.findViewById(R.id.base_text);
        }
    }
}
