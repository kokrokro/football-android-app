package baikal.web.footballapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PendingTeamInvite implements Serializable {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("league")
    @Expose
    private String league;
    @SerializedName("team")
    @Expose
    private String team;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

}
