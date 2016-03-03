package com.marash.prayerreminder;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class showSavedAlerts extends AppCompatActivity {

    private ArrayList<Alert> savedAlerts;
    private EditText savedAlertsEditText;

    private String savedAlertBeforeAfter;
    private String savedAlertPrayerName;
    private String savedAlertTime;

    ListView prayerListView;
    ArrayAdapter<String> prayerAdapter;

    ArrayList<String> alerts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_saved_alerts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        showSavedAlertsFunction();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void showSavedAlertsFunction(){

        StorageManager myManager = new StorageManager(showSavedAlerts.this.getApplicationContext());
        savedAlerts = myManager.loadAlert();

        //Log.d("maedehArash", savedAlerts);

        alerts = new ArrayList<String>();
        String st;

        for (Alert a : savedAlerts){
            savedAlertPrayerName = a.getPrayerName();
            savedAlertBeforeAfter = a.getBeforeAfter();
            savedAlertTime = a.getTime();
            st = savedAlertTime + " minutes " + savedAlertBeforeAfter + " " + savedAlertPrayerName;
            alerts.add(st);
        }


        prayerListView = (ListView)findViewById(R.id.listView_savedAlertsList);
        customAdaptorFor_showAlertLayout myAdaptor = new customAdaptorFor_showAlertLayout(alerts,this);
        prayerListView.setAdapter(myAdaptor);


//        prayerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,alerts );
//        prayerListView.setAdapter(prayerAdapter);
//        prayerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });




        //savedAlertsEditText = (EditText)findViewById(R.id.editText_savedAlert1);
        //savedAlertsEditText.setText(savedAlerts);

    }

}
