
package com.tookancustomer.models.bannersData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Banner {


//            "external_link_toggle":1,


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("marketplace_user_id")
    @Expose
    private Integer marketplaceUserId;
    @SerializedName("merchant_id")
    @Expose
    private Integer merchantId;
    @SerializedName("creation_datetime")
    @Expose
    private String creationDatetime;
    @SerializedName("update_datetime")
    @Expose
    private String updateDatetime;
    @SerializedName("external_link")
    @Expose
    private String externalLink;
    @SerializedName("is_active")
    @Expose
    private Integer isActive;
    @SerializedName("external_link_toggle")
    @Expose
    private int externalLinkToggle;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("mobile_image")
    @Expose
    private String mobileImage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        if (name == null)
            return "";
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getMarketplaceUserId() {
        if (marketplaceUserId == null)
            return 0;
        return marketplaceUserId;
    }

    public void setMarketplaceUserId(Integer marketplaceUserId) {
        this.marketplaceUserId = marketplaceUserId;
    }

    public Integer getMerchantId() {
        if (merchantId == null)
            return 0;
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
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

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMobileImage() {
        return mobileImage;
    }

    public void setMobileImage(String mobileImage) {
        this.mobileImage = mobileImage;
    }

    public String getExternalLink() {
        return externalLink;
    }

    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }

    public int getExternalLinkToggle() {
        return externalLinkToggle;
    }

    public void setExternalLinkToggle(int externalLinkToggle) {
        this.externalLinkToggle = externalLinkToggle;
    }
}
