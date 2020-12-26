package com.example.vs1;

public class User {
    private String id;
    private double tem;
    private double oxi;

    public User(String id, double tem, double oxi) {
        this.id = id;
        this.tem = tem;
        this.oxi = oxi;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setTem(double tem) {
        this.tem = tem;
    }

    public void setOxi(double oxi) {
        this.oxi = oxi;
    }
}
