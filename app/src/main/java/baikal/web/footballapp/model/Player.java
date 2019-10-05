package baikal.web.footballapp.model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Player implements Serializable {

    @SerializedName("inviteStatus")
    @Expose
    private String inviteStatus;
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("activeYellowCards")
    @Expose
    private Integer activeYellowCards;
    @SerializedName("yellowCards")
    @Expose
    private Integer yellowCards;
    @SerializedName("redCards")
    @Expose
    private Integer redCards;
    @SerializedName("activeDisquals")
    @Expose
    private Integer activeDisquals;
    @SerializedName("disquals")
    @Expose
    private Integer disquals;
    @SerializedName("matches")
    @Expose
    private Integer matches;
    @SerializedName("goals")
    @Expose
    private Integer goals;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("playerId")
    @Expose
    private String playerId;

    public String getInviteStatus() {
        return inviteStatus;
    }

    public void setInviteStatus(String inviteStatus) {
        this.inviteStatus = inviteStatus;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getActiveYellowCards() {
        return activeYellowCards;
    }

    public void setActiveYellowCards(Integer activeYellowCards) {
        this.activeYellowCards = activeYellowCards;
    }

    public Integer getYellowCards() {
        return yellowCards;
    }

    public void setYellowCards(Integer yellowCards) {
        this.yellowCards = yellowCards;
    }

    public Integer getRedCards() {
        return redCards;
    }

    public void setRedCards(Integer redCards) {
        this.redCards = redCards;
    }

    public Integer getActiveDisquals() {
        return activeDisquals;
    }

    public void setActiveDisquals(Integer activeDisquals) {
        this.activeDisquals = activeDisquals;
    }

    public Integer getDisquals() {
        return disquals;
    }

    public void setDisquals(Integer disquals) {
        this.disquals = disquals;
    }

    public Integer getMatches() {
        return matches;
    }

    public void setMatches(Integer matches) {
        this.matches = matches;
    }

    public Integer getGoals() {
        return goals;
    }

    public void setGoals(Integer goals) {
        this.goals = goals;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

}