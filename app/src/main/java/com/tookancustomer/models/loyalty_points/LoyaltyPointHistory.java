
package com.tookancustomer.models.loyalty_points;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoyaltyPointHistory {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("available_points")
    @Expose
    private Integer availablePoints;
    @SerializedName("order_id")
    @Expose
    private Object orderId;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("expiry_date")
    @Expose
    private String expiryDate;
    @SerializedName("creation_date_time")
    @Expose
    private String creationDateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAvailablePoints() {
        return availablePoints;
    }

    public void setAvailablePoints(Integer availablePoints) {
        this.availablePoints = availablePoints;
    }

    public Object getOrderId() {
        return orderId;
    }

    public void setOrderId(Object orderId) {
        this.orderId = orderId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

}
