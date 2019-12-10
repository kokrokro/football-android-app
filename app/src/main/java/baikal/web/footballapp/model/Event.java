package baikal.web.footballapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Event implements Serializable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("eventType")
    @Expose
    private String eventType;
    @SerializedName("person")
    @Expose
    private String person;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("team")
    @Expose
    private String team;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String player) {
        this.person = player;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String toString()
    {
        String ans = "";

        ans += "_id: " + getId() + "\n";
        ans += "eventType: " + getEventType() + "\n";
        ans += "team: " + getTeam() + "\n";
        ans += "time: " + getTime() + "\n";

        return ans;
    }
}