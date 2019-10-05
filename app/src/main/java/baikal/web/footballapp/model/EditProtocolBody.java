package baikal.web.footballapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EditProtocolBody {
    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("events")
    @Expose
    private List<Event> events = null;

    @SerializedName("playersList")
    @Expose
    private List<String> playerList = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getPlayerList(){
        return playerList;
    }

    public void setPlayerList(List<String> playerList) {
        this.playerList = playerList;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
