package com.example.alf_rb.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

public class User {


    @Getter @Setter
    @SerializedName("person")
    @Expose
    private Person person;
    @Getter @Setter
    @SerializedName("token")
    @Expose
    private String token;


}
