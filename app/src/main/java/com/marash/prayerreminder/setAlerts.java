package com.marash.prayerreminder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;


public class setAlerts extends AppCompatActivity {

    private RadioGroup prayersButGroup;
    private RadioButton selectedPrayer;
    private String selectedPrayerText;

    private RadioGroup beforeAfterGroup;
    private RadioButton selectedBeforeAfter;
    private String selectedBeforeAfterText;

    private EditText timeText;
    private int desiredTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alerts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setPrayerTime();

    }


    public void setPrayerTime(){

        // in this part, we get the name of prayer we want to set alert for that.
        Button saveButton = (Button)findViewById(R.id.button_saveAlert);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedPrayerText = null;
                selectedBeforeAfterText = null;

                prayersButGroup = (RadioGroup)findViewById(R.id.prayerGroup);
                int selectedPrayerId = prayersButGroup.getCheckedRadioButtonId();

                // in this part, we determine whether time is before or after for the selected prayer.
                beforeAfterGroup = (RadioGroup)findViewById(R.id.beforeAfter);
                int selectedTimeId = beforeAfterGroup.getCheckedRadioButtonId();

                // here, we get the preferable time from user.
                timeText = (EditText)findViewById(R.id.editText_preferableTime);
                desiredTime = Integer.parseInt(String.valueOf(timeText.getText()));

                if((selectedPrayerId == -1) && (selectedTimeId == -1) && (timeText.getText().toString().matches(""))) {
                    Toast.makeText(setAlerts.this, "Please complete all parts", Toast.LENGTH_LONG).show();
                }else if((selectedPrayerId == -1) && (selectedTimeId == -1)){
                    Toast.makeText(setAlerts.this, "Please select one prayer and a time from two option, before or after prayer.", Toast.LENGTH_LONG).show();
                }else if((selectedTimeId == -1) && (timeText.getText().toString().matches(""))){
                    Toast.makeText(setAlerts.this, "Please select a time from two option, before or after prayer and set a time for that.", Toast.LENGTH_LONG).show();
                }else if((selectedPrayerId == -1) && (timeText.getText().toString().matches(""))){
                    Toast.makeText(setAlerts.this, "Please select one prayer and set a time for that.", Toast.LENGTH_LONG).show();
                }else if(selectedPrayerId == -1){
                    Toast.makeText(setAlerts.this, "Please select one prayer", Toast.LENGTH_LONG).show();
                }else if(selectedTimeId == -1){
                    Toast.makeText(setAlerts.this, "Please select a time from two option, before or after prayer.", Toast.LENGTH_LONG).show();
                }else if(timeText.getText().toString().matches("")){
                    Toast.makeText(setAlerts.this, "Please set a time for prayer.", Toast.LENGTH_LONG).show();
                }else{
                    selectedPrayer = (RadioButton)findViewById(selectedPrayerId);
                    selectedPrayerText = selectedPrayer.getText().toString();

                    selectedBeforeAfter = (RadioButton)findViewById(selectedTimeId);
                    selectedBeforeAfterText = selectedBeforeAfter.getText().toString();

                    if(selectedBeforeAfterText.equals("Before")){
                        desiredTime = -desiredTime;
                    }

                    ArrayList<Alert> savedAlerts = StorageManager.loadAlert(setAlerts.this.getApplicationContext());
                    boolean isNewAlert = true;

                    if(savedAlerts != null) {
                        String savedAlertPrayerName;
                        int savedAlertTime;
                        ArrayList<Integer> savedRandNumber = new ArrayList<Integer>();
                        for (Alert a : savedAlerts) {
                            savedAlertPrayerName = a.getPrayerName();
                            savedAlertTime = a.getTime();
                            savedRandNumber.add(a.getAlertNumber());
                            if (selectedPrayerText.equals(savedAlertPrayerName) && desiredTime == savedAlertTime) {
                                isNewAlert = false;
                                break;
                            }
                        }

                        if (isNewAlert) {
                            int randomNumber = (int) ((Math.random() * (10000001) + 1000));
                            while (savedRandNumber.contains(randomNumber)) {
                                randomNumber = (int) ((Math.random() * (10000001) + 1000));
                            }
                            Alert alert = new Alert(selectedPrayerText, desiredTime, randomNumber);
                            StorageManager.saveAlert(alert, setAlerts.this.getApplicationContext());
                            AlarmSetter.createOrUpdateAlarm(alert, setAlerts.this.getApplicationContext());
                            ///
                            AlarmSetter.setMainAlarm(setAlerts.this);
                            Toast.makeText(setAlerts.this, "Your new alert has been successfully saved.", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(setAlerts.this, "This alert has been saved previously.", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        int randomNumber = (int) ((Math.random() * (10000001) + 1000));
                        Alert alert = new Alert(selectedPrayerText, desiredTime, randomNumber);
                        StorageManager.saveAlert(alert, setAlerts.this.getApplicationContext());
                        AlarmSetter.createOrUpdateAlarm(alert, setAlerts.this.getApplicationContext());
                        AlarmSetter.setMainAlarm(setAlerts.this);

                        Toast.makeText(setAlerts.this, "Your new alert has been successfully saved.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }
        });
    }
    public void cancelAlertFunction(View view) {
        finish();
    }
}
