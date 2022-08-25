
package com.tookancustomer.models.NLevelWorkFlowModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.models.ThumbList;
import com.tookancustomer.utility.Utils;

import java.io.Serializable;
import java.util.ArrayList;

public class Datum implements Serializable {
    private boolean isSelected = false;
    private boolean isExpanded = false;

    @SerializedName("catalogue_id")
    @Expose
    private Integer catalogueId;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("parent_category_id")
    @Expose
    private Integer parentCategoryId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("layout_type")
    @Expose
    private Integer layoutType;
    @SerializedName("form_id")
    @Expose
    private Integer formId;
    @SerializedName("name")
    @Expose
    private String name;
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
    private ThumbList thumbList;
    @SerializedName("child_layout_type")
    @Expose
    private Integer childLayoutType;
    @SerializedName("geofenceId")
    @Expose
    private Object geofence_id;
    @SerializedName("priority")
    @Expose
    private Object priority;
    @SerializedName("is_dummy")
    @Expose
    private Integer isDummy;
    @SerializedName("is_enabled")
    @Expose
    private Integer isEnabled;
    @SerializedName("level")
    @Expose
    private Integer level;
    @SerializedName("creation_datetime")
    @Expose
    private String creationDatetime;
    @SerializedName("parent_index")
    @Expose
    private Integer parentIndex;
    @SerializedName("products_has_image")
    @Expose
    private int productsHasImage;
    @SerializedName("layout_id")
    @Expose
    public String layoutId;
    @SerializedName("layout_data")
    @Expose
    public LayoutData layoutData;

    @SerializedName("sub_categories")
    @Expose
    public ArrayList<Datum> subCategories = new ArrayList<>();


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public ArrayList<Datum> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(ArrayList<Datum> subCategories) {
        this.subCategories = subCategories;
    }

    private final static long serialVersionUID = 2322956463332522742L;

    public Integer getCatalogueId() {
        return catalogueId != null ? catalogueId : 0;
    }

    public void setCatalogueId(Integer catalogueId) {
        this.catalogueId = catalogueId;
    }

    public Integer getParentCategoryId() {
        return parentCategoryId != null ? parentCategoryId : 0;
    }

    public void setParentCategoryId(Integer parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFormId() {
        return formId != null ? formId : 0;
    }

    public void setFormId(Integer formId) {
        this.formId = formId;
    }

    public String getName() {
        return name != null ? Utils.capitaliseWords(name) : "";
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

    public String getImageUrl() {
        return imageUrl != null ? imageUrl : "";
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThumbUrl400() {
        if (thumbList != null  && !thumbList.get400x400().isEmpty()) {
            thumbUrl = thumbList.get400x400();
        }
        return thumbUrl != null ? thumbUrl : "";
    }

    public String getThumbUrl() {
        if (thumbList != null && !thumbList.get250x250().isEmpty() && !thumbList.get400x400().isEmpty()) {
            if (getLayoutType() == Constants.NLevelLayoutType.LIST_LAYOUT.layoutValue) {
                thumbUrl = thumbList.get250x250();
            } else {
                thumbUrl = thumbList.get400x400();
            }
        }
//        return imageUrl != null ? imageUrl : "";
        return thumbUrl != null ? thumbUrl : "";
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }


    public Integer getChildLayoutType() {
        return childLayoutType != null ? childLayoutType : Constants.NLevelLayoutType.LIST_LAYOUT.layoutValue;
    }

    public void setChildLayoutType(Integer childLayoutType) {
        this.childLayoutType = childLayoutType;
    }

    public Object getPriority() {
        return priority;
    }

    public void setPriority(Object priority) {
        this.priority = priority;
    }

    public Integer getIsDummy() {
        return isDummy != null ? isDummy : 0;
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

    public String getCreationDatetime() {
        return creationDatetime;
    }

    public void setCreationDatetime(String creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    public Integer getParentIndex() {
        return parentIndex != null ? parentIndex : 0;
    }

    public void setParentIndex(Integer parentIndex) {
        this.parentIndex = parentIndex;
    }

    public Integer getProductId() {
        return productId != null ? productId : 0;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getLayoutType() {
        return layoutType != null ? layoutType : Constants.NLevelLayoutType.LIST_LAYOUT.layoutValue;
    }

    public void setLayoutType(Integer layoutType) {
        this.layoutType = layoutType;
    }

    public Object getGeofence_id() {
        return geofence_id;
    }

    public void setGeofence_id(Object geofence_id) {
        this.geofence_id = geofence_id;
    }

    public String getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(String layoutId) {
        this.layoutId = layoutId;
    }

    public LayoutData getLayoutData() {
        return layoutData != null ? layoutData : new LayoutData();
    }

    public void setLayoutData(LayoutData layoutData) {
        this.layoutData = layoutData;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public ThumbList getThumbList() {
        return thumbList != null ? thumbList : new ThumbList();
    }

    public void setThumbList(ThumbList thumbList) {
        this.thumbList = thumbList;
    }

    public int getProductsHasImage() {
        return productsHasImage;
    }
}
