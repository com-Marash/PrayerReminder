package com.marash.prayerreminder;

import android.app.Activity;
import android.media.Ringtone;
import android.media.RingtoneManager;
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
    Ringtone ringtone;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timesup);
        alarmTextView = (TextView) findViewById(R.id.alarmText);
        alarmTextView.setText(textViewString);

        String ringtoneUriString = StorageManager.loadAlarmRingtone(this)[1];
        Uri ringtoneUri = Uri.parse(ringtoneUriString);
        ringtone = RingtoneManager.getRingtone(this, ringtoneUri);
        ringtone.play();
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

        AlarmReciever.wakelock.release();
        ringtone.stop();
        this.finish();
    }
}
