package com.example.scryptan.popoika.Server.Objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("_id")
    @Expose
    public String id;
    @SerializedName("nick")
    @Expose
    public String nick;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("party")
    @Expose
    public String party;

    public User(String id, String nick, String status, String party) {
        this.id = id;
        this.nick = nick;
        this.status = status;
        this.party = party;
    }
}