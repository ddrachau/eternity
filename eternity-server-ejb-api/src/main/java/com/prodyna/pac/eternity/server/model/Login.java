package com.prodyna.pac.eternity.server.model;

/**
 * Created by daniel on 22.04.2015.
 */
public class Login {

    String username;
    String password;
    boolean remember;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }
}
