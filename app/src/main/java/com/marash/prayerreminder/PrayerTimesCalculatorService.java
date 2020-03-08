package com.marash.prayerreminder;

import android.content.Context;
import android.util.Log;

import com.marash.prayerTimes.dto.PrayerTimesDate;
import com.marash.prayerTimes.dto.prayerTimesData;
import com.marash.prayerTimes.main.PrayerTimes;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Maedeh on 8/23/2016.
 */
public class PrayerTimesCalculatorService {

    private static String method;
    private static Double latitude;
    private static Double longitude;

    public static void setLatitude(Double latitude) {
        PrayerTimesCalculatorService.latitude = latitude;
    }

    public static void setLongitude(Double longitude) {
        PrayerTimesCalculatorService.longitude = longitude;
    }

    public static void setMethod(String method) {
        PrayerTimesCalculatorService.method = method;
    }

    public static double[] getLocation(Context context) {
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
        double[] location = getLocation(context);
        prayerTimesData calculatePrayerTimes = prayerTimes.getTimes(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH),
                location[0], location[1], TimeZone.getDefault().getOffset(calendar.getTimeInMillis()) / 3600000d, false);

        Calendar result = (Calendar) calendar.clone();
        PrayerTimesDate calculatesprayerTime = null;
        switch (prayerType) {
            case "Fajr":
                calculatesprayerTime = calculatePrayerTimes.getFajr();
                break;
            case "Sunrise":
                calculatesprayerTime = calculatePrayerTimes.getSunrise();
                break;
            case "Dhuhr":
                calculatesprayerTime = calculatePrayerTimes.getDhuhr();
                break;
            case "Asr":
                calculatesprayerTime = calculatePrayerTimes.getAsr();
                break;
            case "Sunset":
                calculatesprayerTime = calculatePrayerTimes.getSunset();
                break;
            case "Maghrib":
                calculatesprayerTime = calculatePrayerTimes.getMaghrib();
                break;
            case "Isha":
                calculatesprayerTime = calculatePrayerTimes.getIsha();
                break;
            case "Midnight":
                calculatesprayerTime = calculatePrayerTimes.getMidnight();
                break;
            case "Imsaak":
                // TODO: fix this issue after prayerTime module got fixed
                calculatesprayerTime = calculatePrayerTimes.getImsak();
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
