package baikal.web.footballapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RefereeRequestList {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("referees")
    @Expose
    private List<RefereeRequest> referees = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<RefereeRequest> getRefereeRequest() {
        return referees;
    }

    public void setRefereeRequest(List<RefereeRequest> referees) {
        this.referees = referees;
    }
}
