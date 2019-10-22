package baikal.web.footballapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Stage implements Serializable {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("league")
    @Expose
    private String league;
    @SerializedName("sheduleIsCreated")
    @Expose
    private Boolean sheduleIsCreated;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getLeague(){
        return  this.league;
    }
    public void setLeague(String l){
        this.league = l;
    }
    public Boolean getSheduleIsCreated(){
        return this.sheduleIsCreated;
    }
    public void setSheduleIsCreated(Boolean s){
        this.sheduleIsCreated = s;
    }

}
