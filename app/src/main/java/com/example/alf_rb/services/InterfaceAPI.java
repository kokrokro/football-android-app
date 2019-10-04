package com.example.alf_rb.services;

import com.example.alf_rb.entity.User;
import com.example.alf_rb.entity.body.SignIn;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InterfaceAPI {

    //sign in web
    @POST("/api/signin")
    Call<User> signIn(@Body SignIn body);


}
