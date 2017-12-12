package com.example.scryptan.popoika.Views;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.scryptan.popoika.R;
import com.example.scryptan.popoika.Server.Objects.Ivent;
import com.example.scryptan.popoika.Server.Objects.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class TeamActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private LinearLayout contentLL;
    private Button contentBTN;
    private TextView nameTV, descriptionTV, adressTV, stackTV;
    private ImageView photoIV;
    private Ivent ivent;
    private Gson gson;
    private GsonBuilder builder;
    private String iventJSON, adress;
    private Geocoder geocoder;
    List<Address> adresses;
    List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        //------------------------------------------------------------------------------------------
        contentBTN = (Button) findViewById(R.id.contentBTN);
        contentLL = (LinearLayout) findViewById(R.id.contentLL);
        nameTV = (TextView) findViewById(R.id.nameTV);
        descriptionTV = (TextView) findViewById(R.id.descriptionTV);
        adressTV = (TextView) findViewById(R.id.adressTV);
        stackTV = (TextView) findViewById(R.id.stackTV);
        photoIV = (ImageView) findViewById(R.id.photoIV);
        recyclerView = (RecyclerView) findViewById(R.id.contentRV);
        contentLL.setVisibility(View.GONE);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //------------------------------------------------------------------------------------------
        builder = new GsonBuilder();
        gson = builder.create();
        //------------------------------------------------------------------------------------------
        Intent intent = new Intent();
        iventJSON = intent.getStringExtra("Ivent");
        ivent = gson.fromJson(iventJSON, Ivent.class);
        geocoder = new Geocoder(this, Locale.getDefault());
        //------------------------------------------------------------------------------------------
        setData();
        setRecyclerView();
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.contentBTN:
                contentLL.setVisibility(View.VISIBLE);
                contentBTN.setVisibility(View.GONE);
                return;
            case R.id.contentLL:
                contentLL.setVisibility(View.GONE);
                contentBTN.setVisibility(View.VISIBLE);
                return;
        }
    }

    private void setRecyclerView(){
        mAdapter = new MyAdapter(users,getApplicationContext());
        recyclerView.setAdapter(mAdapter);
    }

    private void setData(){
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
    }

}
