package com.marash.prayerreminder;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * Created by Maedeh on 8/31/2016.
 */

//this class checks for the first time usage. Right after installation. and tries
    //to set some custom settings by asking the user to select them.

public class FirstUsage extends Activity{

    private LocationManager myLocManager;
    private TextView tv;
    private Button okButt;
    private LocationBuilder lb;
    private String currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_use_page);

        tv = (TextView)findViewById(R.id.TextView_firstPageCoordination);
        okButt = (Button)findViewById(R.id.firstPageOKButt);

        myLocManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        lb = new LocationBuilder(myLocManager);
    }

    private void coordinationTextChangedListener(){
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
                tv.removeTextChangedListener(this);
            }
        });
    }

    public void openSecondPage(View view) {
        if(okButt.isEnabled()) {
            Intent firstUsageSecondPageIntent = new Intent("com.marash.prayerreminder.FirstUsageSecondPage");
            startActivity(firstUsageSecondPageIntent);
        }
    }

    public void findLocationByGPS(View view) {

        lb.setLocationListener(FirstUsage.this, tv);
        coordinationTextChangedListener();
        lb.GPS_Function(FirstUsage.this);


    }
    public void findLocationByNetwork(View view) {

        if (ActivityCompat.checkSelfPermission(FirstUsage.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(FirstUsage.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        //myLocManager.requestLocationUpdates("network", 5000, 2, myLocListener);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        myLocManager.requestLocationUpdates("gps", 1000, 1, lb.getLocl());
                    } catch (SecurityException e) {
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
