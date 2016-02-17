package com.marash.prayerreminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainPage extends AppCompatActivity {

    private Button butten_setting;
    private Button butten_aboutUs;
    private Button butten_setAlerts;
    private Button butten_showPrayerTimes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        // call button handlers
        settingFunction();
        aboutUsFunction();
        setAlertFunction();
        showprayerTimesFunction();
    }

    public void settingFunction(){

        butten_setting = (Button)findViewById(R.id.button_setting);
        butten_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingIntent = new Intent("com.marash.prayerreminder.Setting");
                startActivity(settingIntent);
            }
        });
    }

    public void aboutUsFunction(){

        butten_aboutUs = (Button)findViewById(R.id.button_aboutUs);
        butten_aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  aboutUsIntent = new Intent("com.marash.prayerreminder.aboutUs");
                startActivity(aboutUsIntent);
            }
        });
    }

    public void setAlertFunction(){

        butten_setAlerts = (Button)findViewById(R.id.button_setAlerts);
        butten_setAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  setAlertIntent = new Intent("com.marash.prayerreminder.setAlerts");
                startActivity(setAlertIntent);
            }
        });
    }

    public void showprayerTimesFunction(){

        butten_showPrayerTimes = (Button)findViewById(R.id.button_showPrayerTimes);
        butten_showPrayerTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  showPrayerTimesIntent = new Intent("com.marash.prayerreminder.showPrayerTimes");
                startActivity(showPrayerTimesIntent);
            }
        });
    }




}
