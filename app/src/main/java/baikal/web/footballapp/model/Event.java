package baikal.web.footballapp.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Event implements Serializable {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("event")
    @Expose
    private String event;
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

    public Event() {
        id = event = eventType = person = time = team = null;
    }

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

    @NonNull
    public String toString()
    {
        String ans = "";

        ans += "_id: " + getId()==null?"null ":getId() + " \n";
        ans += "eventType: " + getEventType() + " \n";
        ans += "event: " + getEvent()==null?"null ":getEvent() + " \n";
        ans += "team: " + getTeam()==null?"null ":getTeam() + " \n";
        ans += "time: " + getTime()==null?"null ":getTime() + " \n \n ";
        return ans;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}