package com.marash.prayerreminder;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by Maedeh on 8/31/2016.
 */
public class mainAlarmReciever extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmSetter.createOrUpdateAllAlarms(context);
        this.completeWakefulIntent(intent);
    }
}
