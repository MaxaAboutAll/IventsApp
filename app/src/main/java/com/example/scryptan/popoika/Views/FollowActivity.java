package com.example.scryptan.popoika.Views;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.scryptan.popoika.R;
import com.example.scryptan.popoika.Server.Objects.Ivent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class FollowActivity extends AppCompatActivity {

    private String iventJSON, adress;
    private Gson gson;
    private GsonBuilder builder;
    TextView nameTV, descriptionTV, adressTV;
    ImageView photoIV;
    private Ivent ivent;
    List<Address> adresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        //-----------------------------------------------------------
        nameTV = (TextView) findViewById(R.id.nameTV);
        descriptionTV = (TextView) findViewById(R.id.descriptionTV);
        adressTV = (TextView) findViewById(R.id.adressTV);
        photoIV = (ImageView) findViewById(R.id.photoIV);
        //-----------------------------------------------------------
        Intent intent = new Intent();
        iventJSON = intent.getStringExtra("ivent");
        //-----------------------------------------------------------
        builder = new GsonBuilder();
        gson = builder.create();
        ivent = gson.fromJson(iventJSON, Ivent.class);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        //-----------------------------------------------------------
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
        }
    }
}
