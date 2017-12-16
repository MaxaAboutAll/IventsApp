package com.example.scryptan.popoika.Views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.example.scryptan.popoika.Services.UserLocationListener;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private UiLifecycleHelper uiHelper;
    private DeviceService deviceService;
    private GoogleSignIn googleSignIn;
    private Register register;
    private Login login;
    private toLogin toSendLogin;
    private toRegister toSendRegister;
    private toGoogleSignIn toSendGoogleSignIn;
    private String  status, toSend, TAG = "LoginActivity", vkName;
    private Gson gson;
    private GsonBuilder builder;
    private GoogleApiClient googleApiClient;
    private int REQ_CODE = 0;
    private VKRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //------------------------------------------------------------------------------------------
        uiHelper = new UiLifecycleHelper(this, new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {

            }
        });
        uiHelper.onCreate(savedInstanceState);
        UserLocationListener.SetUpLocationListener(getApplicationContext());
        //--------------------------------------------------------
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

    public void clickMe(View v){
        switch (v.getId()){
            case R.id.googleSignIn:
                REQ_CODE = 9001;
                googleLogin();
                break;
            case R.id.vkSignIn:
                REQ_CODE = 9002;
                vkLogin();
                break;
            case R.id.fbSignIn:
                Toast.makeText(getApplicationContext(),"Coming soon",Toast.LENGTH_SHORT).show();
                break;
            default:
                return;
        }
    }

    private void vkLogin(){
        VKSdk.login(this);
    }

    private void googleLogin(){
        Auth.GoogleSignInApi.signOut(googleApiClient);
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);
    }

    private void handleResult(GoogleSignInResult result){
        GoogleSignInAccount account = result.getSignInAccount();
        String name = account.getDisplayName();
        String pic = account.getPhotoUrl().toString();
        toSendGoogleSignIn = new toGoogleSignIn(name,status,deviceService.getDeviceID(),"", pic);
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
        if(9001 == REQ_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
            return;
        }
        if(9002 == REQ_CODE){
            vkAuth(requestCode, resultCode, data);
            return;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void vkAuth(int requestCode, int resultCode, Intent data){
        VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "first_name, last_name"));
                VKRequest secRequest = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "photo_200"));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        VKApiUser user = ((VKList<VKApiUser>) response.parsedModel).get(0);
                        vkName = user.first_name+" "+user.last_name;
                    }

                    @Override
                    public void onError(VKError error) {
                        Log.e(TAG, "onResult: -----------"+error.toString()+"----------------------------" );
                    }
                });
                secRequest.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        VKApiUser user = ((VKList<VKApiUser>) response.parsedModel).get(0);
                        String urlImage = user.photo_200;
                        Log.e(TAG, "onResult: -----------"+vkName+" ----------------------------" );
                        Log.e(TAG, "onResult: -----------"+urlImage+" ----------------------------" );
                        toSendGoogleSignIn = new toGoogleSignIn(vkName,status,deviceService.getDeviceID(),"",urlImage);
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

                            }
                        });
                    }

                    @Override
                    public void onError(VKError error) {
                    }

                });

            }
            @Override
            public void onError(VKError error) {

            }
        });
    }

    private void goLogin(String name, String nick){
        toSendLogin = new toLogin(deviceService.getDeviceID(),name,nick,status);
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
    private void goRegister(String name, String nick){
        toSendRegister = new toRegister(deviceService.getDeviceID(),name,nick,status);
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
}
