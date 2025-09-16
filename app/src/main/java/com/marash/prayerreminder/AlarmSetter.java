package com.marash.prayerreminder;

import static android.support.v4.content.ContextCompat.startActivity;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.marash.prayerreminder.dto.AlertDTO;

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

        ArrayList<AlertDTO> savedAlerts = StorageManager.loadAlert(context);
        if (savedAlerts != null && !(savedAlerts.isEmpty())) {
            for (AlertDTO a : savedAlerts) {
                createOrUpdateAlarm(a, context);
            }
        }
    }

    public static void createOrUpdateAlarm(AlertDTO alert, Context context) {

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null && !manager.areNotificationsEnabled()) {
                // Ask user to enable notifications
                Intent intent = new Intent(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        .putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, context.getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }

        Calendar alertCalendar = PrayerTimesCalculatorService.getPrayerTime(alert.getPrayerName(), Calendar.getInstance(), context);

        alertCalendar.add(Calendar.MINUTE, alert.getTime());
        if (alertCalendar.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
            Calendar tomorrowCalendar = Calendar.getInstance();
            tomorrowCalendar.add(Calendar.DAY_OF_YEAR, 1);
            alertCalendar = PrayerTimesCalculatorService.getPrayerTime(alert.getPrayerName(), tomorrowCalendar, context);
            alertCalendar.add(Calendar.MINUTE, alert.getTime());
        }

        Intent showAlarmIntent = new Intent(context, AlarmReceiver.class);
        showAlarmIntent.putExtra("prayerName", alert.getPrayerName());
        showAlarmIntent.putExtra("prayerTime", alert.getTime());
        PendingIntent showAlarmPendingIntent = PendingIntent.getBroadcast(context, alert.getAlertNumber(), showAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


        // For system UI (next alarm info)
        // TODO: display next alarm properly instead of mainPage
        Intent showNextAlarmIntent = new Intent(context, MainPage.class);
        PendingIntent showNextAlarmPendingIntent = PendingIntent.getActivity(
                context,
                1,
                showNextAlarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );


        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long timeInMillis = alertCalendar.getTimeInMillis();

        AlarmManager.AlarmClockInfo alarmClockInfo =
                new AlarmManager.AlarmClockInfo(timeInMillis, showNextAlarmPendingIntent);
        if (Build.VERSION.SDK_INT >= 31) {
            if (alarmMgr.canScheduleExactAlarms()) {

                alarmMgr.setAlarmClock(alarmClockInfo, showAlarmPendingIntent);
            } else {
                // TODO: prevent showing success messages in other locations
                Toast.makeText(context, "Cannot set Alarm because of lack of permission", Toast.LENGTH_SHORT).show();
                Log.e("AlarmSetter", "Cannot set Alarm because of lack of permission");
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                context.startActivity(intent);
            }
        } else if (Build.VERSION.SDK_INT >= 26) {
            alarmMgr.setAlarmClock(alarmClockInfo, showAlarmPendingIntent);
        } else {
            alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, showAlarmPendingIntent);
        }
    }

    public static void deleteAlarm(int alarmRandomNumber, Context context) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, alarmRandomNumber, intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(alarmPendingIntent);
        alarmPendingIntent.cancel();
    }

    public static void setMainAlarm(Context context) {
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
    }
}
