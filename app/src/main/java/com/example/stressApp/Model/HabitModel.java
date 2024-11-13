package com.example.stressApp.Model;

public class HabitModel {
    private String habitName;
    private String description;
    private String duration;

    public HabitModel(String habitName, String description, String duration) {
        this.habitName = habitName;
        this.description = description;
        this.duration = duration;
    }

    public String getHabitName() {
        return habitName;
    }

    public String getDescription() {
        return description;
    }

    public String getDuration() {
        return duration;
    }
}

