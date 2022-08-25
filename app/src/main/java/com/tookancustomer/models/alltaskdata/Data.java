package com.tookancustomer.models.alltaskdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Data implements Serializable {
    private final static long serialVersionUID = -646255669950893325L;

    @SerializedName("delivery_method")
    @Expose
    private int delivery_method = 2; //Home delivery=2 and Self pickup=4
    @SerializedName("business_type")
    @Expose
    private int businessType = 1;
    @SerializedName("pd_or_appointment")
    @Expose
    private int pd_or_appointment = 1;
    @SerializedName("job_id")
    @Expose
    private Integer jobId;
    @SerializedName("job_type")
    @Expose
    private Integer jobType;
    @SerializedName("job_time")
    @Expose
    private String jobTime;
    @SerializedName("job_status")
    @Expose
    private int jobStatus = -1;
    @SerializedName("has_delivery")
    @Expose
    private Integer hasDelivery;
    @SerializedName("has_pickup")
    @Expose
    private Integer hasPickup;
    @SerializedName("job_description")
    @Expose
    private String jobDescription;
    @SerializedName("job_address")
    @Expose
    private String jobAddress;

    @SerializedName("job_latitude")
    @Expose
    private String jobLatitude;
    @SerializedName("job_longitude")
    @Expose
    private String jobLongitude;


    @SerializedName("job_pickup_address")
    @Expose
    private String jobPickupAddress;
    @SerializedName("job_pickup_datetime")
    @Expose
    private String jobPickupDatetime;
    @SerializedName("job_delivery_datetime")
    @Expose
    private String jobDeliveryDatetime;
    @SerializedName("pickup_delivery_relationship")
    @Expose
    private String pickupDeliveryRelationship;
    @SerializedName("creation_datetime")
    @Expose
    private String creationDatetime;
    @SerializedName("vertical")
    @Expose
    private int vertical;
    @SerializedName("isEditAllowed")
    @Expose
    private int isEditAllowed;
    @SerializedName("edit_job_status")
    @Expose
    private int editJobStatus;
    @SerializedName("form_id")
    @Expose
    private int formId;
    @SerializedName("show_status")
    @Expose
    private int showStatus;

    @SerializedName("is_menu_enabled")
    @Expose
    private int isMenuEnabled;

    @SerializedName("orderDetails")
    @Expose
    private List<OrderDetails> orderDetails = null;
    @SerializedName("total_amount")
    @Expose
    private BigDecimal totalAmount;
    @SerializedName("is_job_rated")
    @Expose
    private Integer isJobRated;
    @SerializedName("customer_rating")
    @Expose
    private Number customerRating = 0;
    @SerializedName("customer_comment")
    @Expose
    private String customerComment = "";
    @SerializedName("tracking_link")
    @Expose
    private String trackingLink = "";


    @SerializedName("hippo_transaction_id")
    @Expose
    private String hippoTransectionId = "";


    @SerializedName("merchant_name")
    @Expose
    private String merchantName = "";
    @SerializedName("enable_start_time_end_time")
    @Expose
    private Integer isStartEndTimeEnable = 0;
    @SerializedName("is_custom_order")
    @Expose
    private int isCustomOrder;
    @SerializedName("task_type")
    @Expose
    private int taskType;
    @SerializedName("grouping_tags")
    @Expose
    private ArrayList<String> groupingTags;

    @SerializedName("order_currency_symbol")
    @Expose
    private String orderCurrencySymbol;

    @SerializedName("merchant_id")
    @Expose
    private long merchantId;

    @SerializedName("orderHistory")
    @Expose
    private List<OrderHistory> orderHistory = null;

    public List<OrderHistory> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(List<OrderHistory> orderHistory) {
        this.orderHistory = orderHistory;
    }


    public String getOrderCurrencySymbol() {
        return orderCurrencySymbol;
    }

    public void setOrderCurrencySymbol(String orderCurrencySymbol) {
        this.orderCurrencySymbol = orderCurrencySymbol;
    }

    public ArrayList<String> getGroupingTags() {
        return groupingTags;
    }

    public void setGroupingTags(ArrayList<String> groupingTags) {
        this.groupingTags = groupingTags;
    }

    public String getHippoTransectionId() {
        return hippoTransectionId != null ? hippoTransectionId : "";
    }

    public void setHippoTransectionId(String hippoTransectionId) {
        this.hippoTransectionId = hippoTransectionId;
    }

    public String getMerchantName() {
        return merchantName != null ? merchantName : "";
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public int getIsCustomOrder() {
        return isCustomOrder;
    }

    public String getTrackingLink() {
        return trackingLink != null ? trackingLink : "";
    }

    public void setTrackingLink(String trackingLink) {
        this.trackingLink = trackingLink;
    }

    public String getTotalAmount() {
        return totalAmount != null ? Utils.getDoubleTwoDigits(totalAmount) : "0.00";
    }


    public int getFormId() {
        return formId;
    }

    public void setFormId(int formId) {
        this.formId = formId;
    }

    public int getVertical() {
        return vertical;
    }

    public int getIsEditAllowed() {
        return isEditAllowed;
    }

    public void setIsEditAllowed(int isEditAllowed) {
        this.isEditAllowed = isEditAllowed;
    }

    public void setVertical(int vertical) {
        this.vertical = vertical;
    }

    public static long getSerialVersionUID() {

        return serialVersionUID;
    }

    public String getCreationDatetime() {
        return creationDatetime;
    }

    public void setCreationDatetime(String creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    public Integer getJobId() {
        if (jobId == null)
            return 0;
        return jobId;
    }


    public int getEditJobStatus() {
        return editJobStatus;
    }

    public void setEditJobStatus(int editJobStatus) {
        this.editJobStatus = editJobStatus;
    }
    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getJobType() {
        return jobType;
    }

    public void setJobType(Integer jobType) {
        this.jobType = jobType;
    }

    public String getJobTime() {
        return jobTime;
    }

    public void setJobTime(String jobTime) {
        this.jobTime = jobTime;
    }

    public Integer getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(Integer jobStatus) {
        this.jobStatus = jobStatus;
    }

    public void setJobStatus(int jobStatus) {
        this.jobStatus = jobStatus;
    }

    public Integer getHasDelivery() {
        return hasDelivery;
    }

    public void setHasDelivery(Integer hasDelivery) {
        this.hasDelivery = hasDelivery;
    }

    public Integer getHasPickup() {
        return hasPickup;
    }

    public void setHasPickup(Integer hasPickup) {
        this.hasPickup = hasPickup;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobAddress() {
        return jobAddress != null ? jobAddress : "";
    }

    public void setJobAddress(String jobAddress) {
        this.jobAddress = jobAddress;
    }

    public String getJobPickupAddress() {
        return jobPickupAddress != null ? jobPickupAddress : "";
    }

    public void setJobPickupAddress(String jobPickupAddress) {
        this.jobPickupAddress = jobPickupAddress;
    }

    public long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(long merchantId) {
        this.merchantId = merchantId;
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

    public String getPickupDeliveryRelationship() {
        return pickupDeliveryRelationship;
    }

    public void setPickupDeliveryRelationship(String pickupDeliveryRelationship) {
        this.pickupDeliveryRelationship = pickupDeliveryRelationship;
    }


    public boolean isJobUnassigned() {
        return jobStatus == Constants.TaskStatus.UNASSIGNED.value;
    }

    public int getShowStatus() {
        return showStatus;
    }

    public void setShowStatus(int showStatus) {
        this.showStatus = showStatus;
    }

    public List<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetails> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Integer getIsJobRated() {
        return isJobRated;  //isJobRated=0 --> not rated, =1--> rated , =2 --> skipped
    }

    public void setIsJobRated(Integer isJobRated) {
        this.isJobRated = isJobRated;
    }

    public Number getCustomerRating() {
        return customerRating != null ? customerRating : 0;
    }

    public void setCustomerRating(Number customerRating) {
        this.customerRating = customerRating;
    }

    public String getCustomerComment() {
        return customerComment != null ? customerComment : "";
    }

    public void setCustomerComment(String customerComment) {
        this.customerComment = customerComment;
    }

    public boolean showRatings() {
        return (UIManager.getIsReviewRatingRequired() && jobStatus == Constants.TaskStatus.DELIVERED.value && getCustomerRating().intValue() > 0);
    }

    public Integer getIsStartEndTimeEnable() {
        return isStartEndTimeEnable;
    }

    public void setIsStartEndTimeEnable(Integer isStartEndTimeEnable) {
        this.isStartEndTimeEnable = isStartEndTimeEnable;
    }

    public int getBusinessType() {
        return businessType;
    }

    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }

    public int getPd_or_appointment() {
        return pd_or_appointment;
    }

    public void setPd_or_appointment(int pd_or_appointment) {
        this.pd_or_appointment = pd_or_appointment;
    }

    public int getDelivery_method() {
        return delivery_method;
    }

    public void setDelivery_method(int delivery_method) {
        this.delivery_method = delivery_method;
    }

    public int getTaskType() {
        return taskType;
    }

    public int getIsMenuEnabled() {
        return isMenuEnabled;
    }

    public void setIsMenuEnabled(int isMenuEnabled) {
        this.isMenuEnabled = isMenuEnabled;
    }

    public String getJobLatitude() {
        if (jobLatitude != null && !jobLatitude.isEmpty())
            return jobLatitude;
        else
            return "0.0";
    }

    public void setJobLatitude(String jobLatitude) {
        this.jobLatitude = jobLatitude;
    }

    public String getJobLongitude() {
        if (jobLongitude != null && !jobLongitude.isEmpty())
            return jobLongitude;
        else
            return "0.0";
    }

    public void setJobLongitude(String jobLongitude) {
        this.jobLongitude = jobLongitude;
    }
}
