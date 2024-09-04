package com.example.stressApp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stressApp.Model.FAQModel;
import com.example.stressApp.R;

import java.util.List;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.FAQViewHolder> {

    private List<FAQModel> faqModelList;

    public FAQAdapter(List<FAQModel> faqModelList) {
        this.faqModelList = faqModelList;
    }

    @NonNull
    @Override
    public FAQViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.faq_card, parent, false);
        return new FAQViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FAQViewHolder holder, int position) {
        FAQModel faqModel = faqModelList.get(position);
        holder.questionTextView.setText(faqModel.getQuestion());
        holder.answerTextView.setText(faqModel.getAnswer());

        // Toggle visibility of the answer
        holder.questionTextView.setOnClickListener(v -> {
            if (holder.answerTextView.getVisibility() == View.GONE) {
                holder.answerTextView.setVisibility(View.VISIBLE);
            } else {
                holder.answerTextView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return faqModelList.size();
    }

    static class FAQViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView, answerTextView;

        public FAQViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.faq_question);
            answerTextView = itemView.findViewById(R.id.faq_answer);
        }
    }
}
