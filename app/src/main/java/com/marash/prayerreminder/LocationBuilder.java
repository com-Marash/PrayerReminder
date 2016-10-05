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
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Maedeh on 9/20/2016.
 */
public class LocationBuilder{

    private LocationManager locm;
    private LocationListener locl;

    LocationBuilder(LocationManager mylocm) {
        this.locm = mylocm;
    }


    public LocationListener getLocl(){
        return locl;
    }
    public void setLocationListener(final Context context, final TextView tv){

        this.locl = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                /*----------to get City-Name from coordinates ------------- */
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Log.d("no permission granted", "onLocationChanged: ");
                    // TODO: get permission
                    return;
                }
                Geocoder gcd = new Geocoder(context, Locale.getDefault());
                String localityName = "unknwon city";
                String countryName = "unknown country";
                if (gcd.isPresent()) {
                    List<Address> addresses;
                    try {
                        addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addresses != null && addresses.size() > 0) {

                            Address returnedAddress = addresses.get(0);
                            countryName = returnedAddress.getCountryName() != null ? returnedAddress.getCountryName() : countryName;
                            localityName = returnedAddress.getLocality() != null ? returnedAddress.getLocality() : localityName;
                        }
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                tv.setText("Your current location is: " + countryName + "," + localityName + "\n Your current coordination is:\n" + "Longitude: " + location.getLongitude() + "   Latitude: " + location.getLatitude());
                StorageManager.saveLocation(location.getLongitude(),location.getLatitude(),countryName,localityName,context);
                prayerTimesCalculator.setCoordination(location.getLatitude(),location.getLongitude());

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
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            } else {
                try {
                    locm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locl);
                } catch (SecurityException e) {
                }
            }
        } else {
            try {
                locm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locl);
            } catch (SecurityException e) {
            }
        }
    }

    public void Network_Function(Context context){


        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
            } else {
                try {
                    locm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locl);
                } catch (SecurityException e) {
                }
            }
        } else {
            try {
                locm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locl);
            } catch (SecurityException e) {
            }
        }
    }
}
