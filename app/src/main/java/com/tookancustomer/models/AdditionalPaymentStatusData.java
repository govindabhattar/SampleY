package com.tookancustomer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AdditionalPaymentStatusData implements Serializable {

    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("vendor_id")
    @Expose
    private long vendorId;
    @SerializedName("marketplace_user_id")
    @Expose
    private long marketplaceUserId;
    @SerializedName("user_id")
    @Expose
    private long userId;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("payment_type")
    @Expose
    private long paymentType;
    @SerializedName("job_id")
    @Expose
    private long jobId;
    @SerializedName("payment_for")
    @Expose
    private long paymentFor;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("amount")
    @Expose
    private double amount;
    @SerializedName("status")
    @Expose
    private long status;
    @SerializedName("creation_datetime")
    @Expose
    private String creationDatetime;
    @SerializedName("updation_datetime")
    @Expose
    private String updationDatetime;
    @SerializedName("transaction_status")
    @Expose
    private long transactionStatus;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVendorId() {
        return vendorId;
    }

    public void setVendorId(long vendorId) {
        this.vendorId = vendorId;
    }

    public long getMarketplaceUserId() {
        return marketplaceUserId;
    }

    public void setMarketplaceUserId(long marketplaceUserId) {
        this.marketplaceUserId = marketplaceUserId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public long getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(long paymentType) {
        this.paymentType = paymentType;
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public long getPaymentFor() {
        return paymentFor;
    }

    public void setPaymentFor(long paymentFor) {
        this.paymentFor = paymentFor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getCreationDatetime() {
        return creationDatetime;
    }

    public void setCreationDatetime(String creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    public String getUpdationDatetime() {
        return updationDatetime;
    }

    public void setUpdationDatetime(String updationDatetime) {
        this.updationDatetime = updationDatetime;
    }

    public long getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(long transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

}