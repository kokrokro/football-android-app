package com.example.alf_rb.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class Person {

    @Getter @Setter
    @SerializedName("_id")
    @Expose
    private String id;
    @Getter @Setter
    @SerializedName("surname")
    @Expose
    private String surname;
    @Getter @Setter
    @SerializedName("name")
    @Expose
    private String name;
    @Getter @Setter
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @Getter @Setter
    @SerializedName("birthdate")
    @Expose
    private String birthdate;
    @Getter @Setter
    @SerializedName("photo")
    @Expose
    private String photo;
    @Getter @Setter
    @SerializedName("desc")
    @Expose
    private String desc;
    @Getter @Setter
    @SerializedName("participationMatches")
    @Expose
    private List<Match> participationMatches = null;
    @Getter @Setter
    @SerializedName("participation")
    @Expose
    private List<PersonTeams> participation = null;
    @Getter @Setter
    @SerializedName("pastLeagues")
    @Expose
    private List<PastLeague> pastLeagues = null;
    @Getter @Setter
    @SerializedName("club")
    @Expose
    private String club;
    @Getter @Setter
    @SerializedName("pendingClubInvites")
    @Expose
    private List<Object> pendingClubInvites = null;
    @Getter @Setter
    @SerializedName("login")
    @Expose
    private String login;
    @Getter @Setter
    @SerializedName("password")
    @Expose
    private String password;
    @Getter @Setter
    @SerializedName("type")
    @Expose
    private String type;
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
    @SerializedName("pendingTeamInvites")
    @Expose
    private List<PendingTeamInvite> pendingTeamInvites = null;

}
