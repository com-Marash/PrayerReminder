package com.marash.prayerreminder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.marash.prayerreminder.dto.AlarmRingtoneDTO;
import com.marash.prayerreminder.dto.AlarmRingtoneType;

import java.io.IOException;

public class AlarmService extends Service {

    public static final String ALARM_CHANNEL_ID = "AlarmChannel";

    private MediaPlayer mp;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent stopIntent = new Intent(this, AlarmService.class);
        stopIntent.setAction("STOP_ALARM");
        PendingIntent stopPendingIntent = PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, ALARM_CHANNEL_ID)
                .setContentTitle("Prayer Reminder")
                .setContentText("Your alarm is ringing")
                .setSmallIcon(R.drawable.ic_add_alarm_icon)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(R.drawable.ic_cancel_alarm, "Stop", stopPendingIntent)
                .setDeleteIntent(stopPendingIntent)
                .setCategory(Notification.CATEGORY_ALARM)
                .build();

        // Start foreground service with notification
        startForeground(1, notification);

        // Check for stop action
        if (intent != null && "STOP_ALARM".equals(intent.getAction())) {
            stopAlarm();
            return START_NOT_STICKY;
        }

        // PLaying Alarm:
        AlarmRingtoneDTO ringtoneDTO = StorageManager.loadAlarmRingtone(this);

        Uri ringtoneOrAzanUri;
        mp = new MediaPlayer();
        mp.setAudioAttributes(new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());

        if (ringtoneDTO.getType().equals(AlarmRingtoneType.AZAN)) {
            // it is azan not ringtone
            int azanID = getResources().getIdentifier(ringtoneDTO.getAzanValue(), "raw", getPackageName());
            if (azanID == 0) {
                Log.d("TimesUpActivity", "Azan resource not found, defaulting to moazenzadeh.");
                azanID = R.raw.shia_moazenzadeh;
            }
            ringtoneOrAzanUri = Uri.parse("android.resource://" + getPackageName() + "/" + azanID);
        } else {
            // It is ringtone
            if (ringtoneDTO.getType().equals(AlarmRingtoneType.RINGTONE)) {
                ringtoneOrAzanUri = Uri.parse(ringtoneDTO.getRingtoneURI());
            } else {
                ringtoneOrAzanUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
            mp.setLooping(true);

        }
        try {
            mp.setDataSource(this, ringtoneOrAzanUri);
            mp.prepare();
        } catch (IOException e) {
            Log.e("TimesUpActivity", "Could not load ringtone url for some reason", e);
            stopSelf();
            return START_NOT_STICKY;
        }
        mp.start();


        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(
                    ALARM_CHANNEL_ID,
                    "Alarm Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Channel for prayer alarms");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
    private void stopAlarm() {
        if (mp != null && mp.isPlaying()) {
            mp.stop();
            mp.release();
            mp = null;
        }
        stopForeground(true);
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAlarm();
    }
}