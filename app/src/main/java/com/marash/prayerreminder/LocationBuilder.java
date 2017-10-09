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
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.marash.prayerreminder.dto.PRLocation;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Maedeh on 9/20/2016.
 */
public class LocationBuilder {

    private LocationManager locm;
    private LocationListener locl;

    LocationBuilder(LocationManager mylocm) {
        this.locm = mylocm;
    }

    public LocationListener getLocl() {
        return locl;
    }

    public Future<PRLocation> getLocationByGPS(final Context context){

    }


    private Future<PRLocation> getLocation(final Context context){
        Future<PRLocation> prLocationFuture= new FutureTask<PRLocation>(new Callable<PRLocation>() {
            @Override
            public PRLocation call() throws Exception {
                return null;
            }
        });

        this.locl = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                PRLocation prLocation = new PRLocation(context);
                prLocation.setLocation(location);
                if (Geocoder.isPresent()) {
                    Geocoder gcd = new Geocoder(context, Locale.getDefault());
                    List<Address> addresses;
                    try {
                        addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addresses != null && addresses.size() > 0) {
                            Address returnedAddress = addresses.get(0);
                            if (returnedAddress.getCountryName() != null) {
                                prLocation.setCountry(returnedAddress.getCountryName());
                            }
                            if (returnedAddress.getLocality() != null){
                                prLocation.setCity(returnedAddress.getLocality());
                            }
                        }
                    } catch (IOException e) {
                        // could not get country and city. It is fine, we show them as "unknown" , continue the code
                    }
                }
                StorageManager.saveLocation(location.getLatitude(), location.getLongitude(), prLocation.getCountry(), prLocation.getCity(), context);
                prayerTimesCalculator.setLatitude(location.getLatitude());
                prayerTimesCalculator.setLongitude(location.getLongitude());

                   /*----------to get City-Name from coordinates ------------- */
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locm.removeUpdates(locl);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        }


    }

    public void GPS_Function(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            } else {
                try {
                    locm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 2000, locl);
                } catch (SecurityException e) {

                }
            }
        } else {
            try {
                locm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 2000, locl);
            } catch (SecurityException e) {

            }
        }
    }

    public void Network_Function(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
            } else {
                try {
                    locm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 2000, locl);
                } catch (SecurityException e) {

                }
            }
        } else {
            try {
                locm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 2000, locl);
            } catch (SecurityException e) {

            }
        }
    }

    public void cancelLocationUpdate(Context context) {
           /*----------to get City-Name from coordinates ------------- */
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locm.removeUpdates(locl);
    }
}
