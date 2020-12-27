package com.example.vs1;

public class User {
    private String id;
    private double tem;
    private double oxi;
    private double pul;
    private String time;

    public User(String id, double tem, double oxi, double pul, String time) {
        this.id = id;
        this.tem = tem;
        this.oxi = oxi;
        this.pul = pul;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public double getTem() {
        return tem;
    }

    public double getOxi() {
        return oxi;
    }

    public double getPul() {
        return pul;
    }

    public String getTime() {
        return time;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTem(double tem) {
        this.tem = tem;
    }

    public void setOxi(double oxi) {
        this.oxi = oxi;
    }

    public void setPul(double pul) {
        this.pul = pul;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
