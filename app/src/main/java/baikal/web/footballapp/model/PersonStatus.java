package baikal.web.footballapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PersonStatus implements Serializable {

    @SerializedName("number")
    @Expose
    private Integer number ;
    @SerializedName("played")
    @Expose
    private Boolean played;
    @SerializedName("activeYellowCards")
    @Expose
    private Integer activeYellowCards;
    @SerializedName("activeDisquals")
    @Expose
    private Integer activeDisquals;
    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("team")
    @Expose
    private String team;
    @SerializedName("person")
    @Expose
    private String person;
    @SerializedName("league")
    @Expose
    private String league;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Boolean getPlayed() {
        return played;
    }

    public void setPlayed(Boolean played) {
        this.played = played;
    }

    public Integer getActiveYellowCards() {
        return activeYellowCards;
    }

    public void setActiveYellowCards(Integer activeYellowCards) {
        this.activeYellowCards = activeYellowCards;
    }

    public Integer getActiveDisquals() {
        return activeDisquals;
    }

    public void setActiveDisquals(Integer activeDisquals) {
        this.activeDisquals = activeDisquals;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }
}
