package baikal.web.footballapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SetRefereeList {
    @SerializedName("refereeRequestList")
    @Expose
    private List<RefereeRequestList> refereeRequestList = null;

    public List<RefereeRequestList> getRefereeRequestList() {
        return refereeRequestList;
    }

    public void setRefereeRequestList(List<RefereeRequestList> refereeRequestList) {
        this.refereeRequestList = refereeRequestList;
    }

}
