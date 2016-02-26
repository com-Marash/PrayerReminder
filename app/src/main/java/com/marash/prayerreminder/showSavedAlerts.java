package com.marash.prayerreminder;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class showSavedAlerts extends AppCompatActivity {

    private String savedAlerts;
    private EditText savedAlertsEditText;

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

        Log.d("maedehArash", savedAlerts);


        savedAlertsEditText = (EditText)findViewById(R.id.editText_savedAlert1);
        savedAlertsEditText.setText(savedAlerts);

    }

}
