package com.marash.prayerreminder.dto;

import android.location.Location;

/**
 * Location class including
 *
 * @author Maedeh.
 */

public class PRLocationDTO {

    private final String city;
    private final String country;
    private final Location location;
    public PRLocationDTO(String city, String country, Location location){
        this.city = city;
        this.country = country;
        this.location = location;
    }

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
