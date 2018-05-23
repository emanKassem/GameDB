package com.example.l.gamedb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Theme {
    @SerializedName("games")
    @Expose
    private List<Integer> games = null;

    public List<Integer> getGames() {
        return games;
    }

    public void setGames(List<Integer> games) {
        this.games = games;
    }

}
