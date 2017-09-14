package com.marash.prayerreminder.dto;

import android.content.Context;
import android.location.Location;

import com.marash.prayerreminder.R;

/**
 * description here...
 *
 * @author Maedeh.
 */

public class PRLocation {

    public PRLocation(Context context){
        this.localityName = context.getString(R.string.unknownCity);
        this.countryName = context.getString(R.string.unknownCountry);
    }

    String localityName = context.getString(R.string.unknownCity);
    String countryName = context.getString(R.string.unknownCountry);
    Location location;
}
