package com.example.stressApp.Model;

import java.io.Serializable;

public class VideoModel implements Serializable {
    private String url;
    private String title;
    private String description;
    private String duration;
    private int image;

    public VideoModel() {}

    public VideoModel(String url, String title, String description, String duration, int image) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDuration() {
        return duration;
    }

    public int getImage() {
        return image;
    }
}

