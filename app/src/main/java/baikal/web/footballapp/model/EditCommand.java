package baikal.web.footballapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EditCommand {
    @SerializedName("_id")
    private
    String id;
    @SerializedName("teamId")
    private String teamId;
    @SerializedName("players")
    private
    List<Player> players;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

}
