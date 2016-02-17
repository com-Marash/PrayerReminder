package com.marash.prayerreminder;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;


public class selectLocation extends AppCompatActivity {

    private LocationManager myLocManager;
    private TextView lastKnownLocationText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setLocationFunction();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void setLocationFunction() {

        lastKnownLocationText = (TextView)findViewById(R.id.textView_lastKnownLocation);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET}, 10); // any number you can write.
            return;
        }else {
            Location lastKnownLocation = myLocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            lastKnownLocationText.setText("Longtitude is:" + lastKnownLocation.getLongitude() + "latitude is:" + lastKnownLocation.getLatitude());
        }
    }

}
