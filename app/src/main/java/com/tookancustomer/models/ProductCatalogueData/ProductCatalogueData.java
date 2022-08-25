package com.tookancustomer.models.ProductCatalogueData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.MetaInfo;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductCatalogueData extends BaseModel implements Serializable {

    @SerializedName("data")
    @Expose
    private ArrayList<Datum> data = new ArrayList<>();

    @SerializedName("metaInfo")
    @Expose
    private MetaInfo metaInfo;

    private final static long serialVersionUID = -6229972829953961297L;


    public ArrayList<Datum> getData() {
        return data;
    }

    public void setData(ArrayList<Datum> data) {
        this.data = data;
    }

    public MetaInfo getMetaInfo() {
        return metaInfo;
    }

    public void setMetaInfo(MetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }
}
