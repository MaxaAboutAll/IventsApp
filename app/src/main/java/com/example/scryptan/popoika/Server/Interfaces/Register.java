package com.example.scryptan.popoika.Server.Interfaces;

import com.example.scryptan.popoika.Server.Objects.User;
import com.example.scryptan.popoika.Server.Objects.toServer.toRegister;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Register {
    @POST("/login")
    Call<User> register(@Body toRegister user);
}
