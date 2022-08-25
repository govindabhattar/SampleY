package com.tookancustomer.models.allrecurringdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.checkoutTemplate.model.Template;
import com.tookancustomer.models.PromosModel;
import com.tookancustomer.models.TaxesModel;
import com.tookancustomer.models.taskdetails.LoyaltyPoints;
import com.tookancustomer.utility.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Ashutosh Ojha on 2/18/19.
 */
public class RequestBody {

    @SerializedName("tax")
    @Expose
    private String tax;
    @SerializedName("AppIP")
    @Expose
    private String appIP;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("card_id")
    @Expose
    private String cardId;
    @SerializedName("form_id")
    @Expose
    private String formId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("app_type")
    @Expose
    private String appType;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    //    @SerializedName("products")
//    @Expose
//    private String products;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("vertical")
    @Expose
    private String vertical;
    @SerializedName("day_array")
    @Expose
    private String dayArray;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("vendor_id")
    @Expose
    private String vendorId;
    @SerializedName("has_pickup")
    @Expose
    private String hasPickup;
    @SerializedName("app_version")
    @Expose
    private String appVersion;
    @SerializedName("currency_id")
    @Expose
    private String currencyId;
    @SerializedName("domain_name")
    @Expose
    private String domainName;
    @SerializedName("is_demo_app")
    @Expose
    private String isDemoApp;
    @SerializedName("layout_type")
    @Expose
    private String layoutType;
    @SerializedName("self_pickup")
    @Expose
    private String selfPickup;
    @SerializedName("end_schedule")
    @Expose
    private String endSchedule;
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("device_token")
    @Expose
    private String deviceToken;
    @SerializedName("has_delivery")
    @Expose
    private String hasDelivery;
    @SerializedName("is_scheduled")
    @Expose
    private String isScheduled;
    @SerializedName("reference_id")
    @Expose
    private String referenceId;
    @SerializedName("schedule_time")
    @Expose
    private String scheduleTime;
    @SerializedName("dual_user_key")
    @Expose
    private String dualUserKey;
    @SerializedName("yelo_app_type")
    @Expose
    private String yeloAppType;
    @SerializedName("start_schedule")
    @Expose
    private String startSchedule;
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;
    @SerializedName("occurence_count")
    @Expose
    private String occurenceCount;
    @SerializedName("auto_assignment")
    @Expose
    private String autoAssignment;
    @SerializedName("delivery_charge")
    @Expose
    private String deliveryCharge;
    @SerializedName("job_description")
    @Expose
    private String jobDescription;
    @SerializedName("job_pickup_name")
    @Expose
    private String jobPickupName;
    @SerializedName("app_access_token")
    @Expose
    private String appAccessToken;
    @SerializedName("job_pickup_email")
    @Expose
    private String jobPickupEmail;
    @SerializedName("job_pickup_phone")
    @Expose
    private String jobPickupPhone;
    @SerializedName("checkout_template")
    @Expose
    private ArrayList<Template> checkoutTemplate;
    @SerializedName("is_recurring_task")
    @Expose
    private String isRecurringTask;
    @SerializedName("job_pickup_address")
    @Expose
    private String jobPickupAddress;
    @SerializedName("job_pickup_datetime")
    @Expose
    private String jobPickupDatetime;
    @SerializedName("job_pickup_latitude")
    @Expose
    private String jobPickupLatitude;
    @SerializedName("marketplace_user_id")
    @Expose
    private String marketplaceUserId;
    @SerializedName("job_pickup_longitude")
    @Expose
    private String jobPickupLongitude;
    @SerializedName("marketplace_reference_id")
    @Expose
    private String marketplaceReferenceId;
    @SerializedName("is_app_product_tax_enabled")
    @Expose
    private String isAppProductTaxEnabled;
    @SerializedName("pickup_custom_field_template")
    @Expose
    private String pickupCustomFieldTemplate;

    @SerializedName("is_custom_order")
    @Expose
    private int isCustomOrder;

    @SerializedName("task_type")
    @Expose
    private int taskType = 0; //here 1 means pickup task and 2 means delivery task for laundry
    @SerializedName("delivery_method")
    @Expose
    private int delivery_method = 2; //Home delivery=2 and Self pickup=4

    @SerializedName("job_address")
    @Expose
    private String jobAddress;

    @SerializedName("merchant_name")
    @Expose
    private String merchantName;

    @SerializedName("promoList")
    @Expose
    private ArrayList<PromosModel> promoList = new ArrayList<>();
    @SerializedName("order_amount")
    @Expose
    private BigDecimal orderAmount;

    @SerializedName("user_taxes")
    @Expose
    private ArrayList<TaxesModel> userTaxes = new ArrayList<>();

    @SerializedName("tip")
    @Expose
    private BigDecimal tip;

    @SerializedName("coupon_discount")
    @Expose
    private BigDecimal couponDiscount;

    @SerializedName("total_amount")
    @Expose
    private BigDecimal totalAmount;

    @SerializedName("loyalty_points")
    @Expose
    private LoyaltyPoints loyaltyPoints;

    @SerializedName("refunded_amount")
    @Expose
    private double refundedAmount;

    @SerializedName("remaining_balance")
    @Expose
    private BigDecimal remainingBalance = new BigDecimal(0);

    @SerializedName("return_enabled")
    @Expose
    private Integer return_enabled = 0;


    public String getTotalAmount() {
        return totalAmount != null ? Utils.getDoubleTwoDigits(totalAmount) : "0";
    }

    public LoyaltyPoints getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public Integer getReturn_enabled() {
        return return_enabled;
    }

    public String getCouponDiscount() {
        return couponDiscount != null ? Utils.getDoubleTwoDigits(couponDiscount) : "0";
    }

    public BigDecimal getRemainingBalance() {
        return remainingBalance != null ? remainingBalance : BigDecimal.valueOf(0.00);
    }

    public double getRefundedAmount() {
        return refundedAmount;
    }

    public String getTip() {
        return tip != null ? Utils.getDoubleTwoDigits(tip) : "0";
    }

    public ArrayList<TaxesModel> getUserTaxes() {
        return userTaxes;
    }

    public void setUserTaxes(final ArrayList<TaxesModel> userTaxes) {
        this.userTaxes = userTaxes;
    }

    public String getOrderAmount() {
        return orderAmount != null ? Utils.getDoubleTwoDigits(orderAmount) : "0";
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getAppIP() {
        return appIP;
    }

    public void setAppIP(String appIP) {
        this.appIP = appIP;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

//    public String getProducts() {
//        return products;
//    }
//
//    public void setProducts(String products) {
//        this.products = products;
//    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getVertical() {
        return vertical;
    }

    public void setVertical(String vertical) {
        this.vertical = vertical;
    }


    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getHasPickup() {
        return hasPickup;
    }

    public void setHasPickup(String hasPickup) {
        this.hasPickup = hasPickup;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getIsDemoApp() {
        return isDemoApp;
    }

    public void setIsDemoApp(String isDemoApp) {
        this.isDemoApp = isDemoApp;
    }

    public String getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(String layoutType) {
        this.layoutType = layoutType;
    }

    public String getSelfPickup() {
        return selfPickup;
    }

    public void setSelfPickup(String selfPickup) {
        this.selfPickup = selfPickup;
    }

    public String getEndSchedule() {
        return endSchedule;
    }

    public void setEndSchedule(String endSchedule) {
        this.endSchedule = endSchedule;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getHasDelivery() {
        return hasDelivery;
    }

    public void setHasDelivery(String hasDelivery) {
        this.hasDelivery = hasDelivery;
    }

    public String getIsScheduled() {
        return isScheduled;
    }

    public void setIsScheduled(String isScheduled) {
        this.isScheduled = isScheduled;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String getDualUserKey() {
        return dualUserKey;
    }

    public void setDualUserKey(String dualUserKey) {
        this.dualUserKey = dualUserKey;
    }

    public String getYeloAppType() {
        return yeloAppType;
    }

    public void setYeloAppType(String yeloAppType) {
        this.yeloAppType = yeloAppType;
    }

    public String getStartSchedule() {
        return startSchedule;
    }

    public void setStartSchedule(String startSchedule) {
        this.startSchedule = startSchedule;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getOccurenceCount() {
        return occurenceCount;
    }

    public void setOccurenceCount(String occurenceCount) {
        this.occurenceCount = occurenceCount;
    }

    public String getAutoAssignment() {
        return autoAssignment;
    }

    public void setAutoAssignment(String autoAssignment) {
        this.autoAssignment = autoAssignment;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
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

    public String getAppAccessToken() {
        return appAccessToken;
    }

    public void setAppAccessToken(String appAccessToken) {
        this.appAccessToken = appAccessToken;
    }

    public String getJobPickupEmail() {
        return jobPickupEmail;
    }

    public void setJobPickupEmail(String jobPickupEmail) {
        this.jobPickupEmail = jobPickupEmail;
    }

    public String getJobPickupPhone() {
        return jobPickupPhone;
    }

    public void setJobPickupPhone(String jobPickupPhone) {
        this.jobPickupPhone = jobPickupPhone;
    }

    public ArrayList<PromosModel> getPromoList() {
        return promoList;
    }

    public void setPromoList(final ArrayList<PromosModel> promoList) {
        this.promoList = promoList;
    }

    public ArrayList<Template> getCheckoutTemplate() {
        return checkoutTemplate;
    }

    public void setCheckoutTemplate(final ArrayList<Template> checkoutTemplate) {
        this.checkoutTemplate = checkoutTemplate;
    }

    public String getIsRecurringTask() {
        return isRecurringTask;
    }

    public void setIsRecurringTask(String isRecurringTask) {
        this.isRecurringTask = isRecurringTask;
    }

    public String getJobPickupAddress() {
        return jobPickupAddress;
    }

    public void setJobPickupAddress(String jobPickupAddress) {
        this.jobPickupAddress = jobPickupAddress;
    }

    public String getJobPickupDatetime() {
        return jobPickupDatetime;
    }

    public void setJobPickupDatetime(String jobPickupDatetime) {
        this.jobPickupDatetime = jobPickupDatetime;
    }

    public String getJobPickupLatitude() {
        return jobPickupLatitude;
    }

    public void setJobPickupLatitude(String jobPickupLatitude) {
        this.jobPickupLatitude = jobPickupLatitude;
    }

    public String getMarketplaceUserId() {
        return marketplaceUserId;
    }

    public void setMarketplaceUserId(String marketplaceUserId) {
        this.marketplaceUserId = marketplaceUserId;
    }

    public String getJobPickupLongitude() {
        return jobPickupLongitude;
    }

    public void setJobPickupLongitude(String jobPickupLongitude) {
        this.jobPickupLongitude = jobPickupLongitude;
    }

    public String getMarketplaceReferenceId() {
        return marketplaceReferenceId;
    }

    public void setMarketplaceReferenceId(String marketplaceReferenceId) {
        this.marketplaceReferenceId = marketplaceReferenceId;
    }

    public String getIsAppProductTaxEnabled() {
        return isAppProductTaxEnabled;
    }

    public void setIsAppProductTaxEnabled(String isAppProductTaxEnabled) {
        this.isAppProductTaxEnabled = isAppProductTaxEnabled;
    }

    public String getPickupCustomFieldTemplate() {
        return pickupCustomFieldTemplate;
    }

    public void setPickupCustomFieldTemplate(String pickupCustomFieldTemplate) {
        this.pickupCustomFieldTemplate = pickupCustomFieldTemplate;
    }

    public int getIsCustomOrder() {
        return isCustomOrder;
    }

    public void setIsCustomOrder(int isCustomOrder) {
        this.isCustomOrder = isCustomOrder;
    }

    public String getDayArray() {
        return dayArray;
    }

    public void setDayArray(final String dayArray) {
        this.dayArray = dayArray;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(final int taskType) {
        this.taskType = taskType;
    }

    public int getDelivery_method() {
        return delivery_method;
    }

    public void setDelivery_method(final int delivery_method) {
        this.delivery_method = delivery_method;
    }

    public String getJobAddress() {
        return jobAddress;
    }

    public void setJobAddress(final String jobAddress) {
        this.jobAddress = jobAddress;
    }

    public String getMerchantName() {

        return merchantName == null ? "" : merchantName;
    }

    public void setMerchantName(final String merchantName) {
        this.merchantName = merchantName;
    }
}
