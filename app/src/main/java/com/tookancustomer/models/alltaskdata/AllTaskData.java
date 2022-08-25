package com.tookancustomer.models.alltaskdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.alltaskdata.AllTaskResponse;

import java.io.Serializable;

/**
 * Created by cl-macmini-83 on 19/12/16.
 */

public class AllTaskData extends BaseModel implements Serializable {
    @SerializedName("data")
    @Expose
    private AllTaskResponse data;

    /**
     * @return The data
     */
    public AllTaskResponse getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(AllTaskResponse data) {
        this.data = data;
    }
}
