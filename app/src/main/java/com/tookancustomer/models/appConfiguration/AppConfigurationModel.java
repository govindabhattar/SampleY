package com.tookancustomer.models.appConfiguration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.BaseModel;

import java.io.Serializable;

public class AppConfigurationModel extends BaseModel implements Serializable{


    @SerializedName("data")
    @Expose
    private Datum data;

    public Datum getData() {
        return data;
    }

    public void setData(Datum data) {
        this.data = data;
    }

}
