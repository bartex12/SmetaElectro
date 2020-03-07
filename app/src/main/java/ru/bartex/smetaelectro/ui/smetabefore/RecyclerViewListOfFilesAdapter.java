package ru.bartex.smetaelectro.ui.smetabefore;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import ru.bartex.smetaelectro.R;
import ru.bartex.smetaelectro.data.DataFile;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.files.FileWork;

public class RecyclerViewListOfFilesAdapter extends
        RecyclerView.Adapter<RecyclerViewListOfFilesAdapter.ViewHolder> {

    private static final String TAG = "33333";

    private Context context;
    private SQLiteDatabase database;
    private ArrayList<DataFile> data;
    private OnFileListClickListener onFileListClickListener;
    private int posItem = 0;

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

    void setOnFileListClickListener(OnFileListClickListener onFileListClickListener) {
        this.onFileListClickListener = onFileListClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         context = parent.getContext();
         View view = LayoutInflater.from(context)
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

        // устанавливаем слушатель долгих нажатий на списке для вызова контекстного меню
        holder.base_text_file_names.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                posItem = position;
                return false;
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

    void callSpecification(){
        Log.d(TAG, "RecyclerViewListOfFilesAdapter callSpecification");
        String file_name = data.get(posItem).getFileName();
        //находим id по имени файла
        final long file_id = FileWork.getIdFromName(database, file_name);

        //отправляем интент с id файла
        Intent intentSpecific = new Intent(context, SmetaSpecification.class);
        intentSpecific.putExtra(P.ID_FILE, file_id);
        context.startActivity(intentSpecific);
    }

    void changeName() {
        Log.d(TAG, "RecyclerViewListOfFilesAdapter changeName");
        String file_name = data.get(posItem).getFileName();
        //находим id по имени файла
        final long file_id = FileWork.getIdFromName(database, file_name);

        //отправляем интент с id файла
        Intent intent = new Intent(context, SmetaNewNameChange.class);
        intent.putExtra(P.ID_FILE, file_id);
        context.startActivity(intent);
    }

    //удаление элемента списка смет
    void removeElement() {
        Log.d(TAG, "RecyclerViewListOfFilesAdapter removeElement");
        String file_name = data.get(posItem).getFileName();
        //находим id по имени файла
        final long file_id = FileWork.getIdFromName(database, file_name);
        new AlertDialog.Builder(context)
                .setTitle(R.string.DeleteYesNo)
                .setPositiveButton(R.string.DeleteNo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })

                .setNegativeButton(R.string.DeleteYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Удаляем файл из таблицы FileWork и данные из таблицы FW по file_id
                        FileWork.deleteObject(database, file_id);
                        data.remove(posItem);
                        notifyItemChanged(posItem);
                        Toast.makeText(context, context.getResources().getString(R.string.deleted),
                                Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }


}
