package com.staticvillage.feature.place.store;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.*;
import com.mongodb.util.JSON;
import com.staticvillage.feature.place.model.GeoNeighborhood;
import com.staticvillage.feature.place.model.Neighborhood;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by joelparrish on 4/13/15.
 */
public class GeoStore {
    private static Logger log = LoggerFactory.getLogger(GeoStore.class);
    /**
     * Retrieve neighborhood from coordinates
     *
     * @param latitude
     * @param longitude
     *
     * @return neighborhood
     */
    public Neighborhood getNeighborhood(double latitude, double longitude) {
        try {
            MongoClientURI uri = new MongoClientURI("mongodb://localhost:27017/placey");
            MongoClient client = null;

                client = new MongoClient(uri);

            DB db = client.getDB("placey");

            DBCollection collection = null;
            if (!db.collectionExists("geo"))
                collection = db.createCollection("geo", new BasicDBObject("capped", false));
            else
                collection = db.getCollection("geo");

            BasicDBObject geometry = new BasicDBObject();
            geometry.put("type", "Point");
            geometry.put("coordinates", new double[]{ longitude, latitude });

            BasicDBObject intersect = new BasicDBObject("$geometry",geometry);
            BasicDBObject geo = new BasicDBObject("$geoIntersects", intersect);
            BasicDBObject query = new BasicDBObject("geo",geo);

            log.info(query.toString());
            DBObject result = collection.findOne(query);
            result.removeField("geo");

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(result.toString(), Neighborhood.class);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Neighborhood();
    }

    /**
     * Add a new neighborhood to data store
     *
     * @param neighborhood neighborhood information
     * @return success
     */
    public Boolean setNeighborhood(GeoNeighborhood neighborhood){
        try {
            MongoClientURI uri = new MongoClientURI("mongodb://localhost:27017/placey");
            MongoClient client = null;

            client = new MongoClient(uri);

            DB db = client.getDB("placey");

            DBCollection collection = null;
            if (!db.collectionExists("geo"))
                collection = db.createCollection("geo", new BasicDBObject("capped", false));
            else
                collection = db.getCollection("geo");

            BasicDBObject query = new BasicDBObject();
            query.put("neighborhood", neighborhood.getNeighborhood());
            query.put("city", neighborhood.getCity());
            query.put("state", neighborhood.getState());
            query.put("country", neighborhood.getCountry());

            log.info(query.toString());
            Cursor result = collection.find(query);

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(neighborhood);
            DBObject obj = (DBObject) JSON.parse(json);

            if(result.hasNext()) {
                log.info("neighborhood already exists...updating");
                collection.update(query, obj);
            }else
                collection.insert(obj);

            log.info("Update Successfully");
            return true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("Update failed");
        return false;
    }
}
