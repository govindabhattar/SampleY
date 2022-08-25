package com.tookancustomer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class RecurringSurgeData implements Serializable {

    @SerializedName("valid")
    @Expose
    private Boolean valid;
    @SerializedName("data")
    @Expose
    private ArrayList<RecurringSurgeListData> data = null;

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public ArrayList<RecurringSurgeListData> getData() {
        return data;
    }

    public void setData(ArrayList<RecurringSurgeListData> data) {
        this.data = data;
    }

}