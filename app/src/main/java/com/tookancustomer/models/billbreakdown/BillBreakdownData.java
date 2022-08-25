package com.tookancustomer.models.billbreakdown;

import android.text.Spannable;

import com.tookancustomer.utility.Utils;

import java.math.BigDecimal;

public class BillBreakdownData {
    private boolean isSurge;
    private String name = "";
    private String currencySymbol = "";
    private String description = "";
    private BigDecimal isDeliverySurge;
    private BigDecimal amount;



    private boolean isDiscount = false;
    private double taxPercentage = 0;
    private int taxType = 0; //0 for percentage and 1 for fixed amount

    public int getTaxDiscount() {
        return taxDiscount;
    }

    private int taxDiscount = 0;
    public BillBreakdownData(String name, BigDecimal amount, BigDecimal isDeliverySurge) {
        this.name = name;
        this.amount = amount;

        this.isDeliverySurge = isDeliverySurge;
    }



    public BillBreakdownData(String name, BigDecimal amount, boolean isDiscount, double taxPercentage, int taxType) {
        this.name = name;
        this.amount = amount;
        this.isDiscount = isDiscount;
        this.taxPercentage = taxPercentage;
        this.taxType = taxType;
    }
    public BillBreakdownData(String name, BigDecimal amount, boolean isDiscount, double taxPercentage, int taxType,String currencySymbol) {
        this.name = name;
        this.amount = amount;
        this.isDiscount = isDiscount;
        this.taxPercentage = taxPercentage;
        this.taxType = taxType;
        this.currencySymbol = currencySymbol;
    }

    public BillBreakdownData(String name, BigDecimal amount, String description, boolean isSurge,
                             String currencySymbol, BigDecimal isDeliverySurge) {
        this.name = name;
        this.amount = amount;
        this.description = description;
        this.isSurge = isSurge;
        this.currencySymbol = currencySymbol;
        this.isDeliverySurge = isDeliverySurge;
    }

    public BillBreakdownData(String name, BigDecimal amount, boolean isDiscount, String description, String currencySymbol, BigDecimal isDeliverySurge) {
        this.name = name;
        this.amount = amount;
        this.isDiscount = isDiscount;
        this.description = description;
        this.currencySymbol = currencySymbol;
        this.isDeliverySurge = isDeliverySurge;

    }

    public BillBreakdownData(String name, BigDecimal amount, String currencySymbol, BigDecimal isDeliverySurge) {
        this.name = name;
        this.amount = amount;
        this.taxType = 1;
        this.currencySymbol = currencySymbol;
        this.isDeliverySurge = isDeliverySurge;
    }


    //String taxName, BigDecimal taxAmount, String description, boolean isSurge,
    //                      String currencySymbol, BigDecimal isDeliverySurge

    public BillBreakdownData(String name, BigDecimal amount, boolean isDiscount, BigDecimal isDeliverySurge) {
        this.name = name;
        this.amount = amount;
        this.isDiscount = isDiscount;
        this.isDeliverySurge = isDeliverySurge;
    }

    public BillBreakdownData(String name, BigDecimal amount,
                             boolean isDiscount, String description, BigDecimal isDeliverySurge) {
        this.name = name;
        this.amount = amount;
        this.isDiscount = isDiscount;
        this.description = description;
        this.isDeliverySurge = isDeliverySurge;
    }

    public BillBreakdownData(String name, BigDecimal amount, boolean isDiscount, double taxPercentage, int taxType, int taxDiscount) {
        this.name = name;
        this.amount = amount;
        this.isDiscount = isDiscount;
        this.taxPercentage = taxPercentage;
        this.taxType = taxType;
        this.taxDiscount = taxDiscount;
    }


    public boolean isSurge() {
        return isSurge;
    }

    public void setSurge(boolean surge) {
        isSurge = surge;
    }

    public String getDescription() {
        return description != null ? description : "";
    }

    public String getCurrencySymbol() {
        return currencySymbol != null ? currencySymbol : "";
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public BigDecimal getIsDeliverySurge() {
        return isDeliverySurge;
    }
    // private ArrayList<CheckoutTemplate> list;

    public void setIsDeliverySurge(BigDecimal isDeliverySurge) {
        this.isDeliverySurge = isDeliverySurge;
    }


  /*  public ArrayList<CheckoutTemplate> getList() {
        return list;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isDiscount() {
        return isDiscount;
    }

    public void setDiscount(boolean discount) {
        isDiscount = discount;
    }

    public double getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(double taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public int getTaxType() {
        return taxType;
    }

    public void setTaxType(int taxType) {
        this.taxType = taxType;
    }
}

