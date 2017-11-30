package com.example.scryptan.popoika.Server.Interfaces;

import com.example.scryptan.popoika.Server.Objects.User;
import com.example.scryptan.popoika.Server.Objects.toServer.toLogin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Login {
    @POST("/login")
    Call<User> login(@Body toLogin login);
}
