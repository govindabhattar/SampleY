
package com.tookancustomer.models.alltaskdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Services implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("job_id")
    @Expose
    private Integer jobId;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("tookan_pickup_job_id")
    @Expose
    private Integer tookanPickupJobId;
    @SerializedName("tookan_delivery_job_id")
    @Expose
    private Integer tookanDeliveryJobId;
    @SerializedName("pickup_job_status")
    @Expose
    private Integer pickupJobStatus;
    @SerializedName("delivery_job_status")
    @Expose
    private Integer deliveryJobStatus;
    @SerializedName("pickup_tracking_link")
    @Expose
    private String pickupTrackingLink;
    @SerializedName("delivery_tracking_link")
    @Expose
    private String deliveryTrackingLink;
    @SerializedName("business_type")
    @Expose
    private Integer businessType = 1;
    @SerializedName("job_status")
    @Expose
    private Integer jobStatus = -1;
    @SerializedName("cancel_allowed")
    @Expose
    private Integer cancelAllowed = 0;
    @SerializedName("tracking_link")
    @Expose
    private String trackingLink = "";


    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getTookanPickupJobId() {
        return tookanPickupJobId;
    }

    public void setTookanPickupJobId(Integer tookanPickupJobId) {
        this.tookanPickupJobId = tookanPickupJobId;
    }

    public Integer getTookanDeliveryJobId() {
        return tookanDeliveryJobId;
    }

    public void setTookanDeliveryJobId(Integer tookanDeliveryJobId) {
        this.tookanDeliveryJobId = tookanDeliveryJobId;
    }

    public Integer getPickupJobStatus() {
        return pickupJobStatus;
    }

    public void setPickupJobStatus(Integer pickupJobStatus) {
        this.pickupJobStatus = pickupJobStatus;
    }

    public Integer getDeliveryJobStatus() {
        return deliveryJobStatus;
    }

    public void setDeliveryJobStatus(Integer deliveryJobStatus) {
        this.deliveryJobStatus = deliveryJobStatus;
    }

    public String getPickupTrackingLink() {
        return pickupTrackingLink;
    }

    public void setPickupTrackingLink(String pickupTrackingLink) {
        this.pickupTrackingLink = pickupTrackingLink;
    }

    public String getDeliveryTrackingLink() {
        if (deliveryTrackingLink != null)
            return deliveryTrackingLink;
        return "";
    }

    public void setDeliveryTrackingLink(String deliveryTrackingLink) {
        this.deliveryTrackingLink = deliveryTrackingLink;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public Integer getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(Integer jobStatus) {
        this.jobStatus = jobStatus;
    }

    public Integer getCancelAllowed() {
        return cancelAllowed;
    }

    public void setCancelAllowed(Integer cancelAllowed) {
        this.cancelAllowed = cancelAllowed;
    }

    public String getTrackingLink() {
        return trackingLink;
    }

    public void setTrackingLink(String trackingLink) {
        this.trackingLink = trackingLink;
    }

}
