package com.example.scryptan.popoika.Server.Interfaces;

import com.example.scryptan.popoika.Server.Objects.Ivent;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetIvents {
    @GET("/getIvents")
    Call<List<Ivent>> getIvents();
}
