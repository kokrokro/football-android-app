package baikal.web.footballapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("person")
    @Expose
    private Person person;
    @SerializedName("token")

    @Expose
    private String token;

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

}