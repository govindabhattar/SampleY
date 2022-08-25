package com.tookancustomer.models.MarketplaceStorefrontModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.BaseModel;

public class CityStorefrontsModel extends BaseModel implements Serializable {

    @SerializedName("data")
    @Expose
    private List<Datum> data = new ArrayList<>();

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public CityStorefrontsModel() {
    }

}
