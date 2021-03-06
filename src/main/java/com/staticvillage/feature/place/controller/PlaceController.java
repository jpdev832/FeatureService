package com.staticvillage.feature.place.controller;

import com.staticvillage.feature.place.model.*;
import com.staticvillage.feature.place.store.DataStore;
import com.staticvillage.feature.place.store.MongoDBStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * Created by joelparrish on 2/15/15.
 */
@RestController
public class PlaceController {
    private static Logger log = LoggerFactory.getLogger(PlaceController.class);

    public static final String EXTRA_KEY_TARGET = "target";
    public static final String EXTRA_KEY_ID = "id";
    public static final String EXTRA_KEY_NAME = "name";
    public static final String EXTRA_KEY_NEIGHBORHOOD = "neighborhood";
    public static final String EXTRA_KEY_CITY = "city";
    public static final String EXTRA_KEY_STATE = "state";
    public static final String EXTRA_KEY_COUNTRY = "country";
    public static final String EXTRA_KEY_TYPE = "type";
    public static final String EXTRA_KEY_LAT = "latitude";
    public static final String EXTRA_KEY_LNG = "longitude";
    public static final String EXTRA_KEY_CATEGORY = "category";

    public static final String AUTO_TYPE_PLACE = "place";
    public static final String AUTO_TYPE_FEATURE = "feature";
    public static final String EXTRA_AUTO_TYPE = "auto_type";
    public static final String EXTRA_AUTO_KEY = "auto_key";
    public static final String EXTRA_AUTO_QUERY = "auto_query";

    private DataStore<TransactionObject> store;

    public PlaceController(){
        log.info("initializing store...");
        store = new MongoDBStore();
        log.info("store initialized");
    }

    /**
     * Add/Update a new place to the system
     * @param place place information
     * @return response
     */
    @RequestMapping(value = "/place", method = { RequestMethod.PUT, RequestMethod.POST })
    public Response setPlace(@RequestBody Place place){
        log.info(String.format("Checking if Place already exists %s...", place.toString()));

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(EXTRA_KEY_TARGET, Place.class);
        map.put(EXTRA_KEY_ID, place.getId());
        map.put(EXTRA_KEY_NAME, place.getName().toLowerCase());
        map.put(EXTRA_KEY_COUNTRY, place.getCountry().toLowerCase());
        map.put(EXTRA_KEY_STATE, place.getState().toLowerCase());
        map.put(EXTRA_KEY_CITY, place.getCity().toLowerCase());
        map.put(EXTRA_KEY_NEIGHBORHOOD, place.getNeighborhood().toLowerCase());
        map.put(EXTRA_KEY_LAT, place.getLatitude());
        map.put(EXTRA_KEY_LNG, place.getLongitude());
        map.put(EXTRA_KEY_TYPE, place.getType().toLowerCase());

        TransactionObject[] places = store.retrieve(map);

        if(places != null) {
            if(place.getId() == null) {
                log.info("Place ID is missing");
                return new Response(null, "Failed to update. {Invalid Place ID}", false, "places", "place" );
            }

            log.info(String.format("Updating %s", place.toString()));

            if(store.update(place, map)) {
                log.info(String.format("Updated!: %s", place.getId()));
                return new Response(new Place[]{place}, "success", true, "places", "place");
            }else {
                log.info(String.format("Failed to update: %s", place.getName()));
                return new Response(null, "failed", false, "places", "place");
            }
        } else {
            log.info(String.format("Adding %s", place.toString()));

            String id = store.insert(place, map);
            if (id != null) {
                place.setId(id);
                log.info(String.format("Added!: %s", place.toString()));
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
                                  @RequestParam(value = "type", defaultValue = "")String type,
                                  @RequestParam(value = "lat", defaultValue = "NaN") String latitude,
                                  @RequestParam(value = "lng", defaultValue = "NaN") String longitude,
                                  @RequestParam(value = "name", defaultValue = "")String name){
        log.info(String.format("Finding.. [name:%s,country:%s,state:%s,city:%s,neighborhood%s,type:%s,lat:%s,lng:%s]...",
                name, country, state, city,neighborhood, type, latitude, longitude));

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(EXTRA_KEY_TARGET, Place.class);
        map.put(EXTRA_KEY_NAME, name.toLowerCase());
        map.put(EXTRA_KEY_NEIGHBORHOOD, neighborhood.toLowerCase());
        map.put(EXTRA_KEY_CITY, city.toLowerCase());
        map.put(EXTRA_KEY_STATE, state.toLowerCase());
        map.put(EXTRA_KEY_COUNTRY, country.toLowerCase());
        if(!latitude.equals("Nan"))
            map.put(EXTRA_KEY_LAT, Double.parseDouble(latitude));
        if(!longitude.equals("Nan"))
            map.put(EXTRA_KEY_LNG, Double.parseDouble(longitude));
        map.put(EXTRA_KEY_TYPE, type.toLowerCase());

        TransactionObject[] places = store.retrieve(map);

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
        log.info(String.format("Finding Place with Id=%s...", id));

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(EXTRA_KEY_TARGET, Place.class);
        map.put(EXTRA_KEY_ID, id);

        TransactionObject[] places = store.retrieve(map);

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

        log.info(String.format("Updating %s, %s...", id, place.toString()));

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(EXTRA_KEY_TARGET, Place.class);
        map.put(EXTRA_KEY_ID, id);
        map.put(EXTRA_KEY_NAME, place.getName().toLowerCase());
        map.put(EXTRA_KEY_COUNTRY, place.getCountry().toLowerCase());
        map.put(EXTRA_KEY_STATE, place.getState().toLowerCase());
        map.put(EXTRA_KEY_CITY, place.getCity().toLowerCase());
        map.put(EXTRA_KEY_NEIGHBORHOOD, place.getNeighborhood().toLowerCase());
        map.put(EXTRA_KEY_LAT, place.getLatitude());
        map.put(EXTRA_KEY_LNG, place.getLongitude());
        map.put(EXTRA_KEY_TYPE, place.getType().toLowerCase());

        TransactionObject[] places = store.retrieve(map);

        if(places != null) {
            log.info(String.format("Updating {%s}", place.getName()));

            if(store.update(place, map)) {
                log.info(String.format("Updated!: %s", place.getId()));
                return new Response(new Place[]{place}, "success", true, "places", "place");
            }else {
                log.info(String.format("Failed to update: %s", place.getName()));
                return new Response(null, "failed", false, "places", "place");
            }
        }

        log.info(String.format("Failed to Update: %s", place.toString()));
        return new Response(null, "failed", false, "places", "place");
    }

    /**
     * Retrieve autocomplete place name
     *
     * @param query query string
     * @return autocomplete features
     */
    @RequestMapping(value = "/autocomplete/place/name", method = RequestMethod.GET)
    public Response autoCompletePlaceName(@RequestParam(value = "q", defaultValue = "") String query){
        return autoComplete(store, AUTO_TYPE_PLACE, "name", query.toLowerCase());
    }

    /**
     * Retrieve autocomplete place neighborhood
     *
     * @param query query string
     * @return autocomplete features
     */
    @RequestMapping(value = "/autocomplete/place/neighborhood", method = RequestMethod.GET)
    public Response autoCompletePlaceNeighborhood(@RequestParam(value = "q", defaultValue = "") String query){
        return autoComplete(store, AUTO_TYPE_PLACE, "neighborhood", query.toLowerCase());
    }

    /**
     * Retrieve autocomplete place city
     *
     * @param query query string
     * @return autocomplete features
     */
    @RequestMapping(value = "/autocomplete/place/city", method = RequestMethod.GET)
    public Response autoCompletePlaceCity(@RequestParam(value = "q", defaultValue = "") String query){
        return autoComplete(store, AUTO_TYPE_PLACE, "city", query.toLowerCase());
    }

    /**
     * Retrieve autocomplete place state
     *
     * @param query query string
     * @return autocomplete features
     */
    @RequestMapping(value = "/autocomplete/place/state", method = RequestMethod.GET)
    public Response autoCompletePlaceState(@RequestParam(value = "q", defaultValue = "") String query){
        return autoComplete(store, AUTO_TYPE_PLACE, "state", query.toLowerCase());
    }

    /**
     * Retrieve autocomplete place name
     *
     * @param query query string
     * @return autocomplete features
     */
    @RequestMapping(value = "/autocomplete/place/country", method = RequestMethod.GET)
    public Response autoCompletePlaceCountry(@RequestParam(value = "q", defaultValue = "") String query){
        return autoComplete(store, AUTO_TYPE_PLACE, "country", query.toLowerCase());
    }

    /**
     * Add features to system
     *
     * @param feature feature to add
     * @return status
     */
    @RequestMapping(value = "/place/feature", method = { RequestMethod.PUT, RequestMethod.POST })
    public Response setFeature(@RequestBody Feature feature){
        log.info(String.format("Checking if Feature exists %s...", feature.toString()));

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(EXTRA_KEY_TARGET, Feature.class);
        map.put(EXTRA_KEY_NAME, feature.getName().toLowerCase());
        map.put(EXTRA_KEY_CATEGORY, feature.getCategory().toLowerCase());

        TransactionObject[] features = store.retrieve(map);

        if(features != null) {
            log.info(String.format("Feature already exists: {%s}", feature.toString()));
            return new Response(null, "already exists", false, "features", "feature");
        }

        String id = store.insert(feature, map);
        if(id != null) {
            feature.setId(id);
            log.info(String.format("Feature was added: %s", feature.toString()));
            return new Response(new Feature[]{feature}, "success", true, "features", "feature");
        }else {
            log.info(String.format("Failed to add feature: %s", feature.toString()));
            return new Response(null, "failed", false, "features", "feature");
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

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(EXTRA_KEY_TARGET, Feature.class);
        map.put(EXTRA_KEY_NAME, name.toLowerCase());
        map.put(EXTRA_KEY_CATEGORY, category.toLowerCase());

        TransactionObject[] features = store.retrieve(map);

        if(features == null) {
            log.warn("no features were found!");
            return new Response(null, "Error occurred retrieving features", false, "places", "feature");
        } else {
            log.info(String.format("Found %d features", features.length));
            return new Response(features, "success", true, "features", "feature");
        }
    }

    /**
     * Retrieve feature
     *
     * @param id feature name
     * @return feature
     */
    @RequestMapping(value = "/place/feature/{id}", method = RequestMethod.GET)
    public Response getFeature(@PathVariable("id") String id){
        log.info(String.format("Retrieving Feature [%s]...", id));

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(EXTRA_KEY_TARGET, Feature.class);
        map.put(EXTRA_KEY_ID, id);

        TransactionObject[] features = store.retrieve(map);

        if(features == null) {
            log.warn("no features were found!");
            return new Response(null, "Error occurred retrieving feature", false, "features", "feature");
        } else {
            log.info(String.format("Found %d features", features.length));
            return new Response(features, "success", true, "features", "feature");
        }
    }

    /**
     * Retrieve autocomplete feature name
     *
     * @param query query string
     * @return autocomplete features
     */
    @RequestMapping(value = "/autocomplete/feature/name", method = RequestMethod.GET)
    public Response autoCompleteFeatureName(@RequestParam(value = "q", defaultValue = "") String query){
        return autoComplete(store, AUTO_TYPE_FEATURE, "name", query.toLowerCase());
    }

    /**
     * Retrieve autocomplete feature category
     *
     * @param query query string
     * @return autocomplete features
     */
    @RequestMapping(value = "/autocomplete/feature/category", method = RequestMethod.GET)
    public Response autoCompleteFeatureCategory(@RequestParam(value = "q", defaultValue = "") String query){
        return autoComplete(store, AUTO_TYPE_FEATURE, "category", query.toLowerCase());
    }

    /**
     * AutoComplete function
     *
     * @param dataStore datastore used for retrieving
     * @param type response type
     * @param query query
     * @return autocomplete
     */
    private Response autoComplete(DataStore dataStore, String type, String key, String query){
        log.info(String.format("looking for terms like [%s]...", query));
        if(query.length() < 3)
            return new Response(null, "Too short", false, "autocomplete", type);

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(EXTRA_AUTO_TYPE, type);
        map.put(EXTRA_AUTO_KEY, key);
        map.put(EXTRA_AUTO_QUERY, query);

        Object[] res = dataStore.autoComplete(map);

        if(res == null) {
            log.warn("no autocomplete results");
            return new Response(null, "No autocomplete results found", false, "autocomplete", type);
        } else {
            log.info(String.format("Found %d features", res.length));
            return new Response(res, "success", true, "autocomplete", type);
        }
    }
}
