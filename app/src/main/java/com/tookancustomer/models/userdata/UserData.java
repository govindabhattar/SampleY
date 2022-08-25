package com.tookancustomer.models.userdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.BaseModel;

import java.io.Serializable;

public class UserData extends BaseModel implements Serializable {

    @SerializedName("data")
    @Expose
    private Data datas;

    /**
     * 
     * @return
     *     The data
     */
    public Data getData() {
        return datas;
    }

    /**
     * 
     * @param data
     *     The data
     */
    public void setData(Data data) {
        this.datas = data;
    }

}