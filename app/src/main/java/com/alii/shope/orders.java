package com.alii.shope;

import java.util.ArrayList;

public class orders {
    String name , code , user , price, pricepost , img , maintype , dep , num  , statices  , id;
    int count;
    public orders() {
    }

    public orders(String name, String code, String user, String price, String pricepost, String img, String maintype, String dep, String num, String statices, String id, int count) {
        this.name = name;
        this.code = code;
        this.user = user;
        this.price = price;
        this.pricepost = pricepost;
        this.img = img;
        this.maintype = maintype;
        this.dep = dep;
        this.num = num;
        this.statices = statices;
        this.id = id;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatices() {
        return statices;
    }

    public void setStatices(String statices) {
        this.statices = statices;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public String getMaintype() {
        return maintype;
    }

    public void setMaintype(String maintype) {
        this.maintype = maintype;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPricepost() {
        return pricepost;
    }

    public void setPricepost(String pricepost) {
        this.pricepost = pricepost;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
