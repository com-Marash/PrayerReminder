package com.marash.prayerreminder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class setAlerts extends AppCompatActivity {

    private static RadioGroup prayersButGroup;
    private static RadioButton selectedPrayer;
    private String selectedPrayerText;

    private static RadioGroup beforeAfterGroup;
    private static RadioButton selectedBeforeAfter;
    private String selectedBeforeAfterText;

    private EditText timetext;
    private String timeString;

    private Button saveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alerts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setPrayerTime(){

        // in this part, we get the name of prayer we want to set alert for that.
        prayersButGroup = (RadioGroup)findViewById(R.id.prayerGroup);
        int selectedPrayerId = prayersButGroup.getCheckedRadioButtonId();
        selectedPrayer = (RadioButton)findViewById(selectedPrayerId);
        selectedPrayerText = selectedPrayer.getText().toString();

        // in this part, we determine whether time is before or after for the selected prayer.
        beforeAfterGroup = (RadioGroup)findViewById(R.id.beforeAfter);
        int selectedTimeId = beforeAfterGroup.getCheckedRadioButtonId();
        selectedBeforeAfter = (RadioButton)findViewById(selectedTimeId);
        selectedBeforeAfterText = selectedBeforeAfter.getText().toString();

        // here, we get the preferable time from user.
        timetext = (EditText)findViewById(R.id.editText_preferableTime);
        timeString = timetext.getText().toString();


        saveButton = (Button)findViewById(R.id.button_saveAlert);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Alert newAlertSetting = new Alert(selectedBeforeAfterText, selectedPrayerText, timeString);

            }
        });

    }



}
