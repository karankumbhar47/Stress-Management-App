package com.example.stressApp.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.stressApp.Utils.AppConstants;
import com.example.stressApp.R;
import com.example.stressApp.Model.YogaModel;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YogaAdapter extends RecyclerView.Adapter<YogaAdapter.viewHolder> {
    List<YogaModel> items;
    Context context;
    private YogaAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(YogaAdapter.OnItemClickListener clickListener) {
        listener = clickListener;
    }

    public YogaAdapter(List<YogaModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public YogaAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.yoga_card,parent,false);
        context = parent.getContext();
        return new viewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull YogaAdapter.viewHolder holder, int position) {
        YogaModel yogaModel = items.get(position);

        Pattern pattern = Pattern.compile("^(.*?)\\s*\\(([^)]+)\\)\\s*(.*)$");
        Matcher matcher = pattern.matcher(yogaModel.getName());

        if (matcher.find()) {
            String textOutsideBrackets = matcher.group(1) + matcher.group(3);
            String textInsideBrackets = matcher.group(2);
            String name = textInsideBrackets + "\n(" + textOutsideBrackets + ")";
            SpannableString spannableString = new SpannableString(name);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, textInsideBrackets.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.yogaAsan_name.setText(spannableString);
        }
        else holder.yogaAsan_name.setText(yogaModel.getName());

        Log.d(AppConstants.LOG_YOGA, "onBindViewHolder: model name "+yogaModel.getName());
        Log.d(AppConstants.LOG_YOGA, "onBindViewHolder: model path "+yogaModel.getPath());
        Integer drawableId = AppConstants.drawableMap.get(yogaModel.getPath());
        if (drawableId != null) {
            Glide.with(context).load(drawableId).into(holder.picImage);
            Log.d(AppConstants.LOG_YOGA, "onBindViewHolder: drawable id " + drawableId);
        }


        holder.main_card.setOnClickListener(v -> listener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        TextView yogaAsan_name;
        ImageView picImage;
        CardView main_card;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            yogaAsan_name = itemView.findViewById(R.id.yoga_pose_name);
            picImage = itemView.findViewById(R.id.yoga_pic);
            main_card = itemView.findViewById(R.id.main_card);
        }
    }
}

