package com.tookancustomer.modules.customerSubscription;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CustomerPlan implements Serializable {
    @SerializedName("is_cancel")
    @Expose
    private int isCancel;
    @SerializedName("subscription_id")
    @Expose
    private Integer subscriptionId;
    @SerializedName("plan_id")
    @Expose
    private int planID = 0;
    @SerializedName("number_of_orders")
    @Expose
    private Integer numberOfOrders;
    @SerializedName("plan_name")
    @Expose
    private String planName;
    @SerializedName("expiry_datetime")
    @Expose
    private String expiryDatetime;
    @SerializedName("amount")
    @Expose
    private double amount;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("image_url")
    @Expose
    private String imageUrl = "";
    @SerializedName("number_of_orders_remaining")
    @Expose
    private long numberOfOrdersRemaining;

    public int getIsCancel() {
        return isCancel;
    }

    public void setIsCancel(int isCancel) {
        this.isCancel = isCancel;
    }

    public Integer getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Integer subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Integer getNumberOfOrders() {
        return numberOfOrders;
    }

    public void setNumberOfOrders(Integer numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getExpiryDatetime() {
        return expiryDatetime;
    }

    public void setExpiryDatetime(String expiryDatetime) {
        this.expiryDatetime = expiryDatetime;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public long getNumberOfOrdersRemaining() {
        return numberOfOrdersRemaining;
    }

    public void setNumberOfOrdersRemaining(long numberOfOrdersRemaining) {
        this.numberOfOrdersRemaining = numberOfOrdersRemaining;
    }

    public int getPlanID() {
        return planID;
    }

    public void setPlanID(int planID) {
        this.planID = planID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
