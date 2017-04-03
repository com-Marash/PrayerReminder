package com.marash.prayerreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

/**
 * Created by Maedeh on 8/23/2016.
 */
public class AlarmReciever extends BroadcastReceiver {

    public static PowerManager.WakeLock wakelock;

    @Override
    public void onReceive(Context context, Intent intent) {

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();
        wakelock = wl;

        Intent timesUpIntent = new Intent(context, TimesUpActivity.class);
        timesUpIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        TimesUpActivity.setAlarmText(intent.getIntExtra("prayerTime", -1), intent.getStringExtra("prayerName"),context);
        Log.d("recieved this alarm: ", String.valueOf(intent.getIntExtra("prayerTime", -1)));
        Log.d("recieved this alarm: ", intent.getStringExtra("prayerName"));
        context.startActivity(timesUpIntent);
    }
}
