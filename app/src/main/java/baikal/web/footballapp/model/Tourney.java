package baikal.web.footballapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Tourney implements Serializable {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("creator")
    @Expose
    private String creator;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("maxTeams")
    @Expose
    private Integer maxTeams;
    @SerializedName("maxPlayersInMatch")
    @Expose
    private Integer maxPlayersInMatch;
    @SerializedName("transferBegin")
    @Expose
    private String transferBegin;
    @SerializedName("transferEnd")
    @Expose
    private String transferEnd;
    @SerializedName("beginDate")
    @Expose
    private String beginDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("drawDateTime")
    @Expose
    private String drawDateTime;
    @SerializedName("mainReferee")
    @Expose
    private String mainReferee;
    @SerializedName("playersMin")
    @Expose
    private Integer playersMin;
    @SerializedName("playersMax")
    @Expose
    private Integer playersMax;
    @SerializedName("yellowCardsToDisqual")
    @Expose
    private Integer yellowCardsToDisqual;
    @SerializedName("ageAllowedMin")
    @Expose
    private Integer ageAllowedMin;
    @SerializedName("ageAllowedMax")
    @Expose
    private Integer ageAllowedMax;
    @SerializedName("canBeAddNonPlayed")
    @Expose
    private Boolean canBeAddNonPlayed;
    @SerializedName("canBeDeleteNonPlayed")
    @Expose
    private Boolean canBeDeleteNonPlayed;
    @SerializedName("players")
    @Expose
    List<Person> players;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Integer getMaxTeams() {
        return maxTeams;
    }

    public void setMaxTeams(Integer maxTeams) {
        this.maxTeams = maxTeams;
    }

    public Boolean getCanBeAddNonPlayed() {
        return canBeAddNonPlayed;
    }

    public void setCanBeAddNonPlayed(Boolean canBeAddNonPlayed) {
        this.canBeAddNonPlayed = canBeAddNonPlayed;
    }

    public Boolean getCanBeDeleteNonPlayed() {
        return canBeDeleteNonPlayed;
    }

    public void setCanBeDeleteNonPlayed(Boolean canBeDeleteNonPlayed) {
        this.canBeDeleteNonPlayed = canBeDeleteNonPlayed;
    }

    public Integer getAgeAllowedMax() {
        return ageAllowedMax;
    }

    public void setAgeAllowedMax(Integer ageAllowedMax) {
        this.ageAllowedMax = ageAllowedMax;
    }

    public Integer getAgeAllowedMin() {
        return ageAllowedMin;
    }

    public void setAgeAllowedMin(Integer ageAllowedMin) {
        this.ageAllowedMin = ageAllowedMin;
    }

    public Integer getMaxPlayersInMatch() {
        return maxPlayersInMatch;
    }

    public void setMaxPlayersInMatch(Integer maxPlayersInMatch) {
        this.maxPlayersInMatch = maxPlayersInMatch;
    }

    public Integer getPlayersMax() {
        return playersMax;
    }

    public void setPlayersMax(Integer playersMax) {
        this.playersMax = playersMax;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public Integer getPlayersMin() {
        return playersMin;
    }

    public void setPlayersMin(Integer playersMin) {
        this.playersMin = playersMin;
    }

    public Integer getYellowCardsToDisqual() {
        return yellowCardsToDisqual;
    }

    public void setYellowCardsToDisqual(Integer yellowCardsToDisqual) {
        this.yellowCardsToDisqual = yellowCardsToDisqual;
    }

    public List<Person> getPlayers() {
        return players;
    }

    public void setPlayers(List<Person> players) {
        this.players = players;
    }

    public String getDrawDateTime() {
        return drawDateTime;
    }

    public void setDrawDateTime(String drawDateTime) {
        this.drawDateTime = drawDateTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getMainReferee() {
        return mainReferee;
    }

    public void setMainReferee(String mainReferee) {
        this.mainReferee = mainReferee;
    }

    public String getTransferBegin() {
        return transferBegin;
    }

    public void setTransferBegin(String transferBegin) {
        this.transferBegin = transferBegin;
    }

    public String getTransferEnd() {
        return transferEnd;
    }

    public void setTransferEnd(String transferEnd) {
        this.transferEnd = transferEnd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
