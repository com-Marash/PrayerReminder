package com.marash.prayerreminder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class showSavedAlerts extends AppCompatActivity {

    private TextView showText;

    ListView prayerListView;
    ArrayList<String> alerts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_saved_alerts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        showText = (TextView) findViewById(R.id.textView_savedAlertsText);
        showSavedAlertsFunction();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void showSavedAlertsFunction() {

        ArrayList<Alert> savedAlerts = StorageManager.loadAlert(showSavedAlerts.this.getApplicationContext());

        if (savedAlerts != null && !(savedAlerts.isEmpty())) {
            alerts = new ArrayList<String>();
            String st;

            for (Alert a : savedAlerts) {
                String savedAlertPrayerName = a.getPrayerName();
                int savedAlertTime = a.getTime();
                if (savedAlertTime < 0) {
                    savedAlertTime = -savedAlertTime;
                    st = savedAlertTime + " " + getString(R.string.minutesBefore) + " " + savedAlertPrayerName;

                } else {
                    st = savedAlertTime + " " + getString(R.string.minutesAfter) + " " + savedAlertPrayerName;
                }
                alerts.add(st);
            }

            prayerListView = (ListView) findViewById(R.id.listView_savedAlertsList);
            customAdaptorFor_showAlertLayout myAdaptor = new customAdaptorFor_showAlertLayout(alerts, this);
            prayerListView.setAdapter(myAdaptor);
        } else {
            showText.setText(R.string.noSavedAlarm);
        }
    }
}
