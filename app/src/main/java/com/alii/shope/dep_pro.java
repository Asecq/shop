package com.alii.shope;

public class dep_pro {
    private String Descrption , category,image , pdata , ptime , pname , pid , pprice , Pcode ,Pmodel , loaction ,count;

    public dep_pro() {
    }

    public dep_pro(String descrption, String category, String image, String pdata, String ptime, String pname, String pid, String pprice, String pcode, String pmodel, String loaction, String count) {
        Descrption = descrption;
        this.category = category;
        this.image = image;
        this.pdata = pdata;
        this.ptime = ptime;
        this.pname = pname;
        this.pid = pid;
        this.pprice = pprice;
        Pcode = pcode;
        Pmodel = pmodel;
        this.loaction = loaction;
        this.count = count;
    }

    public String getDescrption() {
        return Descrption;
    }

    public void setDescrption(String descrption) {
        Descrption = descrption;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPdata() {
        return pdata;
    }

    public void setPdata(String pdata) {
        this.pdata = pdata;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPprice() {
        return pprice;
    }

    public void setPprice(String pprice) {
        this.pprice = pprice;
    }

    public String getPcode() {
        return Pcode;
    }

    public void setPcode(String pcode) {
        Pcode = pcode;
    }

    public String getPmodel() {
        return Pmodel;
    }

    public void setPmodel(String pmodel) {
        Pmodel = pmodel;
    }

    public String getLoaction() {
        return loaction;
    }

    public void setLoaction(String loaction) {
        this.loaction = loaction;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
