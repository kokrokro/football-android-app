package baikal.web.footballapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Team implements Serializable {
    @SerializedName("creatorPhone")
    @Expose
    private String creatorPhone;
    @SerializedName("trainer")
    @Expose
    private String trainer;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("group")
    @Expose
    private String group;
    @SerializedName("goals")
    @Expose
    private Integer goals;
    @SerializedName("goalsReceived")
    @Expose
    private Integer goalsReceived;
    @SerializedName("wins")
    @Expose
    private Integer wins;
    @SerializedName("place")
    @Expose
    private Integer place;
    @SerializedName("playoffPlace")
    @Expose
    private Integer playoffPlace;
    @SerializedName("losses")
    @Expose
    private Integer losses;
    @SerializedName("draws")
    @Expose
    private Integer draws;
    @SerializedName("groupScore")
    @Expose
    private int groupScore;
    @SerializedName("players")
    @Expose
    private List<Player> players = null;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("creator")
    @Expose
    private String creator;
    @SerializedName("madeToPlayoff")
    @Expose
    private Boolean madeToPlayoff;
    @SerializedName("club")
    @Expose
    private String club;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("league")
    @Expose
    private String league;

    public String getTrainer() { return trainer;    }

    public void setTrainer(String trainer) { this.trainer = trainer;    }

    public String getCreatorPhone(){ return this.creatorPhone;}

    public void setCreatorPhone(String creatorPhone) { this.creatorPhone = creatorPhone;    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getGoals() {
        return goals;
    }

    public void setGoals(Integer goals) {
        this.goals = goals;
    }
    public Boolean getMadeToPlayoff() {
        return madeToPlayoff;
    }

    public void setMadeToPlayoff(Boolean madeToPlayoff) {
        this.madeToPlayoff = madeToPlayoff;
    }

    public Integer getPlayoffPlace() {
        return playoffPlace;
    }

    public void setPlayoffPlace(Integer playoffPlace) {
        this.playoffPlace = playoffPlace;
    }

    public Integer getGoalsReceived() {
        return goalsReceived;
    }

    public void setGoalsReceived(Integer goalsReceived) {
        this.goalsReceived = goalsReceived;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public Integer getPlace() {
        return place;
    }

    public void setPlace(Integer place) {
        this.place = place;
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

    public int getGroupScore() {
        return groupScore;
    }

    public void setGroupScore(int groupScore) {
        this.groupScore = groupScore;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
