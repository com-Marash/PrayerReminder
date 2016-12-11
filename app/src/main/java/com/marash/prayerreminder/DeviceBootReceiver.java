package com.marash.prayerreminder;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by Maedeh on 11/28/2016.
 */

public class DeviceBootReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
            AlarmSetter.createOrUpdateAllAlarms(context);
            this.completeWakefulIntent(intent);
        }

    }
}
