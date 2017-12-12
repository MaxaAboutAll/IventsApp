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
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("surname")
    @Expose
    public String surname;
    @SerializedName("pswd")
    @Expose
    public String pswd;

    public User(String id, String telephoneId, String name, String surname, String pswd) {
        this.id = id;
        this.telephoneId = telephoneId;
        this.name = name;
        this.surname = surname;
        this.pswd = pswd;
    }
}