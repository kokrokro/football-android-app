package baikal.web.footballapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseInvite implements Serializable {
    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("team")
    @Expose
    private String team;
    @SerializedName("person")
    @Expose
    private String person;
    @SerializedName("status")
    @Expose
    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
