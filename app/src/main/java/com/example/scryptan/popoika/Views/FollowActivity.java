package com.example.scryptan.popoika.Views;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scryptan.popoika.R;
import com.example.scryptan.popoika.Server.ApiUtils;
import com.example.scryptan.popoika.Server.Interfaces.FollowIvent;
import com.example.scryptan.popoika.Server.Objects.Ivent;
import com.example.scryptan.popoika.Server.Objects.User;
import com.example.scryptan.popoika.Server.Objects.toServer.toCreateNewIvent;
import com.example.scryptan.popoika.Server.Objects.toServer.toFollowIvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowActivity extends AppCompatActivity {

    private String iventJSON, adress, userJSON;
    private Gson gson;
    private GsonBuilder builder;
    private FollowIvent followIvent;
    Button followBTN;
    TextView nameTV, descriptionTV, adressTV, stackTV;
    ImageView photoIV;
    private Ivent ivent;
    List<Address> adresses;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        //------------------------------------------------------------------------------------------
        followIvent = ApiUtils.followIvent();
        //------------------------------------------------------------------------------------------
        followBTN = (Button) findViewById(R.id.followBTN);
        nameTV = (TextView) findViewById(R.id.nameTV);
        descriptionTV = (TextView) findViewById(R.id.descriptionTV);
        adressTV = (TextView) findViewById(R.id.adressTV);
        stackTV = (TextView) findViewById(R.id.stackTV);
        photoIV = (ImageView) findViewById(R.id.photoIV);
        //------------------------------------------------------------------------------------------
        Intent intent = getIntent();
        iventJSON = intent.getStringExtra("Ivent");
        userJSON = intent.getStringExtra("User");
        //------------------------------------------------------------------------------------------
        builder = new GsonBuilder();
        gson = builder.create();
        ivent = gson.fromJson(iventJSON, Ivent.class);
        user = gson.fromJson(userJSON, User.class);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        //------------------------------------------------------------------------------------------
        if(ivent!=null) {
            try {
                adresses = geocoder.getFromLocation(Double.parseDouble(ivent.Latitude), Double.parseDouble(ivent.Longitude), 1);
                adress = adresses.get(0).getAddressLine(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Picasso.with(getApplicationContext()).load(ivent.pic).into(photoIV);
            nameTV.setText(ivent.name);
            descriptionTV.setText(ivent.description);
            adressTV.setText(adress);
            stackTV.setText(ivent.amount+"/"+ivent.stack);
        }
        Picasso.with(getApplicationContext())
                .load(ivent.pic)
                .resize(100, 100)
                .centerCrop()
                .into(photoIV);
        if(user.party!=null||user.party!=""){
            followBTN.setVisibility(View.GONE);
        }
    }

    public void onClick(View view){
        FollowTheIvent();
    }
    public void FollowTheIvent(){
       followIvent.followIvent(new toFollowIvent(ivent._id,user.id)).enqueue(new Callback<User>() {
           @Override
           public void onResponse(Call<User> call, Response<User> response) {
               if(response.isSuccessful()){
                   finish();
               }
           }

           @Override
           public void onFailure(Call<User> call, Throwable t) {

           }
       });
    }
}
