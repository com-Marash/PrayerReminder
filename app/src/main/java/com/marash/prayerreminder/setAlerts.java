package com.marash.prayerreminder;

        import android.app.Application;
        import android.content.Context;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.RadioButton;
        import android.widget.RadioGroup;
        import android.widget.Toast;


public class setAlerts extends AppCompatActivity {

    private RadioGroup prayersButGroup;
    private RadioButton selectedPrayer;
    private String selectedPrayerText;

    private RadioGroup beforeAfterGroup;
    private RadioButton selectedBeforeAfter;
    private String selectedBeforeAfterText;

    private EditText timetext;
    private String timeString;

    private Button saveButton;
    public boolean saveToStorage;



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


        saveButton = (Button)findViewById(R.id.button_saveAlert);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedPrayerText = null;
                selectedBeforeAfterText = null;
                timeString = null;

                prayersButGroup = (RadioGroup)findViewById(R.id.prayerGroup);
                int selectedPrayerId = prayersButGroup.getCheckedRadioButtonId();

                // in this part, we determine whether time is before or after for the selected prayer.
                beforeAfterGroup = (RadioGroup)findViewById(R.id.beforeAfter);
                int selectedTimeId = beforeAfterGroup.getCheckedRadioButtonId();

                // here, we get the preferable time from user.
                timetext = (EditText)findViewById(R.id.editText_preferableTime);
                timeString = timetext.getText().toString();

                if((selectedPrayerId == -1) && (selectedTimeId == -1) && (timeString.matches(""))) {
                    Toast.makeText(setAlerts.this, "Please complete all parts", Toast.LENGTH_LONG).show();
                }else if((selectedPrayerId == -1) && (selectedTimeId == -1)){
                    Toast.makeText(setAlerts.this, "Please select one prayer and a time from two option, before or after prayer.", Toast.LENGTH_LONG).show();
                }else if((selectedTimeId == -1) && (timeString.matches(""))){
                    Toast.makeText(setAlerts.this, "Please select a time from two option, before or after prayer and set a time for that.", Toast.LENGTH_LONG).show();
                }else if((selectedPrayerId == -1) && (timeString.matches(""))){
                    Toast.makeText(setAlerts.this, "Please select one prayer and set a time for that.", Toast.LENGTH_LONG).show();
                }else if(selectedPrayerId == -1){
                    Toast.makeText(setAlerts.this, "Please select one prayer", Toast.LENGTH_LONG).show();
                }else if(selectedTimeId == -1){
                    Toast.makeText(setAlerts.this, "Please select a time from two option, before or after prayer.", Toast.LENGTH_LONG).show();
                }else if(timeString.matches("")){
                    Toast.makeText(setAlerts.this, "Please set a time for prayer.", Toast.LENGTH_LONG).show();
                }else{
                    selectedPrayer = (RadioButton)findViewById(selectedPrayerId);
                    selectedPrayerText = selectedPrayer.getText().toString();

                    selectedBeforeAfter = (RadioButton)findViewById(selectedTimeId);
                    selectedBeforeAfterText = selectedBeforeAfter.getText().toString();


                    Alert alert = new Alert(selectedPrayerText, selectedBeforeAfterText, timeString);
                    //Log.d("maedeh", selectedBeforeAfterText + "," + selectedPrayerText + "," + timeString);

                    StorageManager stManager = new StorageManager(setAlerts.this.getApplicationContext());
                    saveToStorage = stManager.saveAlert(alert);

                    if (saveToStorage) {
                        Toast.makeText(setAlerts.this, "Your new alert has been successfully saved.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(setAlerts.this, "There is a problem to save your alert. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
                //Log.d("maedeh", selectedBeforeAfterText + "," + selectedPrayerText + "," + timeString);
            }
        });

    }



}
