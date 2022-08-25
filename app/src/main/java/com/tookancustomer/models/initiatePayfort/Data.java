
package com.tookancustomer.models.initiatePayfort;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("response_code")
    @Expose
    private String responseCode;
    @SerializedName("card_number")
    @Expose
    private String cardNumber;
    @SerializedName("signature")
    @Expose
    private String signature;
    @SerializedName("merchant_identifier")
    @Expose
    private String merchantIdentifier;
    @SerializedName("access_code")
    @Expose
    private String accessCode;
    @SerializedName("payment_option")
    @Expose
    private String paymentOption;
    @SerializedName("expiry_date")
    @Expose
    private String expiryDate;
    @SerializedName("customer_ip")
    @Expose
    private String customerIp;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("eci")
    @Expose
    private String eci;
    @SerializedName("fort_id")
    @Expose
    private String fortId;
    @SerializedName("command")
    @Expose
    private String command;
    @SerializedName("3ds_url")
    @Expose
    private String _3dsUrl;
    @SerializedName("response_message")
    @Expose
    private String responseMessage;
    @SerializedName("merchant_reference")
    @Expose
    private String merchantReference;
    @SerializedName("customer_email")
    @Expose
    private String customerEmail;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("status")
    @Expose
    private String status;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getMerchantIdentifier() {
        return merchantIdentifier;
    }

    public void setMerchantIdentifier(String merchantIdentifier) {
        this.merchantIdentifier = merchantIdentifier;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(String paymentOption) {
        this.paymentOption = paymentOption;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCustomerIp() {
        return customerIp;
    }

    public void setCustomerIp(String customerIp) {
        this.customerIp = customerIp;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getEci() {
        return eci;
    }

    public void setEci(String eci) {
        this.eci = eci;
    }

    public String getFortId() {
        return fortId;
    }

    public void setFortId(String fortId) {
        this.fortId = fortId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String get3dsUrl() {
        return _3dsUrl;
    }

    public void set3dsUrl(String _3dsUrl) {
        this._3dsUrl = _3dsUrl;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getMerchantReference() {
        return merchantReference;
    }

    public void setMerchantReference(String merchantReference) {
        this.merchantReference = merchantReference;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
