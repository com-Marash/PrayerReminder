package com.marash.prayerreminder;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by Maedeh on 8/30/2016.
 */
public class TimesUpActivity extends Activity {

    private TextView alarmTextView;
    private static String textViewString;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.timesup);
        alarmTextView = (TextView) findViewById(R.id.alarmText);
        alarmTextView.setText(textViewString);
        blink();

        String[] ringtoneTitleAndUri = StorageManager.loadAlarmRingtone(this);
        Uri ringtoneUri;

        if (ringtoneTitleAndUri != null && ringtoneTitleAndUri.length > 1){
            String ringtoneUriString = ringtoneTitleAndUri[1];
            ringtoneUri = Uri.parse(ringtoneUriString);
        }else{
            ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        }

        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_ALARM);
        mp.setLooping(true);
        try {
            mp.setDataSource(this, ringtoneUri);
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.start();

        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    private void blink() {
        Animation anim = new AlphaAnimation(0.6f, 1.0f);
        anim.setDuration(1000); //You can manage the blinking time with this parameter
        anim.setStartOffset(10);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        alarmTextView.startAnimation(anim);
    }

    public static void setAlarmText(int time, String prayerName, Context context) {
        String beforeAfter;
        if (time < 0) {
            beforeAfter = context.getString(R.string.Before);
            time = -time;
        } else {
            beforeAfter = context.getString(R.string.After);
        }
        textViewString = context.getString(R.string.timeToPray) + " " + time + " " + context.getString(R.string.Minutes) + " " + beforeAfter + " " + prayerName + ".";
    }

    public void stopAlarm(View view) {
        mp.stop();
        if (AlarmReceiver.wakelock != null && AlarmReceiver.wakelock.isHeld()) {
            AlarmReceiver.wakelock.release();
        }
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }
}
