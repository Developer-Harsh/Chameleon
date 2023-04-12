package com.sneproj.chameleon.model;

public class AppNotification {
    public String name, message, img;
    long date;

    public AppNotification() {

    }

    public AppNotification(String name, String message, long date, String img) {
        this.name = name;
        this.message = message;
        this.date = date;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
