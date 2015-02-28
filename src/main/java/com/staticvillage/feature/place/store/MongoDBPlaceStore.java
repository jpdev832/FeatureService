package com.staticvillage.feature.place.store;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.*;
import com.mongodb.util.JSON;
//import com.staticvillage.feature.place.model.Feature;
import com.staticvillage.feature.place.model.Place;

import java.io.IOException;
//import java.lang.reflect.Array;
import java.net.UnknownHostException;
//import java.util.ArrayList;
//import java.util.Arrays;

/**
 * Created by joelparrish on 2/25/15.
 */
public class MongoDBPlaceStore extends MongoDBStore implements DataStore<Place> {
    public static final String COLL_PLACE = "places";
    //public static final String COLL_FEATURE = "features";

    /**
     * Retrieve data from the data store
     *
     * @param extras any additional data need for data store
     * @return retrieved objects
     */
    @Override
    public Place[] retrieve(Object... extras) {
        if(extras.length < 5)
            return null;

        try {
            long id = Long.parseLong((String)extras[0]);

            DBObject query = getQuery(id, (String)extras[1], (String)extras[2], (String)extras[3],
                    (String)extras[4], (String)extras[5], "");
            DBCollection collection = getCollection(COLL_PLACE);

            int index = 0 ;

            ObjectMapper mapper = new ObjectMapper();

            DBCursor cursor;
            if(query == null)
                cursor = collection.find();
            else
                cursor = collection.find(query);

            Place[] objects = new Place[cursor.count()];
            while(cursor.hasNext()){
                DBObject obj = cursor.next();

                Place object = mapper.readValue(obj.toString(), Place.class);
                objects[index++] = object;
            }

            if(cursor.count() > 0)
                return objects;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Insert data into the data store
     *
     * @param object object to store
     * @param extras any additional data need for data store
     * @return insert status
     */
    @Override
    public boolean insert(Place object, Object... extras) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            DBCollection collection = getCollection(COLL_PLACE);

            String json = mapper.writeValueAsString(object);
            System.out.println(String.format("Adding: %s", json));
            DBObject dbObj = (DBObject) JSON.parse(json);

            WriteResult writeResult = collection.insert(dbObj, WriteConcern.NORMAL);
            System.out.println(String.format("Affected rows: %d", writeResult.getN()));
            return writeResult.getN() > 0;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println(String.format("Error occurred: %s", e.getMessage()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Update data in the data store
     *
     * @param object object to update
     * @param extras any additional data need for data store
     * @return update status
     */
    @Override
    public boolean update(Place object, Object... extras) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            DBCollection collection = getCollection(COLL_PLACE);

            String json = mapper.writeValueAsString(object);
            DBObject dbObj = (DBObject) JSON.parse(json);

            DBObject query = new BasicDBObject(KEY_ID, object.getId());
            WriteResult writeResult = collection.update(query, dbObj);

            return writeResult.getN() > 0;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return false;
    }

    /*private void checkAndStoreNewFeatures(Feature[] features){
        try {
            ArrayList<Long> featureIds = new ArrayList<Long>(features.length);
            for(Feature f : features)
                featureIds.add(f.getId());

            DBObject query = getQueryWithin(KEY_ID, featureIds);
            DBCollection collection = getCollection(COLL_FEATURE);

            ObjectMapper mapper = new ObjectMapper();

            DBCursor cursor = null;
            if(query == null)
                cursor = collection.find();
            else
                cursor = collection.find(query);

            if(cursor.count() != features.length) {
                MongoDBFeatureStore featureStore = new MongoDBFeatureStore();
                while (cursor.hasNext()) {
                    DBObject obj = cursor.next();
                    Feature object = mapper.readValue(obj.toString(), Feature.class);

                    featureStore.insert(object);
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
