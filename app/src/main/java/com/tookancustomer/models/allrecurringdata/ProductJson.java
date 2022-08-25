package com.tookancustomer.models.allrecurringdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ashutosh Ojha on 2/18/19.
 */
public class ProductJson {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("unit_price")
    @Expose
    private Integer unitPrice;
    @SerializedName("total_price")
    @Expose
    private Integer totalPrice;
    @SerializedName("customizations")
    @Expose
    private List<Object> customizations = null;
    @SerializedName("return_enabled")
    @Expose
    private Integer returnEnabled;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Object> getCustomizations() {
        return customizations;
    }

    public void setCustomizations(List<Object> customizations) {
        this.customizations = customizations;
    }

    public Integer getReturnEnabled() {
        return returnEnabled;
    }

    public void setReturnEnabled(Integer returnEnabled) {
        this.returnEnabled = returnEnabled;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
