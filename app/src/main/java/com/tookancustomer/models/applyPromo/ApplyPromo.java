package com.tookancustomer.models.applyPromo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.userdata.VendorPromos;

import java.io.Serializable;

public class ApplyPromo extends BaseModel implements Serializable {


    @SerializedName("data")
    @Expose
    private VendorPromos vendorPromos;

    /**
     *
     * @return
     *     The data
     */
    public VendorPromos getData() {
        return vendorPromos;
    }

    /**
     *
     * @param data
     *     The data
     */
    public void setData(VendorPromos data) {
        this.vendorPromos = data;
    }

}
