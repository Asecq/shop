package com.alii.shope.Model;

public class Users {
    private String  username,pass,phone , type;

    public Users() {
    }

    public Users(String username, String pass, String phone , String type) {
        this.username = username;
        this.pass = pass;
        this.phone = phone;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
