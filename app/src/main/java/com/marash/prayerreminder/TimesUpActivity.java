package com.marash.prayerreminder;

import android.app.Activity;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Maedeh on 8/30/2016.
 */
public class TimesUpActivity extends Activity {

    private TextView alarmTextView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timesup);
        alarmTextView = (TextView) findViewById(R.id.alarmText);


        String ringtoneUriString = StorageManager.loadAlarmRingtone(this)[1];
        Uri ringtoneUri = Uri.parse(ringtoneUriString);
        Ringtone ringtone = RingtoneManager.getRingtone(this, ringtoneUri);
        ringtone.play();
    }

    public void setAlarmText(String alarmText) {
        alarmTextView.setText(alarmText);
    }

}
