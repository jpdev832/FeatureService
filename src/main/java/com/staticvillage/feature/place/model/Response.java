package com.staticvillage.feature.place.model;

/**
 * Created by joelparrish on 3/3/15.
 */
public class Response {
    private TransactionObject[] data;
    private int count;
    private String message;
    private boolean ok;
    private String index;
    private String type;

    public Response(TransactionObject[] data, String message, boolean ok, String index, String type){
        this.data = data;
        this.message = message;
        this.ok = ok;
        this.index = index;
        this.type = type;

        if(data != null)
            this.count = data.length;
        else
            this.count = 0;
    }

    public TransactionObject[] getData() { return data; }

    public int getCount() {
        return count;
    }

    public String getMessage() {
        return message;
    }

    public boolean isOk() { return ok; }

    public String getIndex() { return index; }

    public String getType() { return type; }
}
