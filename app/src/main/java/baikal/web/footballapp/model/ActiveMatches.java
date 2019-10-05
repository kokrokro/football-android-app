package baikal.web.footballapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActiveMatches {
    @SerializedName("matches")
    @Expose
    private List<ActiveMatch> matches = null;
    @SerializedName("count")
    @Expose
    private Integer count;

    public List<ActiveMatch> getMatches() {
        return matches;
    }

    public void setMatches(List<ActiveMatch> matches) {
        this.matches = matches;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}