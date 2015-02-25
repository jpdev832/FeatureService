package com.staticvillage.feature.place.store;

/**
 * Created by joelparrish on 2/24/15.
 */
public interface DataStore<T> {
    /**
     * Retrieve data from the data store
     *
     * @param clazz class of retrieved objects
     * @param extras any additional data need for data store
     * @return retrieved objects
     */
    public T[] retrieve(Class<T> clazz, Object extras);

    /**
     * Insert data into the data store
     *
     * @param object object to store
     * @param extras any additional data need for data store
     * @return insert status
     */
    public boolean insert(T object, Object extras);

    /**
     * Update data in the data store
     *
     * @param object object to update
     * @param extras any additional data need for data store
     * @return update status
     */
    public boolean update(T object, Object extras);
}
