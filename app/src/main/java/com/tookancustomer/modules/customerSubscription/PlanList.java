package com.tookancustomer.modules.customerSubscription;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class PlanList implements Serializable {

    @SerializedName("plan_id")
    @Expose
    private Integer planId;
    @SerializedName("marketplace_user_id")
    @Expose
    private Integer marketplaceUserId;
    @SerializedName("plan_name")
    @Expose
    private String planName;
    @SerializedName("amount")
    @Expose
    private double amount;
    @SerializedName("plan_duration")
    @Expose
    private Integer planDuration;
    @SerializedName("reminder_days")
    @Expose
    private Integer reminderDays;
    @SerializedName("number_of_orders")
    @Expose
    private Integer numberOfOrders;
    @SerializedName("image_url")
    @Expose
    private String imageUrl = "";
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("is_active")
    @Expose
    private Integer isActive;
    @SerializedName("is_deleted")
    @Expose
    private Integer isDeleted;
    @SerializedName("creation_datetime")
    @Expose
    private String creationDatetime;
    @SerializedName("update_datetime")
    @Expose
    private String updateDatetime;

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public Integer getMarketplaceUserId() {
        return marketplaceUserId;
    }

    public void setMarketplaceUserId(Integer marketplaceUserId) {
        this.marketplaceUserId = marketplaceUserId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Integer getPlanDuration() {
        return planDuration;
    }

    public void setPlanDuration(Integer planDuration) {
        this.planDuration = planDuration;
    }

    public Integer getReminderDays() {
        return reminderDays;
    }

    public void setReminderDays(Integer reminderDays) {
        this.reminderDays = reminderDays;
    }

    public Integer getNumberOfOrders() {
        return numberOfOrders;
    }

    public void setNumberOfOrders(Integer numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCreationDatetime() {
        return creationDatetime;
    }

    public void setCreationDatetime(String creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    public String getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(String updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

}