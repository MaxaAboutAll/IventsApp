package com.example.scryptan.popoika.Server.Objects.toServer;

public class toLogin {

    public String telephoneId;
    public String name;
    public String nick;
    public String status;

    public toLogin(String telephoneId, String name, String nick, String status) {
        this.telephoneId = telephoneId;
        this.name = name;
        this.nick = nick;
        this.status = status;
    }
}
