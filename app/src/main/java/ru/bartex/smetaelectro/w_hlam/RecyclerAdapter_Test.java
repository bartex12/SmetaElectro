package ru.bartex.smetaelectro.w_hlam;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.bartex.smetaelectro.R;

public class RecyclerAdapter_Test
        extends RecyclerView.Adapter<RecyclerAdapter_Test.ViewHolder> {

    public static final String TAG = "33333";
    private ArrayList<String> data ;

    public RecyclerAdapter_Test() {
        data = new ArrayList<>();
        for (int i = 0; i<10; i++){
            data.add("Строка " + i);
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter_Test.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View rowView;

        switch (viewType) {

            case VIEW_TYPES.Normal:
                rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_normal, parent, false);
                break;
            case VIEW_TYPES.Header:
                rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false);
                break;
            case VIEW_TYPES.Footer:
                rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer, parent, false);
                break;
            default:
                rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.normal, parent, false);
                break;
        }
        return new ViewHolder (rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter_Test.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        switch(viewType) {
            case VIEW_TYPES.Header:
                // handle row header
                holder.base_text_header.setText("HEADER position = " + position);
                break;
            case VIEW_TYPES.Footer:
                // handle row footer
                holder.base_text_footer.setText("FOOTER position = " + position);

                break;
            case VIEW_TYPES.Normal:
                // handle row item
                    holder.normal.setText("NORMAL position = " + data.get(position-1));

                break;
        }
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        if (data.size() == 0) {
            //Return 1 here to show nothing
            return 1;
        }
        // Add extra view to show the footer view
        return (data.size() + 2);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView base_text_header; //header
        TextView normal;
        TextView base_text_footer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            base_text_header = itemView.findViewById(R.id.base_text_header);
            normal =  itemView.findViewById(R.id.normal);
            base_text_footer = itemView.findViewById(R.id.base_text_footer);
        }
    }

    private class VIEW_TYPES {
        public static final int Header = 1;
        public static final int Normal = 2;
        public static final int Footer = 3;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return VIEW_TYPES.Header;
        }else if (position == (data.size()+1)) {
            return VIEW_TYPES.Footer;
        }else{
            return VIEW_TYPES.Normal;
        }
    }
}
