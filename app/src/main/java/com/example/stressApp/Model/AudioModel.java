package com.example.stressApp.Model;

import java.io.Serializable;

public class AudioModel implements Serializable {
    private String fileName;
    private String title;
    private String duration;

    public AudioModel(String fileName, String title, String duration) {
        this.fileName = fileName;
        this.title = title;
        this.duration = duration;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
