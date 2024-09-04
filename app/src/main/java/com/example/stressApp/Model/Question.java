package com.example.stressApp.Model;

import java.util.List;

public class Question {

    private String questionText;
    private int selectedOption;
    private List<Option> options;

    public Question(String questionText, List<Option> options) {
        this.questionText = questionText;
        this.options = options;
        this.selectedOption = -1;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public static class Option {

        private String text;
        private int points;

        public Option(String text, int points) {
            this.text = text;
            this.points = points;
        }

        public String getText() {
            return text;
        }

        public int getPoints() {
            return points;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setPoints(int points) {
            this.points = points;
        }
    }

    public int getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(int selectedOption) {
        this.selectedOption = selectedOption;
    }
}

