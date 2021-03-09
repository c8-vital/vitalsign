package com.example.vs1;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String tem;
    private String oxi;
    private String pul;
    private String time;
    private String number;
    private String name;
    private String staff;


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

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getStaff() {
        return staff;
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

    public void setNumber(String number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }
}
