package com.example.scryptan.popoika.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.scryptan.popoika.R;
import com.example.scryptan.popoika.Server.ApiUtils;
import com.example.scryptan.popoika.Server.Interfaces.GetUser;
import com.example.scryptan.popoika.Server.Objects.User;
import com.example.scryptan.popoika.Server.Objects.toServer.toGetUser;
import com.example.scryptan.popoika.Services.DeviceService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartActivity extends AppCompatActivity {

    private GetUser getUser;
    private Gson gson;
    private GsonBuilder builder;
    private String userJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //------------------------------------------------------------------------------------------
        builder = new GsonBuilder();
        gson = builder.create();
        //------------------------------------------------------------------------------------------
        getUser = ApiUtils.getUser();
        //------------------------------------------------------------------------------------------
        checkOnline();
    }
    private boolean checkOnline(){
        getUser.getUser(new toGetUser(new DeviceService(getContentResolver()).getDeviceID())).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    Intent intent;
                    switch (response.body().status){
                        case "online":
                            intent = new Intent(StartActivity.this, MapsActivity.class);
                            userJSON = gson.toJson(response.body());
                            intent.putExtra("User", userJSON);
                            startActivity(intent);
                            finish();
                            return;
                        default:
                            intent = new Intent(StartActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            return;
                    }
                }else{
                    Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        return false;
    }
}
