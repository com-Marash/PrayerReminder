package com.marash.prayerreminder;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Maedeh on 8/31/2016.
 */

//this class checks for the first time usage. Right after installation. and tries
    //to set some custom settings by asking the user to select them.

public class FirstUsage extends Activity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_use_page);

        Log.d("first use", "yayyy: ");
        //when every thing set do this. and return to main page.
        //this.finish();
    }


}
