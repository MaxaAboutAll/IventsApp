package com.example.scryptan.popoika.Views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.scryptan.popoika.R;
import com.example.scryptan.popoika.Server.ApiUtils;
import com.example.scryptan.popoika.Server.Interfaces.GetIvents;
import com.example.scryptan.popoika.Server.Objects.Ivent;
import com.example.scryptan.popoika.Server.Objects.User;
import com.example.scryptan.popoika.Services.UserLocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private GetIvents getIvents;
    private List<Ivent> ivents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //------------------------------------------------------------------------------------------
        UserLocationListener.SetUpLocationListener(getApplicationContext());
        getIvents = ApiUtils.getIvents();
        //------------------------------------------------------------------------------------------
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //------------------------------------------------------------------------------------------
        getAllIvents();
    }

    public void getAllIvents(){
        getIvents.getIvents().enqueue(new Callback<List<Ivent>>() {
            @Override
            public void onResponse(Call<List<Ivent>> call, Response<List<Ivent>> response) {
                ivents = response.body();
                setMap();
            }

            @Override
            public void onFailure(Call<List<Ivent>> call, Throwable t) {

            }
        });
    }

    public void setMap(){
        for (int i = 0; i <ivents.size() ; i++) {
            LatLng location = new LatLng(Double.parseDouble(ivents.get(i).Latitude),Double.parseDouble(ivents.get(i).Longitude));
            String title = ivents.get(i).name;
            mMap.addMarker(new MarkerOptions().position(location).title(title)).setTag(ivents.get(0));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        if(UserLocationListener.imHere!=null) {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(UserLocationListener.imHere.getLatitude(), UserLocationListener.imHere.getLongitude())).zoom(17).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }
    public void onClick(View view){
       switch (view.getId()){
            case R.id.addFAB:
                Intent intent = new Intent(MapsActivity.this, AddActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        User newUser = (User)marker.getTag();
        Toast.makeText(getApplicationContext(),newUser.id+" "+newUser.telephoneId+" "+newUser.nick+" "+newUser.pswd,Toast.LENGTH_SHORT).show();
        return false;
    }
}