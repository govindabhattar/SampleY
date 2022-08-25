
package com.tookancustomer.models.ProductCatalogueData.productWithSellersData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Seller {

    @SerializedName("seller_id")
    @Expose
    private Integer sellerId;
    @SerializedName("seller_name")
    @Expose
    private String sellerName;
    @SerializedName("price")
    @Expose
    private Integer price;

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

}
