package com.tookancustomer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaytmResponse {

    @SerializedName("BANKNAME")
    @Expose
    private String bankname;
    @SerializedName("BANKTXNID")
    @Expose
    private String banktxnid;
    @SerializedName("CHECKSUMHASH")
    @Expose
    private String checksumhash;
    @SerializedName("CURRENCY")
    @Expose
    private String currency;
    @SerializedName("GATEWAYNAME")
    @Expose
    private String gatewayname;
    @SerializedName("MID")
    @Expose
    private String mid;
    @SerializedName("ORDERID")
    @Expose
    private String orderid;
    @SerializedName("PAYMENTMODE")
    @Expose
    private String paymentmode;
    @SerializedName("RESPCODE")
    @Expose
    private String respcode;
    @SerializedName("RESPMSG")
    @Expose
    private String respmsg;
    @SerializedName("STATUS")
    @Expose
    private String status;
    @SerializedName("TXNAMOUNT")
    @Expose
    private String txnamount;
    @SerializedName("TXNDATE")
    @Expose
    private String txndate;
    @SerializedName("TXNID")
    @Expose
    private String txnid;

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getBanktxnid() {
        return banktxnid;
    }

    public void setBanktxnid(String banktxnid) {
        this.banktxnid = banktxnid;
    }

    public String getChecksumhash() {
        return checksumhash;
    }

    public void setChecksumhash(String checksumhash) {
        this.checksumhash = checksumhash;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getGatewayname() {
        return gatewayname;
    }

    public void setGatewayname(String gatewayname) {
        this.gatewayname = gatewayname;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getPaymentmode() {
        return paymentmode;
    }

    public void setPaymentmode(String paymentmode) {
        this.paymentmode = paymentmode;
    }

    public String getRespcode() {
        return respcode;
    }

    public void setRespcode(String respcode) {
        this.respcode = respcode;
    }

    public String getRespmsg() {
        return respmsg;
    }

    public void setRespmsg(String respmsg) {
        this.respmsg = respmsg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTxnamount() {
        return txnamount;
    }

    public void setTxnamount(String txnamount) {
        this.txnamount = txnamount;
    }

    public String getTxndate() {
        return txndate;
    }

    public void setTxndate(String txndate) {
        this.txndate = txndate;
    }

    public String getTxnid() {
        return txnid;
    }

    public void setTxnid(String txnid) {
        this.txnid = txnid;
    }

}
