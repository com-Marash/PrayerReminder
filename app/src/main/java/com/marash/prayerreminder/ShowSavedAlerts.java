package com.marash.prayerreminder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import com.marash.prayerreminder.dto.AlertDTO;

import java.util.ArrayList;

public class ShowSavedAlerts extends AppCompatActivity {

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

        ArrayList<AlertDTO> savedAlerts = StorageManager.loadAlert(ShowSavedAlerts.this.getApplicationContext());

        if (savedAlerts != null && !(savedAlerts.isEmpty())) {
            alerts = new ArrayList<String>();
            String st;

            for (AlertDTO a : savedAlerts) {
                String savedAlertPrayerName = a.getPrayerName();
                int savedAlertTime = a.getTime();
                if (savedAlertTime < 0) {
                    savedAlertTime = -savedAlertTime;
                    st = savedAlertTime + " " + getString(R.string.minutesBefore) + " " + getString(savedAlertPrayerName);

                } else {
                    st = savedAlertTime + " " + getString(R.string.minutesAfter) + " " + getString(savedAlertPrayerName);
                }
                alerts.add(st);
            }

            prayerListView = (ListView) findViewById(R.id.listView_savedAlertsList);
            CustomAdaptorForShowAlertLayout myAdaptor = new CustomAdaptorForShowAlertLayout(alerts, this);
            prayerListView.setAdapter(myAdaptor);
        } else {
            showText.setText(R.string.noSavedAlarm);
        }
    }

    public String getString(String abc){

        int resID = getResources().getIdentifier(abc, "string",  getPackageName());

        return this.getResources().getString(resID);
    }
}
