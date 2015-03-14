package com.staticvillage.feature.place.store;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.*;
import com.mongodb.util.JSON;
import com.staticvillage.feature.place.controller.PlaceController;
import com.staticvillage.feature.place.model.Feature;
import com.staticvillage.feature.place.model.Place;
import com.staticvillage.feature.place.model.TransactionObject;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by joelparrish on 2/25/15.
 */
public class MongoDBStore implements DataStore<TransactionObject> {
    private static Logger log = LoggerFactory.getLogger(MongoDBStore.class);

    public static final String mongoUri = "mongodb://localhost:27017/placey";
    public static final String DB_PLACE = "placey";
    public static final String COLLECTION_PLACE = "places";
    public static final String COLLECTION_FEATURE = "features";

    public static final String KEY_ID = "id";
    public static final String KEY_NEIGHBORHOOD = "neighborhood";
    public static final String KEY_CITY = "city";
    public static final String KEY_STATE = "state";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_NAME = "name";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_LAT = "latitude";
    public static final String KEY_LNG = "longitude";
    public static final String KEY_TYPE = "type";

    /**
     * Retrieve data
     *
     * @param extras any additional data need for data store
     * @return data
     */
    @Override
    public TransactionObject[] retrieve(HashMap<String, Object> extras) {
        try {
            if(!extras.containsKey(PlaceController.EXTRA_KEY_TARGET))
                return null;

            String strCollection;

            if(extras.get(PlaceController.EXTRA_KEY_TARGET) == Place.class)
                strCollection = COLLECTION_PLACE;
            else if(extras.get(PlaceController.EXTRA_KEY_TARGET) == Feature.class)
                strCollection = COLLECTION_FEATURE;
            else
                return null;

            DBObject query = getQuery(extras);
            DBCollection collection = getCollection(strCollection);

            int index = 0 ;

            ObjectMapper mapper = new ObjectMapper();

            DBCursor cursor;
            if(query == null)
                cursor = collection.find();
            else
                cursor = collection.find(query);

            TransactionObject[] objects = new TransactionObject[cursor.count()];
            while(cursor.hasNext()){
                DBObject obj = cursor.next();
                obj.put(KEY_ID, ((ObjectId)obj.get("_id")).toString());
                obj.removeField("_id");

                TransactionObject object = null;
                if(extras.get(PlaceController.EXTRA_KEY_TARGET) == Place.class)
                    object = mapper.readValue(obj.toString(), Place.class);
                else if(extras.get(PlaceController.EXTRA_KEY_TARGET) == Feature.class)
                    object = mapper.readValue(obj.toString(), Feature.class);

                objects[index++] = object;
            }

            if(cursor.count() > 0)
                return objects;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Insert data
     *
     * @param object object to store
     * @param extras any additional data need for data store
     * @return id
     */
    @Override
    public String insert(TransactionObject object, HashMap<String, Object> extras) {
        if(!extras.containsKey(PlaceController.EXTRA_KEY_TARGET))
            return null;

        ObjectMapper mapper = new ObjectMapper();
        try {
            String strCollection;
            if(extras.get(PlaceController.EXTRA_KEY_TARGET) == Place.class)
                strCollection = COLLECTION_PLACE;
            else if(extras.get(PlaceController.EXTRA_KEY_TARGET) == Feature.class)
                strCollection = COLLECTION_FEATURE;
            else
                return null;

            DBCollection collection = getCollection(strCollection);

            String json = mapper.writeValueAsString(object);
            DBObject dbObj = (DBObject) JSON.parse(json);
            dbObj.removeField(KEY_ID);

            System.out.println(String.format("Adding: %s", dbObj.toString()));

            WriteResult writeResult = collection.insert(dbObj, WriteConcern.NORMAL);
            System.out.println(String.format("Affected rows: %d", writeResult.getN()));
            return ((ObjectId)dbObj.get("_id")).toString();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println(String.format("Error occurred: %s", e.getMessage()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Bulk insert operation
     *
     * @param objects objects to store
     * @param extras any additional data need for data store
     * @return id
     */
    @Override
    public String[] insertAll(TransactionObject[] objects, HashMap<String, Object> extras) {
        return null;
    }

    /**
     * Update data
     *
     * @param object object to update
     * @param extras any additional data need for data store
     * @return id
     */
    @Override
    public boolean update(TransactionObject object, HashMap<String, Object> extras) {
        if(!extras.containsKey(PlaceController.EXTRA_KEY_TARGET))
            return false;

        ObjectMapper mapper = new ObjectMapper();
        try {
            String strCollection;
            if(extras.get(PlaceController.EXTRA_KEY_TARGET) == Place.class)
                strCollection = COLLECTION_PLACE;
            else if(extras.get(PlaceController.EXTRA_KEY_TARGET) == Feature.class)
                strCollection = COLLECTION_FEATURE;
            else
                return false;

            DBCollection collection = getCollection(strCollection);

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
    public boolean delete(String id, HashMap<String, Object> extras) {
        if(!extras.containsKey(PlaceController.EXTRA_KEY_TARGET) || !extras.containsKey(PlaceController.EXTRA_KEY_ID))
            return false;

        ObjectMapper mapper = new ObjectMapper();
        try {
            String strCollection;
            if(extras.get(PlaceController.EXTRA_KEY_TARGET) == Place.class)
                strCollection = COLLECTION_PLACE;
            else if(extras.get(PlaceController.EXTRA_KEY_TARGET) == Feature.class)
                strCollection = COLLECTION_FEATURE;
            else
                return false;

            DBCollection collection = getCollection(strCollection);

            DBObject query = new BasicDBObject("_id", new ObjectId(id));
            WriteResult writeResult = collection.remove(query);

            log.info(String.format("Deleted %s", id));
            return true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deleteAll(HashMap<String, Object> extras) {
        return false;
    }

    /**
     * Auto complete query
     *
     * @param extras additional data need for data store
     * @return autocomplete results
     */
    @Override
    public Object[] autoComplete(HashMap<String, Object> extras) {
        if(!extras.containsKey(PlaceController.EXTRA_AUTO_TYPE))
            return new Object[0];

        String key = (String)extras.get(PlaceController.EXTRA_AUTO_KEY);
        String query = (String)extras.get(PlaceController.EXTRA_AUTO_QUERY);

        if(key == null || query == null)
            return new Object[0];

        try {
            String strCollection;
            if(extras.get(PlaceController.EXTRA_AUTO_TYPE) == PlaceController.AUTO_TYPE_PLACE)
                strCollection = COLLECTION_PLACE;
            else if(extras.get(PlaceController.EXTRA_AUTO_TYPE) == PlaceController.AUTO_TYPE_FEATURE)
                strCollection = COLLECTION_FEATURE;
            else
                return new Object[0];

            DBCollection collection = getCollection(strCollection);
            Pattern q = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
            DBObject obj = new BasicDBObject(key, q);
            List res = collection.distinct(key, obj);

            return res.toArray(new Object[]{});
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return new Object[0];
    }

    /**
     * Get MongoDB place collection for given country
     *
     * @param collection collection
     * @return MongoDB DBCollection
     * @throws java.net.UnknownHostException
     */
    protected DBCollection getCollection(String collection) throws UnknownHostException {
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
     * @param map hashmap of query items
     * @return retrieval query
     */
    protected DBObject getQuery(HashMap<String, Object> map){
        BasicDBObject dbObject = new BasicDBObject();

        if (map.containsKey(PlaceController.EXTRA_KEY_ID) && (String) map.get(PlaceController.EXTRA_KEY_ID) != null &&
                !((String) map.get(PlaceController.EXTRA_KEY_ID)).isEmpty())
            dbObject.put("_id", new ObjectId((String) map.get(PlaceController.EXTRA_KEY_ID)));
        if(map.containsKey(PlaceController.EXTRA_KEY_COUNTRY) &&
                (String) map.get(PlaceController.EXTRA_KEY_COUNTRY) != null &&
                !((String) map.get(PlaceController.EXTRA_KEY_COUNTRY)).isEmpty())
            dbObject.put(KEY_COUNTRY, (String) map.get(PlaceController.EXTRA_KEY_COUNTRY));
        if(map.containsKey(PlaceController.EXTRA_KEY_STATE) &&
                (String) map.get(PlaceController.EXTRA_KEY_STATE) != null &&
                !((String) map.get(PlaceController.EXTRA_KEY_STATE)).isEmpty())
            dbObject.put(KEY_STATE, (String) map.get(PlaceController.EXTRA_KEY_STATE));
        if(map.containsKey(PlaceController.EXTRA_KEY_CITY) &&
                (String) map.get(PlaceController.EXTRA_KEY_CITY) != null &&
                !((String) map.get(PlaceController.EXTRA_KEY_CITY)).isEmpty())
            dbObject.put(KEY_CITY, (String) map.get(PlaceController.EXTRA_KEY_CITY));
        if(map.containsKey(PlaceController.EXTRA_KEY_NEIGHBORHOOD) &&
                (String) map.get(PlaceController.EXTRA_KEY_NEIGHBORHOOD) != null &&
                !((String) map.get(PlaceController.EXTRA_KEY_NEIGHBORHOOD)).isEmpty())
            dbObject.put(KEY_NEIGHBORHOOD, (String) map.get(PlaceController.EXTRA_KEY_NEIGHBORHOOD));
        if(map.containsKey(PlaceController.EXTRA_KEY_NAME) &&
                (String) map.get(PlaceController.EXTRA_KEY_NAME) != null &&
                !((String) map.get(PlaceController.EXTRA_KEY_NAME)).isEmpty())
            dbObject.put(KEY_NAME, (String) map.get(PlaceController.EXTRA_KEY_NAME));
        if(map.containsKey(PlaceController.EXTRA_KEY_CATEGORY) &&
                (String) map.get(PlaceController.EXTRA_KEY_CATEGORY) != null &&
                !((String) map.get(PlaceController.EXTRA_KEY_CATEGORY)).isEmpty())
            dbObject.put(KEY_CATEGORY, (String) map.get(PlaceController.EXTRA_KEY_CATEGORY));
        if(map.containsKey(PlaceController.EXTRA_KEY_TYPE) &&
                (String) map.get(PlaceController.EXTRA_KEY_TYPE) != null &&
                !((String) map.get(PlaceController.EXTRA_KEY_TYPE)).isEmpty())
            dbObject.put(KEY_TYPE, (String) map.get(PlaceController.EXTRA_KEY_TYPE));

        return dbObject;
    }

    /**
     * Long within query
     *
     * @param key key
     * @param lng long values
     * @return objects with values
     */
    protected DBObject getQueryWithin(String key, List<Long> lng){
        BasicDBList lst = new BasicDBList();
        lst.addAll(lng);

        BasicDBObject inObj = new BasicDBObject("$in", lst);
        return new BasicDBObject(key, inObj);
    }
}
