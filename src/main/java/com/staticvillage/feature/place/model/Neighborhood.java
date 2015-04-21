package com.staticvillage.feature.place.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by joelparrish on 4/12/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Neighborhood {
    private String neighborhood;
    private String city;
    private String state;
    private String country;

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
