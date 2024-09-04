package com.example.stressApp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stressApp.Model.Question;
import com.example.stressApp.R;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private final List<Question> questionList;

    public QuestionAdapter(List<Question> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_card, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questionList.get(position);
        holder.bind(holder.getAdapterPosition(), question);
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        private final TextView questionTextView;
        private final RadioGroup optionsRadioGroup;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.text_view_question);
            optionsRadioGroup = itemView.findViewById(R.id.radio_group_options);
        }

        public void bind(int position, Question question) {
            questionTextView.setText(String.format("%s. %s", position + 1, question.getQuestionText()));
            List<Question.Option> options = question.getOptions();

            RadioButton option0 = (RadioButton) optionsRadioGroup.getChildAt(0);
            RadioButton option1 = (RadioButton) optionsRadioGroup.getChildAt(1);
            RadioButton option2 = (RadioButton) optionsRadioGroup.getChildAt(2);
            RadioButton option3 = (RadioButton) optionsRadioGroup.getChildAt(3);

            option0.setText(options.get(0).getText());
            option1.setText(options.get(1).getText());
            option2.setText(options.get(2).getText());
            option3.setText(options.get(3).getText());

            // Clear listeners to avoid unwanted state changes
            option0.setOnCheckedChangeListener(null);
            option1.setOnCheckedChangeListener(null);
            option2.setOnCheckedChangeListener(null);
            option3.setOnCheckedChangeListener(null);

            // Restore the selected option
            switch (question.getSelectedOption()) {
                case 0:
                    option0.setChecked(true);
                    break;
                case 1:
                    option1.setChecked(true);
                    break;
                case 2:
                    option2.setChecked(true);
                    break;
                case 3:
                    option3.setChecked(true);
                    break;
                default:
                    optionsRadioGroup.clearCheck();
                    break;
            }

            // Set listeners after restoring the state
            option0.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) question.setSelectedOption(0);
            });
            option1.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) question.setSelectedOption(1);
            });
            option2.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) question.setSelectedOption(2);
            });
            option3.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) question.setSelectedOption(3);
            });
        }
    }
}
