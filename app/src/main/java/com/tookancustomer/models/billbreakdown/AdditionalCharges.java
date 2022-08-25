package com.tookancustomer.models.billbreakdown;

/*import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class AdditionalCharge {
    private boolean isDiscount = false;
    private BigDecimal isDeliverySurge;

    public BigDecimal getIsDeliverySurge() {
        return isDeliverySurge;
    }

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("percentage")
    @Expose
    private double percentage;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("amount")
    @Expose
    private double amount;

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    private String currencySymbol;
    public boolean isDiscount() {
        return isDiscount;
    }

    public void setDiscount(boolean discount) {
        isDiscount = discount;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

}*/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.List;

public class AdditionalCharges {
    private String currencySymbol;
    private boolean isDiscount = false;
    private BigDecimal isDeliverySurge;
    @SerializedName("onSubTotal")
    @Expose
    private List<OnSubTotal> onSubTotal = null;
    @SerializedName("onTotal")
    @Expose
    private List<OnTotal> onTotal = null;

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public boolean isDiscount() {
        return isDiscount;
    }

    public void setDiscount(boolean discount) {
        isDiscount = discount;
    }

    public BigDecimal getIsDeliverySurge() {
        return isDeliverySurge;
    }

    public List<OnSubTotal> getOnSubTotal() {
        return onSubTotal;
    }

    public void setOnSubTotal(List<OnSubTotal> onSubTotal) {
        this.onSubTotal = onSubTotal;
    }

    public List<OnTotal> getOnTotal() {
        return onTotal;
    }

    public void setOnTotal(List<OnTotal> onTotal) {
        this.onTotal = onTotal;
    }

}

