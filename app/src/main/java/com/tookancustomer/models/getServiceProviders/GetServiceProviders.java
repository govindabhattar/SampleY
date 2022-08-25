
package com.tookancustomer.models.getServiceProviders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.BaseModel;

import java.util.List;

public class GetServiceProviders extends BaseModel {


    @SerializedName("data")
    @Expose
    private List<Datum> data = null;


    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

}
