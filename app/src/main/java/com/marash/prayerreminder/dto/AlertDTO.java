package com.marash.prayerreminder.dto;

/**
 * Created by Maedeh on 2/22/2016.
 */
public class AlertDTO {

    private final String prayerName;
    private final int time;
    private final int alertNumber;

    public AlertDTO(String prayerName, int time, int alertNumber) {
        this.prayerName = prayerName;
        this.time = time;
        this.alertNumber = alertNumber;
    }

    public int getAlertNumber() {
        return alertNumber;
    }

    public String getPrayerName() {
        return prayerName;
    }

    public int getTime() {
        return time;
    }

}
