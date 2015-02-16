package com.staticvillage.feature.place.model;

/**
 * Created by joelparrish on 2/15/15.
 */
public class Place {
    public long id;
    public String neighborhood;
    public String city;
    public String state;
    public String name;
    public String location;
    public Feature[] features;

    public long getId() {
        return id;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Feature[] getFeatures() {
        return features;
    }
}
