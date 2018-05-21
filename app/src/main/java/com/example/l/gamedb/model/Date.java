package com.example.l.gamedb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Date {
    @SerializedName("human")
    @Expose
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
