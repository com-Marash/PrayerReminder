package com.marash.prayerreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Maedeh on 8/31/2016.
 */
public class mainAlarmReciever extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        AlarmSetter.setMainAlarm(context);
        AlarmSetter.createOrUpdateAllAlarms(context);
        this.completeWakefulIntent(intent);
    }
}
