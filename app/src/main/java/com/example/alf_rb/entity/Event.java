package com.example.alf_rb.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class Event implements Serializable {

    @Getter @Setter
    @SerializedName("_id")
    @Expose
    private String id;
    @Getter @Setter
    @SerializedName("eventType")
    @Expose
    private String eventType;
    @Getter @Setter
    @SerializedName("player")
    @Expose
    private String player;
    @Getter @Setter
    @SerializedName("time")
    @Expose
    private String time;

}