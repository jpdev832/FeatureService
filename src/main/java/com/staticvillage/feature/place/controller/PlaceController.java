package com.staticvillage.feature.place.controller;

import com.mongodb.*;
import com.staticvillage.feature.place.model.Feature;
import com.staticvillage.feature.place.model.FeatureResponse;
import com.staticvillage.feature.place.model.Place;
import com.staticvillage.feature.place.model.PlaceResponse;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Created by joelparrish on 2/15/15.
 */
@RestController
public class PlaceController {
    public static final String mongoUri = "mongodb://jpdev832.dyndns.org:27017/placey";
    public static final String DB_PLACE = "placey";
    public static final String COLL_PLACE = "places";
    public static final String COLL_FEATURE = "features";

    public static final String KEY_ID = "id";
    public static final String KEY_NEIGHBORHOOD = "neighborhood";
    public static final String KEY_CITY = "city";
    public static final String KEY_STATE = "state";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_NAME = "name";
    public static final String KEY_CATEGORY = "category";

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
    public PlaceResponse getPlace(@RequestParam(value = "id", defaultValue = "-1")String idStr,
                                  @RequestParam(value = "country", defaultValue = "usa")String country,
                                  @RequestParam(value = "state", defaultValue = "")String state,
                                  @RequestParam(value = "city", defaultValue = "")String city,
                                  @RequestParam(value = "neighborhood", defaultValue = "")String neighborhood,
                                  @RequestParam(value = "name", defaultValue = "")String name){
        long id = Long.parseLong(idStr);

        DBObject query = getQuery(id, country, state, city, neighborhood, name, "");

        Place[] places;
        try {
            places = new Place().retrieve(Place.class, getCollection(COLL_PLACE), query);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return new PlaceResponse(null, e.getMessage());
        }

        return new PlaceResponse(places, "success");
    }

    /**
     * Add a new place to the system
     * @param place place information
     * @return response
     */
    @RequestMapping(value = "/place", method = RequestMethod.PUT)
    public PlaceResponse setPlace(@RequestBody Place place){
        DBObject query = getQuery(place.getId(), "", "", "", "", "", "");

        try {
            if(place.retrieve(Place.class, getCollection(COLL_PLACE), query) != null) {
                System.out.println(String.format("Already exists: %s", place.name));
                return new PlaceResponse(null, "already exists");
            }

            if(place.insert(getCollection(COLL_PLACE), place)) {
                System.out.println(String.format("Added!: %s", place.name));
                return new PlaceResponse(new Place[]{place}, "success");
            }else {
                System.out.println(String.format("Failed to Add: %s", place.name));
                return new PlaceResponse(null, "failed");
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return new PlaceResponse(null, String.format("error occurred: %s", e.getMessage()));
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
        DBObject query = getQuery(place.getId(), "", "", "", "", "", "");

        try {
            if(place.retrieve(Place.class, getCollection(COLL_PLACE), query) == null)
                return new PlaceResponse(null, "unknown place");

            if(place.update(KEY_ID, getCollection(COLL_PLACE), place))
                return new PlaceResponse(new Place[]{place}, "success");
            else
                return new PlaceResponse(null, "failed");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return new PlaceResponse(null, String.format("error occurred: %s", e.getMessage()));
        }
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
    public FeatureResponse getFeature(@RequestParam(value = "id", defaultValue = "-1")String idStr,
                                       @RequestParam(value = "name", defaultValue = "")String name,
                                       @RequestParam(value = "category", defaultValue = "")String category){
        long id = Long.parseLong(idStr);

        DBObject query = getQuery(id, "", "", "", "", name, category);

        Feature[] features;
        try {
            features = new Feature().retrieve(Feature.class, getCollection(COLL_FEATURE), query);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return new FeatureResponse(null, e.getMessage());
        }

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
        DBObject query = getQuery(feature.getId(), "", "", "", "", "", "");

        try {
            if(feature.retrieve(Feature.class, getCollection(COLL_FEATURE), query) != null)
                return new FeatureResponse(null, "already exists");

            if(feature.insert(getCollection(COLL_PLACE), feature))
                return new FeatureResponse(new Feature[]{feature}, "success");
            else
                return new FeatureResponse(null, "failed");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return new FeatureResponse(null, String.format("error occurred: %s", e.getMessage()));
        }
    }

    /**
     * Get MongoDB place collection for given country
     *
     * @param collection collection
     * @return MongoDB DBCollection
     * @throws UnknownHostException
     */
    private DBCollection getCollection(String collection) throws UnknownHostException {
        MongoClientURI uri = new MongoClientURI(mongoUri);
        MongoClient client = new MongoClient(uri);

        DB mdb = client.getDB(DB_PLACE);

        DBCollection dbCollection;
        if(mdb.collectionExists(collection))
            dbCollection = mdb.getCollection(collection);
        else
            dbCollection = mdb.createCollection(collection, new BasicDBObject("capped", false));

        return dbCollection;
    }

    /**
     * Get retrieval query
     *
     * @param country country
     * @param state state
     * @param city city
     * @param neighborhood neighborhood
     * @param name place name
     * @return retrieval query
     */
    private DBObject getQuery(long id, String country, String state, String city, String neighborhood, String name,
                              String category){
        if(id < 1 && country.isEmpty() && state.isEmpty() && city.isEmpty() && neighborhood.isEmpty() && name.isEmpty()
                && category.isEmpty())
            return null;

        BasicDBObject dbObject = new BasicDBObject();
        if(id > 0)
            dbObject.put(KEY_ID, id);
        if(!country.isEmpty())
            dbObject.put(KEY_COUNTRY, country);
        if(!state.isEmpty())
            dbObject.put(KEY_STATE, state);
        if(!city.isEmpty())
            dbObject.put(KEY_CITY, city);
        if(!neighborhood.isEmpty())
            dbObject.put(KEY_NEIGHBORHOOD, neighborhood);
        if(!name.isEmpty())
            dbObject.put(KEY_NAME, name);
        if(!category.isEmpty())
            dbObject.put(KEY_CATEGORY, category);

        return dbObject;
    }

    private DBObject getQueryWithin(String key, long... lng){
        BasicDBList lst = new BasicDBList();
        lst.addAll(Arrays.asList(lng));

        BasicDBObject inObj = new BasicDBObject("$in", lst);
        return new BasicDBObject(key, inObj);
    }

    private DBObject getQueryWithin(String key, String... str){
        BasicDBList lst = new BasicDBList();
        lst.addAll(Arrays.asList(str));

        BasicDBObject inObj = new BasicDBObject("$in", lst);
        return new BasicDBObject(key, inObj);
    }
}
