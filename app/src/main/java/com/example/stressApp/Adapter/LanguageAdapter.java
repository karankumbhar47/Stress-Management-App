package com.example.stressApp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stressApp.R;

import java.util.ArrayList;
import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.viewHolder> {
    List<String> items;
    Context context;
    private int active;
    private LanguageAdapter.OnItemClickListener listener;

    public void setActive(int active){
        this.active = active;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(LanguageAdapter.OnItemClickListener clickListener) {
        listener = clickListener;
    }

    public LanguageAdapter(Context context, List<String> items, int index) {
        this.context = context;
        this.items = items;
        this.active = index;
    }

    @NonNull
    @Override
    public LanguageAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.language_card,parent,false);
        context = parent.getContext();
        return new viewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageAdapter.viewHolder holder, int position) {
        String language = items.get(position);
        holder.language_name.setText((CharSequence) language);

        if(active==position) {
            holder.main_card.setCardBackgroundColor(context.getColor(R.color.blue));
            holder.language_name.setTypeface(null, Typeface.BOLD);
        }
        else {
            holder.main_card.setCardBackgroundColor(context.getColor(R.color.gray_faint));
            holder.language_name.setTypeface(null, Typeface.NORMAL);
        }

        holder.main_card.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Confirm!")
                    .setMessage("Are you sure you want to change the language to "+language)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        setActive(position);
                        notifyDataSetChanged();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        TextView language_name;
        CardView main_card;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            language_name = itemView.findViewById(R.id.language_textView);
            main_card = itemView.findViewById(R.id.main_card);
        }
    }
}

