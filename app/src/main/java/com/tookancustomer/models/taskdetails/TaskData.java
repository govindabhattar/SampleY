package com.tookancustomer.models.taskdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.checkoutTemplate.model.Template;
import com.tookancustomer.models.PromosModel;
import com.tookancustomer.models.TaxesModel;
import com.tookancustomer.models.alltaskdata.OrderDetails;
import com.tookancustomer.models.billbreakdown.AdditionalCharges;
import com.tookancustomer.models.userdata.PaymentMethod;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TaskData implements Serializable {
    @SerializedName("DELIVERY_CHARGE_SURGE_AMOUNT")
    @Expose
    private BigDecimal deliverySurgeAmount;
    @SerializedName("ADDITIONAL_AMOUNT")
    @Expose
    private BigDecimal additionalAmount;
    @SerializedName("TAXABLE_AMOUNT")
    @Expose
    private BigDecimal taxableAmount;
    @SerializedName("additional_charge")
    @Expose
    private BigDecimal AdditionalCharge;
    @SerializedName("additional_charges_json")
    @Expose
    private AdditionalCharges additionalCharges;
    @SerializedName("custom_pickup_name")
    @Expose
    private String customPickupName = "";
    @SerializedName("custom_pickup_email")
    @Expose
    private String customPickupEmail = "";
    @SerializedName("custom_pickup_phone")
    @Expose
    private String customPickupPhone = "";
    @SerializedName("custom_pickup_address")
    @Expose
    private String customPickupAddress = "";
    @SerializedName("transaction_id")
    @Expose
    private String transactionId = "";
    @SerializedName("transaction_status")
    @Expose
    private int transactionStatus = 0; // transaction status value tells whether the order is paid or not
    @SerializedName("overall_transaction_status")
    @Expose
    private int overallTransactionStatus = 2;
    @SerializedName("payment_method")
    @Expose
    private long paymentType = 8;
    @SerializedName("payment_name")
    @Expose
    private String paymentName;
    @SerializedName("delivery_method")
    @Expose
    private int delivery_method = 2; //Home delivery=2 and Self pickup=4
    @SerializedName("job_details_by_fleet")
    @Expose
    private Object jobDetailsByFleet;
    @SerializedName("fleet_id")
    @Expose
    private Object fleetId;
    @SerializedName("merchant_id")
    @Expose
    private long merchantId;
    @SerializedName("order_id")
    @Expose
    private Object orderId;
    @SerializedName("fleet_phone")
    @Expose
    private String fleetPhone;
    @SerializedName("fleet_image")
    @Expose
    private String fleetImage;
    @SerializedName("fleet_vehicle_color")
    @Expose
    private String fleetVehicleColor;
    @SerializedName("fleet_license")
    @Expose
    private String fleetLicense;
    @SerializedName("fleet_vehicle_description")
    @Expose
    private String fleetVehicleDescription;
    @SerializedName("fleet_rating")
    @Expose
    private String fleetRating;
    @SerializedName("fleet_vehicle_type")
    @Expose
    private int fleetVehicleType;
    @SerializedName("form_id")
    @Expose
    private int formId;
    @SerializedName("task_type")
    @Expose
    private int taskType = 0; //here 1 means pickup task and 2 means delivery task for laundry
    @SerializedName("business_type")
    @Expose
    private int businessType = 1;
    @SerializedName("pd_or_appointment")
    @Expose
    private int pd_or_appointment = 1;
    @SerializedName("fleet_latitude")
    @Expose
    private String fleetLatitude;
    @SerializedName("fleet_longitude")
    @Expose
    private String fleetLongitude;
    @SerializedName("fleet_name")
    @Expose
    private String fleetName;
    @SerializedName("team_name")
    @Expose
    private Object teamName;
    @SerializedName("domain")
    @Expose
    private Object domain;
    @SerializedName("recurring_id")
    @Expose
    private String recurringId;
    @SerializedName("total_distance_travelled")
    @Expose
    private String totalDistanceTravelled;
    @SerializedName("total_time_spent_at_task_till_completion")
    @Expose
    private String totalTimeTaken;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("job_vertical")
    @Expose
    private Integer jobVertical;
    @SerializedName("is_job_rated")
    @Expose
    private Integer isJobRated;
    @SerializedName("customer_username")
    @Expose
    private String customerUsername;
    @SerializedName("customer_phone")
    @Expose
    private String customerPhone;
    @SerializedName("customer_email")
    @Expose
    private String customerEmail;
    @SerializedName("tags")
    @Expose
    private String tags;
    @SerializedName("geofence")
    @Expose
    private Integer geofence;
    @SerializedName("job_hash")
    @Expose
    private String jobHash;
    @SerializedName("team_id")
    @Expose
    private Integer teamId;
    @SerializedName("job_type")
    @Expose
    private Integer jobType;
    @SerializedName("is_customer_rated")
    @Expose
    private Integer isCustomerRated;
    @SerializedName("customer_comment")
    @Expose
    private String customerComment = "";
    @SerializedName("customer_rating")
    @Expose
    private Number customerRating = 0;
    @SerializedName("job_pickup_email")
    @Expose
    private String jobPickupEmail;
    @SerializedName("job_pickup_name")
    @Expose
    private String jobPickupName;
    @SerializedName("job_pickup_phone")
    @Expose
    private String jobPickupPhone;
    @SerializedName("job_latitude")
    @Expose
    private String jobLatitude;
    @SerializedName("job_longitude")
    @Expose
    private String jobLongitude;
    @SerializedName("job_address")
    @Expose
    private String jobAddress;
    @SerializedName("job_status")
    @Expose
    private int jobStatus = -1;
    @SerializedName("job_description")
    @Expose
    private String jobDescription;
    @SerializedName("has_pickup")
    @Expose
    private Integer hasPickup;
    @SerializedName("completed_by_admin")
    @Expose
    private Integer completedByAdmin;
    @SerializedName("pickup_delivery_relationship")
    @Expose
    private String pickupDeliveryRelationship;
    @SerializedName("job_pickup_datetime")
    @Expose
    private String jobPickupDatetime;
    @SerializedName("job_id")
    @Expose
    private Integer jobId;
    @SerializedName("job_delivery_datetime")
    @Expose
    private String jobDeliveryDatetime;
    @SerializedName("job_pickup_latitude")
    @Expose
    private String jobPickupLatitude;
    @SerializedName("job_pickup_longitude")
    @Expose
    private String jobPickupLongitude;
    @SerializedName("job_pickup_address")
    @Expose
    private String jobPickupAddress;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("started_datetime")
    @Expose
    private String startedDatetime;
    @SerializedName("completed_datetime")
    @Expose
    private String completedDatetime;
    @SerializedName("open_tracking_link")
    @Expose
    private Integer openTrackingLink;
    @SerializedName("arrived_datetime")
    @Expose
    private String arrivedDatetime;
    @SerializedName("acknowledged_datetime")
    @Expose
    private String acknowledgedDatetime;
    @SerializedName("partner_order_id")
    @Expose
    private Object partnerOrderId;
    @SerializedName("link_task")
    @Expose
    private LinkTask linkTask;
    @SerializedName("fields")
    @Expose
    private Fields fields;
    @SerializedName("task_history")
    @Expose
    private List<TaskHistory> taskHistory = null;
    @SerializedName("fleet_movement")
    @Expose
    private List<FleetMovement> fleetMovement = null;
    @SerializedName("orderDetails")
    @Expose
    private List<OrderDetails> orderDetails = new ArrayList<>();
    @SerializedName("show_status")
    @Expose
    private int showStatus;
    @SerializedName("cancel_allowed")
    @Expose
    private int cancelAllowed = 0;
    @SerializedName("merchant_name")
    @Expose
    private String merchantName;
    @SerializedName("is_custom_order")
    @Expose
    private int isCustomOrder;
    @SerializedName("is_pickup_anywhere")
    @Expose
    private int isPickupAnywhere = 0;
    @SerializedName("tax")
    @Expose
    private BigDecimal tax;
    @SerializedName("delivery_charge")
    @Expose
    private BigDecimal deliveryCharge;
    @SerializedName("tip")
    @Expose
    private BigDecimal tip;
    @SerializedName("coupon_discount")
    @Expose
    private BigDecimal couponDiscount;
    @SerializedName("total_amount")
    @Expose
    private BigDecimal totalAmount;
    @SerializedName("surge_amount")
    @Expose
    private BigDecimal surgeAmount;
    @SerializedName("order_amount")
    @Expose
    private BigDecimal orderAmount;
    @SerializedName("user_taxes")
    @Expose
    private ArrayList<TaxesModel> userTaxes = new ArrayList<>();
    @SerializedName("delivery_taxes")
    @Expose
    private ArrayList<TaxesModel> deliveryTaxes = new ArrayList<>();
    @SerializedName("tracking_link")
    @Expose
    private String trackingLink = "";
    @SerializedName("enable_start_time_end_time")
    @Expose
    private Integer isStartEndTimeEnable = 0;
    @SerializedName("hippo_transaction_id")
    @Expose
    private String hippoTransactionId = "";
    @SerializedName("grouping_tags")
    @Expose
    private ArrayList<String> groupingTags;
    @SerializedName("return_enabled")
    @Expose
    private Integer return_enabled = 0;
    @SerializedName("checkout_template")
    @Expose
    private ArrayList<Template> checkoutTemplate;
    @SerializedName("loyalty_points")
    @Expose
    private LoyaltyPoints loyaltyPoints;
    @SerializedName("is_cancelation_policy_enabled")
    @Expose
    private int isCancelationPolicyEnabled;
    @SerializedName("remaining_balance")
    @Expose
    private BigDecimal remainingBalance = new BigDecimal(0);
    @SerializedName("promoList")
    @Expose
    private ArrayList<PromosModel> promoList = new ArrayList<>();
    @SerializedName("isEditedTask")
    @Expose
    private int isEditedTask;
    @SerializedName("refunded_amount")
    @Expose
    private double refundedAmount;
    @SerializedName("tookan_job_hash")
    @Expose
    private String tookanJobHash;
    @SerializedName("payment_methods")
    @Expose
    private List<PaymentMethod> paymentMethods = new ArrayList<>();
    @SerializedName("agent_info")
    @Expose
    private AgentInfo agentInfo;
    @SerializedName("transaction_charges")
    @Expose
    private double transaction_charges;
    @SerializedName("order_currency_symbol")
    @Expose
    private String orderCurrencySymbol;
    @SerializedName("delivery_charge_surge_amount")
    @Expose
    private BigDecimal deliveryChargeSurgeAmount;

    public ArrayList<TaxesModel> getDeliveryTaxes() {
        return deliveryTaxes;
    }

    public BigDecimal getDeliverySurgeAmount() {
        return deliverySurgeAmount;
    }

    public void setDeliverySurgeAmount(BigDecimal deliverySurgeAmount) {
        this.deliverySurgeAmount = deliverySurgeAmount;
    }

    public BigDecimal getAdditionalAmount() {
        return additionalAmount;
    }

    public void setAdditionalAmount(BigDecimal additionalAmount) {
        this.additionalAmount = additionalAmount;
    }

    public BigDecimal getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(BigDecimal taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public BigDecimal getAdditionalCharge() {
        return AdditionalCharge;
    }

    public void setAdditionalCharge(BigDecimal additionalCharge) {
        AdditionalCharge = additionalCharge;
    }

    public AdditionalCharges getAdditionalCharges() {
        return additionalCharges;
    }

    public void setAdditionalCharges(AdditionalCharges additionalCharges) {
        this.additionalCharges = additionalCharges;
    }

    public AgentInfo getAgentInfo() {
        return agentInfo;
    }

    public void setAgentInfo(AgentInfo agentInfo) {
        this.agentInfo = agentInfo;
    }

    public String getOrderCurrencySymbol() {
        return (orderCurrencySymbol != null && !orderCurrencySymbol.isEmpty()) ? orderCurrencySymbol : Utils.getCurrencySymbol();
    }

    public void setOrderCurrencySymbol(String orderCurrencySymbol) {
        this.orderCurrencySymbol = orderCurrencySymbol;
    }

    public BigDecimal getDeliveryChargeSurgeAmount() {
        return deliveryChargeSurgeAmount;
    }

    public void setDeliveryChargeSurgeAmount(BigDecimal deliveryChargeSurgeAmount) {
        this.deliveryChargeSurgeAmount = deliveryChargeSurgeAmount;
    }

    public double getTransaction_charges() {
        return transaction_charges;
    }

    public List<PaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public LoyaltyPoints getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(LoyaltyPoints loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public String getTrackingLink() {
        return trackingLink != null ? trackingLink : "";
    }

    public void setTrackingLink(String trackingLink) {
        this.trackingLink = trackingLink;
    }


    public ArrayList<TaxesModel> getUserTaxes() {
        return userTaxes;
    }

    public void setUserTaxes(ArrayList<TaxesModel> userTaxes) {
        this.userTaxes = userTaxes;
    }

    public String getTax() {
        return tax != null ? Utils.getDoubleTwoDigits(tax) : "0";
    }

    public String getDeliveryCharge() {
        return deliveryCharge != null ? Utils.getDoubleTwoDigits(deliveryCharge) : "0";
    }

    public String getTip() {
        return tip != null ? Utils.getDoubleTwoDigits(tip) : "0";
    }

    public String getCouponDiscount() {
        return couponDiscount != null ? Utils.getDoubleTwoDigits(couponDiscount) : "0";
    }

    public String getTotalAmount() {
        return totalAmount != null ? Utils.getDoubleTwoDigits(totalAmount) : "0";
    }

    public BigDecimal getSurgeAmount() {
        if (surgeAmount == null)
            return BigDecimal.valueOf(0);
        return surgeAmount;
    }

    public String getOrderAmount() {
        return orderAmount != null ? Utils.getDoubleTwoDigits(orderAmount) : "0";
    }

    public String getJobAddress() {
        return jobAddress;
    }

    public void setJobAddress(String jobAddress) {
        this.jobAddress = jobAddress;
    }

    public int getShowStatus() {
        return showStatus;
    }

    public void setShowStatus(int showStatus) {
        this.showStatus = showStatus;
    }

    public Integer getJobVertical() {
        return jobVertical;
    }

    public void setJobVertical(Integer jobVertical) {
        this.jobVertical = jobVertical;
    }

    public String getFleetLicense() {
        return fleetLicense;
    }

    public void setFleetLicense(String fleetLicense) {
        this.fleetLicense = fleetLicense;
    }

    public String getFleetLatitude() {
        return fleetLatitude;
    }

    public void setFleetLatitude(String fleetLatitude) {
        this.fleetLatitude = fleetLatitude;
    }

    public String getFleetLongitude() {
        return fleetLongitude;
    }

    public void setFleetLongitude(String fleetLongitude) {
        this.fleetLongitude = fleetLongitude;
    }

    public Object getJobDetailsByFleet() {
        return jobDetailsByFleet;
    }

    public void setJobDetailsByFleet(Object jobDetailsByFleet) {
        this.jobDetailsByFleet = jobDetailsByFleet;
    }

    public Object getFleetId() {
        return fleetId;
    }

    public void setFleetId(Object fleetId) {
        this.fleetId = fleetId;
    }

    public int getFormId() {
        return formId;
    }

    public void setFormId(int formId) {
        this.formId = formId;
    }

    public String getFleetName() {
        return fleetName;
    }

    public void setFleetName(String fleetName) {
        this.fleetName = fleetName;
    }

    public Object getTeamName() {
        return teamName;
    }

    public void setTeamName(Object teamName) {
        this.teamName = teamName;
    }

    public Object getDomain() {
        return domain;
    }

    public void setDomain(Object domain) {
        this.domain = domain;
    }

    public String getRecurringId() {
        return recurringId;
    }

    public void setRecurringId(String recurringId) {
        this.recurringId = recurringId;
    }

    public String getTotalDistanceTravelled() {
        return totalDistanceTravelled;
    }

    public void setTotalDistanceTravelled(String totalDistanceTravelled) {
        this.totalDistanceTravelled = totalDistanceTravelled;
    }

    public long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(long merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCustomerUsername() {
        return jobType == Constants.TaskType.PICK_UP.value ? jobPickupName : customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    public String getCustomerPhone() {
        return jobType == Constants.TaskType.PICK_UP.value ? jobPickupPhone : customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public String getCustomerEmail() {
        return jobType == Constants.TaskType.PICK_UP.value ? jobPickupEmail : customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getGeofence() {
        return geofence;
    }

    public void setGeofence(Integer geofence) {
        this.geofence = geofence;
    }

    public String getJobHash() {
        return jobHash;
    }

    public void setJobHash(String jobHash) {
        this.jobHash = jobHash;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public Integer getJobType() {
        return jobType;
    }

    public void setJobType(Integer jobType) {
        this.jobType = jobType;
    }

    public Integer getIsCustomerRated() {
        return isCustomerRated;
    }

    public void setIsCustomerRated(Integer isCustomerRated) {
        this.isCustomerRated = isCustomerRated;
    }

    public String getCustomerComment() {
        return customerComment;
    }

    public void setCustomerComment(String customerComment) {
        this.customerComment = customerComment;
    }

    public Number getCustomerRating() {
        return customerRating;
    }

    public void setCustomerRating(Number customerRating) {
        this.customerRating = customerRating;
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

    public String getJobPickupPhone() {
        return jobPickupPhone;
    }

    public void setJobPickupPhone(String jobPickupPhone) {
        this.jobPickupPhone = jobPickupPhone;
    }

    public Double getJobLatitude() {
        return Double.parseDouble(jobType == Constants.TaskType.PICK_UP.value ? jobPickupLatitude : jobLatitude);
    }

    public void setJobLatitude(String jobLatitude) {
        this.jobLatitude = jobLatitude;
    }

    public Double getJobLongitude() {
        return Double.parseDouble(jobType == Constants.TaskType.PICK_UP.value ? jobPickupLongitude : jobLongitude);
    }

    public void setJobLongitude(String jobLongitude) {
        this.jobLongitude = jobLongitude;
    }

    public String getDeliveryJobAddress() {
        return jobAddress;
    }

    public int getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(int jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public Integer getHasPickup() {
        return hasPickup;
    }

    public void setHasPickup(Integer hasPickup) {
        this.hasPickup = hasPickup;
    }

    public Integer getCompletedByAdmin() {
        return completedByAdmin;
    }

    public void setCompletedByAdmin(Integer completedByAdmin) {
        this.completedByAdmin = completedByAdmin;
    }

    public String getPickupDeliveryRelationship() {
        return pickupDeliveryRelationship;
    }

    public void setPickupDeliveryRelationship(String pickupDeliveryRelationship) {
        this.pickupDeliveryRelationship = pickupDeliveryRelationship;
    }

    public String getJobPickupDatetime() {
        return jobPickupDatetime;
    }

    public void setJobPickupDatetime(String jobPickupDatetime) {
        this.jobPickupDatetime = jobPickupDatetime;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getJobDeliveryDatetime() {
        return jobDeliveryDatetime;
    }

    public void setJobDeliveryDatetime(String jobDeliveryDatetime) {
        this.jobDeliveryDatetime = jobDeliveryDatetime;
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

    public String getJobPickupAddress() {
        return jobPickupAddress != null ? jobPickupAddress : "";
    }

    public void setJobPickupAddress(String jobPickupAddress) {
        this.jobPickupAddress = jobPickupAddress;
    }

    public String getStartedDatetime() {
        return startedDatetime;
    }

    public void setStartedDatetime(String startedDatetime) {
        this.startedDatetime = startedDatetime;
    }

    public String getCompletedDatetime() {
        return completedDatetime;
    }

    public void setCompletedDatetime(String completedDatetime) {
        this.completedDatetime = completedDatetime;
    }

    public Integer getOpenTrackingLink() {
        return openTrackingLink;
    }

    public void setOpenTrackingLink(Integer openTrackingLink) {
        this.openTrackingLink = openTrackingLink;
    }

    public String getArrivedDatetime() {
        return arrivedDatetime;
    }

    public void setArrivedDatetime(String arrivedDatetime) {
        this.arrivedDatetime = arrivedDatetime;
    }

    public String getAcknowledgedDatetime() {
        return acknowledgedDatetime;
    }

    public void setAcknowledgedDatetime(String acknowledgedDatetime) {
        this.acknowledgedDatetime = acknowledgedDatetime;
    }

    public Object getPartnerOrderId() {
        return partnerOrderId;
    }

    public void setPartnerOrderId(Object partnerOrderId) {
        this.partnerOrderId = partnerOrderId;
    }

    public LinkTask getLinkTask() {
        return linkTask;
    }

    public void setLinkTask(LinkTask linkTask) {
        this.linkTask = linkTask;
    }

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }

    public List<TaskHistory> getTaskHistory() {
        return taskHistory;
    }

    public void setTaskHistory(List<TaskHistory> taskHistory) {
        this.taskHistory = taskHistory;
    }

    public List<FleetMovement> getFleetMovement() {
        return fleetMovement;
    }

    public void setFleetMovement(List<FleetMovement> fleetMovement) {
        this.fleetMovement = fleetMovement;
    }

    public String getFleetPhone() {
        return fleetPhone;
    }

    public void setFleetPhone(String fleetPhone) {
        this.fleetPhone = fleetPhone;
    }

    public String getFleetImage() {
        return fleetImage;
    }

    public void setFleetImage(String fleetImage) {
        this.fleetImage = fleetImage;
    }

    public String getFleetVehicleColor() {
        return fleetVehicleColor;
    }

    public void setFleetVehicleColor(String fleetVehicleColor) {
        this.fleetVehicleColor = fleetVehicleColor;
    }

    public String getFleetVehicleDescription() {
        return fleetVehicleDescription;
    }

    public void setFleetVehicleDescription(String fleetVehicleDescription) {
        this.fleetVehicleDescription = fleetVehicleDescription;
    }

    public String getFleetRating() {
        return fleetRating;
    }

    public void setFleetRating(String fleetRating) {
        this.fleetRating = fleetRating;
    }

    public int getFleetVehicleType() {
        return fleetVehicleType;
    }

    public void setFleetVehicleType(int fleetVehicleType) {
        this.fleetVehicleType = fleetVehicleType;
    }

    public String getTotalTimeTaken() {
        return totalTimeTaken;
    }

    public void setTotalTimeTaken(String totalTimeTaken) {
        this.totalTimeTaken = totalTimeTaken;
    }

    public boolean hasDelivery() {
        return linkTask != null && linkTask.getJobId() != null;
    }

    public boolean isRepayButtonShown() {
        return !(jobStatus == Constants.TaskStatus.DELIVERED.value || jobStatus == Constants.TaskStatus.REJECTED.value || jobStatus == Constants.TaskStatus.CANCELLED.value);

    }

    public List<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetails> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public String getMerchantName() {
        return merchantName != null ? merchantName : "";
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Integer getIsJobRated() {
        return isJobRated;  //isJobRated=0 --> not rated, =1--> rated , =2 --> skipped
    }

    public void setIsJobRated(Integer isJobRated) {
        this.isJobRated = isJobRated;
    }

    public boolean isShowRatingButton() {
        return (UIManager.getIsReviewRatingRequired() && jobStatus == Constants.TaskStatus.DELIVERED.value && (isJobRated == 0 || isJobRated == 2));
    }

    public boolean isRatingDialogForced() {
        return (UIManager.getIsReviewRatingRequired() && jobStatus == Constants.TaskStatus.DELIVERED.value && (isJobRated == 0));
    }

    public boolean showRatings() {
        return (UIManager.getIsReviewRatingRequired() && jobStatus == Constants.TaskStatus.DELIVERED.value && getCustomerRating().intValue() > 0);
    }

    public int getCancelAllowed() {
        return cancelAllowed;
    }

    public void setCancelAllowed(int cancelAllowed) {
        this.cancelAllowed = cancelAllowed;
    }

    public Object getOrderId() {
        return orderId;
    }

    public void setOrderId(Object orderId) {
        this.orderId = orderId;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getDeliveryMethod() {
        return delivery_method;
    }

    public void setDeliveryMethod(int delivery_method) {
        this.delivery_method = delivery_method;
    }

    public String getHippoTransactionId() {
        return hippoTransactionId;
    }

    public void setHippoTransactionId(String hippoTransactionId) {
        this.hippoTransactionId = hippoTransactionId;
    }

    public ArrayList<String> getGroupingTags() {
        return groupingTags;
    }

    public void setGroupingTags(ArrayList<String> groupingTags) {
        this.groupingTags = groupingTags;
    }

    public Integer getReturn_enabled() {
        return return_enabled;
    }

    public void setReturn_enabled(Integer return_enabled) {
        this.return_enabled = return_enabled;
    }

    public int getIsCustomOrder() {
        return isCustomOrder;
    }

    public void setIsCustomOrder(int isCustomOrder) {
        this.isCustomOrder = isCustomOrder;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public ArrayList<Template> getCheckoutTemplate() {
        return checkoutTemplate;
    }

    public void setCheckoutTemplate(ArrayList<Template> checkoutTemplate) {
        this.checkoutTemplate = checkoutTemplate;
    }

    public int getTransactionStatus() {
        return transactionStatus;
    }

    public String getTransactionId() {
        return transactionId != null ? transactionId : "";
    }

    public long getPaymentType() {
        return paymentType;
    }

    public int getIsCancelationPolicyEnabled() {
        return isCancelationPolicyEnabled;
    }

    public BigDecimal getRemainingBalance() {
        return remainingBalance != null ? remainingBalance : BigDecimal.valueOf(0.00);
    }

    public void setRemainingBalance(BigDecimal remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public int getOverallTransactionStatus() {
        return overallTransactionStatus;
    }

    public ArrayList<PromosModel> getPromoList() {
        return promoList;
    }

    public boolean getIsEditedTask() {
        return isEditedTask == 1;
    }

    public double getRefundedAmount() {
        return refundedAmount;
    }

    public String getTookanJobHash() {
        return tookanJobHash;
    }

    public int getIsPickupAnywhere() {
        return isPickupAnywhere;
    }

    public boolean getPdFlow() {

        return (customPickupAddress != null && !customPickupAddress.isEmpty());
    }

    public String getCustomPickupName() {
        return customPickupName;
    }

    public String getCustomPickupEmail() {
        return customPickupEmail;
    }

    public String getCustomPickupPhone() {
        return customPickupPhone;
    }

    public String getCustomPickupAddress() {
        return customPickupAddress;
    }
}
