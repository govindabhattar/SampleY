
package com.tookancustomer.models.listingdata;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.BaseModel;


public class ListingData extends BaseModel{

    @SerializedName("data")
    @Expose
    private ArrayList<Datum> data=new ArrayList();
//    @SerializedName("data")
//    @Expose
//    private BannersData data = null;

    public ArrayList<Datum> getData() {
        return data;
    }

    public void setData(ArrayList<Datum> data) {
        this.data = data;
    }
}
