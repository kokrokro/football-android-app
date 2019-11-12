package baikal.web.footballapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Person implements Serializable {
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("surname")
    @Expose
    private String surname;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("birthdate")
    @Expose
    private String birthdate;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("participationMatches")
    @Expose
    private List<Match> participationMatches = null;
    @SerializedName("participation")
    @Expose
    private List<PersonTeams> participation = null;
    @SerializedName("pastLeagues")
    @Expose
    private List<PastLeague> pastLeagues = null;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("club")
    @Expose
    private String club;
//    private Club club;
    @SerializedName("pendingClubInvites")
    @Expose
    private List<Object> pendingClubInvites = null;
    @SerializedName("login")
    @Expose
    private String login;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("pendingTeamInvites")
    @Expose
    private List<PendingTeamInvite> pendingTeamInvites = null;
    @SerializedName("favoriteTourney")
    @Expose
    private List<String> favoriteTourney = new ArrayList<>();

    public List<String> getFavouriteTourney() {  return favoriteTourney;    }

    public void setFavouriteTourney(List<String> favouriteTourney) {  this.favoriteTourney = favouriteTourney; }

    public String getRegion() { return region; }

    public void setRegion(String region) { this.region = region;  }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<PersonTeams> getParticipation() {
        return participation;
    }

    public void setParticipation(List<PersonTeams> participation) {
        this.participation = participation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public List<Object> getPendingClubInvites() {
        return pendingClubInvites;
    }

    public void setPendingClubInvites(List<Object> pendingClubInvites) {
        this.pendingClubInvites = pendingClubInvites;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public List<PendingTeamInvite> getPendingTeamInvites() {
        return pendingTeamInvites;
    }

    public void setPendingTeamInvites(List<PendingTeamInvite> pendingTeamInvites) {
        this.pendingTeamInvites = pendingTeamInvites;
    }
    public List<PastLeague> getPastLeagues() {
        return pastLeagues;
    }

    public void setPastLeagues(List<PastLeague> pastLeagues) {
        this.pastLeagues = pastLeagues;
    }
    public void setParticipationMatches(List<Match> participationMatches) {
        this.participationMatches = participationMatches;
    }
    public List<Match> getParticipationMatches() {
        return participationMatches;
    }

}