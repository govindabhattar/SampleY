
package com.tookancustomer.models.allTasks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MultiPrice {

    @SerializedName("unit")
    @Expose
    private Integer unit;
    @SerializedName("price_type")
    @Expose
    private Integer priceType;
    @SerializedName("price")
    @Expose
    private Integer price;

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public Integer getPriceType() {
        return priceType;
    }

    public void setPriceType(Integer priceType) {
        this.priceType = priceType;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

}
