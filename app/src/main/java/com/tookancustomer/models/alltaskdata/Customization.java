package com.tookancustomer.models.alltaskdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by socomo on 05/10/17.
 */

public class Customization implements Serializable {


    @SerializedName("order_item_id")
    @Expose
    private Integer orderItemId;

    @SerializedName("job_id")
    @Expose
    private Integer jobId;

    @SerializedName("product_id")
    @Expose
    private Integer productId;

    @SerializedName("cust_id")
    @Expose
    private Integer custId;

    @SerializedName("unit_price")
    @Expose
    private Double unitPrice;

    @SerializedName("quantity")
    @Expose
    private Integer quantity;

    @SerializedName("total_price")
    @Expose
    private Double totalPrice;

    @SerializedName("customization_linking")
    @Expose
    private String customizationLinking;

    //    @SerializedName("cust_name")
    @SerializedName(value = "cust_name", alternate = {"name"})
    @Expose
    private String custName;

    @SerializedName("status")
    @Expose
    private Integer status;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    public Integer getOrderItemId() {
        return orderItemId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public Integer getProductId() {
        return productId;
    }

    public Integer getCustId() {
        return custId;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public String getCustomizationLinking() {
        return customizationLinking;
    }

    public Integer getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setCustomizationLinking(String customizationLinking) {
        this.customizationLinking = customizationLinking;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
