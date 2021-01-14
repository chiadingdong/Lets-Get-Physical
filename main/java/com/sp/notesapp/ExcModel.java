package com.sp.notesapp;

public class ExcModel {
    private String image;
    private String title, timeAndCalories, description;
    private String videoUrl;

    public ExcModel() {
    }

    public ExcModel(String image, String title, String timeAndCalories, String description, String videoUrl) {
        this.image = image;
        this.title = title;
        this.timeAndCalories = timeAndCalories;
        this.description = description;
        this.videoUrl = videoUrl;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getTimeAndCalories() {
        return timeAndCalories;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}
