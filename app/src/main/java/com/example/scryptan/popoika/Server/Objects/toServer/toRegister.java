package com.example.scryptan.popoika.Server.Objects.toServer;

public class toRegister {
    public String telephoneId;
    public String name;
    public String nick;
    public String status;

    public toRegister(String telephoneId, String name, String nick, String status) {
        this.telephoneId = telephoneId;
        this.name = name;
        this.nick = nick;
        this.status = status;
    }

}
