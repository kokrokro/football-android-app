package baikal.web.footballapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Announce {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

}