package com.example.scryptan.popoika.Server.Objects.toServer;

public class toLogin {
    String nick;
    String pswd;
    String telephone_id;
    String status;

    public toLogin(String nick, String pswd, String telephone_id, String status) {
        this.nick = nick;
        this.pswd = pswd;
        this.telephone_id = telephone_id;
        this.status = status;
    }
}