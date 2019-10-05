package baikal.web.footballapp.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Announces {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("announces")
    @Expose
    private List<Announce> announces = null;

    public List<Announce> getAnnounces() {
        return announces;
    }

    public void setAnnounces(List<Announce> announces) {
        this.announces = announces;
    }


    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}