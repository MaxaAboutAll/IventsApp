package com.example.scryptan.popoika.Server.Interfaces;


import com.example.scryptan.popoika.Server.Objects.User;
import com.example.scryptan.popoika.Server.Objects.toServer.toGoogleSignIn;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GoogleSignIn {
    @POST("/googleSignIn")
    Call<User> googleSignIn(@Body toGoogleSignIn toGoogleSignIn);
}