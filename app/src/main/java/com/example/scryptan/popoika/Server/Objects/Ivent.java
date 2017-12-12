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
    @SerializedName("stack")
    @Expose
    public String stack;
    @SerializedName("amount")
    @Expose
    public String amount;

    public Ivent(String _id, String name, String description, String latitude, String longitude, String pic, String stack, String amount) {
        this._id = _id;
        this.name = name;
        this.description = description;
        Latitude = latitude;
        Longitude = longitude;
        this.pic = pic;
        this.stack = stack;
        this.amount = amount;
    }
}
