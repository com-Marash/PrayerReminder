package com.marash.prayerreminder;

import android.util.Log;

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

        prayerTimesData calculatesprayerTimes = prayerTimes.getTimes(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), latitude, longitude);

        Calendar result = (Calendar) calendar.clone();
        PrayerTimesDate calculatesprayerTime = null;
        switch (prayerType){
            case "Fajr":
                calculatesprayerTime = calculatesprayerTimes.getFajr();
                break;
            case "Sunrise":
                calculatesprayerTime = calculatesprayerTimes.getSunrise();
                break;
            case "Dhuhr":
                calculatesprayerTime = calculatesprayerTimes.getDhuhr();
                break;
            case "Asr":
                calculatesprayerTime = calculatesprayerTimes.getAsr();
                break;
            case "Sunset":
                calculatesprayerTime = calculatesprayerTimes.getSunset();
                break;
            case "Maghrib":
                calculatesprayerTime = calculatesprayerTimes.getMaghrib();
                break;
            case "Isha":
                calculatesprayerTime = calculatesprayerTimes.getIsha();
                break;
            case "Midnight":
                calculatesprayerTime = calculatesprayerTimes.getMidnight();
                break;
            case "Imsak":
                calculatesprayerTime = calculatesprayerTimes.getImsak();
                break;
            default:
                return null;
        }
        if (calculatesprayerTime != null) {
            result.set(Calendar.HOUR_OF_DAY, calculatesprayerTime.getHour());
            result.set(Calendar.MINUTE, calculatesprayerTime.getMin());
            Log.d("hour",calculatesprayerTime.getHour()+"");
            Log.d("min",calculatesprayerTime.getMin()+"");
            Log.d("lati - longi", latitude + " "+longitude);
        }
        return result;
    }

}
