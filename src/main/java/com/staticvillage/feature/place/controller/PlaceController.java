package com.staticvillage.feature.place.controller;

import com.mongodb.*;
import com.staticvillage.feature.place.model.*;
import com.staticvillage.feature.place.store.DataStore;
import com.staticvillage.feature.place.store.MongoDBFeatureStore;
import com.staticvillage.feature.place.store.MongoDBPlaceStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Created by joelparrish on 2/15/15.
 */
@RestController
public class PlaceController {
    private static Logger log = LoggerFactory.getLogger(PlaceController.class);

    //Todo - initialize from configuration at later point
    private DataStore<Place> placeStore;
    private DataStore<Feature> featureStore;

    public PlaceController(){
        log.info("initializing stores...");
        placeStore = new MongoDBPlaceStore();
        featureStore = new MongoDBFeatureStore();
        log.info("stores initialized");
    }

    /**
     * Add/Update a new place to the system
     * @param place place information
     * @return response
     */
    @RequestMapping(value = "/place", method = { RequestMethod.PUT, RequestMethod.POST })
    public Response setPlace(@RequestBody Place place){
        log.info(String.format("Checking if Place already exists [%s,%s,%s,%s,%s]...", place.getName(),
                place.getCountry(), place.getState(), place.getCity(), place.getNeighborhood()));

        Place[] places = placeStore.retrieve(place.getId(), place.getCountry(), place.getState(), place.getCity(),
                place.getNeighborhood(), place.getName());

        if(places != null) {
            if(place.getId() == null) {
                log.info("Place ID is missing");
                return new Response(null, "Failed to update. {Invalid Place ID}", false, "places", "place" );
            }

            log.info(String.format("Updating {%s}", place.getName()));

            if(placeStore.update(place)) {
                log.info(String.format("Updated!: %s", place.getId()));
                return new Response(new Place[]{place}, "success", true, "places", "place");
            }else {
                log.info(String.format("Failed to update: %s", place.getName()));
                return new Response(null, "failed", false, "places", "place");
            }
        } else {
            log.info(String.format("Adding {%s}", place.getName()));

            if (placeStore.insert(place)) {
                log.info(String.format("Added!: %s", place.getId()));
                return new Response(new Place[]{place}, "success", true, "places", "place");
            } else {
                log.info(String.format("Failed to Add: %s", place.getName()));
                return new Response(null, "failed", false, "places", "place");
            }
        }
    }

    /**
     * Get place REST request
     *
     * @param country country
     * @param state state
     * @param city city
     * @param neighborhood neighborhood
     * @param name place name
     * @return response
     */
    @RequestMapping(value = "/place", method = RequestMethod.GET)
    public Response getPlace(@RequestParam(value = "country", defaultValue = "")String country,
                                  @RequestParam(value = "state", defaultValue = "")String state,
                                  @RequestParam(value = "city", defaultValue = "")String city,
                                  @RequestParam(value = "neighborhood", defaultValue = "")String neighborhood,
                                  @RequestParam(value = "name", defaultValue = "")String name){
        log.info(String.format("Checking if Place already exists [%s,%s,%s,%s,%s]...", name, country, state, city,
                neighborhood));
        Place[] places = placeStore.retrieve("", country, state, city, neighborhood, name);

        if(places == null) {
            log.warn("No items were found!");
            return new Response(null, "Error occurred retrieving places", false, "places", "place");
        } else {
            log.info(String.format("Found %d places", places.length));
            return new Response(places, "success", true, "places", "place");
        }
    }

    /**
     * Get place REST request
     *
     * @return response
     */
    @RequestMapping(value = "/place/{id}", method = RequestMethod.GET)
    public Response getPlace(@PathVariable("id") String id){
        log.info(String.format("Checking if Place already exists [%s]...", id));
        Place[] places = placeStore.retrieve(id, "", "", "", "", "");

        if(places == null) {
            log.warn("No items were found!");
            return new Response(null, "Error occurred retrieving places", false, "places", "place");
        } else {
            log.info(String.format("Found %d places", places.length));
            return new Response(places, "success", true, "places", "place");
        }
    }

    /**
     * Update place in the system
     *
     * @param place place information
     * @return response
     */
    @RequestMapping(value = "/place/{id}", method = RequestMethod.POST)
    public Response setPlace(@PathVariable("id") String id,
                                  @RequestBody Place place){

        log.info(String.format("Checking if Place exists [%s,%s,%s,%s,%s,%s]...", id, place.getName(),
                place.getCountry(), place.getState(), place.getCity(), place.getNeighborhood()));

        Place[] places = placeStore.retrieve(place.getId(), place.getCountry(), place.getState(), place.getCity(),
                place.getNeighborhood(), place.getName());

        if(places != null) {
            log.info(String.format("Updating {%s}", place.getName()));

            if(placeStore.update(place)) {
                log.info(String.format("Updated!: %s", place.getId()));
                return new Response(new Place[]{place}, "success", true, "places", "place");
            }else {
                log.info(String.format("Failed to update: %s", place.getName()));
                return new Response(null, "failed", false, "places", "place");
            }
        }

        log.info(String.format("Failed to Update: %s", place.getName()));
        return new Response(null, "failed", false, "places", "place");
    }

    /**
     * Add features to system
     *
     * @param feature feature to add
     * @return status
     */
    @RequestMapping(value = "/place/feature", method = { RequestMethod.PUT, RequestMethod.POST })
    public Response setFeature(@RequestBody Feature feature){
        log.info(String.format("Checking if Feature exists [%s,%s]...", feature.getName(), feature.getCategory()));
        Feature[] features = featureStore.retrieve(feature.getId(), feature.getName(), feature.getCategory());

        if(features != null) {
            log.info(String.format("Feature already exists: {%s}", feature.getName()));
            return new Response(null, "already exists", false, "places", "feature");
        }

        if(featureStore.insert(feature)) {
            log.info(String.format("Feature was added: {%s}", feature.getId()));
            return new Response(new Feature[]{feature}, "success", true, "places", "feature");
        }else {
            log.info(String.format("Failed to add feature: {%s}", feature.getName()));
            return new Response(null, "failed", false, "places", "feature");
        }
    }

    /**
     * Retrieve features
     *
     * @param name feature name
     * @param category feature category
     * @return features
     */
    @RequestMapping(value = "/place/feature", method = RequestMethod.GET)
    public Response getFeature(@RequestParam(value = "name", defaultValue = "")String name,
                                      @RequestParam(value = "category", defaultValue = "")String category){
        log.info(String.format("Checking if Feature exists [%s,%s]...", name, category));
        Feature[] features = featureStore.retrieve("", name, category);

        if(features == null) {
            log.warn("no features were found!");
            return new Response(null, "Error occurred retrieving features", false, "places", "feature");
        } else {
            log.info(String.format("Found %d features", features.length));
            return new Response(features, "success", true, "places", "feature");
        }
    }

    /**
     * Retrieve feature
     *
     * @return feature
     */
    @RequestMapping(value = "/place/feature/{id}", method = RequestMethod.GET)
    public Response getFeature(@PathVariable("id") String id){
        log.info(String.format("Retrieving Feature [%s]...", id));
        Feature[] features = featureStore.retrieve(id, "", "");

        if(features == null) {
            log.warn("no features were found!");
            return new Response(null, "Error occurred retrieving feature", false, "places", "feature");
        } else {
            log.info(String.format("Found %d features", features.length));
            return new Response(features, "success", true, "places", "feature");
        }
    }
}
