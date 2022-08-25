package com.tookancustomer.models.reqCatalogue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Ashutosh Ojha on 2019-05-07.
 */
public class ReqCatalogue implements Serializable {

    @SerializedName("catalogue_id")
    @Expose
    private Integer catalogueId;
    @SerializedName("parent_category_id")
    @Expose
    private Integer parentCategoryId;
    @SerializedName("super_catalogue_id")
    @Expose
    private Object superCatalogueId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("form_id")
    @Expose
    private Integer formId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("name_json")
    @Expose
    private String nameJson;
    @SerializedName("description_json")
    @Expose
    private String descriptionJson;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("thumb_url")
    @Expose
    private String thumbUrl;
    @SerializedName("thumb_list")
    @Expose
    private Object thumbList;
    @SerializedName("layout_type")
    @Expose
    private Integer layoutType;
    @SerializedName("layout_id")
    @Expose
    private String layoutId;
    @SerializedName("priority")
    @Expose
    private Integer priority;
    @SerializedName("is_dummy")
    @Expose
    private Integer isDummy;
    @SerializedName("is_enabled")
    @Expose
    private Integer isEnabled;
    @SerializedName("level")
    @Expose
    private Integer level;
    @SerializedName("address")
    @Expose
    private Object address;
    @SerializedName("creation_datetime")
    @Expose
    private String creationDatetime;
    @SerializedName("update_datetime")
    @Expose
    private String updateDatetime;
    @SerializedName("latitude")
    @Expose
    private Object latitude;
    @SerializedName("longitude")
    @Expose
    private Object longitude;
    @SerializedName("has_children")
    @Expose
    private Integer hasChildren;
    @SerializedName("has_products")
    @Expose
    private Integer hasProducts;
    @SerializedName("is_deleted")
    @Expose
    private Integer isDeleted;
    @SerializedName("child_layout_type")
    @Expose
    private Integer childLayoutType;
    @SerializedName("admin_catalogue_id")
    @Expose
    private Object adminCatalogueId;
    @SerializedName("products_has_image")
    @Expose
    private Integer productsHasImage;
    @SerializedName("is_side_order")
    @Expose
    private Integer isSideOrder;
    @SerializedName("show_catalog")
    @Expose
    private Integer showCatalog;
    @SerializedName("has_active_children")
    @Expose
    private Integer hasActiveChildren;
    @SerializedName("is_required")
    @Expose
    private Integer isRequired;
    @SerializedName("depth")
    @Expose
    private Integer depth;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("marketplace_user_id")
    @Expose
    private Integer marketplaceUserId;
    @SerializedName("marketplace_storefront_user_id")
    @Expose
    private Integer marketplaceStorefrontUserId;
    @SerializedName("is_active")
    @Expose
    private Integer isActive;
    @SerializedName("create_datetime")
    @Expose
    private String createDatetime;

    public Integer getCatalogueId() {
        return catalogueId;
    }

    public void setCatalogueId(Integer catalogueId) {
        this.catalogueId = catalogueId;
    }

    public Integer getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Integer parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public Object getSuperCatalogueId() {
        return superCatalogueId;
    }

    public void setSuperCatalogueId(Object superCatalogueId) {
        this.superCatalogueId = superCatalogueId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFormId() {
        return formId;
    }

    public void setFormId(Integer formId) {
        this.formId = formId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameJson() {
        return nameJson;
    }

    public void setNameJson(String nameJson) {
        this.nameJson = nameJson;
    }

    public String getDescriptionJson() {
        return descriptionJson;
    }

    public void setDescriptionJson(String descriptionJson) {
        this.descriptionJson = descriptionJson;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public Integer getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(Integer layoutType) {
        this.layoutType = layoutType;
    }

    public String getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(String layoutId) {
        this.layoutId = layoutId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getIsDummy() {
        return isDummy;
    }

    public void setIsDummy(Integer isDummy) {
        this.isDummy = isDummy;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
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

    public Integer getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(Integer hasChildren) {
        this.hasChildren = hasChildren;
    }

    public Integer getHasProducts() {
        return hasProducts;
    }

    public void setHasProducts(Integer hasProducts) {
        this.hasProducts = hasProducts;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getChildLayoutType() {
        return childLayoutType;
    }

    public void setChildLayoutType(Integer childLayoutType) {
        this.childLayoutType = childLayoutType;
    }

    public Object getAdminCatalogueId() {
        return adminCatalogueId;
    }

    public void setAdminCatalogueId(Object adminCatalogueId) {
        this.adminCatalogueId = adminCatalogueId;
    }

    public Integer getProductsHasImage() {
        return productsHasImage;
    }

    public void setProductsHasImage(Integer productsHasImage) {
        this.productsHasImage = productsHasImage;
    }

    public Integer getIsSideOrder() {
        return isSideOrder;
    }

    public void setIsSideOrder(Integer isSideOrder) {
        this.isSideOrder = isSideOrder;
    }

    public Integer getShowCatalog() {
        return showCatalog;
    }

    public void setShowCatalog(Integer showCatalog) {
        this.showCatalog = showCatalog;
    }

    public Integer getHasActiveChildren() {
        return hasActiveChildren;
    }

    public void setHasActiveChildren(Integer hasActiveChildren) {
        this.hasActiveChildren = hasActiveChildren;
    }

    public Integer getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Integer isRequired) {
        this.isRequired = isRequired;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMarketplaceUserId() {
        return marketplaceUserId;
    }

    public void setMarketplaceUserId(Integer marketplaceUserId) {
        this.marketplaceUserId = marketplaceUserId;
    }

    public Integer getMarketplaceStorefrontUserId() {
        return marketplaceStorefrontUserId;
    }

    public void setMarketplaceStorefrontUserId(Integer marketplaceStorefrontUserId) {
        this.marketplaceStorefrontUserId = marketplaceStorefrontUserId;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }
}
