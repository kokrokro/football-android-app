package baikal.web.footballapp.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Clubs {

    @SerializedName("clubs")
    @Expose
    private List<Club> clubs = null;

    public List<Club> getClubs() {
        return clubs;
    }

    public void setClubs(List<Club> clubs) {
        this.clubs = clubs;
    }

}