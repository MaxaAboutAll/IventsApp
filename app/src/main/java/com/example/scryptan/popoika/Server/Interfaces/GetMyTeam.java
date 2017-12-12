package com.example.scryptan.popoika.Server.Interfaces;

import com.example.scryptan.popoika.Server.Objects.Ivent;
import com.example.scryptan.popoika.Server.Objects.toServer.toGetMyTeam;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GetMyTeam {
    @POST
    Call<Ivent> getMyTeam(@Body toGetMyTeam toGetMyTeam);
}
