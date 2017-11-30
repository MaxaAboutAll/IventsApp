package com.example.scryptan.popoika.Server.Objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ivent {
    @SerializedName("_id")
    @Expose
    public String _id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("latitude")
    @Expose
    public String Latitude;
    @SerializedName("longitude")
    @Expose
    public String Longitude;
    @SerializedName("pic")
    @Expose
    public String pic;
    public Ivent(String name, String description, String latitude, String longitude) {
        this.name = name;
        this.description = description;
        Latitude = latitude;
        Longitude = longitude;
    }
}
