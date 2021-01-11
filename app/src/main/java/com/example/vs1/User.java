package com.example.vs1;

public class User {
    private String id;
    private String tem;
    private String oxi;
    private String pul;
    private String time;

    public User(String id, String tem, String oxi, String pul, String time) {
        this.id = id;
        this.tem = tem;
        this.oxi = oxi;
        this.pul = pul;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public String getTem() {
        return tem;
    }

    public String getOxi() {
        return oxi;
    }

    public String getPul() {
        return pul;
    }

    public String getTime() {
        return time;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTem(String tem) {
        this.tem = tem;
    }

    public void setOxi(String oxi) {
        this.oxi = oxi;
    }

    public void setPul(String pul) {
        this.pul = pul;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
