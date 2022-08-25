package com.tookancustomer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InstaPayModel extends BaseModel implements Serializable {

    @SerializedName("data")
    @Expose
    private Object url;


    public String getUrl() {
        return url.toString();
    }

    public void setUrl(Object url) {
        this.url = url;
    }
}