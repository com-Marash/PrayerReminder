package com.marash.prayerreminder;

/**
 * Created by Maedeh on 2/22/2016.
 */
public class Alert{

    private String prayerName;
    private int time;
    private int alertNumber;

    public Alert(String prayerName, int time, int alertNumber){
        this.prayerName = prayerName;
        this.time = time;
        this.alertNumber = alertNumber;
    }

    public void setAlertNumber(int alertNumber){
        this.alertNumber = alertNumber;
    }

    public int getAlertNumber(){
        return alertNumber;
    }

    public String getPrayerName() {
        return prayerName;
    }

    public void setPrayerName(String prayerName) {
        this.prayerName = prayerName;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }



}
