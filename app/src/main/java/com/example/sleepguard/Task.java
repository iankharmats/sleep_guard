package com.example.sleepguard;

public class Task {

    private String id;
    private String title;
    private String hours;
    private String minutes;

    public Task(String id, String title, String hours, String minutes)
    {
        this.id = id;
        this.title = title;
        this.hours = hours;
        this.minutes = minutes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }
}
