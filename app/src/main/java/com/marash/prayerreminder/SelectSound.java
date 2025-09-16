package com.marash.prayerreminder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.ArrayUtils;
import com.marash.prayerreminder.dto.AlarmRingtoneDTO;
import com.marash.prayerreminder.dto.AlarmRingtoneType;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Maedeh on 5/25/2016.
 */
public class SelectSound extends AppCompatActivity {

    private TextView sound_TextView;
    private Uri existingRingtone = null;
    private String selectedAzanTitle;
    private String selectedAzanValue;

    // for playing selected azan
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setRingtoneText();
    }

    private void setRingtoneText() {

        AlarmRingtoneDTO selectedRingTone = StorageManager.loadAlarmRingtone(SelectSound.this);
        sound_TextView = (TextView) findViewById(R.id.textView_soundText);

        if (selectedRingTone.getType().equals(AlarmRingtoneType.NOT_SET)) {
            sound_TextView.setText(getString(R.string.noRingtone));
            existingRingtone = (Uri) null;
        } else if (selectedRingTone.getType().equals(AlarmRingtoneType.AZAN)) {
            sound_TextView.setText("Azan selected : " + getAzanTitleByAnaznValue(selectedRingTone.getAzanValue()));
        } else if (selectedRingTone.getType().equals(AlarmRingtoneType.RINGTONE)){
            sound_TextView.setText(getString(R.string.musicWithName) + " " + selectedRingTone.getRingtoneTitle() + " " + getString(R.string.ringtoneHasSet));
            existingRingtone = Uri.parse(selectedRingTone.getRingtoneURI());

        }
    }

    private String getAzanTitleByAnaznValue(String azanValue) {
        final CharSequence[] shiaAzanTitles = {getString(R.string.aghati), getString(R.string.moazenzadeh), getString(R.string.rezaeian), getString(R.string.sobhdel), getString(R.string.tantavi), getString(R.string.abuzeid)};
        final CharSequence[] shiaAzanValues = {"shia_aghati", "shia_moazenzadeh", "shia_rezaeian", "shia_sobhdel", "shia_tantavi", "shia_abuzeid"};
        final CharSequence[] sunniAzanTitles = {getString(R.string.abdolbaset), getString(R.string.abdorrahman), getString(R.string.madinah), getString(R.string.makkah), getString(R.string.menshawi), getString(R.string.saeedhafez)};
        final CharSequence[] sunniAzanValues = {"sunni_abdolbaset", "sunni_abdorrahman", "sunni_madinah", "sunni_makkah", "sunni_menshawi", "sunni_saeedhafez"};

        CharSequence[] azanItems = ArrayUtils.concat(sunniAzanTitles, shiaAzanTitles);
        CharSequence[] azanItemValues = ArrayUtils.concat(sunniAzanValues, shiaAzanValues);
        int rowIdIndex = Arrays.asList(azanItemValues).indexOf(azanValue);
        return rowIdIndex != -1 ? (String) azanItems[rowIdIndex] : "";
    }

    public void phoneRingtone_selection_list() {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, getString(R.string.selectANewRingtone));

        if (existingRingtone == null) {
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Settings.System.DEFAULT_RINGTONE_URI);

        } else {
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, existingRingtone);
        }

        //very important
        this.startActivityForResult(intent, 5);
        //very important
    }

    private void open_azan_selection_list() {
        String calculationMethod = StorageManager.loadCalculationMethod(SelectSound.this);
        CharSequence[] azanItems;
        CharSequence[] azanItemValues;

        final CharSequence[] shiaAzantitles = {getString(R.string.aghati), getString(R.string.moazenzadeh), getString(R.string.rezaeian), getString(R.string.sobhdel), getString(R.string.tantavi), getString(R.string.abuzeid)};
        final CharSequence[] shiaAzanValues = {"shia_aghati", "shia_moazenzadeh", "shia_rezaeian", "shia_sobhdel", "shia_tantavi", "shia_abuzeid"};
        final CharSequence[] sunniAzantitles = {getString(R.string.abdolbaset), getString(R.string.abdorrahman), getString(R.string.madinah), getString(R.string.makkah), getString(R.string.menshawi), getString(R.string.saeedhafez)};
        final CharSequence[] sunniAzanValues = {"sunni_abdolbaset", "sunni_abdorrahman", "sunni_madinah", "sunni_makkah", "sunni_menshawi", "sunni_saeedhafez"};

        if (calculationMethod.equals("Tehran") || calculationMethod.equals("Jafari")) {
            azanItems = shiaAzantitles;
            azanItemValues = shiaAzanValues;
        } else if (calculationMethod.equals("Egypt") || calculationMethod.equals("Karachi") || calculationMethod.equals("Makkah") || calculationMethod.equals("MWL")) {
            azanItems = sunniAzantitles;
            azanItemValues = sunniAzanValues;
        } else {
            //this is for ISNA.
            azanItems = ArrayUtils.concat(sunniAzantitles, shiaAzantitles);
            azanItemValues = ArrayUtils.concat(sunniAzanValues, shiaAzanValues);
        }

        AlarmRingtoneDTO savedAzan = StorageManager.loadAlarmRingtone(this.getApplicationContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(SelectSound.this);

        int currentSelectedAzan = -1;
        if (savedAzan.getType().equals(AlarmRingtoneType.AZAN)) {
            currentSelectedAzan = Arrays.asList(azanItemValues).indexOf(savedAzan.getAzanValue());
        }
        builder.setSingleChoiceItems(azanItems, currentSelectedAzan, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int methodNumber) {
                selectedAzanTitle = (String) azanItems[methodNumber];
                selectedAzanValue = (String) azanItemValues[methodNumber];
                int azanID = getResources().getIdentifier(selectedAzanValue, "raw", getPackageName());
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build());
                try {
                    mediaPlayer.setDataSource(SelectSound.this.getApplicationContext(), Uri.parse("android.resource://" + getPackageName() + "/" + azanID));
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    Log.e("TimesUpActivity", "Could not load ringtone url for some reason", e);
                }
                mediaPlayer.start();
            }
        });


        builder.setTitle("Select Azan").setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (selectedAzanTitle != null) {
                    Toast.makeText(SelectSound.this, selectedAzanTitle + " " + "Azan have been selected", Toast.LENGTH_LONG).show();
                    StorageManager.saveAlarmRingtone(selectedAzanValue, SelectSound.this.getApplicationContext());
                    setRingtoneText();
                }
            }
        });

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setOnDismissListener(dialog -> {
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
        });

        builder.create().show();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {

        //very important
        if (resultCode == Activity.RESULT_OK && requestCode == 5) {
            Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

            if (uri != null) {
                existingRingtone = uri;
                //default changed to the selected ringtone.
                //////
                Ringtone ringtone = RingtoneManager.getRingtone(SelectSound.this.getApplicationContext(), uri);
                String title = ringtone.getTitle(this);

                Toast.makeText(SelectSound.this, title + " " + getString(R.string.alarmWasSet), Toast.LENGTH_LONG).show();

                sound_TextView = (TextView) findViewById(R.id.textView_soundText);
                sound_TextView.setText(title + " " + getString(R.string.alarmWasSelected));

                StorageManager.saveAlarmRingtone(new AlarmRingtoneDTO(AlarmRingtoneType.RINGTONE, title, uri.toString(), null), SelectSound.this);
                finish();

            } else {
                Toast.makeText(SelectSound.this, getString(R.string.selectARingtone), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void phoneRingtones(View view) {
        phoneRingtone_selection_list();
    }

    public void azanselections(View view) {
        open_azan_selection_list();
    }

}
