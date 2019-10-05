package baikal.web.footballapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataClub implements Serializable {
    //    @SerializedName("Club")
//    private Club club;
//
//    public Club getClub() {
//        return club;
//    }
//
//    public void setClub(Club club) {
//        this.club = club;
//    }
    @SerializedName("club")
    @Expose
    private Club club;

    public Club getResultClub() {
        return club;
    }

    public void setResultClub(Club person) {
        this.club = club;
    }

}
