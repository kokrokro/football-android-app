package com.example.alf_rb.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

public class PastLeague {

    @Getter @Setter
    @SerializedName("name")
    @Expose
    private String name;
    @Getter @Setter
    @SerializedName("tourney")
    @Expose
    private String tourney;
    @Getter @Setter
    @SerializedName("teamName")
    @Expose
    private String teamName;
    @Getter @Setter
    @SerializedName("place")
    @Expose
    private String place;
}
