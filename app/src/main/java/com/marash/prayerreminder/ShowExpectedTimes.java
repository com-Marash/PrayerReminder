package com.marash.prayerreminder;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by Maedeh on 12/9/2016.
 */

public class ShowExpectedTimes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_expected_alerts);

        TextView tv = (TextView) findViewById(R.id.textView_showExpectedAlerts);
        String st = "List of all alerts: \n \n";

        // chcek main alert
        Intent mainAlarmIntent = new Intent("com.marash.prayerreminder.mainAlarmReciever");
        PendingIntent alarmIntent = PendingIntent.getBroadcast(ShowExpectedTimes.this, 1,
                mainAlarmIntent, PendingIntent.FLAG_NO_CREATE);
        if (alarmIntent != null) {
            st += "Main Alert is available" + "\n";
            Log.d("ExpectedAlert", "mainAlert is available");
        }else{
            st += "not main alert" + "\n";
        }

        // check prayer alerts
        ArrayList<Alert> savedAlerts = StorageManager.loadAlert(ShowExpectedTimes.this);
        if (savedAlerts == null || savedAlerts.size() == 0) {
            st += ("not any prayer alert!");
        }else{
            for (Alert alert : savedAlerts) {
                alarmIntent = PendingIntent.getBroadcast(ShowExpectedTimes.this, alert.getAlertNumber(),
                        new Intent("com.marash.prayerreminder.AlarmReciever"), PendingIntent.FLAG_NO_CREATE);
                if (alarmIntent != null) {
                    st += "alarm type: " + alert.getPrayerName() + " , alarm time: " + alert.getTime() + "\n";
                    Log.d("ExpectedAlert", "" + alarmIntent.toString());
                }
            }
        }
        Log.d("Expected Alerts", "" + st);
        tv.setText(st);

    }
}
