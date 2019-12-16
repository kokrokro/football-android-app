package baikal.web.footballapp.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Referee implements Serializable {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("person")
    @Expose
    private String person;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    @NonNull
    public String toString()
    {
        return getType() + " " + getPerson();
    }
}