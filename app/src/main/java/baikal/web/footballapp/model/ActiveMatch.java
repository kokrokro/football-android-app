package baikal.web.footballapp.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActiveMatch implements Serializable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("round")
    @Expose
    private String round;
    @SerializedName("tour")
    @Expose
    private String tour;
    @SerializedName("group")
    @Expose
    private String group;
    @SerializedName("place")
    @Expose
    private String place;
    @SerializedName("playersList")
    @Expose
    private List<String> playersList = null;
    @SerializedName("played")
    @Expose
    private Boolean played;
    @SerializedName("winner")
    @Expose
    private String winner;
    @SerializedName("score")
    @Expose
    private String score;
    @SerializedName("fouls")
    @Expose
    private String fouls;
    @SerializedName("autoGoal")
    @Expose
    private String autoGoal;
    @SerializedName("teamOne")
    @Expose
    private Team teamOne;
    @SerializedName("teamTwo")
    @Expose
    private Team teamTwo;
    @SerializedName("stage")
    @Expose
    private String stage;
    @SerializedName("league")
    @Expose
    private String league;
    @SerializedName("events")
    @Expose
    private List<Event> events = null;
    @SerializedName("referees")
    @Expose
    private List<Referee> referees = null;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("leagueID")
    @Expose
    private String leagueID;

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

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
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

    public Team getTeamOne() {
        return teamOne;
    }

    public void setTeamOne(Team teamOne) {
        this.teamOne = teamOne;
    }

    public Team getTeamTwo() {
        return teamTwo;
    }

    public void setTeamTwo(Team teamTwo) {
        this.teamTwo = teamTwo;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<Referee> getReferees() {
        return referees;
    }

    public void setReferees(List<Referee> referees) { this.referees = referees; }

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

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getLeagueID() { return leagueID; }

    public void setLeagueID(String leagueID) { this.leagueID = leagueID; }

    public String getRound() { return round; }

    public void setRound(String round) { this.round = round; }

    public String getGroup() { return group; }

    public void setGroup(String group) { this.group = group; }

    public String getFouls() { return fouls; }

    public void setFouls(String fouls) { this.fouls = fouls; }

    public String getPenalty ()
    {
        String ans = "";

        return ans;
    }
}
