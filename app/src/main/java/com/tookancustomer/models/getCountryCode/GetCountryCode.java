
package com.tookancustomer.models.getCountryCode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.BaseModel;

import java.io.Serializable;

public class GetCountryCode extends BaseModel implements Serializable {

    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("geo")
    @Expose
    private Geo geo;
    @SerializedName("original")
    @Expose
    private Original original;


    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Geo getGeo() {
        return geo;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }

    public Original getOriginal() {
        return original;
    }

    public void setOriginal(Original original) {
        this.original = original;
    }

}
