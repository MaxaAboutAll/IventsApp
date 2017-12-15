package com.example.scryptan.popoika.Views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.scryptan.popoika.R;
import com.example.scryptan.popoika.Server.ApiUtils;
import com.example.scryptan.popoika.Server.Interfaces.ExitIvent;
import com.example.scryptan.popoika.Server.Interfaces.FollowIvent;
import com.example.scryptan.popoika.Server.Interfaces.GetIvents;
import com.example.scryptan.popoika.Server.Interfaces.GetMyTeam;
import com.example.scryptan.popoika.Server.Objects.Ivent;
import com.example.scryptan.popoika.Server.Objects.User;
import com.example.scryptan.popoika.Server.Objects.toServer.toExitIvent;
import com.example.scryptan.popoika.Server.Objects.toServer.toFollowIvent;
import com.example.scryptan.popoika.Server.Objects.toServer.toGetMyTeam;
import com.example.scryptan.popoika.Services.UserLocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, SwipeRefreshLayout.OnRefreshListener {

    private GoogleMap mMap;
    private GetIvents getIvents;
    private GetMyTeam getMyTeam;
    private ExitIvent exitIvent;
    private List<Ivent> ivents;
    private Gson gson;
    private GsonBuilder builder;
    private FloatingActionButton teamFAB, plusFAB, exitFAB;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private User user;
    private String userJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ivents = new ArrayList<>();
        //------------------------------------------------------------------------------------------
        teamFAB = (FloatingActionButton) findViewById(R.id.teamFAB);
        plusFAB = (FloatingActionButton) findViewById(R.id.addFAB);
        exitFAB = (FloatingActionButton) findViewById(R.id.exitFAB);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.fab_coordinator_layout) ;
        //------------------------------------------------------------------------------------------
        builder = new GsonBuilder();
        gson = builder.create();
        //------------------------------------------------------------------------------------------
        UserLocationListener.SetUpLocationListener(getApplicationContext());
        exitIvent = ApiUtils.exitIvent();
        getIvents = ApiUtils.getIvents();
        getMyTeam = ApiUtils.getMyTeam();
        //------------------------------------------------------------------------------------------
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //------------------------------------------------------------------------------------------
        Intent intent = getIntent();
        userJSON = intent.getStringExtra("User");
        user = gson.fromJson(userJSON, User.class);
        //------------------------------------------------------------------------------------------
        teamFAB.setVisibility(View.GONE);
        exitFAB.setVisibility(View.GONE);
        //------------------------------------------------------------------------------------------

    }

    public void getMyTeamInIvent(){
        getMyTeam.getMyTeam(new toGetMyTeam(user.id)).enqueue(new Callback<Ivent>() {
            @Override
            public void onResponse(Call<Ivent> call, Response<Ivent> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                }
                if(!(response.body()==null)){
                    Log.e("TAG", "onResponse: "+response.body().description );
                    ivents.add(response.body());
                    plusFAB.setVisibility(View.GONE);
                    teamFAB.setVisibility(View.VISIBLE);
                    exitFAB.setVisibility(View.VISIBLE);
                    setMap();
                    mSwipeRefreshLayout.setRefreshing(false);
                }else {
                    getAllIvents();
                }
            }

            @Override
            public void onFailure(Call<Ivent> call, Throwable t) {

            }
        });
    }

    public void getAllIvents(){
        plusFAB.setVisibility(View.VISIBLE);
        exitFAB.setVisibility(View.GONE);
        getIvents.getIvents().enqueue(new Callback<List<Ivent>>() {
            @Override
            public void onResponse(Call<List<Ivent>> call, Response<List<Ivent>> response) {
                ivents = response.body();
                setMap();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Ivent>> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
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
        Intent intent;
        switch (view.getId()){
            case R.id.addFAB:
                intent = new Intent(MapsActivity.this, AddActivity.class);
                intent.putExtra("User", userJSON);
                startActivity(intent);
                return;
           case R.id.teamFAB:
                intent = new Intent(MapsActivity.this, TeamActivity.class);
                String newIvent = gson.toJson(ivents.get(0));
                intent.putExtra("User", userJSON);
                intent.putExtra("Ivent", newIvent);
                startActivity(intent);
                return;
            case R.id.exitFAB:
                exitFromParty();
                return;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Ivent thisIvent = (Ivent) marker.getTag();
        String iventJSON = gson.toJson(thisIvent);
        Intent intent = new Intent(this,FollowActivity.class);
        intent.putExtra("Ivent",iventJSON);
        intent.putExtra("User", userJSON);
        startActivity(intent);
        return false;
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
        getMyTeamInIvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyTeamInIvent();
    }

    public void exitFromParty(){
        exitIvent.exitIvent(new toExitIvent(user.id)).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    getAllIvents();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}