package com.example.scryptan.popoika.Views;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.scryptan.popoika.R;
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

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {

        }
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(56.851722, 60.566761);
       // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Lyceum")).setTag(new User("1","asdsad","Zhenya","Kukareku"));
        if(UserLocationListener.imHere!=null) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(UserLocationListener.imHere.getLatitude(), UserLocationListener.imHere.getLongitude())));
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