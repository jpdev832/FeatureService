package com.staticvillage.feature.place.model;

import java.util.List;

/**
 * Created by joelparrish on 4/19/15.
 */
public class Geo {
    private String type;
    private List<List<List<Double>>> coordinates;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<List<List<Double>>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<List<List<Double>>> coordinates) {
        this.coordinates = coordinates;
    }
}
