package com.tookancustomer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.models.userdata.PaymentSettings;
import com.tookancustomer.utility.Utils;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by cl-macmini-83 on 10/10/16.
 */

public class TaxesModel implements Serializable {
    private boolean isDiscount = false;
    private boolean showPercentage = false;
    private String description = "";
    @SerializedName("tax_name")
    @Expose
    private String taxName;
    @SerializedName("tax_percentage")
    @Expose
    private double taxPercentage;
    @SerializedName("tax_amount")
    @Expose
    private BigDecimal taxAmount;
    private int taxDiscount;
    @SerializedName("tax_type")
    @Expose
    private int taxType; //0 for percentage and 1 for fixed amount

    public int getTaxAppliedOn() {
        return taxAppliedOn;
    }

    @SerializedName("tax_applied_on")
    @Expose
    private int taxAppliedOn; //0 for percentage and 1 for fixed amount

    private boolean isSurge;



    private BigDecimal isDeliverySurge;

    public BigDecimal getIsDeliverySurge() {
        return isDeliverySurge;
    }

    public void setIsDeliverySurge(BigDecimal isDeliverySurge) {
        this.isDeliverySurge = isDeliverySurge;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    private String currencySymbol;

    public TaxesModel(String taxName, BigDecimal taxAmount, BigDecimal isDeliverySurge) {
        setData(taxName, taxAmount, false, "", false, null, isDeliverySurge);
    }

    public TaxesModel(String taxName, BigDecimal taxAmount, String description, BigDecimal isDeliverySurge) {
        setData(taxName, taxAmount, false, description, false, null, isDeliverySurge);
    }

    public TaxesModel(String taxName, BigDecimal taxAmount, String description, boolean isSurge,
                      String currencySymbol, BigDecimal isDeliverySurge) {
        setData(taxName, taxAmount, false, description, isSurge, currencySymbol, isDeliverySurge);
    }

    public TaxesModel(String taxName, BigDecimal taxAmount, boolean isDiscount, BigDecimal isDeliverySurge) {
        setData(taxName, taxAmount, isDiscount, "", false, null, isDeliverySurge);
    }

    public TaxesModel(String taxName, BigDecimal taxAmount, boolean isDiscount, String description, BigDecimal isDeliverySurge) {
        setData(taxName, taxAmount, isDiscount, description, false, null, isDeliverySurge);
    }

    public TaxesModel(String taxName, BigDecimal taxAmount, boolean isDiscount, String description, String currencySymbol, BigDecimal isDeliverySurge) {
        setData(taxName, taxAmount, isDiscount, description, false, currencySymbol, isDeliverySurge);
    }

    public TaxesModel(String taxName, BigDecimal taxAmount, PaymentSettings paymentSettings, BigDecimal isDeliverySurge) {
        setData(taxName, taxAmount, false, "", false, paymentSettings.getSymbol(), isDeliverySurge);
    }


    private void setData(String taxName, BigDecimal taxAmount, boolean isDiscount, String description, boolean surge, String currencySymbol, BigDecimal isDeliverySurge) {
        this.taxName = taxName;
        this.taxAmount = taxAmount;
        this.isDiscount = isDiscount;
        this.description = description;
        this.taxPercentage = 0.0;
        this.taxType = 1;
        this.isSurge = surge;
        this.currencySymbol = currencySymbol;
        this.isDeliverySurge = isDeliverySurge;
    }

    public String getCurrencySymbol() {
        return (currencySymbol != null && !currencySymbol.isEmpty()) ? currencySymbol : Utils.getCurrencySymbol();
    }


    public boolean isSurge() {
        return isSurge;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public double getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(Double taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public String getTaxAmount() {
        return taxAmount != null ? Utils.getDoubleTwoDigits(taxAmount) : "0";
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public boolean isDiscount() {
        return isDiscount;
    }

    public void setDiscount(boolean discount) {
        isDiscount = discount;
    }

    public int getTaxType() {
        return taxType;
    }

    public void setTaxType(int taxType) {
        this.taxType = taxType;
    }

    public int getTaxDiscount() {
        return taxDiscount;
    }

    public void setTaxDiscount(int taxDiscount) {
        this.taxDiscount = taxDiscount;
    }

    public String getDescription() {
        return description != null ? description : "";
    }
}