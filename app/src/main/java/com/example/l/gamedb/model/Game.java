package com.example.l.gamedb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Game {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("total_rating")
    @Expose
    private Double totalRating;
    @SerializedName("games")
    @Expose
    private List<Integer> games = null;
    @SerializedName("developers")
    @Expose
    private List<Integer> developers = null;
    @SerializedName("publishers")
    @Expose
    private List<Integer> publishers = null;
    @SerializedName("game_modes")
    @Expose
    private List<Integer> gameModes = null;
    @SerializedName("themes")
    @Expose
    private List<Integer> themes = null;
    @SerializedName("first_release_date")
    @Expose
    private Double firstReleaseDate;

    @SerializedName("screenshots")
    @Expose
    private List<Url> screenshots = null;
    @SerializedName("cover")
    @Expose
    private Url cover;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Double getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(Double totalRating) {
        this.totalRating = totalRating;
    }

    public List<Integer> getGames() {
        return games;
    }

    public void setGames(List<Integer> games) {
        this.games = games;
    }

    public List<Integer> getDevelopers() {
        return developers;
    }

    public void setDevelopers(List<Integer> developers) {
        this.developers = developers;
    }

    public List<Integer> getPublishers() {
        return publishers;
    }

    public void setPublishers(List<Integer> publishers) {
        this.publishers = publishers;
    }

    public List<Integer> getGameModes() {
        return gameModes;
    }

    public void setGameModes(List<Integer> gameModes) {
        this.gameModes = gameModes;
    }

    public List<Integer> getThemes() {
        return themes;
    }

    public void setThemes(List<Integer> themes) {
        this.themes = themes;
    }

    public Double getFirstReleaseDate() {
        return firstReleaseDate;
    }

    public void setFirstReleaseDate(Double firstReleaseDate) {
        this.firstReleaseDate = firstReleaseDate;
    }

    public List<Url> getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(List<Url> screenshots) {
        this.screenshots = screenshots;
    }

    public Url getCover() {
        return cover;
    }

    public void setCover(Url cover) {
        this.cover = cover;
    }
}
