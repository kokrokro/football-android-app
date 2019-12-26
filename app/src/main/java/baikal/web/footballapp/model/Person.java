package baikal.web.footballapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Person implements Serializable {

    @SerializedName("surname")
    @Expose
    private String surname;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("favoriteTourney")
    @Expose
    private List<String> favoriteTourney = new ArrayList<>();
    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("login")
    @Expose
    private String login;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("birthdate")
    @Expose
    private String birthdate;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("id")
    @Expose
    private String id;


    @SerializedName("participationMatches")
    @Expose
    private List<Match> participationMatches = null;
    @SerializedName("participation")
    @Expose
    private List<PersonTeams> participation = null;
    @SerializedName("pastLeagues")
    @Expose
    private List<PastLeague> pastLeagues = null;
    @SerializedName("club")
    @Expose
    private String club;
    @SerializedName("pendingClubInvites")
    @Expose
    private List<Object> pendingClubInvites = null;
    @SerializedName("pendingTeamInvites")
    @Expose
    private List<PendingTeamInvite> pendingTeamInvites = null;

    public String getSurnameAndName() {
        String p1 = "";
        if (surname != null)
            p1 = surname;

        if (name != null)
            p1 += " " + name;

        return p1;
    }

    public String getSurnameWithInitials() {
        String p1 = "";
        String p2 = "";
        String p3 = "";

        if (surname != null)
            p1 = surname;

        if (name != null && name.length() > 0)
            p2 = " " + name.substring(0,1) + ".";

        if (lastname != null && lastname.length() > 0)
            p3 = " " + lastname.substring(0,1) + ".";

        return p1 + p2 + p3;
    }

    public String getNameWithSurname() {
        String result = "";

        if (name != null) {
            result = name;
        }

        if (surname != null) {
           result = result + " " + surname;
        }

        return result;
    }

    public String getSurnameNameLastName() {
        String result = "";

        if (surname != null)
            result = surname;
        if (name != null)
            result += " " + name;
        if (lastname != null)
            result += " " + lastname;

        return result;
    }

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

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String toString()
    {
        String ans = " \n\n";

        ans += "surname: "; ans += getName() + "\n";
        ans += "name: "; ans += getSurname() + "\n";
        ans += "lastname: "; ans += getLastname() + "\n";
        ans += "photo: "; ans += getPhoto() + "\n";
        ans += "favoriteTourney: "; ans += getFavouriteTourney().toString() + "\n";
        ans += "_id: "; ans += get_id() + "\n";
        ans += "login: "; ans += getLogin() + "\n";
        ans += "password: "; ans += getPassword() + "\n";
        ans += "region: "; ans += getRegion() + "\n";
        ans += "birthdate: "; ans += getBirthdate() + "\n";
        ans += "createdAt: "; ans += getCreatedAt() + "\n";
        ans += "updatedAt: "; ans += getUpdatedAt() + "\n";
        ans += "__v: "; ans += getV() + "\n";
        ans += "id: "; ans += getId() + "\n";

        return ans + " \n\n";
    }
}