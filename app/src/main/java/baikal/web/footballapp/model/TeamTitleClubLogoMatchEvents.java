package baikal.web.footballapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TeamTitleClubLogoMatchEvents implements Serializable {
    @SerializedName("clubLogo1")
    @Expose
    private String clubLogo1;
    @SerializedName("nameTeam1")
    @Expose
    private String nameTeam1;
    @SerializedName("clubLogo2")
    @Expose
    private String clubLogo2;
    @SerializedName("nameTeam2")
    @Expose
    private String nameTeam2;
    @SerializedName("playerEvents")
    @Expose
    private List<PlayerEvent> playerEvents = null;

    public List<PlayerEvent> getPlayerEvents() {
        return playerEvents;
    }

    public void setPlayerEvents(List<PlayerEvent> playerEvents) {
        this.playerEvents = playerEvents;
    }
    public String getClubLogo1() {
        return clubLogo1;
    }

    public void setClubLogo1(String clubLogo1) {
        this.clubLogo1 = clubLogo1;
    }
    public String getNameTeam1() {
        return nameTeam1;
    }

    public void setNameTeam1(String nameTeam1) {
        this.nameTeam1 = nameTeam1;

    }
    public String getClubLogo2() {
        return clubLogo2;
    }

    public void setClubLogo2(String clubLogo2) {
        this.clubLogo2 = clubLogo2;
    }
    public String getNameTeam2() {
        return nameTeam2;
    }

    public void setNameTeam2(String nameTeam2) {
        this.nameTeam2 = nameTeam2;
    }
}
