
package com.tookancustomer.models.ProductCatalogueData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.utility.Utils;

import java.io.Serializable;

public class Seller implements Serializable {

    @SerializedName("seller_id")
    @Expose
    private Integer sellerId = 0;
    @SerializedName(value = "store_name", alternate = {"seller_name"})
    @Expose
    private String storeName = "";
    @SerializedName("display_name")
    @Expose
    private String displayName = "";
    @SerializedName("logo")
    @Expose
    private String logo = "";
    @SerializedName("price")
    @Expose
    private Number price = 0;
    @SerializedName("seller_is_enabled")
    @Expose
    private Integer sellerIsEnabled = 0;

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public Number getPrice() {
        return price;
    }

    public void setPrice(Number price) {
        this.price = price;
    }

    public Integer getSellerIsEnabled() {
        return sellerIsEnabled;
    }

    public void setSellerIsEnabled(Integer sellerIsEnabled) {
        this.sellerIsEnabled = sellerIsEnabled;
    }

    public String getLogo() {
        return logo != null ? logo : "";
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getStoreName() {
        return storeName != null ? Utils.capitaliseWords(storeName) : "";
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
