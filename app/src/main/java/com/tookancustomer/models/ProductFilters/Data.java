package com.tookancustomer.models.ProductFilters;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Serializable{

    @SerializedName("filterAndValues")
    @Expose
    private List<FilterAndValue> filterAndValues = null;
    @SerializedName("min_price")
    @Expose
    private int minPrice;
    @SerializedName("max_price")
    @Expose
    private int maxPrice;

    public List<FilterAndValue> getFilterAndValues() {
        return filterAndValues;
    }

    public void setFilterAndValues(List<FilterAndValue> filterAndValues) {
        this.filterAndValues = filterAndValues;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

}