package com.staticvillage.feature.place.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by joelparrish on 4/18/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoNeighborhood extends Neighborhood {
    private Geo geo;

    public Geo getGeo() {
        return geo;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }

    @Override
    public String toString(){
        String geoStr = (getGeo() == null) ? "<empty>":"<polygon>";

        return "{"+
                "neighborhood:"+getNeighborhood() + "," +
                "city:"+getCity() + "," +
                "state:"+getState() + "," +
                "country:"+getCountry() + "," +
                "geo:"+ geoStr +
                "}";
    }
}
