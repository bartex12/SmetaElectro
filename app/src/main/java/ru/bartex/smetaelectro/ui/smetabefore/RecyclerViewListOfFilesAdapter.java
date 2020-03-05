package ru.bartex.smetaelectro.ui.smetabefore;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.data.DataFile;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.files.FileWork;

public class RecyclerViewListOfFilesAdapter extends
        RecyclerView.Adapter<RecyclerViewListOfFilesAdapter.ViewHolder> {

    private static final String TAG = "33333";
    private SQLiteDatabase database;
    private ArrayList<DataFile> data;
    private OnFileListClickListener onFileListClickListener;

     RecyclerViewListOfFilesAdapter(SQLiteDatabase database){
        this.database = database;
       // список имён файлов из базы данных
        data = new ArrayList<>(FileWork.readFilesData(database));
        Log.d(TAG, "RecyclerViewListOfFilesAdapter data.size() = " + data.size());
    }

    //интерфейс слушателя щелчков на списке
    public interface OnFileListClickListener {
        void onFileListClick(String fileName);
    }

    public void setOnFileListClickListener(OnFileListClickListener onFileListClickListener) {
        this.onFileListClickListener = onFileListClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_single, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.base_text_file_names.setText(data.get(position).getFileName());

        holder.base_text_file_names.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fileName = data.get(position).getFileName();
                Log.d(TAG, "** RecyclerViewListOfFilesAdapter onClick fileName = " + fileName);
                onFileListClickListener.onFileListClick(fileName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder {

        TextView base_text_file_names;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            base_text_file_names = itemView.findViewById(R.id.base_text);
        }
    }
}
