package com.marash.prayerreminder;

import com.marash.prayerTimes.dto.Coordination;
import com.marash.prayerTimes.dto.prayerTimesData;
import com.marash.prayerTimes.dto.prayerTimesDouble;
import com.marash.prayerTimes.main.PrayerTimes;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Maedeh on 8/23/2016.
 */
public class prayerTimesCalculator {
    private static PrayerTimes prayerTimes;
    private static Coordination coordination;

    public static void setPrayerTimes(String method){
        prayerTimes = new PrayerTimes();
        prayerTimes.setMethod(PrayerTimes.methods.valueOf(method));
    }
    public static void setPrayerTimes( double latitude, double longitute){
        coordination = new Coordination(latitude,longitute);
    }

    public static Date getTimes(String prayerType, Calendar calendar){
        prayerTimesData calculatedTimes = null;
        try {
            calculatedTimes = prayerTimes.getTimes(new int[]{calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) +1,calendar.get(Calendar.DAY_OF_MONTH)}, coordination, (double) -5, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // TODO: continue code
        return null
    }

}
