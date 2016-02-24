package com.marash.prayerreminder;

/**
 * Created by Maedeh on 2/22/2016.
 */
public class Alert{

    private String beforeAfter;
    private String prayerName;
    private String time;

    public Alert(String beforeAfter, String prayerName, String time){
        this.beforeAfter = beforeAfter;
        this.prayerName = prayerName;
        this.time = time;
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
