package baikal.web.footballapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Stadium implements Serializable {
    @SerializedName("tourney")
    @Expose
    private String tourney;
    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private String address;

    public Stadium () {
        tourney = "";
        _id = "";
        name = "";
        address = "";
    }

    public String getTourney() {
        return tourney;
    }

    public void setTourney(String tourney) {
        this.tourney = tourney;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
