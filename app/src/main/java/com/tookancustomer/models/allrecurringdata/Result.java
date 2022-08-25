package com.tookancustomer.models.allrecurringdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.checkoutTemplate.model.Template;
import com.tookancustomer.models.RecurringSurgeListData;
import com.tookancustomer.models.TaxesModel;
import com.tookancustomer.models.alltaskdata.OrderDetails;
import com.tookancustomer.utility.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ashutosh Ojha on 2/18/19.
 */
public class Result {

    @SerializedName("rule_id")
    @Expose
    private int ruleId;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("vendor_id")
    @Expose
    private int vendorId;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("marketplace_user_id")
    @Expose
    private int marketplaceUserId;


    @SerializedName("transaction_charges")
    @Expose
    private String transaction_charges;


    @SerializedName("total_recurring_amount")
    @Expose
    private BigDecimal total_recurring_amount;



    @SerializedName("day_array")
    @Expose
    private List<Integer> dayArray = null;
    @SerializedName("request_body")
    @Expose
    private RequestBody requestBody;
    @SerializedName("product_json")
    @Expose
    private List<ProductJson> productJson = null;
    @SerializedName("occurrence_count")
    @Expose
    private int occurenceCount;
    @SerializedName("schedule_time")
    @Expose
    private String scheduleTime;
    @SerializedName("start_schedule")
    @Expose
    private String startSchedule;
    @SerializedName("end_schedule")
    @Expose
    private String endSchedule;
    @SerializedName("is_active")
    @Expose
    private int isActive;
    @SerializedName("is_paused")
    @Expose
    private int isPaused;
    @SerializedName("creation_datetime")
    @Expose
    private String creationDatetime;
    @SerializedName("update_datetime")
    @Expose
    private String updateDatetime;

    @SerializedName("amount")
    @Expose
    private String amount;

    @SerializedName("job_pickup_address")
    @Expose
    private String jobPickupAddress;

    @SerializedName("merchant_name")
    @Expose
    private String merchantName;

    @SerializedName("checkout_template")
    @Expose
    private ArrayList<Template> checkoutTemplate;

    @SerializedName("products")
    @Expose
//    @SerializedName("orderDetails")
//    @Expose
    private List<OrderDetails> orderDetails = null;


    @SerializedName("user_taxes")
    @Expose
    private ArrayList<TaxesModel> userTaxes = new ArrayList<>();

    @SerializedName("is_custom_order")
    @Expose
    private int isCustomOrder;

    @SerializedName("order_amount")
    @Expose
    private BigDecimal orderAmount;

    @SerializedName("total_amount")
    @Expose
    private BigDecimal totalAmount;

    @SerializedName("delivery_charge")
    @Expose
    private BigDecimal deliveryCharge;

    @SerializedName("business_type")
    @Expose
    private int businessType = 1;

    @SerializedName("pd_or_appointment")
    @Expose
    private int pd_or_appointment = 1;

    @SerializedName("task_type")
    @Expose
    private int taskType = 0; //here 1 means pickup task and 2 means delivery task for laundry

    @SerializedName("schedule_type")
    @Expose
    private int scheduleType = 0; //here 1 means EndDate and 2 means End Count

    @SerializedName("order_currency_symbol")
    @Expose
    private String orderCurrencySymbol;


    @SerializedName("recurring_surge_detail")
    @Expose
    private ArrayList<RecurringSurgeListData> recurringSurgeListData;


    public String getOrderCurrencySymbol() {
        return orderCurrencySymbol;
    }

    public void setOrderCurrencySymbol(String orderCurrencySymbol) {
        this.orderCurrencySymbol = orderCurrencySymbol;
    }


    public String getTransaction_charges() {
        return transaction_charges;
    }

    public BigDecimal getTotal_recurring_amount() {
        return total_recurring_amount;
    }




    public int getPd_or_appointment() {
        return pd_or_appointment;
    }

    public int getTaskType() {
        return taskType;
    }

    public int getBusinessType() {
        return businessType;
    }


    public int getScheduleType() {
        return scheduleType;
    }

    public int getStatus() {
        return status;
    }

    public String getOrderAmount() {
        return orderAmount != null ? Utils.getDoubleTwoDigits(orderAmount) : "0";
    }

    public String getTotalAmount() {
        return totalAmount != null ? Utils.getDoubleTwoDigits(totalAmount) : "0";
    }

    public String getDeliveryCharge() {
        return deliveryCharge != null ? Utils.getDoubleTwoDigits(deliveryCharge) : "0";
    }

    public ArrayList<RecurringSurgeListData> getRecurringSurgeListData() {
        return recurringSurgeListData;
    }

    public void setRecurringSurgeListData(ArrayList<RecurringSurgeListData> recurringSurgeListData) {
        this.recurringSurgeListData = recurringSurgeListData;
    }

    public int getIsCustomOrder() {
        return isCustomOrder;
    }

    public ArrayList<TaxesModel> getUserTaxes() {
        return userTaxes;
    }

    public List<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public ArrayList<Template> getCheckoutTemplate() {
        return checkoutTemplate;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getJobPickupAddress() {
        return jobPickupAddress;
    }

    public String getAmount() {
        return amount;
    }

    public int getIsPaused() {
        return isPaused;
    }

    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMarketplaceUserId() {
        return marketplaceUserId;
    }

    public void setMarketplaceUserId(int marketplaceUserId) {
        this.marketplaceUserId = marketplaceUserId;
    }


    public void setDayArray(final List<Integer> dayArray) {
        this.dayArray = dayArray;
    }

    public List<Integer> getDayArray() {
        return dayArray;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    public List<ProductJson> getProductJson() {
        return productJson;
    }

    public void setProductJson(List<ProductJson> productJson) {
        this.productJson = productJson;
    }

    public int getOccurenceCount() {
        return occurenceCount;
    }

    public void setOccurenceCount(int occurenceCount) {
        this.occurenceCount = occurenceCount;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String getStartSchedule() {
        return startSchedule;
    }

    public void setStartSchedule(String startSchedule) {
        this.startSchedule = startSchedule;
    }

    public String getEndSchedule() {
        return endSchedule;
    }

    public void setEndSchedule(String endSchedule) {
        this.endSchedule = endSchedule;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
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
