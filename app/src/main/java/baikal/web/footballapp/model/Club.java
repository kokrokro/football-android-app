package baikal.web.footballapp.model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Club implements Serializable {

    @SerializedName("info")
    @Expose
    private String info;
    @SerializedName("addLogo")
    @Expose
    private String addLogo;
    @SerializedName("addInfo")
    @Expose
    private String addInfo;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("owner")
    @Expose
    private Owner owner;
//    private Person owner;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getAddLogo() {
        return addLogo;
    }

    public void setAddLogo(String addLogo) {
        this.addLogo = addLogo;
    }

    public String getAddInfo() {
        return addInfo;
    }

    public void setAddInfo(String addInfo) {
        this.addInfo = addInfo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }


}