package com.marash.prayerreminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainPage extends AppCompatActivity {

    private Button butten_setting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        // call button handlers
        settingFunction();
    }

    public void settingFunction(){

        butten_setting = (Button)findViewById(R.id.button_setting);
        butten_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  settingIntent = new Intent("com.marash.prayerreminder.Setting");
                startActivity(settingIntent);
            }
        });
    }

}
