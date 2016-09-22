package com.marash.prayerreminder;

import com.marash.prayerTimes.dto.PrayerTimesDate;
import com.marash.prayerTimes.dto.prayerTimesData;
import com.marash.prayerTimes.main.PrayerTimes;
import java.util.Calendar;

/**
 * Created by Maedeh on 8/23/2016.
 */
public class prayerTimesCalculator {
    private static PrayerTimes prayerTimes;
    private static double latitude;
    private static double longitude;

    public static void setPrayerTimes(String method){
        prayerTimes = new PrayerTimes(PrayerTimes.methods.valueOf(method));
    }
    public static void setCoordination( double lat, double lng){
        latitude = lat;
        longitude = lng;
    }

    public static Calendar getPrayerTime(String prayerType, Calendar calendar){

        //TODO longtitude,lattitude
        prayerTimesData calculatedPrayerTimes = prayerTimes.getTimes(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), 81, 42);

        Calendar result = (Calendar) calendar.clone();
        PrayerTimesDate calculatedPrayerTime = null;
        switch (prayerType){
            case "Fajr":
                calculatedPrayerTime = calculatedPrayerTimes.getFajr();
                break;
            case "Sunrise":
                calculatedPrayerTime = calculatedPrayerTimes.getSunrise();
                break;
            case "Dhuhr":
                calculatedPrayerTime = calculatedPrayerTimes.getDhuhr();
                break;
            case "Asr":
                calculatedPrayerTime = calculatedPrayerTimes.getAsr();
                break;
            case "Sunset":
                calculatedPrayerTime = calculatedPrayerTimes.getSunset();
                break;
            case "Maghrib":
                calculatedPrayerTime = calculatedPrayerTimes.getMaghrib();
                break;
            case "Isha":
                calculatedPrayerTime = calculatedPrayerTimes.getIsha();
                break;
            case "Midnight":
                calculatedPrayerTime = calculatedPrayerTimes.getMidnight();
                break;
            case "Imsak":
                calculatedPrayerTime = calculatedPrayerTimes.getImsak();
                break;
            default:
                return null;
        }
        if (calculatedPrayerTime != null) {
            result.set(Calendar.HOUR, calculatedPrayerTime.getHour());
            result.set(Calendar.MINUTE, calculatedPrayerTime.getHour());
        }

        return result;
    }

}
