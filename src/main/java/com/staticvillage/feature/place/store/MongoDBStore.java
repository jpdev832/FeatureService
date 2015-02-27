package com.staticvillage.feature.place.store;

import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by joelparrish on 2/25/15.
 */
public class MongoDBStore {
    public static final String mongoUri = "mongodb://localhost:27017/placey";
    public static final String DB_PLACE = "placey";

    public static final String KEY_ID = "id";
    public static final String KEY_NEIGHBORHOOD = "neighborhood";
    public static final String KEY_CITY = "city";
    public static final String KEY_STATE = "state";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_NAME = "name";
    public static final String KEY_CATEGORY = "category";

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
     * @param country country
     * @param state state
     * @param city city
     * @param neighborhood neighborhood
     * @param name place name
     * @return retrieval query
     */
    protected DBObject getQuery(long id, String country, String state, String city, String neighborhood, String name,
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