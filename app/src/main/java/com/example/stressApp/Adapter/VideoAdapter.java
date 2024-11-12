package com.example.stressApp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stressApp.Model.VideoModel;
import com.example.stressApp.R;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.viewHolder> {
    private final List<VideoModel> videoModelList;
    private final ItemClickListener itemClickListener;

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public VideoAdapter(List<VideoModel> videoModelList, ItemClickListener itemClickListener) {
        this.videoModelList = videoModelList;
        this.itemClickListener = itemClickListener;
    }


    @NonNull
    @Override
    public VideoAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_videos_list, parent, false);
        return new VideoAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.viewHolder holder, int position) {
        holder.yogaIcon_imageView.setImageResource(getImageId(videoModelList.get(position).getImage()));
        holder.videoTitle_textView.setText(videoModelList.get(position).getTitle());
        holder.videoDuration_textView.setText(String.format("Duration: %s", videoModelList.get(position).getDuration()));
        holder.yogaIcon_imageView.setOnClickListener(v -> itemClickListener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return videoModelList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        TextView videoTitle_textView, videoDuration_textView;
        ImageView yogaIcon_imageView;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            yogaIcon_imageView = itemView.findViewById(R.id.imageRecyclerView);
            videoTitle_textView = itemView.findViewById(R.id.video_title_textView);
            videoDuration_textView = itemView.findViewById(R.id.video_duration_textView);
        }
    }

    private int getImageId(int id) {
        int imageId;
        switch (id) {
            case 1:
                imageId = R.drawable.meditation1;
                break;
            case 2:
                imageId = R.drawable.meditation2;
                break;
            case 3:
                imageId = R.drawable.meditation3;
                break;
            case 4:
                imageId = R.drawable.meditation4;
                break;
            case 5:
                imageId = R.drawable.meditation5;
                break;
            case 6:
                imageId = R.drawable.meditation6;
                break;
            default:
                imageId = -1;
                break;
        }
        return imageId;
    }

}

