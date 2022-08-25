
package com.tookancustomer.models.getServiceProviders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Datum {

    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("array")
    @Expose
    private List<Array> array = null;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<Array> getArray() {
        return array;
    }

    public void setArray(List<Array> array) {
        this.array = array;
    }

}
