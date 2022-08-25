
package com.tookancustomer.models.superCategories;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.userdata.Datum;

import java.io.Serializable;
import java.util.ArrayList;

public class SuperCategoriesData extends BaseModel implements Serializable{


    @SerializedName("data")
    @Expose
    private ArrayList<Datum> data = null;


    public ArrayList<Datum> getData() {
        return data;
    }

    public void setData(ArrayList<Datum> data) {
        this.data = data;
    }

}
