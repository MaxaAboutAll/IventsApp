package com.example.scryptan.popoika.Server.Interfaces;

import com.example.scryptan.popoika.Server.Objects.User;
import com.example.scryptan.popoika.Server.Objects.toServer.toGetMyPartyFriends;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface GetMyPartyFriends {
    @POST("/getMyPartFriends")
    Call<List<User>> getFriends(@Body toGetMyPartyFriends toGetMyPartyFriends);
}
