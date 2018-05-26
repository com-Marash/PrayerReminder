package com.marash.prayerreminder.dto;

import android.location.Location;

/**
 * Location class including
 *
 * @author Maedeh.
 */

public class PRLocation {
    public PRLocation(String city, String country, Location location){
        this.city = city;
        this.country = country;
        this.location = location;
    }
    private String city;
    private String country;
    private Location location;

    public Location getLocation() {
        return location;
    }
    public String getCountry() {
        return country;
    }
    public String getCity() {
        return city;
    }
}
