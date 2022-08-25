package com.tookancustomer.models.OnGoingOrdersData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OnGoingOrderData implements Serializable {

    @SerializedName("job_id")
    @Expose
    private Integer jobId;
    @SerializedName("business_type")
    @Expose
    private int businessType;
    @SerializedName("job_status")
    @Expose
    private Integer jobStatus;
    @SerializedName("delivery_or_pickup")
    @Expose
    private Integer deliveryOrPickup;
    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("display_name")
    @Expose
    private String displayName;


    @SerializedName("hippo_transaction_id")
    @Expose
    private String hippoTransectionId;
    @SerializedName("is_custom_order")
    @Expose
    private int isCustomOrder = 0;
    @SerializedName("grouping_tags")
    @Expose
    private ArrayList<String> groupingTags;


    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("logo")
    @Expose
    private String logo;


    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("tracking_link")
    @Expose
    private List<String> trackingLink = null;

    public String getHippoTransectionId() {
        return hippoTransectionId != null ? hippoTransectionId : "";
    }

    public void setHippoTransectionId(String hippoTransectionId) {
        this.hippoTransectionId = hippoTransectionId;
    }

    public int getIsCustomOrder() {
        return isCustomOrder;
    }

    public void setIsCustomOrder(int isCustomOrder) {
        this.isCustomOrder = isCustomOrder;
    }

    public ArrayList<String> getGroupingTags() {
        return groupingTags;
    }

    public void setGroupingTags(ArrayList<String> groupingTags) {
        this.groupingTags = groupingTags;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public int getBusinessType() {
        return businessType;
    }

    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }

    public Integer getJobStatus() {
        return jobStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setJobStatus(Integer jobStatus) {
        this.jobStatus = jobStatus;
    }

    public Integer getDeliveryOrPickup() {
        return deliveryOrPickup;
    }

    public void setDeliveryOrPickup(Integer deliveryOrPickup) {
        this.deliveryOrPickup = deliveryOrPickup;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<String> getTrackingLink() {
        return trackingLink;
    }

    public void setTrackingLink(List<String> trackingLink) {
        this.trackingLink = trackingLink;
    }

}