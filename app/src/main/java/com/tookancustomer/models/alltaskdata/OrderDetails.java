package com.tookancustomer.models.alltaskdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by socomo on 05/10/17.
 */

public class OrderDetails implements Serializable {


    @SerializedName("customizations")
    @Expose
    private List<Customization> customizations = null;

    //    @SerializedName("product")
    @SerializedName(value = "product", alternate = {"products"})
    @Expose
    private Product Product = null;

    @SerializedName("job_id")
    @Expose
    private Integer jobId;


    public List<Customization> getCustomizations() {
        return customizations;
    }

    public Product getProduct() {
        return Product;
    }

    public Integer getJobId() {
        return jobId;
    }


}
