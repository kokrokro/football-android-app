package baikal.web.footballapp.protocol.CustomProtocolInput;

public class InputEventData {

    private String eventType;
    private int eventIconId;

    public InputEventData (String eventType, int eventIconId)  {
        this.eventType = eventType;
        this.eventIconId = eventIconId;
    }

    public int getEventIconId() {
        return eventIconId;
    }

    public String getEventType() {
        return eventType;
    }
}
