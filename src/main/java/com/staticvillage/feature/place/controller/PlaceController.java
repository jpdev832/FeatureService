package com.staticvillage.feature.place.controller;

import com.mongodb.*;
import com.staticvillage.feature.place.model.Place;
import com.staticvillage.feature.place.model.PlaceResponse;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by joelparrish on 2/15/15.
 */
@RestController
public class PlaceController {
    public static final String mongoUri = "mongodb://localhost:27017/places";
    public static final String KEY_ID = "id";
    public static final String KEY_NEIGHBORHOOD = "neighborhood";
    public static final String KEY_CITY = "city";
    public static final String KEY_STATE = "state";
    public static final String KEY_NAME = "name";
    public static final String KEY_FEATURES = "features";

    @RequestMapping(value = "/place", method = RequestMethod.GET)
    public PlaceResponse getPlace(@RequestParam(value = "id", defaultValue = "-1")String idStr){
        long id = Long.parseLong(idStr);

        Place[] places = null;

        try {
            if(id == -1)
                places = retrievePlaces();
            else
                places = retrievePlace(id);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return new PlaceResponse(null, e.getMessage());
        }

        return new PlaceResponse(places, "Success");
    }

    @RequestMapping(value = "/place", method = RequestMethod.PUT)
    public Place setPlace(@RequestBody Place place){

        return null;
    }

    @RequestMapping(value = "/place", method = RequestMethod.POST)
    public Place updatePlace(@RequestBody Place place){

        return null;
    }

    private Place[] retrievePlace(long id) throws UnknownHostException {
        MongoClientURI uri = new MongoClientURI(mongoUri);
        MongoClient client = client = new MongoClient(uri);

        DB db = client.getDB("places");
        DBCollection collection = db.getCollection("usa");

        DBObject query = new BasicDBObject(KEY_ID, id);
        DBCursor cursor = collection.find(query);

        Place[] places = new Place[1];
        while(cursor.hasNext()){
            DBObject obj = cursor.next();

            Place place = new Place();
            place.id = (Integer)obj.get(KEY_ID);
            place.neighborhood = (String)obj.get(KEY_NEIGHBORHOOD);
            place.city = (String)obj.get(KEY_CITY);
            place.state = (String)obj.get(KEY_STATE);
            place.name = (String)obj.get(KEY_NAME);

            //add features;

            places[0] = place;
        }

        return places;
    }

    private Place[] retrievePlaces() throws UnknownHostException {
        int index = 0 ;

        MongoClientURI uri = new MongoClientURI(mongoUri);
        MongoClient client = client = new MongoClient(uri);

        DB db = client.getDB("places");
        DBCollection collection = db.getCollection("usa");

        DBCursor cursor = collection.find();
        Place[] places = new Place[cursor.count()];
        while(cursor.hasNext()){
            DBObject obj = cursor.next();

            Place place = new Place();
            place.id = (Integer)obj.get(KEY_ID);
            place.neighborhood = (String)obj.get(KEY_NEIGHBORHOOD);
            place.city = (String)obj.get(KEY_CITY);
            place.state = (String)obj.get(KEY_STATE);
            place.name = (String)obj.get(KEY_NAME);

            //add features

            places[index++] = place;
        }

        return places;
    }
}
