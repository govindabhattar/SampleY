package com.tookancustomer.customfield;

import com.google.gson.annotations.Expose;
import com.tookancustomer.customfield.Datum;
import com.tookancustomer.models.alltaskdata.Data;
import com.tookancustomer.models.producttypedata.ProductTypeData;

import java.util.ArrayList;

/**
 * Created by neerajwadhwani on 25/07/18.
 */

public class AddProductData {


    @Expose
    private ArrayList<Datum> data = null;
    private final static long serialVersionUID = 310698901313454510L;

    public ArrayList<Datum> getData() {
        return data;
    }

    public void setData(ArrayList<Datum> data) {
        this.data = data;
    }
}
