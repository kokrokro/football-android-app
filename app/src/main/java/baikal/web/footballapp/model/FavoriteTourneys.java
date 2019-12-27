package baikal.web.footballapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FavoriteTourneys implements Serializable {
    @SerializedName("favoriteTourney")
    @Expose
    private List<String> favoriteTourney = new ArrayList<>();

    public FavoriteTourneys(List<String> favoriteTourney) {
        this.favoriteTourney.clear();
        this.favoriteTourney.addAll(favoriteTourney);
    }

    public List<String> getFavoriteTourney() {
        return favoriteTourney;
    }

    public void setFavoriteTourney(List<String> favoriteTourney) {
        this.favoriteTourney = favoriteTourney;
    }
}
