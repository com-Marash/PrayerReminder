package com.marash.prayerreminder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;


public class selectLocation extends AppCompatActivity {

    private LocationManager myLocManager;
    private TextView lastKnownLocationText;
    private LocationListener myLocListener;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myLocManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        lastKnownLocationFunction();
        setLocationChangeListener();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private void setLocationChangeListener(){
        myLocListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lastKnownLocationText.setText("Longtitude is:" + location.getLongitude() + "\n latitude is:" + location.getLatitude());
                if (ActivityCompat.checkSelfPermission(selectLocation.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(selectLocation.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                myLocManager.removeUpdates(myLocListener);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
    }


    private void lastKnownLocationFunction() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            lastKnownLocationText = (TextView) findViewById(R.id.textView_lastKnownLocation);

            Location lastKnownLocation = myLocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (lastKnownLocation == null) {
                lastKnownLocation = myLocManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                if (lastKnownLocation == null) {
                    lastKnownLocation = myLocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            }

            if (lastKnownLocation != null) {
                lastKnownLocationText.setText("Longtitude is:" + lastKnownLocation.getLongitude() + "\n latitude is:" + lastKnownLocation.getLatitude());
                prayerTimesCalculator.setCoordination(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());
            } else {
                lastKnownLocationText.setText("Unknown");
            }
        }
    }

    public void updateLocationByGPSFunction() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(selectLocation.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(selectLocation.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            } else {
                try {
                    myLocManager.requestLocationUpdates("gps", 5000, 2, myLocListener);
                } catch (SecurityException e) {
                    Log.d("security exception", "onClick: ");
                }
            }
        } else {
            try {
                myLocManager.requestLocationUpdates("gps", 5000, 2, myLocListener);
            } catch (SecurityException e) {
                Log.d("security exception", "onClick: ");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        myLocManager.requestLocationUpdates("gps", 5000, 2, myLocListener);
                    } catch (SecurityException e) {
                        Log.d("security exception", "onClick: ");
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    // todo: Show a message to user that you denied the permission
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void updateLocationByNetworkFunction(){

        if (ActivityCompat.checkSelfPermission(selectLocation.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(selectLocation.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        myLocManager.requestLocationUpdates("network", 5000, 2, myLocListener);
    }



    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "selectLocation Page", // TODO: Define a title for the content shown.
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.marash.prayerreminder/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "selectLocation Page", // TODO: Define a title for the content shown.

                Uri.parse("http://host/path"),

                Uri.parse("android-app://com.marash.prayerreminder/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
