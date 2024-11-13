package com.example.stressApp.Model;

import java.util.List;

public class ChallengeModel {
    private String name;
    private String description;
    private List<String> steps;
    private List<String> tips;
    private String duration;
    private String difficultyLevel;
    private String category;
    private String completionCriteria;
    private String motivationalQuote;

    public ChallengeModel(String name, String description, List<String> steps, List<String> tips, String duration, String difficultyLevel, String category, String completionCriteria, String motivationalQuote){
        this.name = name;
        this.description = description;
        this.steps = steps;
        this.tips = tips;
        this.duration = duration;
        this.difficultyLevel = difficultyLevel;
        this.category = category;
        this.completionCriteria = completionCriteria;
        this.motivationalQuote = motivationalQuote;
    }

    // Getters and setters for each field
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getSteps() { return steps; }
    public void setSteps(List<String> steps) { this.steps = steps; }

    public List<String> getTips() { return tips; }
    public void setTips(List<String> tips) { this.tips = tips; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(String difficultyLevel) { this.difficultyLevel = difficultyLevel; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getCompletionCriteria() { return completionCriteria; }
    public void setCompletionCriteria(String completionCriteria) { this.completionCriteria = completionCriteria; }

    public String getMotivationalQuote() { return motivationalQuote; }
    public void setMotivationalQuote(String motivationalQuote) { this.motivationalQuote = motivationalQuote; }
}
