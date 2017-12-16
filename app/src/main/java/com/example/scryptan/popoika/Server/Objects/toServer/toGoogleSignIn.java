package com.example.scryptan.popoika.Server.Objects.toServer;

public class toGoogleSignIn {
    String nick;
    String status;
    String telephone_id;
    String party;
    String pic;

    public toGoogleSignIn(String nick, String status, String telephone_id, String party, String pic) {
        this.nick = nick;
        this.status = status;
        this.telephone_id = telephone_id;
        this.party = party;
        this.pic = pic;
    }
}
