package com.marash.prayerreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Maedeh on 8/23/2016.
 */
public class AlarmSetter {

    private final static int MAIN_ALERT_JOB_ID = 1;

    // This is a utility class (all methods are static), therefor, we do not want instantiate it
    private AlarmSetter() {
    }

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

        alertCalendar.add(Calendar.MINUTE, alert.getTime());
        if (alertCalendar.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
            Calendar tomorrowCalendar = Calendar.getInstance();
            tomorrowCalendar.add(Calendar.DAY_OF_YEAR, 1);
            alertCalendar = prayerTimesCalculator.getPrayerTime(alert.getPrayerName(), tomorrowCalendar, context);
            alertCalendar.add(Calendar.MINUTE, alert.getTime());
        }

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("prayerName", alert.getPrayerName());
        intent.putExtra("prayerTime", alert.getTime());

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, alert.getAlertNumber(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        doSetAlarm(alertCalendar.getTimeInMillis(), alarmIntent, context);
    }

    public static void deleteAlarm(int alarmRandomNumber, Context context) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, alarmRandomNumber, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(alarmIntent);
        alarmIntent.cancel();
    }

    public static void setMainAlarm(Context context) {

        if (Build.VERSION.SDK_INT >= 23) {
            // use jobScheduler for android 6+
            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            // check if job hasBeenScheduled or not
            boolean hasBeenScheduled = false;
            for (JobInfo jobInfo : jobScheduler.getAllPendingJobs()) {
                if (jobInfo.getId() == MAIN_ALERT_JOB_ID) {
                    hasBeenScheduled = true;
                    break;
                }
            }
            // schedule the job if it was not scheduled in passed
            if (!hasBeenScheduled) {
                jobScheduler.schedule(new JobInfo.Builder(MAIN_ALERT_JOB_ID, new ComponentName(context, MainAlarmReceiverService.class))
                        .setPersisted(true)
                        .setPeriodic(1000 * 60 * 60 * 6) // reschedule main alert every 6 hours
                        .build());
            }

        } else {
            Intent intent = new Intent(context, MainAlarmReceiver.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar alertCalendar = Calendar.getInstance();
            alertCalendar.set(Calendar.HOUR_OF_DAY, 10);
            alertCalendar.set(Calendar.MINUTE, 30);
            alertCalendar.set(Calendar.SECOND, 59);

            if (alertCalendar.compareTo(Calendar.getInstance()) < 0) {
                alertCalendar.add(Calendar.DAY_OF_YEAR, 1);
            }
            doSetAlarm(alertCalendar.getTimeInMillis(), alarmIntent, context);
        }
    }

    private static void doSetAlarm(long timeInMillis, PendingIntent alarmIntent, Context context) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= 26) {
            alarmMgr.setAlarmClock(new AlarmManager.AlarmClockInfo(timeInMillis, null), alarmIntent);
        } else if (Build.VERSION.SDK_INT >= 23) {
            alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, alarmIntent);
        } else if (Build.VERSION.SDK_INT >= 19) {
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, alarmIntent);
        } else {
            alarmMgr.set(AlarmManager.RTC_WAKEUP, timeInMillis, alarmIntent);
        }
    }
}
