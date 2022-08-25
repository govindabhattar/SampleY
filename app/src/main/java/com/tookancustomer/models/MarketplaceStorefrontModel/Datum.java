package com.tookancustomer.models.MarketplaceStorefrontModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.NLevelWorkFlowModel.Button;
import com.tookancustomer.models.userdata.PaymentMethod;
import com.tookancustomer.models.userdata.PaymentSettings;
import com.tookancustomer.modules.merchantCatalog.constants.MerchantCatalogConstants;
import com.tookancustomer.modules.merchantCatalog.models.categories.Result;
import com.tookancustomer.utility.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Datum implements Serializable {
    @SerializedName("is_static_address_enabled")
    @Expose
    public int isStaticAddressEnabled = 0;
    @SerializedName("static_address_enabled_for_custom_order")
    @Expose
    public int staticAddressEnabledForCustomOrder = 0;
    @SerializedName("is_single_storefront")
    @Expose
    public int isSingleStorefront = 0;
    @SerializedName("is_free_delivery_enabled")
    @Expose
    public int isFreeDeliveryEnabled;
    @SerializedName("free_delivery_amount")
    @Expose
    public Double free_delivery_amount;
    @SerializedName("free_delivery_toggle")
    @Expose
    public Double free_delivery_toggle;
    @SerializedName("show_merchant_timings")
    @Expose
    public int showMerchantTimings;
    @SerializedName("custom_tag_for_merchant")
    @Expose
    public String custom_tag_for_merchant;
    @SerializedName("is_buffer_slot_for_everyday_enabled")
    @Expose
    public int isBufferSlotForEverydayEnabled = 0;
    @SerializedName("loyalty_point_benefit_blocked")
    @Expose
    public int loyalty_point_benefit_blocked = 0;
    @SerializedName("merchant_discount")
    @Expose
    public Double merchantDiscount = 0.0;
    @SerializedName("button_type")
    @Expose
    public Button buttons;
    @SerializedName("is_wishlisted")
    @Expose
    private int isWishlisted;
    private int selectedPickupMode = 0; // none==0 , home_delivery==1, self_pickup==2
    @SerializedName("available_for_self_pickup")
    @Expose
    private int availableForSelfPickup;
    @SerializedName("available_for_home_delivery")
    @Expose
    private int availableForHomeDelivery;
    @SerializedName("available_for_pick_and_drop")
    @Expose
    private int availableForPickAndDrop;
    @SerializedName("available")
    @Expose
    private Integer isStorefrontOpened = 1;
    @SerializedName("pick_and_drop")
    @Expose
    private int isPdFlow = 1;
    @SerializedName("merchantMinimumOrder")
    @Expose
    private Double merchantMinimumOrder = 0.0;
    @SerializedName("minimum_self_pickup_amount")
    @Expose
    private Double minimumSelfPickupAmount = 0.0;
    @SerializedName("phone")
    @Expose
    private String phone = "";
    @SerializedName(value = "storefront_user_id", alternate = "user_id")
    @Expose
    private Integer storefrontUserId = 0;
    @SerializedName("store_name")
    @Expose
    private String storeName = "";
    @SerializedName("logo")
    @Expose
    private String logo = "";
    @SerializedName("address")
    @Expose
    private String address = "";
    @SerializedName("email")
    @Expose
    private String email = "";
    @SerializedName("display_address")
    @Expose
    private String displayAddress = "";
    @SerializedName("thumb_list")
    @Expose
    private Object thumbList;
    @SerializedName("thumb_url")
    @Expose
    private String thumbUrl;
    @SerializedName("description")
    @Expose
    private String description = "";
    @SerializedName("latitude")
    @Expose
    private String latitude = "0.0";
    @SerializedName("longitude")
    @Expose
    private String longitude = "0.0";
    @SerializedName("serving_distance")
    @Expose
    private Number servingDistance = 0;
    @SerializedName("distance")
    @Expose
    private String distance = "0";
    @SerializedName("store_rating")
    @Expose
    private Number storeRating = 0;
    @SerializedName("store_timings_arr")
    @Expose
    private ArrayList<StoreTimingsArr> storeTimingsArr = new ArrayList<>();
    @SerializedName("last_review_rating")
    @Expose
    private ArrayList<LastReviewRating> lastReviewRating = new ArrayList<>();
    @SerializedName("total_ratings_count")
    @Expose
    private int totalRatingsCount = 0;
    @SerializedName("total_review_count")
    @Expose
    private int totalReviewCount = 0;
    @SerializedName("my_review")
    @Expose
    private String myReview = "";
    @SerializedName("my_rating")
    @Expose
    private Number myRating = 0;
    @SerializedName("instant_task")
    @Expose
    private Integer instantTask = 1;
    @SerializedName("scheduled_task")
    @Expose
    private Integer scheduledTask = 0;
    @SerializedName("is_veg_filter_active")
    @Expose
    private int isVegFilterActive = 0;
    @SerializedName("show_outstocked_product")
    @Expose
    private Integer showOutstockedProduct = 1;
    @SerializedName("enable_tookan_agent")
    @Expose
    private Integer enableTookanAgent = 0;
    @SerializedName("buffer_schedule")
    @Expose
    private Number bufferSchedule = 5;
    @SerializedName("is_review_rating_enabled")
    @Expose
    private int isReviewRatingEnabled;
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
    @SerializedName("home_delivery")
    @Expose
    private int homeDelivery = 1;
    @SerializedName("self_pickup")
    @Expose
    private int selfPickup = 0;
    @SerializedName("is_sponsored")
    @Expose
    private int is_sponsored = 0;
    @SerializedName("pre_booking_buffer")
    @Expose
    private int preBookingBuffer = 0;
    @SerializedName("is_menu_enabled")
    @Expose
    private int isMenuEnabled = 0;
    @SerializedName("business_categories_name")
    @Expose
    private String businessCategoriesName = "";
    @SerializedName("business_catalog_mapping_enabled")
    @Expose
    private int businessCatalogMappingEnabled = 0;
    @SerializedName("has_categories")
    @Expose
    private int hasCategories = 1;
    @SerializedName("show_product_image")
    @Expose
    private int showProductImage = 1;
    @SerializedName("is_recurring_enabled")
    @Expose
    private int recurrinTask;
    @SerializedName("product_list")
    @Expose
    private List<com.tookancustomer.models.ProductCatalogueData.Datum> productList = new ArrayList<>();
    @SerializedName("category_layout_type")
    @Expose
    private int categoryLayoutType = 1;
    @SerializedName("product_layout_type")
    @Expose
    private int productLayoutType = 1;
    @SerializedName("category_button_type")
    @Expose
    private int categoryButtonType = 3;
    @SerializedName("product_button_type")
    @Expose
    private int productButtonType = 1;
    @SerializedName("last_level_catalog_view")
    @Expose
    private int lastLevelCatalogView = 0;
    @SerializedName("merchant_delivery_time")
    @Expose
    private int merchantDeliveryTime = 0;
    @SerializedName("merchant_as_delivery_manager")
    @Expose
    private int merchantAsDeliveryManager = 0;
    @SerializedName("is_banners_enabled_merchant")
    @Expose
    private int isBannersEnabledMerchant = 0;
    @SerializedName("create_delivery_slots")
    @Expose
    private int createDeliverySlots = 0;
    @SerializedName("custom_order_active_for_store")
    @Expose
    private int customOrderActiveForStore;
    @SerializedName("can_serve")
    @Expose
    private int canServe = 1;
    @SerializedName("banner_images")
    @Expose
    private List<BannerImage> bannerImages = null;
    @SerializedName("requiredCatalogues")
    @Expose
    private List<Result> reqCatalogues;
    //    @SerializedName("payment_methods")
//    @Expose
    private List<PaymentMethod> paymentMethods = new ArrayList<>();
    @SerializedName("payment_settings")
    @Expose
    private PaymentSettings paymentSettings;
    @SerializedName("order_preparation_time")
    @Expose
    private int orderPreparationTime = 0;
    @SerializedName("is_order_agent_scheduling_enabled")
    @Expose
    private int isOrderAgentschedulingEnabled = 0;
    @SerializedName("delivery_time")
    @Expose
    private int deliveryTime = 0;
    @SerializedName("estimatedAddOn")
    @Expose
    private int estimatedAddOn = 0;
    @SerializedName("estimatedTime")
    @Expose
    private int estimatedTime = 0;
    @SerializedName("display_range_intervals")
    @Expose
    private int displayRangeIntervals;
    @SerializedName("delivery_charge")
    @Expose
    private double deliveryCharge;
    public Datum(Double merchantMinimumOrder, String phone, Integer storefrontUserId,
                 String storeName, String logo, String address, String email, String displayAddress,
                 String description, String latitude, String longitude, Number servingDistance,
                 String distance, Number storeRating, ArrayList<LastReviewRating> lastReviewRating,
                 int totalRatingsCount, int totalReviewCount, String myReview, Number myRating,
                 Integer instantTask, Integer scheduledTask, Integer showOutstockedProduct,
                 Integer enableTookanAgent, Number bufferSchedule, Button buttons, Integer businessType,
                 Integer multipleProductInSingleCart, Integer pdOrAppointment, Integer isStartEndTimeEnable,
                 int isStorefrontOpened, int isReviewRatingEnabled, int homeDelivery, int selfPickup,
                 String businessCategoriesName, Double merchantDiscount, int is_sponsored,
                 boolean isMenuEnabled, int isRecurringTask, int customOrderActiveForStore, int isVegFilterActive) {

        this.businessCategoriesName = businessCategoriesName;
        this.merchantDiscount = merchantDiscount;
        this.is_sponsored = is_sponsored;
        this.phone = phone;
        this.storefrontUserId = storefrontUserId;
        this.storeName = storeName;
        this.logo = logo;
        this.address = address;
        this.email = email;
        this.displayAddress = displayAddress;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.servingDistance = servingDistance;
        this.distance = distance;
        this.storeRating = storeRating;
        this.lastReviewRating = lastReviewRating;
        this.totalRatingsCount = totalRatingsCount;
        this.totalReviewCount = totalReviewCount;
        this.myReview = myReview;
        this.myRating = myRating;
        this.instantTask = instantTask;
        this.scheduledTask = scheduledTask;
        this.showOutstockedProduct = showOutstockedProduct;
        this.enableTookanAgent = enableTookanAgent;
        this.bufferSchedule = bufferSchedule;
        this.buttons = buttons;
        this.businessType = businessType;
        this.multipleProductInSingleCart = multipleProductInSingleCart;
        this.pdOrAppointment = pdOrAppointment;
        this.isStartEndTimeEnable = isStartEndTimeEnable;
        this.isStorefrontOpened = isStorefrontOpened;
        this.isReviewRatingEnabled = isReviewRatingEnabled;
        this.homeDelivery = homeDelivery;
        this.selfPickup = selfPickup;
        this.recurrinTask = isRecurringTask;
        //this.isMenuEnabled=isMenuEnabled;
        this.customOrderActiveForStore = customOrderActiveForStore;
        this.deliveryCharge = deliveryCharge;
        this.isVegFilterActive = isVegFilterActive;
    }
    public Datum(Double merchantMinimumOrder, String phone, Integer storefrontUserId, String storeName, String logo,
                 String address, String email, String displayAddress, String description, String latitude, String longitude,
                 Number servingDistance, String distance, Number storeRating, ArrayList<LastReviewRating> lastReviewRating,
                 int totalRatingsCount, int totalReviewCount, String myReview, Number myRating, Integer instantTask,
                 Integer scheduledTask, Integer showOutstockedProduct, Integer enableTookanAgent, Number bufferSchedule,
                 Button buttons, Integer businessType, Integer multipleProductInSingleCart, Integer pdOrAppointment,
                 Integer isStartEndTimeEnable, Integer isStorefrontOpened, Integer isReviewRatingEnabled, int homeDelivery, int selfPickup
            , String businessCategoriesName, Double merchantDiscount, int is_sponsored, int isRecurringTask, double deliveryCharge) {
        this.businessCategoriesName = businessCategoriesName;
        this.merchantDiscount = merchantDiscount;
        this.is_sponsored = is_sponsored;
        this.phone = phone;
        this.storefrontUserId = storefrontUserId;
        this.storeName = storeName;
        this.logo = logo;
        this.address = address;
        this.email = email;
        this.displayAddress = displayAddress;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.servingDistance = servingDistance;
        this.distance = distance;
        this.storeRating = storeRating;
        this.lastReviewRating = lastReviewRating;
        this.totalRatingsCount = totalRatingsCount;
        this.totalReviewCount = totalReviewCount;
        this.myReview = myReview;
        this.myRating = myRating;
        this.instantTask = instantTask;
        this.scheduledTask = scheduledTask;
        this.showOutstockedProduct = showOutstockedProduct;
        this.enableTookanAgent = enableTookanAgent;
        this.bufferSchedule = bufferSchedule;
        this.buttons = buttons;
        this.businessType = businessType;
        this.multipleProductInSingleCart = multipleProductInSingleCart;
        this.pdOrAppointment = pdOrAppointment;
        this.isStartEndTimeEnable = isStartEndTimeEnable;
        this.isStorefrontOpened = isStorefrontOpened;
        this.isReviewRatingEnabled = isReviewRatingEnabled;
        this.homeDelivery = homeDelivery;
        this.selfPickup = selfPickup;
        this.recurrinTask = isRecurringTask;
        this.deliveryCharge = deliveryCharge;
    }

    public Datum(Double merchantMinimumOrder, String phone, Integer storefrontUserId, String storeName, String logo,
                 String address, String email, String displayAddress, String description, String latitude, String longitude,
                 Number servingDistance, String distance, Number storeRating, ArrayList<LastReviewRating> lastReviewRating,
                 int totalRatingsCount, int totalReviewCount, String myReview, Number myRating, Integer instantTask,
                 Integer scheduledTask, Integer showOutstockedProduct, Integer enableTookanAgent, Number bufferSchedule,
                 Button buttons, Integer businessType, Integer multipleProductInSingleCart, Integer pdOrAppointment,
                 Integer isStartEndTimeEnable, Integer isStorefrontOpened, Integer isReviewRatingEnabled, int homeDelivery, int selfPickup,
                 String businessCategoriesName, Double merchantDiscount, int is_sponsored, boolean isMenuEnabled, int isRecurringTask,
                 int customOrderActiveForStore, double deliveryCharge, int merchantAsDeliveryManager, int isWishlisted) {
        this.businessCategoriesName = businessCategoriesName;
        this.merchantDiscount = merchantDiscount;
        this.is_sponsored = is_sponsored;
        this.phone = phone;
        this.storefrontUserId = storefrontUserId;
        this.storeName = storeName;
        this.logo = logo;
        this.address = address;
        this.email = email;
        this.displayAddress = displayAddress;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.servingDistance = servingDistance;
        this.distance = distance;
        this.storeRating = storeRating;
        this.lastReviewRating = lastReviewRating;
        this.totalRatingsCount = totalRatingsCount;
        this.totalReviewCount = totalReviewCount;
        this.myReview = myReview;
        this.myRating = myRating;
        this.instantTask = instantTask;
        this.scheduledTask = scheduledTask;
        this.showOutstockedProduct = showOutstockedProduct;
        this.enableTookanAgent = enableTookanAgent;
        this.bufferSchedule = bufferSchedule;
        this.buttons = buttons;
        this.businessType = businessType;
        this.multipleProductInSingleCart = multipleProductInSingleCart;
        this.pdOrAppointment = pdOrAppointment;
        this.isStartEndTimeEnable = isStartEndTimeEnable;
        this.isStorefrontOpened = isStorefrontOpened;
        this.isReviewRatingEnabled = isReviewRatingEnabled;
        this.homeDelivery = homeDelivery;
        this.selfPickup = selfPickup;
        this.isMenuEnabled = isMenuEnabled ? 1 : 0;
        this.recurrinTask = isRecurringTask;
        this.customOrderActiveForStore = customOrderActiveForStore;
        this.deliveryCharge = deliveryCharge;
        this.merchantAsDeliveryManager = merchantAsDeliveryManager;
        this.isWishlisted = isWishlisted;
    }

    public int getIsWishlisted() {
        return isWishlisted;
    }

    public void setIsWishlisted(int isWishlisted) {
        this.isWishlisted = isWishlisted;
    }

    public int getStaticAddressEnabledForCustomOrder() {
        return staticAddressEnabledForCustomOrder;
    }

    public int getIsStaticAddressEnabled() {
        return isStaticAddressEnabled;
    }

    public int getIsFreeDeliveryEnabled() {
        return isFreeDeliveryEnabled;
    }

    public void setIsFreeDeliveryEnabled(int isFreeDeliveryEnabled) {
        this.isFreeDeliveryEnabled = isFreeDeliveryEnabled;
    }

    public double getFree_delivery_amount() {
        return free_delivery_amount != null ? free_delivery_amount : 0.0;
    }

    public void setFree_delivery_amount(Double free_delivery_amount) {
        this.free_delivery_amount = free_delivery_amount;
    }

    public Double getFree_delivery_toggle() {
        return free_delivery_toggle;
    }

    public void setFree_delivery_toggle(Double free_delivery_toggle) {
        this.free_delivery_toggle = free_delivery_toggle;
    }

    public ArrayList<StoreTimingsArr> getStoreTimingsArr() {
        return storeTimingsArr;
    }

    public void setStoreTimingsArr(ArrayList<StoreTimingsArr> storeTimingsArr) {
        this.storeTimingsArr = storeTimingsArr;
    }

    public int getIsBufferSlotForEverydayEnabled() {
        return isBufferSlotForEverydayEnabled;
    }

    public void setIsBufferSlotForEverydayEnabled(int isBufferSlotForEverydayEnabled) {
        this.isBufferSlotForEverydayEnabled = isBufferSlotForEverydayEnabled;
    }

    public int getLoyalty_point_benefit_blocked() {
        return loyalty_point_benefit_blocked;
    }

    public void setLoyalty_point_benefit_blocked(int loyalty_point_benefit_blocked) {
        this.loyalty_point_benefit_blocked = loyalty_point_benefit_blocked;
    }

    public int getAvailableForSelfPickup() {
        return availableForSelfPickup;
    }

    public void setAvailableForSelfPickup(int availableForSelfPickup) {
        this.availableForSelfPickup = availableForSelfPickup;
    }

    public int getAvailableForHomeDelivery() {
        return availableForHomeDelivery;
    }

    public void setAvailableForHomeDelivery(int availableForHomeDelivery) {
        this.availableForHomeDelivery = availableForHomeDelivery;
    }

    public int getAvailableForPickAndDrop() {
        return availableForPickAndDrop;
    }

    public void setAvailableForPickAndDrop(int availableForPickAndDrop) {
        this.availableForPickAndDrop = availableForPickAndDrop;
    }

    public boolean isVegFilterActive() {
        if (isVegFilterActive == 1)
            return true;
        return false;
    }

    public void setIsVegFilterActive(int isVegFilterActive) {
        this.isVegFilterActive = isVegFilterActive;
    }

    public int getShowMerchantTimings() {
        return showMerchantTimings;
    }

    public String getCustom_tag_for_merchant() {
        return custom_tag_for_merchant;
    }

    public void setCustom_tag_for_merchant(String custom_tag_for_merchant) {
        this.custom_tag_for_merchant = custom_tag_for_merchant;
    }

    public PaymentSettings getPaymentSettings() {
        return paymentSettings;
    }

    public void setPaymentSettings(PaymentSettings paymentSettings) {
        this.paymentSettings = paymentSettings;
    }

    public int getDisplayRangeIntervals() {
        return displayRangeIntervals;
    }

    public void setDisplayRangeIntervals(int displayRangeIntervals) {
        this.displayRangeIntervals = displayRangeIntervals;
    }

    public int getCustomOrderActiveForStore() {
        return customOrderActiveForStore;
    }

    public void setCustomOrderActiveForStore(int customOrderActiveForStore) {
        this.customOrderActiveForStore = customOrderActiveForStore;
    }


    public double getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(double deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public List<PaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public boolean getIsPdFlow() {
        return isPdFlow == 1;
    }

    public void setIsPdFlow(int isPdFlow) {
        this.isPdFlow = isPdFlow;
    }

    public int getPdFlow() {
        return isPdFlow;
    }

    public List<Result> getReqCatalogues() {
        return reqCatalogues;
    }

    public int getIsBannersEnabledMerchant() {
        return isBannersEnabledMerchant;
    }

    public void setIsBannersEnabledMerchant(final int isBannersEnabled) {
        this.isBannersEnabledMerchant = isBannersEnabled;
    }

    public int getCreateDeliverySlots() {
        return createDeliverySlots;
    }


    public List<BannerImage> getBannerImages() {
        return bannerImages;
    }

    public void setBannerImages(final List<BannerImage> bannerImages) {
        this.bannerImages = bannerImages;
    }

    public int getPreBookingBuffer() {
        return preBookingBuffer;
    }

    public void setPreBookingBuffer(int preBookingBuffer) {
        this.preBookingBuffer = preBookingBuffer;
    }

    public int getMerchantAsDeliveryManager() {
        return merchantAsDeliveryManager;
    }

    public void setMerchantAsDeliveryManager(int merchantAsDeliveryManager) {
        this.merchantAsDeliveryManager = merchantAsDeliveryManager;
    }

    public int getIsOrderAgentschedulingEnabled() {
        return isOrderAgentschedulingEnabled;
    }

    public void setIsOrderAgentschedulingEnabled(int isOrderAgentschedulingEnabled) {
        this.isOrderAgentschedulingEnabled = isOrderAgentschedulingEnabled;
    }

    public boolean isOrderAgentShedulingEnabled() {
        if (isOrderAgentschedulingEnabled == 1)
            return true;
        return false;
    }

    public int getMerchantDeliveryTime() {
        return merchantDeliveryTime;
    }

    public void setMerchantDeliveryTime(int merchantDeliveryTime) {
        this.merchantDeliveryTime = merchantDeliveryTime;
    }

    public int getOrderPreparationTime() {
        return orderPreparationTime;
    }

    public void setOrderPreparationTime(int orderPreparationTime) {
        this.orderPreparationTime = orderPreparationTime;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(int deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public int getRecurrinTask() {
        return recurrinTask;
    }

    public void setRecurrinTask(int recurrinTask) {
        this.recurrinTask = recurrinTask;
    }

    public int getEstimatedAddOn() {
        return estimatedAddOn;
    }

    public void setEstimatedAddOn(int estimatedAddOn) {
        this.estimatedAddOn = estimatedAddOn;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Double getMinimumSelfPickupAmount() {
        return minimumSelfPickupAmount != null ? minimumSelfPickupAmount : 0.0;
    }

    public void setMinimumSelfPickupAmount(Double minimumSelfPickupAmount) {
        this.minimumSelfPickupAmount = minimumSelfPickupAmount;
    }

    public Integer getMultipleProductInSingleCart() {
        return multipleProductInSingleCart;
    }

    public void setMultipleProductInSingleCart(Integer multipleProductInSingleCart) {
        this.multipleProductInSingleCart = multipleProductInSingleCart;
    }

    public Integer getPdOrAppointment() {
        return pdOrAppointment;
    }

    public void setPdOrAppointment(Integer pdOrAppointment) {
        this.pdOrAppointment = pdOrAppointment;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public Integer getShowOutstockedProduct() {
        return showOutstockedProduct;
    }

    public void setShowOutstockedProduct(Integer showOutstockedProduct) {
        this.showOutstockedProduct = showOutstockedProduct;
    }

    public Integer getScheduledTask() {
        return scheduledTask;
    }

    public void setScheduledTask(Integer scheduledTask) {
        this.scheduledTask = scheduledTask;
    }

    public Number getBufferSchedule() {
        return bufferSchedule != null ? bufferSchedule : 5;
    }

    public void setBufferSchedule(Number bufferSchedule) {
        this.bufferSchedule = bufferSchedule;
    }

    public Integer getInstantTask() {
        return instantTask;
    }

    public void setInstantTask(Integer instantTask) {
        this.instantTask = instantTask;
    }

    public Integer getIsStartEndTimeEnable() {
        return isStartEndTimeEnable != null ? isStartEndTimeEnable : 0;
    }

    public void setIsStartEndTimeEnable(Integer isStartEndTimeEnable) {
        this.isStartEndTimeEnable = isStartEndTimeEnable;
    }

    public Number getStoreRating() {
        return storeRating != null ? storeRating : 0;
    }

    public void setStoreRating(Number storeRating) {
        this.storeRating = storeRating;
    }

    public ArrayList<LastReviewRating> getLastReviewRating() {
        return lastReviewRating != null ? lastReviewRating : new ArrayList<LastReviewRating>();
    }

    public void setLastReviewRating(ArrayList<LastReviewRating> lastReviewRating) {
        this.lastReviewRating = lastReviewRating;
    }

    public int getTotalRatingsCount() {
        return totalRatingsCount;
    }

    public void setTotalRatingsCount(int totalRatingsCount) {
        this.totalRatingsCount = totalRatingsCount;
    }

    public int getTotalReviewCount() {
        return totalReviewCount;
    }

    public void setTotalReviewCount(int totalReviewCount) {
        this.totalReviewCount = totalReviewCount;
    }

    public String getMyReview() {
        return myReview != null ? myReview : "";
    }

    public void setMyReview(String myReview) {
        this.myReview = myReview;
    }

    public Number getMyRating() {
        return myRating != null ? myRating : 0;
    }

    public void setMyRating(Number myRating) {
        this.myRating = myRating;
    }

    public String getPhone() {
        return phone != null ? phone : "";
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getStorefrontUserId() {
        return storefrontUserId;
    }

    public void setStorefrontUserId(Integer storefrontUserId) {
        this.storefrontUserId = storefrontUserId;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public Object getThumbList() {
        return thumbList;
    }

    public void setThumbList(Object thumbList) {
        this.thumbList = thumbList;
    }

    public String getStoreName() {
        return storeName != null ? storeName : "";
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getAddress() {
        return address != null ? address : "";
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email != null ? email : "";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayAddress() {
        return displayAddress != null && !displayAddress.isEmpty() ? displayAddress : getAddress();
    }

    public void setDisplayAddress(String displayAddress) {
        this.displayAddress = displayAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLatitude() {
        if (latitude != null && !latitude.isEmpty()) {
            return latitude;
        } else {
            return "0.0";
        }

    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        if (longitude != null && !longitude.isEmpty()) {
            return longitude;
        } else {
            return "0.0";
        }
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Number getServingDistance() {
        return servingDistance;
    }

    public void setServingDistance(Number servingDistance) {
        this.servingDistance = servingDistance;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Double getMerchantMinimumOrder() {
        return merchantMinimumOrder != null ? merchantMinimumOrder : 0.0;
    }

    public void setMerchantMinimumOrder(Double merchantMinimumOrder) {
        this.merchantMinimumOrder = merchantMinimumOrder;
    }

    public Button getButtons() {
        return buttons != null ? buttons : new Button();
    }

    public void setButtons(Button buttons) {
        this.buttons = buttons;
    }

    public Integer getEnableTookanAgent() {
        return enableTookanAgent;
    }

    public void setEnableTookanAgent(Integer enableTookanAgent) {
        this.enableTookanAgent = enableTookanAgent;
    }

    public List<com.tookancustomer.models.ProductCatalogueData.Datum> getProductList() {
        return productList;
    }

    public void setProductList(List<com.tookancustomer.models.ProductCatalogueData.Datum> productList) {
        this.productList = productList;
    }

    public int getIsStorefrontOpened() {
//        return 1;
        /*if (businessType != null && isStorefrontOpened != null) {
            return businessType == Constants.BusinessType.PRODUCTS_BUSINESS_TYPE ? isStorefrontOpened : 1;
        } else {
            return 0;
        }*/
        if (isStorefrontOpened != null) {
            return isStorefrontOpened;
        } else {
            return 0;
        }
    }


    public void setIsStorefrontOpened(int isStorefrontOpened) {
        this.isStorefrontOpened = isStorefrontOpened;
    }

    public boolean isStoreAvailableForBooking() {
        return (getIsStorefrontOpened() == 1 || scheduledTask == 1);
    }

    public int getIsReviewRatingEnabled() {
        return isReviewRatingEnabled;
    }

    public void setIsReviewRatingEnabled(int isReviewRatingEnabled) {
        this.isReviewRatingEnabled = isReviewRatingEnabled;
    }

    public int getHomeDelivery() {
        return homeDelivery;
    }

    public void setHomeDelivery(int homeDelivery) {
        this.homeDelivery = homeDelivery;
    }

    public int getSelfPickup() {
        return selfPickup;
    }

    public void setSelfPickup(int selfPickup) {
        this.selfPickup = selfPickup;
    }

    public int getSelectedPickupMode() {
        return selectedPickupMode;
    }

    public void setSelectedPickupMode(int selectedPickupMode) {
        this.selectedPickupMode = selectedPickupMode;
    }

    public int getIs_sponsored() {
        return is_sponsored;
    }

    public void setIs_sponsored(int is_sponsored) {
        this.is_sponsored = is_sponsored;
    }

    public String getMerchantDiscount() {
        return merchantDiscount != null ? Utils.getDecimalFormatForPercentage().format(merchantDiscount) : "0";
    }

    public void setMerchantDiscount(Double merchantDiscount) {
        this.merchantDiscount = merchantDiscount;
    }

    public String getBusinessCategoriesName() {
        return businessCategoriesName != null ? businessCategoriesName.replace(",", " \u2022 ") : "";
    }


    public void setBusinessCategoriesName(String businessCategoriesName) {
        this.businessCategoriesName = businessCategoriesName;
    }

    public boolean getIsMenuEnabled() {
        return isMenuEnabled == 1;
    }

    public void setIsMenuEnabled(int isMenuEnabled) {
        this.isMenuEnabled = isMenuEnabled;
    }

    public int getBusinessCatalogMappingEnabled() {
        return businessCatalogMappingEnabled;
    }

    public void setBusinessCatalogMappingEnabled(int businessCatalogMappingEnabled) {
        this.businessCatalogMappingEnabled = businessCatalogMappingEnabled;
    }

    public int getHasCategories() {
        return hasCategories;
    }

    public void setHasCategories(int hasCategories) {
        this.hasCategories = hasCategories;
    }

    public int getShowProductImage() {
        return showProductImage;
    }

    public void setShowProductImage(int showProductImage) {
        this.showProductImage = showProductImage;
    }

    public int getCategoryLayoutType() {
        return categoryLayoutType;
    }

    public void setCategoryLayoutType(int categoryLayoutType) {
        this.categoryLayoutType = categoryLayoutType;
    }

    public int getProductLayoutType() {
        return productLayoutType;
    }

    public void setProductLayoutType(int productLayoutType) {
        this.productLayoutType = productLayoutType;
    }

    public int getCategoryButtonType() {
        return categoryButtonType;
    }

    public void setCategoryButtonType(int categoryButtonType) {
        this.categoryButtonType = categoryButtonType;
    }

    public int getProductButtonType() {
        return productButtonType != 0 ? productButtonType : MerchantCatalogConstants.ButtonTypes.ADD_AND_REMOVE_BUTTON.buttonValue; //If button type is not set, then return + - buttons
    }

    public void setProductButtonType(int productButtonType) {
        this.productButtonType = productButtonType;
    }

    public int getLastLevelCatalogView() {
        return lastLevelCatalogView;
    }

    public void setLastLevelCatalogView(int lastLevelCatalogView) {
        this.lastLevelCatalogView = lastLevelCatalogView;
    }

    public int getCanServe() {
        return canServe;
    }

    public int getIsSingleStorefront() {
        return isSingleStorefront;
    }

    public void setIsSingleStorefront(int isSingleStorefront) {
        this.isSingleStorefront = isSingleStorefront;
    }

}
