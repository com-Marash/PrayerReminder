package com.marash.prayerreminder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.SettableFuture;
import com.marash.prayerreminder.dto.PRLocation;

import static android.graphics.Color.GRAY;

/**
 * First page of application, when user open the  app for first time
 * Created by Maedeh on 8/31/2016.
 */

//this class checks for the first time usage. Right after installation. and tries
//to set some custom settings by asking the user to select them.

public class FirstUsage extends Activity {

    private TextView tv;
    private Button okButt;
    private LocationBuilder lb;
    private PRLocation currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_first_use);

        tv = findViewById(R.id.TextView_firstPageCoordination);
        okButt = findViewById(R.id.firstPageOKButt);
        //disabling confirm button
        okButt.setEnabled(false);
        okButt.getBackground().setColorFilter(GRAY, PorterDuff.Mode.MULTIPLY);

        LocationManager myLocManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //check if location in disabled in user setting
        //TODO

        lb = new LocationBuilder(myLocManager);

        //set a default ringtone for the first time. and we save it.
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        Ringtone ringtone = RingtoneManager.getRingtone(FirstUsage.this.getApplicationContext(), uri);
        String title = ringtone.getTitle(this);
        StorageManager.saveAlarmRingtone(title, uri.toString(), FirstUsage.this.getApplicationContext());

    }

    // ok button onclick event
    public void openSecondPage(View view) {
        if (okButt.isEnabled()) {
            StorageManager.saveLocation(currentLocation.getLocation().getLatitude(), currentLocation.getLocation().getLongitude(), currentLocation.getCountry(), currentLocation.getCity(), FirstUsage.this);
            prayerTimesCalculator.setLatitude(currentLocation.getLocation().getLatitude());
            prayerTimesCalculator.setLongitude(currentLocation.getLocation().getLongitude());
            Intent firstUsageSecondPageIntent = new Intent("com.marash.prayerreminder.FirstUsageSecondPage");
            startActivity(firstUsageSecondPageIntent);
        }
    }

    public void findLocationByGPS(View view) {
        findLocationByGPS(view.getContext());
    }

    public void findLocationByGPS(final Context context) {
        final SettableFuture<PRLocation> locationFuture = lb.getLocationByGPS(FirstUsage.this);
        if (locationFuture != null) {
            handleFuture(locationFuture, context);
        }
    }

    public void findLocationByNetwork(View view) {
        findLocationByNetwork(view.getContext());
    }

    public void findLocationByNetwork(Context context) {
        if (isNetworkAvailable()) {
            SettableFuture<PRLocation> locationFuture = lb.getLocationByNetwork(FirstUsage.this);
            if (locationFuture != null) {
                handleFuture(locationFuture, context);
            }
        } else {
            Toast.makeText(this, getString(R.string.offlineMessage), Toast.LENGTH_LONG).show();
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
                            tv.setText(context.getString(R.string.currentLocation) + " " + prLocation.getCity() + ", " + prLocation.getCountry() + "\n" + context.getString(R.string.CurrentCoordination) + "\n" + context.getString(R.string.longitude) + " " + prLocation.getLocation().getLongitude() + "\n" + context.getString(R.string.latitude) + " " + prLocation.getLocation().getLatitude());
                            okButt.setEnabled(true);
                            okButt.getBackground().setColorFilter(null);
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
        ProgressDialog pd = new ProgressDialog(FirstUsage.this);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        findLocationByGPS(this);
                    } catch (SecurityException e) {
                        Toast.makeText(FirstUsage.this, getString(R.string.cannotUpdateByGPS), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(FirstUsage.this, getString(R.string.GPSPermission), Toast.LENGTH_LONG).show();
                }
                return;
            }
            case 2: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        findLocationByNetwork(this);
                    } catch (SecurityException e) {
                        Toast.makeText(FirstUsage.this, getString(R.string.cannotUpdateByNetwork), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(FirstUsage.this, getString(R.string.networkPermission), Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
