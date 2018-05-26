package com.marash.prayerreminder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.SettableFuture;
import com.marash.prayerreminder.dto.PRLocation;

import static android.graphics.Color.GRAY;


public class selectLocation extends AppCompatActivity {

    private TextView locationText;
    private LocationBuilder lb;
    private Button confirmButt;
    private PRLocation currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        confirmButt = (Button) findViewById(R.id.button_confirmLocation);
        //disabling confirm button
        confirmButt.setEnabled(false);
        confirmButt.getBackground().setColorFilter(GRAY, PorterDuff.Mode.MULTIPLY);
        //
        LocationManager myLocManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationText = (TextView) findViewById(R.id.textView_lastKnownLocation);

        lb = new LocationBuilder(myLocManager);

        checkForLastKnownLocation();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void checkForLastKnownLocation() {
        String[] lastKnownLocationText = StorageManager.loadLocation(selectLocation.this);
        String country = this.getString(R.string.unknownCountry);
        String city = this.getString(R.string.unknownCity);
        // lastKnownLocationText null point exception fixed
        // TODO: support string.xml files
        if (lastKnownLocationText != null) {
            if (lastKnownLocationText[2] == null && lastKnownLocationText[3] == null) {
            } else if (lastKnownLocationText[2] == null) {
                city = lastKnownLocationText[3];
            } else if (lastKnownLocationText[3] == null) {
                country = lastKnownLocationText[2];
            } else {
                country = lastKnownLocationText[2];
                city = lastKnownLocationText[3];
            }
            String longitude = lastKnownLocationText[1];
            String latitude = lastKnownLocationText[0];
            locationText.setText(getString(R.string.country) + ": " + country + "\n" + getString(R.string.city) + ": " + city + "\n" +
                    getString(R.string.longitude) + " " + longitude + "\n" + getString(R.string.latitude) + " " + latitude);
        } else {
            locationText.setText(getString(R.string.country) + ": " + country + "\n" + getString(R.string.city) + ": " + city + "\n" +
                    getString(R.string.longitude) + " " + "Unknown longitude" + "\n" + getString(R.string.latitude) + " " + "Unknown latitude");
        }

    }

    public void updateLocationByGPSFunction(View view) {
        final LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(manager != null) {
            if (isGPSAvailable(manager)) {
                final Context context = view.getContext();
                final SettableFuture<PRLocation> locationFuture = lb.getLocationByGPS(context);
                if (locationFuture != null) {
                    handleFuture(locationFuture, context);
                }
            } else {
                Toast.makeText(this, getString(R.string.NoGPSMessage), Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, getString(R.string.LocationIsNotEnabled), Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkProviderAvailable(LocationManager manager) {
        return manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean isGPSAvailable(LocationManager manager) {
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void updateLocationByNetworkFunction(View view) {
        Context context = view.getContext();
        final LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(manager != null && isNetworkProviderAvailable(manager)) {
            if (isNetworkAvailable()) {
                SettableFuture<PRLocation> locationFuture = lb.getLocationByNetwork(context);
                if (locationFuture != null) {
                    handleFuture(locationFuture, context);
                }
            } else {
                Toast.makeText(this, getString(R.string.offlineMessage), Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, getString(R.string.LocationIsNotEnabled), Toast.LENGTH_LONG).show();
        }
    }

    private void handleFuture(final SettableFuture<PRLocation> locationFuture, final Context context) {
        final ProgressDialog pd = createPD(locationFuture);
        pd.show();
        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    final PRLocation prLocation = locationFuture.get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            locationText.setText(context.getString(R.string.currentLocation) + " " + prLocation.getCity() + ", " + prLocation.getCountry() + "\n" + context.getString(R.string.CurrentCoordination) + "\n" + context.getString(R.string.longitude) + " " + prLocation.getLocation().getLongitude() + "\n" + context.getString(R.string.latitude) + " " + prLocation.getLocation().getLatitude());
                            confirmButt.setEnabled(true);
                            confirmButt.getBackground().setColorFilter(null);
                        }
                    });
                    currentLocation = prLocation;
                } catch (Exception e) {
                    e.printStackTrace();
                    // future to get location got canceled. nothing to do.
                } finally {
                    pd.dismiss();
                }
            }
        };
        mThread.start();
    }

    private ProgressDialog createPD(final SettableFuture<PRLocation> locationFuture) {
        ProgressDialog pd = new ProgressDialog(selectLocation.this);
        pd.setTitle(getString(R.string.loadLocation));
        pd.setMessage(getString(R.string.waitLocation));
        pd.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                lb.cancelLocationUpdate();
                locationFuture.cancel(true);
                dialog.dismiss();
            }
        });
        return pd;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetworkInfo != null && activeNetworkInfo.isConnected());
    }

    public void confirmButtFunction(View view) {
        Toast.makeText(selectLocation.this, getString(R.string.locationUpdated), Toast.LENGTH_LONG).show();
        StorageManager.saveLocation(currentLocation.getLocation().getLatitude(), currentLocation.getLocation().getLongitude(), currentLocation.getCountry(), currentLocation.getCity(), selectLocation.this);
        prayerTimesCalculator.setLatitude(currentLocation.getLocation().getLatitude());
        prayerTimesCalculator.setLongitude(currentLocation.getLocation().getLongitude());
        AlarmSetter.createOrUpdateAllAlarms(view.getContext());
        finish();
    }
}
