package com.marash.prayerreminder;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Maedeh on 8/30/2016.
 */
public class TimesUpActivity extends Activity {

    private static TextView alarmTextView;
    private static String textViewString;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.timesup);
        alarmTextView = (TextView) findViewById(R.id.alarmText);
        alarmTextView.setText(textViewString);

        String ringtoneUriString = StorageManager.loadAlarmRingtone(this)[1];
        Uri ringtoneUri = Uri.parse(ringtoneUriString);

        mp = MediaPlayer.create(TimesUpActivity.this,ringtoneUri);
        mp.setLooping(true);
        mp.start();
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public static void setAlarmText(int time, String prayerName) {
        String beforeAfter;
        if(time<0){
            beforeAfter = "before";
            time = -time;
        }else{
            beforeAfter = "after";
        }
        textViewString = "Time to pray! It is "+time+" minute "+beforeAfter+ " "+prayerName+".";
    }

    public void stopAlarm(View view) {

        if (AlarmReciever.wakelock.isHeld()){
            AlarmReciever.wakelock.release();
        }
        mp.stop();
        ExitApplication.exitApp(TimesUpActivity.this);
    }
}
