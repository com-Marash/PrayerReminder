package com.marash.prayerreminder;

/**
 * Created by Maedeh on 2/22/2016.
 */
public class Alert{

    private String beforeAfter;
    private String prayerName;
    private String time;
    private int alertNumber;

    public Alert(String prayerName, String beforeAfter, String time, int alertNumber){
        this.beforeAfter = beforeAfter;
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


    public String getBeforeAfter() {
        return beforeAfter;
    }

    public void setBeforeAfter(String beforeAfter) {
        this.beforeAfter = beforeAfter;
    }

    public String getPrayerName() {
        return prayerName;
    }

    public void setPrayerName(String prayerName) {
        this.prayerName = prayerName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



}
