package com.example.RBP687.myapplication.backend;

/**
 * Created by RBP687 on 7/28/2016.
 */
public class User {

    private int id;
    private String event;
    private String over;
    private String url;

    public User(int id, String event, String over, String url) {
        this.id = id;
        this.event = event;
        this.over = over;
        this.url = url;
    }
    public User() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getOver() {
        return over;
    }

    public void setOver(String over) {
        this.over = over;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
