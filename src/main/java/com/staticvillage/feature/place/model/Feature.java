package com.staticvillage.feature.place.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by joelparrish on 2/15/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Feature implements TransactionObject {
    private String id;
    private String name;
    private String category;
    private float value;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory(){
        return category;
    }

    public float getValue() { return value; }
}
