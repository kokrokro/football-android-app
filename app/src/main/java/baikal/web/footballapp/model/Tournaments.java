package baikal.web.footballapp.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tournaments {

    @SerializedName("leagues")
    @Expose
    private List<League> leagues = null;

    @SerializedName("count")
    @Expose
    private Integer count;

    public List<League> getLeagues() {
        return leagues;
    }

    public void setLeagues(List<League> leagues) {
        this.leagues = leagues;
    }


    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}