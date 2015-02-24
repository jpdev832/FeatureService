package com.staticvillage.feature.place.model;

/**
 * Created by joelparrish on 2/15/15.
 */
public class FeatureResponse {
    private Feature[] features;
    private String status;
    private int count;

    public FeatureResponse(Feature[] features, String status){
        this.features = features;
        this.status = status;
        if(features != null)
            this.count = features.length;
        else
            this.count = 0;
    }

    public Feature[] getFeatures() {
        return features;
    }

    public String getStatus() {
        return status;
    }

    public int getCount() {
        return count;
    }
}
