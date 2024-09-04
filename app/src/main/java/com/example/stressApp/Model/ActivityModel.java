package com.example.stressApp.Model;

public class ActivityModel {
    private int id;
    private String name;
    private String icon_key;
    private String activityInfo;
    private int progress;

    public ActivityModel(int id, String name, String icon_key, String activityInfo) {
        this.id = id;
        this.name = name;
        this.icon_key = icon_key;
        this.activityInfo = activityInfo;
        this.progress = 0;
    }

    public String getName() {
        return name;
    }

    public String getIcon_key() {
        return icon_key;
    }

    public String getActivityInfo() {
        return activityInfo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon_key(String icon_key) {
        this.icon_key = icon_key;
    }

    public void setActivityInfo(String activityInfo) {
        this.activityInfo = activityInfo;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
