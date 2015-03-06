package com.staticvillage.feature.place.store;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.*;
import com.mongodb.util.JSON;
//import com.staticvillage.feature.place.model.Feature;
import com.staticvillage.feature.place.model.Place;
import org.bson.types.ObjectId;

import java.io.IOException;
//import java.lang.reflect.Array;
import java.net.UnknownHostException;
import java.util.List;
import java.util.regex.Pattern;
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
            DBObject query = getQuery((String)extras[0], (String)extras[1], (String)extras[2], (String)extras[3],
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
                obj.put(KEY_ID, ((ObjectId)obj.get("_id")).toString());
                obj.removeField("_id");

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
            DBObject dbObj = (DBObject) JSON.parse(json);
            dbObj.removeField(KEY_ID);

            System.out.println(String.format("Adding: %s", dbObj.toString()));

            WriteResult writeResult = collection.insert(dbObj, WriteConcern.NORMAL);
            System.out.println(String.format("Affected rows: %d", writeResult.getN()));
            return true;
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

            String id = (String) dbObj.get(KEY_ID);
            dbObj.removeField(KEY_ID);

            DBObject query = new BasicDBObject("_id", new ObjectId(id));
            WriteResult writeResult = collection.update(query, dbObj);

            return writeResult.getN() > 0;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Object[] autoComplete(Object... objects) {
        if(objects.length < 2)
            return new Object[0];

        String key = (String)objects[0];
        String query = (String)objects[1];

        try {
            DBCollection collection = getCollection(COLL_PLACE);
            Pattern q = Pattern.compile(String.format("^%s", query), Pattern.CASE_INSENSITIVE);
            DBObject obj = new BasicDBObject(key, q);
            List res = collection.distinct(key, obj);

            return res.toArray(new Object[]{});
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return new Object[0];
    }
}
