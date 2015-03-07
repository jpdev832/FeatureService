package com.staticvillage.feature.place.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by joelparrish on 2/15/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Place implements TransactionObject {
    private String id;
    private String neighborhood;
    private String city;
    private String state;
    private String country;
    private String name;
    private String location;
    private String type;
    private Feature[] features;

    public String getId() {
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

    public String getType() { return type; }

    public Feature[] getFeatures() {
        return features;
    }
}
