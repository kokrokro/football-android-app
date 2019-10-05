package baikal.web.footballapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PlayerEvent implements Serializable {
    @SerializedName("clubLogo")
    @Expose
    private String clubLogo;
    @SerializedName("nameTeam")
    @Expose
    private String nameTeam;
    @SerializedName("person")
    @Expose
    private Person person = null;
    @SerializedName("event")
    @Expose
    private Event event = null;
    public String getClubLogo() {
        return clubLogo;
    }

    public void setClubLogo(String clubLogo) {
        this.clubLogo = clubLogo;
    }
    public String getNameTeam() {
        return nameTeam;
    }

    public void setNameTeam(String nameTeam) {
        this.nameTeam = nameTeam;
    }
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
