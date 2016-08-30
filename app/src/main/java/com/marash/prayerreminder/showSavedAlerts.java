package com.marash.prayerreminder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class showSavedAlerts extends AppCompatActivity {

    private ArrayList<Alert> savedAlerts;

    private String savedAlertPrayerName;
    private int savedAlertTime;

    ListView prayerListView;
    ArrayList<String> alerts;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_saved_alerts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        showSavedAlertsFunction();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void showSavedAlertsFunction(){

        savedAlerts = StorageManager.loadAlert(showSavedAlerts.this.getApplicationContext());

        alerts = new ArrayList<String>();
        String st;

        for (Alert a : savedAlerts){
            savedAlertPrayerName = a.getPrayerName();
            savedAlertTime = a.getTime();
            if(savedAlertTime < 0){
                savedAlertTime = -savedAlertTime;
                st = savedAlertTime + " minutes before " + savedAlertPrayerName;

            }else{
                st = savedAlertTime + " minutes after " + savedAlertPrayerName;
            }
            alerts.add(st);
        }

        prayerListView = (ListView)findViewById(R.id.listView_savedAlertsList);
        customAdaptorFor_showAlertLayout myAdaptor = new customAdaptorFor_showAlertLayout(alerts,this);
        prayerListView.setAdapter(myAdaptor);
    }

}
