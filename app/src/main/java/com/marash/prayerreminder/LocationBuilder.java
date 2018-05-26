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
import android.widget.Toast;

import com.google.common.util.concurrent.SettableFuture;
import com.marash.prayerreminder.dto.PRLocation;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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

    public SettableFuture<PRLocation> getLocationByGPS(final Context context) {
        SettableFuture<PRLocation> settableFuture = SettableFuture.create();
        setLocationListener(settableFuture, context);
        requestLocationUpdatebyGPS(settableFuture, context);
        return settableFuture;
    }

    public SettableFuture<PRLocation> getLocationByNetwork(final Context context) {
        SettableFuture<PRLocation> settableFuture = SettableFuture.create();
        setLocationListener(settableFuture, context);
        requestLocationUpdateByNetwork(settableFuture, context);
        return settableFuture;
    }

    private void setLocationListener(final SettableFuture<PRLocation> futureToUpdate, final Context context) {
        this.locl = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locm.removeUpdates(locl);
                String city = context.getString(R.string.unknownCity);
                String country = context.getString(R.string.unknownCountry);
                if (Geocoder.isPresent()) {
                    Geocoder gcd = new Geocoder(context, Locale.getDefault());
                    List<Address> addresses;
                    try {
                        addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addresses != null && addresses.size() > 0) {
                            Address returnedAddress = addresses.get(0);
                            if (returnedAddress.getCountryName() != null) {
                                country = returnedAddress.getCountryName();
                            }
                            if (returnedAddress.getLocality() != null) {
                                city = returnedAddress.getLocality();
                            }
                        }
                    } catch (IOException e) {
                        // could not get country and city. It is fine, we show them as "unknown" , continue the code
                    }
                }
                futureToUpdate.set(new PRLocation(city, country, location));
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
        };
    }

    private void requestLocationUpdatebyGPS(SettableFuture<PRLocation> settableFuture, Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                settableFuture.cancel(true);
                cancelLocationUpdate();
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                try {
                    locm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 200, locl);
                } catch (SecurityException e) {
                    settableFuture.cancel(true);
                    cancelLocationUpdate();
                    Toast.makeText(context, context.getString(R.string.cannotUpdateByGPS), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            try {
                locm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 200, locl);
            } catch (SecurityException e) {
                settableFuture.cancel(true);
                cancelLocationUpdate();
                Toast.makeText(context, context.getString(R.string.cannotUpdateByGPS), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void requestLocationUpdateByNetwork(SettableFuture<PRLocation> settableFuture, Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                settableFuture.cancel(true);
                cancelLocationUpdate();
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
            } else {
                try {
                    locm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 200, locl);
                } catch (SecurityException e) {
                    settableFuture.cancel(true);
                    cancelLocationUpdate();
                    Toast.makeText(context, context.getString(R.string.cannotUpdateByNetwork), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            try {
                locm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 200, locl);
            } catch (SecurityException e) {
                settableFuture.cancel(true);
                cancelLocationUpdate();
                Toast.makeText(context, context.getString(R.string.cannotUpdateByNetwork), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void cancelLocationUpdate() {
        locm.removeUpdates(locl);
    }
}
