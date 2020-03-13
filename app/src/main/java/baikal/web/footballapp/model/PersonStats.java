package baikal.web.footballapp.model;

import android.util.Log;

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
    private Integer goals;
    @SerializedName("parrent")
    @Expose
    private String parrent;
    @SerializedName("on_")
    @Expose
    private String on_;
    @SerializedName("onModel")
    @Expose
    private String onModel;

    public PersonStats addPersonStats (PersonStats personStats) {
        PersonStats ans = new PersonStats(this);

        if (getPerson() == null) {
            Log.d("PERSON STATS", "SUM EVALUATION !!!!!!!!!!!!!!!!!!!!!!!!!!! setting person");
            setPerson(personStats.getPerson());
            ans.setPerson(personStats.getPerson());
        }

        if (getOnModel() == null) {
            Log.d("PERSON STATS", "SUM EVALUATION !!!!!!!!!!!!!!!!!!!!!!!!!!! setting onModel");
            setOnModel(personStats.getOnModel());
            ans.setOnModel(personStats.getOnModel());
        }

//        if (getOn_() == null) {
//            setOn_(personStats.getOn_());
//            ans.setOn_(personStats.getOn_());
//        }

        if (ans.getOnModel().equals(personStats.getOnModel()) && ans.getPerson().equals(personStats.getPerson())) {
            Log.d("PERSON STATS", "SUM EVALUATION !!!!!!!!!!!!!!!!!!!!!!!!!!!");
            ans.setYellowCards(
                    ans.getYellowCards() + personStats.getYellowCards());

            ans.setRedCards(
                    ans.getRedCards() + personStats.getRedCards());

            ans.setGoals(
                    ans.getGoals() + personStats.getGoals());

            ans.setDisquals(
                    ans.getDisquals() + personStats.getDisquals());

            ans.setMatches(
                    ans.getMatches() + personStats.getMatches());
        }

        return ans;
    }

    public PersonStats () {
        person = null;
        onModel = null;
        on_ = null;
        parrent = null;

        yellowCards = 0;
        redCards = 0;
        goals = 0;
        disquals = 0;
        matches = 0;
    }

    public PersonStats (PersonStats personStats) {
        setPerson(personStats.getPerson());
        setYellowCards(personStats.getYellowCards());
        setRedCards(personStats.getRedCards());
        setGoals(personStats.getGoals());
        setDisquals(personStats.getDisquals());
        setMatches(personStats.getMatches());
        setParrent(personStats.getParrent());
        setOn_(personStats.getOn_());
        setOnModel(personStats.getOnModel());
    }

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

    public Integer getGoals() {
        return goals;
    }

    public void setGoals(Integer goals) {
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
