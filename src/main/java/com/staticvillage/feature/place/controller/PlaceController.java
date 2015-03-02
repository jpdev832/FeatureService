package com.staticvillage.feature.place.controller;

import com.mongodb.*;
import com.staticvillage.feature.place.model.Feature;
import com.staticvillage.feature.place.model.FeatureResponse;
import com.staticvillage.feature.place.model.Place;
import com.staticvillage.feature.place.model.PlaceResponse;
import com.staticvillage.feature.place.store.DataStore;
import com.staticvillage.feature.place.store.MongoDBFeatureStore;
import com.staticvillage.feature.place.store.MongoDBPlaceStore;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Created by joelparrish on 2/15/15.
 */
@RestController
public class PlaceController {
    //Todo - initialize from configuration at later point
    private DataStore<Place> placeStore;
    private DataStore<Feature> featureStore;

    public PlaceController(){
        placeStore = new MongoDBPlaceStore();
        featureStore = new MongoDBFeatureStore();
    }

    /**
     * Get place REST request
     *
     * @param idStr place id
     * @param country country
     * @param state state
     * @param city city
     * @param neighborhood neighborhood
     * @param name place name
     * @return response
     */
    @RequestMapping(value = "/place", method = RequestMethod.GET)
    public PlaceResponse getPlace(@RequestParam(value = "id", defaultValue = "")String idStr,
                                  @RequestParam(value = "country", defaultValue = "")String country,
                                  @RequestParam(value = "state", defaultValue = "")String state,
                                  @RequestParam(value = "city", defaultValue = "")String city,
                                  @RequestParam(value = "neighborhood", defaultValue = "")String neighborhood,
                                  @RequestParam(value = "name", defaultValue = "")String name){
        Place[] places = placeStore.retrieve(idStr, country, state, city, neighborhood, name);

        if(places == null)
            return new PlaceResponse(null, "Error occurred retrieving places");
        else
            return new PlaceResponse(places, "success");
    }

    /**
     * Add a new place to the system
     * @param place place information
     * @return response
     */
    @RequestMapping(value = "/place", method = RequestMethod.PUT)
    public PlaceResponse setPlace(@RequestBody Place place){
        Place[] places = placeStore.retrieve("", place.getCountry(), place.getState(), place.getCity(),
                place.getNeighborhood(), place.getName());

        if(places != null) {
            System.out.println(String.format("Already exists: %s", place.getName()));
            return new PlaceResponse(null, "already exists");
        }

        if(placeStore.insert(place)) {
            System.out.println(String.format("Added!: %s", place.getName()));
            return new PlaceResponse(new Place[]{place}, "success");
        }else {
            System.out.println(String.format("Failed to Add: %s", place.getName()));
            return new PlaceResponse(null, "failed");
        }
    }

    /**
     * Update place request call
     *
     * @param place place information
     * @return Response
     */
    @RequestMapping(value = "/place", method = RequestMethod.POST)
    public PlaceResponse updatePlace(@RequestBody Place place){
        Place[] places = placeStore.retrieve(place.getId(), place.getCountry(), place.getState(), place.getCity(),
                place.getNeighborhood(), place.getName());

        if(places == null) {
            System.out.println(String.format("Already exists: %s", place.getName()));
            return new PlaceResponse(null, "unknown place");
        }

        if(placeStore.update(place))
            return new PlaceResponse(new Place[]{place}, "success");
        else
            return new PlaceResponse(null, "failed");
    }

    /**
     * Retrieve features
     *
     * @param idStr id string
     * @param name feature name
     * @param category feature category
     * @return features
     */
    @RequestMapping(value = "/place/feature", method = RequestMethod.GET)
    public FeatureResponse getFeature(@RequestParam(value = "id", defaultValue = "")String idStr,
                                       @RequestParam(value = "name", defaultValue = "")String name,
                                       @RequestParam(value = "category", defaultValue = "")String category){
        Feature[] features = featureStore.retrieve(idStr, name, category);

        if(features == null)
            return new FeatureResponse(null, "Error occurred retrieving places");
        else
            return new FeatureResponse(features, "success");
    }

    /**
     * Add features to system
     *
     * @param feature feature to add
     * @return status
     */
    @RequestMapping(value = "/place/feature", method = RequestMethod.PUT)
    public FeatureResponse setFeature(@RequestBody Feature feature){
        Feature[] features = featureStore.retrieve("", feature.getName(), feature.getCategory());

        if(features != null) {
            System.out.println(String.format("Already exists: %s", feature.getName()));
            return new FeatureResponse(null, "already exists");
        }

        if(featureStore.insert(feature)) {
            System.out.println(String.format("Added!: %s", feature.getName()));
            return new FeatureResponse(new Feature[]{feature}, "success");
        }else {
            System.out.println(String.format("Failed to Add: %s", feature.getName()));
            return new FeatureResponse(null, "failed");
        }
    }
}
