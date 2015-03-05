package com.staticvillage.feature.place.store;

import com.mongodb.*;
import org.bson.types.ObjectId;

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
    protected DBObject getQuery(String id, String country, String state, String city, String neighborhood, String name,
                              String category){

        if((id == null || id.isEmpty()) && (country == null || country.isEmpty()) && (state == null || state.isEmpty() )
                && (city == null || city.isEmpty()) && (neighborhood == null || neighborhood.isEmpty()) &&
                (name == null || name.isEmpty()) && (category == null || category.isEmpty()))
            return null;

        BasicDBObject dbObject = new BasicDBObject();
        if(id != null && !id.isEmpty())
            dbObject.put("_id", new ObjectId(id));
        if(country !=null && !country.isEmpty())
            dbObject.put(KEY_COUNTRY, country);
        if(state != null && !state.isEmpty())
            dbObject.put(KEY_STATE, state);
        if(city != null && !city.isEmpty())
            dbObject.put(KEY_CITY, city);
        if(neighborhood != null && !neighborhood.isEmpty())
            dbObject.put(KEY_NEIGHBORHOOD, neighborhood);
        if(name != null && !name.isEmpty())
            dbObject.put(KEY_NAME, name);
        if(category != null && !category.isEmpty())
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
