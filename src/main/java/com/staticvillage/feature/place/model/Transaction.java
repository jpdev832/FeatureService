package com.staticvillage.feature.place.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.*;
import com.mongodb.util.JSON;

import java.io.IOException;
import java.lang.reflect.Array;

/**
 * Created by joelparrish on 2/22/15.
 */
public abstract class Transaction<T extends TransactionObject> {
    public T[] retrieve(Class<T> clazz, DBCollection collection, DBObject query){
        int index = 0 ;

        ObjectMapper mapper = new ObjectMapper();

        DBCursor cursor = null;
        if(query == null)
            cursor = collection.find();
        else
            cursor = collection.find(query);

        T[] objects = (T[]) Array.newInstance(clazz, cursor.count());
        while(cursor.hasNext()){
            DBObject obj = cursor.next();

            try {
                System.out.println(String.format("Retrieved: %s", obj.toString()));
                T object = mapper.readValue(obj.toString(), clazz);
                objects[index++] = object;
            } catch (IOException e) {
                e.printStackTrace();
                objects = null;
            }
        }

        if(cursor.count() > 0)
            return objects;
        else
            return null;
    }

    public boolean insert(DBCollection collection, T object){
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(object);
            System.out.println(String.format("Adding: %s", json));
            DBObject dbObj = (DBObject) JSON.parse(json);

            WriteResult writeResult = collection.insert(dbObj, WriteConcern.NORMAL);
            System.out.println(String.format("Affected rows: %d", writeResult.getN()));
            return writeResult.getN() > 0;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println(String.format("Error occurred: %s", e.getMessage()));
            return false;
        }
    }

    public boolean update(String key, DBCollection collection, T object){
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(object);
            DBObject dbObj = (DBObject) JSON.parse(json);

            DBObject query = new BasicDBObject(key, object.getId());
            WriteResult writeResult = collection.update(query, dbObj);

            return writeResult.getN() > 0;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
