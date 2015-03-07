package com.staticvillage.feature.place.store;

import java.util.HashMap;

/**
 * Created by joelparrish on 2/24/15.
 */
public interface DataStore<T> {
    /**
     * Retrieve data from the data store
     *
     * @param extras any additional data need for data store
     * @return retrieved objects
     */
    public T[] retrieve(HashMap<String, Object> extras);

    /**
     * Insert data into the data store
     *
     * @param object object to store
     * @param extras any additional data need for data store
     * @return insert status
     */
    public boolean insert(T object, HashMap<String, Object> extras);

    /**
     * Insert N data elements into the data store
     *
     * @param objects objects to store
     * @param extras any additional data need for data store
     * @return insert status
     */
    public boolean insertAll(T[] objects, HashMap<String, Object> extras);

    /**
     * Update data in the data store
     *
     * @param object object to update
     * @param extras any additional data need for data store
     * @return update status
     */
    public boolean update(T object, HashMap<String, Object> extras);

    /**
     * Retrieve autocomplete results
     *
     * @param extras additional data need for data store
     * @return autocomplete results
     */
    public Object[] autoComplete(HashMap<String, Object> extras);
}
