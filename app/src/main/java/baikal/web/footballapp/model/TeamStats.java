package baikal.web.footballapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TeamStats implements Serializable {
    @SerializedName("team")
    @Expose
    private String team;
    @SerializedName("wins")
    @Expose
    private Integer wins;
    @SerializedName("losses")
    @Expose
    private Integer losses;
    @SerializedName("draws")
    @Expose
    private Integer draws;
    @SerializedName("goals")
    @Expose
    private Integer goals;
    @SerializedName("goalsReceived")
    @Expose
    private Integer goalsReceived;
    @SerializedName("parrent")
    @Expose
    private String parrent;
    @SerializedName("on_")
    @Expose
    private String on_;
    @SerializedName("onModel")
    @Expose
    private String onModel;


    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public Integer getLosses() {
        return losses;
    }

    public void setLosses(Integer losses) {
        this.losses = losses;
    }

    public Integer getDraws() {
        return draws;
    }

    public void setDraws(Integer draws) {
        this.draws = draws;
    }

    public Integer getGoals() {
        return goals;
    }

    public void setGoals(Integer goals) {
        this.goals = goals;
    }

    public Integer getGoalsReceived() {
        return goalsReceived;
    }

    public void setGoalsReceived(Integer goalsReceived) {
        this.goalsReceived = goalsReceived;
    }

    public String getParrent() {
        return parrent;
    }

    public void setParrent(String parrent) {
        this.parrent = parrent;
    }

    public String getOn_() {
        return on_;
    }

    public void setOn_(String on_) {
        this.on_ = on_;
    }

    public String getOnModel() {
        return onModel;
    }

    public void setOnModel(String onModel) {
        this.onModel = onModel;
    }
}
