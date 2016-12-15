package com.marash.prayerreminder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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


public class selectLocation extends AppCompatActivity {

    private TextView locationText;
    private LocationBuilder lb;
    private ProgressDialog pd;
    private Button confirmButt,gpsButt,networkButt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        confirmButt = (Button)findViewById(R.id.button_confirmLocation);
        gpsButt = (Button)findViewById(R.id.button_updateLocationGPS);
        networkButt = (Button)findViewById(R.id.updateLocationNetwork);
        LocationManager myLocManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationText = (TextView) findViewById(R.id.textView_lastKnownLocation);

        pd = new ProgressDialog(selectLocation.this);
        pd.setTitle("Loading Location");
        pd.setMessage("Please wait while the application is retrieving your location.");
        pd.setCancelable(false);
        pd.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
        locationText.setText("Your last known location is: \n"+country+", "+city+"\n"+
                                "Last known Coordination is: \n"+"Longitude: "+longitude+", Latitude: "+latitude);
    }

    private void coordinationTextChangedListener(){
        locationText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                confirmButt.setEnabled(true);
                locationText.removeTextChangedListener(this);
                pd.dismiss();
            }
        });
    }


    public void updateLocationByGPSFunction(View view) {
        lb.setLocationListener(selectLocation.this, locationText);
        coordinationTextChangedListener();
        lb.GPS_Function(selectLocation.this);
        gpsButt.setEnabled(false);
        networkButt.setEnabled(false);
        pd.show();
    }

    public void updateLocationByNetworkFunction(View view) {
        if (isNetworkAvailable()){
            lb.setLocationListener(selectLocation.this, locationText);
            coordinationTextChangedListener();
            lb.Network_Function(selectLocation.this);
            gpsButt.setEnabled(false);
            networkButt.setEnabled(false);
            pd.show();
        }else {
            Toast.makeText(this,"You are offline! Please check your connectivity and try again.",Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
