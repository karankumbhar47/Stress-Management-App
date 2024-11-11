package com.example.stressApp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stressApp.Model.DiaryEventModel;
import com.example.stressApp.R;

import java.util.List;

public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.viewHolder>{
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    Context context;
    private List<DiaryEventModel> eventList;
    private InformationAdapter.OnItemClickListener listener;

    public InformationAdapter(Context context, List<DiaryEventModel> itemList) {
        this.context = context;
        this.eventList = itemList;
    }


    public void setOnItemClickListener(InformationAdapter.OnItemClickListener clickListener) {
        listener = clickListener;
    }


    @NonNull
    @Override
    public InformationAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_listview_shape,parent,false);
        context = parent.getContext();
        return new InformationAdapter.viewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull InformationAdapter.viewHolder holder, int position) {
        DiaryEventModel event = eventList.get(position);
        holder.description.setText(event.getDescription());
        holder.date.setText(event.getDateTime());
        Log.d("Update_Diary", "updateDiaryEvent:  "+event.getDateTime());
        holder.main_card.setOnClickListener(v -> listener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        TextView description;
        TextView date;
        CardView main_card;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date_textView);
            description = itemView.findViewById(R.id.description_textView);
            main_card = itemView.findViewById(R.id.main_card);
        }
    }
}
