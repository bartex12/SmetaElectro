package ru.bartex.smetaelectro.ui.main;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.bartex.smetaelectro.R;

public class RecyclerViewMainAdapter extends RecyclerView.Adapter<RecyclerViewMainAdapter.ViewHolder> {

    private static final String TAG = "33333";
    private String[] stringListMain;
    private List<String> listOfMain;
    private Context context;
    private OnMainListClickListener onMainListClickListener;

    public RecyclerViewMainAdapter(List<String> listOfMain){
       this.listOfMain =listOfMain;
       Log.d(TAG, "RecyclerViewMainAdapter listOfMain.size() = " + listOfMain.size());
    }


    //интерфейс слушателя щелчков на списке
    public interface OnMainListClickListener {
        void onMainListClick(int position);
    }
    //метод установки слушателя щелчков на списке
    public void setOnMainListClickListener(OnMainListClickListener onMainListClickListener) {
        this.onMainListClickListener = onMainListClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(
                R.layout.list_item_main, parent, false);
        return new ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.picture.setImageResource(R.drawable.p1);
        holder.base_text.setText(listOfMain.get(position));
        holder.base_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMainListClickListener.onMainListClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfMain.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView picture;
        TextView base_text;

        public ViewHolder(View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.picture);
            base_text = itemView.findViewById(R.id.base_text);
        }
    }


}
