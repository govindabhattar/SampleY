package com.tookancustomer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cl-macmini-83 on 17/12/16.
 */

public class MetaInfo {
    @SerializedName("total_count")
    @Expose
    private int totalCount;
    @SerializedName("more_products_available")
    @Expose
    private int moreProductsAvailable = 1;
    @SerializedName("discount")
    @Expose
    private float discount;
    @SerializedName("parent_category_id")
    @Expose
    private String parentCategoryId;
    @SerializedName("show_images")
    @Expose
    private int showImages;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getMoreProductsAvailable() {
        return moreProductsAvailable;
    }

    public void setMoreProductsAvailable(int moreProductsAvailable) {
        this.moreProductsAvailable = moreProductsAvailable;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public String getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(String parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public int getShowImages() {
        return showImages;
    }
}
