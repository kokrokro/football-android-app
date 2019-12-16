package baikal.web.footballapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    @SerializedName("person")
    @Expose
    private Person person;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("recentReferees")
    @Expose
    private List<String> recentReferees;

    public Person getUser() {
        return person;
    }

    public void setUser(Person person) {
        this.person = person;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<String> getRecentReferees() { return recentReferees; }

    public void setRecentReferees(List<String> recentReferees) {this.recentReferees = recentReferees; }
}