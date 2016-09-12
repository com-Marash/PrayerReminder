package com.marash.prayerreminder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;

public class Setting extends AppCompatActivity {

    private Button selectLocationButton,SelectAlarmSound,calculationMethodeButton,
                    butten_aboutUs,butten_setAlerts,button_showSavedAlerts;
    private String selection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        selectLocationFunction();
        selectAlarmSoundFunction();
        selectCalculationMethode();


        aboutUsFunction();
        setAlertFunction();
        showSavedAlertsFunction();

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

    public void selectCalculationMethode(){
        calculationMethodeButton = (Button)findViewById(R.id.Button_calculationMethode);
        calculationMethodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculationMethodFunction().show();
            }
        });
    }

    private AlertDialog calculationMethodFunction() {
        final CharSequence[] methodesItems = {"ISNA", "MWL", "Makkah", "Karachi", "Jafari", "Tehran", "Egypt"};
        String savedCalcMethode;

        savedCalcMethode = StorageManager.loadCalculationMethode(Setting.this.getApplicationContext());

        if (savedCalcMethode == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
            builder.setTitle("Choose Calculation Methode").setSingleChoiceItems(methodesItems, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int methodeNumber) {
                    selection = (String) methodesItems[methodeNumber];
                }
            }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(Setting.this, "You have selected " + selection + " as your calculation methode.", Toast.LENGTH_LONG).show();
                    StorageManager.saveCalculationMethode(selection, Setting.this.getApplicationContext());
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            return builder.create();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
            builder.setTitle("Choose Calculation Methode").setSingleChoiceItems(methodesItems, Arrays.asList(methodesItems).indexOf(savedCalcMethode), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int methodeNumber) {
                    selection = (String) methodesItems[methodeNumber];
                }
            }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(selection == null){
                        Toast.makeText(Setting.this, "Please select a method from the list.", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(Setting.this, "You have selected " + selection + " as your calculation methode.", Toast.LENGTH_LONG).show();
                        StorageManager.saveCalculationMethode(selection, Setting.this.getApplicationContext());
                    }
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            return builder.create();
        }
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
}
