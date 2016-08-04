package com.marash.prayerreminder;

import android.app.AlertDialog;
import android.app.Dialog;
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

    private Button selectLocationButton;
    private Button SelectAlarmSound;
    private Button calculationMethodeButton;
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

    public AlertDialog calculationMethodFunction() {
        final CharSequence[] methodesItems = {"Qom", "Tehran", "Egypt"};
        String savedCalcMethode;

        final StorageManager stManager = new StorageManager(Setting.this.getApplicationContext());
        savedCalcMethode = stManager.loadCalculationMethode();

        if (savedCalcMethode == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
            builder.setTitle("Choose Calculation Methode").setSingleChoiceItems(methodesItems, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int methodeNumber) {
                    selection = (String) methodesItems[methodeNumber];
                }
            }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(Setting.this, "You have selected " + selection + " as your calculation methode.", Toast.LENGTH_LONG).show();
                    stManager.saveCalculationMethode(selection);

                    //// TODO: 8/4/2016 for arash code!
                }
            }).setNegativeButton("Cancle", new DialogInterface.OnClickListener() {

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
                    Toast.makeText(Setting.this, "You have selected " + selection + " as your calculation methode.", Toast.LENGTH_LONG).show();
                    stManager.saveCalculationMethode(selection);
                    //// TODO: 8/4/2016 for arash code!
                }
            }).setNegativeButton("Cancle", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            return builder.create();
        }
    }
}
