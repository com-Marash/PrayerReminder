package com.marash.prayerreminder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.Arrays;

public class Setting extends AppCompatActivity {

    private String selection;
    private String selectionValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        selectLocationFunction();
        selectAlarmSoundFunction();
        selectCalculationMethod();

        aboutUsFunction();
        setAlertFunction();
        showSavedAlertsFunction();

        selectPrayersToShowFunction();
    }

    public void selectLocationFunction() {

        Button selectLocationButton = (Button) findViewById(R.id.button_selectLocation);
        selectLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectLocationIntent = new Intent("com.marash.prayerreminder.selectLocation");
                startActivity(selectLocationIntent);
            }
        });
    }

    public void selectAlarmSoundFunction() {

        Button selectAlarmSound = (Button) findViewById(R.id.button_alarmSound);
        selectAlarmSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectSoundIntent = new Intent("com.marash.prayerreminder.selectSound");
                startActivity(selectSoundIntent);
            }
        });
    }

    public void selectCalculationMethod() {
        Button calculationMethodeButton = (Button) findViewById(R.id.Button_calculationMethode);
        calculationMethodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculationMethodFunction().show();
            }
        });
    }

    private AlertDialog calculationMethodFunction() {
        final CharSequence[] methodsItems = {getString(R.string.Egypt), getString(R.string.Tehran), getString(R.string.Jafari), getString(R.string.Karachi),
                getString(R.string.Makkah), getString(R.string.MWL), getString(R.string.Isna)};
        final CharSequence[] methodsItemValues = {"Egypt", "Tehran", "Jafari", "Karachi", "Makkah", "MWL", "ISNA"};
        String savedCalcMethod;

        savedCalcMethod = StorageManager.loadCalculationMethode(Setting.this.getApplicationContext());


        AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
        builder.setTitle(getString(R.string.calculationMethodButton)).setSingleChoiceItems(methodsItems, Arrays.asList(methodsItemValues).indexOf(savedCalcMethod), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int methodNumber) {
                selection = (String) methodsItems[methodNumber];
                selectionValue = (String) methodsItemValues[methodNumber];
            }
        }).setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (selection != null) {
                    Toast.makeText(Setting.this, selection + " " + getString(R.string.calculationMethodSelected), Toast.LENGTH_LONG).show();
                    StorageManager.saveCalculationMethode(selectionValue, Setting.this.getApplicationContext());
                    prayerTimesCalculator.setMethod(selectionValue);
                    AlarmSetter.createOrUpdateAllAlarms(Setting.this);
                }
            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return builder.create();

    }

    private void showSavedAlertsFunction() {

        Button button_showSavedAlerts = (Button) findViewById(R.id.button_showSavedAlerts);
        button_showSavedAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showSavedAlertsIntent = new Intent("com.marash.prayerreminder.showSavedAlerts");
                startActivity(showSavedAlertsIntent);
            }
        });
    }


    public void setAlertFunction() {

        Button butten_setAlerts = (Button) findViewById(R.id.button_setAlerts);
        butten_setAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setAlertIntent = new Intent("com.marash.prayerreminder.setAlerts");
                startActivity(setAlertIntent);
            }
        });
    }

    public void aboutUsFunction() {

        Button butten_aboutUs = (Button) findViewById(R.id.button_aboutUs);
        butten_aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aboutUsIntent = new Intent("com.marash.prayerreminder.aboutUs");
                startActivity(aboutUsIntent);
            }
        });
    }

    public void AlarmVolume(View view) {

        /*
        This part of code creates a dialog which has a seekbar for volume.
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.volume_dialog, (ViewGroup) findViewById(R.id.settingsItem));
        builder.setView(v).setTitle(getString(R.string.alarmVolumeTitle)).setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //setContentView(R.layout.volume_dialog);
        SeekBar seekbarVolume = (SeekBar) v.findViewById(R.id.volumeSeekBar);
        final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        seekbarVolume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM));
        seekbarVolume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_ALARM));

        seekbarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_ALARM, progress, 0);
            }
        });
        builder.create();
        builder.show();
    }

    public void selectPrayersToShowFunction() {

        Button selectLocationButton = findViewById(R.id.button_prayersToShow);
        selectLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prayersToShowFunction().show();
            }
        });
    }

    private AlertDialog prayersToShowFunction() {
        final CharSequence[] methodsItems = {getString(R.string.Imsaak), getString(R.string.Fajr), getString(R.string.Sunrise), getString(R.string.Dhuhr),
                getString(R.string.Asr), getString(R.string.Sunset), getString(R.string.Maghrib), getString(R.string.Isha), getString(R.string.Midnight)};

        final boolean[] savedPrayersToShowBoolean = StorageManager.loadSavedPrayersToShow(Setting.this.getApplicationContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);

        builder.setTitle(getString(R.string.PrayersToShow)).setMultiChoiceItems(methodsItems, savedPrayersToShowBoolean, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                savedPrayersToShowBoolean[indexSelected] = isChecked;
            }
        }).setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                StringBuilder s = new StringBuilder("" + savedPrayersToShowBoolean[0]);
                for (int j = 1; j < savedPrayersToShowBoolean.length; j++) {
                    s.append(",").append(savedPrayersToShowBoolean[j]);
                }
                StorageManager.saveSavedPrayersToShow(s.toString(), Setting.this.getApplicationContext());
                MainPage.refreshPrayerTimes = true;
            }
        });
        return builder.create();
    }

}
