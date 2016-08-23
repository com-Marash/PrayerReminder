package com.marash.prayerreminder;

import java.util.Date;

/**
 * Created by Maedeh on 8/23/2016.
 */
public class AlarmHandler {

    public static void createOrUpdateAllAlarms(){
        // TODO 1- read all alarms in database



        // 2- create/update pendingIntent for each alarm (make it unique by using random number)
        // 3- set alarms in android system

    }

    public static void createOrUpdateAlarm(Date alarmDate, Double alarmRandomNumber, String alarmType){
        // 1- create/update pendingIntent for the specific alarm
        // 2- set the alarm in android system
    }

    public static void deleteAlarm(Double alarmRandomNumber, String alarmType){
        // find the pendingIntent for the specific alarm
        // delete it if it exists
    }





}
