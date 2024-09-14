package com.example.thirdapplication.Domain;

public class OngoingDomain {
    private String place;
    private String date;
    private int ProgressPercent;
    private String picPath;

    public OngoingDomain(String place, String date, int progressPercent, String picPath) {
        this.place = place;
        this.date = date;
        ProgressPercent = progressPercent;
        this.picPath = picPath;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getProgressPercent() {
        return ProgressPercent;
    }

    public void setProgressPercent(int progressPercent) {
        ProgressPercent = progressPercent;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}
