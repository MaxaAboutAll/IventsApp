package com.example.scryptan.popoika.Views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.scryptan.popoika.R;
import com.example.scryptan.popoika.Server.ApiUtils;
import com.example.scryptan.popoika.Server.Interfaces.GoogleSignIn;
import com.example.scryptan.popoika.Server.Interfaces.Login;
import com.example.scryptan.popoika.Server.Interfaces.Register;
import com.example.scryptan.popoika.Server.Objects.User;
import com.example.scryptan.popoika.Server.Objects.toServer.toGoogleSignIn;
import com.example.scryptan.popoika.Server.Objects.toServer.toLogin;
import com.example.scryptan.popoika.Server.Objects.toServer.toRegister;
import com.example.scryptan.popoika.Services.DeviceService;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private DeviceService deviceService;
    private GoogleSignIn googleSignIn;
    private Register register;
    private Login login;
    private toLogin toSendLogin;
    private toRegister toSendRegister;
    private toGoogleSignIn toSendGoogleSignIn;
    private String nick, pswd, status, toSend, TAG = "LoginActivity";
    private EditText loginET,pswdET;
    private Gson gson;
    private GsonBuilder builder;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //--------------------------------------------------------
        loginET = (EditText) findViewById(R.id.emailET);
        pswdET = (EditText) findViewById(R.id.pswdET);
        status = "online";
        deviceService = new DeviceService(getContentResolver());
        //---------------------------------------------------------
        register = ApiUtils.register();
        login = ApiUtils.login();
        googleSignIn = ApiUtils.googleSignIn();
        //---------------------------------------------------------
        builder = new GsonBuilder();
        gson = builder.create();
        //---------------------------------------------------------
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.loginBTN:
                goLogin();
                break;
            case R.id.registerBTN:
                goRegister();
                break;
            case R.id.googleLoginBTN:
                googleLogin();
                break;
            default:
                return;
        }
    }

    private void goLogin(){
        nick = loginET.getText().toString();
        pswd = pswdET.getText().toString();
        toSendLogin = new toLogin(nick,pswd,deviceService.getDeviceID(),status);
        login.login(toSendLogin).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    check("500");
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(response.code()==404){
                    check("404");
                    return;
                }
                toSend = gson.toJson(response);
                Intent intent = new Intent(LoginActivity.this,MapsActivity.class);
                intent.putExtra("User",toSend);
                startActivity(intent);
                finish();
                return;
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
    private void goRegister(){
        nick = loginET.getText().toString();
        pswd = pswdET.getText().toString();
        toSendRegister = new toRegister(nick,pswd,deviceService.getDeviceID(),status);
        register.register(toSendRegister).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    check("500");
                    return;
                }
                toSend = gson.toJson(response);
                Intent intent = new Intent(LoginActivity.this,MapsActivity.class);
                intent.putExtra("User",toSend);
                startActivity(intent);
                finish();
                return;
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }

    private void googleLogin(){
        Auth.GoogleSignInApi.signOut(googleApiClient);
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);
    }

    private void handleResult(GoogleSignInResult result){
        GoogleSignInAccount account = result.getSignInAccount();
        String email = account.getEmail();
        toSendGoogleSignIn = new toGoogleSignIn(email,status,deviceService.getDeviceID());
        googleSignIn.googleSignIn(toSendGoogleSignIn).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    check("500");
                    return;
                }
                if(response.code()==404){
                    check("404");
                    return;
                }
                toSend = gson.toJson(response);
                Intent intent = new Intent(LoginActivity.this,MapsActivity.class);
                intent.putExtra("User",toSend);
                startActivity(intent);
                finish();
                return;
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                check("500");
                Log.e(TAG, "onFailure: " );
            }
        });
    }

    private void check(String statusCode){
        Log.e(TAG, "check: inCheck");
        if(statusCode!=null) {
            switch (statusCode) {
                case "404": {
                    Toast.makeText(getApplicationContext(), "Net tebya", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "check: 404");
                    return;
                }
                case "500": {
                    Log.e(TAG, "check: 500");
                    Toast.makeText(getApplicationContext(), "Troubles with server", Toast.LENGTH_SHORT).show();
                    return;
                }
                default:
                    return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
