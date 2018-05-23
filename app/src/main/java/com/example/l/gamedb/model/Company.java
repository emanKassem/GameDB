package com.example.l.gamedb.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Company {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("developed")
    @Expose
    private List<Integer> developed = null;
    @SerializedName("published")
    @Expose
    private List<Integer> published = null;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getDeveloped() {
        return developed;
    }

    public void setDeveloped(List<Integer> developed) {
        this.developed = developed;
    }

    public List<Integer> getPublished() {
        return published;
    }

    public void setPublished(List<Integer> published) {
        this.published = published;
    }

}
