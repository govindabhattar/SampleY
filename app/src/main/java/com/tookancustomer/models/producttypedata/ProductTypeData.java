
package com.tookancustomer.models.producttypedata;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductTypeData {

    @SerializedName("product_type_id")
    @Expose
    private Integer productTypeId;
    @SerializedName("marketplace_user_id")
    @Expose
    private Integer marketplaceUserId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("custom_fields")
    @Expose
    private List<CustomField> customFields = null;
    @SerializedName("creation_datetime")
    @Expose
    private String creationDatetime;
    @SerializedName("updation_datetime")
    @Expose
    private String updationDatetime;
    @SerializedName("is_enabled")
    @Expose
    private Integer isEnabled;

    public Integer getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Integer productTypeId) {
        this.productTypeId = productTypeId;
    }

    public Integer getMarketplaceUserId() {
        return marketplaceUserId;
    }

    public void setMarketplaceUserId(Integer marketplaceUserId) {
        this.marketplaceUserId = marketplaceUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CustomField> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<CustomField> customFields) {
        this.customFields = customFields;
    }

    public String getCreationDatetime() {
        return creationDatetime;
    }

    public void setCreationDatetime(String creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    public String getUpdationDatetime() {
        return updationDatetime;
    }

    public void setUpdationDatetime(String updationDatetime) {
        this.updationDatetime = updationDatetime;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

}
