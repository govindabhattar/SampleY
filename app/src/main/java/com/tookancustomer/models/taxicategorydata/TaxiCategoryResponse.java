
package com.tookancustomer.models.taxicategorydata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.userdata.Category;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TaxiCategoryResponse extends BaseModel implements Serializable {


    @SerializedName("data")
    @Expose
    private List<Category> data = new ArrayList<>();


    public List<Category> getData() {
        return data;
    }

    public void setData(List<Category> data) {
        this.data = data;
    }

}
