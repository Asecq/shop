package com.alii.shope;

public class categorys {

    String Name, key;

    public categorys() {
    }

    public categorys(String name, String key) {
        Name = name;
        this.key = key;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
