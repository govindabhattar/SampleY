package com.tookancustomer.models.appConfiguration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.models.LanguageStrings.LanguagesCode;
import com.tookancustomer.models.userdata.CancelConfig;
import com.tookancustomer.models.userdata.PaymentSettings;
import com.tookancustomer.models.userdata.UserOptions;
import com.tookancustomer.models.userdata.UserRights;
import com.tookancustomer.utility.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Datum implements Serializable {
    @SerializedName("is_marketplace_delivery_availabile")
    @Expose
    private int isMarketPlaceDeliveryAvailabile;

    @SerializedName("static_address_enabled_for_custom_order")
    @Expose
    public int staticAddressEnabledForCustomOrder = 0;
    @SerializedName("additional_tookan_status")
    @Expose
    private int additionalTookanStatus = 0;
    @SerializedName("accept_reject_enabled")
    @Expose
    private int acceptRejectEnabled;
    @SerializedName("is_food_ready_option_enable")
    @Expose
    private int isFoodReadyOptionEnable;
    @SerializedName("is_custom_address_enabled")
    @Expose
    private int isCustomAddressEnabled;
    @SerializedName("enabled_tracking_link_after_dispatched")
    @Expose
    private int enabledTrackingLinkAfterDispatched;
    @SerializedName("sochitel_enable")
    private int sochitelEnable = 0;
    @SerializedName("is_merchant_chat_enable")
    private int isMerchantChatEnable;
    @SerializedName("referral_code_for_signup")
    @Expose
    private int referral_code_for_signup = 0;
    @SerializedName("is_guest_checkout_enabled")
    @Expose
    private int isGuestCheckoutEnabled;
    @SerializedName("email_config_for_guest_checkout")
    @Expose
    private int emailConfigForGuestCheckout;
    @SerializedName("phone_config_for_guest_checkout")
    @Expose
    private int phoneConfigForGuestCheckout;
    @SerializedName("is_landmark_mandatory")
    @Expose
    private int isLandMarkMandatory;
    @SerializedName("is_postal_code_mandatory")
    @Expose
    private int isPostalCodeMandatory;
    @SerializedName("is_apartment_no_mandatory")
    @Expose
    private int isApartmentNoMandatory;
    @SerializedName("is_show_delivery_popup")
    @Expose
    private int isShowDeliveryPopup = 0;
    @SerializedName("is_product_share_enabled")
    @Expose
    private int isProductShareEnabled;
    @SerializedName("date_format")
    @Expose
    private String date_format = "";
    @SerializedName("map_object")
    @Expose
    private MapObject MapObject;
    @SerializedName("map_type")
    @Expose
    private int mapType = 0;
    @SerializedName("hide_gdpr")
    @Expose
    private int hideGdpr = 0;
    @SerializedName("is_customer_login_required")
    @Expose
    private int isCustomerLoginRequired = 0;
    @SerializedName("is_subscription_enabled")
    @Expose
    private int isSubscriptionEnabled = 0;
    @SerializedName("is_multi_currency_enabled")
    @Expose
    private int isMultiCurrencyEnabled = 0;
    @SerializedName("currency_format")
    @Expose
    private int currency_format = 1;
    @SerializedName("decimal_calculation_precision_point")
    @Expose
    private int decimalCalculationPrecisionPoint = 2;
    @SerializedName("decimal_display_precision_point")
    @Expose
    private int decimalDisplayPrecisionPoint = 2;
    @SerializedName("display_merchant_address")
    @Expose
    private int displayMerchantAddress = 1;
    @SerializedName("google_analytics_is_active")
    @Expose
    private int googleAnalyticsIsActive = 0;
    @SerializedName("google_analytics_tracking_id")
    @Expose
    private String googleAnalyticsTrackingId = "";
    @SerializedName("google_analytics_client_id")
    @Expose
    private String googleAnalyticsClientId = "";
    @SerializedName("GOOGLE_ANALYTIC_OPTION_LIST")
    @Expose
    private List<GoogleAnalyticsOptions> googleAnalyticsOptionsArrayList = new ArrayList<>();
    @SerializedName("enable_veg_non_veg_filter")
    @Expose
    private int enableVegNonVegFilter = 0;
    @SerializedName("enable_veg_non_veg_label")
    @Expose
    private int enableVegNonVegLabel = 0;
    @SerializedName("cancellation_reason_type")
    @Expose
    private int cancellationReasonType = 0; //0 -- Self explanatory reason only, 1 -- Reasons from backend
    @SerializedName("app_version")
    @Expose
    private Integer appVersion;
    @SerializedName("is_force")
    @Expose
    private Integer isForce;
    @SerializedName("app_url")
    @Expose
    private String appUrl;
    @SerializedName("whats_new")
    @Expose
    private String whatsNew;
    @SerializedName("form_id")
    @Expose
    private Integer formId;
    @SerializedName("form_type")
    @Expose
    private Integer formType;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("domain_name")
    @Expose
    private String domainName;
    @SerializedName("work_flow")
    @Expose
    private Integer workFlow;
    @SerializedName("selected_template")
    @Expose
    private String selectedTemplate;
    @SerializedName("delivery_template")
    @Expose
    private String deliveryTemplate;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("fav_logo")
    @Expose
    private String favLogo;
    @SerializedName("font")
    @Expose
    private Object font;
    @SerializedName("nav_bar")
    @Expose
    private Object navBar;
    @SerializedName("login_required")
    @Expose
    private Integer loginRequired;
    @SerializedName("auto_assign")
    @Expose
    private Integer autoAssign;
    @SerializedName("app_signup_allowed")
    @Expose
    private int signupAllow = 1;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("pickup_delivery_flag")
    @Expose
    private Integer pickupDeliveryFlag;
    @SerializedName("autofill_required")
    @Expose
    private Integer autofillRequired;
    @SerializedName("creation_datetime")
    @Expose
    private String creationDatetime;
    @SerializedName("map_theme")
    @Expose
    private Integer mapTheme;
    @SerializedName("call_tasks_as")
    @Expose
    private String callTasksAs;
    @SerializedName("force_pickup_delivery")
    @Expose
    private String forcePickupDelivery;
    @SerializedName("is_fb_required")
    @Expose
    private int isFacebookRequired = 0;
    @SerializedName("is_google_required")
    @Expose
    private int isGoogleRequired = 0;
    @SerializedName("is_instagram_required")
    @Expose
    private int isInstagramRequired = 0;
    @SerializedName("is_otp_enabled")
    @Expose
    private int isOtpRequired = 0;
    @SerializedName("is_loyalty_point_enabled")
    @Expose
    private int isLoyaltyEnable = 0;
    @SerializedName("is_promo_required")
    @Expose
    private Integer isPromoRequired = 0;
    @SerializedName("customer_signup_template")
    @Expose
    private Integer isCustomerSignupTemplate = 0;
    @SerializedName("is_referral_required")
    @Expose
    private Integer isReferralRequired = 0;
    @SerializedName("vertical")
    @Expose
    private int vertical;
    @SerializedName("product_view")
    @Expose
    private Integer productView;
    @SerializedName("is_destination_required")
    @Expose
    private Integer isDestinationRequired = 0;
    @SerializedName("show_waiting_screen")
    @Expose
    private Integer showWaitingScreen = 0;
    @SerializedName("waiting_screen_time")
    @Expose
    private Integer waitingScreenTime = 0;
    @SerializedName("is_review_rating_enabled")
    @Expose
    private Integer isReviewRatingEnabled = 0;
    @SerializedName("show_active_task")
    @Expose
    private Integer showActiveTask;
    @SerializedName("show_category")
    @Expose
    private Integer showCategory;
    @SerializedName("signup_field")
    @Expose
    private int signupField = 2; //0 Email reqd , 1 Phone Reqd , 2 Both email and phone reqd
    @SerializedName("is_rating_required")
    @Expose
    private Integer isRatingRequired;
    @SerializedName("is_scheduling_enabled")
    @Expose

    private Integer isSchedulingEnabled;
    @SerializedName("schedule_offset_time")
    @Expose
    private Integer scheduleOffsetTime;
    @SerializedName("is_credit_enabled")
    @Expose
    private Integer isCreditEnabled = 0;
    @SerializedName("show_payment_screen")
    @Expose
    private Integer showPaymentScreen = 0;
    @SerializedName("show_t_n_c")
    @Expose
    private int showTNC = 0;
    @SerializedName("is_tnc_active")
    @Expose
    private int isTncActive;
    @SerializedName("nlevel_enabled")
    @Expose
    private int nlevelEnabled = 0;
    @SerializedName("form_name")
    @Expose
    private String formName;
    @SerializedName("userOptions")
    @Expose
    private UserOptions userOptions;
    @SerializedName("contact_details")
    @Expose
    private ContactDetails contactDetails;
    @SerializedName("is_customer_subscription_enabled")
    @Expose
    private int isCustomerSubscriptionEnabled = 0;
    @SerializedName("is_customer_subscription_mandatory")
    @Expose
    private int isCustomerSubscriptionMandatory = 0;
    @SerializedName("deliveryOptions")
    @Expose
    private UserOptions deliveryOptions;
    @SerializedName("cancel_config")
    @Expose
    private List<CancelConfig> cancelConfig = new ArrayList<>();
    @SerializedName("is_web_enabled")
    @Expose
    private Integer isWebEnabled;
    @SerializedName("is_ios_enabled")
    @Expose
    private Integer isIosEnabled;
    @SerializedName("is_android_enabled")
    @Expose
    private Integer isAndroidEnabled;
    @SerializedName("deleted_on")
    @Expose
    private String deletedOn;
    @SerializedName("vat")
    @Expose
    private double vat;
    @SerializedName("referral_share_message")
    @Expose
    private String referralShareMessage;
    @SerializedName("referral_fb_message")
    @Expose
    private String referralFbMessage;
    @SerializedName("scheduled_task")
    @Expose
    private Integer scheduledTask = 0;
    @SerializedName("buffer_schedule")
    @Expose
    private Number bufferSchedule = 0;
    @SerializedName("show_date_filter")
    @Expose
    private int showDateFilter = 1;
    @SerializedName("is_fugu_chat_enabled")
    @Expose
    private Integer isFuguChatEnabled = 0;
    @SerializedName("is_dual_user_enable")
    @Expose
    private int isDualUserEnable = 0;
    @SerializedName("map_view")
    @Expose
    private int mapView = 0;
    @SerializedName("is_email_verification_required")
    @Expose
    private int isEmailVerificationRequried = 0;
    @SerializedName("merchant_view_profile")
    @Expose
    private int merchantViewProfile = 0;// view profile button on PDP screen
    @SerializedName("is_banners_enabled")
    @Expose
    private int isBannersEnabled = 0;
    @SerializedName("is_business_category_enabled")
    @Expose
    private int isBusinessCategoryEnabled = 0;
    @SerializedName("fugu_chat_token")
    @Expose
    private String fuguChatToken = "";
    @SerializedName("referral_details")
    @Expose
    private String referralDetails;
    @SerializedName("service_tax")
    @Expose
    private double serviceTax;
    @SerializedName("tax")
    @Expose
    private double tax;
    @SerializedName("referral_description")
    @Expose
    private String referralDescription;
    @SerializedName("payment_settings")  //only in case of app_configurations
    @Expose
    private List<PaymentSettings> paymentSettings = new ArrayList<>();
    @SerializedName("terminology")
    @Expose
    private Terminology terminology;
    @SerializedName("privacy_policy")
    @Expose
    private String privacyPolicy = "";
    @SerializedName("business_model_type")
    @Expose
    private String businessModelType = "";
    @SerializedName("onboarding_business_type")
    @Expose
    private int onboardingBusinessType = 0;
    @SerializedName("user_rights")
    @Expose
    private List<UserRights> userRights = new ArrayList<>();
    @SerializedName("languages")
    @Expose
    private List<LanguagesCode> languages = new ArrayList<>();
    //    @SerializedName("language_strings")
//    @Expose
//    private LanguageStrings languageStrings;
    @SerializedName("language_strings")
    @Expose
    private Map<String, String> languageStrings;
    @SerializedName("is_static_address_enabled")
    @Expose
    private int isStaticAddressEnabled;
    @SerializedName("delivery_by_merchant")
    @Expose
    private int deliveryByMerchant;
    @SerializedName("minimum_tip")
    @Expose
    private Double minimumTip = 0.0;
    @SerializedName("tip_enable_disable")
    @Expose
    private int tipEnableDisable = 0; //0  if no tip ,, 1 if tip enabled
    @SerializedName("minimum_tip_type")
    @Expose
    private int minimumTipType = 2; //1 for percentage ,, 2 for price in amount
    @SerializedName("enable_default_tip")
    @Expose
    private int enableDefaultTip = 0; // Mean of tip is shown in case of minimum tip or not
    @SerializedName("is_custom_order_active")
    @Expose
    private int isCustomOrderActive;
    @SerializedName("is_menu_enabled")
    @Expose
    private int isMenuEnabled;
    @SerializedName("custom_quotation_enabled")
    @Expose
    private int customQuotationEnabled;
    @SerializedName("is_dynamic_pages_active")
    @Expose
    private int isDynamicPagesActive = 0;
    @SerializedName("dynamic_pages_details")
    @Expose
    private ArrayList<DynamicPagesDetails> dynamicPagesDetails = new ArrayList<>();
    @SerializedName("side_order")
    @Expose
    private int sideOrder = 0;
    @SerializedName("post_payment_enable")
    @Expose
    private int postPaymentEnable = 0;
    @SerializedName("is_cancellation_policy_enabled")
    @Expose
    private int isCancellationPolicyEnabled = 0;
    @SerializedName("is_cancellation_policy_active")
    @Expose
    private int isCancellationPolicyActive = 0;
    @SerializedName("is_landing_page_enabled")
    @Expose
    private int isLandingPageEnable = 0;
    @SerializedName("MERCHANT_COUNT")
    @Expose
    private int merchantCount = 0;
    @SerializedName("enabled_marketplace_storefront")
    @Expose
    private ArrayList<Integer> enabledMarketplaceStorefront = new ArrayList<>();
    @SerializedName("mobile_background_image")
    @Expose
    private String mobileBackgroundImage;
    @SerializedName("facebook_app_id")
    @Expose
    private String facebookAppId;
    @SerializedName("instagram_app_id")
    @Expose
    private String instagramAppId;
    @SerializedName("google_client_app_id")
    @Expose
    private String googleClientAppId;
    @SerializedName("show_service_time")
    @Expose
    private int showServiceTime = 1;
    @SerializedName("custom_order_page")
    @Expose
    private CustomOrderPage customOrderPage;
    @SerializedName("is_gift_card_activated")
    @Expose
    private int isGiftCardActivated;
    @SerializedName("gift_card_description")
    @Expose
    private String giftCardDescription = "";
    @SerializedName("is_anywhere_required")
    @Expose
    private int isAnywhereRequired = 1;
    @SerializedName("is_custom_required")
    @Expose
    private int isCustomRequired = 1;
    @SerializedName("is_hold_amount_active")
    @Expose
    private int isHoldAmountActive;
    @SerializedName("is_tookan_active")
    @Expose
    private int isTookanActive;
    @SerializedName("max_schedule_days_limit")
    @Expose
    private int maxScheduleDaysLimit;
    @SerializedName("vendor_otp_login_sign_up")
    @Expose
    private int vendorOTPLoginSignup = 0;
    @SerializedName("is_reward_active")
    @Expose
    private int isRewardActive;
    @SerializedName("time_format")
    @Expose
    private int timeFormat = 1;
    @SerializedName("show_delivery_time")
    @Expose
    private int showDeliveryTime;
    @SerializedName("is_customer_verification_required")
    @Expose
    private int isCustomerVerificationRequired;
    @SerializedName("custom_checkout_template_on_single_screen")
    @Expose
    private int customCheckoutTemplateOnSingleScreen;
    @SerializedName("custom_order_phone")
    @Expose
    private int customOrderPhone = 1; //0 hide ...1 mandatory and visible .....2 optional and visible
    @SerializedName("custom_order_email")
    @Expose
    private int customOrderEmail = 1; //0 hide ...1 mandatory and visible .....2 optional and visible
    @SerializedName("custom_order_name")
    @Expose
    private int customOrderName = 1; //0 hide ...1 mandatory and visible .....2 optional and visible
    @SerializedName("is_debt_enabled")
    @Expose
    private int isDebtEnabled = 0; // If debt amount of user is > 0 , then check for this toggle to block user from accessing the app.
    @SerializedName("is_debt_payment_compulsory")
    @Expose
    private int isDebtPaymentCompulsory = 0;
    @SerializedName("show_delivery_charge_on_list_page")
    @Expose
    private int showDeliveryChargeOnListPage = 0;
    @SerializedName("selected_delivery_method_for_apps")
    @Expose
    private int defaultDeliveryMethod = 4;
    @SerializedName("merchant_select_payment_method")
    @Expose
    private int merchantSelectPaymentMethod = 0; // If  1==Merchant's payment methods will be used on payment else admin's payment methods
    @SerializedName("is_product_image_visible_on_checkout")
    @Expose
    private int isProductImageVisibleOnCheckout = 0;
    @SerializedName("is_recurring_enabled")
    @Expose
    private int recurringTask = 1;
    @SerializedName("display_range_intervals")
    @Expose
    private int displayRangeIntervals;
    @SerializedName("show_tip_in_pickup")
    @Expose
    private int showTipInPickup = 0;
    @SerializedName("mobile_icon_self_pickup_image")
    @Expose
    private String mobile_icon_self_pickup_image;
    @SerializedName("mobile_icon_pick_and_drop_image")
    @Expose
    private String mobile_icon_pick_and_drop_image;
    @SerializedName("mobile_icon_home_delivery_image")
    @Expose
    private String mobile_icon_home_delivery_image;
    @SerializedName("is_popup_enabled")
    private int is_popup_enabled;
    @SerializedName("popup_image_url")
    private String popup_image_url;
    @SerializedName("popup_redirect_link")
    private String popup_redirect_link;

    public int getStaticAddressEnabledForCustomOrder() {
        return staticAddressEnabledForCustomOrder;
    }

    public int getAcceptRejectEnabled() {
        return acceptRejectEnabled;
    }

    public int getIsCustomAddressEnabled() {
        return isCustomAddressEnabled;
    }

    public void setIsCustomAddressEnabled(int isCustomAddressEnabled) {
        this.isCustomAddressEnabled = isCustomAddressEnabled;
    }

    public int getIsFoodReadyOptionEnable() {
        return isFoodReadyOptionEnable;
    }

    public int getReferral_code_for_signup() {
        return referral_code_for_signup;
    }

    public void setReferral_code_for_signup(int referral_code_for_signup) {
        this.referral_code_for_signup = referral_code_for_signup;
    }

    public int getSochitelEnable() {
        return sochitelEnable;
    }

    public void setSochitelEnable(int sochitelEnable) {
        this.sochitelEnable = sochitelEnable;
    }

    public int getEnabledTrackingLinkAfterDispatched() {
        return enabledTrackingLinkAfterDispatched;
    }

    public void setEnabledTrackingLinkAfterDispatched(int enabledTrackingLinkAfterDispatched) {
        this.enabledTrackingLinkAfterDispatched = enabledTrackingLinkAfterDispatched;
    }

    public int getIsProductShareEnabled() {
        return isProductShareEnabled;
    }

    public void setIsProductShareEnabled(int isProductShareEnabled) {
        this.isProductShareEnabled = isProductShareEnabled;
    }

    public int getIsShowDeliveryPopup() {
        return isShowDeliveryPopup;
    }

    public void setIsShowDeliveryPopup(int isShowDeliveryPopup) {
        this.isShowDeliveryPopup = isShowDeliveryPopup;
    }

    public int getShowTipInPickup() {
        return showTipInPickup;
    }

    public void setShowTipInPickup(int showTipInPickup) {
        this.showTipInPickup = showTipInPickup;
    }

    public String getMobile_icon_pick_and_drop_image() {
        return mobile_icon_pick_and_drop_image;
    }

    public void setMobile_icon_pick_and_drop_image(String mobile_icon_pick_and_drop_image) {
        this.mobile_icon_pick_and_drop_image = mobile_icon_pick_and_drop_image;
    }

    public String getMobile_icon_home_delivery_image() {
        return mobile_icon_home_delivery_image;
    }

    public void setMobile_icon_home_delivery_image(String mobile_icon_home_delivery_image) {
        this.mobile_icon_home_delivery_image = mobile_icon_home_delivery_image;
    }

    public String getMobile_icon_self_pickup_image() {
        return mobile_icon_self_pickup_image;
    }

    public void setMobile_icon_self_pickup_image(String mobile_icon_self_pickup_image) {
        this.mobile_icon_self_pickup_image = mobile_icon_self_pickup_image;
    }


    public String getDate_format() {
        if (!date_format.isEmpty())
            return date_format;
        else
            return "yyyy-MM-dd";
    }

    public void setDate_format(String date_format) {
        this.date_format = date_format;
    }

    public int getShowDeliveryChargeOnListPage() {
        return showDeliveryChargeOnListPage;
    }

    public void setShowDeliveryChargeOnListPage(int showDeliveryChargeOnListPage) {
        this.showDeliveryChargeOnListPage = showDeliveryChargeOnListPage;
    }

    public int getIsProductImageVisibleOnCheckout() {
        return isProductImageVisibleOnCheckout;
    }

    public void setIsProductImageVisibleOnCheckout(int isProductImageVisibleOnCheckout) {
        this.isProductImageVisibleOnCheckout = isProductImageVisibleOnCheckout;
    }

    public int getMerchantSelectPaymentMethod() {
        return merchantSelectPaymentMethod;
    }

    public void setMerchantSelectPaymentMethod(int merchantSelectPaymentMethod) {
        this.merchantSelectPaymentMethod = merchantSelectPaymentMethod;
    }

    public int getDefaultDeliveryMethod() {
        return defaultDeliveryMethod;
    }

    public void setDefaultDeliveryMethod(int defaultDeliveryMethod) {
        this.defaultDeliveryMethod = defaultDeliveryMethod;
    }

    public int getMapView() {
        return mapView;
    }

    public void setMapView(int mapView) {
        this.mapView = mapView;
    }

    public int getShowDeliveryTime() {
        return showDeliveryTime;
    }

    public int getTimeFormat() {
        return timeFormat;
    }

    public int getDisplayRangeIntervals() {
        return displayRangeIntervals;
    }

    public void setDisplayRangeIntervals(int displayRangeIntervals) {
        this.displayRangeIntervals = displayRangeIntervals;
    }

    public int getMaxScheduleDaysLimit() {
        return maxScheduleDaysLimit;
    }

    public void setMaxScheduleDaysLimit(int maxScheduleDaysLimit) {
        this.maxScheduleDaysLimit = maxScheduleDaysLimit;
    }

    public int getVendorOTPLoginSignup() {
        return vendorOTPLoginSignup;
    }

    public void setVendorOTPLoginSignup(int vendorOTPLoginSignup) {
        this.vendorOTPLoginSignup = vendorOTPLoginSignup;
    }

    public int getSideOrder() {
        return sideOrder;
    }

    public void setSideOrder(final int showSideOrder) {
        this.sideOrder = showSideOrder;
    }

    public int getRecurringTask() {
        return recurringTask;
    }

    public void setRecurringTask(int recurringTask) {
        this.recurringTask = recurringTask;
    }

    public ArrayList<DynamicPagesDetails> getDynamicPagesDetails() {
        return dynamicPagesDetails != null ? dynamicPagesDetails : new ArrayList<DynamicPagesDetails>();
    }

    public void setDynamicPagesDetails(ArrayList<DynamicPagesDetails> dynamicPagesDetails) {
        this.dynamicPagesDetails = dynamicPagesDetails;
    }

    public int getIsLoyaltyEnable() {
        return isLoyaltyEnable;
    }

    public void setIsLoyaltyEnable(Integer isLoyaltyEnable) {
        this.isLoyaltyEnable = isLoyaltyEnable;
    }

    public int getIsDynamicPagesActive() {
        return isDynamicPagesActive;
    }

    public void setIsDynamicPagesActive(int isDynamicPagesActive) {
        this.isDynamicPagesActive = isDynamicPagesActive;
    }

    public int getMinimumTipType() {
        return minimumTipType;
    }

    public void setMinimumTipType(int minimumTipType) {
        this.minimumTipType = minimumTipType;
    }

    public Double getMinimumTip() {
        return minimumTip != null ? Double.valueOf(Utils.getDoubleTwoDigits(minimumTip)) : 0;
    }

    public void setMinimumTip(Double minimumTip) {
        this.minimumTip = minimumTip;
    }

    public int getIsInstagramRequired() {
        return isInstagramRequired;
    }

    public void setIsInstagramRequired(final Integer isInstagramRequired) {
        this.isInstagramRequired = isInstagramRequired;
    }


    public int getNlevelEnabled() {
        return nlevelEnabled;
    }

    public void setNlevelEnabled(Integer nlevelEnabled) {
        this.nlevelEnabled = nlevelEnabled;
    }

    public List<LanguagesCode> getLanguages() {
        return languages;
    }

    public void setLanguages(List<LanguagesCode> languages) {
        this.languages = languages;
    }

//    public LanguageStrings getLanguageStrings() {
//        return languageStrings;
//    }
//
//    public void setLanguageStrings(LanguageStrings languageStrings) {
//        this.languageStrings = languageStrings;
//    }


    public Map<String, String> getLanguageStrings() {
        return languageStrings;
    }

    public void setLanguageStrings(Map<String, String> languageStrings) {
        this.languageStrings = languageStrings;
    }

    public String getPrivacyPolicy() {
        return privacyPolicy;
    }

    public void setPrivacyPolicy(String privacyPolicy) {
        this.privacyPolicy = privacyPolicy;
    }

    public Terminology getTerminology() {
        return terminology != null ? terminology : new Terminology();
    }

    public void setTerminology(Terminology terminology) {
        this.terminology = terminology;
    }


    public Integer getIsCustomerSignupTemplate() {
        return isCustomerSignupTemplate;
    }

    public void setIsCustomerSignupTemplate(Integer isCustomerSignupTemplate) {
        this.isCustomerSignupTemplate = isCustomerSignupTemplate;
    }

    public int getIsFuguChatEnabled() {
        return isFuguChatEnabled;
    }

    public void setIsFuguChatEnabled(int isFuguChatEnabled) {
        this.isFuguChatEnabled = isFuguChatEnabled;
    }

    public int getIsDualUserEnable() {
        return isDualUserEnable;
    }

    public void setIsDualUserEnable(Integer isDualUserEnable) {
        this.isDualUserEnable = isDualUserEnable;
    }

    public String getFuguChatToken() {
        return fuguChatToken != null ? fuguChatToken : "";
    }

    public void setFuguChatToken(String fuguChatToken) {
        this.fuguChatToken = fuguChatToken;
    }


    public String getReferralShareMessage() {
        return referralShareMessage;
    }

    public void setReferralShareMessage(String referralShareMessage) {
        this.referralShareMessage = referralShareMessage;
    }

    public String getReferralFbMessage() {
        return referralFbMessage;
    }

    public void setReferralFbMessage(String referralFbMessage) {
        this.referralFbMessage = referralFbMessage;
    }

    public String getReferralDetails() {
        return referralDetails;
    }

    public void setReferralDetails(String referralDetails) {
        this.referralDetails = referralDetails;
    }


    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }

    public double getServiceTax() {
        return serviceTax;
    }

    public void setServiceTax(double serviceTax) {
        this.serviceTax = serviceTax;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public String getReferralDescription() {
        return referralDescription;
    }

    public void setReferralDescription(String referralDescription) {
        this.referralDescription = referralDescription;
    }

    public List<PaymentSettings> getPaymentSettings() {
        return paymentSettings;
    }

    public void setPaymentSettings(List<PaymentSettings> paymentSettings) {
        this.paymentSettings = paymentSettings;
    }

    public int getIsCustomerSubscriptionEnabled() {
        return isCustomerSubscriptionEnabled;
    }

    public void setIsCustomerSubscriptionEnabled(int isCustomerSubscriptionEnabled) {
        this.isCustomerSubscriptionEnabled = isCustomerSubscriptionEnabled;
    }

    public int getIsCustomerSubscriptionMandatory() {
        return isCustomerSubscriptionMandatory;
    }

    public void setIsCustomerSubscriptionMandatory(int isCustomerSubscriptionMandatory) {
        this.isCustomerSubscriptionMandatory = isCustomerSubscriptionMandatory;
    }

    public Integer getIsCreditEnabled() {
        return isCreditEnabled;
    }

    public void setIsCreditEnabled(Integer isCreditEnabled) {
        this.isCreditEnabled = isCreditEnabled;
    }

    public Integer getShowPaymentScreen() {
        return showPaymentScreen;
    }

    public void setShowPaymentScreen(Integer showPaymentScreen) {
        this.showPaymentScreen = showPaymentScreen;
    }

    public Integer getScheduleOffsetTime() {
        return scheduleOffsetTime;
    }

    public void setScheduleOffsetTime(Integer scheduleOffsetTime) {
        this.scheduleOffsetTime = scheduleOffsetTime;
    }

    public Integer getIsSchedulingEnabled() {
        return isSchedulingEnabled;
    }

    public void setIsSchedulingEnabled(Integer isSchedulingEnabled) {
        this.isSchedulingEnabled = isSchedulingEnabled;
    }

    public Integer getShowWaitingScreen() {
        return showWaitingScreen;
    }

    public void setShowWaitingScreen(Integer showWaitingScreen) {
        this.showWaitingScreen = showWaitingScreen;
    }

    public Integer getWaitingScreenTime() {
        return waitingScreenTime;
    }

    public void setWaitingScreenTime(Integer waitingScreenTime) {
        this.waitingScreenTime = waitingScreenTime;
    }

    public Integer getShowActiveTask() {
        return showActiveTask;
    }

    public void setShowActiveTask(Integer showActiveTask) {
        this.showActiveTask = showActiveTask;
    }

    public Integer getShowCategory() {
        return showCategory;
    }

    public void setShowCategory(Integer showCategory) {
        this.showCategory = showCategory;
    }

    public Integer getIsDestinationRequired() {
        return isDestinationRequired;
    }

    public void setIsDestinationRequired(Integer isDestinationRequired) {
        this.isDestinationRequired = isDestinationRequired;
    }

    public Integer getFormType() {
        return formType;
    }

    public void setFormType(Integer formType) {
        this.formType = formType;
    }

    public String getDeliveryTemplate() {
        return deliveryTemplate;
    }

    public void setDeliveryTemplate(String deliveryTemplate) {
        this.deliveryTemplate = deliveryTemplate;
    }

    public int getIsFacebookRequired() {
        return isFacebookRequired;
    }

    public void setIsFacebookRequired(Integer isFacebookRequired) {
        this.isFacebookRequired = isFacebookRequired;
    }

    public int getIsGoogleRequired() {
        return isGoogleRequired;
    }

    public void setIsGoogleRequired(Integer isGoogleRequired) {
        this.isGoogleRequired = isGoogleRequired;
    }

    public int getIsOtpRequired() {
        return isOtpRequired;
    }

    public void setIsOtpRequired(Integer isOtpRequired) {
        this.isOtpRequired = isOtpRequired;
    }

    public Integer getIsPromoRequired() {
        return isPromoRequired;
    }

    public void setIsPromoRequired(Integer isPromoRequired) {
        this.isPromoRequired = isPromoRequired;
    }

    public Integer getIsReferralRequired() {
        return isReferralRequired;
    }

    public void setIsReferralRequired(Integer isReferralRequired) {
        this.isReferralRequired = isReferralRequired;
    }

    public int getVertical() {
        return vertical;
    }

    public void setVertical(int vertical) {
        this.vertical = vertical;
    }

    public Integer getProductView() {
        return productView;
    }

    public void setProductView(Integer productView) {
        this.productView = productView;
    }

    public String getForcePickupDelivery() {
        return forcePickupDelivery;
    }

    public void setForcePickupDelivery(String forcePickupDelivery) {
        this.forcePickupDelivery = forcePickupDelivery;
    }


    public Integer getIsRatingRequired() {
        return isRatingRequired;
    }

    public void setIsRatingRequired(Integer isRatingRequired) {
        this.isRatingRequired = isRatingRequired;
    }

    /**
     * @return The formId
     */
    public Integer getFormId() {
        return formId;
    }

    /**
     * @param formId The form_id
     */
    public void setFormId(Integer formId) {
        this.formId = formId;
    }

    /**
     * @return The isWebEnabled
     */
    public Integer getIsWebEnabled() {
        return isWebEnabled;
    }

    /**
     * @param isWebEnabled The is_web_enabled
     */
    public void setIsWebEnabled(Integer isWebEnabled) {
        this.isWebEnabled = isWebEnabled;
    }

    /**
     * @return The isIosEnabled
     */
    public Integer getIsIosEnabled() {
        return isIosEnabled;
    }

    /**
     * @param isIosEnabled The is_ios_enabled
     */
    public void setIsIosEnabled(Integer isIosEnabled) {
        this.isIosEnabled = isIosEnabled;
    }

    /**
     * @return The isAndroidEnabled
     */
    public Integer getIsAndroidEnabled() {
        return isAndroidEnabled;
    }

    /**
     * @param isAndroidEnabled The is_android_enabled
     */
    public void setIsAndroidEnabled(Integer isAndroidEnabled) {
        this.isAndroidEnabled = isAndroidEnabled;
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
     * @param domainName The domain_name
     */
    public void setDomainName(String domainName) {
        this.domainName = domainName;
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
     * @param workFlow The work_flow
     */
    public void setWorkFlow(Integer workFlow) {
        this.workFlow = workFlow;
    }

    /**
     * @return The selectedTemplate
     */
    public String getSelectedTemplate() {
        return selectedTemplate;
    }

    /**
     * @param selectedTemplate The selected_template
     */
    public void setSelectedTemplate(String selectedTemplate) {
        this.selectedTemplate = selectedTemplate;
    }

    /**
     * @return The color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color The color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * @return The logo
     */
    public String getLogo() {
        return logo;
    }

    /**
     * @param logo The logo
     */
    public void setLogo(String logo) {
        this.logo = logo;
    }

    /**
     * @return The favLogo
     */
    public String getFavLogo() {
        return favLogo;
    }

    /**
     * @param favLogo The fav_logo
     */
    public void setFavLogo(String favLogo) {
        this.favLogo = favLogo;
    }

    /**
     * @return The font
     */
    public Object getFont() {
        return font;
    }

    /**
     * @param font The font
     */
    public void setFont(Object font) {
        this.font = font;
    }

    /**
     * @return The navBar
     */
    public Object getNavBar() {
        return navBar;
    }

    /**
     * @param navBar The nav_bar
     */
    public void setNavBar(Object navBar) {
        this.navBar = navBar;
    }

    /**
     * @return The loginRequired
     */
    public Integer getLoginRequired() {
        return loginRequired;
    }

    /**
     * @param loginRequired The login_required
     */
    public void setLoginRequired(Integer loginRequired) {
        this.loginRequired = loginRequired;
    }

    /**
     * @return The autoAssign
     */
    public Integer getAutoAssign() {
        return autoAssign;
    }

    /**
     * @param autoAssign The auto_assign
     */
    public void setAutoAssign(Integer autoAssign) {
        this.autoAssign = autoAssign;
    }

    /**
     * @return The signupAllow
     */
    public int getSignupAllow() {
        return signupAllow;
    }

    /**
     * @param signupAllow The signup_allow
     */
    public void setSignupAllow(int signupAllow) {
        this.signupAllow = signupAllow;
    }

    /**
     * @return The status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return The pickupDeliveryFlag
     */
    public Integer getPickupDeliveryFlag() {
        return pickupDeliveryFlag;
    }

    /**
     * @param pickupDeliveryFlag The pickup_delivery_flag
     */
    public void setPickupDeliveryFlag(Integer pickupDeliveryFlag) {
        this.pickupDeliveryFlag = pickupDeliveryFlag;
    }

    /**
     * @return The autofillRequired
     */
    public Integer getAutofillRequired() {
        return autofillRequired;
    }

    /**
     * @param autofillRequired The autofill_required
     */
    public void setAutofillRequired(Integer autofillRequired) {
        this.autofillRequired = autofillRequired;
    }

    /**
     * @return The creationDatetime
     */
    public String getCreationDatetime() {
        return creationDatetime;
    }

    /**
     * @param creationDatetime The creation_datetime
     */
    public void setCreationDatetime(String creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    /**
     * @return The deletedOn
     */
    public String getDeletedOn() {
        return deletedOn;
    }

    /**
     * @param deletedOn The deleted_on
     */
    public void setDeletedOn(String deletedOn) {
        this.deletedOn = deletedOn;
    }

    /**
     * @return The mapTheme
     */
    public Integer getMapTheme() {
        return mapTheme;
    }

    /**
     * @param mapTheme The map_theme
     */
    public void setMapTheme(Integer mapTheme) {
        this.mapTheme = mapTheme;
    }

    public String getCallTasksAs() {
        return callTasksAs;
    }

    public void setCallTasksAs(String callTasksAs) {
        this.callTasksAs = callTasksAs;
    }


    public int getShowTNC() {
        return showTNC;
    }

    public void setShowTNC(Integer showTNC) {
        this.showTNC = showTNC;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public UserOptions getUserOptions() {
        return userOptions;
    }

    public void setUserOptions(UserOptions userOptions) {
        this.userOptions = userOptions;
    }

    public ContactDetails getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(ContactDetails contactDetails) {
        this.contactDetails = contactDetails;
    }

    public UserOptions getDeliveryOptions() {
        return deliveryOptions;
    }

    public void setDeliveryOptions(UserOptions deliveryOptions) {
        this.deliveryOptions = deliveryOptions;
    }

    public List<CancelConfig> getCancelConfig() {
        return cancelConfig;
    }

    public void setCancelConfig(List<CancelConfig> cancelConfig) {
        this.cancelConfig = cancelConfig;
    }


    public Integer getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(Integer appVersion) {
        this.appVersion = appVersion;
    }

    public Integer getIsForce() {
        return isForce;
    }

    public void setIsForce(Integer isForce) {
        this.isForce = isForce;
    }


    public Integer getScheduledTask() {
        return scheduledTask;
    }

    public void setScheduledTask(Integer scheduledTask) {
        this.scheduledTask = scheduledTask;
    }

    public Number getBufferSchedule() {
        return bufferSchedule != null ? bufferSchedule : 0;
    }

    public void setBufferSchedule(Number bufferSchedule) {
        this.bufferSchedule = bufferSchedule;
    }

    public Integer getIsReviewRatingEnabled() {
        return isReviewRatingEnabled;
    }

    public void setIsReviewRatingEnabled(Integer isReviewRatingEnabled) {
        this.isReviewRatingEnabled = isReviewRatingEnabled;
    }

    public List<UserRights> getUserRights() {
        return userRights;
    }

    public void setUserRights(List<UserRights> userRights) {
        this.userRights = userRights;
    }

    public String getBusinessModelType() {
        return businessModelType != null ? businessModelType : "";
    }

    public void setBusinessModelType(String businessModelType) {
        this.businessModelType = businessModelType;
    }

    public int getShowDateFilter() {
        return showDateFilter;
    }

    public void setShowDateFilter(Integer showDateFilter) {
        this.showDateFilter = showDateFilter;
    }

    public int getMerchantViewProfile() {
        return merchantViewProfile;
    }

    public void setMerchantViewProfile(int merchantViewProfile) {
        this.merchantViewProfile = merchantViewProfile;
    }

    public int getIsEmailVerificationRequried() {
        return isEmailVerificationRequried;
    }

    public void setIsEmailVerificationRequried(int isEmailVerificationRequried) {
        this.isEmailVerificationRequried = isEmailVerificationRequried;
    }

    public int getIsTncActive() {
        return isTncActive;
    }

    public void setIsTncActive(Integer isTncActive) {
        this.isTncActive = isTncActive;
    }

    public int getEnableVegNonVegFilter() {
        return enableVegNonVegFilter;
    }

    public void setEnableVegNonVegFilter(int enableVegNonVegFilter) {
        this.enableVegNonVegFilter = enableVegNonVegFilter;
    }

    public int getEnableVegNonVegLabel() {
        return enableVegNonVegLabel;
    }

    public void setEnableVegNonVegLabel(int enableVegNonVegLabel) {
        this.enableVegNonVegLabel = enableVegNonVegLabel;
    }

    public int getSignupField() {
        return signupField;
    }

    public int getGoogleAnalyticsIsActive() {
        return googleAnalyticsIsActive;
    }

    public void setGoogleAnalyticsIsActive(int googleAnalyticsIsActive) {
        this.googleAnalyticsIsActive = googleAnalyticsIsActive;
    }

    public String getGoogleAnalyticsTrackingId() {
        return googleAnalyticsTrackingId;
    }

    public void setGoogleAnalyticsTrackingId(String googleAnalyticsTrackingId) {
        this.googleAnalyticsTrackingId = googleAnalyticsTrackingId;
    }

    public String getGoogleAnalyticsClientId() {
        return googleAnalyticsClientId;
    }

    public void setGoogleAnalyticsClientId(String googleAnalyticsClientId) {
        this.googleAnalyticsClientId = googleAnalyticsClientId;
    }

    public List<GoogleAnalyticsOptions> getGoogleAnalyticsOptionsArrayList() {
        return googleAnalyticsOptionsArrayList;
    }

    public void setGoogleAnalyticsOptionsArrayList(List<GoogleAnalyticsOptions> googleAnalyticsOptionsArrayList) {
        this.googleAnalyticsOptionsArrayList = googleAnalyticsOptionsArrayList;
    }

    public int getDisplayMerchantAddress() {
        return displayMerchantAddress;
    }

    public void setDisplayMerchantAddress(int displayMerchantAddress) {
        this.displayMerchantAddress = displayMerchantAddress;
    }

    public int getCancellationReasonType() {
        return cancellationReasonType;
    }

    public void setCancellationReasonType(int cancellationReasonType) {
        this.cancellationReasonType = cancellationReasonType;
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

    public int getDecimalCalculationPrecisionPoint() {
        return decimalCalculationPrecisionPoint;
    }

    public void setDecimalCalculationPrecisionPoint(int decimalCalculationPrecisionPoint) {
        this.decimalCalculationPrecisionPoint = decimalCalculationPrecisionPoint;
    }

    public int getDecimalDisplayPrecisionPoint() {
        return decimalDisplayPrecisionPoint;
    }

    public void setDecimalDisplayPrecisionPoint(int decimalDisplayPrecisionPoint) {
        this.decimalDisplayPrecisionPoint = decimalDisplayPrecisionPoint;
    }

    public int getIsStaticAddressEnabled() {
        return isStaticAddressEnabled;
    }

    public void setIsStaticAddressEnabled(int isStaticAddressEnabled) {
        this.isStaticAddressEnabled = isStaticAddressEnabled;
    }

    public int getDeliveryByMerchant() {
        return deliveryByMerchant;
    }

    public void setDeliveryByMerchant(int deliveryByMerchant) {
        this.deliveryByMerchant = deliveryByMerchant;
    }

    public int getIsCustomOrderActive() {
        return isCustomOrderActive;
    }

    public void setIsCustomOrderActive(int isCustomOrderActive) {
        this.isCustomOrderActive = isCustomOrderActive;
    }

    public int getOnboardingBusinessType() {
        return onboardingBusinessType;
    }

    public void setOnboardingBusinessType(int onboardingBusinessType) {
        this.onboardingBusinessType = onboardingBusinessType;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getWhatsNew() {
        return whatsNew;
    }

    public void setWhatsNew(String whatsNew) {
        this.whatsNew = whatsNew;
    }

    public MapObject getMapObject() {
        return MapObject;
    }

    public void setMapObject(MapObject mapObject) {
        MapObject = mapObject;
    }

    public int getIsSubscriptionEnabled() {
        return isSubscriptionEnabled;
    }

    public void setIsSubscriptionEnabled(int isSubscriptionEnabled) {
        this.isSubscriptionEnabled = isSubscriptionEnabled;
    }

    public int getEnableDefaultTip() {
        return enableDefaultTip;
    }

    public int getTipEnableDisable() {
        return tipEnableDisable;
    }

    public int getCustomQuotationEnabled() {
        return customQuotationEnabled;
    }

    public void setCustomQuotationEnabled(int customQuotationEnabled) {
        this.customQuotationEnabled = customQuotationEnabled;
    }

    public int getIsMenuEnabled() {
        return isMenuEnabled;
    }

    public void setIsMenuEnabled(int isMenuEnabled) {
        this.isMenuEnabled = isMenuEnabled;
    }

    public int getPostPaymentEnable() {
        return postPaymentEnable;
    }

    public int getIsCancellationPolicyEnabled() {
        return isCancellationPolicyEnabled;
    }

    public int getIsCancellationPolicyActive() {
        return isCancellationPolicyActive;
    }

    public int getIsLandingPageEnable() {
//        return 1;
        return isLandingPageEnable;
    }

    public int getEnabledMerchantCount() {
        return merchantCount;
    }

    public ArrayList<Integer> getEnabledMarketplaceStorefront() {
        return enabledMarketplaceStorefront;
    }

    public String getMobileBackgroundImage() {
        return mobileBackgroundImage;
    }

    public int getShowServiceTime() {
        return showServiceTime;
    }

    public CustomOrderPage getCustomOrderPage() {
        return customOrderPage;
    }

    public int getIsGiftCardActivated() {
        return isGiftCardActivated;
    }

    public String getGiftCardDescription() {
        return giftCardDescription != null ? giftCardDescription : "";
    }

    public int getIsAnywhereRequired() {
        return isAnywhereRequired;
    }

    public int getIsCustomRequired() {
        return isCustomRequired;
    }

    public String getFacebookAppId() {
        return facebookAppId;
    }

    public void setFacebookAppId(String facebookAppId) {
        this.facebookAppId = facebookAppId;
    }

    public String getInstagramAppId() {
        return instagramAppId;
    }

    public void setInstagramAppId(String instagramAppId) {
        this.instagramAppId = instagramAppId;
    }

    public String getGoogleClientAppId() {
        return googleClientAppId;
    }

    public void setGoogleClientAppId(String googleClientAppId) {
        this.googleClientAppId = googleClientAppId;
    }

    public int getIsHoldAmountActive() {
        return isHoldAmountActive;
    }

    public int getIsTookanActive() {
        return isTookanActive;
    }

    public int getHideGdpr() {
        return hideGdpr;
    }

    public int getIsRewardActive() {
        return isRewardActive;
    }

    public int getIsCustomerLoginRequired() {
        return isCustomerLoginRequired;
    }

    public int getIsCustomerVerificationRequired() {
        return isCustomerVerificationRequired;
    }

    public int getCustomCheckoutTemplateOnSingleScreen() {
        return customCheckoutTemplateOnSingleScreen;
    }

    public int getCustomOrderPhone() {
        return customOrderPhone;
    }

    public int getCustomOrderEmail() {
        return customOrderEmail;
    }

    public int getCustomOrderName() {
        return customOrderName;
    }

    public int getIsDebtEnabled() {
        return isDebtEnabled;
    }

    public int getIsDebtPaymentCompulsory() {
        return isDebtPaymentCompulsory;
    }

    public int getCurrency_format() {
        return currency_format;
    }

    public void setCurrency_format(int currency_format) {
        this.currency_format = currency_format;
    }

    public int getIsMultiCurrencyEnabled() {
        return isMultiCurrencyEnabled;
    }

    public int getMapType() {
        return mapType;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
    }

    public boolean isPopupEnabled() {
        return is_popup_enabled == 1;
    }

    public void setPopupEnabled(boolean isEnabled) {
        is_popup_enabled = isEnabled ? 1 : 0;
    }

    public String getPopup_image_url() {
        return popup_image_url;
    }

    public String getPopup_redirect_link() {
        return popup_redirect_link;
    }

    public int getIsLandMarkMandatory() {
        return isLandMarkMandatory;
    }

    public int getIsPostalCodeMandatory() {
        return isPostalCodeMandatory;
    }

    public int getIsApartmentNoMandatory() {
        return isApartmentNoMandatory;
    }

    public int getIsGuestCheckoutEnabled() {
        return isGuestCheckoutEnabled;
    }

    public int getEmailConfigForGuestCheckout() {
        return emailConfigForGuestCheckout;
    }

    public int getPhoneConfigForGuestCheckout() {
        return phoneConfigForGuestCheckout;
    }

    public int getIsMerchantChatEnable() {
        return isMerchantChatEnable;
    }

    public void setIsMerchantChatEnable(int isMerchantChatEnable) {
        this.isMerchantChatEnable = isMerchantChatEnable;
    }

    public int getIsMarketPlaceDeliveryAvailabile() {
        return isMarketPlaceDeliveryAvailabile;
    }

    public void setIsMarketPlaceDeliveryAvailabile(int isMarketPlaceDeliveryAvailabile) {
        this.isMarketPlaceDeliveryAvailabile = isMarketPlaceDeliveryAvailabile;
    }


    public boolean getAdditionalTookanStatus() {
        if (additionalTookanStatus == 1)
            return true;
        else
            return false;
    }

    public void setAdditionalTookanStatus(int additionalTookanStatus) {
        this.additionalTookanStatus = additionalTookanStatus;
    }
}
