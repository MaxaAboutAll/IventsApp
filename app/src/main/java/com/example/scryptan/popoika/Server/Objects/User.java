package com.example.scryptan.popoika.Server.Objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("_id")
    @Expose
    public String id;
    @SerializedName("telephone_id")
    @Expose
    public String telephoneId;
    @SerializedName("nick")
    @Expose
    public String nick;
    @SerializedName("pswd")
    @Expose
    public String pswd;

    public User(String id, String telephoneId, String nick, String pswd) {
        this.id = id;
        this.telephoneId = telephoneId;
        this.nick = nick;
        this.pswd = pswd;
    }
}