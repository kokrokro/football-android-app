package baikal.web.footballapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PastLeague implements Serializable {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("tourney")
    @Expose
    private String tourney;
    @SerializedName("teamName")
    @Expose
    private String teamName;
    @SerializedName("place")
    @Expose
    private String place;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
    public String getTourney() {
        return tourney;
    }

    public void setTourney(String tourney) {
        this.tourney = tourney;
    }
    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
