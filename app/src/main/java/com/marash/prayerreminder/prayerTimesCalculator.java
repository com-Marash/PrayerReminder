package com.marash.prayerreminder;

import android.content.Context;
import android.util.Log;

import com.marash.prayerTimes.dto.PrayerTimesDate;
import com.marash.prayerTimes.dto.prayerTimesData;
import com.marash.prayerTimes.main.PrayerTimes;

import java.util.Calendar;

/**
 * Created by Maedeh on 8/23/2016.
 */
public class prayerTimesCalculator {

    private static String method;
    private static Double latitude;
    private static Double longitude;

    public static void setLatitude(Double latitude) {
        prayerTimesCalculator.latitude = latitude;
    }

    public static void setLongitude(Double longitude) {
        prayerTimesCalculator.longitude = longitude;
    }

    public static void setMethod(String method) {
        prayerTimesCalculator.method = method;
    }

    public static double[] getLccation(Context context) {
        if (latitude == null || longitude == null) {
            String[] location = StorageManager.loadLocation(context);
            latitude = Double.valueOf(location[0]);
            longitude = Double.valueOf(location[1]);
        }
        return new double[]{latitude, longitude};
    }

    public static String getMethod(Context context) {
        if (method == null) {
            method = StorageManager.loadCalculationMethode(context);
        }
        return method;
    }

    public static Calendar getPrayerTime(String prayerType, Calendar calendar, Context context) {

        PrayerTimes prayerTimes = new PrayerTimes(PrayerTimes.methods.valueOf(getMethod(context)));
        double[] location = getLccation(context);
        prayerTimesData calculatesprayerTimes = prayerTimes.getTimes(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), location[0], location[1]);

        Calendar result = (Calendar) calendar.clone();
        PrayerTimesDate calculatesprayerTime = null;
        switch (prayerType) {
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
            Log.d("hour", calculatesprayerTime.getHour() + "");
            Log.d("min", calculatesprayerTime.getMin() + "");
            Log.d("lati - longi", latitude + " " + longitude);
        }
        return result;
    }

}
