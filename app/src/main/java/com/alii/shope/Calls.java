package com.alii.shope;

public class Calls  {

    String code , time , note , number , statics , saller , dep , user;

    public Calls() {
    }

    public Calls(String user ,String code, String time, String note, String number , String statics , String saller , String dep) {
        this.code = code;
        this.time = time;
        this.note = note;
        this.number = number;
        this.saller = saller;
        this.statics = statics;
        this.dep = dep;
        this.dep = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public String getSaller() {
        return saller;
    }

    public void setSaller(String saller) {
        this.saller = saller;
    }

    public String getStatics() {
        return statics;
    }

    public void setStatics(String statics) {
        this.statics = statics;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
