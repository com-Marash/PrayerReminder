package com.marash.prayerreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.marash.prayerTimes.main.PrayerTimes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Maedeh on 8/23/2016.
 */
public class AlarmSetter {

    public static void createOrUpdateAllAlarms(Context context){
        // TODO 1- read all alarms in database
        ArrayList<Alert> savedAlerts = StorageManager.loadAlert(context);

        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReciever.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 123, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 2- create/update pendingIntent for each alarm (make it unique by using random number)
        // 3- set alarms in android system

    }

    public static void createOrUpdateAlarm(Alert alert, Context context){
        // 1- create/update pendingIntent for the specific alarm
        // 2- set the alarm in android system

        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReciever.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, alert.getAlertNumber(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar alertCalendar = prayerTimesCalculator.getPrayerTime(alert.getPrayerName(), Calendar.getInstance());


        alertCalendar.add(Calendar.MINUTE,alert.getTime());

        alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,alertCalendar.getTimeInMillis(),alarmIntent);

    }

    public static void deleteAlarm(Double alarmRandomNumber, String alarmType){
        // find the pendingIntent for the specific alarm
        // delete it if it exists
    }





}
