package com.example.scryptan.popoika.Server.Interfaces;

import com.example.scryptan.popoika.Server.Objects.User;
import com.example.scryptan.popoika.Server.Objects.toServer.toExitIvent;
import com.example.scryptan.popoika.Server.Objects.toServer.toFollowIvent;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ExitIvent {
    @POST("/exitIvent")
    Call<User> exitIvent(@Body toExitIvent toExitIvent);
}
