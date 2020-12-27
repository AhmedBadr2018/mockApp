package com.badr.mockapp;

public class DataItem {

    private double RSRP;
    private double RSRQ;
    private double SINR;
    private String time;

    public DataItem() {}

    public DataItem(double RSRP, double RSRQ, double SINR, String time) {
        this.RSRP = RSRP;
        this.RSRQ = RSRQ;
        this.SINR = SINR;
        this.time = time;
    }

    public double getRSRP() {
        return RSRP;
    }

    public void setRSRP(double RSRP) {
        this.RSRP = RSRP;
    }

    public double getRSRQ() {
        return RSRQ;
    }

    public void setRSRQ(double RSRQ) {
        this.RSRQ = RSRQ;
    }

    public double getSINR() {
        return SINR;
    }

    public void setSINR(double SINR) {
        this.SINR = SINR;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
