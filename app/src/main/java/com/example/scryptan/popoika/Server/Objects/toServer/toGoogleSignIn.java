package com.example.scryptan.popoika.Server.Objects.toServer;

public class toGoogleSignIn {
    String nick;
    String status;
    String telephone_id;
    String party;

    public toGoogleSignIn(String nick, String status, String telephone_id, String party) {
        this.nick = nick;
        this.status = status;
        this.telephone_id = telephone_id;
        this.party = party;
    }
}
