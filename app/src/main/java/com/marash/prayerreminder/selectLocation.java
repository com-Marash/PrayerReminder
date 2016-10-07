package com.marash.prayerreminder;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class selectLocation extends AppCompatActivity {

    private LocationManager myLocManager;
    private TextView locationText;
    private LocationBuilder lb;
    private String[] lastKnownLocationText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myLocManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationText = (TextView) findViewById(R.id.textView_lastKnownLocation);

        lb = new LocationBuilder(myLocManager);

        checkForLastKnownLocation();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void checkForLastKnownLocation(){
        lastKnownLocationText = StorageManager.loadLocation(selectLocation.this);
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
        String longitude = lastKnownLocationText[0];
        String latitude = lastKnownLocationText[1];
        locationText.setText("Your last known location is: \n"+"Country name: "+country+" City name: "+city+"\n"+
                                "Your last known Coordination is: \n"+"Longitude: "+longitude+"Latitude: "+latitude);
    }

    public void updateLocationByGPSFunction(View view) {
        lb.setLocationListener(selectLocation.this, locationText);
        lb.GPS_Function(selectLocation.this);
    }

    public void updateLocationByNetworkFunction(View view) {
        if (isNetworkAvailable()){
            lb.setLocationListener(selectLocation.this, locationText);
            lb.Network_Function(selectLocation.this);
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
