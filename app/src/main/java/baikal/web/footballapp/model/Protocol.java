package baikal.web.footballapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Protocol implements Serializable {

    @SerializedName("_id")
    private String id;

    @SerializedName("league")
    private League league;

    @SerializedName("teamOne")
    private Team teamOne;

    @SerializedName("teamTwo")
    private Team teamTwo;

    @SerializedName("date")
    private Date date;

    @SerializedName("stage")
    private Stage stage;

    @SerializedName("round")
    private String round;

    @SerializedName("tour")
    private String tour;

    @SerializedName("group")
    private String group;

    @SerializedName("place")
    private String place;

    @SerializedName("events")
    private List<Event> events;

    @SerializedName("playersList")
    private Person playersList;

    @SerializedName("referees")
    private Referee referees;

    @SerializedName("played")
    private Boolean played;

    @SerializedName("winner")
    private String winner;

    @SerializedName("score")
    private String score;

    @SerializedName("fouls")
    private String fouls;

    @SerializedName("autoGoals")
    private String autoGoals;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getTour() {
        return tour;
    }

    public void setTour(String tour) {
        this.tour = tour;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public Person getPlayersList() {
        return playersList;
    }

    public void setPlayersList(Person playersList) {
        this.playersList = playersList;
    }

    public Referee getReferees() {
        return referees;
    }

    public void setReferees(Referee referees) {
        this.referees = referees;
    }

    public Boolean getPlayed() {
        return played;
    }

    public void setPlayed(Boolean played) {
        this.played = played;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getFouls() {
        return fouls;
    }

    public void setFouls(String fouls) {
        this.fouls = fouls;
    }

    public String getAutoGoals() {
        return autoGoals;
    }

    public void setAutoGoals(String autoGoals) {
        this.autoGoals = autoGoals;
    }
}
