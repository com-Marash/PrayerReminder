package com.marash.prayerreminder;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.marash.prayerreminder.dto.AlarmRingtoneDTO;
import com.marash.prayerreminder.dto.AlarmRingtoneType;

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
        }
        mp.start();

        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= 27) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        }
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
//        if (AlarmReceiver.wakelock != null && AlarmReceiver.wakelock.isHeld()) {
//            AlarmReceiver.wakelock.release();
//        }
        finish();
    }
}
