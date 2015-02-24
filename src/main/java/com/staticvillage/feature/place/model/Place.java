package com.staticvillage.feature.place.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by joelparrish on 2/15/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Place extends Transaction<Place> implements TransactionObject {
    public long id;
    public String neighborhood;
    public String city;
    public String state;
    public String country;
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

    public String getCountry() {
        return country;
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
