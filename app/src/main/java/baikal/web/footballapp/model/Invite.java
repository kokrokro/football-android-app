package baikal.web.footballapp.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Invite implements Serializable {
    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("team")
    @Expose
    private Team team;
    @SerializedName("person")
    @Expose
    private Person person;
    @SerializedName("status")
    @Expose
    private String status;

    public Invite () {
        team = null;
        person = null;
        status = null;
        _id = null;
    }

    @NonNull
    public String toString() {
        return String.format("_id: %s\nperson: %s\nteam: %s\nstatus: %s\n", get_id(), getPerson().getId(), getTeam(), getStatus());
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
