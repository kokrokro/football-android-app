package baikal.web.footballapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EventList implements Serializable {
    @SerializedName("events")
    @Expose
    private List<Event> events;

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void addEvent (Event event) {
        if (events == null)
            events = new ArrayList<>();
        this.events.add(event);
    }
}
