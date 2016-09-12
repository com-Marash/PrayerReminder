package com.marash.prayerreminder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class showSavedAlerts extends AppCompatActivity {

    private ArrayList<Alert> savedAlerts;
    private TextView showText;
    private String savedAlertPrayerName;
    private int savedAlertTime;

    ListView prayerListView;
    ArrayList<String> alerts;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_saved_alerts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        showText = (TextView)findViewById(R.id.textView_savedAlertsText);
        showSavedAlertsFunction();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void showSavedAlertsFunction(){

        savedAlerts = StorageManager.loadAlert(showSavedAlerts.this.getApplicationContext());

        if(!(savedAlerts.isEmpty())) {
            alerts = new ArrayList<String>();
            String st;

            for (Alert a : savedAlerts) {
                savedAlertPrayerName = a.getPrayerName();
                savedAlertTime = a.getTime();
                if (savedAlertTime < 0) {
                    savedAlertTime = -savedAlertTime;
                    st = savedAlertTime + " minutes before " + savedAlertPrayerName;

                } else {
                    st = savedAlertTime + " minutes after " + savedAlertPrayerName;
                }
                alerts.add(st);
            }
            showText.setText("Your saved alerts:");

            prayerListView = (ListView) findViewById(R.id.listView_savedAlertsList);
            customAdaptorFor_showAlertLayout myAdaptor = new customAdaptorFor_showAlertLayout(alerts, this);
            prayerListView.setAdapter(myAdaptor);
        }else {
            Log.d("mamama", "showSavedAlertsFunction: ");
            showText.setText("There is not any saved alerts to show.");
        }
    }

}
