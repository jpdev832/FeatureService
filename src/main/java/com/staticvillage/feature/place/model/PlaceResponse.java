package com.staticvillage.feature.place.model;

/**
 * Created by joelparrish on 2/15/15.
 */
public class PlaceResponse {
    private Place[] places;
    private String status;
    private int count;

    public PlaceResponse(Place[] places, String status){
        this.places = places;
        this.status = status;
        if(places != null)
            this.count = places.length;
        else
            this.count = 0;
    }

    public Place[] getPlaces() {
        return places;
    }

    public String getStatus() {
        return status;
    }

    public int getCount() {
        return count;
    }
}
