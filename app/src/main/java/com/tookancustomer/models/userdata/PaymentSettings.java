
package com.tookancustomer.models.userdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.utility.Utils;

import java.io.Serializable;
import java.math.BigDecimal;

public class PaymentSettings implements Serializable {


    @SerializedName("currency_id")
    @Expose
    private int currencyId;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("allowed_payment_methods")
    @Expose
    private int allowedPaymentMethods;

    @SerializedName("minimum_amount")
    @Expose
    private BigDecimal minimumAmount;


    @SerializedName("is_zero_decimal_currency")
    @Expose
    private int is_zeroDecimalCurrency;

    @SerializedName("gateway_mapped_payment_method")
    @Expose
    private long gatewayMappedPaymentMethod;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getSymbol() {
        return (symbol != null && !symbol.isEmpty()) ? symbol : Utils.getCurrencySymbol();
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAllowedPaymentMethods() {
        return allowedPaymentMethods;
    }

    public void setAllowedPaymentMethods(int allowedPaymentMethods) {
        this.allowedPaymentMethods = allowedPaymentMethods;
    }

    public BigDecimal getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(BigDecimal minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    public int getIs_zeroDecimalCurrency() {
        return is_zeroDecimalCurrency;
    }

    public void setIs_zeroDecimalCurrency(int is_zeroDecimalCurrency) {
        this.is_zeroDecimalCurrency = is_zeroDecimalCurrency;
    }

    public long getGatewayMappedPaymentMethod() {
        return gatewayMappedPaymentMethod;
    }

    public void setGatewayMappedPaymentMethod(int gatewayMappedPaymentMethod) {
        this.gatewayMappedPaymentMethod = gatewayMappedPaymentMethod;
    }

}