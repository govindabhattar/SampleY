package com.tookancustomer.models.ProductFilters;


import java.io.Serializable;

public class AllowValue implements Serializable{
    public AllowValue(String displayName, boolean isChecked) {
        this.displayName = displayName;
        this.isChecked = isChecked;
    }

    public AllowValue(String displayName, boolean isChecked, double cost) {
        this.displayName = displayName;
        this.isChecked = isChecked;
        this.cost = cost;
    }

    private String displayName;
    private boolean isChecked;
    private double cost;

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    private String currencySymbol;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}