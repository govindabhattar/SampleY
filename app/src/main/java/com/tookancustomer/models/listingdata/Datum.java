
package com.tookancustomer.models.listingdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("parent_category_id")
    @Expose
    private Object parentCategoryId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("marketplace_user_id")
    @Expose
    private Object marketplaceUserId;
    @SerializedName("form_id")
    @Expose
    private Integer formId;
    @SerializedName("is_enabled")
    @Expose
    private Integer isEnabled;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("multi_image_url")
    @Expose
    private Object multiImageUrl;
    @SerializedName("thumb_url")
    @Expose
    private String thumbUrl;
    @SerializedName("thumb_list")
    @Expose
    private Object thumbList;
    @SerializedName("geofence_id")
    @Expose
    private Object geofenceId;
    @SerializedName("layout_id")
    @Expose
    private String layoutId;
    @SerializedName("priority")
    @Expose
    private Object priority;
    @SerializedName("rate_card")
    @Expose
    private Object rateCard;
    @SerializedName("creation_datetime")
    @Expose
    private String creationDatetime;
    @SerializedName("update_datetime")
    @Expose
    private String updateDatetime;
    @SerializedName("is_deleted")
    @Expose
    private Integer isDeleted;
    @SerializedName("layout_type")
    @Expose
    private Integer layoutType;
    @SerializedName("latitude")
    @Expose
    private Object latitude;
    @SerializedName("longitude")
    @Expose
    private Object longitude;
    @SerializedName("address")
    @Expose
    private Object address;
    @SerializedName("display_address")
    @Expose
    private Object displayAddress;
    @SerializedName("is_edible")
    @Expose
    private Integer isEdible;
    @SerializedName("is_veg")
    @Expose
    private Integer isVeg;
    @SerializedName("fatafat_prod_id")
    @Expose
    private Integer fatafatProdId;
    @SerializedName("inventory_enabled")
    @Expose
    private Integer inventoryEnabled;
    @SerializedName("available_quantity")
    @Expose
    private Integer availableQuantity;
    @SerializedName("agent_id")
    @Expose
    private Integer agentId;
    @SerializedName("enable_tookan_agent")
    @Expose
    private Integer enableTookanAgent;
    @SerializedName("product_type_id")
    @Expose
    private Integer productTypeId;
    @SerializedName("custom_fields")
    @Expose
    private String customFields;
    @SerializedName("display_custom_fields")
    @Expose
    private String displayCustomFields;
    @SerializedName("pre_booking_buffer")
    @Expose
    private Object preBookingBuffer;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("unit")
    @Expose
    private Integer unit;
    @SerializedName("unit_type")
    @Expose
    private Integer unitType;
    @SerializedName("last_review_rating")
    @Expose
    private String lastReviewRating;
    @SerializedName("total_review_count")
    @Expose
    private Integer totalReviewCount;
    @SerializedName("total_ratings_count")
    @Expose
    private Integer totalRatingsCount;
    @SerializedName("layout_data")
    @Expose
    private LayoutData layoutData;

    @SerializedName("is_review_rating_enabled")
    @Expose
    private int is_review_rating_enabled = 0;

    @SerializedName("product_rating")
    @Expose
    private Number productRating = 0;

    public Number getProductRating() {
        return productRating;
    }

    public void setProductRating(Number productRating) {
        this.productRating = productRating;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Object parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Object getMarketplaceUserId() {
        return marketplaceUserId;
    }

    public void setMarketplaceUserId(Object marketplaceUserId) {
        this.marketplaceUserId = marketplaceUserId;
    }

    public Integer getFormId() {
        return formId;
    }

    public void setFormId(Integer formId) {
        this.formId = formId;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Object getMultiImageUrl() {
        return multiImageUrl;
    }

    public void setMultiImageUrl(Object multiImageUrl) {
        this.multiImageUrl = multiImageUrl;
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

    public Object getGeofenceId() {
        return geofenceId;
    }

    public void setGeofenceId(Object geofenceId) {
        this.geofenceId = geofenceId;
    }

    public String getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(String layoutId) {
        this.layoutId = layoutId;
    }

    public Object getPriority() {
        return priority;
    }

    public void setPriority(Object priority) {
        this.priority = priority;
    }

    public Object getRateCard() {
        return rateCard;
    }

    public void setRateCard(Object rateCard) {
        this.rateCard = rateCard;
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

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(Integer layoutType) {
        this.layoutType = layoutType;
    }

    public Object getLatitude() {
        return latitude;
    }

    public void setLatitude(Object latitude) {
        this.latitude = latitude;
    }

    public Object getLongitude() {
        return longitude;
    }

    public void setLongitude(Object longitude) {
        this.longitude = longitude;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
    }

    public Object getDisplayAddress() {
        return displayAddress;
    }

    public void setDisplayAddress(Object displayAddress) {
        this.displayAddress = displayAddress;
    }

    public Integer getIsEdible() {
        return isEdible;
    }

    public void setIsEdible(Integer isEdible) {
        this.isEdible = isEdible;
    }

    public Integer getIsVeg() {
        return isVeg;
    }

    public void setIsVeg(Integer isVeg) {
        this.isVeg = isVeg;
    }

    public Integer getFatafatProdId() {
        return fatafatProdId;
    }

    public void setFatafatProdId(Integer fatafatProdId) {
        this.fatafatProdId = fatafatProdId;
    }

    public Integer getInventoryEnabled() {
        return inventoryEnabled;
    }

    public void setInventoryEnabled(Integer inventoryEnabled) {
        this.inventoryEnabled = inventoryEnabled;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public Integer getEnableTookanAgent() {
        return enableTookanAgent;
    }

    public void setEnableTookanAgent(Integer enableTookanAgent) {
        this.enableTookanAgent = enableTookanAgent;
    }

    public Integer getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Integer productTypeId) {
        this.productTypeId = productTypeId;
    }

    public String getCustomFields() {
        return customFields;
    }

    public void setCustomFields(String customFields) {
        this.customFields = customFields;
    }

    public String getDisplayCustomFields() {
        return displayCustomFields;
    }

    public void setDisplayCustomFields(String displayCustomFields) {
        this.displayCustomFields = displayCustomFields;
    }

    public Object getPreBookingBuffer() {
        return preBookingBuffer;
    }

    public void setPreBookingBuffer(Object preBookingBuffer) {
        this.preBookingBuffer = preBookingBuffer;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public Integer getUnitType() {
        return unitType;
    }

    public void setUnitType(Integer unitType) {
        this.unitType = unitType;
    }

    public String getLastReviewRating() {
        return lastReviewRating;
    }

    public void setLastReviewRating(String lastReviewRating) {
        this.lastReviewRating = lastReviewRating;
    }

    public Integer getTotalReviewCount() {
        return totalReviewCount;
    }

    public void setTotalReviewCount(Integer totalReviewCount) {
        this.totalReviewCount = totalReviewCount;
    }

    public Integer getTotalRatingsCount() {
        return totalRatingsCount;
    }

    public void setTotalRatingsCount(Integer totalRatingsCount) {
        this.totalRatingsCount = totalRatingsCount;
    }

    public LayoutData getLayoutData() {
        return layoutData;
    }

    public void setLayoutData(LayoutData layoutData) {
        this.layoutData = layoutData;
    }

    public int getIs_review_rating_enabled() {
        return is_review_rating_enabled;
    }

    public void setIs_review_rating_enabled(int is_review_rating_enabled) {
        this.is_review_rating_enabled = is_review_rating_enabled;
    }


}
