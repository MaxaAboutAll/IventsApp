package com.example.scryptan.popoika.Server;


import com.example.scryptan.popoika.Server.Interfaces.ExitIvent;
import com.example.scryptan.popoika.Server.Interfaces.FollowIvent;
import com.example.scryptan.popoika.Server.Interfaces.GetIvents;
import com.example.scryptan.popoika.Server.Interfaces.GetMyPartyFriends;
import com.example.scryptan.popoika.Server.Interfaces.GetMyTeam;
import com.example.scryptan.popoika.Server.Interfaces.GetUser;
import com.example.scryptan.popoika.Server.Interfaces.GoogleSignIn;
import com.example.scryptan.popoika.Server.Interfaces.Login;
import com.example.scryptan.popoika.Server.Interfaces.Register;
import com.example.scryptan.popoika.Server.Interfaces.UploadImage;

public class ApiUtils {
    public static final String BASE_URl = "http://192.168.1.40:3000/";

    public static Login login(){
        return RetrofitClient.getClient(BASE_URl).create(Login.class);
    }
    public static Register register(){
        return RetrofitClient.getClient(BASE_URl).create(Register.class);
    }
    public static GoogleSignIn googleSignIn() {
        return RetrofitClient.getClient(BASE_URl).create(GoogleSignIn.class);
    }
    public static UploadImage uploadImage(){
        return RetrofitClient.getClient(BASE_URl).create(UploadImage.class);
    }
    public static GetIvents getIvents(){
        return RetrofitClient.getClient(BASE_URl).create(GetIvents.class);
    }
    public static GetMyPartyFriends getMyPartyFriends(){
        return RetrofitClient.getClient(BASE_URl).create(GetMyPartyFriends.class);
    }
    public static GetMyTeam getMyTeam(){
        return RetrofitClient.getClient(BASE_URl).create(GetMyTeam.class);
    }
    public static GetUser getUser(){
        return RetrofitClient.getClient(BASE_URl).create(GetUser.class);
    }
    public static FollowIvent followIvent(){
        return RetrofitClient.getClient(BASE_URl).create(FollowIvent.class);
    }
    public static ExitIvent exitIvent(){
        return RetrofitClient.getClient(BASE_URl).create(ExitIvent.class);
    }
}
