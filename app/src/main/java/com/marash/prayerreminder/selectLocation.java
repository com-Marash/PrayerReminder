package com.marash.prayerreminder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static android.graphics.Color.*;


public class selectLocation extends AppCompatActivity {

    private TextView locationText;
    private LocationBuilder lb;
    private ProgressDialog pd;
    private Button confirmButt;

    private TextWatcher locationTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            //Enabling confirm button
            confirmButt.setEnabled(true);
            confirmButt.getBackground().setColorFilter(null);
            //
            locationText.removeTextChangedListener(this);
            pd.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        confirmButt = (Button)findViewById(R.id.button_confirmLocation);
        //disabling confirm button
        confirmButt.setEnabled(false);
        confirmButt.getBackground().setColorFilter(GRAY, PorterDuff.Mode.MULTIPLY);
        //
        LocationManager myLocManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationText = (TextView) findViewById(R.id.textView_lastKnownLocation);

        pd = new ProgressDialog(selectLocation.this);
        pd.setTitle("Loading Location");
        pd.setMessage("Please wait while the location is being loaded.");
        pd.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                lb.cancelLocationUpdate(selectLocation.this);
                cancelCoordinationTextChangedListener();
                dialog.dismiss();
            }
        });

        lb = new LocationBuilder(myLocManager);

        checkForLastKnownLocation();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void checkForLastKnownLocation(){
        String[] lastKnownLocationText = StorageManager.loadLocation(selectLocation.this);
        String country = "Unknown";
        String city = "Unknown";
        if(lastKnownLocationText[2] == null && lastKnownLocationText[3] == null){
        }else if(lastKnownLocationText[2] == null){
            city = lastKnownLocationText[3];
        }else if(lastKnownLocationText[3] == null){
            country = lastKnownLocationText[2];
        }else{
            country = lastKnownLocationText[2];
            city = lastKnownLocationText[3];
        }
        String longitude = lastKnownLocationText[1];
        String latitude = lastKnownLocationText[0];
        locationText.setText("Country: " + country + "\n" + "City: " + city + "\n"+
                                "Longitude: " + longitude + "\n" + "Latitude: " + latitude);
    }

    private void coordinationTextChangedListener(Context context){
        AlarmSetter.createOrUpdateAllAlarms(context);
        locationText.addTextChangedListener(locationTextWatcher);
    }

    private void cancelCoordinationTextChangedListener(){
        locationText.removeTextChangedListener(locationTextWatcher);
    }

    public void updateLocationByGPSFunction(View view) {
        lb.setLocationListener(selectLocation.this, locationText);
        coordinationTextChangedListener(view.getContext());
        lb.GPS_Function(selectLocation.this);
        pd.show();
    }

    public void updateLocationByNetworkFunction(View view) {
        if (isNetworkAvailable()){
            lb.setLocationListener(selectLocation.this, locationText);
            coordinationTextChangedListener(view.getContext());
            lb.Network_Function(selectLocation.this);
            pd.show();
        }else {
            Toast.makeText(this,"You are offline! Check connection and try again.",Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void confirmButtFunction(View view) {
        Toast.makeText(selectLocation.this, "Location updated successfully.", Toast.LENGTH_LONG).show();
        finish();
    }
}
