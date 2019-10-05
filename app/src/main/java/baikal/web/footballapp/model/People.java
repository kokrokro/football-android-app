package baikal.web.footballapp.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class People {

    @SerializedName("people")
    @Expose
    private List<Person> people = null;
    @SerializedName("count")
    @Expose
    private Integer count;

    public List<Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
