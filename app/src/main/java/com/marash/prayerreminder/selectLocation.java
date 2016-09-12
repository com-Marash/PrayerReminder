package com.marash.prayerreminder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;


public class selectLocation extends AppCompatActivity {

    private LocationManager myLocManager;
    private TextView lastKnownLocationText;
    private LocationListener myLocListener;
    private Button updateByGPS, updateByNetwork;

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
        updateLocationByGPSFunction();
        updateLocationByNetworkFunction();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    public void lastKnownLocationFunction() {

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

        updateByGPS = (Button) findViewById(R.id.button_updateLocationGPS);
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

        updateByGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(selectLocation.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(selectLocation.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                myLocManager.requestLocationUpdates("gps", 5000, 2, myLocListener);
            }
        });
    }


    public void updateLocationByNetworkFunction(){

        updateByNetwork = (Button) findViewById(R.id.updateLocationNetwork);
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

        updateByNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(selectLocation.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(selectLocation.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                myLocManager.requestLocationUpdates("network", 5000, 2, myLocListener);
            }
        });

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
