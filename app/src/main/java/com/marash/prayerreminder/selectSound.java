package com.marash.prayerreminder;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;


/**
 * Created by Maedeh on 5/25/2016.
 */
public class selectSound extends AppCompatActivity {

    private TextView sound_TextView;
    private Button ringtoneButton;
    private Uri existingRingtone = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        defaultFunction();
    }

    public void defaultFunction(){
        String[] selectedRingToneData;
        StorageManager stManager = new StorageManager(selectSound.this.getApplicationContext());
        selectedRingToneData = stManager.loadAlarmRingtone();
        //Log.d(alarmRingtone,"maedeh");
        sound_TextView = (TextView)findViewById(R.id.textView_soundText);

        if(selectedRingToneData.equals(null) || selectedRingToneData[0] == null || selectedRingToneData[1] == null){
            sound_TextView.setText("No ringtone has been set for prayers alarm. Please choose from list.");
            existingRingtone = (Uri) null;
        }else{
            sound_TextView.setText(selectedRingToneData[0]+ " has been set as your prayers alarm ringtone.");
            existingRingtone = Uri.parse(selectedRingToneData[1]);
        }

        ringtoneButton = (Button)findViewById(R.id.button_changePrayerAlarm);
        ringtoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound_selection_list();
            }
        });
    }

    public void sound_selection_list() {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select a ringtone as your prayer alarm");
        //Log.d(existingRingtone.toString(), "arash");

        if(existingRingtone==null){
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Settings.System.DEFAULT_RINGTONE_URI);

        }else{
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, existingRingtone);
        }

        //very important
        this.startActivityForResult(intent, 5);
        //very important
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent)
    {

        //very important
        if (resultCode == Activity.RESULT_OK && requestCode == 5) {
            Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

            if (uri != null) {
                existingRingtone = uri;
                //default changed to the selected ringtone.
                //RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM, existingRingtone);
                //////
                Ringtone ringtone = RingtoneManager.getRingtone(selectSound.this.getApplicationContext(), uri);
                String title = ringtone.getTitle(this);

                Toast.makeText(selectSound.this, title + " was set as your prayer alarm.", Toast.LENGTH_LONG).show();

                sound_TextView = (TextView) findViewById(R.id.textView_soundText);
                sound_TextView.setText(title + " has been set as your prayers alarm ringtone.");

                StorageManager stManager = new StorageManager(selectSound.this.getApplicationContext());
                stManager.saveAlarmRingtone(title, uri.toString());


            } else {
                Toast.makeText(selectSound.this, "Please select a ringtone as your prayers alarm from the list.", Toast.LENGTH_LONG).show();
            }
        }


    }


//    public static void storeToneUri(Context context, Uri toneUri) {
//        if(toneUri == null)
//            return;
//
//        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_NAME,
//                Context.MODE_PRIVATE).edit();
//        editor.putString(PROPERTY_NOTIFICATION_SOUND, toneUri.toString());
//        editor.commit();
//    }
//
//    public static Uri getToneUri(Context context) {
//
//        SharedPreferences prefs = context.getSharedPreferences(
//                PREFERENCES_NAME, Context.MODE_PRIVATE);
//
//        Uri toneUri = Settings.System.DEFAULT_NOTIFICATION_URI;
//        String soundString = prefs.getString(PROPERTY_NOTIFICATION_SOUND, null);
//        if(soundString != null) {
//            toneUri = Uri.parse(soundString);
//            if(toneUri == null) {
//                toneUri = Settings.System.DEFAULT_NOTIFICATION_URI;
//            }
//        }
//
//        return toneUri;
//    }


}
