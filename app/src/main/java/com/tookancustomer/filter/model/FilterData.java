
package com.tookancustomer.filter.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilterData {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
