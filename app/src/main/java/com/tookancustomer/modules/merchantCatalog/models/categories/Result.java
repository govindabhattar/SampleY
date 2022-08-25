
package com.tookancustomer.modules.merchantCatalog.models.categories;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.ProductCatalogueData.Datum;
import com.tookancustomer.models.ThumbList;

import java.io.Serializable;
import java.util.ArrayList;

public class Result implements Serializable {

    public ArrayList<Result> cachedCategoryList;
    public ArrayList<Datum> cachedProductList;

    @SerializedName("catalogue_id")
    @Expose
    public int catalogueId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("image_url")
    @Expose
    public String imageUrl;
    @SerializedName("thumb_url")
    @Expose
    public String thumbUrl;

    @SerializedName("thumb_list")
    @Expose
    public ThumbList thumbList;
    @SerializedName("has_products")
    @Expose
    public int hasProducts;
    @SerializedName("is_side_order")
    @Expose
    public int isSideOrder;
    @SerializedName("has_children")
    @Expose
    public int hasChildren;
    @SerializedName("is_enabled")
    @Expose
    public int isEnabled;

    @SerializedName("is_required")
    @Expose
    public int isRequired;


    public int getIsRequired() {
        return isRequired;
    }

    public int getCatalogueId() {
        return catalogueId;
    }

    public String getName() {
        return name != null ? name : "";
    }

    public String getDescription() {
        return description != null ? description : "";
    }

    public String getImageUrl() {
        return imageUrl != null ? imageUrl : "";
    }

    public String getThumbUrl() {
        if (thumbList != null && !thumbList.get250x250().isEmpty()) {
            thumbUrl = thumbList.get250x250();
        }
        return thumbUrl != null ? thumbUrl : "";
    }

    public ThumbList getThumbList() {
        return thumbList;
    }

    public int getHasProducts() {
        return hasProducts;
    }

    public int getIsSideOrder() {
        return isSideOrder;
    }

    public int getHasChildren() {
        return hasChildren;
    }

    public int getIsEnabled() {
        return isEnabled;
    }

    public ArrayList<Result> getCachedCategoryList() {
        return cachedCategoryList;
    }

    public void setCachedCategoryList(ArrayList<Result> cachedCategoryList) {
        this.cachedCategoryList = cachedCategoryList;
    }

    public ArrayList<Datum> getCachedProductList() {
        return cachedProductList;
    }

    public void setCachedProductList(ArrayList<Datum> cachedProductList) {
        this.cachedProductList = cachedProductList;
    }
}
