package com.staticvillage.feature.place.store;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.*;
import com.mongodb.util.JSON;
import com.staticvillage.feature.place.model.Feature;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by joelparrish on 2/25/15.
 */
public class MongoDBFeatureStore extends MongoDBStore implements DataStore<Feature> {
    public static final String COLL_FEATURE = "features";

    /**
     * Retrieve data from the data store
     *
     * @param extras any additional data need for data store
     * @return retrieved objects
     */
    @Override
    public Feature[] retrieve(Object... extras) {
        if(extras.length < 2)
            return null;

        try {
            long id = Long.parseLong((String)extras[0]);

            DBObject query = getQuery(id, "", "", "", "", (String)extras[0], (String)extras[1]);
            DBCollection collection = getCollection(COLL_FEATURE);

            int index = 0 ;

            ObjectMapper mapper = new ObjectMapper();

            DBCursor cursor;
            if(query == null)
                cursor = collection.find();
            else
                cursor = collection.find(query);

            Feature[] objects = new Feature[cursor.count()];
            while(cursor.hasNext()){
                DBObject obj = cursor.next();

                Feature object = mapper.readValue(obj.toString(), Feature.class);
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
    public boolean insert(Feature object, Object... extras) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            DBCollection collection = getCollection(COLL_FEATURE);

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
    public boolean update(Feature object, Object... extras) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            DBCollection collection = getCollection(COLL_FEATURE);

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
}
