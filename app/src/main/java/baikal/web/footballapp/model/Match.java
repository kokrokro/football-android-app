package baikal.web.footballapp.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Match implements Serializable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("stage")
    @Expose
    private Boolean stage;
    @SerializedName("played")
    @Expose
    private Boolean played;
    @SerializedName("tour")
    @Expose
    private String tour;
    @SerializedName("playersList")
    @Expose
    private List<String> playersList = null;
    @SerializedName("place")
    @Expose
    private String place;
    @SerializedName("league")
    @Expose
    private String league;
    @SerializedName("teamOne")
    @Expose
    private String teamOne;
    @SerializedName("teamTwo")
    @Expose
    private String teamTwo;
    @SerializedName("events")
    @Expose
    private List<Event> events = null;
    @SerializedName("referries")
    @Expose
    private List<Object> referries = null;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("referees")
    @Expose
    private List<Referee> referees = null;
    @SerializedName("score")
    @Expose
    private String score;
    @SerializedName("autoGoal")
    @Expose
    private String autoGoal;
    @SerializedName("penalty")
    @Expose
    private String penalty;
    @SerializedName("winner")
    @Expose
    private String winner;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getStage() {
        return stage;
    }

    public void setStage(Boolean stage) {
        this.stage = stage;
    }

    public Boolean getPlayed() {
        return played;
    }

    public void setPlayed(Boolean played) {
        this.played = played;
    }

    public String getTour() {
        return tour;
    }

    public void setTour(String tour) {
        this.tour = tour;
    }

    public List<String> getPlayersList() {
        return playersList;
    }

    public void setPlayersList(List<String> playersList) {
        this.playersList = playersList;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getTeamOne() {
        return teamOne;
    }

    public void setTeamOne(String teamOne) {
        this.teamOne = teamOne;
    }

    public String getTeamTwo() {
        return teamTwo;
    }

    public void setTeamTwo(String teamTwo) {
        this.teamTwo = teamTwo;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<Object> getReferries() {
        return referries;
    }

    public void setReferries(List<Object> referries) {
        this.referries = referries;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public List<Referee> getReferees() {
        return referees;
    }

    public void setReferees(List<Referee> referees) {
        this.referees = referees;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
    public String getAutoGoal() {
        return autoGoal;
    }

    public void setAutoGoal(String autoGoal) {
        this.autoGoal = autoGoal;
    }
    public String getPenalty() {
        return penalty;
    }

    public void setPenalty(String penalty) {
        this.penalty = penalty;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

}