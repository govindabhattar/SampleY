
package com.tookancustomer.models.businessCategoriesData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("marketplace_user_id")
    @Expose
    private int marketplaceUserId;
    @SerializedName("creation_datetime")
    @Expose
    private String creationDatetime;
    @SerializedName("update_datetime")
    @Expose
    private String updateDatetime;
    @SerializedName("is_active")
    @Expose
    private int isActive;
    @SerializedName("is_custom_order_active")
    @Expose
    private int isCustomOrderActive;
    @SerializedName("thumb_list")
    @Expose
    private ThumbList thumb_list;
    @SerializedName("is_all_category")
    @Expose
    private int is_all_category;

    @SerializedName("external_link")
    @Expose
    private String externalLink;


    public String getExternalLink() {
        return externalLink==null?"":externalLink;
    }

    private boolean isSelected;

    public Result(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public ThumbList getThumb_list() {
        return thumb_list;
    }

    public int getId() {
        return id;
    }

    public int getIs_all_category() {
        return is_all_category;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getMarketplaceUserId() {
        return marketplaceUserId;
    }

    public void setMarketplaceUserId(int marketplaceUserId) {
        this.marketplaceUserId = marketplaceUserId;
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

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getIsCustomOrderActive() {
        return isCustomOrderActive;
    }

    public void setIsCustomOrderActive(int isCustomOrderActive) {
        this.isCustomOrderActive = isCustomOrderActive;
    }
}
