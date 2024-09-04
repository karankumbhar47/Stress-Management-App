package com.example.stressApp.Model;

public class FAQModel {
    private final String question;
    private final String answer;

    public FAQModel(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}