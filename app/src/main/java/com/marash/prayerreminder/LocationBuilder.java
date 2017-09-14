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

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
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

    public Future<Location> getLocationByGPS(final Context context){
        Future<Location> future = new Future<Location>() {
            @Override
            public boolean cancel(boolean b) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public Location get() throws InterruptedException, ExecutionException {
                return null;
            }

            @Override
            public Location get(long l, @NonNull TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
                return null;
            }
        };
        return future;
    }


    public void setLocationListener(final Context context, final TextView tv) {

        this.locl = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Geocoder gcd = new Geocoder(context, Locale.getDefault());
                String localityName = context.getString(R.string.unknownCity);
                String countryName = context.getString(R.string.unknownCountry);
                if (gcd.isPresent()) {
                    List<Address> addresses;
                    try {
                        addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addresses != null && addresses.size() > 0) {

                            Address returnedAddress = addresses.get(0);
                            countryName = returnedAddress.getCountryName() != null ? returnedAddress.getCountryName() : countryName;
                            localityName = returnedAddress.getLocality() != null ? returnedAddress.getLocality() : localityName;
                        }
                    } catch (IOException e) {
                    }
                }
                //TODO crash here!
                tv.setText(context.getString(R.string.currentLocation) + " " + localityName + ", " + countryName + "\n" + context.getString(R.string.CurrentCoordination) + "\n" + context.getString(R.string.longitude) + " " + location.getLongitude() + "\n" + context.getString(R.string.latitude) + " " + location.getLatitude());
                StorageManager.saveLocation(location.getLatitude(), location.getLongitude(), countryName, localityName, context);
                prayerTimesCalculator.setLatitude(location.getLatitude());
                prayerTimesCalculator.setLongitude(location.getLongitude());

                   /*----------to get City-Name from coordinates ------------- */
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
