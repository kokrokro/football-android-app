package baikal.web.footballapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetLeagueInfo implements Serializable {

    @SerializedName("league")
    @Expose
    private LeagueInfo league;

    public LeagueInfo getLeagueInfo() {
        return league;
    }

    public void setLeagueInfo(LeagueInfo league) {
        this.league = league;
    }

}
