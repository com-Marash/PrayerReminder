package com.marash.prayerreminder;

import android.app.Activity;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Maedeh on 5/25/2016.
 */
public class selectSound extends AppCompatActivity {

    private TextView sound_TextView;
    private String alarmRingtone;
    private Button ringtoneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        defaultFunction();
    }

    public void defaultFunction(){
        StorageManager stManager = new StorageManager(selectSound.this.getApplicationContext());
        alarmRingtone = stManager.loadAlarmRingtone();
        sound_TextView = (TextView)findViewById(R.id.textView_soundText);

        if(alarmRingtone.equals(null)){
            sound_TextView.setText("No ringtone has been set for prayers alarm. Please choose from list.");
        }else{
            sound_TextView.setText(alarmRingtone+ " has been set as your prayers alarm ringtone.");
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
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
        this.startActivityForResult(intent, 5);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent)
    {
        if (resultCode == Activity.RESULT_OK && requestCode == 5)
        {
            Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

            if (uri != null)
            {


                Ringtone ringtone = RingtoneManager.getRingtone(selectSound.this.getApplicationContext(), uri);
                String title = ringtone.getTitle(this);

                Toast.makeText(selectSound.this, title + "was set as your prayer alarm.", Toast.LENGTH_LONG).show();

                sound_TextView = (TextView)findViewById(R.id.textView_soundText);
                sound_TextView.setText(title + " has been set as your prayers alarm ringtone.");

                StorageManager stManager = new StorageManager(selectSound.this.getApplicationContext());
                stManager.saveAlarmRingtone(title);

            }
            else
            {
                Toast.makeText(selectSound.this, "Please select a ringtone as your prayers alarm from the list.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
