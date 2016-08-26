package com.marash.prayerreminder;

import com.marash.prayerTimes.dto.prayerTimesData;
import com.marash.prayerTimes.main.PrayerTimes;

import java.util.Calendar;
import java.util.Date;

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

    public static prayerTimesData getTimes(String prayerType, Calendar calendar){
        return prayerTimes.getTimes(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) +1,calendar.get(Calendar.DAY_OF_MONTH), latitude, longitude);
    }

}
