package com.example.scryptan.popoika.Server.Interfaces;

import com.example.scryptan.popoika.Server.Objects.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetMyPartyFriends {
    @GET
    Call<List<User>> getFriends();
}
