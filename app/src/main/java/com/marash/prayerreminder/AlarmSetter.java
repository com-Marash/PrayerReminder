package com.marash.prayerreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Maedeh on 8/23/2016.
 */
public class AlarmSetter {

    public static void createOrUpdateAllAlarms(Context context){

        ArrayList<Alert> savedAlerts = StorageManager.loadAlert(context);
        for (Alert a:savedAlerts){
            createOrUpdateAlarm(a,context);
        }
    }

    public static void createOrUpdateAlarm(Alert alert, Context context){

        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReciever.class);

        intent.putExtra("prayerName", alert.getPrayerName());
        intent.putExtra("prayerTime", alert.getTime());

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, alert.getAlertNumber(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar alertCalendar = prayerTimesCalculator.getPrayerTime(alert.getPrayerName(), Calendar.getInstance());
        alertCalendar.add(Calendar.MINUTE, alert.getTime());

        //Log.d("createOrUpdateAlarm", alertCalendar.toString());

        if (Build.VERSION.SDK_INT >= 23) {
            alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alertCalendar.getTimeInMillis(), alarmIntent);
        }else{
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, alertCalendar.getTimeInMillis(), alarmIntent);
        }

    }

    public static void deleteAlarm(int alarmRandomNumber, String prayerName, Context context){

        Intent intent = new Intent(context, AlarmReciever.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context,alarmRandomNumber,intent,PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(alarmIntent);
    }
}
