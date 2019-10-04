package com.example.alf_rb.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class PendingTeamInvite implements Serializable {

    @Getter @Setter
    @SerializedName("_id")
    @Expose
    private String id;
    @Getter @Setter
    @SerializedName("league")
    @Expose
    private String league;
    @Getter @Setter
    @SerializedName("team")
    @Expose
    private String team;


}
