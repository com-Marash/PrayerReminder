package com.marash.prayerreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

public class MainPage extends AppCompatActivity {

    private Button butten_setting;
    private Button butten_aboutUs;
    private Button butten_setAlerts;
    private Button butten_showPrayerTimes;
    private Button button_showSavedAlerts;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        setMainAlarm();

        //FirstUse Handler
        SharedPreferences prefs = getSharedPreferences("FirstUsePreferences", MODE_PRIVATE);
        boolean isFirstUsage = prefs.getBoolean("first_usage", true);

        if(isFirstUsage){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("first_usage", false);
            editor.commit();

            Intent firstUsageIntent = new Intent("com.marash.prayerreminder.FirstUsage");
            startActivity(firstUsageIntent);

        }
        //


        // call button handlers
        settingFunction();
        aboutUsFunction();
        setAlertFunction();
        showprayerTimesFunction();
        showSavedAlertsFunction();
    }

    private void setMainAlarm() {
        AlarmManager alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, mainAlarmReciever.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar alertCalendar = Calendar.getInstance();
        alertCalendar.set(Calendar.HOUR,23);
        alertCalendar.set(Calendar.MINUTE,59);
        alertCalendar.set(Calendar.SECOND,59);

        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, alertCalendar.getTimeInMillis(), 24*3600*1000 ,alarmIntent);

    }

    private void showSavedAlertsFunction() {

        button_showSavedAlerts = (Button)findViewById(R.id.button_showSavedAlerts);
        button_showSavedAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showSavedAlertsIntent = new Intent("com.marash.prayerreminder.showSavedAlerts");
                startActivity(showSavedAlertsIntent);
            }
        });
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
                Intent setAlertIntent = new Intent("com.marash.prayerreminder.setAlerts");
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
