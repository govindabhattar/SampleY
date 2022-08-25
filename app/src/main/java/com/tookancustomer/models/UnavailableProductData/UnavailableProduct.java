package com.tookancustomer.models.UnavailableProductData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UnavailableProduct {

    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("quantity")
    @Expose
    private int quantity = 0;
    @SerializedName("is_enabled")
    @Expose
    private Integer isEnabled;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

}
