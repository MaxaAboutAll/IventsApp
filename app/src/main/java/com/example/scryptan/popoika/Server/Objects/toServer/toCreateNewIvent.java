package com.example.scryptan.popoika.Server.Objects.toServer;

public class toCreateNewIvent {
    String _id;
    String name;
    String description;
    String Latitude;
    String Longitude;
    String stack;
    String amount;

    public toCreateNewIvent(String _id, String name, String description, String latitude, String longitude, String stack, String amount) {
        this._id = _id;
        this.name = name;
        this.description = description;
        Latitude = latitude;
        Longitude = longitude;
        this.stack = stack;
        this.amount = amount;
    }
}
