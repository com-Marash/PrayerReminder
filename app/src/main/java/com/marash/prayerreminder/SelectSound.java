package com.marash.prayerreminder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.ArrayUtils;

import java.util.Arrays;

/**
 * Created by Maedeh on 5/25/2016.
 */
public class SelectSound extends AppCompatActivity {

    private TextView sound_TextView;
    private Uri existingRingtone = null;
    private String azanSelections;
    private String azanSelectionsValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        defaultFunction();
    }

    public void defaultFunction() {
        String[] selectedRingToneData;
        selectedRingToneData = StorageManager.loadAlarmRingtone(SelectSound.this);
        sound_TextView = (TextView) findViewById(R.id.textView_soundText);

        if (selectedRingToneData == null) {
            sound_TextView.setText(getString(R.string.noRingtone));
            existingRingtone = (Uri) null;
        } else {
            if(selectedRingToneData[1] == null){
                // azan rington is selecred
                sound_TextView.setText("TODO: Azan selected : " + selectedRingToneData[0]);
            }else{
                sound_TextView.setText(getString(R.string.musicWithName) + " " + selectedRingToneData[0] + " " + getString(R.string.ringtoneHasSet));
                existingRingtone = Uri.parse(selectedRingToneData[1]);
            }
        }
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
        final CharSequence[] shiaAzanItems = {getString(R.string.aghati), getString(R.string.moazenzadeh), getString(R.string.rezaeian), getString(R.string.sobhdel), getString(R.string.tantavi), getString(R.string.abuzeid)};
        final CharSequence[] shiaAzanValues = {"aghati","moazenzadeh","rezaeian","sobhdel","tantavi","abuzeid" };
        final CharSequence[] sunniAzanItems = {getString(R.string.abdolbaset), getString(R.string.abdorrahman), getString(R.string.madinah), getString(R.string.makkah), getString(R.string.menshawi), getString(R.string.saeedhafez)};
        final CharSequence[] sunniAzanValues = {"abdolbaset","abdorrahman","madinah","makkah","menshawi","saeedhafez"};

        if(calculationMethod.equals("Tehran") || calculationMethod.equals("Jafari")){
            azanItems = shiaAzanItems;
            azanItemValues = shiaAzanValues;
        }else if(calculationMethod.equals("Egypt") || calculationMethod.equals("Karachi") || calculationMethod.equals("Makkah") || calculationMethod.equals("MWL")){
            azanItems = sunniAzanItems;
            azanItemValues = sunniAzanValues;
        }else{
            //this is for ISNA.
            azanItems = ArrayUtils.concat(sunniAzanItems, shiaAzanItems);
            azanItemValues = ArrayUtils.concat(sunniAzanValues , shiaAzanValues);
        }

        String[] savedAzan;
        savedAzan = StorageManager.loadAlarmRingtone(this.getApplicationContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(SelectSound.this);

        if(savedAzan != null && savedAzan[1] == null){
            builder.setSingleChoiceItems(azanItems, Arrays.asList(azanItemValues).indexOf(savedAzan[0]), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int methodNumber) {
                    azanSelections = (String) azanItems[methodNumber];
                    azanSelectionsValue = (String) azanItemValues[methodNumber];
                }
            });
        }

        builder.setTitle("TODO: Select Azan title").setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (azanSelections != null) {
                    Toast.makeText(SelectSound.this, azanSelections + " " + "TODO: Azan have been selecred", Toast.LENGTH_LONG).show();
                    StorageManager.saveAlarmRingtone(azanSelectionsValue, SelectSound.this.getApplicationContext());
                }
            }
        });

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // TODO: play the selected azan

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

                StorageManager.saveAlarmRingtone(title, uri.toString(), SelectSound.this);
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
