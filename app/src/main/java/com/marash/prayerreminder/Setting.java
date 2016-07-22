package com.marash.prayerreminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class Setting extends AppCompatActivity {

    private Button selectLocationButton;
    private Button SelectAlarmSound;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        selectLocationFunction();
        selectAlarmSoundFunction();
    }



    public void selectLocationFunction(){

        selectLocationButton = (Button)findViewById(R.id.button_selectLocation);
        selectLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectLocationIntent = new Intent("com.marash.prayerreminder.selectLocation");
                startActivity(selectLocationIntent);
            }
        });

    }

    public void selectAlarmSoundFunction(){

        SelectAlarmSound = (Button)findViewById(R.id.button_alarmSound);
        SelectAlarmSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectSoundIntent = new Intent("com.marash.prayerreminder.selectSound");
                startActivity(selectSoundIntent);
            }
        });
    }


}
