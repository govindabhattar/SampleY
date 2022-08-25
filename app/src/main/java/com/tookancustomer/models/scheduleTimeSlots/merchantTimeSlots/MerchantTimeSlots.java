
package com.tookancustomer.models.scheduleTimeSlots.merchantTimeSlots;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.BaseModel;

public class MerchantTimeSlots extends BaseModel {

    @SerializedName("data")
    @Expose
    public MerchantTimeSlotData data;

    public MerchantTimeSlotData getData() {
        return data;
    }

    public void setData(MerchantTimeSlotData data) {
        this.data = data;
    }
}
