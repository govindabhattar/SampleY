
package com.tookancustomer.models.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Pending implements Serializable {

    @SerializedName("job_id")
    @Expose
    private Integer jobId;
    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("merchant_name")
    @Expose
    private String merchantName;
    @SerializedName("job_description")
    @Expose
    private String jobDescription;
    @SerializedName("job_pickup_name")
    @Expose
    private String jobPickupName;
    @SerializedName("job_pickup_phone")
    @Expose
    private String jobPickupPhone;
    @SerializedName("job_pickup_address")
    @Expose
    private String jobPickupAddress;
    @SerializedName("marketplace_user_id")
    @Expose
    private Integer marketplaceUserId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("job_pickup_datetime")
    @Expose
    private String jobPickupDatetime;
    @SerializedName("job_delivery_datetime")
    @Expose
    private String jobDeliveryDatetime;
    @SerializedName("customer_rating")
    @Expose
    private Object customerRating;
    @SerializedName("seller_id")
    @Expose
    private Integer sellerId;
    @SerializedName("job_status_title")
    @Expose
    private String jobStatusTitle;
    @SerializedName("can_change_status")
    @Expose
    private Integer canChangeStatus;

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobPickupName() {
        return jobPickupName;
    }

    public void setJobPickupName(String jobPickupName) {
        this.jobPickupName = jobPickupName;
    }

    public String getJobPickupPhone() {
        return jobPickupPhone;
    }

    public void setJobPickupPhone(String jobPickupPhone) {
        this.jobPickupPhone = jobPickupPhone;
    }

    public String getJobPickupAddress() {
        return jobPickupAddress;
    }

    public void setJobPickupAddress(String jobPickupAddress) {
        this.jobPickupAddress = jobPickupAddress;
    }

    public Integer getMarketplaceUserId() {
        return marketplaceUserId;
    }

    public void setMarketplaceUserId(Integer marketplaceUserId) {
        this.marketplaceUserId = marketplaceUserId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getJobPickupDatetime() {
        return jobPickupDatetime;
    }

    public void setJobPickupDatetime(String jobPickupDatetime) {
        this.jobPickupDatetime = jobPickupDatetime;
    }

    public String getJobDeliveryDatetime() {
        return jobDeliveryDatetime;
    }

    public void setJobDeliveryDatetime(String jobDeliveryDatetime) {
        this.jobDeliveryDatetime = jobDeliveryDatetime;
    }

    public Object getCustomerRating() {
        return customerRating;
    }

    public void setCustomerRating(Object customerRating) {
        this.customerRating = customerRating;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public String getJobStatusTitle() {
        return jobStatusTitle;
    }

    public void setJobStatusTitle(String jobStatusTitle) {
        this.jobStatusTitle = jobStatusTitle;
    }

    public Integer getCanChangeStatus() {
        return canChangeStatus;
    }

    public void setCanChangeStatus(Integer canChangeStatus) {
        this.canChangeStatus = canChangeStatus;
    }

}
