package com.example.scryptan.popoika.Server.Interfaces;

import com.example.scryptan.popoika.Server.Objects.Ivent;
import com.example.scryptan.popoika.Server.Objects.User;
import com.example.scryptan.popoika.Server.Objects.toServer.toCreateNewIvent;
import com.example.scryptan.popoika.Server.Objects.toServer.toGoogleSignIn;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CreateNewIvent {
    @POST("/createNewIvent")
    Call<Ivent> createNewIvent(@Body toCreateNewIvent toCreateNewIvent);
}
