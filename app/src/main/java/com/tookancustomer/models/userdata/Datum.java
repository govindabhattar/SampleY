package com.tookancustomer.models.userdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.models.NLevelWorkFlowModel.Button;
import com.tookancustomer.utility.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Datum implements Serializable {
    @SerializedName("form_id")
    @Expose
    private Integer formId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("domain_name")
    @Expose
    private String domainName;
    @SerializedName("work_flow")
    @Expose
    private Integer workFlow;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("pickup_delivery_flag")
    @Expose
    private Integer pickupDeliveryFlag;
    //    @SerializedName("payment_methods")
    @SerializedName("activePaymentMethods")
    @Expose
    private List<PaymentMethod> paymentMethods = new ArrayList<>();
    @SerializedName("force_pickup_delivery")
    @Expose
    private int forcePickupDelivery;
    @SerializedName("vertical")
    @Expose
    private Integer vertical;
    @SerializedName("product_view")
    @Expose
    private Integer productView;
    @SerializedName("pdp_view")
    @Expose
    private Integer pdpView;
    @SerializedName("pdp_view_templates")
    @Expose
    private int pdpViewTemplates;
    @SerializedName("instant_task")
    @Expose
    private Integer instantTask = 1;
    @SerializedName("scheduled_task")
    @Expose
    private Integer scheduledTask = 0;
    @SerializedName("buffer_schedule")
    @Expose
    private Number bufferSchedule = 5;
    @SerializedName("form_name")
    @Expose
    private String formName;
    @SerializedName("app_description")
    @Expose
    private String appDescription;
    @SerializedName("merchantMinimumOrder")
    @Expose
    private Double merchantMinimumOrder = 0.0;


    @SerializedName("minimum_self_pickup_amount")
    @Expose
    private Double minimum_self_pickup_amount = 0.0;

    @SerializedName("show_prefilled_data")
    @Expose
    private int isShowPrefilledData = 0;   // 1 empty  2 showPrefiled  3 hide prefiled
    @SerializedName("payment_settings")  //only in case of app_configurations
    @Expose
    private List<PaymentSettings> paymentSettings = new ArrayList<>();
    @SerializedName("is_review_rating_enabled")
    @Expose
    private Integer isReviewRatingEnabled = 0;
    @SerializedName("show_product_price")
    @Expose
    private Integer showProductPrice = 0;
    @SerializedName("is_fugu_chat_enabled")
    @Expose
    private int isFuguChatEnabled = 0;
    @SerializedName("fugu_chat_token")
    @Expose
    private String fuguChatToken = "";
    @SerializedName("show_outstocked_product")
    @Expose
    private Integer showOutStockedProduct = 0;
    @SerializedName("enable_tookan_agent")
    @Expose
    private Integer enableTookanAgent = 0;
    @SerializedName("button_type")
    @Expose
    public Button buttons;
    @SerializedName("business_type")
    @Expose
    private Integer businessType = 1; //1 for products and 2 for services
    @SerializedName("multiple_product_single_cart")
    @Expose
    private Integer multipleProductInSingleCart = 1; //1 for multi product selection and 2 for only single product can be added to cart
    @SerializedName("pd_or_appointment")
    @Expose
    private Integer pdOrAppointment = 1; //1 for pickup delivery flow and 2 for appointment flow
    @SerializedName("enable_start_time_end_time")
    @Expose
    private Integer isStartEndTimeEnable = 0; //0 for only start date and 1 for start end date
    @SerializedName("background_image")
    @Expose
    private String backgroundImage;
    @SerializedName("mobile_background_image")
    @Expose
    private String mobileBackgroundImage;
    @SerializedName("display_merchant_details_page")
    @Expose
    private int displayMerchantDetailsPage = 1;
    @SerializedName("display_merchant_phone")
    @Expose
    private int displayMerchantPhone = 1;
    @SerializedName("display_merchant_location")
    @Expose
    private int displayMerchantLocation = 1;
    @SerializedName("admin_home_delivery")
    @Expose
    private int homeDelivery = 1;
    @SerializedName("admin_self_pickup")
    @Expose
    private int selfPickup = 0;

    @SerializedName("admin_pick_and_drop")
    @Expose
    private int pickupAndDrop = 0;
    @SerializedName("is_banners_enabled")
    @Expose
    private int isBannersEnabled = 0;
    @SerializedName("is_business_category_enabled")
    @Expose
    private int isBusinessCategoryEnabled = 0;
    @SerializedName("is_menu_enabled")
    @Expose
    private int isMenuEnabled;

    @SerializedName("merchant_customer_rating")
    @Expose
    private int merchantCustomerRating;

    public int getMerchantCustomerRating() {
        return merchantCustomerRating;
    }

    private List<PaymentMethod> paymentMethodsForStore = new ArrayList<>();


    public Datum(Integer formId, Integer userId, String domainName, Integer workFlow, String logo, Integer pickupDeliveryFlag
            , int forcePickupDelivery, Integer vertical, Integer productView, Integer scheduledTask, Number bufferSchedule
            , String formName, List<PaymentSettings> paymentSettings, Integer isReviewRatingEnabled, Integer isFuguChatEnabled
            , String fuguChatToken) {
        this.formId = formId;
        this.userId = userId;
        this.domainName = domainName;
        this.workFlow = workFlow;
        this.logo = logo;
        this.pickupDeliveryFlag = pickupDeliveryFlag;
        this.forcePickupDelivery = forcePickupDelivery;
        this.vertical = vertical;
        this.productView = productView;
        this.scheduledTask = scheduledTask;
        this.bufferSchedule = bufferSchedule;
        this.formName = formName;
        this.paymentSettings = paymentSettings;
        this.isReviewRatingEnabled = isReviewRatingEnabled;
        this.isFuguChatEnabled = isFuguChatEnabled;
        this.fuguChatToken = fuguChatToken;
    }


    public List<PaymentMethod> getPaymentMethodsForStore() {
        return paymentMethodsForStore;
    }

    public void setPaymentMethodsForStore(List<PaymentMethod> paymentMethodsForStore) {
        this.paymentMethodsForStore = paymentMethodsForStore;
    }

    /**
     * @return The formId
     */
    public Integer getFormId() {
        return formId;
    }

    /**
     * @return The userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId The user_id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return The domainName
     */
    public String getDomainName() {
        return domainName;
    }

    /**
     * @return The workFlow
     */
    public Integer getWorkFlow() {
        if (workFlow == Constants.LayoutType.PICKUP_DELIVERY) {
            switch (pickupDeliveryFlag) {
                case 0:
                    return Constants.LayoutType.PICKUP;
                case 1:
                    return Constants.LayoutType.DELIVERY;
                default:
                    return workFlow;
            }
        }
        return workFlow;
    }

    /**
     * @return The logo
     */
    public String getLogo() {
        return logo;
    }

    public List<PaymentMethod> getPaymentMethods() {

//        if (paymentMethodsForStore != null) {
//            return paymentMethodsForStore;
//        } else {
        return paymentMethods;

//        }
    }

    public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
        this.paymentMethods = new ArrayList<>();
        this.paymentMethods = paymentMethods;
    }

    public int getForcePickupDelivery() {
        return forcePickupDelivery;
    }

    public Integer getVertical() {
        return vertical;
    }

    public Integer getProductView() {
        return productView != null ? productView : 0;
    }

    public Integer getPdpView() {
        return pdpView != null ? pdpView : 0;
    }

    public int getPdpViewTemplates() {
        return pdpViewTemplates;
    }

    public Integer getInstantTask() {
        return instantTask;
    }

    public Integer getScheduledTask() {
        return scheduledTask;
    }

    public Number getBufferSchedule() {
        return bufferSchedule != null ? bufferSchedule : 5;
    }

    public String getFormName() {
        return formName != null ? Utils.capitaliseWords(formName) : "";
    }

    public String getAppDescription() {
        return appDescription;
    }

    public Double getMinimum_self_pickup_amount() {
        return minimum_self_pickup_amount;
    }

    public void setMinimum_self_pickup_amount(Double minimum_self_pickup_amount) {
        this.minimum_self_pickup_amount = minimum_self_pickup_amount;
    }

    public Double getMerchantMinimumOrder() {
        return merchantMinimumOrder != null ? merchantMinimumOrder : 0.0;
    }

    public void setMerchantMinimumOrder(Double merchantMinimumOrder) {
        this.merchantMinimumOrder = merchantMinimumOrder;
    }

    public int getIsShowPrefilledData() {
        return isShowPrefilledData;
    }

    public List<PaymentSettings> getPaymentSettings() {
        return paymentSettings;
    }

    public Integer getIsReviewRatingEnabled() {
        return isReviewRatingEnabled;
    }

    public Integer getShowProductPrice() {
        return showProductPrice != null ? showProductPrice : 0;
    }

    public int getIsFuguChatEnabled() {
        return isFuguChatEnabled;
    }

    public String getFuguChatToken() {
        return fuguChatToken != null ? fuguChatToken : "";
    }

    public Integer getShowOutStockedProduct() {
        return showOutStockedProduct;
    }

    public Integer getEnableTookanAgent() {
        return enableTookanAgent;
    }

    public Button getButtons() {
        return buttons != null ? buttons : new Button();
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public Integer getMultipleProductInSingleCart() {
        return multipleProductInSingleCart;
    }

    public Integer getPdOrAppointment() {
        return pdOrAppointment;
    }

    public Integer getIsStartEndTimeEnable() {
        return isStartEndTimeEnable;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public String getMobileBackgroundImage() {
        return mobileBackgroundImage;
    }

    public int getDisplayMerchantDetailsPage() {
        return displayMerchantDetailsPage;
    }

    public int getDisplayMerchantPhone() {
        return displayMerchantPhone;
    }

    public int getDisplayMerchantLocation() {
        return displayMerchantLocation;
    }

    public int getHomeDelivery() {
        return homeDelivery;
    }

    public int getSelfPickup() {
        return selfPickup;
    }

    public int getIsBannersEnabled() {
        return isBannersEnabled;
    }

    public void setIsBannersEnabled(int isBannersEnabled) {
        this.isBannersEnabled = isBannersEnabled;
    }

    public int getIsBusinessCategoryEnabled() {
        return isBusinessCategoryEnabled;
    }

    public void setIsBusinessCategoryEnabled(int isBusinessCategoryEnabled) {
        this.isBusinessCategoryEnabled = isBusinessCategoryEnabled;
    }

    public int getIsMenuEnabled() {
        return isMenuEnabled;
    }

    public int getPickupAndDrop() {
        return pickupAndDrop;
    }

    public void setPickupAndDrop(int pickupAndDrop) {
        this.pickupAndDrop = pickupAndDrop;
    }
}
