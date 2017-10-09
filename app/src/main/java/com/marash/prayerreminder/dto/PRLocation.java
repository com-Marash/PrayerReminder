package com.marash.prayerreminder.dto;

import android.content.Context;
import android.location.Location;

import com.marash.prayerreminder.R;

/**
 * Location class including
 *
 * @author Maedeh.
 */

public class PRLocation {
    public PRLocation(Context context){
        this.city = context.getString(R.string.unknownCity);
        this.country = context.getString(R.string.unknownCountry);
    }
    private String city;
    private String country;
    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
