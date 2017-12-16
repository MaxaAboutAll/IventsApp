package com.example.scryptan.popoika.Server.Interfaces;

import com.example.scryptan.popoika.Server.Objects.User;
import com.example.scryptan.popoika.Server.Objects.toServer.toDeleteIvent;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DeleteIvent {
    @POST("/deleteIvent")
    Call<User> deleteIvent(@Body toDeleteIvent toDeleteIvent);
}
