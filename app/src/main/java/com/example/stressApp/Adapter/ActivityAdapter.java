package com.example.stressApp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.stressApp.Model.ActivityModel;
import com.example.stressApp.R;
import com.example.stressApp.Utils.AppConstants;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.viewHolder> {
    List<ActivityModel> items;
    Context context;
    private ActivityAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ActivityAdapter.OnItemClickListener clickListener) {
        listener = clickListener;
    }

    public ActivityAdapter(List<ActivityModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ActivityAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_card,parent,false);
        context = parent.getContext();
        return new viewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityAdapter.viewHolder holder, int position) {
        ActivityModel activityModel = items.get(position);
        holder.activity_name.setText(activityModel.getName());
        holder.progressBar.setProgress(activityModel.getProgress());
        holder.main_card.setOnClickListener(v -> listener.onItemClick(position));

        Integer drawableId = AppConstants.drawableMap.get(activityModel.getIcon_key());
        if (drawableId != null)
            Glide.with(context) .load(drawableId) .into(holder.picImage);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        TextView activity_name;
        ImageView picImage;
        CardView main_card;
        ProgressBar progressBar;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            activity_name = itemView.findViewById(R.id.activity_name);
            picImage = itemView.findViewById(R.id.activity_image);
            main_card = itemView.findViewById(R.id.main_card);
            progressBar = itemView.findViewById(R.id.activity_progress_bar);
        }
    }
}

