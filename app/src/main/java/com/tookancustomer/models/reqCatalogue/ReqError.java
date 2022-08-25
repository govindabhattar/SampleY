package com.tookancustomer.models.reqCatalogue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.modules.merchantCatalog.models.categories.MerchantCategoriesData;

import java.io.Serializable;

/**
 * Created by Ashutosh Ojha on 2019-05-07.
 */
public class ReqError implements Serializable {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private MerchantCategoriesData merchantCategoriesData;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public MerchantCategoriesData getMerchantCategoriesData() {
        return merchantCategoriesData;
    }

    public void setMerchantCategoriesData(MerchantCategoriesData merchantCategoriesData) {
        this.merchantCategoriesData = merchantCategoriesData;
    }

}
