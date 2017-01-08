package com.marash.prayerreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.ContentValues.TAG;

/**
 * Created by Maedeh on 8/23/2016.
 */
public class AlarmSetter {

    public static void createOrUpdateAllAlarms(Context context) {

        ArrayList<Alert> savedAlerts = StorageManager.loadAlert(context);
        if (savedAlerts != null && !(savedAlerts.isEmpty())) {
            for (Alert a : savedAlerts) {
                createOrUpdateAlarm(a, context);
            }
        }
    }

    public static void createOrUpdateAlarm(Alert alert, Context context) {

        Calendar alertCalendar = prayerTimesCalculator.getPrayerTime(alert.getPrayerName(), Calendar.getInstance(), context);

        Log.d("minute1", alertCalendar.get(Calendar.MINUTE) + " " + alertCalendar.get(Calendar.HOUR_OF_DAY));

        alertCalendar.add(Calendar.MINUTE, alert.getTime());
        if (alertCalendar.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
            Calendar tomorrowCalendar = Calendar.getInstance();
            tomorrowCalendar.add(Calendar.DAY_OF_YEAR, 1);
            alertCalendar = prayerTimesCalculator.getPrayerTime(alert.getPrayerName(), tomorrowCalendar, context);
            alertCalendar.add(Calendar.MINUTE, alert.getTime());
        }

        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("com.marash.prayerreminder.AlarmReciever");
        intent.putExtra("prayerName", alert.getPrayerName());
        intent.putExtra("prayerTime", alert.getTime());

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, alert.getAlertNumber(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= 23) {
            alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alertCalendar.getTimeInMillis(), alarmIntent);
        } else {
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, alertCalendar.getTimeInMillis(), alarmIntent);
        }
    }

    public static void deleteAlarm(int alarmRandomNumber, Context context) {
        Intent intent = new Intent("com.marash.prayerreminder.AlarmReciever");
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, alarmRandomNumber, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(alarmIntent);
        alarmIntent.cancel();
    }

    public static void setMainAlarm(Context context) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("com.marash.prayerreminder.mainAlarmReciever");
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar alertCalendar = Calendar.getInstance();
        alertCalendar.set(Calendar.HOUR_OF_DAY, 10);
        alertCalendar.set(Calendar.MINUTE, 30);
        alertCalendar.set(Calendar.SECOND, 59);

        if (alertCalendar.compareTo(Calendar.getInstance()) < 0) {
            alertCalendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        Log.d(TAG, "setMainAlarm: " + alertCalendar.toString());
        if (Build.VERSION.SDK_INT >= 23) {
            alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alertCalendar.getTimeInMillis(), alarmIntent);
        } else {
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, alertCalendar.getTimeInMillis(), alarmIntent);
        }
    }
}
