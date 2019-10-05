package baikal.web.footballapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddTeam {

    @SerializedName("team")
    @Expose
    private Team team;


    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

}