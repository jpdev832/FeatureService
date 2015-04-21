package com.staticvillage.feature.place.controller;

import com.staticvillage.feature.place.model.GeoNeighborhood;
import com.staticvillage.feature.place.model.Neighborhood;
import com.staticvillage.feature.place.store.GeoStore;
import org.springframework.web.bind.annotation.*;

/**
 * Created by joelparrish on 4/13/15.
 */
@RestController
public class GeoController {
    private GeoStore store;

    public GeoController(){
        store = new GeoStore();
    }

    /**
     * Get neighborhood REST data
     *
     * @param lat latitude
     * @param lng longitude
     * @return neighborhood
     */
    @RequestMapping(value = "/neighborhood", method = RequestMethod.GET)
    public Neighborhood getNeighborHood(@RequestParam(value = "lat", defaultValue = "-91") String lat,
                                    @RequestParam(value = "lng", defaultValue = "-181") String lng){
        double latitude = Double.parseDouble(lat);
        double longitude = Double.parseDouble(lng);

        return store.getNeighborhood(latitude, longitude);
    }

    /**
     * Add new neighborhood geospatial data
     *
     * @param neighborhood neighborhood
     * @return success
     */
    @RequestMapping(value = "/neighborhood", method = {RequestMethod.PUT,RequestMethod.POST})
    public Boolean setNeighborhoodBoundaries(@RequestBody GeoNeighborhood neighborhood){
        return store.setNeighborhood(neighborhood);
    }

    //Todo - add new method for creating new neighborhood geospatial data from a GeoJson FeatureCollection
}
