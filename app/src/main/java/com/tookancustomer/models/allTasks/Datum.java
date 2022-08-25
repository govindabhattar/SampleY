
package com.tookancustomer.models.allTasks;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("business_type")
    @Expose
    private Integer businessType;
    @SerializedName("created_by")
    @Expose
    private Integer createdBy;
    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("job_id")
    @Expose
    private Integer jobId;
    @SerializedName("job_type")
    @Expose
    private Integer jobType;
    @SerializedName("job_time")
    @Expose
    private String jobTime;
    @SerializedName("has_delivery")
    @Expose
    private Integer hasDelivery;
    @SerializedName("has_pickup")
    @Expose
    private Integer hasPickup;
    @SerializedName("job_description")
    @Expose
    private String jobDescription;
    @SerializedName("coupon_discount")
    @Expose
    private Integer couponDiscount;
    @SerializedName("return_enabled")
    @Expose
    private Integer returnEnabled;
    @SerializedName("job_pickup_phone")
    @Expose
    private String jobPickupPhone;
    @SerializedName("job_pickup_address")
    @Expose
    private String jobPickupAddress;
    @SerializedName("job_pickup_datetime")
    @Expose
    private String jobPickupDatetime;
    @SerializedName("checkout_template")
    @Expose
    private List<Object> checkoutTemplate = null;
    @SerializedName("job_delivery_datetime")
    @Expose
    private String jobDeliveryDatetime;
    @SerializedName("pickup_delivery_relationship")
    @Expose
    private Object pickupDeliveryRelationship;
    @SerializedName("fleet_id")
    @Expose
    private Object fleetId;
    @SerializedName("creation_datetime")
    @Expose
    private String creationDatetime;
    @SerializedName("vertical")
    @Expose
    private Integer vertical;
    @SerializedName("marketplace_user_id")
    @Expose
    private Integer marketplaceUserId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("form_id")
    @Expose
    private Integer formId;
    @SerializedName("updated_at")
    @Expose
    private Object updatedAt;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("refunded_amount")
    @Expose
    private Object refundedAmount;
    @SerializedName("remaining_balance")
    @Expose
    private Integer remainingBalance;
    @SerializedName("tax")
    @Expose
    private Integer tax;
    @SerializedName("delivery_charge")
    @Expose
    private Integer deliveryCharge;
    @SerializedName("total_amount")
    @Expose
    private Integer totalAmount;
    @SerializedName("order_amount")
    @Expose
    private Integer orderAmount;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("surge_amount")
    @Expose
    private Integer surgeAmount;
    @SerializedName("is_job_rated")
    @Expose
    private Integer isJobRated;
    @SerializedName("is_rating_deleted")
    @Expose
    private Integer isRatingDeleted;
    @SerializedName("customer_rating")
    @Expose
    private Object customerRating;
    @SerializedName("customer_comment")
    @Expose
    private String customerComment;
    @SerializedName("pd_or_appointment")
    @Expose
    private Integer pdOrAppointment;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("merchant_email")
    @Expose
    private String merchantEmail;
    @SerializedName("merchant_phone_number")
    @Expose
    private String merchantPhoneNumber;
    @SerializedName("additional_charges")
    @Expose
    private Integer additionalCharges;
    @SerializedName("additional_charge_label")
    @Expose
    private Object additionalChargeLabel;
    @SerializedName("overall_transaction_status")
    @Expose
    private Integer overallTransactionStatus;
    @SerializedName("merchant_name")
    @Expose
    private String merchantName;
    @SerializedName("store_name_json")
    @Expose
    private String storeNameJson;
    @SerializedName("merchant_is_deleted")
    @Expose
    private Integer merchantIsDeleted;
    @SerializedName("merchant_id")
    @Expose
    private Integer merchantId;
    @SerializedName("merchant_address")
    @Expose
    private String merchantAddress;
    @SerializedName("merchant_display_address")
    @Expose
    private String merchantDisplayAddress;
    @SerializedName("merchant_latitude")
    @Expose
    private String merchantLatitude;
    @SerializedName("merchant_longitude")
    @Expose
    private String merchantLongitude;
    @SerializedName("is_cancel_allowed")
    @Expose
    private Integer isCancelAllowed;
    @SerializedName("tip")
    @Expose
    private Integer tip;
    @SerializedName("delivery_method")
    @Expose
    private Integer deliveryMethod;
    @SerializedName("is_custom_order")
    @Expose
    private Integer isCustomOrder;
    @SerializedName("task_type")
    @Expose
    private Integer taskType;
    @SerializedName("is_urgent_delivery")
    @Expose
    private Integer isUrgentDelivery;
    @SerializedName("customer_id")
    @Expose
    private Integer customerId;
    @SerializedName("customer_username")
    @Expose
    private String customerUsername;
    @SerializedName("transaction_status")
    @Expose
    private Integer transactionStatus;
    @SerializedName("job_pickup_latitude")
    @Expose
    private String jobPickupLatitude;
    @SerializedName("job_pickup_longitude")
    @Expose
    private String jobPickupLongitude;
    @SerializedName("job_latitude")
    @Expose
    private String jobLatitude;
    @SerializedName("job_longitude")
    @Expose
    private String jobLongitude;
    @SerializedName("fugu_channel_id")
    @Expose
    private Object fuguChannelId;
    @SerializedName("is_pickup_anywhere")
    @Expose
    private Object isPickupAnywhere;
    @SerializedName("job_pickup_email")
    @Expose
    private String jobPickupEmail;
    @SerializedName("job_pickup_name")
    @Expose
    private String jobPickupName;
    @SerializedName("custom_pickup_address")
    @Expose
    private String customPickupAddress;
    @SerializedName("custom_pickup_latitude")
    @Expose
    private String customPickupLatitude;
    @SerializedName("custom_pickup_longitude")
    @Expose
    private String customPickupLongitude;
    @SerializedName("merchant_module")
    @Expose
    private Integer merchantModule;
    @SerializedName("admin_setting_for_merchant")
    @Expose
    private Integer adminSettingForMerchant;
    @SerializedName("delivery_by_merchant")
    @Expose
    private Object deliveryByMerchant;
    @SerializedName("job_status")
    @Expose
    private Integer jobStatus;
    @SerializedName("cancel_order_admin")
    @Expose
    private Integer cancelOrderAdmin;
    @SerializedName("promo_code")
    @Expose
    private String promoCode;
    @SerializedName("customer_is_deleted")
    @Expose
    private Integer customerIsDeleted;
    @SerializedName("is_cash_on_delivery")
    @Expose
    private Integer isCashOnDelivery;
    @SerializedName("enabled_payment_gateways")
    @Expose
    private Object enabledPaymentGateways;
    @SerializedName("hold_amount")
    @Expose
    private Integer holdAmount;
    @SerializedName("tookan_scheduled_datetime")
    @Expose
    private Object tookanScheduledDatetime;
    @SerializedName("tookan_active_when_job_created")
    @Expose
    private Integer tookanActiveWhenJobCreated;
    @SerializedName("merchant_earning")
    @Expose
    private Integer merchantEarning;
    @SerializedName("commission_amount")
    @Expose
    private Integer commissionAmount;
    @SerializedName("commission_payout_status")
    @Expose
    private String commissionPayoutStatus;
    @SerializedName("customer_rating_by_merchant")
    @Expose
    private Integer customerRatingByMerchant;
    @SerializedName("customer_review_by_merchant")
    @Expose
    private String customerReviewByMerchant;
    @SerializedName("order_preparation_time")
    @Expose
    private Integer orderPreparationTime;
    @SerializedName("is_scheduled")
    @Expose
    private Integer isScheduled;
    @SerializedName("currency_code")
    @Expose
    private String currencyCode;
    @SerializedName("currency_symbol")
    @Expose
    private String currencySymbol;
    @SerializedName("merchant_enabled_payment_methods")
    @Expose
    private Object merchantEnabledPaymentMethods;
    @SerializedName("merchant_payment_methods")
    @Expose
    private Integer merchantPaymentMethods;
    @SerializedName("merchant_currency_id")
    @Expose
    private Object merchantCurrencyId;
    @SerializedName("transaction_charges")
    @Expose
    private Integer transactionCharges;
    @SerializedName("debt_amount")
    @Expose
    private Integer debtAmount;
    @SerializedName("order_currency_symbol")
    @Expose
    private String orderCurrencySymbol;
    @SerializedName("delivery_charge_surge_amount")
    @Expose
    private Integer deliveryChargeSurgeAmount;
    @SerializedName("is_menu_enabled")
    @Expose
    private Integer isMenuEnabled;
    @SerializedName("custom_pickup_name")
    @Expose
    private Object customPickupName;
    @SerializedName("custom_pickup_phone")
    @Expose
    private Object customPickupPhone;
    @SerializedName("custom_pickup_email")
    @Expose
    private Object customPickupEmail;
    @SerializedName("c_phone")
    @Expose
    private String cPhone;
    @SerializedName("j_address")
    @Expose
    private String jAddress;
    @SerializedName("c_email")
    @Expose
    private String cEmail;
    @SerializedName("c_username")
    @Expose
    private String cUsername;
    @SerializedName("sochitel_reference_id")
    @Expose
    private Object sochitelReferenceId;
    @SerializedName("sochitel_remark")
    @Expose
    private Object sochitelRemark;
    @SerializedName("additional_charge")
    @Expose
    private Integer additionalCharge;
    @SerializedName("additional_charges_json")
    @Expose
    private AdditionalChargesJson additionalChargesJson;
    @SerializedName("TAXABLE_AMOUNT")
    @Expose
    private Integer taxableAmount;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("adjusted_amount")
    @Expose
    private Integer adjustedAmount;
    @SerializedName("customer_phone")
    @Expose
    private String customerPhone;
    @SerializedName("job_address")
    @Expose
    private String jobAddress;
    @SerializedName("customer_email")
    @Expose
    private String customerEmail;
    @SerializedName("payment_method")
    @Expose
    private Integer paymentMethod;
    @SerializedName("custom_tag_for_merchant")
    @Expose
    private Object customTagForMerchant;
    @SerializedName("isEditAllowed")
    @Expose
    private Integer isEditAllowed;
    @SerializedName("user_taxes")
    @Expose
    private List<Object> userTaxes = null;
    @SerializedName("edit_job_status")
    @Expose
    private Integer editJobStatus;
    @SerializedName("show_status")
    @Expose
    private Integer showStatus;
    @SerializedName("orderDetails")
    @Expose
    private List<OrderDetail> orderDetails = null;
    @SerializedName("tracking_link")
    @Expose
    private Object trackingLink;
    @SerializedName("cancel_allowed")
    @Expose
    private Integer cancelAllowed;
    @SerializedName("updated_by")
    @Expose
    private String updatedBy;
    @SerializedName("reason")
    @Expose
    private Object reason;
    @SerializedName("cancellation_reason")
    @Expose
    private Object cancellationReason;
    @SerializedName("tookan_job_hash")
    @Expose
    private Object tookanJobHash;
    @SerializedName("agent_id")
    @Expose
    private Integer agentId;
    @SerializedName("amount_add_by_agent")
    @Expose
    private Object amountAddByAgent;
    @SerializedName("pickup_barcode")
    @Expose
    private Object pickupBarcode;
    @SerializedName("delivery_barcode")
    @Expose
    private Object deliveryBarcode;
    @SerializedName("job_otp")
    @Expose
    private Object jobOtp;
    @SerializedName("hippo_transaction_id")
    @Expose
    private String hippoTransactionId;
    @SerializedName("grouping_tags")
    @Expose
    private List<String> groupingTags = null;

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getJobId() {
        return jobId;
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

    public Integer getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(Integer couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public Integer getReturnEnabled() {
        return returnEnabled;
    }

    public void setReturnEnabled(Integer returnEnabled) {
        this.returnEnabled = returnEnabled;
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

    public String getJobPickupDatetime() {
        return jobPickupDatetime;
    }

    public void setJobPickupDatetime(String jobPickupDatetime) {
        this.jobPickupDatetime = jobPickupDatetime;
    }

    public List<Object> getCheckoutTemplate() {
        return checkoutTemplate;
    }

    public void setCheckoutTemplate(List<Object> checkoutTemplate) {
        this.checkoutTemplate = checkoutTemplate;
    }

    public String getJobDeliveryDatetime() {
        return jobDeliveryDatetime;
    }

    public void setJobDeliveryDatetime(String jobDeliveryDatetime) {
        this.jobDeliveryDatetime = jobDeliveryDatetime;
    }

    public Object getPickupDeliveryRelationship() {
        return pickupDeliveryRelationship;
    }

    public void setPickupDeliveryRelationship(Object pickupDeliveryRelationship) {
        this.pickupDeliveryRelationship = pickupDeliveryRelationship;
    }

    public Object getFleetId() {
        return fleetId;
    }

    public void setFleetId(Object fleetId) {
        this.fleetId = fleetId;
    }

    public String getCreationDatetime() {
        return creationDatetime;
    }

    public void setCreationDatetime(String creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    public Integer getVertical() {
        return vertical;
    }

    public void setVertical(Integer vertical) {
        this.vertical = vertical;
    }

    public Integer getMarketplaceUserId() {
        return marketplaceUserId;
    }

    public void setMarketplaceUserId(Integer marketplaceUserId) {
        this.marketplaceUserId = marketplaceUserId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getFormId() {
        return formId;
    }

    public void setFormId(Integer formId) {
        this.formId = formId;
    }

    public Object getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Object updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Object getRefundedAmount() {
        return refundedAmount;
    }

    public void setRefundedAmount(Object refundedAmount) {
        this.refundedAmount = refundedAmount;
    }

    public Integer getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(Integer remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public Integer getTax() {
        return tax;
    }

    public void setTax(Integer tax) {
        this.tax = tax;
    }

    public Integer getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(Integer deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Integer orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getSurgeAmount() {
        return surgeAmount;
    }

    public void setSurgeAmount(Integer surgeAmount) {
        this.surgeAmount = surgeAmount;
    }

    public Integer getIsJobRated() {
        return isJobRated;
    }

    public void setIsJobRated(Integer isJobRated) {
        this.isJobRated = isJobRated;
    }

    public Integer getIsRatingDeleted() {
        return isRatingDeleted;
    }

    public void setIsRatingDeleted(Integer isRatingDeleted) {
        this.isRatingDeleted = isRatingDeleted;
    }

    public Object getCustomerRating() {
        return customerRating;
    }

    public void setCustomerRating(Object customerRating) {
        this.customerRating = customerRating;
    }

    public String getCustomerComment() {
        return customerComment;
    }

    public void setCustomerComment(String customerComment) {
        this.customerComment = customerComment;
    }

    public Integer getPdOrAppointment() {
        return pdOrAppointment;
    }

    public void setPdOrAppointment(Integer pdOrAppointment) {
        this.pdOrAppointment = pdOrAppointment;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getMerchantEmail() {
        return merchantEmail;
    }

    public void setMerchantEmail(String merchantEmail) {
        this.merchantEmail = merchantEmail;
    }

    public String getMerchantPhoneNumber() {
        return merchantPhoneNumber;
    }

    public void setMerchantPhoneNumber(String merchantPhoneNumber) {
        this.merchantPhoneNumber = merchantPhoneNumber;
    }

    public Integer getAdditionalCharges() {
        return additionalCharges;
    }

    public void setAdditionalCharges(Integer additionalCharges) {
        this.additionalCharges = additionalCharges;
    }

    public Object getAdditionalChargeLabel() {
        return additionalChargeLabel;
    }

    public void setAdditionalChargeLabel(Object additionalChargeLabel) {
        this.additionalChargeLabel = additionalChargeLabel;
    }

    public Integer getOverallTransactionStatus() {
        return overallTransactionStatus;
    }

    public void setOverallTransactionStatus(Integer overallTransactionStatus) {
        this.overallTransactionStatus = overallTransactionStatus;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getStoreNameJson() {
        return storeNameJson;
    }

    public void setStoreNameJson(String storeNameJson) {
        this.storeNameJson = storeNameJson;
    }

    public Integer getMerchantIsDeleted() {
        return merchantIsDeleted;
    }

    public void setMerchantIsDeleted(Integer merchantIsDeleted) {
        this.merchantIsDeleted = merchantIsDeleted;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantAddress() {
        return merchantAddress;
    }

    public void setMerchantAddress(String merchantAddress) {
        this.merchantAddress = merchantAddress;
    }

    public String getMerchantDisplayAddress() {
        return merchantDisplayAddress;
    }

    public void setMerchantDisplayAddress(String merchantDisplayAddress) {
        this.merchantDisplayAddress = merchantDisplayAddress;
    }

    public String getMerchantLatitude() {
        return merchantLatitude;
    }

    public void setMerchantLatitude(String merchantLatitude) {
        this.merchantLatitude = merchantLatitude;
    }

    public String getMerchantLongitude() {
        return merchantLongitude;
    }

    public void setMerchantLongitude(String merchantLongitude) {
        this.merchantLongitude = merchantLongitude;
    }

    public Integer getIsCancelAllowed() {
        return isCancelAllowed;
    }

    public void setIsCancelAllowed(Integer isCancelAllowed) {
        this.isCancelAllowed = isCancelAllowed;
    }

    public Integer getTip() {
        return tip;
    }

    public void setTip(Integer tip) {
        this.tip = tip;
    }

    public Integer getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(Integer deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public Integer getIsCustomOrder() {
        return isCustomOrder;
    }

    public void setIsCustomOrder(Integer isCustomOrder) {
        this.isCustomOrder = isCustomOrder;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public Integer getIsUrgentDelivery() {
        return isUrgentDelivery;
    }

    public void setIsUrgentDelivery(Integer isUrgentDelivery) {
        this.isUrgentDelivery = isUrgentDelivery;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    public Integer getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(Integer transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getJobPickupLatitude() {
        return jobPickupLatitude;
    }

    public void setJobPickupLatitude(String jobPickupLatitude) {
        this.jobPickupLatitude = jobPickupLatitude;
    }

    public String getJobPickupLongitude() {
        return jobPickupLongitude;
    }

    public void setJobPickupLongitude(String jobPickupLongitude) {
        this.jobPickupLongitude = jobPickupLongitude;
    }

    public String getJobLatitude() {
        return jobLatitude;
    }

    public void setJobLatitude(String jobLatitude) {
        this.jobLatitude = jobLatitude;
    }

    public String getJobLongitude() {
        return jobLongitude;
    }

    public void setJobLongitude(String jobLongitude) {
        this.jobLongitude = jobLongitude;
    }

    public Object getFuguChannelId() {
        return fuguChannelId;
    }

    public void setFuguChannelId(Object fuguChannelId) {
        this.fuguChannelId = fuguChannelId;
    }

    public Object getIsPickupAnywhere() {
        return isPickupAnywhere;
    }

    public void setIsPickupAnywhere(Object isPickupAnywhere) {
        this.isPickupAnywhere = isPickupAnywhere;
    }

    public String getJobPickupEmail() {
        return jobPickupEmail;
    }

    public void setJobPickupEmail(String jobPickupEmail) {
        this.jobPickupEmail = jobPickupEmail;
    }

    public String getJobPickupName() {
        return jobPickupName;
    }

    public void setJobPickupName(String jobPickupName) {
        this.jobPickupName = jobPickupName;
    }

    public String getCustomPickupAddress() {
        return customPickupAddress;
    }

    public void setCustomPickupAddress(String customPickupAddress) {
        this.customPickupAddress = customPickupAddress;
    }

    public String getCustomPickupLatitude() {
        return customPickupLatitude;
    }

    public void setCustomPickupLatitude(String customPickupLatitude) {
        this.customPickupLatitude = customPickupLatitude;
    }

    public String getCustomPickupLongitude() {
        return customPickupLongitude;
    }

    public void setCustomPickupLongitude(String customPickupLongitude) {
        this.customPickupLongitude = customPickupLongitude;
    }

    public Integer getMerchantModule() {
        return merchantModule;
    }

    public void setMerchantModule(Integer merchantModule) {
        this.merchantModule = merchantModule;
    }

    public Integer getAdminSettingForMerchant() {
        return adminSettingForMerchant;
    }

    public void setAdminSettingForMerchant(Integer adminSettingForMerchant) {
        this.adminSettingForMerchant = adminSettingForMerchant;
    }

    public Object getDeliveryByMerchant() {
        return deliveryByMerchant;
    }

    public void setDeliveryByMerchant(Object deliveryByMerchant) {
        this.deliveryByMerchant = deliveryByMerchant;
    }

    public Integer getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(Integer jobStatus) {
        this.jobStatus = jobStatus;
    }

    public Integer getCancelOrderAdmin() {
        return cancelOrderAdmin;
    }

    public void setCancelOrderAdmin(Integer cancelOrderAdmin) {
        this.cancelOrderAdmin = cancelOrderAdmin;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public Integer getCustomerIsDeleted() {
        return customerIsDeleted;
    }

    public void setCustomerIsDeleted(Integer customerIsDeleted) {
        this.customerIsDeleted = customerIsDeleted;
    }

    public Integer getIsCashOnDelivery() {
        return isCashOnDelivery;
    }

    public void setIsCashOnDelivery(Integer isCashOnDelivery) {
        this.isCashOnDelivery = isCashOnDelivery;
    }

    public Object getEnabledPaymentGateways() {
        return enabledPaymentGateways;
    }

    public void setEnabledPaymentGateways(Object enabledPaymentGateways) {
        this.enabledPaymentGateways = enabledPaymentGateways;
    }

    public Integer getHoldAmount() {
        return holdAmount;
    }

    public void setHoldAmount(Integer holdAmount) {
        this.holdAmount = holdAmount;
    }

    public Object getTookanScheduledDatetime() {
        return tookanScheduledDatetime;
    }

    public void setTookanScheduledDatetime(Object tookanScheduledDatetime) {
        this.tookanScheduledDatetime = tookanScheduledDatetime;
    }

    public Integer getTookanActiveWhenJobCreated() {
        return tookanActiveWhenJobCreated;
    }

    public void setTookanActiveWhenJobCreated(Integer tookanActiveWhenJobCreated) {
        this.tookanActiveWhenJobCreated = tookanActiveWhenJobCreated;
    }

    public Integer getMerchantEarning() {
        return merchantEarning;
    }

    public void setMerchantEarning(Integer merchantEarning) {
        this.merchantEarning = merchantEarning;
    }

    public Integer getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(Integer commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public String getCommissionPayoutStatus() {
        return commissionPayoutStatus;
    }

    public void setCommissionPayoutStatus(String commissionPayoutStatus) {
        this.commissionPayoutStatus = commissionPayoutStatus;
    }

    public Integer getCustomerRatingByMerchant() {
        return customerRatingByMerchant;
    }

    public void setCustomerRatingByMerchant(Integer customerRatingByMerchant) {
        this.customerRatingByMerchant = customerRatingByMerchant;
    }

    public String getCustomerReviewByMerchant() {
        return customerReviewByMerchant;
    }

    public void setCustomerReviewByMerchant(String customerReviewByMerchant) {
        this.customerReviewByMerchant = customerReviewByMerchant;
    }

    public Integer getOrderPreparationTime() {
        return orderPreparationTime;
    }

    public void setOrderPreparationTime(Integer orderPreparationTime) {
        this.orderPreparationTime = orderPreparationTime;
    }

    public Integer getIsScheduled() {
        return isScheduled;
    }

    public void setIsScheduled(Integer isScheduled) {
        this.isScheduled = isScheduled;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public Object getMerchantEnabledPaymentMethods() {
        return merchantEnabledPaymentMethods;
    }

    public void setMerchantEnabledPaymentMethods(Object merchantEnabledPaymentMethods) {
        this.merchantEnabledPaymentMethods = merchantEnabledPaymentMethods;
    }

    public Integer getMerchantPaymentMethods() {
        return merchantPaymentMethods;
    }

    public void setMerchantPaymentMethods(Integer merchantPaymentMethods) {
        this.merchantPaymentMethods = merchantPaymentMethods;
    }

    public Object getMerchantCurrencyId() {
        return merchantCurrencyId;
    }

    public void setMerchantCurrencyId(Object merchantCurrencyId) {
        this.merchantCurrencyId = merchantCurrencyId;
    }

    public Integer getTransactionCharges() {
        return transactionCharges;
    }

    public void setTransactionCharges(Integer transactionCharges) {
        this.transactionCharges = transactionCharges;
    }

    public Integer getDebtAmount() {
        return debtAmount;
    }

    public void setDebtAmount(Integer debtAmount) {
        this.debtAmount = debtAmount;
    }

    public String getOrderCurrencySymbol() {
        return orderCurrencySymbol;
    }

    public void setOrderCurrencySymbol(String orderCurrencySymbol) {
        this.orderCurrencySymbol = orderCurrencySymbol;
    }

    public Integer getDeliveryChargeSurgeAmount() {
        return deliveryChargeSurgeAmount;
    }

    public void setDeliveryChargeSurgeAmount(Integer deliveryChargeSurgeAmount) {
        this.deliveryChargeSurgeAmount = deliveryChargeSurgeAmount;
    }

    public Integer getIsMenuEnabled() {
        return isMenuEnabled;
    }

    public void setIsMenuEnabled(Integer isMenuEnabled) {
        this.isMenuEnabled = isMenuEnabled;
    }

    public Object getCustomPickupName() {
        return customPickupName;
    }

    public void setCustomPickupName(Object customPickupName) {
        this.customPickupName = customPickupName;
    }

    public Object getCustomPickupPhone() {
        return customPickupPhone;
    }

    public void setCustomPickupPhone(Object customPickupPhone) {
        this.customPickupPhone = customPickupPhone;
    }

    public Object getCustomPickupEmail() {
        return customPickupEmail;
    }

    public void setCustomPickupEmail(Object customPickupEmail) {
        this.customPickupEmail = customPickupEmail;
    }

    public String getcPhone() {
        return cPhone;
    }

    public void setcPhone(String cPhone) {
        this.cPhone = cPhone;
    }

    public String getjAddress() {
        return jAddress;
    }

    public void setjAddress(String jAddress) {
        this.jAddress = jAddress;
    }

    public String getcEmail() {
        return cEmail;
    }

    public void setcEmail(String cEmail) {
        this.cEmail = cEmail;
    }

    public String getcUsername() {
        return cUsername;
    }

    public void setcUsername(String cUsername) {
        this.cUsername = cUsername;
    }

    public Object getSochitelReferenceId() {
        return sochitelReferenceId;
    }

    public void setSochitelReferenceId(Object sochitelReferenceId) {
        this.sochitelReferenceId = sochitelReferenceId;
    }

    public Object getSochitelRemark() {
        return sochitelRemark;
    }

    public void setSochitelRemark(Object sochitelRemark) {
        this.sochitelRemark = sochitelRemark;
    }

    public Integer getAdditionalCharge() {
        return additionalCharge;
    }

    public void setAdditionalCharge(Integer additionalCharge) {
        this.additionalCharge = additionalCharge;
    }

    public AdditionalChargesJson getAdditionalChargesJson() {
        return additionalChargesJson;
    }

    public void setAdditionalChargesJson(AdditionalChargesJson additionalChargesJson) {
        this.additionalChargesJson = additionalChargesJson;
    }

    public Integer getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(Integer taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getAdjustedAmount() {
        return adjustedAmount;
    }

    public void setAdjustedAmount(Integer adjustedAmount) {
        this.adjustedAmount = adjustedAmount;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getJobAddress() {
        return jobAddress;
    }

    public void setJobAddress(String jobAddress) {
        this.jobAddress = jobAddress;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Integer getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Integer paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Object getCustomTagForMerchant() {
        return customTagForMerchant;
    }

    public void setCustomTagForMerchant(Object customTagForMerchant) {
        this.customTagForMerchant = customTagForMerchant;
    }

    public Integer getIsEditAllowed() {
        return isEditAllowed;
    }

    public void setIsEditAllowed(Integer isEditAllowed) {
        this.isEditAllowed = isEditAllowed;
    }

    public List<Object> getUserTaxes() {
        return userTaxes;
    }

    public void setUserTaxes(List<Object> userTaxes) {
        this.userTaxes = userTaxes;
    }

    public Integer getEditJobStatus() {
        return editJobStatus;
    }

    public void setEditJobStatus(Integer editJobStatus) {
        this.editJobStatus = editJobStatus;
    }

    public Integer getShowStatus() {
        return showStatus;
    }

    public void setShowStatus(Integer showStatus) {
        this.showStatus = showStatus;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Object getTrackingLink() {
        return trackingLink;
    }

    public void setTrackingLink(Object trackingLink) {
        this.trackingLink = trackingLink;
    }

    public Integer getCancelAllowed() {
        return cancelAllowed;
    }

    public void setCancelAllowed(Integer cancelAllowed) {
        this.cancelAllowed = cancelAllowed;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Object getReason() {
        return reason;
    }

    public void setReason(Object reason) {
        this.reason = reason;
    }

    public Object getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(Object cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public Object getTookanJobHash() {
        return tookanJobHash;
    }

    public void setTookanJobHash(Object tookanJobHash) {
        this.tookanJobHash = tookanJobHash;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public Object getAmountAddByAgent() {
        return amountAddByAgent;
    }

    public void setAmountAddByAgent(Object amountAddByAgent) {
        this.amountAddByAgent = amountAddByAgent;
    }

    public Object getPickupBarcode() {
        return pickupBarcode;
    }

    public void setPickupBarcode(Object pickupBarcode) {
        this.pickupBarcode = pickupBarcode;
    }

    public Object getDeliveryBarcode() {
        return deliveryBarcode;
    }

    public void setDeliveryBarcode(Object deliveryBarcode) {
        this.deliveryBarcode = deliveryBarcode;
    }

    public Object getJobOtp() {
        return jobOtp;
    }

    public void setJobOtp(Object jobOtp) {
        this.jobOtp = jobOtp;
    }

    public String getHippoTransactionId() {
        return hippoTransactionId;
    }

    public void setHippoTransactionId(String hippoTransactionId) {
        this.hippoTransactionId = hippoTransactionId;
    }

    public List<String> getGroupingTags() {
        return groupingTags;
    }

    public void setGroupingTags(List<String> groupingTags) {
        this.groupingTags = groupingTags;
    }

}
