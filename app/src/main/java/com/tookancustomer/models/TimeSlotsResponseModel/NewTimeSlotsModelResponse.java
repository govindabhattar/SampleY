
package com.tookancustomer.models.TimeSlotsResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.BaseModel;

public class NewTimeSlotsModelResponse extends BaseModel {

    @SerializedName("data")
    @Expose
    private NewData data;

    public NewData getData() {
        return data;
    }

    public void setData(NewData data) {
        this.data = data;
    }

}
