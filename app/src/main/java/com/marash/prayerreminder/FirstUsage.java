package com.marash.prayerreminder;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Maedeh on 8/31/2016.
 */

//this class checks for the first time usage. Right after installation. and tries
    //to set some custom settings by asking the user to select them.

public class FirstUsage extends Activity{

    private LocationListener myLocListener;
    private LocationManager myLocManager;
    private TextView tv;
    private Button okButt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_use_page);

        tv = (TextView)findViewById(R.id.TextView_firstPageCoordination);
        okButt = (Button)findViewById(R.id.firstPageOKButt);

        myLocManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        setLocationChangeListener();
        coordinationTextChanged();
    }

    private void coordinationTextChanged(){
        tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                okButt.setEnabled(true);
            }
        });
    }

    private void setLocationChangeListener() {
        myLocListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                /*----------to get City-Name from coordinates ------------- */

                Geocoder gcd = new Geocoder(FirstUsage.this, Locale.getDefault());
                if(gcd.isPresent()){
                    List<Address> addresses;
                    try {
                        addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addresses != null && addresses.size() > 0) {

                            Address returnedAddress = addresses.get(0);
                            String countryName = returnedAddress.getCountryName() != null ?  returnedAddress.getCountryName():"unknown country" ;
                            String localityName = returnedAddress.getLocality() != null ? returnedAddress.getLocality():"unknwon city";
                            tv.setText("Your current location is: " +countryName+","+localityName  + "\n Your current coordination is:\n" + "Longitude: " + location.getLongitude() + "   Latitude: " + location.getLatitude());
                        }else{
                            tv.setText("Your current coordination is:\n" + "Longitude: " + location.getLongitude() + "   Latitude: " + location.getLatitude());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        tv.setText("Your current coordination is:\n" + "Longitude: " + location.getLongitude() + "   Latitude: " + location.getLatitude());
                    }
                }else{
                    tv.setText("Your current coordination is:\n" + "Longitude: " + location.getLongitude() + "   Latitude: " + location.getLatitude());
                }

                if (ActivityCompat.checkSelfPermission(FirstUsage.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(FirstUsage.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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


    public void openSecondPage(View view) {
        if(okButt.isEnabled()) {
            Intent firstUsageSecondPageIntent = new Intent("com.marash.prayerreminder.FirstUsageSecondPage");
            startActivity(firstUsageSecondPageIntent);
        }
    }

    public void findLocationByGPS(View view) {

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(FirstUsage.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(FirstUsage.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            } else {
                try {
                    myLocManager.requestLocationUpdates("gps", 5000, 2, myLocListener);
                } catch (SecurityException e) {
                }
            }
        } else {
            try {
                myLocManager.requestLocationUpdates("gps", 5000, 2, myLocListener);
            } catch (SecurityException e) {
            }
        }
    }

    public void findLocationByNetwork(View view) {

        if (ActivityCompat.checkSelfPermission(FirstUsage.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(FirstUsage.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        myLocManager.requestLocationUpdates("network", 5000, 2, myLocListener);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        myLocManager.requestLocationUpdates("gps", 5000, 2, myLocListener);
                    } catch (SecurityException e) {
                        //Log.d("security exception", "onClick: ");
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    // todo: Show a message to user that you denied the permission
                }
                return;
            }
        }
    }



}
