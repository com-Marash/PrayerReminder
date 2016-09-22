package com.marash.prayerreminder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.common.api.GoogleApiClient;


public class selectLocation extends AppCompatActivity {

    private LocationManager myLocManager;
    private TextView locationText;
    private LocationListener myLocListener;
    private LocationBuilder lb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myLocManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationText = (TextView) findViewById(R.id.textView_lastKnownLocation);

        //lastKnownLocationFunction();


        lb = new LocationBuilder(myLocManager,selectLocation.this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    public void updateLocationByGPSFunction(View view) {

        lb.GPS_Function(selectLocation.this);
        locationText.setText(lb.getTextString());
    }


    public void updateLocationByNetworkFunction(View view){

        if (ActivityCompat.checkSelfPermission(selectLocation.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(selectLocation.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        myLocManager.requestLocationUpdates("network", 5000, 2, myLocListener);
    }
}
