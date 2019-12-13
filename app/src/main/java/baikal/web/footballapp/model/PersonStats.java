package baikal.web.footballapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PersonStats implements Serializable {
    @SerializedName("person")
    @Expose
    private String person;
    @SerializedName("yellowCards")
    @Expose
    private Integer yellowCards;
    @SerializedName("redCards")
    @Expose
    private Integer redCards;
    @SerializedName("disquals")
    @Expose
    private Integer disquals;
    @SerializedName("matches")
    @Expose
    private Integer matches;
    @SerializedName("goals")
    @Expose
    private String goals;
    @SerializedName("parrent")
    @Expose
    private String parrent;
    @SerializedName("on_")
    @Expose
    private String on_;
    @SerializedName("onModel")
    @Expose
    private String onModel;

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public Integer getYellowCards() {
        return yellowCards;
    }

    public void setYellowCards(Integer yellowCards) {
        this.yellowCards = yellowCards;
    }

    public Integer getRedCards() {
        return redCards;
    }

    public void setRedCards(Integer redCards) {
        this.redCards = redCards;
    }

    public Integer getDisquals() {
        return disquals;
    }

    public void setDisquals(Integer disquals) {
        this.disquals = disquals;
    }

    public Integer getMatches() {
        return matches;
    }

    public void setMatches(Integer matches) {
        this.matches = matches;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public String getParrent() {
        return parrent;
    }

    public void setParrent(String parrent) {
        this.parrent = parrent;
    }

    public String getOn_() {
        return on_;
    }

    public void setOn_(String on_) {
        this.on_ = on_;
    }

    public String getOnModel() {
        return onModel;
    }

    public void setOnModel(String onModel) {
        this.onModel = onModel;
    }
}
