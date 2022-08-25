package com.tookancustomer.modules.merchantCatalog.models.categories;

import java.io.Serializable;
import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MerchantCategoriesData implements Serializable {

    @SerializedName("result")
    @Expose
    public ArrayList<Result> result = new ArrayList<>();
    @SerializedName("has_products")
    @Expose
    public int hasProducts;
    @SerializedName("has_children")
    @Expose
    public int hasChildren;
    @SerializedName("has_image")
    @Expose
    public int hasImage;

    public ArrayList<Result> getResult() {
        return result;
    }

    public int getHasProducts() {
        return hasProducts;
    }

    public int getHasChildren() {
        return hasChildren;
    }

    public int getHasImage() {
        return hasImage;
    }
}