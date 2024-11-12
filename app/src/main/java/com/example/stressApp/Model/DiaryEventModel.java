package com.example.stressApp.Model;

public class DiaryEventModel {
    private String description;
    private String dateTime;

    public DiaryEventModel(String description, String dateTime) {
        this.description = description;
        this.dateTime = dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }
}
