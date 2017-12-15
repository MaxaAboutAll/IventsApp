package com.example.scryptan.popoika.Server.Interfaces;

import com.example.scryptan.popoika.Server.Objects.User;
import com.example.scryptan.popoika.Server.Objects.toServer.toGetUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GetUser {
    @POST("/getUser")
    Call<User> getUser(@Body toGetUser toGetUser);
}
