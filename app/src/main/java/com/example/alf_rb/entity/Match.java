package com.example.alf_rb.entity;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

public class Match {

    @Getter @Setter
    @SerializedName("_id")
    @Expose
    private String id;
    @Getter @Setter
    @SerializedName("date")
    @Expose
    private String date;
    @Getter @Setter
    @SerializedName("stage")
    @Expose
    private Boolean stage;
    @Getter @Setter
    @SerializedName("played")
    @Expose
    private Boolean played;
    @Getter @Setter
    @SerializedName("tour")
    @Expose
    private String tour;
    @Getter @Setter
    @SerializedName("playersList")
    @Expose
    private List<String> playersList = null;
    @Getter @Setter
    @SerializedName("place")
    @Expose
    private String place;
    @Getter @Setter
    @SerializedName("league")
    @Expose
    private String league;
    @Getter @Setter
    @SerializedName("teamOne")
    @Expose
    private String teamOne;
    @Getter @Setter
    @SerializedName("teamTwo")
    @Expose
    private String teamTwo;
    @Getter @Setter
    @SerializedName("events")
    @Expose
    private List<Event> events = null;
    @Getter @Setter
    @SerializedName("referries")
    @Expose
    private List<Object> referries = null;
    @Getter @Setter
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @Getter @Setter
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @Getter @Setter
    @SerializedName("__v")
    @Expose
    private Integer v;
    @Getter @Setter
    @SerializedName("referees")
    @Expose
    private List<Referee> referees = null;
    @Getter @Setter
    @SerializedName("score")
    @Expose
    private String score;
    @Getter @Setter
    @SerializedName("autoGoal")
    @Expose
    private String autoGoal;
    @Getter @Setter
    @SerializedName("penalty")
    @Expose
    private String penalty;
    @Getter @Setter
    @SerializedName("winner")
    @Expose
    private String winner;

}