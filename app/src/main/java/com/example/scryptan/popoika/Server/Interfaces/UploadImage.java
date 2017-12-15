package com.example.scryptan.popoika.Server.Interfaces;

import com.example.scryptan.popoika.Server.Objects.User;
import com.example.scryptan.popoika.Server.Objects.toServer.toCreateNewIvent;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadImage {
    @Multipart
    @POST("/addIvent")
    Call<User> upload(
            @Part MultipartBody.Part file,
            @Part("name") RequestBody name,
            @Part("description") RequestBody desc,
            @Part("latitude") RequestBody lat,
            @Part("longitude") RequestBody longi,
            @Part("stack") RequestBody stack,
            @Part("amount") RequestBody amount,
            @Part("_id") RequestBody userId
    );
}