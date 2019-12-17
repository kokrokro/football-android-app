package baikal.web.footballapp.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class League implements Serializable {
    @SerializedName("creator")
    @Expose
    private String creator;
    @SerializedName("mainReferee")
    @Expose
    private String mainReferee;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("matches")
    @Expose
    private List<Match> matches = null;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("tourney")
    @Expose
    private String tourney;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("beginDate")
    @Expose
    private String beginDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("transferBegin")
    @Expose
    private String transferBegin;
    @SerializedName("transferEnd")
    @Expose
    private String transferEnd;
    @SerializedName("playersMin")
    @Expose
    private Integer playersMin;
    @SerializedName("playersMax")
    @Expose
    private Integer playersMax;
    @SerializedName("playersCapacity")
    @Expose
    private Integer playersCapacity;
    @SerializedName("ageAllowedMin")
    @Expose
    private Integer ageAllowedMin;
    @SerializedName("ageAllowedMax")
    @Expose
    private Integer ageAllowedMax;
    @SerializedName("maxTeams")
    @Expose
    private Integer maxTeams;
    @SerializedName("teams")
    @Expose
    private List<Team> teams = null;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTourney() {
        return tourney;
    }

    public void setTourney(String tourney) {
        this.tourney = tourney;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public Integer getPlayersMin() {
        return playersMin;
    }

    public void setPlayersMin(Integer playersMin) {
        this.playersMin = playersMin;
    }

    public Integer getPlayersMax() {
        return playersMax;
    }

    public void setPlayersMax(Integer playersMax) {
        this.playersMax = playersMax;
    }

    public Integer getPlayersCapacity() {
        return playersCapacity;
    }

    public void setPlayersCapacity(Integer playersCapacity) {
        this.playersCapacity = playersCapacity;
    }

    public Integer getAgeAllowedMin() {
        return ageAllowedMin;
    }

    public void setAgeAllowedMin(Integer ageAllowedMin) {
        this.ageAllowedMin = ageAllowedMin;
    }

    public Integer getAgeAllowedMax() {
        return ageAllowedMax;
    }

    public void setAgeAllowedMax(Integer ageAllowedMax) {
        this.ageAllowedMax = ageAllowedMax;
    }

    public Integer getMaxTeams() {
        return maxTeams;
    }

    public void setMaxTeams(Integer maxTeams) {
        this.maxTeams = maxTeams;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getMainReferee() {
        return mainReferee;
    }

    public void setMainReferee(String mainReferee) {
        this.mainReferee = mainReferee;
    }
}
