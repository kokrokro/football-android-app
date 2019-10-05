package baikal.web.footballapp.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Advertisings {

    @SerializedName("ads")
    @Expose
    private List<Ad> ads = null;
    @SerializedName("count")
    @Expose
    private Integer count;

    public List<Ad> getAds() {
        return ads;
    }

    public void setAds(List<Ad> ads) {
        this.ads = ads;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}