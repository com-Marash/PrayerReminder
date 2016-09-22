package com.marash.prayerreminder;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
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
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Maedeh on 9/20/2016.
 */
public class LocationBuilder extends Activity {

    private String textString = null;
    private LocationManager locm;
    private LocationListener locl;
    String countryName,localityName;

    public String getTextString(){
        return textString;
    }

    LocationBuilder(LocationManager mylocm, final Context context){
        this.locm = mylocm;
        this.locl = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                /*----------to get City-Name from coordinates ------------- */

                Geocoder gcd = new Geocoder(context, Locale.getDefault());
                if (gcd.isPresent()) {
                    List<Address> addresses;
                    try {
                        addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addresses != null && addresses.size() > 0) {

                            Address returnedAddress = addresses.get(0);
                            countryName = returnedAddress.getCountryName() != null ? returnedAddress.getCountryName() : "unknown country";
                            localityName = returnedAddress.getLocality() != null ? returnedAddress.getLocality() : "unknwon city";
                            textString = ("Your current location is: " + countryName + "," + localityName + "\n Your current coordination is:\n" + "Longitude: " + location.getLongitude() + "   Latitude: " + location.getLatitude());
                        } else {
                            textString = ("Your current location is: " + countryName + "," + localityName + "\n Your current coordination is:\n" + "Longitude: " + location.getLongitude() + "   Latitude: " + location.getLatitude());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        textString = ("Your current location is: " + countryName + "," + localityName + "\n Your current coordination is:\n" + "Longitude: " + location.getLongitude() + "   Latitude: " + location.getLatitude());
                    }
                } else {
                    textString = ("Your current location is: " + countryName + "," + localityName + "\n Your current coordination is:\n" + "Longitude: " + location.getLongitude() + "   Latitude: " + location.getLatitude());
                }

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locm.removeUpdates(locl);
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

    public void GPS_Function(Context context){
        if (Build.VERSION.SDK_INT >= 23) {
            Log.d("sara", "GPS_Function: ");
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                Log.d("saba", "GPS_Function: ");
            } else {
                try {
                    locm.requestLocationUpdates("gps", 5000, 2, locl);
                    Log.d("sada", "GPS_Function: ");
                } catch (SecurityException e) {
                }
            }
        } else {
            try {
                locm.requestLocationUpdates("gps", 5000, 2, locl);
            } catch (SecurityException e) {
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        locm.requestLocationUpdates("gps", 5000, 2, locl);
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
