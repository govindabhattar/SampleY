package com.tookancustomer.models.UnavailableProductData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UnavailableProductData {
    @SerializedName("unavailable_products")
    @Expose
    private List<UnavailableProduct> unavailableProducts = new ArrayList<>();

    public List<UnavailableProduct> getUnavailableProducts() {
        return unavailableProducts;
    }

    public void setUnavailableProducts(List<UnavailableProduct> unavailableProducts) {
        this.unavailableProducts = unavailableProducts;
    }

}
