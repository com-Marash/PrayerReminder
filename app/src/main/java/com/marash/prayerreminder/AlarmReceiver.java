package com.marash.prayerreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String prayerName = intent.getStringExtra("prayerName");
        int prayerTime = intent.getIntExtra("prayerTime", 0);

        Intent serviceIntent = new Intent(context, AlarmService.class);
        serviceIntent.putExtra("prayerName", prayerName);
        serviceIntent.putExtra("prayerTime", prayerTime);

        ContextCompat.startForegroundService(context, serviceIntent);
    }
}